import java.net.ServerSocket;
import java.io.IOException;

public class MyServerSocket extends ServerSocket {

    public ServerSocket ss;
    public boolean connect;

    // Crea un socket de servidor, vinculado al puerto especificado.
    public MyServerSocket(int port) throws IOException {
        this.ss = new ServerSocket(port);
        this.connect = true;
    }

    // Retorna el port on es troba el servidor
    public int getLocalPort() {
        return ss.getLocalPort();
    }

    // Retorna true si hi ha algú conectat al servidor
    public boolean isBound() {
        return connect;
    }

    // Retorn true si el servidor esta tancat
    public boolean isClosed() {
        return !connect;
    }

    // Escucha si se realiza una conexión a este socket y la acepta
    public MySocket accept() {
        try {
            if(connect){
            return new MySocket(ss.accept());
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Tanquem el socket
    public void close() {
        try {
            connect = false;
            if (this.ss != null && !this.ss.isClosed()) {
                ss.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
