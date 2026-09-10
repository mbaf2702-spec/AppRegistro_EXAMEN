package com.reclutamiento.vista;

import javax.swing.*;
import java.awt.*;

public class BienvenidaForm extends JFrame {

    private TarjetaRedondeada tarjetaContenido;

    public BienvenidaForm() {
        super("NexoTalento - Bienvenida");
        construirInterfaz();
        iniciarAnimacionEntrada();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        PanelDegradado fondo = new PanelDegradado(Estilo.INDIGO, Estilo.TURQUESA);
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);

        tarjetaContenido = new TarjetaRedondeada(new Color(255, 255, 255, 235), 28);
        tarjetaContenido.setPreferredSize(new Dimension(620, 400));
        tarjetaContenido.setLayout(new BoxLayout(tarjetaContenido, BoxLayout.Y_AXIS));
        tarjetaContenido.setBorder(BorderFactory.createEmptyBorder(45, 55, 40, 55));

        JLabel lblIcono = new JLabel("\uD83E\uDD1D");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));
        lblIcono.setForeground(Estilo.TURQUESA);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNombre = new JLabel("NexoTalento");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblNombre.setForeground(Estilo.INDIGO);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLema = new JLabel("Conectamos talento con oportunidades");
        lblLema.setFont(Estilo.SUBTITULO);
        lblLema.setForeground(Estilo.GRIS_TEXTO);
        lblLema.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblProposito = new JLabel("<html><div style='text-align:center; width:420px'>"
                + "Somos el area de Talento Humano encargada de gestionar cada nueva "
                + "contratacion: desde la apertura de una vacante hasta la entrevista final. "
                + "Ingresa para continuar con el proceso de seleccion."
                + "</div></html>");
        lblProposito.setFont(Estilo.NORMAL);
        lblProposito.setForeground(Estilo.GRIS_TEXTO);
        lblProposito.setAlignmentX(Component.CENTER_ALIGNMENT);

        BotonRedondeado btnComenzar = new BotonRedondeado("Comenzar  \u2192", Estilo.AZUL);
        btnComenzar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComenzar.setMaximumSize(new Dimension(220, 46));
        btnComenzar.addActionListener(e -> irASeleccionRol());

        tarjetaContenido.add(lblIcono);
        tarjetaContenido.add(Box.createVerticalStrut(10));
        tarjetaContenido.add(lblNombre);
        tarjetaContenido.add(Box.createVerticalStrut(4));
        tarjetaContenido.add(lblLema);
        tarjetaContenido.add(Box.createVerticalStrut(22));
        tarjetaContenido.add(lblProposito);
        tarjetaContenido.add(Box.createVerticalStrut(28));
        tarjetaContenido.add(btnComenzar);

        fondo.add(tarjetaContenido);
    }

    private void iniciarAnimacionEntrada() {
        tarjetaContenido.setAlpha(0f);
        Timer timer = new Timer(15, null);
        timer.addActionListener(new java.awt.event.ActionListener() {
            float progreso = 0f;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                progreso += 0.04f;
                if (progreso >= 1f) {
                    progreso = 1f;
                    timer.stop();
                }
                tarjetaContenido.setAlpha(progreso);
            }
        });
        timer.start();
    }

    private void irASeleccionRol() {
        new RolInicialForm().setVisible(true);
        dispose();
    }
}