
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Srv {

  
    public static void main(String[] args) 
    {
        try
        {
            ServerSocket server = new ServerSocket(2345);
            
            while(true)
            {
                System.out.println("Oczekuje na polaczenie...");
                Socket socket = server.accept();
                InetAddress  addr =  socket.getInetAddress();
                System.out.println("Po³¹czenie zadresu: " + addr.getHostName() + " [" + addr.getHostAddress() + "]");
                pause(10000);
                socket.close();
                
                
                
            }
        } catch (IOException e)
        {
            System.out.println(e);
        }
        
    }
    
    private static void pause(int ms) {
        try
        {
            Thread.sleep(ms);
        }catch (InterruptedException e) { }
    }
}
