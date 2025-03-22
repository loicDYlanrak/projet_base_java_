package main;

import jdbc.*;

public class MainJDBC {
    public static void main(String[] args) {
        try {
            // Connexion à la base de donnees
            Connection connection = new Connection("testDB");

            // Utilisation de Statement
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE users (id int, name varchar, age int)");
            statement.executeUpdate("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 25)");
            statement.executeUpdate("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 30)");

            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            System.out.println("Contenu de la table 'users':");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getObject("id") + ", Name: " + resultSet.getObject("name") + ", Age: " + resultSet.getObject("age"));
            }
            resultSet.close();

            // Utilisation de PreparedStatement
            PreparedStatement preparedStatement = connection.createPreparedStatement("INSERT INTO users (id, name, age) VALUES (?, ?, ?)");
            preparedStatement.setParameter(1, 3);
            preparedStatement.setParameter(2, "Charlie");
            preparedStatement.setParameter(3, 22);

            ResultSet preparedResultSet = statement.executeQuery("SELECT * FROM users");
            System.out.println("\nApres insertion avec PreparedStatement:");
            while (preparedResultSet.next()) {
                System.out.println("ID: " + preparedResultSet.getObject("id") + ", Name: " + preparedResultSet.getObject("name") + ", Age: " + preparedResultSet.getObject("age"));
            }
            preparedResultSet.close();

            // Fermeture des ressources
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
