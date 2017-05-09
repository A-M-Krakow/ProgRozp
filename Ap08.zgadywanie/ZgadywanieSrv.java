import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by anna on 09.05.2017.
 */
public class ZgadywanieSrv {
    private static ServerSocket server;
    private static final int PORT = 22;

    public static void main(String args[])
    {
        String przeslanaLiczba;
        Boolean zlaLiczba;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Serwer uruchomiony!");

            while (true)
            {
                Socket socket = server.accept();
                Random rand = new Random();
                int wylosowanaLiczba = rand.nextInt(100);
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Serwer zgadywanie! Komenda /pass kończy działanie.");
                boolean koniec = false;

                while (!koniec)
                {
                    zlaLiczba = false;
                    przeslanaLiczba = in.nextLine();
                    if (przeslanaLiczba.trim().toLowerCase().equals("/pass"))
                    {
                        koniec = true;
                    }
                    else {
                        try {
                            Integer.parseInt(przeslanaLiczba);
                             } catch (NumberFormatException e) {
                            out.println("Zła liczba!");
                            zlaLiczba = true;
                            }

                        if (!zlaLiczba) {
                            if (Integer.parseInt(przeslanaLiczba) < wylosowanaLiczba) {
                                out.println("Za mało!");
                            } else {
                                if (Integer.parseInt(przeslanaLiczba) > wylosowanaLiczba) {
                                    out.println("Za dużo!");
                                } else {
                                    out.println("Bingo!");
                                }

                            }

                        }
                    }

                }
                socket.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server !=null) {
                try {
                    server.close();
                } catch (IOException e) { }
            }
        }
    }
}
