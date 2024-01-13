import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Para ejectuar el servidor se debe hacer de la siguiente manera:
// >> java Server 

public class Server {
    // Port on s'executarà en servidor
    private static final int PORT = 8080;
    // Diccionari de clients pel seu nick i el seu socket
    private static Map<String, MySocket> clientDictionary = new ConcurrentHashMap<>();
    public MySocket mySocket;

    public static void main(String[] args) throws IOException {

        MyServerSocket myServerSocket = null;

        myServerSocket = new MyServerSocket(PORT);
        System.out.println("Server STARTED!!!");

        while (true) {
            // Esperem la següent conexió del client
            MySocket client = myServerSocket.accept();
            client.printLine("Connected to Server!!!");

            new Thread() {
                public void run() {
                    client.printLine("Introduce your UserName: ");
                    String name = client.readLine();
                    client.printLine("Hi " + name + ", you are now in chat!");
                    clientDictionary.put(name, client);
                    String text;
                    while ((text = client.readLine()) != null) {
                        broadcast(text, name);
                        System.out.println(name + " says: " + text);

                    }
                    System.out.println(name + " has left the chat");
                    clientDictionary.remove(name);
                    client.close();
                }
            }.start();
        }
    }

    public static void broadcast(String message, String name) {
        MySocket client = clientDictionary.get(name);

        for (Map.Entry<String, MySocket> entry : clientDictionary.entrySet()) {
            String actualUser = entry.getKey();
            MySocket socketActual = entry.getValue();
            if (actualUser != name) {
                socketActual.printLine(name + "> " + message);
            }
        }
    }
}
