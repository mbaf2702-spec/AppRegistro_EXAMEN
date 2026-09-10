package com.reclutamiento.vista;

import com.reclutamiento.dao.UsuarioDAO;
import com.reclutamiento.modelo.Usuario;
import com.reclutamiento.util.SesionActual;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private TarjetaRedondeada tarjeta;

    public LoginForm() {
        super("NexoTalento - Sistema de Reclutamiento");
        construirInterfaz();
        iniciarAnimacionEntrada();
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

        JLabel lblTitulo = new JLabel("<html>Reclutamiento<br>y Selecci\u00f3n</html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Gesti\u00f3n inteligente del talento");
        lblSub.setFont(Estilo.SUBTITULO);
        lblSub.setForeground(new Color(220, 226, 245));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel linea = new JPanel();
        linea.setBackground(Estilo.TURQUESA);
        linea.setMaximumSize(new Dimension(50, 4));
        linea.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIcono = new JLabel("\uD83E\uDD1D");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblFrase = new JLabel("<html><i>\u201cEl talento adecuado<br>construye un mejor ma\u00f1ana\u201d</i></html>");
        lblFrase.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblFrase.setForeground(new Color(220, 226, 245));
        lblFrase.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(btnVolver);
        panel.add(Box.createVerticalStrut(20));
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

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Estilo.FONDO);

        tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 24);
        tarjeta.setPreferredSize(new Dimension(380, 430));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(40, 35, 30, 35));

        JLabel lblBienvenido = new JLabel("Bienvenido");
        lblBienvenido.setFont(Estilo.TITULO_GRANDE);
        lblBienvenido.setForeground(Estilo.INDIGO);
        lblBienvenido.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIngresaDatos = new JLabel("Ingresa tus datos para continuar");
        lblIngresaDatos.setFont(Estilo.SUBTITULO);
        lblIngresaDatos.setForeground(Estilo.GRIS_TEXTO);
        lblIngresaDatos.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(Estilo.NORMAL);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        estilizarCampo(txtUsuario);

        JLabel lblClave = new JLabel("Contrase\u00f1a");
        lblClave.setFont(Estilo.NORMAL);
        lblClave.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtClave = new JPasswordField();
        estilizarCampo(txtClave);

        BotonRedondeado btnIngresar = new BotonRedondeado("Ingresar  \u2192", Estilo.AZUL);
        btnIngresar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(310, 42));
        btnIngresar.addActionListener(e -> intentarLogin());

        JButton btnOlvide = new JButton("\u00bfOlvidaste tu contrase\u00f1a?");
        btnOlvide.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnOlvide.setBorderPainted(false);
        btnOlvide.setContentAreaFilled(false);
        btnOlvide.setForeground(Estilo.AZUL);
        btnOlvide.setFont(Estilo.NORMAL);
        btnOlvide.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOlvide.addActionListener(e -> new RecuperarContrasenaForm().setVisible(true));

        tarjeta.add(lblBienvenido);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblIngresaDatos);
        tarjeta.add(Box.createVerticalStrut(28));
        tarjeta.add(lblUsuario);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(txtUsuario);
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(lblClave);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(txtClave);
        tarjeta.add(Box.createVerticalStrut(28));
        tarjeta.add(btnIngresar);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(btnOlvide);

        panel.add(tarjeta);
        txtClave.addActionListener(e -> intentarLogin());
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

    private void iniciarAnimacionEntrada() {
        tarjeta.setAlpha(0f);
        Timer timer = new Timer(15, null);
        timer.addActionListener(new java.awt.event.ActionListener() {
            float progreso = 0f;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                progreso += 0.05f;
                if (progreso >= 1f) {
                    progreso = 1f;
                    timer.stop();
                }
                tarjeta.setAlpha(progreso);
            }
        });
        timer.start();
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());

        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar usuario y contrasena.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = dao.validarLogin(usuario, clave);
            if (u == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contrasena incorrectos.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                txtClave.setText("");
                return;
            }
            SesionActual.iniciar(u);
            new MenuPrincipal().setVisible(true);
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar a la base de datos.\nDetalle: " + ex.getMessage(),
                    "Error de conexion", JOptionPane.ERROR_MESSAGE);
        }
    }
}