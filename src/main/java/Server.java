import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Server {

    private static final int PORT = 3443;
    private List<Client> clients = new ArrayList<>();

    public Server() {
        Socket socket = null;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            Logger.getLogger("newLogger").info("Server started\n");

            while (true) {
                socket = serverSocket.accept();

                Client client = new Client(socket, this);
                clients.add(client);

                new Thread(client).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
                serverSocket.close();
                Logger.getLogger("newLogger").info("Server closed\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                Logger.getLogger("newLogger").info("Null Point\n");
                ex.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        for (Client client : clients) {
            client.sendMessage(message);// TODO
        }
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

}
