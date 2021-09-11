import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class ClientProtocol {
    

    public void startClient(JFrame frame) throws Exception{
        try (var socket = new Socket("localhost", 7001)) {

            //THIS MAP STORES ALL THE DATA SENT TO THE SERVER
            Map<String, String> map = new HashMap<String, String>();



            JPanel panel = new JPanel();
            JLabel requestLabel = new JLabel();
            JTextField replyText = new JTextField(25);
            JButton sendButton = new JButton("SEND");

            //THIS LOOP RUNS FOR EVERY REQUEST THE SERVER SENDS
            while (true)
            {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String request = in.readUTF();

                //WHEN THE SERVER SENDS A CLOSE MESSAGE, WE REPLY WITH AN OK CONFIRMATION AND CLOSE THE CONNECTION
                if (request.equals("CLOSE"))
                {
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("OK");
                    out.flush();
                    socket.close();
                    System.out.println("\nThe connection with the server has closed successfully...");
                    return;
                }

                //WHEN THE SERVER SENDS AN ALL MESSAGE WE SEND THE HASHMAP WITH THE ACCUMULATED DATA
                if (request.equals("ALL")){
                    ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());
                    outObj.writeObject(map);
                    outObj.flush();
                    continue;
                }

                //WE PASS THE SERVERS REQUEST OVER TO THE USER AND COLLECT THE RESPONSE THROUGH THE COMMAND LINE

                ActionListener sendListener = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (e.getSource() == sendButton && !replyText.getText().isEmpty()) {
                                sendButton.setEnabled(false);
                                String reply = replyText.getText();
                                map.put(request, reply);
                                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                out.writeUTF(reply);
                                out.flush();
                                TimeUnit.SECONDS.sleep(2);
                                replyText.setText("");
                                sendButton.setEnabled(true);


                            }
                        }
                        catch (IOException | InterruptedException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                };

                requestLabel.setText("What is the "+request+"?");
                sendButton.addActionListener(sendListener);
                panel.removeAll();
                panel.add(requestLabel);
                panel.add(replyText);
                panel.add(sendButton);
                frame.getContentPane().add(panel);
                frame.setVisible(true);



                System.out.println("What is the "+request+"?");

                /*Scanner sc= new Scanner(System.in);
                String reply = sc.nextLine();
                map.put(request,reply);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(reply);
                out.flush();*/


            }
         
         }

        }


}
