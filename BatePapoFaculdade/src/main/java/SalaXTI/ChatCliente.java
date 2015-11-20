package SalaXTI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ChatCliente extends JFrame {

	JTextField textoParaEnviar;
	Socket socket;
	PrintWriter escritor;
	String nome;
	JTextArea textoREcebido;
	Scanner leitor;

	private class EscutaServidor implements Runnable {

		public void run() {
			try {
				String texto;
				while ((texto = leitor.nextLine()) != null) {
					textoREcebido.append(texto + "\n");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}
	
	

	public ChatCliente(String nome) {
		super(nome);
		
		/* Nome agora recebe a String digitada no JTextField do JOptionPane*/
		this.nome = JOptionPane.showInputDialog(null, "Digite seu nome!");

		Font fonte = new Font("Serif", Font.PLAIN, 16);

		textoParaEnviar = new JTextField();
		textoParaEnviar.setLocale(new Locale("pt","BR"));
		textoParaEnviar.setFont(fonte);
		textoParaEnviar.requestFocus();
		textoParaEnviar.addKeyListener(new EnviarKeyListener());

		JButton botao = new JButton("Enviar");
		botao.setFont(fonte);
		botao.addActionListener(new EnviarListener());

		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, textoParaEnviar);
		envio.add(BorderLayout.EAST, botao);
		
		textoREcebido = new JTextArea();
		textoREcebido.setLineWrap(true);
		
		textoREcebido.setLocale(new Locale("pt","BR"));
		textoREcebido.setFont(fonte);
		JScrollPane scroll = new JScrollPane(textoREcebido);
		scroll.setLocale(new Locale("pt","BR"));

		getContentPane().add(BorderLayout.CENTER, scroll);
		getContentPane().add(BorderLayout.SOUTH, envio);
		configurarRede();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
	}
	
	/**
	 * Essa EnviarKeyListener inner class foi criada para o KeyListener.
	 * Esse Listener faz com que as messagem possam ser enviadas a partir da tecla enter.
	 * @author Kelvy
	 *
	 */
	
	public class EnviarKeyListener implements KeyListener{

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_ENTER) {
				escritor.println(nome + " : " + textoParaEnviar.getText());
				escritor.flush();
				textoParaEnviar.setText(" ");
				textoParaEnviar.requestFocus();
			}
			
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private class EnviarListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			escritor.println(nome + " : " + textoParaEnviar.getText());
			escritor.flush();
			textoParaEnviar.setText(" ");
			textoParaEnviar.requestFocus();
		}

	}

	private void configurarRede() {
		try {
			socket = new Socket("127.0.0.1", 5000);
			escritor = new PrintWriter(socket.getOutputStream());
			leitor = new Scanner(socket.getInputStream());
			new Thread(new EscutaServidor()).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		new ChatCliente("");
		
	}
	
}
