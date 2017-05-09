package Ap11;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by anna on 09.05.2017.
 */

    public class ChatGUISrv extends JFrame {
    private JButton uruchom, zatrzymaj;
    private JPanel panel;
    private JTextField port;
    private JTextArea komunikaty;

    private int numerPortu = 22;
    private boolean uruchomiony = false;

    private Vector<Polaczenie> klienci = new Vector<Polaczenie>();

    public ChatGUISrv() {
        super("Czat serwer");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel(new FlowLayout());
        komunikaty = new JTextArea();
        komunikaty.setLineWrap(true);
        komunikaty.setEditable(false);

        port = new JTextField((new Integer(numerPortu)).toString(), 8);
        uruchom = new JButton("Uruchom");
        zatrzymaj = new JButton("Zatrzymaj");
        zatrzymaj.setEnabled(false);

        ObslugaZdarzen obsluga = new ObslugaZdarzen();

        uruchom.addActionListener(obsluga);
        zatrzymaj.addActionListener(obsluga);


        panel.add(new JLabel("Port: "));
        panel.add(port);
        panel.add(uruchom);
        panel.add(zatrzymaj);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(komunikaty), BorderLayout.CENTER);

        setVisible(true);
    }

    private class ObslugaZdarzen implements ActionListener {
        private Serwer srw;

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Uruchom")) {
                srw = new Serwer();
                srw.start();
                uruchomiony = true;
                uruchom.setEnabled (false);
                zatrzymaj.setEnabled(true);
                port.setEnabled(false);
                repaint();
            }
            if (e.getActionCommand().equals("Zatrzymaj")){
                srw.kill();
                uruchomiony = false;
                zatrzymaj.setEnabled(false);
                uruchom.setEnabled(true);
                repaint();
            }
        }
    }

    private class Serwer extends Thread {
        private ServerSocket server;

        public void kill() {
            try {
                server.close();

                for (Polaczenie klient : klienci) {
                    try {
                        klient.wyjscie.println("Serwer przestał działać!");
                        klient.socket.close();

                    } catch (IOException e) {
                    } ;
                }
                wyswietlKomunikat("Wszystkie Połączenia zostały zakonczone.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {
            try {
                server = new ServerSocket(new Integer(port.getText()));
                wyswietlKomunikat("Serwer uruchomiony na porcie: " + port.getText() + "\n");

                while (uruchomiony) {
                    Socket socket = server.accept();
                    wyswietlKomunikat("Nowe połączenie.\n");
                    new Polaczenie(socket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (server != null) server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                wyswietlKomunikat("Serwer zatrzymany \n");

        }
    }
    private class Polaczenie extends Thread {
        private BufferedReader wejscie;
        private PrintWriter wyjscie;

        private Socket socket;
        private String nick;

        private String linia;

        public Polaczenie(Socket socket) {
            this.socket = socket;

            synchronized (klienci) {
                klienci.add(this);
            }
        }

        public void wyslijDoWszystkich(String tekst) {
            for (Polaczenie klient : klienci) {
                synchronized (klienci) {
                    if (klient != this)
                        klient.wyjscie.println(tekst);
                }
            }
        }

        public void run() {
            try {
                wejscie = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                wyjscie = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                wyjscie.println("Połączony z serwerem. Komenda /end kończy połączenie.");
                wyjscie.println("Podaj nick: ");
                nick = wejscie.readLine();

                wyslijDoWszystkich("Użytkownik " + nick + " dołączył do czatu");

                while (uruchomiony && !(linia = wejscie.readLine()).equalsIgnoreCase("/end")) {

                    wyslijDoWszystkich("<" + nick + ">" + linia);
                }
                wyjscie.println("Żegnaj. \n");

                synchronized (klienci) {
                    klienci.remove(this);
                }
                wyslijDoWszystkich("Użytkownik " + nick + " opuścił czat.");
                wyswietlKomunikat("Połączenie zostało zakończone. \n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    wejscie.close();
                    wyjscie.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

        private void wyswietlKomunikat(String tekst) {
            komunikaty.append(tekst);
            komunikaty.setCaretPosition(komunikaty.getDocument().getLength());
        }
        public static void main(String args[]) {
            new ChatGUISrv();
        }
    }

