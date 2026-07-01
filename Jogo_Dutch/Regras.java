package Jogo_Dutch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Regras extends JPanel{

	private JFrame frame;
	
	private final int botaoX = 400;
	private final int botaoY = 650;
	private final int botaoL = 150;
	private final int botaoA = 50;

	
	public static void main(String[] args) {

	Regras regras = new Regras();
	regras.start();
	
	}
	
	
	public Regras() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int cliqueX = e.getX();
				int cliqueY = e.getY();
				
				if (cliqueX >= botaoX && cliqueX <= botaoX + botaoL &&
					cliqueY >= botaoY && cliqueY <= botaoY + botaoA) {
					
					
					if (frame != null) {
						frame.dispose();
					}
					
					Regras2 regras2 = new Regras2();
					regras2.start();

				}
				
				
				
				
				
				if (cliqueX >= botaoX-350 && cliqueX <= botaoX-350 + botaoL &&
						cliqueY >= botaoY && cliqueY <= botaoY + botaoA) {
						
						
						if (frame != null) {
							frame.dispose();
						}
						

						Menu menu = new Menu();
						menu.start();
						
					}
				
				
			
				
				
			}	
		});	
		}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new java.awt.Color(0, 128, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		g.setColor(new java.awt.Color(0, 128, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 20.0f));
		g.drawString("🃏 DUTCH – Regras Rápidas", 40, 50);
		
		g.setColor(Color.RED);
		g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 20.0f));
		g.drawString("🎯 Objetivo", 40, 90);
		
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(java.awt.Font.PLAIN, 20.0f));
		g.drawString("Ter a menor pontuação quando a rodada terminar.", 40, 120);
		
		g.setColor(Color.RED);
		g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 20.0f));
		g.drawString("🔹 Preparação", 40, 170);
		
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(java.awt.Font.PLAIN, 20.0f));
		g.drawString("Cada jogador recebe 4 cartas viradas para baixo.", 40, 200);
		g.drawString("Pode olhar apenas 1 carta no início.", 40, 225);
		g.drawString("O restante forma o monte de compra.", 40, 250);
		
		g.setColor(Color.RED);
		g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 20.0f));
		g.drawString("🔹 Seu Turno", 40, 300);
		
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(java.awt.Font.PLAIN, 20.0f));
		g.drawString("Compre uma carta do monte ou do descarte. Escolha:", 40, 330);
		g.drawString("🔄 Trocar por uma das suas cartas.", 60, 355);
		g.drawString("🗑️ Descartar a carta comprada.", 60, 380);
		g.drawString("Passe a vez.", 40, 405);
		
		g.setColor(Color.RED);
		g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 20.0f));
		g.drawString("🔹 Cartas Especiais", 40, 455);
		
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(java.awt.Font.PLAIN, 18.0f));
		g.drawString("👑 Rei Vermelho (K♦/K♥) = 0 pontos", 40, 485);
		g.drawString("🤴 Valete (J) = Troca de cartas com o adversário (apenas uma carta)", 20, 510);
		
		g.drawString("👸 Dama (Q) = Olhe uma carta do seu próprio baralho", 40, 535);
		
		
		

		g.setColor(Color.RED);
		g.fillRect(botaoX, botaoY, botaoL, botaoA);
		g.setColor(Color.WHITE);
		g.drawRect(botaoX, botaoY, botaoL, botaoA);
		g.setFont(g.getFont().deriveFont(18.0f));

		g.drawString("Mais Regras", botaoX+25, botaoY+30);
		
		
		
		
		g.setColor(Color.RED);
		g.fillRect(botaoX-350, botaoY, botaoL, botaoA);
		g.setColor(Color.WHITE);
		g.drawRect(botaoX-350, botaoY, botaoL, botaoA);
		g.setFont(g.getFont().deriveFont(18.0f));

		g.drawString("Voltar", botaoX-306, botaoY+30);
		
	
	
		
	}
	
	
	
	
	public void createScreen() {
		frame = new JFrame("Jogo Dutch - Regras");
		frame.add(this);
		frame.setSize(600, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		createScreen(); 
	}
	
	
	
}
