package com.reclutamiento.modelo;

import java.math.BigDecimal;
import java.sql.Date;

public class Vacante {
    private int idVacante;
    private String cargo;
    private String departamento;
    private BigDecimal salarioOfertado;
    private Date fechaApertura;
    private String estado; // "Abierta" o "Cerrada"

    public Vacante() { }

    public int getIdVacante() { return idVacante; }
    public void setIdVacante(int idVacante) { this.idVacante = idVacante; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public BigDecimal getSalarioOfertado() { return salarioOfertado; }
    public void setSalarioOfertado(BigDecimal salarioOfertado) { this.salarioOfertado = salarioOfertado; }

    public Date getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(Date fechaApertura) { this.fechaApertura = fechaApertura; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        // Se usa para mostrar la vacante de forma legible en los JComboBox
        return idVacante + " - " + cargo + " (" + departamento + ")";
    }
}