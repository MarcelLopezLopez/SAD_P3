import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MySocket extends Socket {

    public Socket sc;
    // Variable per llegir
    public BufferedReader read;
    // Variable per escriure
    public PrintWriter write;
    // Variable per guardar el nom del socket
    public String nick;
    // Varibale que guarda el port al que es conecta el socket
    int port;

    // Constructor basic que crea un socket per defecte sense conectar
    public MySocket(Socket socket) throws IOException {
        try {
            this.sc = socket;
            this.read = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            this.write = new PrintWriter(new OutputStreamWriter(sc.getOutputStream()));
        } catch (IOException ex) {
            throw new IOException("Error al inicializar MySocket", ex);
        }

    }

    // Constructor que crea un socket amb un nick i definint a qui es vol conectar
    public MySocket(String host, int port) throws IOException {
        try {
            this.port = port;
            sc = new Socket(host, port);
            this.read = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            this.write = new PrintWriter(new OutputStreamWriter(sc.getOutputStream()));
        } catch (IOException ex) {
            throw new IOException("Error al inicializar MySocket", ex);
        }
    }

    public Socket getSocket(){
        return this.sc;
    }

    // Fucnió per obtenir el nom del socket
    public String getNick(){
        return nick;
    }

    // Funció per escollir el nom del socket
    public void setNick(String nick){
        this.nick = nick;
    }

    // Funció que retorna el port al que esta conectat el socket
    public int getPort(){
        return this.port;
    }

    // Funció que llegirá una linea de caràcters
    public String readLine() {
        String line = null;
        try {
            line = read.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return line;
    }

    // Lee el siguiente int, sino devuelve null
    public int readInt() {
        return Integer.parseInt(readLine());
    }

    // Lee el siguiente double, sino devuelve null
    public Double readDOuble() {
        return Double.parseDouble(readLine());
    }

    // Lee el siguiente boolean, sino devuelve null
    public Boolean readBool() {
        return Boolean.parseBoolean(readLine());
    }

    // Lee el siguiente short, sino devuleve null
    public Short readSHort() {
        return Short.parseShort(readLine());
    }

    // Lee el siguiente long, sino devuleve null
    public Long readLong() {
        return Long.parseLong(readLine());
    }

    // Lee el siguiente Byte, sino devuelve null
    public Byte readByte() {
        return Byte.parseByte(readLine());
    }

    // Funcio per escriure una linea
    public void printLine(String line) {
        // Printegem la linea
        write.println(line);
        // Netejem el corrent
        write.flush();
    }

    // Funcio per escriure un int
    public void printInt(int num) {
        write.println(num);
        write.flush();
    }

    // Funcio per escriure un double
    public void printDouble(Double dob) {
        write.println(dob);
        write.flush();
    }

    // Funcio per escriure un boolean
    public void printBool(Boolean bool) {
        write.println(bool);
        write.flush();
    }

    // Funcio per escriure un short
    public void printShort(Short curt) {
        write.println(curt);
        write.flush();
    }

    // Funcio per escriure un long
    public void printLong(Long llarg) {
        write.println(llarg);
        write.flush();
    }

    // Funcio per escriure un byte
    public void printByte(Byte byt) {
        write.println(byt);
        write.flush();
    }

    // Funció per tancar el socket
    public void close() {
        try {
            write.close();
            read.close();
            sc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}