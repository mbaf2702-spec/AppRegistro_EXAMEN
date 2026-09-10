package com.reclutamiento.modelo;

import java.math.BigDecimal;
import java.sql.Date;

public class Entrevista {
    private int idEntrevista;
    private int idAspirante;
    private String nombreAspirante; // solo para mostrar en tablas (join)
    private Date fechaEntrevista;
    private String entrevistador;
    private BigDecimal puntaje;
    private String resultado; // "Aprobado", "No aprobado", "Pendiente"
    private String observaciones;
    private int idUsuario;

    public Entrevista() { }

    public int getIdEntrevista() { return idEntrevista; }
    public void setIdEntrevista(int idEntrevista) { this.idEntrevista = idEntrevista; }

    public int getIdAspirante() { return idAspirante; }
    public void setIdAspirante(int idAspirante) { this.idAspirante = idAspirante; }

    public String getNombreAspirante() { return nombreAspirante; }
    public void setNombreAspirante(String nombreAspirante) { this.nombreAspirante = nombreAspirante; }

    public Date getFechaEntrevista() { return fechaEntrevista; }
    public void setFechaEntrevista(Date fechaEntrevista) { this.fechaEntrevista = fechaEntrevista; }

    public String getEntrevistador() { return entrevistador; }
    public void setEntrevistador(String entrevistador) { this.entrevistador = entrevistador; }

    public BigDecimal getPuntaje() { return puntaje; }
    public void setPuntaje(BigDecimal puntaje) { this.puntaje = puntaje; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
}