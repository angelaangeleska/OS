    package labs;

    import java.io.*;
    import java.net.InetAddress;
    import java.net.Socket;

    public class TCPClient extends Thread{
        private int serverPort;
        private String serverAddress;

        public TCPClient(int serverPort, String serverAddress) {
            this.serverPort = serverPort;
            this.serverAddress = serverAddress;
        }

        @Override
        public void run() {
            Socket socket = null;
            BufferedWriter writer = null;
            BufferedReader reader = null;
            BufferedReader keyboard_reader = null;
            try {
                socket = new Socket(InetAddress.getByName(this.serverAddress), serverPort);
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                keyboard_reader = new BufferedReader(new InputStreamReader(System.in));

                writer.write("hello:221083\n");
                writer.flush();

                String line = reader.readLine();
                System.out.printf(line + "\n");
                if (line.equals("Server says: Succesfully logged on to server!")) {
                    writer.write("login:221083\n");
                    writer.flush();

                    line = reader.readLine();
                    System.out.printf(line + "\n");

                    String input =  keyboard_reader.readLine();
                    while (!input.equals("END")) {
                        writer.write("221083:" + input + "\n");
                        writer.flush();

                        line = reader.readLine();
                        System.out.printf(line + "\n");
                        input = keyboard_reader.readLine();
                    }
                }
                socket.close();
                writer.close();
                reader.close();
                keyboard_reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static void main(String[] args) {
            TCPClient client = new TCPClient(9753, "194.149.135.49");
            client.start();
        }
    }
