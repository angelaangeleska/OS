package mail;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MailWorker extends Thread {
    private Socket socket;
    private String filePath;
    Lock fileLock;

    public MailWorker(Socket socket, String filePath) {
        this.socket = socket;
        this.filePath = filePath;
        fileLock = new ReentrantLock();
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            FileWriter fw = new FileWriter(filePath, true);

            writer.write("READY\n");
            writer.flush();

            String line = reader.readLine();

            if (line.startsWith("MAIL FROM:")){
                String email = line.split(":")[1];
                if (email.contains("@")){
                    writer.write("250\n");
                    writer.flush();
                    fw.append(line).append("\n");
                    fw.flush();
                } else {
                    throw new IOException();
                }
            }

            line = reader.readLine();
            if (line.startsWith("MAIL TO:")){
                String to = line.split(":")[1];
                if (to.contains("@")){
                    writer.write("THANK YOU\n");
                    writer.flush();
                    fw.append(line).append("\n");
                    fw.flush();
                } else {
                    throw new IOException();
                }
            }

            line = reader.readLine();
            if (line.startsWith("SUBJECT:")) {
                String subject = line.split(":")[1];
                if (subject.length() > 200) {
                    throw new IOException();
                } else {
                    writer.write("OK\n");
                    writer.flush();
                    fw.append(line).append("\n");
                    fw.flush();
                }
            }

            StringBuilder str = new StringBuilder();
            line = reader.readLine();
            while (!line.equals("!")) {
                str.append(line);
                str.append("\n");
                fw.append(line).append("\n");
                fw.flush();
                line = reader.readLine();
            }


            String data = str.toString();
            byte[] bytes = data.getBytes();
            int bytesCount = bytes.length;

            writer.write("RECEIVED " + bytesCount + " bytes\n");
            writer.flush();

            line = reader.readLine();
            fw.append(line).append("\n");
            fw.flush();

            socket.close();
            writer.close();
            reader.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}