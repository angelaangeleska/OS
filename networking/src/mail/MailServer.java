package mail;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MailServer extends Thread {
    private final int port;
    private final String filePath;

    public MailServer(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        System.out.println("Server starting...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
            System.out.println("SERVER: started!");
            System.out.println("SERVER: waiting for connections...");

            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    System.out.println("SERVER: new client");
                    new MailWorker(socket, filePath).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        MailServer server = new MailServer(5432, "src/mail/log.dat");
        server.start();
    }
}