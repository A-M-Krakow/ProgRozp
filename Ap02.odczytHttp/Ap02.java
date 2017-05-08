import java.io.*; 
import java.net.*;

public class Ap02 {

      

    public static void main( String[] args ) {
    {
       String adres = "uek.krakow.pl";
       String sciezka = "/";
       
       System.out.println("Pobieram dane ze strony: " + adres);
       
       try
       {
       	// Ustanowienie polaczenia z serwerem (port 80)
       	Socket socket = new Socket(adres, 80);
       	
       	// Utworzenie strumienia danych wejœciowych i wyjœciowych.
       	PrintStream out = new PrintStream(socket.getOutputStream());
       	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       	
       	// wysy³anie zapytania (zgodnie z protoko³em HTTP)
       	out.print("GET" + sciezka + "HTTP/1.0" + "\r\n");
       	out.print("Host: " + adres + "\r\n");
       	out.print("\r\n");
       	out.flush();
       	
       	//odczyt danych z serwera
       	
       	String linia = in.readLine();
       	while(linia!=null)
       	{
       		System.out.println(linia);
       		linia = in.readLine();
       	}
       
       //Zamkniecie strumieni
       in.close();
       out.close();
       socket.close();
       }
      	catch(Exception e) {
      		System.out.println("B³¹d");
      	}
       
       }
    }
}

