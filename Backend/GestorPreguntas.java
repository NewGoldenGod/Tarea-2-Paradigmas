package Backend;

import java.io.*;
import java.nio.file.*;
import java.util.*;
public class GestorPreguntas {
    private List<Pregunta> preguntas = new ArrayList<>();
    public void cargarDesdeArchivo(String rutaArchivo) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));
        int numLinea = 0;

        for (String linea : lineas) {
            numLinea++;
            if (linea.trim().isEmpty()) continue; // Saltar líneas vacías
            try {
                String[] partes = linea.split(",", -1);
                if (partes.length != 6) {
                    throw new IllegalArgumentException("Línea mal formada (deben haber 6 campos)");
                }
                Pregunta.Tipo tipo = Pregunta.Tipo.valueOf(partes[0].trim().toUpperCase());
                Pregunta.NivelBloom nivel = Pregunta.NivelBloom.valueOf(partes[1].trim().toUpperCase());
                String enunciado = partes[2].trim();
                String opcionesRaw = partes[3].trim();
                String respuesta = partes[4].trim();
                int tiempo = Integer.parseInt(partes[5].trim());

                List<String> opciones = tipo == Pregunta.Tipo.MULTIPLE ?
                        Arrays.asList(opcionesRaw.split(";")) :
                        tipo == Pregunta.Tipo.VERDADERO_FALSO ?
                        Arrays.asList("TRUE", "FALSE") : Collections.emptyList();
                
                // Validaciones específicas
                if (enunciado.isEmpty()) throw new IllegalArgumentException("Enunciado vacío");
                if (tiempo <= 0) throw new IllegalArgumentException("Tiempo debe ser positivo");
                if (tipo == Pregunta.Tipo.MULTIPLE && (opciones.size() <=1 || !opciones.contains(respuesta))) {
                    throw new IllegalArgumentException("Opción de respuesta inválida o faltan opciones");
                }
                if (tipo == Pregunta.Tipo.VERDADERO_FALSO && !(respuesta.equalsIgnoreCase("TRUE") || respuesta.equalsIgnoreCase("FALSE"))) {
                    throw new IllegalArgumentException("Respuesta verdadero/falso inválida");
                }
                Pregunta pregunta = new Pregunta(tipo, nivel, enunciado, opciones, respuesta, tiempo);
                preguntas.add(pregunta);

            } catch (Exception e) {
                System.err.println("️Error en línea " + numLinea + ": " + e.getMessage());
            }
            
        }
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public Pregunta getPregunta(int indice) {
    if (indice >= 0 && indice < preguntas.size()) {
        return preguntas.get(indice);
    } else {
        return null; // O lanzar excepción, según prefieras
    }
}
    public int getCantidadPreguntas() {
        return preguntas.size();
    }

    public int obtenerTiempoTotal() {
        return preguntas.stream().mapToInt(Pregunta::getTiempoEstimado).sum();
    }

    public boolean estaCargadoCorrectamente() {
        return !preguntas.isEmpty();
    }
    
        public Map<String, Object> obtenerListasSeparadas() {
        List<String> enunciados = new ArrayList<>();
        List<List<String>> alternativas = new ArrayList<>();
        List<String> respuestas = new ArrayList<>();

        for (Pregunta p : preguntas) {
            enunciados.add(p.getEnunciado());
            alternativas.add(p.getOpciones());
            respuestas.add(p.getRespuestaCorrecta());
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("enunciados", enunciados);
        resultado.put("alternativas", alternativas);
        resultado.put("respuestas", respuestas);

        return resultado;
    }
}
