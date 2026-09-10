package com.reclutamiento.dao;

import com.reclutamiento.conexion.ConexionBD;
import com.reclutamiento.modelo.Entrevista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntrevistaDAO {

    public boolean crear(Entrevista e) throws SQLException {
        String sql = "INSERT INTO entrevistas (id_aspirante, fecha_entrevista, entrevistador, "
                + "puntaje, resultado, observaciones, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, e.getIdAspirante());
            ps.setDate(2, e.getFechaEntrevista());
            ps.setString(3, e.getEntrevistador());
            ps.setBigDecimal(4, e.getPuntaje());
            ps.setString(5, e.getResultado());
            ps.setString(6, e.getObservaciones());
            ps.setInt(7, e.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Entrevista> listarTodas() throws SQLException {
        List<Entrevista> lista = new ArrayList<>();
        String sql = "SELECT en.*, CONCAT(a.nombres, ' ', a.apellidos) AS nombre_aspirante "
                + "FROM entrevistas en JOIN aspirantes a ON en.id_aspirante = a.id_aspirante "
                + "ORDER BY en.id_entrevista DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Entrevista> buscarPorAspirante(String texto) throws SQLException {
        List<Entrevista> lista = new ArrayList<>();
        String sql = "SELECT en.*, CONCAT(a.nombres, ' ', a.apellidos) AS nombre_aspirante "
                + "FROM entrevistas en JOIN aspirantes a ON en.id_aspirante = a.id_aspirante "
                + "WHERE a.nombres LIKE ? OR a.apellidos LIKE ? OR a.cedula LIKE ? "
                + "ORDER BY en.id_entrevista DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String comodin = "%" + texto + "%";
            ps.setString(1, comodin);
            ps.setString(2, comodin);
            ps.setString(3, comodin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public boolean actualizar(Entrevista e) throws SQLException {
        String sql = "UPDATE entrevistas SET fecha_entrevista=?, entrevistador=?, puntaje=?, "
                + "resultado=?, observaciones=? WHERE id_entrevista=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, e.getFechaEntrevista());
            ps.setString(2, e.getEntrevistador());
            ps.setBigDecimal(3, e.getPuntaje());
            ps.setString(4, e.getResultado());
            ps.setString(5, e.getObservaciones());
            ps.setInt(6, e.getIdEntrevista());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idEntrevista) throws SQLException {
        String sql = "DELETE FROM entrevistas WHERE id_entrevista = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEntrevista);
            return ps.executeUpdate() > 0;
        }
    }

    private Entrevista mapear(ResultSet rs) throws SQLException {
        Entrevista e = new Entrevista();
        e.setIdEntrevista(rs.getInt("id_entrevista"));
        e.setIdAspirante(rs.getInt("id_aspirante"));
        e.setNombreAspirante(rs.getString("nombre_aspirante"));
        e.setFechaEntrevista(rs.getDate("fecha_entrevista"));
        e.setEntrevistador(rs.getString("entrevistador"));
        e.setPuntaje(rs.getBigDecimal("puntaje"));
        e.setResultado(rs.getString("resultado"));
        e.setObservaciones(rs.getString("observaciones"));
        e.setIdUsuario(rs.getInt("id_usuario"));
        return e;
    }
    
    /** Cuenta cuantas entrevistas siguen con resultado Pendiente. */
    public int contarPendientes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM entrevistas WHERE resultado = 'Pendiente'";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}