
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Ap03 {


    public static void main(String[] args) 
    {
        Socket socket;
        String host = "192.168.1.1";
        for (int i = 0; i<1024; i++)
        {
            try
            {
                socket = new Socket(host, i);
                System.out.println("Znalazlem serwer na porcie " + i + " komputera: " + host);
                socket.close();
           } catch (UnknownHostException e) 
           	{ System.err.println(e); 
           		break; } 
           catch (IOException e) { }

            
        }
    }
    
}
