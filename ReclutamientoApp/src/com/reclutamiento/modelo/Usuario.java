package com.reclutamiento.modelo;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenaHash;
    private String preguntaSeguridad;
    private String respuestaHash;
    private String rol;

    public Usuario() { }

    public Usuario(int idUsuario, String nombreUsuario, String preguntaSeguridad, String rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.preguntaSeguridad = preguntaSeguridad;
        this.rol = rol;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public String getPreguntaSeguridad() { return preguntaSeguridad; }
    public void setPreguntaSeguridad(String preguntaSeguridad) { this.preguntaSeguridad = preguntaSeguridad; }

    public String getRespuestaHash() { return respuestaHash; }
    public void setRespuestaHash(String respuestaHash) { this.respuestaHash = respuestaHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}