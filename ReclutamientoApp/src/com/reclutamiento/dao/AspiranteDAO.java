package com.reclutamiento.dao;

import com.reclutamiento.conexion.ConexionBD;
import com.reclutamiento.modelo.Aspirante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AspiranteDAO {

    public boolean crear(Aspirante a) throws SQLException {
        String sql = "INSERT INTO aspirantes (cedula, nombres, apellidos, telefono, correo, "
                + "estado, id_vacante, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getCedula());
            ps.setString(2, a.getNombres());
            ps.setString(3, a.getApellidos());
            ps.setString(4, a.getTelefono());
            ps.setString(5, a.getCorreo());
            ps.setString(6, a.getEstado());
            ps.setInt(7, a.getIdVacante());
            if (a.getIdUsuario() > 0) {
                ps.setInt(8, a.getIdUsuario());
            } else {
                // Auto-registro desde el portal: no hay un empleado de RRHH detras
                ps.setNull(8, java.sql.Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existeCedula(String cedula) throws SQLException {
        String sql = "SELECT 1 FROM aspirantes WHERE cedula = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Aspirante> listarTodos() throws SQLException {
        List<Aspirante> lista = new ArrayList<>();
        String sql = "SELECT a.*, v.cargo AS cargo_vacante FROM aspirantes a "
                + "JOIN vacantes v ON a.id_vacante = v.id_vacante "
                + "ORDER BY a.id_aspirante DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Aspirante> buscar(String texto) throws SQLException {
        List<Aspirante> lista = new ArrayList<>();
        String sql = "SELECT a.*, v.cargo AS cargo_vacante FROM aspirantes a "
                + "JOIN vacantes v ON a.id_vacante = v.id_vacante "
                + "WHERE a.cedula LIKE ? OR a.nombres LIKE ? OR a.apellidos LIKE ? "
                + "ORDER BY a.id_aspirante DESC";
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

    public boolean actualizar(Aspirante a) throws SQLException {
        String sql = "UPDATE aspirantes SET cedula=?, nombres=?, apellidos=?, telefono=?, "
                + "correo=?, estado=?, id_vacante=? WHERE id_aspirante=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getCedula());
            ps.setString(2, a.getNombres());
            ps.setString(3, a.getApellidos());
            ps.setString(4, a.getTelefono());
            ps.setString(5, a.getCorreo());
            ps.setString(6, a.getEstado());
            ps.setInt(7, a.getIdVacante());
            ps.setInt(8, a.getIdAspirante());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idAspirante) throws SQLException {
        String sql = "DELETE FROM aspirantes WHERE id_aspirante = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAspirante);
            return ps.executeUpdate() > 0;
        }
    }

    private Aspirante mapear(ResultSet rs) throws SQLException {
        Aspirante a = new Aspirante();
        a.setIdAspirante(rs.getInt("id_aspirante"));
        a.setCedula(rs.getString("cedula"));
        a.setNombres(rs.getString("nombres"));
        a.setApellidos(rs.getString("apellidos"));
        a.setTelefono(rs.getString("telefono"));
        a.setCorreo(rs.getString("correo"));
        a.setFechaRegistro(rs.getDate("fecha_registro"));
        a.setEstado(rs.getString("estado"));
        a.setIdVacante(rs.getInt("id_vacante"));
        a.setCargoVacante(rs.getString("cargo_vacante"));
        a.setIdUsuario(rs.getInt("id_usuario"));
        return a;
    }
    
    /** Cuenta el total de aspirantes registrados. */
    public int contarTodos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM aspirantes";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}