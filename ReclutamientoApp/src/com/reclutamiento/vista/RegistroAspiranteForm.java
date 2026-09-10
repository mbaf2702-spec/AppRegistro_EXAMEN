package com.reclutamiento.vista;

import com.reclutamiento.dao.AspiranteDAO;
import com.reclutamiento.dao.VacanteDAO;
import com.reclutamiento.modelo.Aspirante;
import com.reclutamiento.modelo.Vacante;
import com.reclutamiento.util.ValidadorCedula;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RegistroAspiranteForm extends JFrame {

    private final AspiranteDAO aspiranteDAO = new AspiranteDAO();
    private final VacanteDAO vacanteDAO = new VacanteDAO();

    private JTextField txtCedula, txtNombres, txtApellidos, txtTelefono, txtCorreo;
    private JComboBox<Vacante> comboVacante;

    public RegistroAspiranteForm() {
        super("NexoTalento - Postulacion");
        construirInterfaz();
        cargarVacantesEnCombo();
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
        PanelDegradado panel = new PanelDegradado(Estilo.TURQUESA, Estilo.INDIGO);
        panel.setPreferredSize(new Dimension(340, 620));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JLabel lblTitulo = new JLabel("<html>Postula a tu<br>pr\u00f3xima<br>oportunidad</html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Nosotros te contactaremos");
        lblSub.setFont(Estilo.SUBTITULO);
        lblSub.setForeground(new Color(230, 245, 243));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        tarjeta.setPreferredSize(new Dimension(460, 600));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(35, 40, 30, 40));

        JLabel lblTitulo = new JLabel("Datos del aspirante");
        lblTitulo.setFont(Estilo.TITULO_GRANDE);
        lblTitulo.setForeground(Estilo.INDIGO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(4));

        JLabel lblAyuda = new JLabel("C\u00e9dula y tel\u00e9fono: solo n\u00fameros, sin espacios ni guiones");
        lblAyuda.setFont(Estilo.SUBTITULO);
        lblAyuda.setForeground(Estilo.GRIS_TEXTO);
        lblAyuda.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(lblAyuda);
        tarjeta.add(Box.createVerticalStrut(18));

        txtCedula = campoConEtiqueta(tarjeta, "C\u00e9dula (10 d\u00edgitos)");
        txtNombres = campoConEtiqueta(tarjeta, "Nombres");
        txtApellidos = campoConEtiqueta(tarjeta, "Apellidos");
        txtTelefono = campoConEtiqueta(tarjeta, "Tel\u00e9fono");
        txtCorreo = campoConEtiqueta(tarjeta, "Correo electr\u00f3nico");

        JLabel lblVacante = new JLabel("Vacante a la que aplicas");
        lblVacante.setFont(Estilo.NORMAL);
        lblVacante.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboVacante = new JComboBox<>();
        comboVacante.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboVacante.setMaximumSize(new Dimension(380, 36));
        tarjeta.add(lblVacante);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(comboVacante);
        tarjeta.add(Box.createVerticalStrut(26));

        BotonRedondeado btnPostular = new BotonRedondeado("Enviar postulacion", Estilo.TURQUESA);
        btnPostular.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPostular.setMaximumSize(new Dimension(380, 44));
        btnPostular.addActionListener(e -> postular());
        tarjeta.add(btnPostular);

        contenedor.add(tarjeta);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JTextField campoConEtiqueta(JPanel contenedor, String etiqueta) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(Estilo.NORMAL);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField campo = new JTextField();
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(380, 36));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.GRIS_BORDE, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        contenedor.add(lbl);
        contenedor.add(Box.createVerticalStrut(4));
        contenedor.add(campo);
        contenedor.add(Box.createVerticalStrut(16));
        return campo;
    }

    private void cargarVacantesEnCombo() {
        try {
            List<Vacante> vacantes = vacanteDAO.listarTodas();
            comboVacante.removeAllItems();
            for (Vacante v : vacantes) {
                if ("Abierta".equals(v.getEstado())) {
                    comboVacante.addItem(v);
                }
            }
            if (comboVacante.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "En este momento no hay vacantes abiertas disponibles.",
                        "Sin vacantes", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la lista de vacantes.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String soloDigitos(String texto) {
        return texto == null ? "" : texto.replaceAll("[^0-9]", "");
    }

    private boolean validarFormulario() {
        if (txtCedula.getText().trim().isEmpty() || txtNombres.getText().trim().isEmpty()
                || txtApellidos.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()
                || txtCorreo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String cedulaLimpia = soloDigitos(txtCedula.getText());
        if (!ValidadorCedula.esValida(cedulaLimpia)) {
            JOptionPane.showMessageDialog(this,
                    "La c\u00e9dula ingresada no es v\u00e1lida. Verifica que est\u00e9 escrita correctamente.",
                    "C\u00e9dula inv\u00e1lida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        txtCedula.setText(cedulaLimpia);

        if (!txtCorreo.getText().trim().matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "El correo no tiene un formato v\u00e1lido, ej: nombre@correo.com",
                    "Formato inv\u00e1lido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String telefonoLimpio = soloDigitos(txtTelefono.getText());
        if (telefonoLimpio.length() < 7 || telefonoLimpio.length() > 10) {
            JOptionPane.showMessageDialog(this,
                    "El tel\u00e9fono debe tener entre 7 y 10 d\u00edgitos, solo n\u00fameros.",
                    "Formato inv\u00e1lido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        txtTelefono.setText(telefonoLimpio);

        if (comboVacante.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No hay ninguna vacante disponible para seleccionar.",
                    "Sin vacantes", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void postular() {
        if (!validarFormulario()) return;

        String cedula = txtCedula.getText().trim();

        try {
            if (aspiranteDAO.existeCedula(cedula)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe una postulaci\u00f3n registrada con esa c\u00e9dula.",
                        "C\u00e9dula duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Aspirante a = new Aspirante();
            a.setCedula(cedula);
            a.setNombres(txtNombres.getText().trim());
            a.setApellidos(txtApellidos.getText().trim());
            a.setTelefono(txtTelefono.getText().trim());
            a.setCorreo(txtCorreo.getText().trim());
            a.setEstado("En proceso");
            a.setIdVacante(((Vacante) comboVacante.getSelectedItem()).getIdVacante());
            a.setIdUsuario(0);

            boolean exito = aspiranteDAO.crear(a);
            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Su postulaci\u00f3n ha sido registrada con \u00e9xito. En los pr\u00f3ximos "
                                + "d\u00edas nos comunicaremos con usted.",
                        "Postulaci\u00f3n recibida", JOptionPane.INFORMATION_MESSAGE);
                new RolInicialForm().setVisible(true);
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar la postulacion.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}