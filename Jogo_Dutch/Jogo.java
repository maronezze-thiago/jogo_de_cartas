package com.Jogo_Dutch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Jogo extends JPanel  {
	
	public boolean isRunning = true;
	public Thread thread;
	public JFrame frame;
	
	private Mao maoMaquina;
	private Mao maoJogador;

	public static final int CARD_WIDTH = 48;  
	public static final int CARD_HEIGHT = 64; 
	public static final int SCALE = 4; 
	
	private final int margemX = 40;
	private final int margemY = 140; 
	private final int espacoHorizontal = 100; 
	private final int espacoVertical = 360;   
	
	private final int baralhoX = 1500;
	private final int baralhoY = 450;

	private final int descarteX = 500;
	private final int descarteY = 450;

	private final int tempX = 800;  
	private final int tempY = 450;

	private final int botaoDutchX = 1450;
	private final int botaoDutchY = 250;
	private final int botaoDutchL = 200;
	private final int botaoDutchA = 60;
	
	
	
	private final int botaoResetX = 1450;
	private final int botaoResetY = 850;
	private final int botaoResetL = 200;
	private final int botaoResetA = 60;
	
	
	
	
	private Carta cartaPuxadaDoBaralho = null; 
	private Carta cartaSendoEspiada = null;    
	private long tempoInicioEspiada = 0;       
	private boolean jaEspiouNestaRodada = false;

	private Carta cartaEspiadaRainha = null;
	private long tempoInicioEspiadaRainha = 0;

	private enum EstadoTurno {
		TURNO_JOGADOR,
		ESPIANDO_RAINHA,                 
		MEMORIZANDO_CARTA_NOVA,          
		ESCOLHENDO_MINHA_CARTA_VALETE,   
		ESCOLHENDO_CARTA_OPONENTE_VALETE,
		MEMORIZANDO_VALETE, 
		EXIBINDO_CORTE,                  
		TURNO_MAQUINA,
		FIM_DE_JOGO                      
	}
	private EstadoTurno estadoAtual = EstadoTurno.TURNO_JOGADOR;
	private EstadoTurno estadoAnteriorAoCorte = EstadoTurno.TURNO_JOGADOR; 
	private boolean MaquinaProcessando = false;

	private Carta cartaNovaSendoMemorizada = null; 
	private long tempoInicioMemorizacao = 0;       
	
	private int indiceMinhaCartaValete = -1;
	private int indiceCartaMaquinaValete = -1;
	private long tempoInicioMemorizacaoValete = 0;

	private String mensagemCorte = "";
	private long tempoInicioExibindoCorte = 0;

	private boolean dutchChamado = false;
	private int turnosRestantesPosDutch = -1; 
	private String mensagemFimJogo = "";
	private String mensagemAvisoDutchMaquina = "";
	private long tempoAvisoDutchMaquina = 0;

	private int[] conhecimentoJogador = {-1, -1, -1, -1};
	private int[] memoriaMaquina = {-1, -1, -1, -1};

	private ArrayList<Carta> monteDescarte; 
	private BufferedImage deckSheet;
	private BufferedImage cardBack;
	private Baralho baralho;

	public static void main(String[] args) {
		Jogo jogo = new Jogo();
		jogo.start();
	}
	
	public Jogo() {
		maoMaquina = new Mao();
		maoJogador = new Mao();
		monteDescarte = new ArrayList<>();

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				

				int cliqueX = e.getX();
				int cliqueY = e.getY();
				
				
				
				if(estadoAtual == EstadoTurno.FIM_DE_JOGO){
					if (cliqueX >= botaoResetX && cliqueX <= botaoResetX + botaoResetL &&
							cliqueY >= botaoResetY && cliqueY <= botaoResetY + botaoResetA) {
						System.out.println("teste2");
						Menu menu = new Menu();
						
						menu.setVisible(true);
						
						if(frame != null) {
							
							frame.dispose();
						}
						menu.start();

						
					}
					
					}
					
				
				if (estadoAtual == EstadoTurno.EXIBINDO_CORTE) return;

				
				int larguraCartaVisual = CARD_WIDTH * SCALE;
				int alturaCartaVisual = CARD_HEIGHT * SCALE;

				if (estadoAtual == EstadoTurno.TURNO_JOGADOR && !dutchChamado && cartaPuxadaDoBaralho == null) {
					if (cliqueX >= botaoDutchX && cliqueX <= botaoDutchX + botaoDutchL &&
						cliqueY >= botaoDutchY && cliqueY <= botaoDutchY + botaoDutchA) {
						System.out.println("teste");
						dutchChamado = true;
						turnosRestantesPosDutch = 2; 
						finalizarTurnoJogador();
						return;
					}
				}

				if (estadoAtual != EstadoTurno.TURNO_JOGADOR && 
					estadoAtual != EstadoTurno.ESPIANDO_RAINHA &&
					estadoAtual != EstadoTurno.ESCOLHENDO_MINHA_CARTA_VALETE && 
					estadoAtual != EstadoTurno.ESCOLHENDO_CARTA_OPONENTE_VALETE) {
					return;
				}

				if (estadoAtual == EstadoTurno.TURNO_JOGADOR &&
					cliqueX >= baralhoX && cliqueX <= baralhoX + larguraCartaVisual &&
					cliqueY >= baralhoY && cliqueY <= baralhoY + alturaCartaVisual) {
					
					if (cartaPuxadaDoBaralho == null) {
						if (!baralho.cartas.isEmpty()) {
							cartaPuxadaDoBaralho = baralho.cartas.remove(0);
							if (!cartaPuxadaDoBaralho.estaParaCima()) {
								cartaPuxadaDoBaralho.virarCarta();
							}
						} else {
							calcularVencedor();
						}
					}
					repaint();
					return;
				}

				if (estadoAtual == EstadoTurno.TURNO_JOGADOR && cartaPuxadaDoBaralho != null) {
					if (cliqueX >= tempX && cliqueX <= tempX + larguraCartaVisual &&
						cliqueY >= tempY && cliqueY <= tempY + alturaCartaVisual) {
						
						monteDescarte.add(0, cartaPuxadaDoBaralho);
						
						Carta descartada = cartaPuxadaDoBaralho;
						cartaPuxadaDoBaralho = null; 
						
						if (!verificarCorteAutomaticoGeral()) {
							aplicarEfeitoDescarte(descartada, false);
						}
						return;
					}
				}

				for (int i = 0; i < maoJogador.cartas.size(); i++) {
					int posX = margemX + (i * (larguraCartaVisual + espacoHorizontal));
					int posY = margemY + (1 * (alturaCartaVisual + espacoVertical));

					if (cliqueX >= posX && cliqueX <= posX + larguraCartaVisual &&
						cliqueY >= posY && cliqueY <= posY + alturaCartaVisual) {
						
						if (estadoAtual == EstadoTurno.ESCOLHENDO_MINHA_CARTA_VALETE) {
							indiceMinhaCartaValete = i;
							estadoAtual = EstadoTurno.ESCOLHENDO_CARTA_OPONENTE_VALETE;
							repaint();
							return;
						}
						
						if (estadoAtual == EstadoTurno.ESCOLHENDO_CARTA_OPONENTE_VALETE) {
							return;
						}

						if (estadoAtual == EstadoTurno.ESPIANDO_RAINHA) {
							if (cartaEspiadaRainha == null) {
								Carta cartaClicada = maoJogador.cartas.get(i);
								if (!cartaClicada.estaParaCima()) {
									cartaClicada.virarCarta();
									cartaEspiadaRainha = cartaClicada;
									tempoInicioEspiadaRainha = System.currentTimeMillis();
									conhecimentoJogador[i] = getValorPontuacao(cartaClicada);
								}
							}
							repaint();
							return;
						}

						if (cartaPuxadaDoBaralho != null) {
							Carta cartaAntigaDaMao = maoJogador.cartas.get(i);
							
							if (!cartaAntigaDaMao.estaParaCima()) cartaAntigaDaMao.virarCarta();
							monteDescarte.add(0, cartaAntigaDaMao); 
							
							if (!cartaPuxadaDoBaralho.estaParaCima()) cartaPuxadaDoBaralho.virarCarta();
							maoJogador.cartas.set(i, cartaPuxadaDoBaralho); 
							
							conhecimentoJogador[i] = getValorPontuacao(cartaPuxadaDoBaralho);
							
							cartaNovaSendoMemorizada = cartaPuxadaDoBaralho;
							tempoInicioMemorizacao = System.currentTimeMillis();
							estadoAtual = EstadoTurno.MEMORIZANDO_CARTA_NOVA;
							
							cartaPuxadaDoBaralho = null; 
							
							if (!verificarCorteAutomaticoGeral()) {
								aplicarEfeitoDescarte(cartaAntigaDaMao, false);
							}
						} 
						else {
							if (!jaEspiouNestaRodada) {
								Carta cartaClicada = maoJogador.cartas.get(i);
								if (!cartaClicada.estaParaCima()) {
									cartaClicada.virarCarta(); 
									cartaSendoEspiada = cartaClicada;
									tempoInicioEspiada = System.currentTimeMillis();
									jaEspiouNestaRodada = true; 
									conhecimentoJogador[i] = getValorPontuacao(cartaClicada);
								}
							}
						}
						repaint();
						return;
					}
				}

				for (int i = 0; i < maoMaquina.cartas.size(); i++) {
					int posX = margemX + (i * (larguraCartaVisual + espacoHorizontal));
					int posY = margemY + (0 * (alturaCartaVisual + espacoVertical));

					if (cliqueX >= posX && cliqueX <= posX + larguraCartaVisual &&
						cliqueY >= posY && cliqueY <= posY + alturaCartaVisual) {
						
						if (estadoAtual == EstadoTurno.ESCOLHENDO_CARTA_OPONENTE_VALETE) {
							indiceCartaMaquinaValete = i;
							
							Carta minhaCarta = maoJogador.cartas.get(indiceMinhaCartaValete);
							Carta cartaMaquina = maoMaquina.cartas.get(i);
							
							maoJogador.cartas.set(indiceMinhaCartaValete, cartaMaquina);
							maoMaquina.cartas.set(i, minhaCarta);
							
							if (!maoJogador.cartas.get(indiceMinhaCartaValete).estaParaCima()) {
								maoJogador.cartas.get(indiceMinhaCartaValete).virarCarta();
							}
							if (!maoMaquina.cartas.get(i).estaParaCima()) {
								maoMaquina.cartas.get(i).virarCarta();
							}
							
							conhecimentoJogador[indiceMinhaCartaValete] = getValorPontuacao(cartaMaquina);
							memoriaMaquina[i] = getValorPontuacao(minhaCarta); 
							
							
							tempoInicioMemorizacaoValete = System.currentTimeMillis();
							estadoAtual = EstadoTurno.MEMORIZANDO_VALETE;
							repaint();
							return;
						}
						return;
					}
				}
		

				//--------------------------------------------------------------------------------------------------------------------------------------------------------
				
				
				
			}
			
		});

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setPreferredSize(screenSize);
		    
		try {
			deckSheet = ImageIO.read(getClass().getResourceAsStream("/DeckSheet.png"));
			cardBack = ImageIO.read(getClass().getResourceAsStream("/Flipped.png"));
		} catch (IOException e) {
			System.out.println("Erro ao carregar imagens.");
			e.printStackTrace();
		}

		baralho = new Baralho();
		baralho.popular();
		baralho.embaralhar();
		
		for (int i = 0; i < 4; i++) {
			maoMaquina.adicionar(baralho.cartas.remove(0));
			maoJogador.adicionar(baralho.cartas.remove(0));
		}
	}

	private int getValorPontuacao(Carta carta) {
		if (carta.getNumero() == 13) {
			String naipe = carta.getNaipe().toLowerCase();
			if (naipe.equals("copas") || naipe.equals("ouros")) {
				return 0; // rei vermelho vale zero
			}
		}
		return carta.getNumero();
	}

	private boolean verificarCorteAutomaticoGeral() {
		if (monteDescarte.isEmpty()) return false;
		Carta topo = monteDescarte.get(0);
		int numeroTopo = topo.getNumero();

		limparCartasVisiveisTemporarias();

		for (int i = 0; i < maoJogador.cartas.size(); i++) {
			if (i < conhecimentoJogador.length && conhecimentoJogador[i] == getValorPontuacao(topo)) {
				if (maoJogador.cartas.get(i).getNumero() == numeroTopo) {
					Carta removida = maoJogador.cartas.remove(i);
					if (!removida.estaParaCima()) removida.virarCarta();
					monteDescarte.add(0, removida);

					for (int j = i; j < conhecimentoJogador.length - 1; j++) {
						conhecimentoJogador[j] = conhecimentoJogador[j + 1];
					}
					conhecimentoJogador[conhecimentoJogador.length - 1] = -1;

					mensagemCorte = "CORTE AUTOMÁTICO! Você já conhecia a carta e descartou da mão!";
					acionarEstadoCorte();
					return true;
				}
			}
		}

		for (int i = 0; i < maoMaquina.cartas.size(); i++) {
			if (i < memoriaMaquina.length && memoriaMaquina[i] == getValorPontuacao(topo)) {
				if (maoMaquina.cartas.get(i).getNumero() == numeroTopo) {
					Carta removida = maoMaquina.cartas.remove(i);
					if (!removida.estaParaCima()) removida.virarCarta();
					monteDescarte.add(0, removida);

					for (int j = i; j < memoriaMaquina.length - 1; j++) {
						memoriaMaquina[j] = memoriaMaquina[j + 1];
					}
					memoriaMaquina[memoriaMaquina.length - 1] = -1;

					mensagemCorte = "CORTE AUTOMÁTICO! A Máquina já conhecia e descartou!";
					acionarEstadoCorte();
					return true;
				}
			}
		}
		return false;
	}

	private void limparCartasVisiveisTemporarias() {
		if (cartaSendoEspiada != null) {
			if (cartaSendoEspiada.estaParaCima()) cartaSendoEspiada.virarCarta();
			cartaSendoEspiada = null;
		}
		if (cartaEspiadaRainha != null) {
			if (cartaEspiadaRainha.estaParaCima()) cartaEspiadaRainha.virarCarta();
			cartaEspiadaRainha = null;
		}
		if (cartaNovaSendoMemorizada != null) {
			if (cartaNovaSendoMemorizada.estaParaCima()) cartaNovaSendoMemorizada.virarCarta();
			cartaNovaSendoMemorizada = null;
		}
	}

	private void acionarEstadoCorte() {
		if (estadoAtual != EstadoTurno.EXIBINDO_CORTE) {
			estadoAnteriorAoCorte = estadoAtual;
		}
		estadoAtual = EstadoTurno.EXIBINDO_CORTE;
		tempoInicioExibindoCorte = System.currentTimeMillis();
		repaint();
	}

	private void aplicarEfeitoDescarte(Carta cartaDescartada, boolean eDaMaquina) {
		int numeroCarta = cartaDescartada.getNumero();

		if (numeroCarta == 11) { // VALETE
			if (!eDaMaquina) {
				estadoAtual = EstadoTurno.ESCOLHENDO_MINHA_CARTA_VALETE;
			} else {
				int idxM = -1; int maiorValor = -1;
				for (int i = 0; i < memoriaMaquina.length; i++) {
					if (memoriaMaquina[i] > maiorValor) { maiorValor = memoriaMaquina[i]; idxM = i; }
				}
				if (idxM == -1) idxM = (int) (Math.random() * maoMaquina.cartas.size());
				int idxJ = (int) (Math.random() * maoJogador.cartas.size());
				
				if (idxM < maoMaquina.cartas.size() && idxJ < maoJogador.cartas.size()) {
					Carta cM = maoMaquina.cartas.get(idxM);
					Carta cJ = maoJogador.cartas.get(idxJ);
					
					maoMaquina.cartas.set(idxM, cJ);
					maoJogador.cartas.set(idxJ, cM);
					
					conhecimentoJogador[idxJ] = getValorPontuacao(cM);
					memoriaMaquina[idxM] = -1;
					
					if (!maoJogador.cartas.get(idxJ).estaParaCima()) maoJogador.cartas.get(idxJ).virarCarta();
					if (!maoMaquina.cartas.get(idxM).estaParaCima()) maoMaquina.cartas.get(idxM).virarCarta();
					
					indiceMinhaCartaValete = idxJ;
					indiceCartaMaquinaValete = idxM;
					
					tempoInicioMemorizacaoValete = System.currentTimeMillis();
					estadoAtual = EstadoTurno.MEMORIZANDO_VALETE;
				}
			}
			repaint();
		} 
		else if (numeroCarta == 12) { // RAINHA
			if (!eDaMaquina) {
				if (estadoAtual == EstadoTurno.MEMORIZANDO_CARTA_NOVA && cartaNovaSendoMemorizada != null) {
					cartaEspiadaRainha = cartaNovaSendoMemorizada;
					tempoInicioEspiadaRainha = tempoInicioMemorizacao; 
					estadoAtual = EstadoTurno.ESPIANDO_RAINHA;
				} else {
					estadoAtual = EstadoTurno.ESPIANDO_RAINHA; 
				}
			} else {
				for (int i = 0; i < memoriaMaquina.length; i++) {
					if (memoriaMaquina[i] == -1 && i < maoMaquina.cartas.size()) {
						memoriaMaquina[i] = getValorPontuacao(maoMaquina.cartas.get(i));
						break;
					}
				}
			}
			repaint();
		} else {
			if (!eDaMaquina) {
				finalizarTurnoJogador();
			}
		}
	}

	private void finalizarTurnoJogador() {
		if (dutchChamado) {
			turnosRestantesPosDutch--;
			if (turnosRestantesPosDutch <= 0) {
				calcularVencedor();
				return;
			}
		}

		jaEspiouNestaRodada = false;
		estadoAtual = EstadoTurno.TURNO_MAQUINA;
		repaint();
	}

	private void finalizarTurnoMaquina() {
		if (dutchChamado) {
			turnosRestantesPosDutch--;
			if (turnosRestantesPosDutch <= 0) {
				calcularVencedor();
				return;
			}
		}
		
		System.out.println("--- SEU TURNO ---");
		MaquinaProcessando = false; 
		estadoAtual = EstadoTurno.TURNO_JOGADOR;
		repaint();
	}

	private void calcularVencedor() {
		estadoAtual = EstadoTurno.FIM_DE_JOGO;
		MaquinaProcessando = false;
		
		for (Carta c : maoJogador.cartas) {
			if (!c.estaParaCima()) c.virarCarta();
		}
		for (Carta c : maoMaquina.cartas) {
			if (!c.estaParaCima()) c.virarCarta();
		}

		int pontosJogador = 0;
		for (Carta c : maoJogador.cartas) pontosJogador += getValorPontuacao(c);

		int pontosMaquina = 0;
		for (Carta c : maoMaquina.cartas) pontosMaquina += getValorPontuacao(c);

		if (pontosJogador < pontosMaquina) {
			mensagemFimJogo = "FIM DE JOGO: VOCÊ VENCEU! Pontos Jogador: " + pontosJogador + " | Pontos Máquina: " + pontosMaquina;
		} else if (pontosMaquina < pontosJogador) {
			mensagemFimJogo = "FIM DE JOGO: A MÁQUINA VENCEU! Pontos Jogador: " + pontosJogador + " | Pontos Máquina: " + pontosMaquina;
		} else {
			mensagemFimJogo = "FIM DE JOGO: EMPATE! Ambos com " + pontosJogador + " pontos!";
		}
		System.out.println(mensagemFimJogo);
		repaint();
	}

	private void EscondeTodasCartasDaMaquina() {
		if (estadoAtual == EstadoTurno.FIM_DE_JOGO) return;
		for (Carta c : maoMaquina.cartas) {
			if (c.estaParaCima()) c.virarCarta();
		}
	}

	private void executarJogadaMaquina() {
		try {
			if (estadoAtual == EstadoTurno.FIM_DE_JOGO) return;

			int pontosAtuaisMaquina = 0;
			for (Carta c : maoMaquina.cartas) {
				pontosAtuaisMaquina += getValorPontuacao(c);
			}

			if (!dutchChamado && pontosAtuaisMaquina <= 5) {
				dutchChamado = true;
				turnosRestantesPosDutch = 2; 
				mensagemAvisoDutchMaquina = " A MÁQUINA CHAMOU DUTCH!";
				tempoAvisoDutchMaquina = System.currentTimeMillis();
				repaint();
				Thread.sleep(2000); 
			}

			Thread.sleep(1000); 

			int idxEspiar = -1;
			for (int i = 0; i < memoriaMaquina.length; i++) {
				if (memoriaMaquina[i] == -1) { idxEspiar = i; break; }
			}
			if (idxEspiar == -1) idxEspiar = (int) (Math.random() * maoMaquina.cartas.size());
			if (idxEspiar < maoMaquina.cartas.size()) {
				memoriaMaquina[idxEspiar] = getValorPontuacao(maoMaquina.cartas.get(idxEspiar));
			}
			Thread.sleep(1500); 

			if (!baralho.cartas.isEmpty()) {
				Carta cartaPuxada = baralho.cartas.remove(0);
				int valorPuxado = getValorPontuacao(cartaPuxada); 

				int idxMaior = -1; int maiorVal = -1;
				for (int i = 0; i < memoriaMaquina.length; i++) {
					if (memoriaMaquina[i] != -1 && memoriaMaquina[i] > maiorVal) { maiorVal = memoriaMaquina[i]; idxMaior = i; }
				}

				if (cartaPuxada.getNumero() == 13 && valorPuxado == 13) { // Se for Rei Preto, a IA rejeita e descarta direto
					if (!cartaPuxada.estaParaCima()) cartaPuxada.virarCarta();
					monteDescarte.add(0, cartaPuxada);
					if (!verificarCorteAutomaticoGeral()) {
						aplicarEfeitoDescarte(cartaPuxada, true);
					}
				} 
				else if (cartaPuxada.getNumero() == 13 && valorPuxado == 0) {
					int alvo = (idxMaior != -1) ? idxMaior : (int)(Math.random() * maoMaquina.cartas.size());
					if (alvo >= maoMaquina.cartas.size()) alvo = 0;
					
					Carta antiga = maoMaquina.cartas.get(alvo);
					if (!antiga.estaParaCima()) antiga.virarCarta();
					monteDescarte.add(0, antiga);
					
					if (cartaPuxada.estaParaCima()) cartaPuxada.virarCarta();
					maoMaquina.cartas.set(alvo, cartaPuxada);
					memoriaMaquina[alvo] = valorPuxado;
					
					if (!verificarCorteAutomaticoGeral()) {
						aplicarEfeitoDescarte(antiga, true);
					}
				}
				else if (cartaPuxada.getNumero() == 11 || cartaPuxada.getNumero() == 12 || (idxMaior != -1 && maiorVal > valorPuxado)) {
					int alvo = (idxMaior != -1) ? idxMaior : (int)(Math.random() * maoMaquina.cartas.size());
					if (alvo >= maoMaquina.cartas.size()) alvo = 0;
					
					Carta antiga = maoMaquina.cartas.get(alvo);
					if (!antiga.estaParaCima()) antiga.virarCarta();
					monteDescarte.add(0, antiga);
					
					if (cartaPuxada.estaParaCima()) cartaPuxada.virarCarta();
					maoMaquina.cartas.set(alvo, cartaPuxada);
					memoriaMaquina[alvo] = valorPuxado;
					
					if (!verificarCorteAutomaticoGeral()) {
						aplicarEfeitoDescarte(antiga, true);
					}
				} else {
					int idxDesc = -1;
					for (int i = 0; i < memoriaMaquina.length; i++) {
						if (memoriaMaquina[i] == -1) { idxDesc = i; break; }
					}
					if (valorPuxado <= 5 && idxDesc != -1 && idxDesc < maoMaquina.cartas.size()) {
						Carta antiga = maoMaquina.cartas.get(idxDesc);
						if (!antiga.estaParaCima()) antiga.virarCarta();
						monteDescarte.add(0, antiga);
						
						if (cartaPuxada.estaParaCima()) cartaPuxada.virarCarta();
						maoMaquina.cartas.set(idxDesc, cartaPuxada);
						memoriaMaquina[idxDesc] = valorPuxado;
						
						if (!verificarCorteAutomaticoGeral()) {
							aplicarEfeitoDescarte(antiga, true);
						}
					} else {
						if (!cartaPuxada.estaParaCima()) cartaPuxada.virarCarta();
						monteDescarte.add(0, cartaPuxada);
						
						if (!verificarCorteAutomaticoGeral()) {
							aplicarEfeitoDescarte(cartaPuxada, true);
						}
					}
				}
			}

			while (estadoAtual == EstadoTurno.MEMORIZANDO_VALETE) {
				Thread.sleep(50);
			}

			Thread.sleep(1000);
			EscondeTodasCartasDaMaquina(); 
			finalizarTurnoMaquina();

		} catch (InterruptedException e) {
			e.printStackTrace();
			MaquinaProcessando = false;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new java.awt.Color(0, 128, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		int larguraCartaVisual = CARD_WIDTH * SCALE;
		int alturaCartaVisual = CARD_HEIGHT * SCALE;
		
		
		

	
		if(estadoAtual == EstadoTurno.FIM_DE_JOGO){
			g.setColor(Color.RED);
			g.fillRect(botaoResetX, botaoResetY, botaoResetL, botaoResetA);
			g.setColor(Color.WHITE);
			
			g.drawRect(botaoResetX, botaoResetY, botaoResetL, botaoResetA);
			g.drawString("Recomecar", botaoResetX+30, botaoResetY+35);
		}
		

		if (!dutchChamado && estadoAtual == EstadoTurno.TURNO_JOGADOR && cartaPuxadaDoBaralho == null) {
			g.setColor(Color.RED);
			g.fillRect(botaoDutchX, botaoDutchY, botaoDutchL, botaoDutchA); // desenha o contorno
			g.setColor(Color.WHITE);
			g.drawRect(botaoDutchX, botaoDutchY, botaoDutchL, botaoDutchA);
			g.drawString("CHAMAR DUTCH", botaoDutchX + 30, botaoDutchY + 35);
		}

		if (!baralho.cartas.isEmpty()) {
			if (cardBack != null) g.drawImage(cardBack, baralhoX, baralhoY, larguraCartaVisual, alturaCartaVisual, null);
			g.setColor(java.awt.Color.WHITE);
			g.drawString("Baralho: " + baralho.cartas.size(), baralhoX, baralhoY + alturaCartaVisual + 15);
		}

		if (!monteDescarte.isEmpty()) {
			desenharCarta(g, monteDescarte.get(0), descarteX, descarteY);
		}

		if (cartaPuxadaDoBaralho != null) {
			desenharCarta(g, cartaPuxadaDoBaralho, tempX, tempY);
		}

		for (int i = 0; i < maoMaquina.cartas.size(); i++) {
			desenharCarta(g, maoMaquina.cartas.get(i), margemX + (i * (larguraCartaVisual + espacoHorizontal)), margemY);
		}

		for (int i = 0; i < maoJogador.cartas.size(); i++) {
			desenharCarta(g, maoJogador.cartas.get(i), margemX + (i * (larguraCartaVisual + espacoHorizontal)), margemY + (1 * (alturaCartaVisual + espacoVertical)));
		}
		
		g.setColor(java.awt.Color.YELLOW);
		g.setFont(g.getFont().deriveFont(18.0f));
		
		if (dutchChamado && System.currentTimeMillis() - tempoAvisoDutchMaquina < 2000 && !mensagemAvisoDutchMaquina.isEmpty()) {
			g.setColor(Color.ORANGE);
			g.setFont(g.getFont().deriveFont(24.0f));
			g.drawString(mensagemAvisoDutchMaquina, 50, 80);
		}

		if (estadoAtual == EstadoTurno.FIM_DE_JOGO) {
			g.setColor(Color.CYAN);
			g.setFont(g.getFont().deriveFont(26.0f));
			g.drawString(mensagemFimJogo, 50, 40);
		} else if (estadoAtual == EstadoTurno.EXIBINDO_CORTE) {
			g.setColor(java.awt.Color.ORANGE);
			g.setFont(g.getFont().deriveFont(24.0f));
			g.drawString( mensagemCorte, 50, 40);
		} else if (estadoAtual == EstadoTurno.MEMORIZANDO_VALETE) {
			g.setColor(java.awt.Color.LIGHT_GRAY);
			g.drawString("[VALETE] Cartas trocadas expostas por 2 segundos!", 50, 40);
		} else if (estadoAtual == EstadoTurno.TURNO_JOGADOR) {
			String prefixo = dutchChamado ? "[ÚLTIMA RODADA] " : "";
			if (cartaPuxadaDoBaralho == null) {
				if (!jaEspiouNestaRodada) {
					g.drawString(prefixo + "SEU TURNO: Clique em uma carta sua para ESPIAR ou compre do Baralho.", 50, 40);
				} else {
					g.drawString(prefixo + "SEU TURNO: Espiada realizada! Agora clique no BARALHO para comprar.", 50, 40);
				}
			} else {
				g.drawString(prefixo + "CARTA PUXADA: Clique para TROCAR com a sua mão ou clique no meio para DESCARTÁ-LA.", 50, 40);
			}
		} else if (estadoAtual == EstadoTurno.TURNO_MAQUINA) {
			g.drawString("TURNO DA MAQUINA: A máquina está pensando...", 50, 40);
		}
	}

	private void desenharCarta(Graphics g, Carta carta, int x, int y) {
		int larguraVisual = CARD_WIDTH * SCALE;
		int alturaVisual = CARD_HEIGHT * SCALE;
		if (carta.estaParaCima()) {
			int srcX = carta.getColuna() * CARD_WIDTH;
			int srcY = carta.getLinha() * CARD_HEIGHT;
			g.drawImage(deckSheet, x, y, x + larguraVisual, y + alturaVisual, srcX, srcY, srcX + CARD_WIDTH, srcY + CARD_HEIGHT, null);
		} else {
			if (cardBack != null) g.drawImage(cardBack, x, y, larguraVisual, alturaVisual, null);
		}
	}

	public synchronized void start() {
		createScreen(); 
		isRunning = true;
		thread = new Thread(() -> {
			while (isRunning) {
				
				if (estadoAtual == EstadoTurno.EXIBINDO_CORTE) {
					long tempoAtual = System.currentTimeMillis();
					if (tempoAtual - tempoInicioExibindoCorte >= 2000) {
						limparCartasVisiveisTemporarias();
						
						if (!verificarCorteAutomaticoGeral()) {
							if (estadoAnteriorAoCorte == EstadoTurno.TURNO_JOGADOR) {
								estadoAtual = EstadoTurno.TURNO_JOGADOR;
								finalizarTurnoJogador();
							} else if (estadoAnteriorAoCorte == EstadoTurno.TURNO_MAQUINA) {
								MaquinaProcessando = false; 
								estadoAtual = EstadoTurno.TURNO_MAQUINA;
							} else {
								estadoAtual = EstadoTurno.TURNO_JOGADOR;
							}
						}
					}
				}

				if (cartaSendoEspiada != null) {
					long tempoAtual = System.currentTimeMillis();
					if (tempoAtual - tempoInicioEspiada >= 2000) {
						if (cartaSendoEspiada.estaParaCima()) cartaSendoEspiada.virarCarta(); 
						cartaSendoEspiada = null;     
					}
				}

				if (estadoAtual == EstadoTurno.ESPIANDO_RAINHA && cartaEspiadaRainha != null) {
					long tempoAtual = System.currentTimeMillis();
					if (tempoAtual - tempoInicioEspiadaRainha >= 2000) {
						if (cartaEspiadaRainha.estaParaCima()) {
							cartaEspiadaRainha.virarCarta();
						}
						cartaEspiadaRainha = null;
						cartaNovaSendoMemorizada = null; 
						estadoAtual = EstadoTurno.TURNO_JOGADOR; 
						finalizarTurnoJogador();
					}
				}
				
				if (estadoAtual == EstadoTurno.MEMORIZANDO_CARTA_NOVA && cartaNovaSendoMemorizada != null) {
					long tempoAtual = System.currentTimeMillis();
					if (tempoAtual - tempoInicioMemorizacao >= 2000) {
						if (cartaNovaSendoMemorizada.estaParaCima()) cartaNovaSendoMemorizada.virarCarta(); 
						cartaNovaSendoMemorizada = null;
						estadoAtual = EstadoTurno.TURNO_JOGADOR;
						finalizarTurnoJogador();
					}
				}

				if (estadoAtual == EstadoTurno.MEMORIZANDO_VALETE) {
					long tempoAtual = System.currentTimeMillis();
					if (tempoAtual - tempoInicioMemorizacaoValete >= 2000) {
						if (indiceMinhaCartaValete >= 0 && indiceMinhaCartaValete < maoJogador.cartas.size()) {
							if (maoJogador.cartas.get(indiceMinhaCartaValete).estaParaCima()) {
								maoJogador.cartas.get(indiceMinhaCartaValete).virarCarta();
							}
						}
						if (indiceCartaMaquinaValete >= 0 && indiceCartaMaquinaValete < maoMaquina.cartas.size()) {
							if (maoMaquina.cartas.get(indiceCartaMaquinaValete).estaParaCima()) {
								maoMaquina.cartas.get(indiceCartaMaquinaValete).virarCarta();
							}
						}
						
						indiceMinhaCartaValete = -1;
						indiceCartaMaquinaValete = -1;
						
						if (MaquinaProcessando) {
							estadoAtual = EstadoTurno.TURNO_MAQUINA;
						} else {
							estadoAtual = EstadoTurno.TURNO_JOGADOR; 
							finalizarTurnoJogador();
						}
					}
				}
				
				if (estadoAtual == EstadoTurno.TURNO_MAQUINA && !MaquinaProcessando) {
					MaquinaProcessando = true;
					new Thread(() -> executarJogadaMaquina()).start();
				}
				
				repaint(); 
				try { Thread.sleep(30); } catch (InterruptedException e) { e.printStackTrace(); }
			}
		});
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
	}

	public void createScreen() {
		frame = new JFrame("Jogo Dutch");
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
	
	
	
	

	
	
	
}