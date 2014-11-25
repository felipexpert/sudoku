package sudoku.lang.languages;

import sudoku.lang.Linguagem;
import static sudoku.lang.Text.*;


public class Portugues extends Linguagem{
	
	static {
		portugues = new Portugues();
	}
	
	private static final Portugues portugues;
	
	private Portugues() {
		/*SUDOKU_INICIAL_EXCEPTION, SUDOKU_INSOLUCIONAVEL_EXCEPTION,
	OUTPUT, OUTPUT_PROCESSANDO, CLASSIC, _16X16, RESOLVER, PARAR,
	PARANDO, EXCEPTION, SOLUCOES_ENCONTRADAS, LIMPAR, _6X6, _12X12,
	_4X4, _25X25, _36X36, _49X49, _64X64, _81X81, _100X100, MODALIDADES,
	TOTAL_RESP_ENCONTRADAS_ULTIMO_PUZZLE;*/
		textos.put(RESOLVER, "Resolver");
		textos.put(PARAR, "Parar");
		textos.put(PARANDO, "Parando");
		textos.put(EXCEPTION, "Excessão");
		textos.put(SOLUCOES_ENCONTRADAS, "Soluções encontradas");
		textos.put(LIMPAR, "Limpar");
		textos.put(MODALIDADES, "Modalidades");
		textos.put(TOTAL_RESP_ENCONTRADAS_ULTIMO_PUZZLE, "Total de respostas encontradas no último sudoku");
		textos.put(SUDOKU_INICIAL_EXCEPTION, "Você inseriu algum(ns) campos incoerentes");
		textos.put(SUDOKU_INSOLUCIONAVEL_EXCEPTION, "Não é possível solucionar este sudoku");
		textos.put(_4X4, "Mel na chupeta(4x4)");
		textos.put(_6X6, "Que vida boa(6x6)");
		textos.put(_12X12, "Meio difícil(12x12)");
		textos.put(_16X16, "Difícil(16x16)");
		textos.put(_25X25, "Mais que difícil(25x25)");
		textos.put(_36X36, "Insano (36x36)");
		textos.put(_49X49, "Psicopata (49x49)");
		textos.put(_64X64, "Demoníaco (64x64)");
		textos.put(_81X81, "Diabólico (81x81)");
		textos.put(_100X100, "Santificado (100x100)");
		textos.put(_121X121, "Angelical (121x121)");
		textos.put(_144X144, "Divino (144X144)");
		textos.put(OUTPUT_PROCESSANDO, "Processando");
		textos.put(OUTPUT, "Saída");
		textos.put(CLASSIC, "Clássico(9x9)");
		textos.put(LINGUAGENS, "Linguagens");
		textos.put(CONTATO, "Contato");
		
	}

	@Override
	public void setLinguagem() {
		super.setLinguagem(this);
	}
	
	public static Portugues getInstance() {
		return portugues;
	}

}
