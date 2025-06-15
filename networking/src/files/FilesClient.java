package files;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class FilesClient extends Thread {

    private String address;
    private int port;

    public FilesClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void run() {
        Socket socket = null;
        BufferedReader socketReader = null;
        BufferedWriter writer = null;
        BufferedReader keyboardReader = null;

        try {
            socket = new Socket(InetAddress.getByName(this.address), port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            keyboardReader = new BufferedReader(new InputStreamReader(System.in));

            String userInput = keyboardReader.readLine();
            while (!userInput.equals("QUIT")) {
                writer.write(userInput + "\n");
                writer.flush();

                String line;
                while ((line = socketReader.readLine()) != null && !line.equals("END")) {
                    System.out.println(line);
                }
                userInput = keyboardReader.readLine();
            }
            writer.write(userInput + "\n");
            writer.flush();

            socketReader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FilesClient client = new FilesClient("127.0.0.1", 7953);
        client.start();
    }

}