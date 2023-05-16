import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//open multiple clients by enabling multiple inheritances from client edit configurations/modify options

public class Client implements Runnable{
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 9999);
            output = new PrintWriter(client.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler();
            Thread t = new Thread(inputHandler);
            t.start();

            String inputMsg;
            while((inputMsg= input.readLine()) != null){
                System.out.println(inputMsg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void shutdown() throws IOException {
        input.close();
        output.close();
        client.close();
        System.out.println("You have successfully exited");

    }

    class InputHandler implements Runnable{

        @Override
        public void run() {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                try {
                    String msg = inputReader.readLine();
                    if(msg.equals("/quit")){
                        output.println(msg);
                        inputReader.close();
                        shutdown();
                    }
                    else{
                        output.println(msg);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
