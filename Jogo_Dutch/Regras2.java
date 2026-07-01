package Jogo_Dutch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Regras2 extends JPanel {

	private JFrame frame;

	private final int botaoX = 400;
	private final int botaoY = 650;
	private final int botaoL = 150;
	private final int botaoA = 50;
	
	
	public Regras2() {
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
				
					Regras regras = new Regras();
					regras.start();

				}
				
				
			
				
				
			}	
		});	
		}
	
	
	
	
	public static void main(String[] args) {

		Regras2 regras2 = new Regras2();
		regras2.start();
		
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(new java.awt.Color(0, 128, 0));
			g.fillRect(0, 0, getWidth(), getHeight());
			
			
			g.setColor(Color.RED);
			g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 30.0f));
			g.drawString("🔹 Pontuação", 40, 90);
			
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(java.awt.Font.PLAIN, 18.0f));
			g.drawString("🅰️ Ás = 1 ponto  |  🔢 2-10 = valor da carta", 40, 120);
			g.drawString("🤴 J = 11 pontos  |  👸 Q = 12 pontos  |  👑 Rei Preto = 13 pontos", 20, 140);
			
			g.setColor(Color.RED);
			g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 20.0f));
			g.drawString("🔹 Dutch!", 40, 180);
			
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(java.awt.Font.PLAIN, 20.0f));
			g.drawString("Quando achar que tem a menor pontuação, declare \"Dutch!\".", 40, 200);
			g.drawString("Todos os outros jogadores fazem mais uma rodada.", 40, 220);
			g.drawString("Depois, as cartas são reveladas.", 40, 240);
			g.drawString("🏆 Vitória: Quem tiver a menor pontuação vence.", 40, 260);
			
			g.setColor(new java.awt.Color(0, 0, 102)); 
			g.setFont(g.getFont().deriveFont(java.awt.Font.ITALIC, 20.0f));
			g.drawString("💡 Dica: Memorize suas cartas, ", 60, 320);
			g.drawString("use os poderes do J e da Q para obter informações", 85, 340);			
			g.drawString("e tente ficar com o Rei Vermelho (0 pontos).", 85, 360);
			
			
			
			
			
			g.setColor(Color.RED);
			g.fillRect(botaoX, botaoY, botaoL, botaoA);
			g.setColor(Color.WHITE);
			g.drawRect(botaoX, botaoY, botaoL, botaoA);
			g.setFont(g.getFont().deriveFont(18.0f));

			g.drawString("Voltar", botaoX+45, botaoY+30);
		}
	
	
	public void createScreen() {
		frame = new JFrame("Jogo Dutch - Mais Regras");
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
