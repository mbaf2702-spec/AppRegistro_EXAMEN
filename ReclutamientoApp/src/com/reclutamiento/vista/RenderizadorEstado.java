package com.reclutamiento.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Dibuja el valor de una celda como una "pastilla" de color (pill badge)
 * en vez de texto plano, segun el estado que contenga. Se usa en las
 * columnas de estado de las 3 tablas de gestion.
 */
public class RenderizadorEstado extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
                                                     boolean conFoco, int fila, int columna) {
        JLabel etiqueta = (JLabel) super.getTableCellRendererComponent(
                tabla, valor, seleccionado, conFoco, fila, columna);

        String texto = valor == null ? "" : valor.toString();
        Color colorFondo;
        Color colorTexto = Color.WHITE;

        switch (texto) {
            case "Abierta":
            case "En proceso":
            case "Pendiente":
                colorFondo = new Color(79, 125, 243);
                break;
            case "Contratado":
            case "Aprobado":
                colorFondo = new Color(36, 183, 165);
                break;
            case "Cerrada":
            case "Rechazado":
            case "No aprobado":
                colorFondo = new Color(255, 107, 107);
                break;
            default:
                colorFondo = new Color(200, 200, 200);
                colorTexto = new Color(60, 60, 60);
        }

        etiqueta.setOpaque(false);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        etiqueta.setForeground(colorTexto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        final Color fondoFinal = colorFondo;
        JPanel envoltura = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fondoFinal);
                int alto = getHeight() - 6;
                g2.fillRoundRect(4, 3, getWidth() - 8, alto, alto, alto);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        envoltura.setOpaque(false);
        envoltura.add(etiqueta, BorderLayout.CENTER);
        return envoltura;
    }
}