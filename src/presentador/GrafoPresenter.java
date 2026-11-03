package presentador;

import algoritmos.ConjuntoDominante;
import modelo.Grafo;
import modelo.Vertice;
import vista.IGrafoVista;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Presenter del patrón MVP.
 *
 * Reglas de oro que cumple esta clase:
 *   ✔ Conoce al Modelo  (Grafo, Vertice, ConjuntoDominante)
 *   ✔ Conoce a la Vista SOLO a través de IGrafoVista
 *   ✗ NO importa ninguna clase de javax.swing ni java.awt
 *   ✗ NO conoce VentanaPrincipal, PanelGrafo, PanelControles
 */
public class GrafoPresenter implements IGrafoPresenter {

    private final Grafo             grafo;
    private final ConjuntoDominante algoritmo;
    private final IGrafoVista       vista;

    /**
     * Constructor principal. Recibe la vista y el modelo ya instanciado.
     * Esto permite que Main cree el Grafo de forma explícita
     * (compatible con el formato Windows Builder).
     */
    public GrafoPresenter(IGrafoVista vista, Grafo grafo) {
        this.grafo     = grafo;
        this.algoritmo = new ConjuntoDominante();
        this.vista     = vista;

        // Le entregamos la referencia a nosotros mismos para que la vista
        // pueda disparar eventos. Aquí se conecta el MVP.
        vista.setPresenter(this);

        // Estado inicial: grafo vacío
        actualizarVistaGrafo();
    }

    // ── Implementación de IGrafoPresenter ─────────────────────────────────

    @Override
    public void onAgregarVertice() {
        vista.limpiarEstado();

        String nombre = vista.pedirNombreVertice();
        if (nombre == null) return;
        nombre = nombre.trim();

        if (nombre.isEmpty()) {
            vista.mostrarEstado("El nombre no puede estar vacío.", true);
            return;
        }

        try {
            grafo.agregarVertice(new Vertice(nombre));
            actualizarVistaGrafo();
            vista.resaltarDominante(Collections.emptyList());
            vista.mostrarResultado("Aún no se ejecutó el algoritmo.");
            vista.limpiarCampoVertice();
            vista.mostrarEstado("Vértice \"" + nombre + "\" agregado.", false);
        } catch (IllegalArgumentException ex) {
            vista.mostrarEstado(ex.getMessage(), true);
        }
    }

    @Override
    public void onAgregarArista() {
        vista.limpiarEstado();

        List<String> nombresVertices = grafo.obtenerVertices().stream()
                .map(Vertice::getNombre)
                .collect(Collectors.toList());

        if (nombresVertices.size() < 2) {
            vista.mostrarEstado("Se necesitan al menos 2 vértices.", true);
            return;
        }

        String[] seleccion = vista.pedirArista(nombresVertices);
        if (seleccion == null) return;

        String nOrigen  = seleccion[0];
        String nDestino = seleccion[1];

        Vertice vOrigen  = buscarVertice(nOrigen);
        Vertice vDestino = buscarVertice(nDestino);

        if (vOrigen == null || vDestino == null) {
            vista.mostrarEstado("Vértice no encontrado.", true);
            return;
        }

        try {
            grafo.agregarArista(vOrigen, vDestino);
            actualizarVistaGrafo();
            vista.resaltarDominante(Collections.emptyList());
            vista.mostrarResultado("Aún no se ejecutó el algoritmo.");
            vista.mostrarEstado("Arista " + nOrigen + " ⟷ " + nDestino + " agregada.", false);
        } catch (IllegalArgumentException ex) {
            vista.mostrarEstado(ex.getMessage(), true);
        }
    }

    @Override
    public void onEjecutarAlgoritmo() {
        vista.limpiarEstado();

        if (grafo.cantidadVertices() == 0) {
            vista.mostrarEstado("El grafo está vacío.", true);
            return;
        }

        Set<Vertice> dominante = algoritmo.encontrarConjuntoDominante(grafo);

        List<String> dominanteNombres = dominante.stream()
                .map(Vertice::getNombre)
                .sorted()
                .collect(Collectors.toList());

        vista.resaltarDominante(dominanteNombres);

        StringBuilder sb = new StringBuilder();
        sb.append("Tamano del conjunto dominante: ").append(dominante.size()).append("\n\n");
        sb.append("Vertices del conjunto dominante:\n");
        dominanteNombres.forEach(n -> sb.append("  - ").append(n).append("\n"));
        sb.append("\nTotal vertices del grafo: ").append(grafo.cantidadVertices());

        vista.mostrarResultado(sb.toString());
        vista.mostrarEstado("Algoritmo ejecutado. Conjunto de tamaño " + dominante.size() + ".", false);
    }

    @Override
    public void onLimpiar() {
        if (!vista.confirmarLimpiar()) return;

        grafo.limpiar();
        actualizarVistaGrafo();
        vista.resaltarDominante(Collections.emptyList());
        vista.mostrarResultado("Aún no se ejecutó el algoritmo.");
        vista.mostrarEstado("Grafo limpiado.", false);
    }

    // ── Métodos privados ───────────────────────────────────────────────────

    /**
     * Convierte el estado actual del grafo a datos primitivos
     * y los envía a la vista.
     */
    private void actualizarVistaGrafo() {
        List<String> vertices = grafo.obtenerVertices().stream()
                .map(Vertice::getNombre)
                .collect(Collectors.toList());

        vista.mostrarGrafo(vertices, obtenerAristas());
    }

    /**
     * Construye la lista de aristas como pares de nombres,
     * sin duplicar (grafo no dirigido).
     */
    private List<String[]> obtenerAristas() {
        Set<String>    vistas  = new HashSet<>();
        List<String[]> aristas = new ArrayList<>();

        for (Vertice v : grafo.obtenerVertices()) {
            for (Vertice vecino : grafo.obtenerVecinos(v)) {
                String clave = v.getNombre().compareTo(vecino.getNombre()) < 0
                        ? v.getNombre() + "-" + vecino.getNombre()
                        : vecino.getNombre() + "-" + v.getNombre();

                if (vistas.add(clave)) {
                    aristas.add(new String[]{v.getNombre(), vecino.getNombre()});
                }
            }
        }
        return aristas;
    }

    private Vertice buscarVertice(String nombre) {
        return grafo.obtenerVertices().stream()
                .filter(v -> v.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }
}
