import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client implements Runnable {

    private Server server;
    private PrintWriter outMessage;
    private Scanner inMessage;
    private static final String HOST = "localhost";
    private static final int PORT = 3443;
    private Socket socket = null;
    private static int clientsCount = 0;

    public Client(Socket socket, Server server) {
        try {
            clientsCount++;
            this.server = server;
            this.socket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                server.sendMessage("New member in room");
                server.sendMessage("Members in room " + clientsCount);
                break;
            }

            while (true) {
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    if (clientMessage.equalsIgnoreCase("endSession4556")) {
                        break;
                    }

                    Logger.getLogger("newLogger").info("Message: " + clientMessage);

                    server.sendMessage(clientMessage);
                }
            }

            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            try {
                this.clone();
            } catch (CloneNotSupportedException e) {
                //throw new RuntimeException(e);
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            outMessage.println(message);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        server.removeClient(this);
        clientsCount--;
        server.sendMessage("Members in room: " + clientsCount);
    }
}
