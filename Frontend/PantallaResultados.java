package Frontend;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import Backend.Pregunta;
import Backend.GestorPreguntasResultados;

public class PantallaResultados extends JFrame {

    public PantallaResultados(GestorPreguntasResultados gestor, List<Pregunta> preguntas) {
        setTitle("Resultados del Examen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        JTextArea areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder texto = new StringBuilder();
        texto.append("==== RESUMEN GENERAL ====\n");
        texto.append("Correctas: ").append(gestor.getTotalCorrectas()).append("\n");
        texto.append("Incorrectas: ").append(gestor.getTotalIncorrectas()).append("\n");
        texto.append("Porcentaje: ").append(String.format("%.2f", gestor.getPorcentajeCorrectas())).append(" %\n\n");

        texto.append("==== DETALLE POR PREGUNTA ====\n");
        Map<Integer, String> respuestasUsuario = gestor.getRespuestasUsuario();
        List<String> respuestasCorrectas = gestor.getRespuestasCorrectas();

        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            String respuestaUsuario = respuestasUsuario.getOrDefault(i, "Sin respuesta");
            String respuestaCorrecta = respuestasCorrectas.get(i);
            boolean correcta = respuestaUsuario.equals(respuestaCorrecta);

            texto.append("Pregunta ").append(i + 1).append(": ").append(p.getEnunciado()).append("\n");
            texto.append("Tu respuesta: ").append(respuestaUsuario).append("\n");
            texto.append("Respuesta correcta: ").append(respuestaCorrecta).append("\n");
            texto.append("Resultado: ").append(correcta ? "✓ Correcto\n" : "✗ Incorrecto\n");
            texto.append("----------------------------------\n");
        }
        
        // Al final del StringBuilder en PantallaResultados
        texto.append("\n==== DISTRIBUCIÓN POR NIVEL BLOOM ====\n");
        Map<String, Integer> conteoBloom = gestor.getConteoPorNivel();
        Map<String, Double> porcentajeBloom = gestor.getPorcentajePorNivel();

        for (String nivel : conteoBloom.keySet()) {
            int cantidad = conteoBloom.get(nivel);
            double porcentaje = porcentajeBloom.get(nivel);
            texto.append(String.format("%-12s: %2d preguntas (%.2f%%)\n", nivel, cantidad, porcentaje));
}


        areaResultados.setText(texto.toString());
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
