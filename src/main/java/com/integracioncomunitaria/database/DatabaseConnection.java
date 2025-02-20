package com.integracioncomunitaria.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Datos de conexión a la base de datos
    private static final String URL = "jdbc:mysql://ies9021.edu.ar:3306/ies9021_coco";
    private static final String USER = "ies9021_userdb";
    private static final String PASSWORD = "Xsw23edc.127";

    // Método para obtener la conexión
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se pudo cargar el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: No se pudo conectar a la base de datos.");
            e.printStackTrace();
        }
        return connection;
    }

    // Método para cerrar la conexión
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error: No se pudo cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }

    // Ejemplo de uso
    public static void main(String[] args) {
        Connection connection = getConnection();
        if (connection != null) {
            // Aquí puedes ejecutar consultas o operaciones en la base de datos
            closeConnection(connection);
        }
    }
}