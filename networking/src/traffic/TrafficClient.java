package traffic;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TrafficClient extends Thread{
    private String address;
    private int port;

    public TrafficClient(String address, int port) {
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

            String line = socketReader.readLine();
            if (line.startsWith("HELLO")) {
                writer.write("HELLO " + socket.getLocalPort() + "\n");
                writer.flush();

                line = socketReader.readLine();
                if (line.equals("SEND TRAFFIC DATA")) {
                    String user_input = keyboardReader.readLine();
                    while (!user_input.equals("QUIT")) {
                        writer.write(user_input + "\n");
                        writer.flush();

                        line = socketReader.readLine();
                        if (!line.equals("OK")) {
                            writer.write("QUIT\n");
                            break;
                        }

                        user_input = keyboardReader.readLine();
                    }
                    writer.write(user_input + "\n");
                    writer.flush();
                }
            }

            socketReader.close();
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TrafficClient client = new TrafficClient("127.0.0.1", 9999);
        client.start();
    }
}
