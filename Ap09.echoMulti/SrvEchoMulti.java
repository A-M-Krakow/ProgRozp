import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by anna on 09.05.2017.
 */
public class SrvEchoMulti {
    private static ServerSocket server;
    private static final int PORT = 22;

    public static void main (String args[]){
        int numerPolaczenia = 1;
        try
        {
            server = new ServerSocket(PORT);
            System.out.println("Serwer uruchomiony na porcie: " + PORT);

            while (true) {
                Socket socket = server.accept();
                InetAddress addr = socket.getInetAddress();
                System.out.println("Połączenie numer: " + numerPolaczenia + " z adresu: " + addr.getHostName()
                        + " [" + addr.getHostAddress() + "]");

                new Obsluga(socket, numerPolaczenia).start();
                numerPolaczenia++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Obsluga extends Thread {
    private Socket socket;
    private int numerPolaczenia;

    public Obsluga (Socket socket, int numerPolaczenia) {
        this.socket = socket;
        this.numerPolaczenia = numerPolaczenia;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("Serwer wita użytkownika! Komenta /end kończy połączenie!");

            boolean done = false;

            while (!done)
            {
                String linia = in.readLine();
                out.println("Połączenie: " + numerPolaczenia + " Echo: " + linia);

                if (linia.trim().toLowerCase().equals("/end")) {
                    done = true;
                }
            }
            System.out.println("Połączenie numer: " + numerPolaczenia + " zostało zakończone!");;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
