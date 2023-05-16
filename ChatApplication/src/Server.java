import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private ArrayList<ConnectionHandler> connections;
    private ExecutorService threadPool;

    public Server(){
        connections = new ArrayList<>();
    }
    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(9999);
            threadPool = Executors.newCachedThreadPool();
            System.out.println("Server is running now");
            while (true) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                threadPool.execute(handler);

            }

        } catch (IOException e) {
            // TODO: handle
        }

    }

    public void broadcast(String msg){
        for (ConnectionHandler ch : connections){
            if (ch != null){
                ch.sendMsg(msg);
            }
        }
    }
    public void personalMsg(String Msg, String name, String ownName){
        for (int i=0; i<connections.size(); i++){
            if(connections.get(i).name.equalsIgnoreCase(name) || connections.get(i).name.equalsIgnoreCase(ownName)){
                connections.get(i).sendMsg(Msg);
            }
        }

    }

    class ConnectionHandler implements Runnable{

        private Socket client;
        private BufferedReader input;
        private PrintWriter output;
        private String name;

        public ConnectionHandler(Socket client){
            this.client = client;

        }

        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                output = new PrintWriter(client.getOutputStream(), true);
                output.println("Please enter your name: ");
                name= input.readLine();
                System.out.println(name+ " connected!");
                broadcast(name + " joined the chat!");

                String msg;
                while((msg= input.readLine())!=null){
                    if(msg.startsWith("/name ")){
                        String[] msgsplit = msg.split(" ", 2);
                        if(msgsplit.length==2){
                            broadcast(name+ " renamed to "+msgsplit[1]);
                            System.out.println(name+ " renamed to "+msgsplit[1]);
                            name= msgsplit[1];
                            output.println("Successfully changed name to "+name);
                        }
                        else output.println("Invalid naming convention!");
                    }
                    else if(msg.startsWith("/quit")){
                        broadcast(name+" left the chat");
                        shutdown();
                    }
                    else if(msg.startsWith("/personal ")){
                        String[] msgsplit = msg.split(" ");
                        if(msgsplit.length>2){
                            msg=msg.replaceAll("/personal ","");
                            msg=msg.replaceAll(msgsplit[1],"");
                            personalMsg(name+": "+msg, msgsplit[1], name);
                        }
                    }
                    else{
                        broadcast(name + ": "+msg);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
        public void sendMsg(String msg){
            output.println(msg);

    }
        public void shutdown() throws IOException {
            input.close();
            output.close();
            client.close();

        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();

    }
}
