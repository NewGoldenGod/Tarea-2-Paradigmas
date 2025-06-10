package Backend;

import java.util.List;

// Clase Pregunta
public class Pregunta {
    public enum Tipo { MULTIPLE, VERDADERO_FALSO }
    public enum NivelBloom { RECORDAR, ENTENDER, APLICAR, ANALIZAR, EVALUAR, CREAR }
    
    // Componentes del elemento Pregunta
    private Tipo tipo;
    private NivelBloom nivel;
    private String enunciado;
    private List<String> opciones;
    private String respuestaCorrecta;
    private int tiempoEstimado;
    
    // Constructor Pregunta
    public Pregunta(Tipo tipo, NivelBloom nivel, String enunciado, List<String> opciones, String respuestaCorrecta, int tiempoEstimado) {
        this.tipo = tipo;
        this.nivel = nivel;
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
        this.tiempoEstimado = tiempoEstimado;
    }

    // Establecer getters
    public Tipo getTipo() { return tipo; }
    public NivelBloom getNivel() { return nivel; }
    public String getEnunciado() { return enunciado; }
    public List<String> getOpciones() { return opciones; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public int getTiempoEstimado() { return tiempoEstimado; }
}
