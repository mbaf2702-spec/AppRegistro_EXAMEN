package com.reclutamiento.dao;

import com.reclutamiento.conexion.ConexionBD;
import com.reclutamiento.modelo.Usuario;
import com.reclutamiento.util.SeguridadUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Operaciones sobre la tabla usuarios: validar login, obtener la
 * pregunta de seguridad y permitir el cambio de contrasena.
 */
public class UsuarioDAO {

    public Usuario validarLogin(String nombreUsuario, String contrasenaPlano) throws SQLException {
        String sql = "SELECT id_usuario, nombre_usuario, pregunta_seguridad, rol "
                + "FROM usuarios WHERE nombre_usuario = ? AND contrasena_hash = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ps.setString(2, SeguridadUtil.hashear(contrasenaPlano));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_usuario"),
                            rs.getString("pregunta_seguridad"),
                            rs.getString("rol")
                    );
                }
                return null;
            }
        }
    }

    public String obtenerPreguntaSeguridad(String nombreUsuario) throws SQLException {
        String sql = "SELECT pregunta_seguridad FROM usuarios WHERE nombre_usuario = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("pregunta_seguridad") : null;
            }
        }
    }

    public boolean validarRespuestaSeguridad(String nombreUsuario, String respuestaPlano) throws SQLException {
        String sql = "SELECT respuesta_hash FROM usuarios WHERE nombre_usuario = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                String hashGuardado = rs.getString("respuesta_hash");
                return hashGuardado.equals(SeguridadUtil.hashear(respuestaPlano));
            }
        }
    }

    public boolean actualizarContrasena(String nombreUsuario, String nuevaContrasenaPlano) throws SQLException {
        String sql = "UPDATE usuarios SET contrasena_hash = ? WHERE nombre_usuario = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, SeguridadUtil.hashear(nuevaContrasenaPlano));
            ps.setString(2, nombreUsuario);
            return ps.executeUpdate() > 0;
        }
    }
    
    /** Verifica si ya existe un usuario con ese nombre (para no duplicar). */
    public boolean existeUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE nombre_usuario = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Registra un nuevo usuario (empleado) del sistema. */
    public boolean crear(String nombreUsuario, String contrasenaPlano, String preguntaSeguridad,
                          String respuestaPlano, String rol) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena_hash, pregunta_seguridad, "
                + "respuesta_hash, rol) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ps.setString(2, SeguridadUtil.hashear(contrasenaPlano));
            ps.setString(3, preguntaSeguridad);
            ps.setString(4, SeguridadUtil.hashear(respuestaPlano));
            ps.setString(5, rol);
            return ps.executeUpdate() > 0;
        }
    }
}