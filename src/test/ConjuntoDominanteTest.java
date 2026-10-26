package test;

import static org.junit.Assert.*;

import org.junit.Test;

import algoritmos.ConjuntoDominante;
import modelo.Grafo;
import modelo.Vertice;

import java.util.Set;

public class ConjuntoDominanteTest {

    @Test
    public void grafoVacioDevuelveConjuntoVacio() {

        Grafo grafo = new Grafo();
        ConjuntoDominante algoritmo = new ConjuntoDominante();

        Set<Vertice> resultado = algoritmo.encontrarConjuntoDominante(grafo);

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void unVerticeDevuelveEseVertice() {

        Grafo grafo = new Grafo();
        Vertice a = new Vertice("A");
        grafo.agregarVertice(a);

        ConjuntoDominante algoritmo = new ConjuntoDominante();

        Set<Vertice> resultado =algoritmo.encontrarConjuntoDominante(grafo);

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(a));
    }

    @Test
    public void caminoDeTresVerticesEligeElCentro() {

    	//para comprobar que selecciona al vertice que cubre mas vertices, en este caso el del medio
        Grafo grafo = new Grafo();

        Vertice a = new Vertice("A");
        Vertice b = new Vertice("B");
        Vertice c = new Vertice("C");

        grafo.agregarVertice(a);
        grafo.agregarVertice(b);
        grafo.agregarVertice(c);

        grafo.agregarArista(a, b);
        grafo.agregarArista(b, c);

        ConjuntoDominante algoritmo = new ConjuntoDominante();

        Set<Vertice> resultado =algoritmo.encontrarConjuntoDominante(grafo);

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(b));
    }
}



