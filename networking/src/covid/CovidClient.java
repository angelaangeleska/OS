package covid;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CovidClient extends Thread {
    private String address;
    private int port;

    public CovidClient(String address, int port) {
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
            System.out.println("SERVER: " + line);

            writer.write("HELLO " + socket.getLocalPort() + "\n");
            writer.flush();

            line = socketReader.readLine();
            String serverResponse = "OK";

            if (line.equals("SEND DAILY DATA")) {
                int i = 0;
                while (i<5 && serverResponse.equals("OK")) {
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedDate = today.format(formatter);

                    String data = formattedDate + "," + i + "," + (i+6) + "," + (i+2) + "\n";
                    writer.write(data);
                    writer.flush();

                    i++;
                    serverResponse = socketReader.readLine();
                }

                System.out.println(line);

                writer.write("QUIT\n");
                writer.flush();
            }
            else {
                System.out.println("Error in messages");
            }

            socketReader.close();
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CovidClient covidClient = new CovidClient("127.0.0.1", 8888);
        covidClient.start();
    }

}