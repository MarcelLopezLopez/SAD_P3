import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException {
        // args[0] fara que agafem el primer parametre que li pasem despres d'executar
        // el programa
        // nick //host //port
        MySocket sc = new MySocket(args[0], args[1], Integer.parseInt(args[2]));
        
        // Input threat (keyboard)
        new Thread(() -> {
            BufferedReader kbd = new BufferedReader(new InputStreamReader(System.in));
            String line;
            try {
                while ((line = kbd.readLine()) != null) {
                    // Ponemos el texto en rojo
                    sc.printLine("\u001b[32m");
                    // Ponemos el texto en negrita
                    sc.printLine("\u001b[1m");
                    // Escribimos el nombre del usuario
                    sc.printLine(sc.getNick());
                    // Volvemos a los valores predeterminados
                    sc.printLine("\u001b[0m");
                    sc.printLine(line);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();

        // Output threat (console)
        new Thread(() -> {
            String line;
            while ((line = sc.readLine()) != null) {
                System.out.println(line);
            }
        }).start();
    }
}

