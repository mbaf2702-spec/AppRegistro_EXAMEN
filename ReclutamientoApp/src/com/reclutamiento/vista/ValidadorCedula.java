package com.reclutamiento.util;

/**
 * Valida cedulas ecuatorianas con el algoritmo oficial del digito
 * verificador (modulo 10), no solo la cantidad de digitos.
 */
public class ValidadorCedula {

    public static boolean esValida(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) {
            return false;
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            return false;
        }

        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito > 6) {
            return false;
        }

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            if (digito > 9) {
                digito -= 9;
            }
            suma += digito;
        }

        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int residuo = suma % 10;
        int resultado = (residuo == 0) ? 0 : 10 - residuo;

        return resultado == digitoVerificador;
    }
}