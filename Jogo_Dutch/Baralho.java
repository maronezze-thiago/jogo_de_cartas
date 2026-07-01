package Jogo_Dutch;

import java.util.Random;

public class Baralho extends Mao {

	Random rand = new Random();
	
	public void popular() {
		for(Naipe naipe : Naipe.values()) {
			for(Numero numero : Numero.values()) {
				Carta carta = new Carta(numero, naipe);
				this.adicionar(carta);
			}
		}
	}
	
	public void embaralhar() {
		for(int i = cartas.size() - 1; i > 0; i--) {
			int pick = rand.nextInt(i);
			Carta carta_aleatoria = cartas.get(pick);
			Carta ultima_carta = cartas.get(i);
			cartas.set(i, carta_aleatoria);
			cartas.set(pick, ultima_carta);
		}
	}
}
