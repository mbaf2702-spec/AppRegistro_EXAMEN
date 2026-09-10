package com.reclutamiento.vista;

import com.reclutamiento.dao.AspiranteDAO;
import com.reclutamiento.dao.EntrevistaDAO;
import com.reclutamiento.dao.VacanteDAO;
import com.reclutamiento.util.SesionActual;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MenuPrincipal extends JFrame {

    private final VacanteDAO vacanteDAO = new VacanteDAO();
    private final AspiranteDAO aspiranteDAO = new AspiranteDAO();
    private final EntrevistaDAO entrevistaDAO = new EntrevistaDAO();

    public MenuPrincipal() {
        super("NexoTalento - Menu Principal");
        construirInterfaz();
    }

    private boolean esAdministrador() {
        return SesionActual.obtener() != null && "RRHH".equals(SesionActual.obtener().getRol());
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Estilo.FONDO);
        setLayout(new BorderLayout());

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        PanelDegradado panel = new PanelDegradado(Estilo.INDIGO, Estilo.AZUL);
        panel.setPreferredSize(new Dimension(950, 90));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        String nombreUsuario = SesionActual.obtener() != null
                ? SesionActual.obtener().getNombreUsuario() : "Invitado";
        String subtitulo = esAdministrador()
            ? "Panel de administraci\u00f3n"
            : "Supervisi\u00f3n de entrevistas";

        JLabel lblTitulo = new JLabel("<html><span style='font-size:20px; font-weight:bold;'>Bienvenido, "
                + nombreUsuario + "</span><br><span style='font-size:12px;'>" + subtitulo + "</span></html>");
        lblTitulo.setForeground(Color.WHITE);
        panel.add(lblTitulo, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar sesion");
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBackground(Color.WHITE);
        btnCerrarSesion.setForeground(Estilo.INDIGO);
        btnCerrarSesion.setFont(Estilo.NORMAL);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        JPanel envoltorio = new JPanel(new GridBagLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(btnCerrarSesion);
        panel.add(envoltorio, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(Estilo.FONDO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        contenedor.add(crearFilaEstadisticas());
        contenedor.add(Box.createVerticalStrut(25));
        contenedor.add(crearFilaModulos());

        return contenedor;
    }

    private JPanel crearFilaEstadisticas() {
        int vacantesAbiertas = 0, totalAspirantes = 0, entrevistasPendientes = 0;
        try {
            vacantesAbiertas = vacanteDAO.contarAbiertas();
            totalAspirantes = aspiranteDAO.contarTodos();
            entrevistasPendientes = entrevistaDAO.contarPendientes();
        } catch (SQLException ex) {
            System.err.println("No se pudieron cargar las estadisticas: " + ex.getMessage());
        }

        JPanel fila;
        if (esAdministrador()) {
            fila = new JPanel(new GridLayout(1, 3, 20, 0));
            fila.setOpaque(false);
            fila.add(crearTarjetaEstadistica(String.valueOf(vacantesAbiertas), "Vacantes activas", Estilo.AZUL));
            fila.add(crearTarjetaEstadistica(String.valueOf(totalAspirantes), "Aspirantes registrados", Estilo.TURQUESA));
            fila.add(crearTarjetaEstadistica(String.valueOf(entrevistasPendientes), "Entrevistas pendientes", Estilo.CORAL));
        } else {
            fila = new JPanel(new GridLayout(1, 1));
            fila.setOpaque(false);
            fila.add(crearTarjetaEstadistica(String.valueOf(entrevistasPendientes),
                    "Entrevistas pendientes por revisar", Estilo.CORAL));
        }
        return fila;
    }

    private JPanel crearTarjetaEstadistica(String numero, String etiqueta, Color color) {
        TarjetaRedondeada tarjeta = new TarjetaRedondeada(Estilo.BLANCO, 16);
        tarjeta.setPreferredSize(new Dimension(200, 90));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel lblNumero = new JLabel(numero);
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNumero.setForeground(color);
        lblNumero.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(Estilo.SUBTITULO);
        lblEtiqueta.setForeground(Estilo.GRIS_TEXTO);
        lblEtiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblNumero);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblEtiqueta);
        return tarjeta;
    }

    private JPanel crearFilaModulos() {
        JPanel fila;
        if (esAdministrador()) {
            fila = new JPanel(new GridLayout(1, 3, 20, 0));
            fila.setOpaque(false);
            fila.add(crearTarjetaModulo("Vacantes", "Gestionar cargos disponibles", Estilo.AZUL,
                    () -> new VacantesForm().setVisible(true)));
            fila.add(crearTarjetaModulo("Aspirantes", "Registrar y dar seguimiento a candidatos", Estilo.TURQUESA,
                    () -> new AspirantesForm().setVisible(true)));
            fila.add(crearTarjetaModulo("Entrevistas", "Registrar evaluaciones", Estilo.CORAL,
                    () -> new EntrevistasForm().setVisible(true)));
        } else {
            fila = new JPanel(new GridLayout(1, 1));
            fila.setOpaque(false);
            fila.add(crearTarjetaModulo("Entrevistas pendientes",
                    "Revisa y actualiza el resultado de las entrevistas asignadas", Estilo.CORAL,
                    () -> new EntrevistasForm().setVisible(true)));
        }
        return fila;
    }

    private JPanel crearTarjetaModulo(String titulo, String descripcion, Color color, Runnable accion) {
        TarjetaRedondeada tarjeta = new TarjetaRedondeada(color, 20);
        tarjeta.setPreferredSize(new Dimension(240, 190));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(25, 22, 22, 22));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("<html><div style='width:180px'>" + descripcion + "</div></html>");
        lblDescripcion.setFont(Estilo.SUBTITULO);
        lblDescripcion.setForeground(new Color(240, 240, 240));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        BotonRedondeado btnAbrir = new BotonRedondeado("Abrir  \u2192", color.darker());
        btnAbrir.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAbrir.setMaximumSize(new Dimension(160, 38));
        btnAbrir.addActionListener(e -> accion.run());

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblDescripcion);
        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(btnAbrir);

        return tarjeta;
    }

    private void cerrarSesion() {
        SesionActual.cerrar();
        new LoginForm().setVisible(true);
        dispose();
    }
}