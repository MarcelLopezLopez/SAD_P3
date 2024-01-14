import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.*;

public class ChatVisual {
    private static JTextPane input;
    private static JButton button_send;
    private static JTextField userTextField;
    private static JFrame startChat;
    private static JFrame principal;
    private static JTextArea messages;
    private DefaultListModel<String> model;
    private JList<String> userList;
    private static MySocket socket;
    private String username;
    private String message_user;
    private JTextField textField;

    public ChatVisual() throws IOException {
        super();
        socket = new MySocket("localhost", 8080);
        model = new DefaultListModel<>();
        userList = new JList<>(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ChatVisual chat = new ChatVisual();
                chat.iniciXat();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void iniciXat() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        startChat = new JFrame("Xat");
        startChat.getContentPane().setLayout(new BoxLayout(startChat.getContentPane(), BoxLayout.PAGE_AXIS));
        startChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel enterUsername = new JPanel();
        enterUsername.setLayout(new BoxLayout(enterUsername, BoxLayout.PAGE_AXIS));
        JLabel labelNewUser = new JLabel("Enter your username");
        userTextField = new JTextField(25);
        JButton button = new JButton("Create");
        button.addActionListener(new EnterServerButtonListener());

        enterUsername.add(labelNewUser);
        enterUsername.add(userTextField);
        enterUsername.add(button);

        // Agregamos el campo de texto textField
        textField = new JTextField(25);
        enterUsername.add(textField);

        startChat.add(enterUsername, BorderLayout.PAGE_END);

        startChat.setSize(400, 500);
        startChat.setLocationRelativeTo(null);
        startChat.setResizable(false);
        startChat.setVisible(true);
    }

    public void mainScreen() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        principal = new JFrame("Xat");
        principal.getContentPane().setLayout(new BoxLayout(principal.getContentPane(), BoxLayout.PAGE_AXIS));
        principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        principal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                socket.printLine("Exit " + username);
                updateUserList("Exit " + username);
            }
        });

        messages = new JTextArea(20, 30);
        JPanel output = new JPanel();
        output.setLayout(new BoxLayout(output, BoxLayout.PAGE_AXIS));
        messages.setEditable(false);
        output.add(new JScrollPane(messages));

        JLabel usersLabel = new JLabel("Users available");
        output.add(usersLabel);

        userList.setBackground(new Color(0, 255, 0));
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setMaximumSize(new Dimension(scrollPane.getMaximumSize().width, scrollPane.getMinimumSize().height));
        output.add(scrollPane);
        output.add(new JScrollPane(messages));

        input = new JTextPane();
        input.setLayout(new BoxLayout(input, BoxLayout.LINE_AXIS));

        button_send = new JButton("Send");
        button_send.addActionListener(new SendMessageButtonListener());

        input.add(textField);
        input.add(button_send);
        input.setMaximumSize(new Dimension(input.getMaximumSize().width, input.getMinimumSize().height));

        principal.add(output);
        principal.add(input);

        principal.setSize(400, 500);
        principal.setLocationRelativeTo(null);
        principal.setResizable(false);
        principal.setVisible(true);
    }

    private class SendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            message_user = textField.getText();
            if (message_user.length() >= 1) {
                textField.setText("");
                socket.printLine(message_user);
                messages.append(username + ": " + message_user + "\n");
            }
            textField.requestFocusInWindow();
        }
    }

    void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog", JOptionPane.ERROR_MESSAGE);
    }

    void enterServer() {
        username = userTextField.getText();

        if (username.length() < 1) {
            showErrorMessage("Please fill in the required fields");
        } else {
            try {
                socket.printLine(username);
                System.out.println("User " + username);
                String connected = socket.readLine();
                if (connected.equals("Exist")) {
                    System.out.println("Username already exists");
                    showErrorMessage("The user already exists");
                } else {
                    updateUserList(username);
                    startChat.setVisible(false);
                    mainScreen();
                    startListening();
                }

            } catch (Exception e) {
                showErrorMessage(
                        "Looks like the data you entered is incorrect, make sure the fields are in the correct format and that a server is listening to the specified port");
            }

        }
    }

    private void startListening() {
        new Thread(() -> {
            String line;
            try {
                while ((line = socket.readLine()) != null) {
                    messages.append(line + " \n");

                    if (line.contains("Exit")) {
                        updateUserList(line);
                    } else {
                        String[] parts = line.split(":");
                        updateUserList(parts[0]);
                    }
                }
            } catch (Exception ex) {
                socket.close();
                System.exit(0);
            }
        }).start();
    }

    private class EnterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            enterServer();
        }
    }

    private void updateUserList(String username) {
        if (username.contains("Exit")) {
            String name = username.substring(5);
            if (model.contains(name)) {
                model.removeElement(name);
                System.out.println("Eliminem NOM" + name);
            }
        } else {
            String name = username.substring(0);
            if (!model.contains(name)) {
                model.addElement(name);

                for (int i = 0; i < model.size(); i++) {
                    System.out.println(userList.getModel().getElementAt(i));
                }
                System.out.println(name);
            }
        }
    }
}
