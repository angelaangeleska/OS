package mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MailClient {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 5432;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Step 1: Receive READY from server
            String response = in.readLine();
            System.out.println("Server: " + response);

            // Step 2: Send MAIL FROM
            String emailFrom = "test.sender@example.com";
            out.println("MAIL FROM: " + emailFrom);
            response = in.readLine();
            System.out.println("Server: " + response);

            // Step 4: Send MAIL TO
            String emailTo = "test.receiver@example.com";
            out.println("MAIL TO: " + emailTo);
            response = in.readLine();
            System.out.println("Server: " + response);

            // Step 6: Send SUBJECT
            String subject = "Test Subject";
            out.println("SUBJECT:" + subject);
            response = in.readLine();
            System.out.println("Server: " + response);

            // Step 8: Send message lines, end with !
            String[] messageLines = {
                    "Hello, this is a test email.",
                    "This is the second line.",
                    "!"
            };

            for (String line : messageLines) {
                out.println(line);
            }

            out.println("BYE");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
