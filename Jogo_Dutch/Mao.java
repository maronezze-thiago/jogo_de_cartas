package Jogo_Dutch;

import java.util.ArrayList;

public class Mao {

	
	public ArrayList<Carta> cartas;
	
	public Mao() {
		cartas = new ArrayList<Carta>();
	}
	
	public void limpar() {
		cartas.clear();
	}
	public void adicionar(Carta carta) {
		cartas.add(carta);
	}
	public String mostrar_mao() {
		String str = "";
		
		 for(Carta c: cartas) {
			 str += c.toString()+ "\n";
		 }
		
		return str;
	}
	
	public boolean cartear(Carta carta, Mao outraMao) {
		if(!cartas.contains(carta)) {
			return false;
		}else {
			cartas.remove(carta);
			outraMao.adicionar(carta);
			return true; 
		}
	}
}

