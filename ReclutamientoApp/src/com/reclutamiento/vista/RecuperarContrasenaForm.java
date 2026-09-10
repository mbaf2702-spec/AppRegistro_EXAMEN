package com.reclutamiento.vista;

import com.reclutamiento.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RecuperarContrasenaForm extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private TarjetaRedondeada tarjeta;

    private JTextField txtUsuario;
    private JLabel lblPregunta;
    private JTextField txtRespuesta;
    private JPasswordField txtNuevaClave;
    private JPasswordField txtConfirmarClave;

    private String usuarioEnProceso;

    public RecuperarContrasenaForm() {
        super("NexoTalento - Recuperar contrasena");
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(crearPanelIzquierdo(), BorderLayout.WEST);

        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBackground(Estilo.FONDO);

        tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 24);
        tarjeta.setPreferredSize(new Dimension(380, 460));
        tarjeta.setLayout(cardLayout);
        tarjeta.add(crearPasoUsuario(), "PASO1");
        tarjeta.add(crearPasoRespuesta(), "PASO2");

        panelDerecho.add(tarjeta);
        add(panelDerecho, BorderLayout.CENTER);
    }

    private JPanel crearPanelIzquierdo() {
        PanelDegradado panel = new PanelDegradado(Estilo.INDIGO, Estilo.TURQUESA);
        panel.setPreferredSize(new Dimension(380, 560));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JLabel lblTitulo = new JLabel("<html>Recuperar<br>contrase\u00f1a</html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Protege el acceso a tu cuenta");
        lblSub.setFont(Estilo.SUBTITULO);
        lblSub.setForeground(new Color(220, 226, 245));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel linea = new JPanel();
        linea.setBackground(Estilo.TURQUESA);
        linea.setMaximumSize(new Dimension(50, 4));
        linea.setAlignmentX(Component.LEFT_ALIGNMENT);

        IconoCandado lblIcono = new IconoCandado(Color.WHITE);
        lblIcono.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblFrase = new JLabel("<html><i>\u201cCada paso te acerca<br>a un mejor futuro\u201d</i></html>");
        lblFrase.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblFrase.setForeground(new Color(220, 226, 245));
        lblFrase.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(14));
        panel.add(linea);
        panel.add(Box.createVerticalGlue());
        panel.add(lblIcono);
        panel.add(Box.createVerticalGlue());
        panel.add(lblFrase);

        return panel;
    }

    private JPanel crearPasoUsuario() {
        JPanel panel = new JPanel();
        panel.setBackground(Estilo.BLANCO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 35, 30, 35));

        JLabel titulo = new JLabel("\u00bfCual es tu usuario?");
        titulo.setFont(Estilo.TITULO_GRANDE);
        titulo.setForeground(Estilo.INDIGO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Te mostraremos tu pregunta de seguridad");
        sub.setFont(Estilo.SUBTITULO);
        sub.setForeground(Estilo.GRIS_TEXTO);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        estilizarCampo(txtUsuario);

        BotonRedondeado btnContinuar = new BotonRedondeado("Continuar  \u2192", Estilo.AZUL);
        btnContinuar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnContinuar.setMaximumSize(new Dimension(310, 42));
        btnContinuar.addActionListener(e -> buscarPreguntaSeguridad());

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(sub);
        panel.add(Box.createVerticalStrut(30));
        panel.add(txtUsuario);
        panel.add(Box.createVerticalStrut(24));
        panel.add(btnContinuar);
        return panel;
    }

    private JPanel crearPasoRespuesta() {
        JPanel panel = new JPanel();
        panel.setBackground(Estilo.BLANCO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(35, 35, 20, 35));

        JLabel tituloPaso = new JLabel("Verificar identidad");
        tituloPaso.setFont(Estilo.TITULO);
        tituloPaso.setForeground(Estilo.INDIGO);
        tituloPaso.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPregunta = new JLabel(" ");
        lblPregunta.setFont(Estilo.NORMAL);
        lblPregunta.setForeground(Estilo.GRIS_TEXTO);
        lblPregunta.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtRespuesta = new JTextField();
        estilizarCampo(txtRespuesta);

        JLabel lblNueva = new JLabel("Nueva contrasena");
        lblNueva.setFont(Estilo.NORMAL);
        lblNueva.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtNuevaClave = new JPasswordField();
        estilizarCampo(txtNuevaClave);

        JLabel lblConfirmar = new JLabel("Confirmar contrasena");
        lblConfirmar.setFont(Estilo.NORMAL);
        lblConfirmar.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtConfirmarClave = new JPasswordField();
        estilizarCampo(txtConfirmarClave);

        BotonRedondeado btnCambiar = new BotonRedondeado("Actualizar contrasena", Estilo.TURQUESA);
        btnCambiar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCambiar.setMaximumSize(new Dimension(310, 42));
        btnCambiar.addActionListener(e -> cambiarContrasena());

        panel.add(tituloPaso);
        panel.add(Box.createVerticalStrut(14));
        panel.add(lblPregunta);
        panel.add(Box.createVerticalStrut(6));
        panel.add(txtRespuesta);
        panel.add(Box.createVerticalStrut(16));
        panel.add(lblNueva);
        panel.add(Box.createVerticalStrut(4));
        panel.add(txtNuevaClave);
        panel.add(Box.createVerticalStrut(14));
        panel.add(lblConfirmar);
        panel.add(Box.createVerticalStrut(4));
        panel.add(txtConfirmarClave);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnCambiar);
        return panel;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(Estilo.NORMAL);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(310, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.GRIS_BORDE, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void buscarPreguntaSeguridad() {
        String usuario = txtUsuario.getText().trim();
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe tu nombre de usuario.",
                    "Dato requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            UsuarioDAO dao = new UsuarioDAO();
            String pregunta = dao.obtenerPreguntaSeguridad(usuario);
            if (pregunta == null) {
                JOptionPane.showMessageDialog(this,
                        "No existe ningun usuario con ese nombre.",
                        "Usuario no encontrado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            usuarioEnProceso = usuario;
            lblPregunta.setText("<html>" + pregunta + "</html>");
            cardLayout.show(tarjeta, "PASO2");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar a la base de datos.\nDetalle: " + ex.getMessage(),
                    "Error de conexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarContrasena() {
        String respuesta = txtRespuesta.getText().trim();
        String nueva = new String(txtNuevaClave.getPassword());
        String confirmar = new String(txtConfirmarClave.getPassword());

        if (respuesta.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nueva.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "La nueva contrasena debe tener al menos 6 caracteres.",
                    "Contrasena debil", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!nueva.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contrasenas no coinciden.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            boolean respuestaCorrecta = dao.validarRespuestaSeguridad(usuarioEnProceso, respuesta);
            if (!respuestaCorrecta) {
                JOptionPane.showMessageDialog(this,
                        "La respuesta de seguridad es incorrecta.",
                        "Respuesta incorrecta", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dao.actualizarContrasena(usuarioEnProceso, nueva);
            JOptionPane.showMessageDialog(this,
                    "Contrasena actualizada correctamente. Ya puedes iniciar sesion.",
                    "Listo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar a la base de datos.\nDetalle: " + ex.getMessage(),
                    "Error de conexion", JOptionPane.ERROR_MESSAGE);
        }
    }
}