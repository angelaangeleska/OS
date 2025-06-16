package vaccination;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VaccinationWorker extends Thread {
    private Socket socket;
    private String filePath;

    public VaccinationWorker(Socket socket, String filePath) {
        this.socket = socket;
        this.filePath = filePath;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            File file = new File(filePath);
            boolean fileExists = file.exists();

            FileWriter fileWriter = new FileWriter(filePath, true);

            if (!fileExists) {
                fileWriter.append("date, No. new covid cases, No. hospitalized patients, No. recovered patients\n");
                fileWriter.flush();
            }


            writer.write("HELLO " + socket.getInetAddress().getHostName() + "\n");
            writer.flush();

            String line = reader.readLine();
            if (line.startsWith("HELLO")) {
                writer.write("SEND DAILY DATA\n");
                writer.flush();

                String response = reader.readLine();
                while (!response.equals("QUIT")) {
                    if (response.split(",").length == 3) {
                        LocalDate today = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String formattedDate = today.format(formatter);

                        fileWriter.write(formattedDate + ", " + response + "\n");
                        fileWriter.flush();

                        writer.write("OK\n");
                        writer.flush();
                    } else {
                        throw new IOException();
                    }
                    response = reader.readLine();
                }
                writer.write(response + "\n");
                writer.flush();
            } else {
                throw new IOException();
            }

            writer.close();
            reader.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
