package com.reclutamiento.vista;

import com.reclutamiento.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegistroUsuarioForm extends JFrame {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private JTextField txtNombreUsuario;
    private JPasswordField txtClave, txtConfirmarClave;
    private JComboBox<String> comboPregunta;
    private JTextField txtRespuesta;

    private static final String[] PREGUNTAS = {
            "\u00bfCual es el nombre de tu primera mascota?",
            "\u00bfEn que ciudad naciste?",
            "\u00bfCual es tu comida favorita?",
            "\u00bfCual es el nombre de tu mejor amigo de la infancia?"
    };

    public RegistroUsuarioForm() {
        super("NexoTalento - Crear cuenta de empleado");
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(crearPanelIzquierdo(), BorderLayout.WEST);
        add(crearPanelDerecho(), BorderLayout.CENTER);
    }

    private JPanel crearPanelIzquierdo() {
        PanelDegradado panel = new PanelDegradado(Estilo.TURQUESA, Estilo.AZUL);
        panel.setPreferredSize(new Dimension(340, 620));
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
            new AccesoEmpleadoForm().setVisible(true);
            dispose();
        });

        JLabel lblTitulo = new JLabel("<html>Bienvenido<br>al equipo</html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Crea tu cuenta para comenzar");
        lblSub.setFont(Estilo.SUBTITULO);
        lblSub.setForeground(new Color(230, 245, 250));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(btnVolver);
        panel.add(Box.createVerticalGlue());
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblSub);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JScrollPane crearPanelDerecho() {
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setBackground(Estilo.FONDO);

        TarjetaRedondeada tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 24);
        tarjeta.setPreferredSize(new Dimension(460, 560));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(35, 40, 30, 40));

        JLabel lblTitulo = new JLabel("Crear cuenta");
        lblTitulo.setFont(Estilo.TITULO_GRANDE);
        lblTitulo.setForeground(Estilo.INDIGO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(20));

        JLabel lblUsuario = new JLabel("Nombre de usuario");
        lblUsuario.setFont(Estilo.NORMAL);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtNombreUsuario = new JTextField();
        estilizarCampo(txtNombreUsuario);
        tarjeta.add(lblUsuario);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtNombreUsuario);
        tarjeta.add(Box.createVerticalStrut(16));

        JLabel lblClave = new JLabel("Contrasena (minimo 6 caracteres)");
        lblClave.setFont(Estilo.NORMAL);
        lblClave.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtClave = new JPasswordField();
        estilizarCampo(txtClave);
        tarjeta.add(lblClave);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtClave);
        tarjeta.add(Box.createVerticalStrut(16));

        JLabel lblConfirmar = new JLabel("Confirmar contrasena");
        lblConfirmar.setFont(Estilo.NORMAL);
        lblConfirmar.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtConfirmarClave = new JPasswordField();
        estilizarCampo(txtConfirmarClave);
        tarjeta.add(lblConfirmar);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtConfirmarClave);
        tarjeta.add(Box.createVerticalStrut(16));

        JLabel lblPregunta = new JLabel("Pregunta de seguridad");
        lblPregunta.setFont(Estilo.NORMAL);
        lblPregunta.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboPregunta = new JComboBox<>(PREGUNTAS);
        comboPregunta.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboPregunta.setMaximumSize(new Dimension(380, 36));
        tarjeta.add(lblPregunta);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(comboPregunta);
        tarjeta.add(Box.createVerticalStrut(16));

        JLabel lblRespuesta = new JLabel("Respuesta");
        lblRespuesta.setFont(Estilo.NORMAL);
        lblRespuesta.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtRespuesta = new JTextField();
        estilizarCampo(txtRespuesta);
        tarjeta.add(lblRespuesta);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtRespuesta);
        tarjeta.add(Box.createVerticalStrut(24));

        BotonRedondeado btnCrear = new BotonRedondeado("Crear cuenta", Estilo.TURQUESA);
        btnCrear.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCrear.setMaximumSize(new Dimension(380, 44));
        btnCrear.addActionListener(e -> registrar());
        tarjeta.add(btnCrear);

        contenedor.add(tarjeta);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(Estilo.NORMAL);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(380, 36));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.GRIS_BORDE, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void registrar() {
        String usuario = txtNombreUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());
        String confirmar = new String(txtConfirmarClave.getPassword());
        String pregunta = (String) comboPregunta.getSelectedItem();
        String respuesta = txtRespuesta.getText().trim();

        if (usuario.isEmpty() || clave.isEmpty() || confirmar.isEmpty() || respuesta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (clave.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contrasena debe tener al menos 6 caracteres.",
                    "Contrasena debil", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!clave.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contrasenas no coinciden.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (usuarioDAO.existeUsuario(usuario)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe una cuenta con ese nombre de usuario.",
                        "Usuario duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exito = usuarioDAO.crear(usuario, clave, pregunta, respuesta, "RRHH");
            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Cuenta creada correctamente. Ya puedes iniciar sesion.",
                        "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                new LoginForm().setVisible(true);
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo crear la cuenta.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}