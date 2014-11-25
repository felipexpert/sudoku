package sudoku.lang;

import java.util.HashMap;
import java.util.Map;


public abstract class Linguagem {
	protected Map<Text, String> textos;
	private static Linguagem linguagem;
	
	
	protected Linguagem() {
		textos = new HashMap<>();
		for(Text t : Text.values()) {
			textos.put(t, t.toString());
		}
	}
	
	public static String word(Text texto) {
		return linguagem.textos.get(texto);
	}
	
	protected void setLinguagem(Linguagem linguagem) {
		Linguagem.linguagem = linguagem;
	}
	
	abstract public void setLinguagem();
}
