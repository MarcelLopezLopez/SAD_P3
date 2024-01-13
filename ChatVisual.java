import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class ChatVisual {
    static JTextPane input;
    static JButton button_send;
    static JTextField userTextField;
    static JFrame startChat;
    static JFrame principal;
    static JTextArea messages;
    ArrayList<String> users;
    JTextField textField;
    JList<String> userList;
    DefaultListModel<String> model;

    static MySocket socket;
    String username;
    String message_user;

    public ChatVisual() throws IOException{
        super();
        socket = new MySocket("localhost", 8080);
        users = new ArrayList<>();
        model = new DefaultListModel<String>();
        userList = new JList<String>(model);


    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChatVisual chat = new ChatVisual();
                    chat.iniciXat();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void iniciXat() {

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        startChat = new JFrame("Xat");
        startChat.getContentPane().setLayout(new BoxLayout(startChat.getContentPane(), BoxLayout.PAGE_AXIS));
        startChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an output JPanel and add a JList with the messages inside a
        // JScrollPane

        JPanel enterUsername = new JPanel();
        enterUsername.setLayout(new BoxLayout(enterUsername, BoxLayout.PAGE_AXIS));
        JLabel labelNewUser = new JLabel("Enter your username");
        userTextField = new JTextField(25);
        JButton button = new JButton("Create");
        button.addActionListener(new EnterServerButtonListener());


        enterUsername.add(labelNewUser);
        enterUsername.add(userTextField);
        enterUsername.add(button);
        enterUsername.setMaximumSize(
                new Dimension(enterUsername.getMaximumSize().width, enterUsername.getMinimumSize().height));

        // add panels to main frame
        startChat.add(enterUsername, BorderLayout.PAGE_END);

        // Display the window centered.
        // iniciXat.pack();
        startChat.setSize(400, 500);
        startChat.setLocationRelativeTo(null);
        startChat.setVisible(true);

    }

    public void mainScreen() {

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        principal = new JFrame("Xat");
        principal.getContentPane()
                .setLayout(new BoxLayout(principal.getContentPane(), BoxLayout.PAGE_AXIS));
        principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        principal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                socket.printLine("Exit " +username );
                updateUserList("Exit " + username);
            }
        }); 

            
        
        // Create an output JPanel and add a JTextArea(20, 30) inside a JScrollPane
        messages = new JTextArea(20,30);
        JPanel output = new JPanel();
        output.setLayout(new BoxLayout(output, BoxLayout.PAGE_AXIS));
        messages.setEditable(false);
        output.add(new JScrollPane(messages));


         // Create the list and put it in a scroll pane.
         JLabel usersLabel= new JLabel("Users available");
         output.add(usersLabel);
 
         // Create a list available users 

         userList.setBackground(new Color(0, 255, 0));
         JScrollPane scrollPane = new JScrollPane(userList);
         scrollPane.setMaximumSize(new Dimension(scrollPane.getMaximumSize().width, scrollPane.getMinimumSize().height));
         output.add(scrollPane);
         output.add(new JScrollPane(messages));
        
        // Create an input JPanel and add a JTextField(25) and a JButton
        input = new JTextPane();
        input.setLayout(new BoxLayout(input, BoxLayout.LINE_AXIS));
        textField = new JTextField(25);

        button_send = new JButton("Send");
        button_send.addActionListener(new sendMessageButtonListener());

        input.add(textField);
        input.add(button_send);
        input.setMaximumSize(new Dimension(input.getMaximumSize().width, input.getMinimumSize().height));

        // add panels to main frame
        principal.add(output);
        principal.add(input);

        // Display the window centered.
        // frame.pack();
        principal.setSize(400, 500);
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);

    }

    public class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            message_user = textField.getText();
            if (message_user.length() < 1) {
                // do nothing
            } else {
                System.out.println("Entra boto");
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
                }
                else{
                    updateUserList(username);
                    startChat.setVisible(false);
                    mainScreen();
                    startListenning();
                }
                
            } catch (Exception e) {
                showErrorMessage(
                        "Looks like the data you entered is incorrect, make sure the fields are in the correct format and that a server is listenning to the specified port");
            }

        }
    }


    private void startListenning() {
        new Thread() {
            public void run() {
                String line;
                try {
                    while ((line = socket.readLine()) != null) {
                        messages.append(line + " \n");
                        
                        if(line.contains("Exit")){
                            updateUserList(line);
                        }
                        else{
                            String[] parts = line.split(":");
                            updateUserList(parts[0]);
                        }
                    }
                } catch (Exception ex) {
                    socket.close();
                    System.exit(0);
                }
            }
        }.start();
    }

    public class EnterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            enterServer();
        }
    }

    public void updateUserList(String username) {
        if (username.contains("Exit")) {
            String name = username.substring(5);
            if (users.contains(name)) {
                users.remove(name);
                model.removeElement(name);
                System.out.println("Eliminem NOM" + name);
            }
        } else {
            String name = username.substring(0);
            if (!users.contains(name)) {
                users.add(name);
                model.addElement(name);
                
                for(int i = 0; i< users.size(); i++ ){
                    System.out.println(userList.getModel().getElementAt(i));
                }
                /*socket.println(name);*/
                System.out.println(name);
            }
        }
    }

}