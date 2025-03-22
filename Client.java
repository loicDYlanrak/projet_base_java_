package reseau;

import java.io.*;
import java.net.*;

public class Client {
    private final String ip;
    private final int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connecter() throws IOException {
        this.socket = new Socket(ip, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connecte au serveur " + ip + ":" + port);
    }

    public void envoyerRequete(String requete) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IllegalStateException("Client non connecte au serveur.");
        }
        out.println(requete);
        String ligne;
        while ((ligne = in.readLine()) != null && !ligne.isBlank()) {
            System.out.println(ligne);
        }
    }

    public void deconnecter() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Deconnecte du serveur.");
        }
    }
}
