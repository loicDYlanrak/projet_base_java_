package reseau;

import base.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Serveur {
    private final String ip;
    private final int port;
    private final DatabaseManager dbManager;
    private boolean running;
    private final ExecutorService threadPool;

    public Serveur(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.dbManager = new DatabaseManager();
        this.running = true;
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void demarrer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip))) {
            System.out.println("Serveur demarre sur " + ip + ":" + port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion client : " + clientSocket.getRemoteSocketAddress());
                threadPool.execute(() -> gererClient(clientSocket));
            }
        } finally {
            threadPool.shutdown();
        }
    }

    private void gererClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String requete;
            while ((requete = in.readLine()) != null) {
                if (requete.equalsIgnoreCase("exit")) {
                    out.println("Deconnexion reussie. Au revoir !");
                    break;
                }
                try {
                    String reponse = dbManager.executerRequete(requete);
                    out.println(reponse + "\n");
                } catch (Exception e) {
                    out.println("Erreur : " + e.getMessage() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de communication avec le client : " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture du socket client : " + e.getMessage());
            }
        }
    }
}
