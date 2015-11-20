package SalaXTI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
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

		this.nome = nome;

		Font fonte = new Font("Serif", Font.PLAIN, 16);

		textoParaEnviar = new JTextField();
		textoParaEnviar.setLocale(new Locale("pt","BR"));
		textoParaEnviar.setFont(fonte);
		textoParaEnviar.requestFocus();

		JButton botao = new JButton("Enviar");
		botao.setFont(fonte);
		botao.addActionListener(new EnviarListener());

		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, textoParaEnviar);
		envio.add(BorderLayout.EAST, botao);
		
		textoREcebido = new JTextArea();
		Colar: textoREcebido.setLineWrap(true);
		
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
			socket = new Socket("192.168.0.102", 5000);
			escritor = new PrintWriter(socket.getOutputStream());
			leitor = new Scanner(socket.getInputStream());
			new Thread(new EscutaServidor()).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Locale.setDefault(new Locale("pt","BR"));
		new ChatCliente("Léo");
		//new ChatCliente("Jesus");

	}

}
