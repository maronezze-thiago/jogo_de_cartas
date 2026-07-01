package Jogo_Dutch;

public enum Numero {

	ÁS(1,"ás"), DOIS(2,"2"), TRES(3,"3"),QUATRO(4,"4"),
	CINCO(5,"5"),SEIS(6, "6"),SETE(7,"7"),OITO(8,"8"),
	NOVE(9,"9"), DEZ(10,"10"), VALETE(11,"valete"), RAINHA(12,"rainha"), 
	REI(13,"rei");
	
	private final int valor_numero;
	private final String String_numero;
	
	private Numero(int valor_numero, String string_numero) {
		this.valor_numero = valor_numero;
		this.String_numero = string_numero;
	}
	
	public int get_numero() {
		return valor_numero;
	}
	
	public String printNumero() {
		  return String_numero;
	}
	
	
}
