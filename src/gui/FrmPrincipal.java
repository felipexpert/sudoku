package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import sound.SFCoinEffect;
import sound.SFStarMan;
import sound.SFWorldMap1GrassLand;
import sound.SFYouAreDead;
import sudoku.Modalidade;
import sudoku.Sudoku;
import sudoku.exceptions.SudokuInicialException;
import sudoku.exceptions.SudokuInsolucionavelException;
import sudoku.lang.Linguagem;
import sudoku.lang.Text;
import sudoku.lang.languages.English;
import sudoku.lang.languages.Portugues;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class FrmPrincipal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2785538364957729887L;
	
	
	private JPanel contentPane;
	
	private Sudoku sudoku;
	private PnlSudoku pnlSudoku;
	private JLabel lblModalidade, lblOutput;
	private JTextArea txtAreaOutput;
	
	private JButton btnResolver, btnParar, btnLimpar;
	private JMenu menuModalidades;
	private List<JMenuItem> menuItens;
	

	private NumberFormat nf = NumberFormat.getNumberInstance();
	private JMenu menuLinguas;
	private JMenuItem menuItemPortugues;
	private JMenuItem menuItemEnglish;
	private JLabel lblContato;
	
	/**
	 * Create the frame.
	 */
	public FrmPrincipal() {
		super("Ultra Power Sudoku Solver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuModalidades = new JMenu();
		menuBar.add(menuModalidades);
		
		menuLinguas = new JMenu();
		menuBar.add(menuLinguas);
		
		menuItemPortugues = new JMenuItem("Portugu�s");
		menuItemPortugues.addActionListener(new MudaLingua());
		menuLinguas.add(menuItemPortugues);
		
		menuItemEnglish = new JMenuItem("English");
		menuItemEnglish.addActionListener(new MudaLingua());
		menuLinguas.add(menuItemEnglish);
		
		lblContato = new JLabel();
		menuBar.add(lblContato);
		
		//mntmNewMenuItem = new JMenuItem("New menu item");
		//menuModalidades.add(menuItens);
		
		menuItens = new ArrayList<>();
		
		for(int x = 0; x < Modalidade.values().length; x++) {
			JMenuItem item = new JMenuItem(Modalidade.values()[x].toString());
			item.addActionListener(new AlteraModalidade());
			menuItens.add(x, item);
			//menu - aqui
			menuModalidades.add(menuItens.get(x));
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		
		JPanel pnlCentro = new JPanel();
		pnlCentro.setLayout(new GridLayout(2, 1));
		
		JPanel pnlSudokuInfo = new JPanel();
		JScrollPane scroll = new JScrollPane(pnlSudokuInfo);
		pnlSudokuInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		pnlSudoku = new PnlSudoku();
		pnlSudokuInfo.add(pnlSudoku);
		
		btnResolver = new JButton();
		btnResolver.addActionListener(new ComecarSudoku());
		
		btnLimpar = new JButton();
		btnLimpar.addActionListener(new Limpar());
		
		
		btnParar = new JButton();
		btnParar.addActionListener(new PararSudoku());
		btnParar.setVisible(false);
		
		pnlSudokuInfo.add(btnResolver);
		pnlSudokuInfo.add(btnParar);
		pnlSudokuInfo.add(btnLimpar);
		
		Font font = new Font("Serif", Font.BOLD, 16);
		JPanel pnlOutput = new JPanel();
		pnlOutput.setLayout(new BorderLayout());
		//JScrollPane scroll2 = new JScrollPane(pnlOutput);
		lblOutput = new JLabel();
		lblOutput.setFont(font);
		//JPanel pnlOutputInfo = new JPanel();
		
		pnlOutput.add(BorderLayout.NORTH, lblOutput);
		//pnlOutput.add(BorderLayout.SOUTH, btnParar);
		txtAreaOutput = new JTextArea();
		txtAreaOutput.setEditable(false);
		JScrollPane scroll2 = new JScrollPane(txtAreaOutput);
		pnlOutput.add(BorderLayout.CENTER, scroll2);
		pnlCentro.add(scroll);
		pnlCentro.add(pnlOutput);
		
		getContentPane().add(BorderLayout.CENTER, pnlCentro);
		
		lblModalidade = new JLabel();
		lblModalidade.setFont(font);
		
		getContentPane().add(BorderLayout.NORTH, lblModalidade);
		
		updateLanguage();
		
		SFWorldMap1GrassLand.play();
		
		setVisible(true);
	}
	
	private void updateLanguage() {
		
		for(int x = 0; x < Modalidade.values().length; x++)
			menuItens.get(x).setText(Modalidade.values()[x].toString());
					
		menuModalidades.setText(Linguagem.word(Text.MODALIDADES));
		menuLinguas.setText(Linguagem.word(Text.LINGUAGENS));
		btnResolver.setText(Linguagem.word(Text.RESOLVER));
		btnLimpar.setText(Linguagem.word(Text.LIMPAR));
		btnParar.setText(Linguagem.word(Text.PARAR));
		lblOutput.setText(Linguagem.word(Text.OUTPUT));
		lblModalidade.setText(pnlSudoku.getModalidade().toString());
		lblContato.setText("           " + Linguagem.word(Text.CONTATO) + " " + "felipe.miquilini@gmail.com");
	}
	
	private class PararSudoku implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			sudoku.parar();
			
			btnParar.setText(Linguagem.word(Text.PARANDO));
			
		}
		
	}
	
	private class ComecarSudoku implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e){
			try {
				SFWorldMap1GrassLand.stop();
				lblOutput.setText(Linguagem.word(Text.OUTPUT_PROCESSANDO));
				sudoku = new Sudoku(pnlSudoku.getModalidade(), pnlSudoku.getInicial());
				SFStarMan.play();
				
				pnlSudoku.prenderCampos(pnlSudoku.getInicial());
				
				menuModalidades.setEnabled(false);
				menuLinguas.setEnabled(false);
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							sudoku.comecar();
							
						} catch (SudokuInsolucionavelException e) {
							SFStarMan.stop();
							SFYouAreDead.play();
							JOptionPane.showMessageDialog(FrmPrincipal.this, e.getMessage(), Linguagem.word(Text.EXCEPTION), JOptionPane.ERROR_MESSAGE);
							terminar(sudoku.getRespostaEncontradasAteOMomento());
							SFWorldMap1GrassLand.play();
						}
						
					}
				});
				
				t.start();
				t.setPriority(7);
				
				txtAreaOutput.setText("");
				txtAreaOutput.setFont(PnlSudoku.fontTipo1);
				
				btnParar.setVisible(true);			
				btnResolver.setVisible(false);
				btnLimpar.setVisible(false);
				
				contentPane.updateUI();
				Thread t2 = new Thread(new Runnable() {
					
					@Override
					public void run() {
						List<Integer[]> lista = null;
						while((lista = sudoku.getUltimasRespostas()) != null) {
							
							for(Integer[] array : lista) {
								
								int itens = 0;
								for(Integer i : array) {
									txtAreaOutput.append(i + " ");
									
									if(++itens % pnlSudoku.getModalidade().colunas == 0) {
										txtAreaOutput.append("   ");
									}
									
								}
								txtAreaOutput.append("\n");
								
								
								txtAreaOutput.setCaretPosition(txtAreaOutput.getDocument().getLength());
							}
							

							pnlSudoku.aplicarResolucao(lista.get(lista.size() - 1));
							lblOutput.setText(Linguagem.word(Text.SOLUCOES_ENCONTRADAS) + ": " + nf.format(sudoku.getRespostaEncontradasAteOMomento()));
							SFCoinEffect.play();
						}
						
						SFStarMan.stop();
						terminar(sudoku.getRespostaEncontradasAteOMomento());
						SFWorldMap1GrassLand.play();
						
					}
				});
				t2.start();
				t2.setPriority(9);
				
				
			} catch (SudokuInicialException e1) {
				SFYouAreDead.play();
				JOptionPane.showMessageDialog(FrmPrincipal.this, e1.getMessage(), Linguagem.word(Text.EXCEPTION), JOptionPane.ERROR_MESSAGE);
				SFWorldMap1GrassLand.play();
			}	
		}
			
	}
	
	private void terminar(long totalRespostas) {
		lblOutput.setText(Linguagem.word(Text.TOTAL_RESP_ENCONTRADAS_ULTIMO_PUZZLE) + ": " + nf.format(totalRespostas));
		
		btnParar.setVisible(false);			
		btnResolver.setVisible(true);
		btnLimpar.setVisible(true);
		
		btnParar.setText(Linguagem.word(Text.PARAR));
		
		pnlSudoku.terminar();
		
		//pnlSudoku.setarPuzzle(inicial);
		
		menuModalidades.setEnabled(true);
		menuLinguas.setEnabled(true);
		
		contentPane.updateUI();
	}
	
	private class Limpar implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			pnlSudoku.clean();
			
		}
		
	}
	
	private class AlteraModalidade implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			
			int index = menuItens.indexOf(o);
			
			Modalidade modalidade = Modalidade.values()[index];
			
			lblModalidade.setText(modalidade.toString());
			
			pnlSudoku.updateSudoku(modalidade);
			
			contentPane.updateUI();
			
		}
		
	}
	
	private class MudaLingua implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == menuItemPortugues) {
				Portugues.getInstance().setLinguagem();
			} else {
				English.getInstance().setLinguagem();
			}
			updateLanguage();
		}
		
	}
}
