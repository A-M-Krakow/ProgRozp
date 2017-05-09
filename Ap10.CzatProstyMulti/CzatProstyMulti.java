import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by anna on 09.05.2017.
 */
public class CzatProstyMulti {
    private static ServerSocket server; // deklaracja zmiennej przechowującej socket na którym nasłuchuje serwer
    private static final  int PORT = 22; // definicja portu, na którym nasłuchuje serwer

    public static void main(String args[]){
        try
        {
          server = new ServerSocket(PORT); // definicja socketu serwera na odpowiednim porcie
          System.out.println("Czat serwer uruchomiony na porcie: " + PORT);

          while (true){
              Socket socket = server.accept(); // włączenie akceptowania nowego połączenia

              InetAddress addr = socket.getInetAddress(); // definicja zmiennej przechowującej adres połączonego klienta
              System.out.println("Połączenie z adresu: "+ addr.getHostName() + " [" + addr.getHostAddress() + "]");

              new CzatObsluga(socket).start(); // uruchomienie obsługi czatu dla klienta
          }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class CzatObsluga extends Thread {
    static Vector<CzatObsluga> czaty = new Vector<CzatObsluga>(); // definicja zmiennej przechowującej wszystkie czaty;

    private Socket socket; // deklaracja socketu dla połączenia z klientem
    private BufferedReader in; // deklaracja strumienia danych otrzymanych od klienta
    private PrintWriter out; // deklaracja strumienia danych wysyłanych do klienta
    private String nick; // deklaracja nazwy użytkownika

    public CzatObsluga(Socket socket) { // konstruktor obsługi nowego połączenia - tworzy się go podając socket)
        this.socket = socket;
    }

    private void wyslijDoWszystkich(String tekst) { // metoda wysyłająca dane do wszytkich obecnych
        for (CzatObsluga czat :czaty) { // dla wszystkich czatów z listy czatów
            synchronized(czaty) { // synchronizowane działanie (może być robione tylko przez jeden wątek na raz)
                if (czat != this)  // jeżeli czat nie jest bieżącym czatem
                    czat.out.println("<" + nick + ">" + tekst); //do wszystkich innych wysyłamy napisany tekst
            }
        }
    }

    private void info() { // metoda wyświetlająca informacje na temat czatu
        out.print("Witaj " + nick + ", aktualnie czatują: ");
        for (CzatObsluga czat : czaty) { // dla wszystkich czatów z listy czatów
            synchronized(czaty) { // synchronizowane działanie
                if (czat != this) // jeżeli czat nie jest bieżącym czatem
                    out.print(czat.nick + " "); // wyświetlamy nick jego użytkownika.
            }
        }
        out.println();
    }

    public void run() {
        String linia; // deklaracja napisu wpisanego przez użytkownika
        synchronized (czaty) { //synchronizowane działanie
            czaty.add(this); // dodanie bieżącego czatu do listy wszystkich czatów
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // definicja strumienia wejściowego
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true); // definicja strumienia wyjściowego

            out.println("Połączony z serwerem. Komenda /end kończy połączenie.");   // to wysyłamy do klienta
            out.println("Podaj swój nick: ");                                       // to też
            nick = in.readLine();                                                   // odbieramy od klienta nick
            System.out.println("Do czatu dołączył: " + nick);
            wyslijDoWszystkich("Pojawił się na czacie");                      // wysyłamy do wszystkich
            info();                                                                 // wyświetlamy klientowi info o czacie
            while (!(linia = in.readLine()).equalsIgnoreCase("/end")){ //dopóki klient nie wpisze /end
                wyslijDoWszystkich(linia);                                          // to co napisał wysyłamy do wszystkich
            }
            wyslijDoWszystkich("Opuścił czat");                              // jeżeli wpisał /end to opuszcza czat

            System.out.println("Czat opuścił: " + nick);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close(); // zamknięcie wejścia
                out.close(); // zamknięcie wyjścia
                socket.close(); // zamknięcie socketu
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                synchronized(czaty) {
                    czaty.removeElement(this); // usunięcie bieżącego czatu z listy czatów
                }
            }
        }
    }
}
