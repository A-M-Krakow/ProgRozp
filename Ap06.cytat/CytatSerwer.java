import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by anna on 08.05.2017.
 */
public class CytatSerwer {
    private static ServerSocket server;
    private static final int PORT = 2345;

    public static void main (String args[]){

        String zbiorCytatow[] = {"Ciekawy cytat 1", "Ciekawy cytat 2", "Ciekawy cytat 3", "Ciekawy cytat 4"};



        try
        {
            server = new ServerSocket(PORT);
            System.out.println("Serwer uruchomiony");

            while(true)
            {
                Socket socket = server.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                Random rand = new Random();
                int wylosowanaLiczba = rand.nextInt(zbiorCytatow.length);
                String wylosowanyCytat = zbiorCytatow[wylosowanaLiczba];
                out.println(wylosowanyCytat);
                socket.close();
            }
        }catch (IOException e) {System.out.println(e);}
        finally {
            if (server !=null) {
                try  {server.close();
            }catch (IOException e) {} }

        }


    }
}
