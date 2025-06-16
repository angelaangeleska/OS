package mail;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ConsoleInputMailClient extends Thread {
    private String address;
    private int port;

    public ConsoleInputMailClient(String address, int port) {
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

            if (line.equals("READY")){
                System.out.println("Please enter your email address");
                String from_email = keyboardReader.readLine();

                writer.write("MAIL FROM: " + from_email + "\n");
                writer.flush();

                line = socketReader.readLine();
                if (line.equals("250")) {
                    System.out.println("Please enter the recipient's email address");
                    String to_email = keyboardReader.readLine();

                    writer.write("MAIL TO: " + to_email + "\n");
                    writer.flush();

                    line = socketReader.readLine();
                    if (line.equals("THANK YOU")) {
                        System.out.println("Please enter the subject of your email");
                        String subject = keyboardReader.readLine();

                        writer.write("SUBJECT: " +  subject + "\n");
                        writer.flush();

                        line = socketReader.readLine();
                        if (line.equals("OK")) {
                            System.out.println("Please enter the content of your email message");

                            StringBuilder content = new StringBuilder();
                            String message = keyboardReader.readLine();
                            while (!message.equals("!")) {
                                content.append(message);
                                content.append("\n");

                                message = keyboardReader.readLine();
                            }
                            content.append(message);
                            content.append("\n");
                            writer.write(content.toString());
                            writer.flush();
                        }
                        writer.write("BYE\n");
                        writer.flush();
                    }
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
        ConsoleInputMailClient client = new ConsoleInputMailClient("127.0.0.1", 5432);
        client.start();
    }
}
