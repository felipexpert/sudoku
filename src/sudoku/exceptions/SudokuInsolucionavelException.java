package sudoku.exceptions;

import sudoku.lang.Linguagem;
import sudoku.lang.Text;

public class SudokuInsolucionavelException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9119401734380796307L;
	
	public SudokuInsolucionavelException() {
		super(Linguagem.word(Text.SUDOKU_INSOLUCIONAVEL_EXCEPTION));
	}

}
