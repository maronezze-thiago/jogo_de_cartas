package Jogo_Dutch;

public class Carta {

	private Naipe naipe;
	private Numero numero;
	private boolean estaParaCima;
	
	public Carta(Numero numero, Naipe naipe) {
		this.numero = numero;
		this.naipe = naipe;
		estaParaCima = false;
	}
	
//	// fazer as cartas virarem para testes
//	public Carta(Numero numero, Naipe naipe) {
//		this.numero = numero;
//		this.naipe = naipe;
//		// ALTERADO: Agora todas as cartas nascem viradas para cima para fins de teste
//		this.estaParaCima = true; 
//	}
//	
	
	
	public String getNaipe() {
		return naipe.print_naipe();
	}
	
	public int getNumero() {
		return numero.get_numero();
	}
	
	public void virarCarta() {
		estaParaCima = !estaParaCima;
	}
	
	public boolean estaParaCima() {
		return estaParaCima;
	}
	
	public String toString() {
		String str = "";
		if(estaParaCima) {
			str += numero.printNumero() + " de " + naipe.print_naipe();
		} else {
			str = "Virada para baixo";
		}
		return str;
	}
	
	public int getColuna() {
	    return this.numero.ordinal(); 
	}

	public int getLinha() {
	    return this.naipe.ordinal();
	}

	// --- NOVOS MÉTODOS DE SUPORTE INTEGRADOS ---
	public boolean eVermelha() {
		return naipe == Naipe.COPAS || naipe == Naipe.OUROS;
	}

	public int getPontuacao() {
		if (numero == Numero.REI) {
			return eVermelha() ? 0 : 13;
		}
		return getNumero();
	}

	public String logCarta() {
		return numero.printNumero() + " de " + naipe.print_naipe();
	}
}