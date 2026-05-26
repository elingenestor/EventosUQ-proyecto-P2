package com.uniquindio.proyectop2.patterns.Creational.singleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConexionBD {
    private static volatile ConexionBD instance;

    private final Properties props;
    private final String url;
    private final String user;
    private final String password;
    private final String driver;

    private boolean initialized;

    private ConexionBD() {
        props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró db.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer db.properties", e);
        }

        url = firstNonBlank(
                props.getProperty("db.url"),
                props.getProperty("jdbc.url")
        );

        user = firstNonBlank(
                props.getProperty("db.user"),
                props.getProperty("jdbc.username"),
                props.getProperty("jdbc.user")
        );

        password = firstNonBlank(
                props.getProperty("db.password"),
                props.getProperty("jdbc.password")
        );

        driver = firstNonBlank(
                props.getProperty("db.driver"),
                props.getProperty("jdbc.driver"),
                inferDriver(url)
        );

        if (driver == null || driver.isBlank()) {
            throw new RuntimeException("No se pudo determinar el driver JDBC a usar.");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo cargar el driver JDBC: " + driver, e);
        }
    }

    public static synchronized ConexionBD getInstance() {
        if (instance == null) {
            instance = new ConexionBD();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        ensureInitialized(connection);
        return connection;
    }

    private void ensureInitialized(Connection connection) throws SQLException {
        if (initialized) {
            return;
        }

        synchronized (this) {
            if (initialized) {
                return;
            }

            if (url != null && url.startsWith("jdbc:h2:")) {
                ejecutarScript(connection, "database/schema.sql");

                try {
                    ejecutarScript(connection, "database/data_1.sql");
                } catch (Exception e) {
                    System.out.println("Los datos iniciales ya estaban cargados.");
                }
            }

            initialized = true;
        }
    }

    private void ejecutarScript(Connection connection, String resourcePath) {
        try (InputStream input = ConexionBD.class.getResourceAsStream("/" + resourcePath)) {
            System.out.println("Buscando recurso: " + resourcePath);
            System.out.println(getClass().getClassLoader().getResource(resourcePath));
            if (input == null) {
                throw new RuntimeException("No se encontró el script: " + resourcePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                 Statement statement = connection.createStatement()) {

                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                        continue;
                    }

                    sql.append(trimmed).append(' ');

                    if (trimmed.endsWith(";")) {
                        String instruccion = sql.toString().trim();
                        if (instruccion.endsWith(";")) {
                            instruccion = instruccion.substring(0, instruccion.length() - 1);
                        }
                        if (!instruccion.isBlank()) {
                            statement.execute(instruccion);
                        }
                        sql.setLength(0);
                    }
                }

                String pendiente = sql.toString().trim();
                if (!pendiente.isBlank()) {
                    if (pendiente.endsWith(";")) {
                        pendiente = pendiente.substring(0, pendiente.length() - 1);
                    }
                    statement.execute(pendiente);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo ejecutar el script " + resourcePath, e);
        }
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    private String inferDriver(String jdbcUrl) {
        if (jdbcUrl == null) {
            return null;
        }
        String normalized = jdbcUrl.trim().toLowerCase();
        if (normalized.startsWith("jdbc:h2:")) {
            return "org.h2.Driver";
        }
        if (normalized.startsWith("jdbc:mysql:")) {
            return "com.mysql.cj.jdbc.Driver";
        }
        return null;
    }
}
