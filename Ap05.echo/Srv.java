import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by anna on 08.05.2017.
 */
public class Srv {

    private static ServerSocket server;
    private static final int PORT = 2345;

    public static void main(String args[]){
        String linia;

        try
        {
            server = new ServerSocket(PORT);
            System.out.println("Serwer uruchomiony...");

            while (true)
            {
                Socket socket = server.accept();
                Scanner in = new Scanner (socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Prosty serwer ECHO, komenda /end konczy dzialaanie.");
                boolean koniec = false;

                while (!koniec)
                {
                    linia = in.nextLine();
                    if(linia.trim().toLowerCase().equals("/end")) {
                        koniec = true;
                    }
                    else {
                        out.println(linia);
                    }
                }
                socket.close();
            }

        }catch (IOException e) {
            System.out.println(e);
        }finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) { }
                }
            }
        }
    }

