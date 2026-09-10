package com.reclutamiento.vista;

import com.reclutamiento.dao.AspiranteDAO;
import com.reclutamiento.dao.EntrevistaDAO;
import com.reclutamiento.modelo.Aspirante;
import com.reclutamiento.modelo.Entrevista;
import com.reclutamiento.util.SesionActual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class EntrevistasForm extends JFrame {

    private static final String[] ESCALA_LIKERT = {
            "1 - Muy deficiente",
            "2 - Deficiente",
            "3 - Regular",
            "4 - Bueno",
            "5 - Excelente"
    };

    private final EntrevistaDAO entrevistaDAO = new EntrevistaDAO();
    private final AspiranteDAO aspiranteDAO = new AspiranteDAO();

    private JComboBox<Aspirante> comboAspirante;
    private JTextField txtFecha, txtEntrevistador, txtBuscar;
    private JComboBox<String> comboPuntaje;
    private JComboBox<String> comboResultado;
    private JTextArea txtObservaciones;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private int idSeleccionado = -1;

    private boolean esAdministrador() {
        return SesionActual.obtener() != null && "RRHH".equals(SesionActual.obtener().getRol());
    }

    public EntrevistasForm() {
        super("NexoTalento - Registro de Entrevistas");
        construirInterfaz();
        cargarAspirantesEnCombo();
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
        PanelDegradado panel = new PanelDegradado(Estilo.CORAL, Estilo.INDIGO);
        panel.setPreferredSize(new Dimension(1050, 75));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        String sub = esAdministrador()
                ? "Programa evaluaciones y registra resultados"
                : "Actualiza el resultado de las entrevistas asignadas";
        JLabel lbl = new JLabel("<html><span style='font-size:22px; font-weight:bold;'>REGISTRO DE ENTREVISTAS"
                + "</span><br><span style='font-size:11px;'>" + sub + "</span></html>");
        lbl.setForeground(Color.WHITE);
        panel.add(lbl, BorderLayout.WEST);
        return panel;
    }

    private JScrollPane crearPanelFormulario() {
        TarjetaRedondeada tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 18);
        tarjeta.setPreferredSize(new Dimension(300, 580));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(22, 20, 20, 20));

        JLabel lblTitulo = new JLabel(esAdministrador() ? "Datos de la entrevista" : "Actualizar entrevista");
        lblTitulo.setFont(Estilo.TITULO);
        lblTitulo.setForeground(Estilo.INDIGO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(16));

        JLabel lblAspirante = new JLabel("Aspirante:");
        lblAspirante.setFont(Estilo.NORMAL);
        lblAspirante.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboAspirante = new JComboBox<>();
        comboAspirante.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboAspirante.setMaximumSize(new Dimension(260, 30));
        comboAspirante.setEnabled(esAdministrador());
        tarjeta.add(lblAspirante);
        tarjeta.add(comboAspirante);
        tarjeta.add(Box.createVerticalStrut(12));

        txtFecha = campoTexto(tarjeta, "Fecha entrevista (aaaa-mm-dd):");
        txtEntrevistador = campoTexto(tarjeta, "Entrevistador:");

        JLabel lblPuntaje = new JLabel("Evaluaci\u00f3n (escala Likert):");
        lblPuntaje.setFont(Estilo.NORMAL);
        lblPuntaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboPuntaje = new JComboBox<>(ESCALA_LIKERT);
        comboPuntaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboPuntaje.setMaximumSize(new Dimension(260, 32));
        tarjeta.add(lblPuntaje);
        tarjeta.add(comboPuntaje);
        tarjeta.add(Box.createVerticalStrut(12));

        JLabel lblResultado = new JLabel("Resultado:");
        lblResultado.setFont(Estilo.NORMAL);
        lblResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboResultado = new JComboBox<>(new String[]{"Pendiente", "Aprobado", "No aprobado"});
        comboResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboResultado.setMaximumSize(new Dimension(260, 30));
        tarjeta.add(lblResultado);
        tarjeta.add(comboResultado);
        tarjeta.add(Box.createVerticalStrut(12));

        JLabel lblObs = new JLabel("Observaciones:");
        lblObs.setFont(Estilo.NORMAL);
        lblObs.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(txtObservaciones);
        scrollObs.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollObs.setMaximumSize(new Dimension(260, 80));
        tarjeta.add(lblObs);
        tarjeta.add(scrollObs);
        tarjeta.add(Box.createVerticalStrut(16));

        BotonRedondeado btnGuardar = new BotonRedondeado(
                esAdministrador() ? "Guardar" : "Actualizar resultado", Estilo.CORAL);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(260, 40));
        btnGuardar.addActionListener(e -> guardar());
        tarjeta.add(btnGuardar);
        tarjeta.add(Box.createVerticalStrut(8));

        if (esAdministrador()) {
            BotonRedondeado btnNuevo = new BotonRedondeado("Nuevo / Limpiar", new Color(140, 140, 140));
            btnNuevo.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnNuevo.setMaximumSize(new Dimension(260, 38));
            btnNuevo.addActionListener(e -> limpiarFormulario());
            tarjeta.add(btnNuevo);
            tarjeta.add(Box.createVerticalStrut(8));

            BotonRedondeado btnEliminar = new BotonRedondeado("Eliminar seleccionada", new Color(120, 40, 40));
            btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnEliminar.setMaximumSize(new Dimension(260, 38));
            btnEliminar.addActionListener(e -> eliminar());
            tarjeta.add(btnEliminar);
        }
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
                new Object[]{"ID", "Aspirante", "Fecha", "Entrevistador", "Puntaje", "Resultado"}, 0) {
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

    private void cargarAspirantesEnCombo() {
        try {
            List<Aspirante> aspirantes = aspiranteDAO.listarTodos();
            comboAspirante.removeAllItems();
            for (Aspirante a : aspirantes) {
                comboAspirante.addItem(a);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la lista de aspirantes.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla(String filtro) {
        try {
            List<Entrevista> lista = (filtro == null || filtro.isEmpty())
                    ? entrevistaDAO.listarTodas()
                    : entrevistaDAO.buscarPorAspirante(filtro);

            if (!esAdministrador()) {
                lista.removeIf(en -> !"Pendiente".equals(en.getResultado()));
            }

            modeloTabla.setRowCount(0);
            for (Entrevista en : lista) {
                modeloTabla.addRow(new Object[]{
                        en.getIdEntrevista(), en.getNombreAspirante(), en.getFechaEntrevista(),
                        en.getEntrevistador(), en.getPuntaje(), en.getResultado()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la lista de entrevistas.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        String nombreAspirante = String.valueOf(modeloTabla.getValueAt(fila, 1));
        for (int i = 0; i < comboAspirante.getItemCount(); i++) {
            Aspirante a = comboAspirante.getItemAt(i);
            String nombreCompleto = a.getNombres() + " " + a.getApellidos();
            if (nombreCompleto.equals(nombreAspirante)) {
                comboAspirante.setSelectedIndex(i);
                break;
            }
        }
        txtFecha.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        txtEntrevistador.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));

        String puntajeGuardado = String.valueOf(modeloTabla.getValueAt(fila, 4)).trim();
        boolean encontrado = false;
        for (int i = 0; i < ESCALA_LIKERT.length; i++) {
            if (ESCALA_LIKERT[i].startsWith(puntajeGuardado.split("\\.")[0])) {
                comboPuntaje.setSelectedIndex(i);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            comboPuntaje.setSelectedIndex(0);
        }

        comboResultado.setSelectedItem(String.valueOf(modeloTabla.getValueAt(fila, 5)));
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        if (comboAspirante.getItemCount() > 0) comboAspirante.setSelectedIndex(0);
        txtFecha.setText("");
        txtEntrevistador.setText("");
        comboPuntaje.setSelectedIndex(0);
        txtObservaciones.setText("");
        comboResultado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private boolean validarFormulario() {
        if (comboAspirante.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debes registrar al menos un aspirante antes de crear entrevistas.",
                    "Sin aspirantes", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtFecha.getText().trim().isEmpty() || txtEntrevistador.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
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

    private BigDecimal obtenerValorLikert() {
        String seleccion = (String) comboPuntaje.getSelectedItem();
        String numero = seleccion.split(" - ")[0].trim();
        return new BigDecimal(numero);
    }

    private void guardar() {
        if (!validarFormulario()) return;

        if (!esAdministrador() && idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una entrevista de la tabla para actualizar su resultado.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Entrevista en = new Entrevista();
        en.setIdEntrevista(idSeleccionado);
        en.setIdAspirante(((Aspirante) comboAspirante.getSelectedItem()).getIdAspirante());
        en.setFechaEntrevista(Date.valueOf(txtFecha.getText().trim()));
        en.setEntrevistador(txtEntrevistador.getText().trim());
        en.setPuntaje(obtenerValorLikert());
        en.setResultado((String) comboResultado.getSelectedItem());
        en.setObservaciones(txtObservaciones.getText().trim());
        en.setIdUsuario(SesionActual.obtener().getIdUsuario());

        try {
            boolean exito = (idSeleccionado == -1) ? entrevistaDAO.crear(en) : entrevistaDAO.actualizar(en);
            if (exito) {
                JOptionPane.showMessageDialog(this,
                        idSeleccionado == -1 ? "Entrevista registrada correctamente." : "Entrevista actualizada correctamente.",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTabla(null);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la entrevista.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una entrevista de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "\u00bfSeguro que deseas eliminar esta entrevista? Esta accion no se puede deshacer.",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            entrevistaDAO.eliminar(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Entrevista eliminada correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla(null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar la entrevista.\nDetalle: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}