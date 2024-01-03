import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ChatVisual extends JFrame implements ActionListener {

    private JButton sendButton;
    private JTextField textField;
    private DefaultListModel<String> conversationModel;
    private JList<String> conversationList;

    public ChatVisual() {
        // Llamamos a un metodo para configurar la interfaz del usuario
        setupUI();
    }

    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setTitle("Chat");

        // Configuración de la entrada de texto
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(405, 50));

        JLabel title = new JLabel("Chat");

        // COnfiguració del panel de encapçalat
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(500, 50));
        header.setLayout(new BorderLayout());
        header.add(title, BorderLayout.CENTER);

        // Configuració del model i la llista de converses
        conversationModel = new DefaultListModel<>();
        conversationList = new JList<>(conversationModel);
        JScrollPane scrollPane = new JScrollPane(conversationList);

        // Configuració del panel d'entrada de text i el botó
        JPanel text = new JPanel();
        text.setPreferredSize(new Dimension(500, 50));
        text.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        sendButton = new JButton("Enviar");
        sendButton.addActionListener(this);
        sendButton.setPreferredSize(new Dimension(80, 50));

        text.add(textField);
        text.add(sendButton);

        // Agregar components al JFrame
        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(text, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String message = textField.getText();
            if (!message.isEmpty()) {
                appendMessage("Yo: " + message);
                // Podemos añadir mensajes para enviar al servidor o clientes
            }
            textField.setText("");
        }
    }

    private void appendMessage(String message) {
        conversationModel.addElement(message);
    }

}
