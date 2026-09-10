package com.reclutamiento.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que pinta un degradado diagonal entre 2 colores, usado en los
 * encabezados para dar un efecto mas vivo que un color solido plano.
 */
public class PanelDegradado extends JPanel {

    private final Color colorInicio;
    private final Color colorFin;

    public PanelDegradado(Color colorInicio, Color colorFin) {
        this.colorInicio = colorInicio;
        this.colorFin = colorFin;
    }

        @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        GradientPaint degradado = new GradientPaint(
                0, 0, colorInicio,
                getWidth(), getHeight(), colorFin);
        g2.setPaint(degradado);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}