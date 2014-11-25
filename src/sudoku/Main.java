package sudoku;

import sudoku.lang.languages.English;
import gui.FrmPrincipal;

public class Main {

	private Main() {}
	
	
	public static void main(String[] args) {
		
		//Portugues.getInstance().setLinguagem();
		English.getInstance().setLinguagem();
		new FrmPrincipal();
	}
}
