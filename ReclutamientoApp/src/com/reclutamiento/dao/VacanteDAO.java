package com.reclutamiento.dao;

import com.reclutamiento.conexion.ConexionBD;
import com.reclutamiento.modelo.Vacante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacanteDAO {

    public boolean crear(Vacante v) throws SQLException {
        String sql = "INSERT INTO vacantes (cargo, departamento, salario_ofertado, fecha_apertura, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getCargo());
            ps.setString(2, v.getDepartamento());
            ps.setBigDecimal(3, v.getSalarioOfertado());
            ps.setDate(4, v.getFechaApertura());
            ps.setString(5, v.getEstado());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Vacante> listarTodas() throws SQLException {
        List<Vacante> lista = new ArrayList<>();
        String sql = "SELECT * FROM vacantes ORDER BY id_vacante DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Vacante> buscarPorCargo(String textoBusqueda) throws SQLException {
        List<Vacante> lista = new ArrayList<>();
        String sql = "SELECT * FROM vacantes WHERE cargo LIKE ? OR departamento LIKE ? ORDER BY id_vacante DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String comodin = "%" + textoBusqueda + "%";
            ps.setString(1, comodin);
            ps.setString(2, comodin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public boolean actualizar(Vacante v) throws SQLException {
        String sql = "UPDATE vacantes SET cargo=?, departamento=?, salario_ofertado=?, "
                + "fecha_apertura=?, estado=? WHERE id_vacante=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getCargo());
            ps.setString(2, v.getDepartamento());
            ps.setBigDecimal(3, v.getSalarioOfertado());
            ps.setDate(4, v.getFechaApertura());
            ps.setString(5, v.getEstado());
            ps.setInt(6, v.getIdVacante());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idVacante) throws SQLException {
        String sql = "DELETE FROM vacantes WHERE id_vacante = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVacante);
            return ps.executeUpdate() > 0;
        }
    }

    private Vacante mapear(ResultSet rs) throws SQLException {
        Vacante v = new Vacante();
        v.setIdVacante(rs.getInt("id_vacante"));
        v.setCargo(rs.getString("cargo"));
        v.setDepartamento(rs.getString("departamento"));
        v.setSalarioOfertado(rs.getBigDecimal("salario_ofertado"));
        v.setFechaApertura(rs.getDate("fecha_apertura"));
        v.setEstado(rs.getString("estado"));
        return v;
    }
    
    /** Cuenta cuantas vacantes estan actualmente abiertas. */
    public int contarAbiertas() throws SQLException {
        String sql = "SELECT COUNT(*) FROM vacantes WHERE estado = 'Abierta'";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}