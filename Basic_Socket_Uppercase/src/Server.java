import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(22222);
        System.out.println("Server Started");

        while (true) {
            //open sockets to connect with clients
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            try {
                //reading from client
                Object cMsg = input.readObject();
                System.out.println("From Client: " + (String) cMsg);

                String serverMsg = (String) cMsg;
                serverMsg = serverMsg.toUpperCase();

                //sending to client
                output.writeObject(serverMsg);

            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }
}
