package sudoku;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import sudoku.exceptions.SudokuInicialException;
import sudoku.exceptions.SudokuInsolucionavelException;
import sudoku.lang.languages.Portugues;

public class Sudoku {
	private final Modalidade modalidade;
	
	private final List<Set<Integer>> sudoku;
	
	private final List<Map<Integer, Set<Integer>>> linhas;
	private final List<Map<Integer, Set<Integer>>> colunas;
	private final List<Map<Integer, Set<Integer>>> grades;
	
	private final Set<Integer> todasPossibilidades;
	
	private final Queue<Integer[]> respostas;
	
	//private static boolean continuar = true;
	private final Status status;
	
	private static final int INCLUSIVE = 0;
	private static final int EXCLUSIVE = 1;
	
	{
		linhas = new ArrayList<>();
		colunas = new ArrayList<>();
		grades = new ArrayList<>();
	}
	
	private Sudoku(Sudoku sudoku) { //chute
		modalidade = sudoku.modalidade;
		
		this.sudoku = new ArrayList<>();
		
		for(Set<Integer> set : sudoku.sudoku) {
			Set<Integer> copySet = new HashSet<>();
			for(Integer i : set) {
				copySet.add(new Integer(i));
			}
			this.sudoku.add(copySet);
		}
		
		
		ajeitaSudoku();
		
		todasPossibilidades = sudoku.todasPossibilidades;
		
		respostas = sudoku.respostas;
		
		status = sudoku.status;
		
	}
	
	public Sudoku(Modalidade modalidade, Integer[] inicial) throws SudokuInicialException {
		this.modalidade = modalidade;
		todasPossibilidades = getTodasPossibilidades();
		
		if(inicial.length != modalidade.linhas * modalidade.colunas)
			throw new SudokuInicialException();
		
		sudoku = new ArrayList<>();
		
		for(int x = 0; x < inicial.length; x++) {
			HashSet<Integer> set = new HashSet<>();
			if(inicial[x] == null) {
				set.addAll(todasPossibilidades);
				sudoku.add(set);
				continue;
			} else if(inicial[x] < 1 || inicial[x] > modalidade.possibilidades) {
				throw new SudokuInicialException();
			}
			
			for(int y = x + 1; y < inicial.length; y++) {
				if(inicial[y] == null)
					continue;
				
				if(inicial[x].equals(inicial[y])) {
					if(estaoNaMesmaLinhaColunaOuGrade(x, y))
						throw new SudokuInicialException();
				}
				
			}
			
			set.add(inicial[x]);
			sudoku.add(set);
		}
		
		ajeitaSudoku();
		
		//respostas = new HashSet<>();
		respostas = new LinkedList<>();
		
		status = new Status();
	}
	
	private static class Status {
		private boolean continua = true;
		private long totalRespostas = 0;
	}
	
	private void ajeitaSudoku() {
		
		for(Integer i : primeirosIndexDeCadaLinha()) {
			linhas.add(linha(i));
		}
		
		
		
		for(Integer i : primeirosIndexDeCadaColuna()) {
			colunas.add(coluna(i));
		}
		
		
		
		for(Integer i : primeirosIndexDeCadaGrade()) {
			grades.add(grade(i));
		}
		
	}
	
	private boolean remove(int index, Integer o) throws SudokuInsolucionavelException {
		boolean b = sudoku.get(index).remove(o);
		/*
		if(sudoku.get(index).size() == 0) {
			throw new SudokuInsolucionavelException();
		}
		*/
		
		//if(b || sudoku.get(index).size() == 1)
		//	fiscaliza();
		if(b) fiscaliza2(index);
		return b;
	}
	
	private boolean removeAll(int index, Collection<Integer> colection) throws SudokuInsolucionavelException {
		boolean b = sudoku.get(index).removeAll(colection);
		/*
		if(sudoku.get(index).size() == 0) {
			throw new SudokuInsolucionavelException();
		}
		*/
		//if(b || sudoku.get(index).size() == 1)
		//	fiscaliza();
		if(b) fiscaliza2(index);
		return b;
	}
	
	private Map<Integer, Set<Integer>> getLinhaSemMesmaGradeQQIndex(int index) {
		Map<Integer, Set<Integer>> map = new LinkedHashMap<>();
		
		for(Integer i : linhas.get(index / modalidade.linhas).keySet()) {
			if(!pertenceMesmaGrade(index, i)) {
				map.put(i, sudoku.get(i));
			}
		}
		
		return map;
	}
	
	private Map<Integer, Set<Integer>> getLinhaQQIndex(int index, int op) {
		Map<Integer, Set<Integer>> map = new LinkedHashMap<>(linhas.get(index / modalidade.linhas));
		switch (op) {
		case INCLUSIVE:
			break;
		case EXCLUSIVE:
			map.remove(index);
			break;
		default:
			assert false;
		}
		return map;
	}
	
	private Map<Integer, Set<Integer>> getColunaSemMesmaGradeQQIndex(int index) {
		Map<Integer, Set<Integer>> map = new LinkedHashMap<>();
		
		for(Integer i : colunas.get(index % modalidade.colunas).keySet()) {
			if(!pertenceMesmaGrade(index, i)) {
				map.put(i, sudoku.get(i));
			}
		}
		
		return map;
	}
	
	private Map<Integer, Set<Integer>> getColunaQQIndex(int index, int op) {
		Map<Integer, Set<Integer>> map = new LinkedHashMap<>(colunas.get(index % modalidade.colunas));
		
		switch (op) {
		case INCLUSIVE:
			break;
		case EXCLUSIVE:
			map.remove(index);
			break;
		default:
			assert false;
		}
		return map;
	}
	
	private Map<Integer,  Set<Integer>> getGradeQQIndex(int index, int op) {
		Map<Integer,  Set<Integer>> map = null;
		
		for(Map<Integer,  Set<Integer>> m : grades) {
			if(pertenceMesmaGrade(index, m.keySet().iterator().next())) {
				map = new LinkedHashMap<>(m);
				break;
			}
		}
		assert map != null;
		
		switch (op) {
		case INCLUSIVE:
			break;
		case EXCLUSIVE:
			map.remove(index);
			break;
		default:
			assert false;
		}
		
		return map;
	}
	
	private boolean estaoNaMesmaLinhaColunaOuGrade(int iX, int iY) {
		return pertenceMesmaLinha(iX, iY) | pertenceMesmaColuna(iX, iY) | pertenceMesmaGrade(iX, iY);
	}
	
	private boolean pertenceMesmaLinha(int iX, int iY) {
		return iX / modalidade.linhas == iY / modalidade.linhas;
	}
	
	private boolean pertenceMesmaColuna(int iX, int iY) {
		return iX % modalidade.colunas == iY % modalidade.colunas;
	}
	
	private boolean pertenceMesmaGrade(int iX, int iY) {
		boolean retorno = false;
		// pesquisar com carinho se o erro está aqui
		int gradeXX = iX % modalidade.cGrade;
		int gradeXY = (iX / modalidade.colunas) % modalidade.lGrade;
		
		int gradeNumeroInicial = iX - gradeXX - modalidade.colunas * gradeXY;
		
		A:
		for(int x = 0, soma = 0; x < modalidade.lGrade; x++, soma += modalidade.colunas) {
			for(int y = 0; y < modalidade.cGrade; y++) {
				if(iY == gradeNumeroInicial + soma + y) {
					retorno = true;
					break A;
				}
			}
		}
		return retorno;
	}
	
	//com isto podemos obter cada linha
	private Map<Integer, Set<Integer>> linha(Integer primeiroIndexDaLinha) {
		Map<Integer, Set<Integer>> map = new LinkedHashMap<>();
		
		int indexMax = modalidade.colunas + primeiroIndexDaLinha;
		
		for(int x = primeiroIndexDaLinha; x < indexMax; x++) {
			map.put(x, sudoku.get(x));
		}
		
		return map;
	}
	
	private Set<Integer> primeirosIndexDeCadaLinha() {
		Set<Integer> set = new TreeSet<>();
		
		for(int x = 0, y = 0; x < modalidade.linhas; x++) {
			set.add(y);
			
			y += modalidade.colunas;
		}
		
		return set;
	}
	//com isto podemos obter cada linha - fim
	
	private Map<Integer, Set<Integer>> coluna(Integer primeiroIndexDaColuna) {
		Map<Integer, Set<Integer>> map = new LinkedHashMap<>();
		
		for(int x = primeiroIndexDaColuna, y = 0; y < modalidade.linhas; x += modalidade.colunas, y++) {
			map.put(x, sudoku.get(x));
		}
		
		return map;
	}
	
	private Set<Integer> primeirosIndexDeCadaColuna() {
		Set<Integer> set = new TreeSet<>();
		
		for(int x = 0; x < modalidade.colunas; x++) {
			set.add(x);
		}
		
		return set;
	}
	
	//com isto podemos obter cada grade
	private Map<Integer, Set<Integer>> grade(Integer primeiroIndexDaGrade) {
		Map<Integer, Set<Integer>> map = new LinkedHashMap<>();
		
			for(int x = 0, soma = 0; x < modalidade.lGrade; x++, soma += modalidade.colunas) {
				for(int y = 0; y < modalidade.cGrade; y++) {
					int index = primeiroIndexDaGrade + soma + y;
					map.put(index, sudoku.get(index));
				}
			}
		return map;
	}
	
	private Set<Integer> primeirosIndexDeCadaGrade() {
		Set<Integer> set = new TreeSet<>();
		int numGradeNaLinha = modalidade.colunas / modalidade.cGrade;
		//int numGradeNaColuna = modalidade.linhas / modalidade.lGrade;
		
		for(int x = 0, y = 0; x < sudoku.size();) {
			set.add(x);
			
			x += modalidade.cGrade;
			y++;
			
			if(y == numGradeNaLinha) {
				x += modalidade.colunas * (numGradeNaLinha - 1);
				y = 0;
			}
		}
		
		return set;
	}
	//com isto podemos obter cada grade - fim
	
	@SuppressWarnings("unused")
	@Deprecated
	private void verificaGrade(Integer[] grade) throws SudokuInicialException {
		assert grade.length == modalidade.lGrade * modalidade.cGrade;
		
		for(int x = 0; x < grade.length; x++) {
			if(grade[x] == null)
				continue;
			for(int y = x + 1; y < grade.length; y++) {
				if(grade[y] == null)
					continue;
				
				if(grade[x].equals(grade[y]))
					throw new SudokuInicialException();
			}
		}
	}
	
	private boolean encontrarGenericamente2() throws SudokuInsolucionavelException {
		boolean diminuiu = false;
		for(int x = 0; x < sudoku.size(); x++) {
			if(sudoku.get(x).size() == 1) {
				continue;
			}
			Set<Integer> numerosNaMesmaLinhaColunaEOuGrade = new HashSet<>();
			
			Map<Integer, Set<Integer>> map = getGradeQQIndex(x, EXCLUSIVE);
			map.putAll(getLinhaQQIndex(x, EXCLUSIVE));
			map.putAll(getColunaQQIndex(x, EXCLUSIVE));
			
			for(Set<Integer> set : map.values()) {
				if(set.size() == 1) {
					numerosNaMesmaLinhaColunaEOuGrade.add(set.iterator().next());
				}
			}
			
			/*
			for(int y = 0; y < sudoku.size(); y++) {
				if(estaoNaMesmaLinhaColunaOuGrade(x, y)) {
					if(sudoku.get(y).size() == 1) {
						for(Integer i : sudoku.get(y)) {
							numerosNaMesmaLinhaColunaEOuGrade.add(i);
						}
					}
				}
			}*/
			
			if(numerosNaMesmaLinhaColunaEOuGrade.size() > 0) {
				if(removeAll(x, numerosNaMesmaLinhaColunaEOuGrade))
					if(diminuiu == false)
						diminuiu = true;
				
				//if(sudoku.get(x).size() == 0)
				//	throw new SudokuInsolucionavelException();
				
			}
		}
		return diminuiu;
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private boolean encontrarGenericamente() throws SudokuInsolucionavelException {
		boolean diminuiu = false;
		for(int x = 0; x < sudoku.size(); x++) {
			if(sudoku.get(x).size() == 1) {
				continue;
			}
			Set<Integer> numerosNaMesmaLinhaColunaEOuGrade = new HashSet<>();
			
			for(int y = 0; y < sudoku.size(); y++) {
				if(estaoNaMesmaLinhaColunaOuGrade(x, y)) {
					if(sudoku.get(y).size() == 1) {
						for(Integer i : sudoku.get(y)) {
							numerosNaMesmaLinhaColunaEOuGrade.add(i);
						}
					}
				}
			}
			
			if(numerosNaMesmaLinhaColunaEOuGrade.size() > 0) {
				if(removeAll(x, numerosNaMesmaLinhaColunaEOuGrade))
					if(diminuiu == false)
						diminuiu = true;
				
				//if(sudoku.get(x).size() == 0)
				//	throw new SudokuInsolucionavelException();
				
			}
		}
		return diminuiu;
	}
	
	private boolean encontrarNasLinhasColunasEGrades2() throws SudokuInsolucionavelException {
		boolean encontrou = false;
		
		
		A:
		for(int x = 0; x < sudoku.size(); x++) {
			
			if(sudoku.get(x).size() == 1)
				continue;
			
			
			//int itens = 1;
			Set<Integer> possibilidadesGrades = new HashSet<>();
			
			Map<Integer, Set<Integer>> gradeCerta = getGradeQQIndex(x, EXCLUSIVE);
			assert gradeCerta.size() == modalidade.possibilidades - 1;
			for(Set<Integer> set : gradeCerta.values()) {
				possibilidadesGrades.addAll(set);
			}
			
			Set<Integer> possibilidadesLinhas = new HashSet<>();
			
			Map<Integer, Set<Integer>> linhaCerta = getLinhaQQIndex(x, EXCLUSIVE);
			assert linhaCerta.size() == modalidade.possibilidades - 1;
			for(Set<Integer> set : linhaCerta.values()) {
				possibilidadesLinhas.addAll(set);
			}
			
			Set<Integer> possibilidadesColunas = new HashSet<>();
			
			Map<Integer, Set<Integer>> colunaCerta = getColunaQQIndex(x, EXCLUSIVE);
			assert colunaCerta.size() == modalidade.possibilidades - 1;
			for(Set<Integer> set : colunaCerta.values()) {
				possibilidadesColunas.addAll(set);
			}
			/*
			for(int y = 0; y < sudoku.size(); y++) {
				if(y == x)
					continue;
				
				if(pertenceMesmaGrade(x, y)) {
					possibilidadesGrades.addAll(sudoku.get(y));
				}
				if(pertenceMesmaLinha(x, y)) {
					possibilidadesLinhas.addAll(sudoku.get(y));
				}  else if(pertenceMesmaColuna(x, y)) {
					possibilidadesColunas.addAll(sudoku.get(y));
				}
			}*/
			
			Set<Integer> set = new HashSet<>(sudoku.get(x));
			for(Integer i : set) {
				int founds = 0;
				if(!possibilidadesGrades.contains(i)) {
					sudoku.get(x).clear();
					sudoku.get(x).add(i);
					
					if(++founds > 1) {
						throw new SudokuInsolucionavelException();
					}
					
					if(!encontrou)
						encontrou = true;
					continue A;
				}
			}
			set = new HashSet<>(sudoku.get(x));
			for(Integer i : set) {
				int founds = 0;
				if(!possibilidadesLinhas.contains(i)) {
					sudoku.get(x).clear();
					sudoku.get(x).add(i);
					
					if(++founds > 1) {
						throw new SudokuInsolucionavelException();
					}
					
					if(!encontrou)
						encontrou = true;
					continue A;
				}
			}
			set = new HashSet<>(sudoku.get(x));
			for(Integer i : set) {
				int founds = 0;
				if(!possibilidadesColunas.contains(i)) {
					sudoku.get(x).clear();
					sudoku.get(x).add(i);
					
					if(++founds > 1) {
						throw new SudokuInsolucionavelException();
					}
					
					if(!encontrou)
						encontrou = true;
					continue A;
				}
			}
			
		}
		
		return encontrou;
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private boolean encontrarNasLinhasColunasEGrades() throws SudokuInsolucionavelException {
		boolean encontrou = false;
		
		
		A:
		for(int x = 0; x < sudoku.size(); x++) {
			
			if(sudoku.get(x).size() == 1)
				continue;
			
			//int itens = 1;
			Set<Integer> possibilidadesGrades = new HashSet<>();
			Set<Integer> possibilidadesLinhas = new HashSet<>();
			Set<Integer> possibilidadesColunas = new HashSet<>();
			for(int y = 0; y < sudoku.size(); y++) {
				if(y == x)
					continue;
				
				if(pertenceMesmaGrade(x, y)) {
					possibilidadesGrades.addAll(sudoku.get(y));
				}
				if(pertenceMesmaLinha(x, y)) {
					possibilidadesLinhas.addAll(sudoku.get(y));
				}  else if(pertenceMesmaColuna(x, y)) {
					possibilidadesColunas.addAll(sudoku.get(y));
				}
			}
			
			Set<Integer> set = new HashSet<>(sudoku.get(x));
			for(Integer i : set) {
				int founds = 0;
				if(!possibilidadesGrades.contains(i)) {
					sudoku.get(x).clear();
					sudoku.get(x).add(i);
					
					if(++founds > 1) {
						throw new SudokuInsolucionavelException();
					}
					
					if(!encontrou)
						encontrou = true;
					continue A;
				}
			}
			set = new HashSet<>(sudoku.get(x));
			for(Integer i : set) {
				int founds = 0;
				if(!possibilidadesLinhas.contains(i)) {
					sudoku.get(x).clear();
					sudoku.get(x).add(i);
					
					if(++founds > 1) {
						throw new SudokuInsolucionavelException();
					}
					
					if(!encontrou)
						encontrou = true;
					continue A;
				}
			}
			set = new HashSet<>(sudoku.get(x));
			for(Integer i : set) {
				int founds = 0;
				if(!possibilidadesColunas.contains(i)) {
					sudoku.get(x).clear();
					sudoku.get(x).add(i);
					
					if(++founds > 1) {
						throw new SudokuInsolucionavelException();
					}
					
					if(!encontrou)
						encontrou = true;
					continue A;
				}
			}
			
		}
		
		return encontrou;
	}
	
	
	
	private boolean encontrarPorDeducaoSimples2() throws SudokuInsolucionavelException {
		boolean encontrou = false;
		
		//List<Set<Integer>> possibilidadesUnicasDeCadaLinha = null;
		Set<Integer> possibilidadesDeVariasLinhasDentroDaGrade = null;
		Set<Integer> possibilidadesDeVariasColunasDentroDaGrade = null;
		
		for(int x = 0; x < sudoku.size(); x++) {
			if(sudoku.get(x).size() == 1)
				continue;
			
			//possibilidadesUnicasDeCadaLinha = new ArrayList<Set<Integer>>();
			possibilidadesDeVariasLinhasDentroDaGrade = new HashSet<>();
			possibilidadesDeVariasColunasDentroDaGrade = new HashSet<>();
			
			Map<Integer, Set<Integer>> gradeCerta = getGradeQQIndex(x, EXCLUSIVE);
			
			for(Integer y : gradeCerta.keySet()) {
				
				if(!pertenceMesmaLinha(x, y)) {
					for(Integer i : sudoku.get(x)) {
						for(Integer i2 : gradeCerta.get(y)) {
							if(i.equals(i2)) {
								possibilidadesDeVariasLinhasDentroDaGrade.add(i);
							}
						}
					}
				}
				
				if(!pertenceMesmaColuna(x, y)) {
					for(Integer i : sudoku.get(x)) {
						for(Integer i2 : gradeCerta.get(y)) {
							if(i.equals(i2)) {
								possibilidadesDeVariasColunasDentroDaGrade.add(i);
							}
						}
					}
				}
			}
			
			A:
			for(Integer i : sudoku.get(x)) {
				
				for(Integer i2 : possibilidadesDeVariasLinhasDentroDaGrade) {
					if(i.equals(i2))
						continue A;
				}
				
				for(Integer z : getLinhaSemMesmaGradeQQIndex(x).keySet()) {
					if(remove(z, i)) {
						if(!encontrou)
							encontrou = true;
					}
				}
			}
		
			A:
			for(Integer i : sudoku.get(x)) {
				
				for(Integer i2 : possibilidadesDeVariasColunasDentroDaGrade) {
					if(i.equals(i2))
						continue A;
				}
				
				for(Integer z : getColunaSemMesmaGradeQQIndex(x).keySet()) {
					if(remove(z, i)) {
						if(!encontrou)
							encontrou = true;
					}
				}
			}
		}
		
		return encontrou;
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private boolean encontrarPorDeducaoSimples() throws SudokuInsolucionavelException {
		boolean encontrou = false;
		
		//List<Set<Integer>> possibilidadesUnicasDeCadaLinha = null;
		Set<Integer> possibilidadesDeVariasLinhas = null;
		Set<Integer> possibilidadesDeVariasColunas = null;
		
		for(int x = 0; x < sudoku.size(); x++) {
			if(sudoku.get(x).size() == 1)
				continue;
			
			//possibilidadesUnicasDeCadaLinha = new ArrayList<Set<Integer>>();
			possibilidadesDeVariasLinhas = new HashSet<>();
			possibilidadesDeVariasColunas = new HashSet<>();
			
			int itens = 1;
			for(int y = 0; y < sudoku.size(); y++) {
				if(x == y)
					continue;
				
				if(pertenceMesmaGrade(x, y)) {
					itens++;
					if(!pertenceMesmaLinha(x, y)) {
						for(Integer i : sudoku.get(x)) {
							for(Integer i2 : sudoku.get(y)) {
								if(i.equals(i2)) {
									
									possibilidadesDeVariasLinhas.add(i);
								}
							}
						}
					}
					
					if(!pertenceMesmaColuna(x, y)) {
						for(Integer i : sudoku.get(x)) {
							for(Integer i2 : sudoku.get(y)) {
								if(i.equals(i2)) {
									
									possibilidadesDeVariasColunas.add(i);
								}
							}
						}
					}
				}
				if(itens == modalidade.possibilidades) {
					A:
					for(Integer i : sudoku.get(x)) {
						
						for(Integer i2 : possibilidadesDeVariasLinhas) {
							if(i.equals(i2))
								continue A;
						}
						
						int itens2 = 1; //aqui atencao
						for(int z = 0; z < sudoku.size(); z++) {
							
							if(x == z) {
								continue;
							}
							
							if(pertenceMesmaLinha(x, z) && !pertenceMesmaGrade(x, z)) {
								itens2++;
								if(remove(z, i)) {
									if(!encontrou)
										encontrou = true;
								}
								
								//System.out.println(z + " " + i + " aqui");
								
								
							}
							if(itens2 == modalidade.linhas - modalidade.cGrade) {
								continue A;
							}
						}
					}
				
					A:
					for(Integer i : sudoku.get(x)) {
						
						for(Integer i2 : possibilidadesDeVariasColunas) {
							if(i.equals(i2))
								continue A;
						}
						
						int itens2 = 1; //aqui atencao
						for(int z = 0; z < sudoku.size(); z++) {
							
							if(x == z) {
								continue;
							}
							
							if(pertenceMesmaColuna(x, z) && !pertenceMesmaGrade(x, z)) {
								itens2++;
								if(remove(z, i)) {
									if(!encontrou)
										encontrou = true;
								}
								
								//System.out.println(z + " " + i + " aqui");
								
								
							}
							if(itens2 == modalidade.linhas - modalidade.lGrade) {
								continue A;
							}
						}
					}
				}
			}
		}
		
		return encontrou;
	}
	
	private Set<Integer> getTodasPossibilidades() {
		Set<Integer> set = new HashSet<>();
		
		for(int x = 1; x <= modalidade.possibilidades; x++) {
			set.add(x);
		}
		return set;
	}
	
	private boolean encontraPorDeducaoMediana() throws SudokuInsolucionavelException {
		boolean encontrou = false;
		for(Integer i : primeirosIndexDeCadaGrade()) {
			
			//linhas
			for(int x = 0, z = i; x < modalidade.lGrade; x++, z += modalidade.colunas) {
				
				Set<Integer> linha = new HashSet<>(todasPossibilidades);
				
				
				for(Set<Integer> s : getLinhaSemMesmaGradeQQIndex(z).values()) {
					linha.removeAll(s);
				}
				
				for(Integer i2 : getGradeQQIndex(i, EXCLUSIVE).keySet()) {
					if(!pertenceMesmaLinha(z, i2)) {
						if(removeAll(i2, linha)) {
							if(!encontrou) {
								encontrou = true;
							}
						}
					}
				}
				
			}
			
			//coluna
				for(int x = 0, z = i; x < modalidade.lGrade; x++, z++) {
					
					Set<Integer> coluna = new HashSet<>(todasPossibilidades);
					
					
					for(Set<Integer> s : getColunaSemMesmaGradeQQIndex(z).values()) {
						coluna.removeAll(s);
					}
					
					for(Integer i2 : getGradeQQIndex(i, EXCLUSIVE).keySet()) {
						if(!pertenceMesmaColuna(z, i2)) {
							if(removeAll(i2, coluna)) {
								if(!encontrou) {
									encontrou = true;
								}
							}
						}
					}
					
				}
			
		}
		return encontrou;
	}
	
	private boolean desnudas() throws SudokuInsolucionavelException {
		boolean encontrou = false;
		{
			List<Map<Integer, Set<Integer>>> linhasColunasGrades = new ArrayList<>();
			linhasColunasGrades.addAll(linhas);
			linhasColunasGrades.addAll(colunas);
			linhasColunasGrades.addAll(grades);
			for(Map<Integer, Set<Integer>> regiao : linhasColunasGrades) {
				List<Set<Integer>> regioesRelevantes = new ArrayList<>();
				List<Integer> chaves = new ArrayList<>();
				for(Integer i : regiao.keySet()) {
					Set<Integer> possibilidades = regiao.get(i);
					if(possibilidades.size() > 1) {
						regioesRelevantes.add(possibilidades);
						chaves.add(i);
					}
				}
				if(resolveDesnuda(chaves, regioesRelevantes) && !encontrou)
					encontrou = true;
			}
		}
		return encontrou;
	}
	
	/**
	 * 
	 * @param chaves
	 * @param regiaoRelevante
	 * @return
	 * @throws SudokuInsolucionavelException
	 * 
	 * As chaves recebidas precisam estar sincronizadas com suas respectivas
	 * regioes.
	 */
	private boolean resolveDesnuda(final List<Integer> chaves, final List<Set<Integer>> regiaoRelevante) throws SudokuInsolucionavelException {
		boolean encontrou = false;
		List<Integer> numerosNaoEncontrados = null;
		final int size = regiaoRelevante.size();
		{
			Set<Integer> numerosNaoEncontrados2 = new HashSet<>();
			for(Set<Integer> set : regiaoRelevante) {
				for(Integer i : set) {
					numerosNaoEncontrados2.add(i);
				}
			}
			numerosNaoEncontrados = new ArrayList<>(numerosNaoEncontrados2);
		}
		A:
		for(int f = 2; f < size; f++) {
			Set<Set<Integer>> combinations = combinations(numerosNaoEncontrados, f);
			for(Set<Integer> combination : combinations) {
				List<Integer> pos = new ArrayList<>();
				for(int j = 0; j < size; j++) {
					
					if(combination.containsAll(regiaoRelevante.get(j))) {
						pos.add(j);
						
						if(pos.size() == f) {
							B:
							for(int k = 0; k < size; k++) {
								for(Integer p : pos) {
									if(p.equals(k))
										continue B;
								}
								if(removeAll(chaves.get(k), combination) && !encontrou) {
									encontrou = true;
								}
								/*
								if(regiaoRelevante.get(k).removeAll(combination)) {
									fiscaliza2(sudoku.indexOf(regiaoRelevante.get(k)));
									if(!encontrou) {
										encontrou = true;
									}
								}
								*/
							}
							break A;
						}
						
					}
				}
			}
		}
		return encontrou;
		
	}
	
	public static Set<Set<Integer>> combinations(List<Integer> groupSize, int k) {

	    Set<Set<Integer>> allCombos = new HashSet<Set<Integer>> ();
	    // base cases for recursion
	    if (k == 0) {
	        // There is only one combination of size 0, the empty team.
	        allCombos.add(new HashSet<Integer>());
	        return allCombos;
	    }
	    if (k > groupSize.size()) {
	        // There can be no teams with size larger than the group size,
	        // so return allCombos without putting any teams in it.
	        return allCombos;
	    }

	    // Create a copy of the group with one item removed.
	    List<Integer> groupWithoutX = new ArrayList<Integer> (groupSize);
	    Integer x = groupWithoutX.remove(groupWithoutX.size()-1);

	    Set<Set<Integer>> combosWithoutX = combinations(groupWithoutX, k);
	    Set<Set<Integer>> combosWithX = combinations(groupWithoutX, k-1);
	    for (Set<Integer> combo : combosWithX) {
	        combo.add(x);
	    }
	    allCombos.addAll(combosWithoutX);
	    allCombos.addAll(combosWithX);
	    return allCombos;
	}
	
	private Integer[] getFundamental() throws SudokuInsolucionavelException {
		Integer[] fundamental = new Integer[modalidade.linhas * modalidade.colunas];
		
		for(int x = 0; x < sudoku.size(); x++) {
			Set<Integer> set = sudoku.get(x);
			if(set.size() == 1) {
				fundamental[x] = set.iterator().next();
			} else if(set.size() == 0) {
				throw new SudokuInsolucionavelException();
			}
		}
		
		return fundamental;
	}
	
	
	private void fiscaliza2(int index) throws SudokuInsolucionavelException {
		
		int size = sudoku.get(index).size();
		if(size > 1) {
			return;
		}
		
		if(size == 0) {
			throw new SudokuInsolucionavelException();
		}
	
		//Integer[] fundamental = getFundamental();
	
		Map<Integer, Set<Integer>> map = getGradeQQIndex(index, EXCLUSIVE);
		map.putAll(getLinhaSemMesmaGradeQQIndex(index));
		map.putAll(getColunaSemMesmaGradeQQIndex(index));
		
		for(Integer i : map.keySet()) {
			if(map.get(i).size() > 1)
				continue;
			
			if(sudoku.get(index).iterator().next().equals(map.get(i).iterator().next())) {
				if(estaoNaMesmaLinhaColunaOuGrade(index, i))
					throw new SudokuInsolucionavelException();
			}
		}
		
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private void fiscaliza() throws SudokuInsolucionavelException {
		
		
		Integer[] fundamental = getFundamental();
		
		for(int x = 0; x < fundamental.length; x++) {
			if(fundamental[x] == null)
				continue;
			
			for(int y = x + 1; y < fundamental.length; y++) {
				if(fundamental[y] == null)
					continue;
				
				if(fundamental[x].equals(fundamental[y])) {
					if(estaoNaMesmaLinhaColunaOuGrade(x, y)) {
						sudoku.get(x).remove(fundamental[x]);
						sudoku.get(x).add(-1 * fundamental[x]);
						throw new SudokuInsolucionavelException();
					}
				}
				
			}
		}
	}
	
	private boolean isResolvido() {
		boolean b = true;
		
		for(Set<Integer> set : sudoku) {
			if(set.size() > 1) {
				b = false;
				break;
			}
		}
		
		return b;
	}
	
	@SuppressWarnings("unused")
	private class Resposta {
		private Integer[] resposta;
		
		private Resposta(Integer[] resposta) {
			this.resposta = resposta;
		}
		
		@Override
		public int hashCode() {
			int i = 0;
			
			for(int x = 0; x < resposta.length - 1; x++) {
				i += (resposta[x] + resposta[x + 1]) * 2;
			}
			
			return i;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == this)
				return true;
			if(!(obj instanceof Resposta))
				return false;
			
			Resposta obj2 = (Resposta) obj;
			
			if(this.resposta.length != obj2.resposta.length)
				return false;
			
			boolean b = true;
			
			for(int x = 0; x < resposta.length; x++) {
				if(!resposta[x].equals(obj2.resposta[x])) {
					b = false;
					break;
				}
			}
			
			return b;
		}
	}
	private void chutar2() throws SudokuInsolucionavelException {
	
		Integer copiaNum = null;
		for(int x = 0; x < sudoku.size() && status.continua; x++) {
			if(sudoku.get(x).size() > 1) {
				copiaNum = sudoku.get(x).iterator().next();
				
				Sudoku chute = new Sudoku(this);
				chute.sudoku.get(x).clear();
				chute.sudoku.get(x).add(copiaNum);
				
				try {
					chute.resolverChute();
				} catch (SudokuInsolucionavelException e) {
					remove(x, copiaNum);
					x--;
				}
			}
		}
	}
	
	private void resolverChute() throws SudokuInsolucionavelException {
		
		while(encontrarGenericamente2());
		//encontrarUsandoLogicas();
		if(isResolvido()) {
			//respostas.add(getFundamental());
			addResposta(getFundamental());
			status.totalRespostas++;
			throw new SudokuInsolucionavelException();
		} else {
			chutar2();
		}
	}
	
	private void addResposta(Integer[] r) {
		synchronized(respostas) {
			respostas.add(r);
		}
	}
	
	private void encontrarUsandoLogicas() throws SudokuInsolucionavelException {
		while((encontrarGenericamente2() |
				  encontrarNasLinhasColunasEGrades2() |
				  encontrarPorDeducaoSimples2() |
				  encontraPorDeducaoMediana()) |
				  desnudas());
	}
	
	public void comecar() throws SudokuInsolucionavelException{
		/*	
		encontrarUsandoLogicas();		
		{
			int count = 0;
			for(Set<Integer> s : sudoku) {
				if(s.size() == 1) {
					System.out.print(s.iterator().next() + ", ");
					
				} else {
					System.out.print("[");
					for(Integer i : s) {
						System.out.print(i);
					}
					System.out.print("], ");
				}
				
				if(++count == modalidade.colunas) {
					System.out.print("\n");
					count = 0;
				}
			}
		}
		parar();
		*/
		
		
		try {
			encontrarUsandoLogicas();
			
			if(isResolvido()) {
				//respostas.add(getFundamental());
				addResposta(getFundamental());
				status.totalRespostas++;
			} else {
				chutar2();
			}
		} catch(SudokuInsolucionavelException e) {
			if(status.totalRespostas == 0)
				throw new SudokuInsolucionavelException();
		}
		
		parar();
	}
	
	public List<Integer[]> getUltimasRespostas() {
		final List<Integer[]> lista = new ArrayList<>();
		
		class Obter implements Runnable {
			@Override
			public void run() {
				Integer[] resposta = null;
				do {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						assert false;
					}
					synchronized(respostas) {
						while((resposta = respostas.poll()) != null) {
							lista.add(resposta);
						}
					}
				} while(lista.size() == 0 && status.continua);
			}
		}
		
		Thread t = new Thread(new Obter());
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			assert false;
		}
		
		return (lista.size() > 0 ? lista : null);
	}
	
	public void parar() {
		status.continua = false;
	}
	
	public long getRespostaEncontradasAteOMomento() {
		return status.totalRespostas;
	}
	
	//teste
	@Deprecated
	public static void main(String[] args) {
		Portugues.getInstance().setLinguagem();
		/*
		Integer[] ints = { null, 5, 2, null, null, null, 1, null, null,
				   null, null, 1, null, null, null, null, null, 8,
				   3, null, null, null, null, 5, null, 6, 2,
				   null, null, 9, null, 8, null, null, null, null,
				   null, null, null, 5, 2, 4, null, null, null,
				   null, null, null, null, 6, null, 2, null, null,
				   9, 8, null, 7, null, null, null, null, 5,
				   7, null, null, null, null, null, 6, null, null,
				   null, null, 5, null, null, null, 4, 9, null };
		*/
		/*
		Integer[] ints = { null, null, 7, null, 3, null, 2, null, null,
						   null, null, 1, null, null, null, 9, null, null,
						   2, 4, null, null, null, null, null, 5, 6,
						   null, null, null, 4, null, 9, null, null, null,
						   1, null, null, null, 6, null, null, null, 9,
						   null, null, null, 7, null, 5, null, null, null,
						   8, 6, null, null, null,null, null, 2, 7,
						   null, null, 9, null, null, null, 8, null, null,
						   null, null, 4, null, 8, null, 3, null, null };
		*/
		/*
		Integer[] ints = { null, null, null, null, null, null, null, null, 8,
				   null, null, 5, null, 4, 2, 3, null, null,
				   null, 6, null, null, null, 1, null, 7, null,
				   null, null, null, null, 6, null, 9, 5, null,
				   null, 9, null, 5, null, 7, null, 1, null,
				   null, 7, 4, null, 3, null, null, null, null,
				   null, 5, null, 2, null,null, null, 8, null,
				   null, null, 9, 7, 8, null, 6, null, null,
				   4, null, null, null, null, null, null, null, null };
		*/
		/*
		Integer[] ints = { null, null, null, null, null, null, null, 7, null,
				   8, null, null, null, null, null, null, null, 4,
				   null, null, 4, 5, null, 7, 6, null, null,
				   null, null, 6, null, 2, null, 3, null, null,
				   null, null, null, 3, 5, 8, null, null, null,
				   null, null, 1, null, 9, null, 8, null, null,
				   null, null, 8, 4, null, 2, 7, null, null,
				   3, null, null, null, null, null, null, null, 8,
				   null, 7, null, null, null, null, null, 9, null };
		*/
		/*
		Integer[] ints = { 4, null, null, 2, 7, null, 6, null, null,
				7, 9, 8, 1, 5, 6, 2, 3, 4,
				null, 2, null, 8, 4, null, null, null, 7,
				2, 3, 7, 4, 6, 8, 9, 5, 1,
				8, 4, 9, 5, 3, 1, 7, 2, 6,
				5, 6, 1, 7, 9, 2, 8, 4, 3,
				null, 8, 2, null, 1, 5, 4, 7, 9,
				null, 7, null, null, 2, 4, 3, null, null,
				null, null, 4, null, 8, 7, null, null, 2, };
		*/
		/*
		Integer[] ints = { 1, null, 8, null, null, 2, null, null, null,
				null, 3, null, 4, null, null, 7, null, null,
				7, null, null, null, 6, null, null, 4, null,
				null, 9, null, null, null, null, null, null, 3,
				null, null, 4, null, null, null, 8, null, null,
				5, null, null, null, null, null, null, 2, null,
				null, 1, null, null, 5, null, null, null, 8,
				null, null, 3, null, null, 8, null, 1, null,
				null, null, null, 6, null, null, 3, null, 9, };
		*/
		/*
		Integer[] ints = { 5, 2, 8, 6, null, null, null, 4, 9,
				1, 3, 6, 4, 9, null, null, 2, 5,
				7, 9, 4, 2, null, 5, 6, 3, null,
				null, null, null, 1, null, null, 2, null, null,
				null, null, 7, 8, 2, 6, 3, null, null,
				null, null, 2, 5, null, 9, null, 6, null,
				2, 4, null, 3, null, null, 9, 7, 6,
				8, null, 9, 7, null, 2, 4, 1, 3,
				null, 7, null, 9, null, 4, 5, 8, 2, };
		*/
		Integer[] ints = { 6, 2, 4, 9, null, null, null, null, null,
				7, 3, 9, 1, null, null, null, null, 8,
				8, 1, 5, null, null, 4, null, null, null,
				4, null, null, null, null, 9, 3, 7, null,
				3, null, null, null, 4, null, null, null, 6,
				5, 9, 1, null, null, 3, null, null, 2,
				9, null, null, 4, null, null, 2, null, null,
				1, null, null, 2, 9, 6, null, null, 4,
				2, 4, 8, 3, 5, 7, 1, 6, 9, };
		/*
		Integer[] ints = { 3, 7, null, 4, null, 8, 1, null, null,
				null, null, null, 9, null, 3, 7, null, 4,
				9, 4, null, 1, null, null, null, 8, 3,
				4, 2, null, null, null, null, null, null, 5,
				null, null, null, 5, null, 4, null, null, null,
				8, null, null, null, null, null, null, 4, 6,
				null, 1, null, null, 4, 9, null, null, null,
				5, null, 9, 6, null, null, 4, null, null,
				null, null, 4, 2, null, null, 9, 3, 1, };
				*/
		/*
		Integer[] ints = { null, 1, null, null, null, null, null, 7, null,
				8, null, null, null, null, null, null, null, 4,
				null, null, 4, 5, null, 7, 6, null, null,
				null, null, 6, null, 2, null, 3, null, null,
				null, null, null, 3, 5, 8, null, null, null,
				null, null, 1, null, 9, null, 8, null, null,
				null, null, 8, 4, null, 2, 7, null, null,
				3, null, null, null, null, null, null, null, 8,
				null, 7, null, null, null, null, null, 9, null, };
		*/
		/*
		Integer[] ints = { null, null, 9, null, 3, null, 6, null, null,
				null, 3, 6, null, 1, 4, null, 8, 9,
				1, null, null, 8, 6, 9, null, 3, 5,
				null, 9, null, null, null, null, 8, null, null,
				null, 1, null, null, null, null, null, 9, null,
				null, 6, 8, null, 9, null, 1, 7, null,
				6, null, 1, 9, null, 3, null, null, 2,
				9, 7, 2, 6, 4, null, 3, null, null,
				null, null, 3, null, 2, null, 9, null, null, };
		/*
		Integer[] ints = { null, null, null, null, null, null, null, 7, null,
				   null, null, null, null, null, null, null, null, 4,
				   null, null, null, 5, null, 7, 6, null, null,
				   null, null, 6, null, 2, null, 3, null, null,
				   null, null, null, 3, 5, 8, null, null, null,
				   null, null, 1, null, 9, null, 8, null, null,
				   null, null, 8, 4, null, 2, 7, null, null,
				   3, null, null, null, null, null, null, null, 8,
				   null, 7, null, null, null, null, null, 9, null };
		*/
		
		/*
		Integer[] ints = { 9, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null,
				   null, null, null, null, null, null, null, null, null,
				   null, null, null, null, null, null, null, null, null,
				   null, null, null, null, null, null, null, null, null,
				   null, null, null, null, null, null, null, null, null,
				   null, null, 8, 4, null, 2, 7, null, null,
				   3, null, null, 9, 7, null, null, null, 8,
				   null, 7, null, null, 8, null, null, 9, null };
		*/
		/*
		Integer[] ints = { null, 1, null, null, null, null, null, 7, null,
				8, null, null, null, null, null, null, null, 4,
				   null, null, 4, 5, null, 7, 6, null, null,
				   null, null, 6, null, 2, null, 3, null, null,
				   null, null, null, 3, 5, 8, null, null, null,
				   null, null, 1, null, 9, null, 8, null, null,
				   null, null, 8, 4, null, 2, 7, null, null,
				   3, null, null, null, null, null, null, null, 8,
				   null, 7, null, null, null, null, null, 9, null };
		*/
		/*
		Integer[] ints = { 5, null, null, null, null, 6,
				   null, 4, null, null, 2, null,
				   null, 5, 4, 6, 3, null,
				   null, 1, 3, 2, 5, null,
				   null, 3, null, null, 6, null,
				   4, null, null, null, null, 3 };
		*/
		try {
			final Sudoku s = new Sudoku(Modalidade.CLASSIC, ints);
			
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						s.comecar();
					} catch (SudokuInsolucionavelException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
			/*
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(10000);
						s.parar();
						System.out.println("parou");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}).start();*/
			
			
			List<Integer[]> lista = null;
			while((lista = s.getUltimasRespostas()) != null) {
				for(Integer[] array : lista) {
					int itens = 0;
					for(Integer i : array) {
						System.out.print(i + " ");
						if(++itens % s.modalidade.colunas == 0)
							System.out.println();
					}
					System.out.println();
				}
				System.out.println(lista.size());
			}
				
			
			
		} catch (SudokuInicialException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
}
