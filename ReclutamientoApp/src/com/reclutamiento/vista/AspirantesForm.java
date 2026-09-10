package com.reclutamiento.vista;

import com.reclutamiento.dao.AspiranteDAO;
import com.reclutamiento.dao.VacanteDAO;
import com.reclutamiento.modelo.Aspirante;
import com.reclutamiento.modelo.Vacante;
import com.reclutamiento.util.SesionActual;
import com.reclutamiento.util.ValidadorCedula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AspirantesForm extends JFrame {

    private final AspiranteDAO aspiranteDAO = new AspiranteDAO();
    private final VacanteDAO vacanteDAO = new VacanteDAO();

    private JTextField txtCedula, txtNombres, txtApellidos, txtTelefono, txtCorreo, txtBuscar;
    private JComboBox<Vacante> comboVacante;
    private JComboBox<String> comboEstado;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private int idSeleccionado = -1;
    private String cedulaOriginal = null;

    public AspirantesForm() {
        super("NexoTalento - Aspirantes");
        construirInterfaz();
        cargarVacantesEnCombo();
        cargarTabla(null);
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1050, 660);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Estilo.FONDO);
        setLayout(new BorderLayout(15, 15));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        PanelDegradado panel = new PanelDegradado(Estilo.TURQUESA, Estilo.INDIGO);
        panel.setPreferredSize(new Dimension(1050, 70));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("<html><span style='font-size:18px; font-weight:bold;'>Gesti\u00f3n de Aspirantes"
                + "</span><br><span style='font-size:11px;'>Registra y da seguimiento a cada candidato</span></html>");
        lbl.setForeground(Color.WHITE);
        panel.add(lbl, BorderLayout.WEST);
        return panel;
    }

    private JScrollPane crearPanelFormulario() {
        TarjetaRedondeada tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 18);
        tarjeta.setPreferredSize(new Dimension(310, 580));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(22, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Datos del aspirante");
        lblTitulo.setFont(Estilo.TITULO);
        lblTitulo.setForeground(Estilo.INDIGO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(16));

        txtCedula = campoTexto(tarjeta, "C\u00e9dula (10 d\u00edgitos):");
        txtNombres = campoTexto(tarjeta, "Nombres:");
        txtApellidos = campoTexto(tarjeta, "Apellidos:");
        txtTelefono = campoTexto(tarjeta, "Tel\u00e9fono:");
        txtCorreo = campoTexto(tarjeta, "Correo:");

        JLabel lblVacante = new JLabel("Vacante a la que aplica:");
        lblVacante.setFont(Estilo.NORMAL);
        lblVacante.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboVacante = new JComboBox<>();
        comboVacante.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboVacante.setMaximumSize(new Dimension(270, 32));
        tarjeta.add(lblVacante);
        tarjeta.add(comboVacante);
        tarjeta.add(Box.createVerticalStrut(12));

        JLabel lblEstado = new JLabel("Estado del proceso:");
        lblEstado.setFont(Estilo.NORMAL);
        lblEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboEstado = new JComboBox<>(new String[]{"En proceso", "Contratado", "Rechazado"});
        comboEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboEstado.setMaximumSize(new Dimension(270, 32));
        tarjeta.add(lblEstado);
        tarjeta.add(comboEstado);
        tarjeta.add(Box.createVerticalStrut(20));

        BotonRedondeado btnGuardar = new BotonRedondeado("Guardar aspirante", Estilo.TURQUESA);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(270, 40));
        btnGuardar.addActionListener(e -> guardar());

        BotonRedondeado btnNuevo = new BotonRedondeado("Nuevo / Limpiar", new Color(140, 140, 140));
        btnNuevo.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnNuevo.setMaximumSize(new Dimension(270, 38));
        btnNuevo.addActionListener(e -> limpiarFormulario());

        BotonRedondeado btnEliminar = new BotonRedondeado("Eliminar seleccionado", new Color(168, 50, 50));
        btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminar.setMaximumSize(new Dimension(270, 38));
        btnEliminar.addActionListener(e -> eliminar());

        tarjeta.add(btnGuardar);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(btnNuevo);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(btnEliminar);
        tarjeta.add(Box.createVerticalStrut(10));

        JScrollPane scroll = new JScrollPane(tarjeta);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel crearPanelTabla() {
        TarjetaRedondeada tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 18);
        tarjeta.setLayout(new BorderLayout(8, 8));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setOpaque(false);
        txtBuscar = new JTextField();
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.GRIS_BORDE, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        BotonRedondeado btnBuscar = new BotonRedondeado("Buscar", Estilo.INDIGO);
        btnBuscar.setPreferredSize(new Dimension(110, 32));
        btnBuscar.addActionListener(e -> cargarTabla(txtBuscar.getText().trim()));
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);
        tarjeta.add(panelBusqueda, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "C\u00e9dula", "Nombres", "Apellidos", "Tel\u00e9fono", "Correo", "Vacante", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(Estilo.FONDO);
        tabla.setSelectionBackground(new Color(230, 236, 250));
        tabla.getColumnModel().getColumn(7).setCellRenderer(new RenderizadorEstado());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                cargarSeleccionEnFormulario();
            }
        });
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(null);
        tarjeta.add(scrollTabla, BorderLayout.CENTER);

        return tarjeta;
    }

    private JTextField campoTexto(JPanel contenedor, String etiqueta) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(Estilo.NORMAL);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField campo = new JTextField();
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(270, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.GRIS_BORDE, 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        contenedor.add(lbl);
        contenedor.add(Box.createVerticalStrut(4));
        contenedor.add(campo);
        contenedor.add(Box.createVerticalStrut(12));
        return campo;
    }

    private void cargarVacantesEnCombo() {
        try {
            List<Vacante> vacantes = vacanteDAO.listarTodas();
            comboVacante.removeAllItems();
            for (Vacante v : vacantes) {
                comboVacante.addItem(v);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la lista de vacantes.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla(String filtro) {
        try {
            List<Aspirante> lista = (filtro == null || filtro.isEmpty())
                    ? aspiranteDAO.listarTodos()
                    : aspiranteDAO.buscar(filtro);
            modeloTabla.setRowCount(0);
            for (Aspirante a : lista) {
                modeloTabla.addRow(new Object[]{
                        a.getIdAspirante(), a.getCedula(), a.getNombres(), a.getApellidos(),
                        a.getTelefono(), a.getCorreo(), a.getCargoVacante(), a.getEstado()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la lista de aspirantes.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        cedulaOriginal = String.valueOf(modeloTabla.getValueAt(fila, 1));
        txtCedula.setText(cedulaOriginal);
        txtNombres.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        txtApellidos.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        txtTelefono.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
        txtCorreo.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));
        String cargoVacante = String.valueOf(modeloTabla.getValueAt(fila, 6));
        for (int i = 0; i < comboVacante.getItemCount(); i++) {
            if (comboVacante.getItemAt(i).getCargo().equals(cargoVacante)) {
                comboVacante.setSelectedIndex(i);
                break;
            }
        }
        comboEstado.setSelectedItem(String.valueOf(modeloTabla.getValueAt(fila, 7)));
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        cedulaOriginal = null;
        txtCedula.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        if (comboVacante.getItemCount() > 0) comboVacante.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private boolean validarFormulario() {
        if (txtCedula.getText().trim().isEmpty() || txtNombres.getText().trim().isEmpty()
                || txtApellidos.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()
                || txtCorreo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String cedula = txtCedula.getText().replaceAll("[^0-9]", "");
        if (!ValidadorCedula.esValida(cedula)) {
            JOptionPane.showMessageDialog(this, "La c\u00e9dula ingresada no es v\u00e1lida.",
                    "C\u00e9dula inv\u00e1lida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        txtCedula.setText(cedula);
        if (!txtCorreo.getText().trim().matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "El correo no tiene un formato v\u00e1lido, ej: nombre@correo.com",
                    "Formato inv\u00e1lido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String telefono = txtTelefono.getText().replaceAll("[^0-9]", "");
        if (telefono.length() < 7 || telefono.length() > 10) {
            JOptionPane.showMessageDialog(this, "El tel\u00e9fono debe tener entre 7 y 10 d\u00edgitos num\u00e9ricos.",
                    "Formato inv\u00e1lido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        txtTelefono.setText(telefono);
        if (comboVacante.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debes registrar al menos una vacante antes de agregar aspirantes.",
                    "Sin vacantes", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void guardar() {
        if (!validarFormulario()) return;

        String cedula = txtCedula.getText().trim();
        boolean esNuevo = (idSeleccionado == -1);
        boolean cambioCedula = !esNuevo && !cedula.equals(cedulaOriginal);

        try {
            if ((esNuevo || cambioCedula) && aspiranteDAO.existeCedula(cedula)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un aspirante registrado con esa c\u00e9dula.",
                        "C\u00e9dula duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Aspirante a = new Aspirante();
            a.setIdAspirante(idSeleccionado);
            a.setCedula(cedula);
            a.setNombres(txtNombres.getText().trim());
            a.setApellidos(txtApellidos.getText().trim());
            a.setTelefono(txtTelefono.getText().trim());
            a.setCorreo(txtCorreo.getText().trim());
            a.setEstado((String) comboEstado.getSelectedItem());
            a.setIdVacante(((Vacante) comboVacante.getSelectedItem()).getIdVacante());
            a.setIdUsuario(SesionActual.obtener().getIdUsuario());

            boolean exito = esNuevo ? aspiranteDAO.crear(a) : aspiranteDAO.actualizar(a);
            if (exito) {
                JOptionPane.showMessageDialog(this,
                        esNuevo ? "Aspirante registrado correctamente." : "Aspirante actualizado correctamente.",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTabla(null);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el aspirante.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aspirante de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "\u00bfSeguro que deseas eliminar este aspirante?\n"
                        + "Se eliminaran tambien todas sus entrevistas registradas.",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            aspiranteDAO.eliminar(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Aspirante eliminado correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla(null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el aspirante.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}