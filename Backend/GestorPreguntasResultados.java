package Backend;

import java.util.*;

public class GestorPreguntasResultados {
    private List<String> respuestasCorrectas;
    private Map<Integer, String> respuestasUsuario;
    private Map<String, Integer> conteoPorNivel;
    private Map<String, Double> porcentajePorNivel;


    private int totalCorrectas;
    private int totalIncorrectas;
    private double porcentajeCorrectas;
    
    private void calcularDistribucionBloom(List<Pregunta> preguntas) {
    conteoPorNivel = new HashMap<>();
    porcentajePorNivel = new HashMap<>();

    int total = preguntas.size();

    for (Pregunta p : preguntas) {
        String nivel = p.getNivel().toString();
        conteoPorNivel.put(nivel, conteoPorNivel.getOrDefault(nivel, 0) + 1);
    }

    for (Map.Entry<String, Integer> entry : conteoPorNivel.entrySet()) {
        double porcentaje = (entry.getValue() * 100.0) / total;
        porcentajePorNivel.put(entry.getKey(), porcentaje);
    }
}


    public GestorPreguntasResultados(List<String> respuestasCorrectas, Map<Integer, String> respuestasUsuario, List<Pregunta> preguntas) {
        this.respuestasCorrectas = respuestasCorrectas;
        this.respuestasUsuario = respuestasUsuario;

        calcularResultados();
        calcularDistribucionBloom(preguntas);
}


    private void calcularResultados() {
        totalCorrectas = 0;
        totalIncorrectas = 0;

        for (int i = 0; i < respuestasCorrectas.size(); i++) {
            String respuestaCorrecta = respuestasCorrectas.get(i);
            String respuestaUsuario = respuestasUsuario.getOrDefault(i, "Sin respuesta");

            if (respuestaUsuario.equals(respuestaCorrecta)) {
                totalCorrectas++;
            } else {
                totalIncorrectas++;
            }
        }

        porcentajeCorrectas = respuestasCorrectas.isEmpty() ? 0 :
                (totalCorrectas * 100.0) / respuestasCorrectas.size();
    }

    public int getTotalCorrectas() {
        return totalCorrectas;
    }

    public int getTotalIncorrectas() {
        return totalIncorrectas;
    }

    public double getPorcentajeCorrectas() {
        return porcentajeCorrectas;
    }

    // Si más adelante quieres agregar Bloom:
    public Map<String, Object> getResumenResultados() {
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("correctas", totalCorrectas);
        resumen.put("incorrectas", totalIncorrectas);
        resumen.put("porcentaje", porcentajeCorrectas);
        return resumen;
    }
    
    public String obtenerResumen() {
    return "Correctas: " + totalCorrectas + "\n" +
           "Incorrectas: " + totalIncorrectas + "\n" +
           "Porcentaje de aciertos: " + String.format("%.2f", porcentajeCorrectas) + "%";
}
    public Map<Integer, String> getRespuestasUsuario() {
        return respuestasUsuario;
}

    public List<String> getRespuestasCorrectas() {
        return respuestasCorrectas;
}
    
    public Map<String, Integer> getConteoPorNivel() {
        return conteoPorNivel;
}

    public Map<String, Double> getPorcentajePorNivel() {
        return porcentajePorNivel;
}

}

