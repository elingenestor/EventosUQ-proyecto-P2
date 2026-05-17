package com.uniquindio.proyectop2.patterns.Creational.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {
    private static ConexionBD instance;
    private Properties props;

    private ConexionBD(){
        cargarPropiedades();
    }

    public static synchronized ConexionBD getInstance(){
        if (instance == null) {
            instance = new ConexionBD();
        }
        return instance;
    }

    private void cargarPropiedades (){
        props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontro db.properties");
            }
            props.load(input);
            Class.forName(props.getProperty("driver"));
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar configuracion de BD", e);
        }
    }

    public Connection getConnection () throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("url"),
                props.getProperty("user"),
                props.getProperty("password")
        );
    }
}
