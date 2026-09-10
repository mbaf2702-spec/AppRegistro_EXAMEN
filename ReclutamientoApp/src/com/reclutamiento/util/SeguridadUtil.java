package com.reclutamiento.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Convierte texto plano (contrasenas, respuestas de seguridad) en un
 * hash SHA-256. Nunca se guarda ni se compara texto plano directamente:
 * esto protege la informacion aunque alguien vea la base de datos.
 */
public class SeguridadUtil {

    public static String hashear(String textoPlano) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytesHash = md.digest(textoPlano.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytesHash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException ex) {
            throw new RuntimeException("No se pudo generar el hash de seguridad.", ex);
        }
    }
}