package fr.univ_amu.iut;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class containing the server used to simulate a linux console
 */
public class ServerTerminal {
    private int port;
    private int nbClients;

    /**
     * Constructor for the server hosting the linux console used to play the games
     *
     * @param port
     * @param nbClients
     */
    public ServerTerminal(int port, int nbClients) {
        this.port = port;
        this.nbClients = nbClients;
    }

    /**
     * Method used to launch the server
     *
     * @throws IOException
     */
    public void launch() throws IOException {

        ServerSocket server = new ServerSocket(port);

        System.out.println("Bash server launched on port : " + port);

        for (int i = 1; i <= nbClients; i++) {

            Socket client = server.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            Thread receive = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        while ((msg = in.readLine()) != null) {
                            try {
                                Process process = Runtime.getRuntime().exec(msg);

                                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                                String line = "";

                                while ((line = reader.readLine()) != null) {
                                    out.write(line);
                                    out.newLine();
                                    out.flush();
                                    System.out.println(line);
                                }
                            }catch (IOException | NullPointerException | IllegalArgumentException e) {
                                out.write("Command does not exist");
                                out.newLine();
                                out.flush();
                            }
                        }
                        //Exit if the user disconnects
                        System.out.println("Client disconnected");
                        //Close the flux if the user disconnects
                        out.close();
                        client.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            receive.start();
        }
        server.close();
    }

    public static void main(String[] args) throws IOException {

        ServerTerminal server = new ServerTerminal(10013,10000);
        server.launch();

    }
}