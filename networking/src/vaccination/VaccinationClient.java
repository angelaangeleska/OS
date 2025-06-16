package vaccination;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class VaccinationClient extends Thread{
    private String address;
    private int port;

    public VaccinationClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void run() {
        Socket socket = null;
        BufferedReader socketReader = null;
        BufferedWriter writer = null;
        try {
            socket = new Socket(InetAddress.getByName(this.address), port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line = socketReader.readLine();
            if (line.startsWith("HELLO")) {
                writer.write("HELLO " + socket.getLocalPort() + "\n");
                writer.flush();

                line = socketReader.readLine();
                if (line.equals("SEND DAILY DATA")) {
                    int i = 0;
                    while (i < 10) {
                        writer.write(i + ", " + (i+4) + ", " + (i+10) + "\n");
                        writer.flush();

                        line = socketReader.readLine();
                        if (!line.equals("OK")) {
                            break;
                        }
                        i++;
                    }
                    writer.write("QUIT\n");
                    writer.flush();
                }
            }

            socket.close();
            socketReader.close();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        VaccinationClient client = new VaccinationClient("127.0.0.1", 5555);
        client.start();
    }
}
