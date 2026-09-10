package com.reclutamiento.util;

import com.reclutamiento.modelo.Usuario;

/**
 * Guarda en memoria el usuario que inicio sesion, para poder registrar
 * quien creo cada aspirante/entrevista (trazabilidad) sin pedirselo
 * de nuevo en cada pantalla.
 */
public class SesionActual {
    private static Usuario usuarioActivo;

    public static void iniciar(Usuario usuario) {
        usuarioActivo = usuario;
    }

    public static Usuario obtener() {
        return usuarioActivo;
    }

    public static void cerrar() {
        usuarioActivo = null;
    }
}