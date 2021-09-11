import javax.swing.*;

public class SocketClient {
    

    public static void main(String args[])throws Exception{
        JFrame frame = new JFrame("Client GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,200);
        frame.setVisible(true);

        ClientProtocol clientProtocol = new ClientProtocol();
        clientProtocol.startClient(frame);
    
    }

}

