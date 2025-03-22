package main;

import reseau.*;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            try (FileReader reader = new FileReader("conf/serveur.conf")) {
                props.load(reader);
            }

            String ip = props.getProperty("ip", "localhost");
            int port = Integer.parseInt(props.getProperty("port", "12345"));

            Client client = new Client(ip, port);
            client.connecter();

            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            while (scanner.hasNextLine()) {
                String requete = scanner.nextLine();
                if (requete.equalsIgnoreCase("exit")) {
                    client.envoyerRequete(requete);
                    client.deconnecter();
                    break;
                } else {
                    client.envoyerRequete(requete);
                }
                System.out.print("> ");
            }
        } catch (IOException e) {
            System.err.println("Erreur cote client : " + e.getMessage());
        }
    }
}
