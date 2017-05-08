
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class cli {
    
    public static void main(String args[]) throws IOException
    {
         int number, temp;
        //pobieranie danych od u¿ytkownika
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter any number");
        number = sc.nextInt();
       
        //wysy³anie danych do serwera
        Socket s = new Socket("127.0.0.1",1342); 
        PrintStream p = new PrintStream(s.getOutputStream());
        p.println(number);
        
        //odbieranie odpowiedzi od serwera
        Scanner sc1 = new Scanner(s.getInputStream());
        temp=sc1.nextInt();
        System.out.println(temp); 
    }
}
