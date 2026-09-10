package com.reclutamiento.vista;

import javax.swing.*;
import java.awt.*;

public class RolInicialForm extends JFrame {

    public RolInicialForm() {
        super("NexoTalento - Continuar como");
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(crearPanelIzquierdo(), BorderLayout.WEST);
        add(crearPanelDerecho(), BorderLayout.CENTER);
    }

    private JPanel crearPanelIzquierdo() {
        PanelDegradado panel = new PanelDegradado(Estilo.INDIGO, Estilo.AZUL);
        panel.setPreferredSize(new Dimension(380, 560));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JLabel lblTitulo = new JLabel("<html>\u00bfQui\u00e9n<br>eres?</html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Elige c\u00f3mo quieres continuar");
        lblSub.setFont(Estilo.SUBTITULO);
        lblSub.setForeground(new Color(220, 226, 245));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel linea = new JPanel();
        linea.setBackground(Estilo.TURQUESA);
        linea.setMaximumSize(new Dimension(50, 4));
        linea.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcono = new JLabel("\uD83D\uDC65");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(14));
        panel.add(linea);
        panel.add(Box.createVerticalGlue());
        panel.add(lblIcono);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Estilo.FONDO);

        TarjetaRedondeada tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 24);
        tarjeta.setPreferredSize(new Dimension(400, 340));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(45, 40, 40, 40));

        JLabel lblTitulo = new JLabel("Seleccione una opci\u00f3n");
        lblTitulo.setFont(Estilo.TITULO_GRANDE);
        lblTitulo.setForeground(Estilo.INDIGO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        BotonRedondeado btnAspirante = new BotonRedondeado("Soy un nuevo aspirante  \u2192", Estilo.TURQUESA);
        btnAspirante.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAspirante.setMaximumSize(new Dimension(320, 56));
        btnAspirante.addActionListener(e -> {
            new RegistroAspiranteForm().setVisible(true);
            dispose();
        });

        JLabel lblDescAspirante = new JLabel("<html><div style='width:300px'>Quiero postularme a una vacante disponible</div></html>");
        lblDescAspirante.setFont(Estilo.SUBTITULO);
        lblDescAspirante.setForeground(Estilo.GRIS_TEXTO);
        lblDescAspirante.setAlignmentX(Component.LEFT_ALIGNMENT);

        BotonRedondeado btnEmpleado = new BotonRedondeado("Soy parte del equipo  \u2192", Estilo.AZUL);
        btnEmpleado.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEmpleado.setMaximumSize(new Dimension(320, 56));
        btnEmpleado.addActionListener(e -> {
            new AccesoEmpleadoForm().setVisible(true);
            dispose();
        });

        JLabel lblDescEmpleado = new JLabel("<html><div style='width:300px'>Trabajo en Talento Humano y necesito iniciar sesi\u00f3n</div></html>");
        lblDescEmpleado.setFont(Estilo.SUBTITULO);
        lblDescEmpleado.setForeground(Estilo.GRIS_TEXTO);
        lblDescEmpleado.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(30));
        tarjeta.add(btnAspirante);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblDescAspirante);
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(btnEmpleado);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblDescEmpleado);

        panel.add(tarjeta);
        return panel;
    }
}