package files;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FilesWorker extends Thread {
    private Socket socket;
    Lock fileLock;

    public FilesWorker(Socket socket) {
        this.socket = socket;
        fileLock = new ReentrantLock();
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line = reader.readLine();

            while (!line.equals("QUIT")) {
                if (line.startsWith("COPY")) {
                    String [] parts = line.split(" ");
                    String path_from = parts[1];
                    String path_to = parts[2];

                    Files.createDirectories(Paths.get(path_to).getParent());
                    Files.copy(Paths.get(path_from), Paths.get(path_to), StandardCopyOption.REPLACE_EXISTING);

                    writer.write("END\n");
                    writer.flush();
                } else if (line.startsWith("LIST")) {
                    String path = line.split(":")[1].trim();
                    Files.walk(Paths.get(path)).filter(Files::isRegularFile).filter(x -> x.toString().endsWith(".txt")).forEach(x -> {
                        try {
                            fileLock.lock();
                            try {
                                BasicFileAttributes attributes = Files.readAttributes(x, BasicFileAttributes.class);
                                String message = x.getFileName() + " " + attributes.size() + " " + attributes.lastModifiedTime().toString() + "\n";
                                writer.write(message);
                            } finally {
                                fileLock.unlock();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    writer.write("END\n");
                    writer.flush();
                } else if (line.startsWith("DELETE")) {
                    Files.delete(Paths.get(line.split(" ")[1]));

                    writer.write("END\n");
                    writer.flush();
                }

                line = reader.readLine();
            }

            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}