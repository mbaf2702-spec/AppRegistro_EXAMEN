package com.reclutamiento.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de abrir y cerrar la conexion con la base de datos MySQL.
 * Se centraliza aqui para que si cambia el usuario/clave/servidor,
 * solo se edite este archivo.
 */
public class ConexionBD {

    // ---- Datos de conexion: AJUSTAR segun tu instalacion de MySQL ----
    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DATOS = "db_reclutamiento";
    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String CLAVE = ""; // <-- cambia esto por tu clave real de MySQL

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontro el driver de MySQL (mysql-connector-j). "
                    + "Verifica que el archivo .jar este agregado a las Librerias del proyecto.", ex);
        }
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public static void cerrar(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexion: " + ex.getMessage());
            }
        }
    }
}