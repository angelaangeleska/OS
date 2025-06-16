package mail;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class FileInputMailClient extends Thread {
    private String address;
    private int port;
    private String filePath;

    public FileInputMailClient(String address, int port, String filePath) {
        this.address = address;
        this.port = port;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        BufferedReader fileReader = null;

        try {
            socket = new Socket(InetAddress.getByName(address), port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            fileReader = new BufferedReader(new FileReader(filePath));

            String line = reader.readLine();
            if (line.equals("READY")) {
                String message = fileReader.readLine();
                writer.write("MAIL FROM: " + message + "\n");
                writer.flush();

                line = reader.readLine();
                if (line.equals("250")) {
                    message = fileReader.readLine();
                    writer.write("MAIL TO: " + message + "\n");
                    writer.flush();

                    line = reader.readLine();
                    if (line.equals("THANK YOU")) {
                        message = fileReader.readLine();
                        writer.write("SUBJECT: " + message + "\n");
                        writer.flush();

                        line = reader.readLine();
                        if (line.equals("OK")) {
                            StringBuilder msg = new StringBuilder();
                            message = fileReader.readLine();
                            while (!message.equals("!")) {
                                msg.append(message);
                                msg.append('\n');
                                message = fileReader.readLine();
                            }
                            msg.append(message).append('\n');

                            writer.write(msg.toString());
                            writer.flush();
                        }
                        writer.write("BYE\n");
                        writer.flush();
                    }
                }
            }
            socket.close();
            writer.close();
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FileInputMailClient client = new FileInputMailClient("127.0.0.1", 5432, "src/mail/mail_input.txt");
        client.start();
    }
}
