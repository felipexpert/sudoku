package sudoku.exceptions;

import sudoku.lang.Linguagem;
import sudoku.lang.Text;

public class SudokuInicialException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7836192307697520617L;

	public SudokuInicialException() {
		super(Linguagem.word(Text.SUDOKU_INICIAL_EXCEPTION));
	}
} 
