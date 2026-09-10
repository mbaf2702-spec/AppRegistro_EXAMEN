package com.reclutamiento.vista;

import javax.swing.*;
import java.awt.*;

public class AccesoEmpleadoForm extends JFrame {

    public AccesoEmpleadoForm() {
        super("NexoTalento - Acceso de empleado");
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
        PanelDegradado panel = new PanelDegradado(Estilo.AZUL, Estilo.INDIGO);
        panel.setPreferredSize(new Dimension(380, 560));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JButton btnVolver = new JButton("\u2190 Volver");
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(Estilo.NORMAL);
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            new RolInicialForm().setVisible(true);
            dispose();
        });

        JLabel lblTitulo = new JLabel("<html>Equipo de<br>Talento Humano</html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Accede o crea tu cuenta de empleado");
        lblSub.setFont(Estilo.SUBTITULO);
        lblSub.setForeground(new Color(220, 226, 245));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcono = new JLabel("\uD83D\uDCBC");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(btnVolver);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblSub);
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

        JLabel lblTitulo = new JLabel("\u00bfYa tienes una cuenta?");
        lblTitulo.setFont(Estilo.TITULO_GRANDE);
        lblTitulo.setForeground(Estilo.INDIGO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Elige una opcion para continuar");
        lblSub.setFont(Estilo.SUBTITULO);
        lblSub.setForeground(Estilo.GRIS_TEXTO);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        BotonRedondeado btnIniciarSesion = new BotonRedondeado("Iniciar sesion  \u2192", Estilo.AZUL);
        btnIniciarSesion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIniciarSesion.setMaximumSize(new Dimension(320, 50));
        btnIniciarSesion.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        JLabel lblDescLogin = new JLabel("<html><div style='width:300px'>Ya tengo una cuenta de empleado creada</div></html>");
        lblDescLogin.setFont(Estilo.SUBTITULO);
        lblDescLogin.setForeground(Estilo.GRIS_TEXTO);
        lblDescLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        BotonRedondeado btnRegistrarme = new BotonRedondeado("Registrarme  \u2192", Estilo.TURQUESA);
        btnRegistrarme.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegistrarme.setMaximumSize(new Dimension(320, 50));
        btnRegistrarme.addActionListener(e -> {
            new RegistroUsuarioForm().setVisible(true);
            dispose();
        });

        JLabel lblDescRegistro = new JLabel("<html><div style='width:300px'>Soy nuevo en el equipo, necesito crear mi cuenta</div></html>");
        lblDescRegistro.setFont(Estilo.SUBTITULO);
        lblDescRegistro.setForeground(Estilo.GRIS_TEXTO);
        lblDescRegistro.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblSub);
        tarjeta.add(Box.createVerticalStrut(28));
        tarjeta.add(btnIniciarSesion);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblDescLogin);
        tarjeta.add(Box.createVerticalStrut(22));
        tarjeta.add(btnRegistrarme);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblDescRegistro);

        panel.add(tarjeta);
        return panel;
    }
}