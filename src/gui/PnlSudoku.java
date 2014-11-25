package gui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sudoku.Modalidade;
import sudoku.exceptions.SudokuInicialException;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import java.util.ArrayList;
import java.util.List;

class PnlSudoku extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8159068387726330543L;

	private Modalidade modalidade;
	
	private JTextField[] slots;
	
	public static final Font fontTipo1 = new Font("Serif", Font.PLAIN, 16);
	public static final Font fontTipo2 = new Font("Serif", Font.BOLD, 16);
	
	/**
	 * Create the panel.
	 */
	PnlSudoku() {
		updateSudoku(Modalidade.CLASSIC);
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 4));
		
	}
	
	void updateSudoku(Modalidade modalidade){
		this.modalidade = modalidade;
		slots = new JTextField[modalidade.linhas * modalidade.colunas];
		List<Component> componentes = new ArrayList<>();
		final int linha = modalidade.colunas + (modalidade.colunas / modalidade.cGrade) - 1;
		boolean yellow = true;
		int acasoCor = (Math.round(Math.random()) == 0 ? 1 : -1);
		for(int x = 0, y = 1, z = 1, l = 1, m = 1, n = 1; x < slots.length; x++, y++, z++) {
			
			JTextField tf = new JTextField();
			tf.setHorizontalAlignment(JTextField.CENTER);
			tf.setColumns(2);
			tf.setFont(fontTipo1);
			componentes.add(slots[x] = tf);
			
			
			
			if(y == modalidade.cGrade) {
				if(l != modalidade.colunas / modalidade.cGrade) { 
					componentes.add(new JLabel());
				}
				else					
					l = 0;
				
				l++;
				y = 0;
			}
			
			if(z == modalidade.colunas * modalidade.lGrade) {
				if(m != modalidade.linhas / modalidade.lGrade) {
					for(int x1 = 0; x1 < linha; x1++) {
						componentes.add(new JLabel());
					}
				} else {
					m = 0;
				}
				
				m++;
				z = 0;
			}
			
			if(yellow) {
				tf.setBackground(Color.LIGHT_GRAY);
			}
			
			if(n == modalidade.cGrade + acasoCor) {
				yellow = !yellow;
				n = 1;
			} else {
				n++;
			}
		}
		removeAll();
		setLayout(new GridLayout(0, linha));
		
		for(Component c : componentes) {
			add(c);
		}
		updateUI();
	}
	
	Integer[] getInicial() throws SudokuInicialException {
		Integer[] inicial = new Integer[modalidade.linhas * modalidade.colunas];
		for(int x = 0; x < inicial.length; x++) {
			if(slots[x].getText().equals("")) {
				inicial[x] = null;
			} else {
				try {
					inicial[x] = Integer.valueOf(slots[x].getText().trim());
				} catch(NumberFormatException e) {
					throw new SudokuInicialException();
				}
			}
		}
		
		return inicial;
	}
	
	void clean() {
		for(JTextField t : slots) {
			t.setText("");
		}
		slots[0].requestFocus();
	}
	
	void terminar() {
		for(JTextField t : slots) {
			t.setFont(fontTipo1);
			t.setEditable(true);
		}
		
		updateUI();
	}
	
	void prenderCampos(Integer[] inicial) {
		for(int x = 0; x < slots.length; x++) {
			if(inicial[x] != null) {
				slots[x].setFont(fontTipo2);
			}
			slots[x].setEditable(false);
		}
		
		updateUI();
	}
	
	void aplicarResolucao(Integer[] resolucao) {
		for(int x = 0; x < resolucao.length; x++) {
			slots[x].setText(resolucao[x].toString());
		}
		
	}
	
	void setarPuzzle(Integer[] inicial) {
		for(int x = 0; x < inicial.length; x++) {
			if(inicial[x] == null) {
				slots[x].setText("");
				continue;
			} else {
				slots[x].setText(inicial[x].toString());
			}
		}
	}
	
	Modalidade getModalidade() {
		return modalidade;
	}
}
