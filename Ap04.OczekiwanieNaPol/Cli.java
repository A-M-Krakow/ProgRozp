import java.io.*; 
import java.net.*;

public class Cli {
    
    public static void main(String[] args) 
   	{
   		
   		try
   		{
   			Socket socket = new Socket ("127.0.0.1", 2345);	
   			pause(10000);
   			socket.close();

   				
   		}catch (IOException e) {};
   	
   	
   	
   		
   		
    }

	private static void pause(int ms) {
        try
        {
            Thread.sleep(ms);
        }catch (InterruptedException e) { }
    }
}
