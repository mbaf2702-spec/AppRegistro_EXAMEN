package com.reclutamiento.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel con esquinas redondeadas, sombra suave, y soporte para animar
 * su aparicion (efecto "fade in") cambiando su transparencia.
 */
public class TarjetaRedondeada extends JPanel {

    private final Color colorFondo;
    private final int radio;
    private float alpha = 1f; // 1 = totalmente visible, 0 = invisible

    public TarjetaRedondeada(Color colorFondo, int radio) {
        this.colorFondo = colorFondo;
        this.radio = radio;
        setOpaque(false);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2.setColor(new Color(0, 0, 0, 25));
        g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, radio, radio);

        g2.setColor(colorFondo);
        g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, radio, radio);

        g2.dispose();
        super.paintComponent(g);
    }
}