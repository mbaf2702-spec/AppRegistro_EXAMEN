package com.reclutamiento.vista;

import com.reclutamiento.dao.VacanteDAO;
import com.reclutamiento.modelo.Vacante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class VacantesForm extends JFrame {

    private final VacanteDAO vacanteDAO = new VacanteDAO();

    private JTextField txtCargo, txtDepartamento, txtSalario, txtFecha, txtBuscar;
    private JComboBox<String> comboEstado;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private int idSeleccionado = -1;

    public VacantesForm() {
        super("NexoTalento - Vacantes");
        construirInterfaz();
        cargarTabla(null);
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 640);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Estilo.FONDO);
        setLayout(new BorderLayout(15, 15));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        PanelDegradado panel = new PanelDegradado(Estilo.AZUL, Estilo.INDIGO);
        panel.setPreferredSize(new Dimension(1000, 70));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("<html><span style='font-size:18px; font-weight:bold;'>Gestion de Vacantes"
                + "</span><br><span style='font-size:11px;'>Administra los cargos disponibles</span></html>");
        lbl.setForeground(Color.WHITE);
        panel.add(lbl, BorderLayout.WEST);
        return panel;
    }

    private JScrollPane crearPanelFormulario() {
        TarjetaRedondeada tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 18);
        tarjeta.setPreferredSize(new Dimension(300, 560));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(22, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Informacion de la vacante");
        lblTitulo.setFont(Estilo.TITULO);
        lblTitulo.setForeground(Estilo.INDIGO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(16));

        txtCargo = campoTexto(tarjeta, "Cargo:");
        txtDepartamento = campoTexto(tarjeta, "Departamento:");
        txtSalario = campoTexto(tarjeta, "Salario ofertado ($):");
        txtFecha = campoTexto(tarjeta, "Fecha apertura (aaaa-mm-dd):");

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(Estilo.NORMAL);
        lblEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboEstado = new JComboBox<>(new String[]{"Abierta", "Cerrada"});
        comboEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboEstado.setMaximumSize(new Dimension(260, 32));
        tarjeta.add(lblEstado);
        tarjeta.add(comboEstado);
        tarjeta.add(Box.createVerticalStrut(20));

        BotonRedondeado btnGuardar = new BotonRedondeado("Guardar vacante", Estilo.AZUL);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(260, 40));
        btnGuardar.addActionListener(e -> guardar());

        BotonRedondeado btnNuevo = new BotonRedondeado("Nuevo / Limpiar", new Color(140, 140, 140));
        btnNuevo.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnNuevo.setMaximumSize(new Dimension(260, 38));
        btnNuevo.addActionListener(e -> limpiarFormulario());

        BotonRedondeado btnEliminar = new BotonRedondeado("Eliminar seleccionada", new Color(168, 50, 50));
        btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminar.setMaximumSize(new Dimension(260, 38));
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
                new Object[]{"ID", "Cargo", "Departamento", "Salario", "Fecha apertura", "Estado"}, 0) {
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
        tabla.getColumnModel().getColumn(5).setCellRenderer(new RenderizadorEstado());
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
        campo.setMaximumSize(new Dimension(260, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.GRIS_BORDE, 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        contenedor.add(lbl);
        contenedor.add(Box.createVerticalStrut(4));
        contenedor.add(campo);
        contenedor.add(Box.createVerticalStrut(12));
        return campo;
    }

    private void cargarTabla(String filtro) {
        try {
            List<Vacante> lista = (filtro == null || filtro.isEmpty())
                    ? vacanteDAO.listarTodas()
                    : vacanteDAO.buscarPorCargo(filtro);
            modeloTabla.setRowCount(0);
            for (Vacante v : lista) {
                modeloTabla.addRow(new Object[]{
                        v.getIdVacante(), v.getCargo(), v.getDepartamento(),
                        v.getSalarioOfertado(), v.getFechaApertura(), v.getEstado()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la lista de vacantes.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtCargo.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtDepartamento.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        txtSalario.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        txtFecha.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
        comboEstado.setSelectedItem(String.valueOf(modeloTabla.getValueAt(fila, 5)));
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtCargo.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
        txtFecha.setText("");
        comboEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private boolean validarFormulario() {
        if (txtCargo.getText().trim().isEmpty() || txtDepartamento.getText().trim().isEmpty()
                || txtSalario.getText().trim().isEmpty() || txtFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            BigDecimal salario = new BigDecimal(txtSalario.getText().trim());
            if (salario.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "El salario debe ser mayor a 0.",
                        "Valor invalido", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un numero valido, ej: 550.00",
                    "Formato invalido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Date.valueOf(txtFecha.getText().trim());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener el formato aaaa-mm-dd, ej: 2026-09-03",
                    "Formato invalido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void guardar() {
        if (!validarFormulario()) return;

        Vacante v = new Vacante();
        v.setIdVacante(idSeleccionado);
        v.setCargo(txtCargo.getText().trim());
        v.setDepartamento(txtDepartamento.getText().trim());
        v.setSalarioOfertado(new BigDecimal(txtSalario.getText().trim()));
        v.setFechaApertura(Date.valueOf(txtFecha.getText().trim()));
        v.setEstado((String) comboEstado.getSelectedItem());

        try {
            boolean exito = (idSeleccionado == -1) ? vacanteDAO.crear(v) : vacanteDAO.actualizar(v);
            if (exito) {
                JOptionPane.showMessageDialog(this,
                        idSeleccionado == -1 ? "Vacante registrada correctamente." : "Vacante actualizada correctamente.",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTabla(null);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la vacante.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una vacante de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "\u00bfSeguro que deseas eliminar esta vacante? Esta accion no se puede deshacer.",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            vacanteDAO.eliminar(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Vacante eliminada correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla(null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar: probablemente tiene aspirantes asociados.\nDetalle: " + ex.getMessage(),
                    "No se puede eliminar", JOptionPane.ERROR_MESSAGE);
        }
    }
}