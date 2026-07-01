package Jogo_Dutch;

public enum Naipe {

	PAUS("Paus"),
	COPAS("Copas"),
	ESPADAS("Espadas"),
	OUROS("Ouros");


	private final String texto_naipe;
	
	private Naipe(String texto_naipe) {
		this.texto_naipe = texto_naipe;
	}
	
	
	public String print_naipe() {
		return texto_naipe;
	}




}
