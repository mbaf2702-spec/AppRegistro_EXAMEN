package com.reclutamiento.modelo;

import java.sql.Date;

public class Aspirante {
    private int idAspirante;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private Date fechaRegistro;
    private String estado; // "En proceso", "Contratado", "Rechazado"
    private int idVacante;
    private String cargoVacante; // solo para mostrar en tablas (join)
    private int idUsuario;

    public Aspirante() { }

    public int getIdAspirante() { return idAspirante; }
    public void setIdAspirante(int idAspirante) { this.idAspirante = idAspirante; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdVacante() { return idVacante; }
    public void setIdVacante(int idVacante) { this.idVacante = idVacante; }

    public String getCargoVacante() { return cargoVacante; }
    public void setCargoVacante(String cargoVacante) { this.cargoVacante = cargoVacante; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        return nombres + " " + apellidos + " (" + cedula + ")";
    }
}