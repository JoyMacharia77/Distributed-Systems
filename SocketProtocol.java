import java.io.*;
import java.net.*;
import java.util.Map;

public class SocketProtocol {
    public void startServer(){
        try{
            ServerSocket ss=new ServerSocket(7001);
            Socket s=ss.accept();//establishes connection

            //INDIVIDUAL REQUESTS TO THE CLIENT
            //THIS ARRAY STORES ALL THE QUERIES THAT CAN BE SENT TO THE CLIENT, MORE CAN BE ADDED IF REQUIRED
            String[] queries = {"Student Name","Student Number","Faculty","Course","Degree","Personal Message"};
            String[] replies = new String[queries.length];

            for (int i  = 0;i < queries.length;i++){
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                out.writeUTF(queries[i]);
                out.flush();
                DataInputStream in = new DataInputStream(s.getInputStream());
                replies[i] = in.readUTF();
                System.out.println("Response gotten for "+queries[i]+" is "+replies[i]);
            }

            //ALL REQUEST TO THE CLIENT,
            //SEND THE ALL COMMAND TO GET ALL THE DATA THE CLIENT HAS SENT AT THE SAME TIME IN FORM OF A HASHMAP
            System.out.println("\n\nNow printing out all data recieved from the client ... \n");
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeUTF("ALL");
            out.flush();
            ObjectInputStream inObj = new ObjectInputStream(s.getInputStream());
            Map all = (Map)inObj.readObject();

            //READING OUT ALL THE DATA COLLECTED
            for (int j = 0;j < queries.length;j++){
                System.out.println("The "+queries[j]+" is "+all.get(queries[j]));

            }


            //CLOSING THE CONNECTION TO THE CLIENT... WE SEND A CLOSE COMMAND AND CLOSE THE SERVER ON RECIEVING AN OK CONFIRMATION.
            while (true) {
                DataOutputStream close = new DataOutputStream(s.getOutputStream());
                DataInputStream conf = new DataInputStream(s.getInputStream());
                close.writeUTF("CLOSE");
                close.flush();
                String closeConfirmation = conf.readUTF();
                if (closeConfirmation.equals("OK") == true) {
                    ss.close();
                    break;
                }
            }

            System.out.println("\nThe connection with the client has closed successfully...");
            return;

        }catch(Exception e){System.out.println(e);}
    }


}  