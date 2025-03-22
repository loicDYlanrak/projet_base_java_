package main;

import reseau.*;
import java.io.*;
import java.util.Properties;

public class MainServeur {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            try (FileReader reader = new FileReader("conf/serveur.conf")) {
                props.load(reader);
            }
            String ip = props.getProperty("ip", "localhost");
            int port = Integer.parseInt(props.getProperty("port", "12345"));
            Serveur serveur = new Serveur(ip, port);
            serveur.demarrer();
        } catch (IOException e) {
            System.err.println("Erreur lors du demarrage du serveur : " + e.getMessage());
        }
    }
}
