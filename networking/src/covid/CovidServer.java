package covid;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CovidServer extends Thread {
    private final int port;
    private final String fileOutput;

    public CovidServer(int port, String fileOutput) {
        this.port = port;
        this.fileOutput = fileOutput;
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
                    new CovidWorker(socket, fileOutput).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        CovidServer covidServer = new CovidServer(8888, "src/covid/data.csv");
        covidServer.start();
    }
}