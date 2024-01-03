import java.io.IOException;
import java.security.cert.CRLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;

public class Server {
    // Port on s'executarà en servidor
    private static final int PORT = 8080;
    // Diccionari de clients pel seu nick i el seu socket
    private static Map<String, MySocket> clientDictionary = new ConcurrentHashMap<>();
    // Variable boolean per verificar que no hi hagi noms repetits
    public static boolean nomValid = false;
    public MySocket mySocket;
    public String name;

    public Server(String name, MySocket mySocket){
        this.mySocket = mySocket;
        this.name = name;
    }
    public static void main(String[] args) {

        MyServerSocket myServerSocket = null;

        try {
            myServerSocket = new MyServerSocket(PORT);

            while (true) {
                // Esperem la següent conexió del client
                MySocket client = myServerSocket.accept();
                // Demanem al client que introdueixi un nom i validem si ja existeix
                while (client != null){
                    handleClient(client);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (myServerSocket != null) {
                myServerSocket.close();
            }
        }
    }

    private static void handleClient(MySocket client) {
        new Thread(() -> {
            String name = null;

            try {
                while (!nomValid) {
                    client.printLine("Introdueixi el seu nom d'usuari: ");
                    String line = client.readLine();

                    if (clientDictionary.containsKey(line)) {
                        client.printLine("El nom d'usuari " + line + " ja està sent utilitzat");
                    } else {
                        clientDictionary.put(line, client);
                        name = line;
                        nomValid = true;

                        client.printLine("Hola " + name + " benvingut, t'has unit correctament al xat");
                        System.out.println(name + " s'ha unit al xat");
                    }
                }

                nomValid = false;

                while (true) {
                    String message = client.readLine();
                    if (message == null) {
                        break; // Si el cliente cierra la conexión
                    }

                    // Procesar el mensaje del cliente o realizar alguna acción
                    System.out.println(name + ": " + message);

                    // Puedes enviar mensajes de vuelta al cliente si es necesario
                    // client.printLine("Respuesta al cliente");
                }

            } finally {
                // Cerrar el socket del cliente y eliminarlo del diccionario al salir
                client.close();
                clientDictionary.remove(name);
            }
        }).start();
    }

}
