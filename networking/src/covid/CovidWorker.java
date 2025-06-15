package covid;

import covid.exceptions.InvalidProtocolException;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CovidWorker extends Thread {
    private Socket socket;
    private String fileOutput;
    Lock fileLock;

    public CovidWorker(Socket socket, String fileOutput) {
        this.socket = socket;
        this.fileOutput = fileOutput;
        fileLock = new ReentrantLock();
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            FileWriter fw = new FileWriter(fileOutput, true);

            writer.write("HELLO " + socket.getInetAddress().getHostAddress() + "\n");
            writer.flush();

            String line = reader.readLine();


            if (line.startsWith("HELLO")) {
                writer.write("SEND DAILY DATA\n");
                writer.flush();

                line = reader.readLine();
                while (!line.equals("QUIT")) {
                    if (line.split(",").length == 4) {
                        fileLock.lock();
                        try {
                            fw.append(line).append("\n");
                        } finally {
                            fileLock.unlock();
                        }

                        writer.write("OK\n");
                        writer.flush();
                    } else {
//                        throw new InvalidProtocolException("Invalid daily data format.");
                        throw  new IOException();
                    }
                    line = reader.readLine();
                }

            } else {
                throw new InvalidProtocolException("Client did not send HELLO message as expected.");
            }

            socket.close();
            writer.close();
            fw.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
