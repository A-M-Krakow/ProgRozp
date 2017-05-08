import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by anna on 08.05.2017.
 */
public class CytatKlient {

    static private Socket socket;
    static private final int PORT = 2345;
    static private final String SERVER = "localhost";

    public static void main(String args[]){
        Scanner in = null;

        try {
            System.out.println("Łączę się z serwerem na porcie: " + PORT);
            socket = new Socket (SERVER, PORT);
            in = new Scanner(socket.getInputStream());
        }catch (UnknownHostException e) {return;}
         catch (IOException e) {return;}

         System.out.println(in.nextLine());
         System.out.println("Połączenie zostało zakończone!");
         try {
             socket.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
}
