import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class Cliente {
	
		public static void main (String args[]){
			Socket s = null;
			PrintStream ps = null;
		try{
			String x = "Teste 123 edgar raivoso";
			s = new Socket("127.0.0.1",7000);
			ps = new PrintStream(s.getOutputStream());
			ps.println(x);
			
			s.close();
		}
		catch (IOException e) {
			e.printStackTrace();
	}
	
	}
	}


