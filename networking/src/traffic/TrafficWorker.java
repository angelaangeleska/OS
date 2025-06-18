package traffic;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TrafficWorker extends Thread {
    private final Socket socket;
    private final String filePath;

    public TrafficWorker(Socket socket, String filePath) {
        this.socket = socket;
        this.filePath = filePath;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            File file = new File(filePath);
            boolean writeHeader = !file.exists();
            FileWriter fileWriter = new FileWriter(filePath, true);
            if (writeHeader) {
                fileWriter.append("date, location, vehicle count, average speed (km/h), congestion level (LOW/MEDIUM/HIGH)\n");
                fileWriter.flush();
            }


            writer.write("HELLO: " + socket.getInetAddress().getHostName() + "\n");
            writer.flush();

            String line = reader.readLine();
            if (line.startsWith("HELLO")) {
                writer.write("SEND TRAFFIC DATA\n");
                writer.flush();
                // <location>, <vehicle count>, <average speed>, <congestion level>

                line = reader.readLine();
                while (!line.equals("QUIT")) {
                    String[] parts = line.split(",\\s+");
                    if (parts.length != 4) {
                        writer.write("ERROR: invalid data\n");
                        writer.flush();
                        break;
                    } else {
                        try {
                            double veh_count = Double.parseDouble(parts[1]);
                            double avg_speed = Double.parseDouble(parts[2]);
                            if (parts[3].equals("LOW") || parts[3].equals("MEDIUM") || parts[3].equals("HIGH")) {
                                String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                fileWriter.append(formattedDate + ", " + line + "\n");
                                fileWriter.flush();

                                writer.write("OK\n");
                                writer.flush();

                                line = reader.readLine();
                            } else {
                                writer.write("ERROR: invalid data\n");
                                writer.flush();

                                break;
                            }
                        } catch (Exception e) {
                            writer.write("ERROR: invalid data\n");
                            writer.flush();

                            break;
                        }
                    }
                }

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
