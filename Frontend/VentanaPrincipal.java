package Frontend;
import Backend.GestorPreguntas;
import Backend.Pregunta;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Ventana principal del programa que muestra la interfaz para cargar
 * el archivo de preguntas e iniciar la prueba.
 */
public class VentanaPrincipal extends JFrame {

    // Componentes de la interfaz
    private JLabel labelEstado;
    private JLabel labelResumen;
    private JButton botonCargar;
    private JButton botonIniciar;

    // Colores para la interfaz
    private Color colorPrimario = new Color(70, 130, 180);
    private Color colorSecundario = new Color(176, 196, 222);
    private Color colorTexto = new Color(44, 62, 80);
    
    // Fuentes 
    private Font fuenteTitulo = new Font("Arial", Font.BOLD, 16);
    private Font fuenteNormal = new Font("Arial", Font.PLAIN, 14);

    // Backend
    private Backend.GestorPreguntas gestor = new Backend.GestorPreguntas();

    public VentanaPrincipal() {
        super("Tarea 2 - Prueba con Taxonom\u00eda de Bloom");

        // Configuración básica de la ventana
        setLayout(new BorderLayout(20, 20));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);  // Centra la ventana en la pantalla
        getContentPane().setBackground(Color.WHITE);

        // Creación de los paneles principales
        crearPanelSuperior();
        crearPanelCentral();
        crearPanelInferior();

        // Eventos de los botones
        botonCargar.addActionListener(e -> cargarArchivo());
        botonIniciar.addActionListener(e -> iniciarPrueba());

        setVisible(true);
    }

    // Crea la parte superior con el título y botón de carga
    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout(15, 15));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titulo = new JLabel("Sistema de Pruebas - Taxonom\u00eda de Bloom", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(colorPrimario);
        panelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel panelBotonCargar = new JPanel();
        panelBotonCargar.setBackground(Color.WHITE);
        botonCargar = crearBotonEstilizado("Cargar archivo de preguntas");
        panelBotonCargar.add(botonCargar);
        panelSuperior.add(panelBotonCargar, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
    }

    // Panel central con el estado y botón de inicio
    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(20, 0));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 20, 10, 20),
            BorderFactory.createLineBorder(colorSecundario, 2, true)
        ));

        labelEstado = new JLabel("Archivo no cargado", SwingConstants.CENTER);
        labelEstado.setFont(fuenteTitulo);
        labelEstado.setForeground(colorTexto);
        
        JPanel panelBotonIniciar = new JPanel();
        panelBotonIniciar.setBackground(Color.WHITE);
        botonIniciar = crearBotonEstilizado("Iniciar prueba");
        botonIniciar.setEnabled(false);  // Deshabilitado hasta que se cargue un archivo
        panelBotonIniciar.add(botonIniciar);

        panelCentral.add(labelEstado, BorderLayout.CENTER);
        panelCentral.add(panelBotonIniciar, BorderLayout.EAST);

        add(panelCentral, BorderLayout.CENTER);
    }

    // Panel inferior con el resumen
    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        labelResumen = new JLabel("Total: 0 preguntas. Tiempo: 0 segundos", SwingConstants.CENTER);
        labelResumen.setFont(fuenteNormal);
        labelResumen.setForeground(colorTexto);
        panelInferior.add(labelResumen, BorderLayout.CENTER);

        add(panelInferior, BorderLayout.SOUTH);
    }

    // Crea botones con estilo personalizado
    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(fuenteNormal);
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorPrimario);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto al pasar el mouse
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorPrimario.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorPrimario);
            }
        });
        
        return boton;
    }

    // Maneja la carga del archivo
    private void cargarArchivo() {
    JFileChooser selector = new JFileChooser();
    selector.setDialogTitle("Seleccionar archivo de preguntas");
    int resultado = selector.showOpenDialog(this);

    if (resultado == JFileChooser.APPROVE_OPTION) {
        File archivo = selector.getSelectedFile();
        
        try {
            gestor.cargarDesdeArchivo(archivo.getAbsolutePath());
for (Pregunta p : gestor.getPreguntas()) {
    System.out.println("Tipo: " + p.getTipo());
    System.out.println("Enunciado: " + p.getEnunciado());
    System.out.println("Opciones: " + p.getOpciones());
    System.out.println("Respuesta correcta: " + p.getRespuestaCorrecta());
    System.out.println("Tiempo estimado (segundos): " + p.getTiempoEstimado());
    System.out.println("-----------------------------");
}
            if (gestor.estaCargadoCorrectamente()) {
                labelEstado.setText("Archivo cargado: " + archivo.getName());
                labelResumen.setText("Total: " + gestor.getCantidadPreguntas() +
                                     " preguntas. Tiempo: " + (gestor.obtenerTiempoTotal())/60 + " minutos" + (gestor.obtenerTiempoTotal())%60 + "segundos");
                botonIniciar.setEnabled(true);
            } else {
                labelEstado.setText("Error: el archivo no contiene preguntas válidas.");
                labelResumen.setText("Total: 0 preguntas. Tiempo: 0 segundos");
                botonIniciar.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar el archivo:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            labelEstado.setText("Archivo no cargado");
            labelResumen.setText("Total: 0 preguntas. Tiempo: 0 segundos");
            botonIniciar.setEnabled(false);
        }
    }
}

    // Inicia la prueba
    private void iniciarPrueba() {
    // Configura el estilo del diálogo
    UIManager.put("OptionPane.messageFont", fuenteNormal);
    UIManager.put("OptionPane.buttonFont", fuenteNormal);
    UIManager.put("OptionPane.background", Color.WHITE);
    UIManager.put("Panel.background", Color.WHITE);

    JOptionPane.showMessageDialog(
        this,
        "\u00a1Bienvenido \nPresione OK para comenzar.",
        "Inicio de Prueba",
        JOptionPane.INFORMATION_MESSAGE
    );

    // Abre la nueva ventana de opciones (el examen)
    PantallaOpciones pantallaOpciones = new PantallaOpciones(gestor);
    pantallaOpciones.setVisible(true);

    // Cierra la ventana actual
    this.dispose();
}

} 