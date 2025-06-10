package Backend;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GestorPreguntas {
    private List<Pregunta> preguntas = new ArrayList<>();
    
    public void cargarDesdeArchivo(String rutaArchivo) throws IOException {
        preguntas.clear(); // Limpiar preguntas anteriores
        List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));
        int numLinea = 0;
        int preguntasCargadas = 0;
        int erroresEncontrados = 0;

        System.out.println("Iniciando carga del archivo: " + rutaArchivo);
        System.out.println("Total de líneas a procesar: " + lineas.size());

        for (String linea : lineas) {
            numLinea++;
            if (linea.trim().isEmpty()) {
                System.out.println("Línea " + numLinea + ": vacía, saltando...");
                continue; // Saltar líneas vacías
            }
            
            try {
                System.out.println("Procesando línea " + numLinea + ": " + linea);
                String[] partes = linea.split(",", -1);
                
                if (partes.length != 6) {
                    throw new IllegalArgumentException("Línea mal formada (deben haber 6 campos, encontrados: " + partes.length + ")");
                }

                // Procesar cada campo
                String tipoStr = partes[0].trim().toUpperCase();
                String nivelStr = partes[1].trim().toUpperCase();
                String enunciado = partes[2].trim();
                String opcionesRaw = partes[3].trim();
                String respuesta = partes[4].trim();
                String tiempoStr = partes[5].trim();

                // Validar y convertir tipo
                Pregunta.Tipo tipo;
                try {
                    tipo = Pregunta.Tipo.valueOf(tipoStr);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Tipo de pregunta inválido: " + tipoStr);
                }

                // Validar y convertir nivel Bloom
                Pregunta.NivelBloom nivel;
                try {
                    nivel = Pregunta.NivelBloom.valueOf(nivelStr);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Nivel Bloom inválido: " + nivelStr);
                }

                // Validar tiempo
                int tiempo;
                try {
                    tiempo = Integer.parseInt(tiempoStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Tiempo inválido: " + tiempoStr);
                }

                // Procesar opciones según el tipo
                List<String> opciones;
                if (tipo == Pregunta.Tipo.MULTIPLE) {
                    if (opcionesRaw.isEmpty()) {
                        throw new IllegalArgumentException("Las preguntas múltiples deben tener opciones");
                    }
                    opciones = Arrays.asList(opcionesRaw.split(";"));
                } else if (tipo == Pregunta.Tipo.VERDADERO_FALSO) {
                    opciones = Arrays.asList("TRUE", "FALSE");
                } else {
                    opciones = Collections.emptyList();
                }
                
                // Validaciones específicas
                if (enunciado.isEmpty()) {
                    throw new IllegalArgumentException("Enunciado vacío");
                }
                
                if (tiempo <= 0) {
                    throw new IllegalArgumentException("Tiempo debe ser positivo");
                }
                
                if (tipo == Pregunta.Tipo.MULTIPLE && (opciones.size() <= 1 || !opciones.contains(respuesta))) {
                    throw new IllegalArgumentException("Opción de respuesta inválida o faltan opciones. Respuesta: '" + respuesta + "', Opciones: " + opciones);
                }
                
                if (tipo == Pregunta.Tipo.VERDADERO_FALSO && !(respuesta.equalsIgnoreCase("TRUE") || respuesta.equalsIgnoreCase("FALSE"))) {
                    throw new IllegalArgumentException("Respuesta verdadero/falso inválida: " + respuesta);
                }

                // Crear y agregar la pregunta
                Pregunta pregunta = new Pregunta(tipo, nivel, enunciado, opciones, respuesta, tiempo);
                preguntas.add(pregunta);
                preguntasCargadas++;
                
                System.out.println("✓ Pregunta " + preguntasCargadas + " cargada exitosamente");

            } catch (Exception e) {
                erroresEncontrados++;
                System.err.println("❌ Error en línea " + numLinea + ": " + e.getMessage());
                System.err.println("   Contenido: " + linea);
            }
        }

        System.out.println("\n=== RESUMEN DE CARGA ===");
        System.out.println("Líneas procesadas: " + numLinea);
        System.out.println("Preguntas cargadas exitosamente: " + preguntasCargadas);
        System.out.println("Errores encontrados: " + erroresEncontrados);
        System.out.println("========================\n");

        if (preguntas.isEmpty()) {
            throw new IOException("No se pudo cargar ninguna pregunta válida del archivo");
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