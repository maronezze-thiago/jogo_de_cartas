package com.Jogo_Dutch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Menu extends JPanel {

	private JFrame frame;
	
	private final int botaoX = 145;
	private final int botaoY = 350;
	private final int botaoL = 200;
	private final int botaoA = 50;

	public static void main(String[] args) {
	    Menu menu = new Menu();
		menu.start();
	}
	
	public Menu() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int cliqueX = e.getX();
				int cliqueY = e.getY();
				
				if (cliqueX >= botaoX && cliqueX <= botaoX + botaoL &&
					cliqueY >= botaoY && cliqueY <= botaoY + botaoA) {
					
					Jogo jogo = new Jogo();
					
					if (frame != null) {
						frame.dispose();
					}
					
					jogo.start();
				}
				
				
				
				
				if(cliqueX >= botaoX && cliqueX <= botaoX + botaoL &&
						cliqueY >= botaoY+70 && cliqueY <= botaoY+70 + botaoA) {
					
					if (frame != null) {
						frame.dispose();
					}
					
					Regras regras = new Regras();
					regras.start();

					
					
					}
				
				
			}	
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); 
		
		g.setColor(new java.awt.Color(0, 128, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.black);
		g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 40.0f));
		g.drawString("DUTCH", 175, 100);
		
		
		g.setColor(Color.RED);
		g.fillRect(botaoX, botaoY, botaoL, botaoA);
		g.setColor(Color.WHITE);
		g.drawRect(botaoX, botaoY, botaoL, botaoA);
		g.setFont(g.getFont().deriveFont(18.0f));

		g.drawString("Jogar", botaoX+70, botaoY+30);
		
		
		
		g.setColor(Color.RED);
		g.fillRect(botaoX, botaoY+70, botaoL, botaoA);
		g.setColor(Color.WHITE);
		g.drawRect(botaoX, botaoY+70, botaoL, botaoA);
		g.setFont(g.getFont().deriveFont(18.0f));

		g.drawString("Regras", botaoX+65, botaoY+100);
		
		
	}
	
	public void createScreen() {
		frame = new JFrame("Jogo Dutch - Menu");
		frame.add(this);
		frame.setSize(500, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		createScreen(); 
	}
}