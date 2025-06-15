package files;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FilesServer extends Thread {
    private final int port;

    public FilesServer(int port) {
        this.port = port;
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
                    new FilesWorker(socket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Integer serverPort = 7953;
        FilesServer server = new FilesServer(serverPort);
        server.start();
    }
}
