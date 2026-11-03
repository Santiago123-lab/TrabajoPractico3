package vista;

import presentador.IGrafoPresenter;

import java.util.List;

/**
 * Interfaz de la Vista en el patrón MVP.
 * 
 * - Solo recibe datos primitivos (String, List<String>, boolean).
 * - No importa ninguna clase del paquete modelo.
 * - Expone setPresenter() para que el Presenter se registre.
 * - Expone métodos de entrada (diálogos) para que el Presenter
 *   pida datos al usuario sin tocar Swing.
 */
public interface IGrafoVista {

    /** Registra el presenter y conecta los eventos de la vista. */
    void setPresenter(IGrafoPresenter presenter);

    /** Hace visible la ventana (equivalente a setVisible(true)). */
    void mostrar();

    // ── Métodos de presentación ────────────────────────────────────────────

    /**
     * Actualiza la representación visual del grafo completo.
     * @param vertices lista de nombres de vértices
     * @param aristas  lista de pares [nombre_origen, nombre_destino]
     */
    void mostrarGrafo(List<String> vertices, List<String[]> aristas);

    /**
     * Resalta el conjunto dominante sin cambiar la topología del grafo.
     * @param dominante nombres de los vértices en el conjunto dominante
     */
    void resaltarDominante(List<String> dominante);

    /** Muestra el texto del resultado del algoritmo. */
    void mostrarResultado(String texto);

    /** Muestra un mensaje de estado (éxito o error). */
    void mostrarEstado(String mensaje, boolean esError);

    /** Borra el mensaje de estado actual. */
    void limpiarEstado();

    // ── Métodos de entrada (diálogos) ─────────────────────────────────────

    /**
     * Pide al usuario el nombre de un nuevo vértice.
     * @return el nombre ingresado, o null si canceló
     */
    String pedirNombreVertice();

    /**
     * Pide al usuario los dos extremos de una arista.
     * @param verticesDisponibles nombres de vértices existentes para mostrar en la UI
     * @return arreglo [nombre_origen, nombre_destino], o null si canceló
     */
    String[] pedirArista(List<String> verticesDisponibles);

    /**
     * Pide confirmación para limpiar el grafo.
     * @return true si el usuario confirmó
     */
    boolean confirmarLimpiar();

    /** Limpia el campo de ingreso de nombre de vértice. */
    void limpiarCampoVertice();
}
