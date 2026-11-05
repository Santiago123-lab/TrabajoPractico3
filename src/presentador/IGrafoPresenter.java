package presentador;

/**
 * Interfaz del Presenter en el patrón MVP.
 * Define las acciones que la Vista puede disparar.
 * No conoce ni Swing ni las clases del modelo.
 */
public interface IGrafoPresenter {

    void onAgregarVertice();
    void onAgregarArista();
    void onEjecutarAlgoritmo();
    void onLimpiar();
    void onCargarJSON();
    void onGuardarJSON();
}
