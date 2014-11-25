package sudoku;

import sudoku.lang.Linguagem;
import sudoku.lang.Text;

public enum Modalidade {
	
	_4x4(4, 4, 4, 2, 2) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._4X4));
		}
	},
	
	_6x6(6, 6, 6, 2, 3) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._6X6));
		}
	},
	CLASSIC(9, 9, 9, 3, 3) {
		@Override
		public String toString() {
			return(Linguagem.word(Text.CLASSIC));
		}
	},
	_12x12(12, 12, 12, 3, 4) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._12X12));
		}
	},
	_16x16(16, 16, 16, 4, 4) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._16X16));
		}
	},
	_25x25(25, 25, 25, 5, 5) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._25X25));
		}
	},
	_36x36(36, 36, 36, 6, 6) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._36X36));
		}
	},
	_49x49(49, 49, 49, 7, 7) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._49X49));
		}
	},
	_64x64(64, 64, 64, 8, 8) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._64X64));
		}
	},
	_81x81(81, 81, 81, 9, 9) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._81X81));
		}
	},
	_100x100(100, 100, 100, 10, 10) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._100X100));
		}
	},
	_121x121(121, 121, 121, 11, 11) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._121X121));
		}
	},
	_144x144(144, 144, 144, 12, 12) {
		@Override
		public String toString() {
			return(Linguagem.word(Text._144X144));
		}
	};
	
	public final int linhas, colunas, grades, lGrade, cGrade, possibilidades;
	
	private Modalidade(int linhas, int colunas, int grades, int lGrade, int cGrade) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.grades = grades;
		this.lGrade = lGrade;
		this.cGrade = cGrade;
		possibilidades = lGrade * cGrade;
	}
}
