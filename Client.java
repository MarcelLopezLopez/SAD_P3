import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

// Para ejectuar el cliente se debe hacer de la siguiente manera:
// >> java Client localhost 8080
public class Client {

    public static void main(String[] args) throws IOException {
        // args[0] fara que agafem el primer parametre que li pasem despres d'executar
        // el programa
        //host //port
        MySocket sc = new MySocket(args[0], Integer.parseInt(args[1]));

        BufferedReader kbd = new BufferedReader(new InputStreamReader(System.in));
        
        // Input threat (keyboard)
        new Thread(() -> {
            String line;
            try {
                while ((line = kbd.readLine()) != null) {
                    sc.printLine(line);
                    if(line.matches("exit")){
                        sc.close();
                        break;
                    }
                }
                sc.printLine("exit");
                sc.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();

        // Output threat (console)
        new Thread(() -> {
            String line;
            while ((line = sc.readLine()) != null) {
                if(line.matches("exit")){
                    break;
                }
                System.out.println(line);
            }
            System.out.println("Client Disconnected!!!");
                sc.close();
                System.exit(0);
        }).start();
    }
}
