package vista;

import presentador.IGrafoPresenter;

import java.util.List;

public interface IGrafoVista {

    void setPresenter(IGrafoPresenter presenter);

    void mostrar();

    // Métodos de presentación =======================================

    void mostrarGrafo(List<String> vertices, List<String[]> aristas);

    void resaltarDominante(List<String> dominante);

    void mostrarResultado(String texto);

    void mostrarEstado(String mensaje, boolean esError);

    void limpiarEstado();

    // Métodos de entrada (diálogos) ==================================

    String pedirNombreVertice();

    String[] pedirArista(List<String> verticesDisponibles);

    boolean confirmarLimpiar();

    void limpiarCampoVertice();
    
    String pedirRutaCargarJSON();

    String pedirRutaGuardarJSON();
}
