package com.reclutamiento.vista;

import javax.swing.*;
import java.awt.*;

public class IconoCandado extends JComponent {

    private final Color color;

    public IconoCandado(Color color) {
        this.color = color;
        setOpaque(false);
        setPreferredSize(new Dimension(110, 110));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        g2.drawArc(cx - 22, cy - 42, 44, 44, 0, 180);
        g2.drawRoundRect(cx - 32, cy - 12, 64, 50, 14, 14);
        g2.fillOval(cx - 5, cy + 6, 10, 10);
        g2.drawLine(cx, cy + 16, cx, cy + 26);

        g2.dispose();
    }
}