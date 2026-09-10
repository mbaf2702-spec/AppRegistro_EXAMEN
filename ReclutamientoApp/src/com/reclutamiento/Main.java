package com.reclutamiento;

import com.reclutamiento.vista.BienvenidaForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar el estilo del sistema: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> new BienvenidaForm().setVisible(true));
    }
}