import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
//import javax.swing.text.JTextComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainScreen {

	private JFrame frame;
	private JTextField txtBusca;
	private JTextField txtNome;
	private JTextField txtTel;
	private JTextField txtEmail;
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/Agenda";
	private String user = "root";
	private String password = "Prisma2019";
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtBusca = new JTextField();
		txtBusca.setBounds(10, 11, 300, 20);
		frame.getContentPane().add(txtBusca);
		txtBusca.setColumns(10);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblId.setBounds(10, 42, 46, 14);
		frame.getContentPane().add(lblId);
		
		JLabel lblID = new JLabel("");
		lblID.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblID.setBounds(66, 42, 98, 14);
		frame.getContentPane().add(lblID);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNome.setBounds(10, 78, 46, 14);
		frame.getContentPane().add(lblNome);
		
		JLabel lblTelefone = new JLabel("Telefone:");
		lblTelefone.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTelefone.setBounds(10, 118, 68, 14);
		frame.getContentPane().add(lblTelefone);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(10, 161, 46, 14);
		frame.getContentPane().add(lblEmail);
		
		txtNome = new JTextField();
		txtNome.setBounds(66, 77, 300, 20);
		frame.getContentPane().add(txtNome);
		txtNome.setColumns(10);
		
		txtTel = new JTextField();
		txtTel.setBounds(78, 117, 288, 20);
		frame.getContentPane().add(txtTel);
		txtTel.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(66, 160, 300, 20);
		frame.getContentPane().add(txtEmail);
		txtEmail.setColumns(10);
		
		// Buscar pessoas no banco de dados
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(txtBusca.getText().length() > 0) {
					String[] resultado = buscarNome(txtBusca.getText());
					if(resultado[0] != null) {
						lblID.setText(resultado[0]);
						txtNome.setText(resultado[1]);
						txtTel.setText(resultado[2]);
						txtEmail.setText(resultado[3]);
					}
					else {
						JOptionPane.showMessageDialog(btnPesquisar, "Nenhum nome inicia com "+txtBusca.getText()+" em sua agenda");
						limparCampos();
						lblID.setText("");
					}
				}
				else {
					JOptionPane.showMessageDialog(btnPesquisar, "Informe o inicio do nome a ser buscado");
					limparCampos();
					lblID.setText("");
				}
			}
		});
		btnPesquisar.setBounds(320, 10, 89, 23);
		frame.getContentPane().add(btnPesquisar);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String nome = txtNome.getText();
				String telefone = txtTel.getText();
				String email = txtEmail.getText();
				
				int resultado = cadastrar(nome, telefone, email);
				System.out.println("Resultado: "+resultado);
				if(resultado == 1) {
					JOptionPane.showMessageDialog(btnCadastrar, "O registro do contato "+nome+" foi realizado com sucesso");
					limparCampos();
					lblID.setText("");
				}
				else {
					JOptionPane.showMessageDialog(btnCadastrar, "Ocorreram problemas ao salvar o registro");
				}
				
			}
		});
		btnCadastrar.setBounds(10, 214, 89, 23);
		frame.getContentPane().add(btnCadastrar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int confirma = JOptionPane.showConfirmDialog(btnExcluir, "Deseja realmente excluir o registro "+lblID.getText()+"?");
				
				if(confirma == JOptionPane.YES_OPTION) {
					if(excluir(lblID.getText())) {
						JOptionPane.showMessageDialog(btnExcluir, "Registro excluído com sucesso!");
						limparCampos();
						lblID.setText("");
					}
					else {
						JOptionPane.showMessageDialog(btnExcluir, "Foram encontrados problemas ao excluir o registro!");
					}
				}
				else if(confirma == JOptionPane.NO_OPTION || confirma == JOptionPane.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(btnExcluir, "Registro não excluído");
				}
				
				
			}
		});
		btnExcluir.setBounds(177, 214, 89, 23);
		frame.getContentPane().add(btnExcluir);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int confirma = JOptionPane.showConfirmDialog(btnEditar, "Deseja editar esse registro?");
				
				if(confirma == JOptionPane.YES_OPTION) {
					if(editar(lblID.getText())) {
						JOptionPane.showMessageDialog(btnEditar, "O registro foi editado com sucesso!");
						limparCampos();
						lblID.setText("");
					}
					else {
						JOptionPane.showMessageDialog(btnEditar, "Houve problemas ao editar o registro!");
					}
				}
				else if(confirma == JOptionPane.NO_OPTION || confirma == JOptionPane.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(btnEditar, "A edição do registro foi abortada");
				}
				
			}
		});
		btnEditar.setBounds(335, 214, 89, 23);
		frame.getContentPane().add(btnEditar);
	}
	//Funcionalidade do botão editar
		private void limparCampos() {
			txtNome.setText("");
			txtTel.setText("");
			txtEmail.setText("");
		}
		
		
	//Funcionalidade do botão pesquisar
	
	private String[] buscarNome(String nome) {
		String[] resultado = null; 
		
		try {
			Class.forName(driver);
			String query = "SELECT * FROM contato WHERE Nome LIKE '"+ nome + "%'";
			
			try {
				Connection con = DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				int colNum = rs.getMetaData().getColumnCount();
				resultado = new String[colNum];
				if(rs.next()) {
					for(int i=0; i<colNum; i++) {
						resultado[i] = rs.getString(i+1);
					}
				}
				
				st.close();
				con.close();
			} catch (SQLException e){
				System.out.println(e);
			}
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		}
		
		return resultado;
		
	}
	
	//Funcionalidade do botão cadastrar
	private int cadastrar(String nome, String telefone, String email) {
		int resultado = 0;
		try {
			Class.forName(driver);
			String query = "INSERT INTO `agenda`.`contato` (`Nome`, `Telefone`, `Email`) VALUES " 
			+ "('"+nome+"', '"+telefone+"', '"+email+"');";
			
			try {
				Connection con = DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				resultado = st.executeUpdate(query);
				st.close();
				con.close();
			} catch(SQLException e) {
				System.out.println(e);
			}
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		}
		
		return resultado;
	}
	
	//Funcionalidade do botão excluir
	private boolean excluir(String id) {
		boolean resultado = false;
		try {
			Class.forName(driver);
			String query = "DELETE FROM contato WHERE id = "+id; 
			
			try {
				Connection con = DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				st.executeUpdate(query);
				st.close();
				con.close();
				resultado = true;
			} catch(SQLException e) {
				System.out.println(e);
			}
			
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		}
		return resultado;
	}
	
	//Funcionalidade do botão editar
	private boolean editar(String id) {
		boolean resultado = false;
		try {
			Class.forName(driver);
			String query = "UPDATE contato SET Nome='"+txtNome.getText()+"', "+ "Telefone='"+txtTel.getText()+"', Email='"+txtEmail.getText()+"' WHERE id = " + id;
			
			try {
				
				Connection con = DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();
				st.executeUpdate(query);
				st.close();
				con.close();
				resultado = true;
				
			}catch(SQLException e) {
				System.out.println(e);
			} 
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		}
		
		return resultado;
	}
	
}
