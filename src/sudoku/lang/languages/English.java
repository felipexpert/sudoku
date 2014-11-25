package sudoku.lang.languages;

import static sudoku.lang.Text.CLASSIC;
import static sudoku.lang.Text.EXCEPTION;
import static sudoku.lang.Text.LIMPAR;
import static sudoku.lang.Text.LINGUAGENS;
import static sudoku.lang.Text.MODALIDADES;
import static sudoku.lang.Text.OUTPUT;
import static sudoku.lang.Text.OUTPUT_PROCESSANDO;
import static sudoku.lang.Text.PARANDO;
import static sudoku.lang.Text.PARAR;
import static sudoku.lang.Text.RESOLVER;
import static sudoku.lang.Text.SOLUCOES_ENCONTRADAS;
import static sudoku.lang.Text.SUDOKU_INICIAL_EXCEPTION;
import static sudoku.lang.Text.SUDOKU_INSOLUCIONAVEL_EXCEPTION;
import static sudoku.lang.Text.TOTAL_RESP_ENCONTRADAS_ULTIMO_PUZZLE;
import static sudoku.lang.Text._100X100;
import static sudoku.lang.Text._121X121;
import static sudoku.lang.Text._12X12;
import static sudoku.lang.Text._144X144;
import static sudoku.lang.Text._16X16;
import static sudoku.lang.Text._25X25;
import static sudoku.lang.Text._36X36;
import static sudoku.lang.Text._49X49;
import static sudoku.lang.Text._4X4;
import static sudoku.lang.Text._64X64;
import static sudoku.lang.Text._6X6;
import static sudoku.lang.Text._81X81;
import sudoku.lang.Linguagem;
import sudoku.lang.Text;

public class English extends Linguagem{
	static {
		english = new English();
	}
	
	private static final English english;
	
	private English() {
		textos.put(RESOLVER, "Solve");
		textos.put(PARAR, "Stop");
		textos.put(PARANDO, "Stoping");
		textos.put(EXCEPTION, "Exception");
		textos.put(SOLUCOES_ENCONTRADAS, "Solutions found");
		textos.put(LIMPAR, "Clean");
		textos.put(MODALIDADES, "Modalities");
		textos.put(TOTAL_RESP_ENCONTRADAS_ULTIMO_PUZZLE, "Total solutions found in the last sudoku");
		textos.put(SUDOKU_INICIAL_EXCEPTION, "You have inserted one or more incoherent fields");
		textos.put(SUDOKU_INSOLUCIONAVEL_EXCEPTION, "It's not possible to solve this sudoku");
		textos.put(_4X4, "Really easy(4x4)");
		textos.put(_6X6, "Easy(6x6)");
		textos.put(_12X12, "Almost hard(12x12)");
		textos.put(_16X16, "Hard(16x16)");
		textos.put(_25X25, "Very hard(25x25)");
		textos.put(_36X36, "Insane (36x36)");
		textos.put(_49X49, "psychopath (49x49)");
		textos.put(_64X64, "Demonic (64x64)");
		textos.put(_81X81, "Diabolic (81x81)");
		textos.put(_100X100, "Sanctified (100x100)");
		textos.put(_121X121, "angelic (121x121)");
		textos.put(_144X144, "Divine (144X144)");
		textos.put(OUTPUT_PROCESSANDO, "Processing");
		textos.put(OUTPUT, "Output");
		textos.put(CLASSIC, "Classic(9x9)");
		textos.put(LINGUAGENS, "Languages");
		textos.put(Text.CONTATO, "Contact");
		
	}
	@Override
	public void setLinguagem() {
		super.setLinguagem(this);
	}
	
	public static English getInstance() {
		return english;
	}
}
