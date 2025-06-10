package Frontend;

import Backend.Pregunta;
import Backend.GestorPreguntas;
import Backend.GestorPreguntasResultados;

import javax.swing.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;import java.awt.event.ActionEvent;
import java.util.*;
import java.awt.BorderLayout;


/**
 * Pantalla que muestra una pregunta y opciones tipo test.
 */
public class PantallaOpciones extends JFrame {

    private JLabel labelPregunta;
    private JRadioButton[] opcionesRadio;
    private ButtonGroup grupoOpciones;
    private JButton botonAnterior;
    private JButton botonSiguiente;

    private GestorPreguntas gestor;
    private int indiceActual = 0;

    // Nuevo: Almacenar la respuesta seleccionada por cada índice de pregunta
    private Map<Integer, String> respuestasUsuario = new HashMap<>();

    private Color colorPrimario = new Color(70, 130, 180);
    private Color colorTexto = new Color(44, 62, 80);
    private Font fuenteTitulo = new Font("Arial", Font.BOLD, 16);
    private Font fuenteNormal = new Font("Arial", Font.PLAIN, 14);

    public PantallaOpciones(GestorPreguntas gestor) {
        super("Pregunta de la prueba");
        this.gestor = gestor;

        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(Color.WHITE);

        crearComponentes();
        cargarPregunta();

        setVisible(true);
    }

    private void crearComponentes() {
        labelPregunta = new JLabel("", SwingConstants.CENTER);
        labelPregunta.setFont(fuenteTitulo);
        labelPregunta.setForeground(colorTexto);
        labelPregunta.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(labelPregunta, BorderLayout.NORTH);

        JPanel panelOpciones = new JPanel();
        panelOpciones.setBackground(Color.WHITE);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        opcionesRadio = new JRadioButton[4];
        grupoOpciones = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            opcionesRadio[i] = new JRadioButton();
            opcionesRadio[i].setFont(fuenteNormal);
            opcionesRadio[i].setBackground(Color.WHITE);
            grupoOpciones.add(opcionesRadio[i]);
            panelOpciones.add(opcionesRadio[i]);
        }

        add(panelOpciones, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(Color.WHITE);

        botonAnterior = crearBoton("Anterior");
        botonAnterior.addActionListener(this::accionAnterior);
        panelBotones.add(botonAnterior);

        botonSiguiente = crearBoton("Siguiente");
        botonSiguiente.addActionListener(this::accionSiguiente);
        panelBotones.add(botonSiguiente);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(fuenteNormal);
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorPrimario);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void cargarPregunta() {
    Pregunta pregunta = gestor.getPregunta(indiceActual);
    labelPregunta.setText((indiceActual + 1) + ". " + pregunta.getEnunciado());

    List<String> opciones = pregunta.getOpciones();
    grupoOpciones.clearSelection();

    for (int i = 0; i < opcionesRadio.length; i++) {
        if (i < opciones.size()) {
            opcionesRadio[i].setText(opciones.get(i));
            opcionesRadio[i].setVisible(true);
            
            // Restaurar la selección si existe una respuesta previa
            if (respuestasUsuario.containsKey(indiceActual) && 
                respuestasUsuario.get(indiceActual).equals(opciones.get(i))) {
                opcionesRadio[i].setSelected(true);
            }
        } else {
            opcionesRadio[i].setVisible(false);
        }
    }

    botonAnterior.setEnabled(indiceActual > 0);
    botonSiguiente.setText((indiceActual == gestor.getCantidadPreguntas() - 1) ? "Entregar tarea" : "Siguiente");
}

    private void guardarSeleccionActual() {
     for (int i = 0; i < opcionesRadio.length; i++) {
        if (opcionesRadio[i].isSelected()) {
            respuestasUsuario.put(indiceActual, opcionesRadio[i].getText());
            break;
        }
    }
}
    
    private void accionAnterior(ActionEvent e) {
        guardarSeleccionActual();
        if (indiceActual > 0) {
            indiceActual--;
            cargarPregunta();
        }
    }

    private void accionSiguiente(ActionEvent e) {
        guardarSeleccionActual();
        if (indiceActual < gestor.getCantidadPreguntas() - 1) {
            indiceActual++;
            cargarPregunta();
        } else {
            entregarTarea();
        }
    }

    private void entregarTarea() {
    guardarSeleccionActual();

    // Obtener las respuestas correctas
    List<String> respuestasCorrectas = new ArrayList<>();
    for (int i = 0; i < gestor.getCantidadPreguntas(); i++) {
        respuestasCorrectas.add(gestor.getPregunta(i).getRespuestaCorrecta());
    }

    // Crear gestor de resultados
    GestorPreguntasResultados resultados = new GestorPreguntasResultados(respuestasCorrectas, respuestasUsuario, gestor.getPreguntas());

    // Crear pantalla de resultados (cuando la tengas)
    PantallaResultados pantallaResultados = new PantallaResultados(resultados, gestor.getPreguntas());
    pantallaResultados.setVisible(true);

    dispose();
}
}
