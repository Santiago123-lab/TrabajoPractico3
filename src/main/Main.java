package main;

import java.awt.EventQueue;

import modelo.Grafo;
import presentador.GrafoPresenter;
import vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Grafo grafo = new Grafo();
                    VentanaPrincipal vista = new VentanaPrincipal();
                    GrafoPresenter presentador = new GrafoPresenter(vista, grafo);
                    vista.mostrar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
