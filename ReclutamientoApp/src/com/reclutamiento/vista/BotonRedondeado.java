package com.reclutamiento.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Boton con esquinas redondeadas y un efecto de oscurecimiento leve
 * al pasar el mouse por encima (hover), para que la app se sienta
 * mas interactiva que un JButton comun.
 */
public class BotonRedondeado extends JButton {

    private final Color colorBase;
    private final Color colorHover;
    private boolean sobreElBoton = false;

    public BotonRedondeado(String texto, Color colorBase) {
        super(texto);
        this.colorBase = colorBase;
        this.colorHover = colorBase.darker();

        setFont(Estilo.BOTON);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sobreElBoton = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                sobreElBoton = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(sobreElBoton ? colorHover : colorBase);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2.dispose();
        super.paintComponent(g);
    }
}