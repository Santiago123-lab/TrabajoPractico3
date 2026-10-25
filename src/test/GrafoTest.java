package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import modelo.Grafo;
import modelo.Vertice;

public class GrafoTest {

private Grafo grafo;
private Vertice a;
private Vertice b;
private Vertice c;

    @Before
    public void setUp() {
        grafo = new Grafo();

        a = new Vertice("A");
        b = new Vertice("B");
        c = new Vertice("C");

        grafo.agregarVertice(a);
        grafo.agregarVertice(b);
        grafo.agregarVertice(c);

        grafo.agregarArista(a, b);
        grafo.agregarArista(b, c);
    }

    @Test
    public void testCantidadVertices() {
        assertEquals(3, grafo.cantidadVertices());
    }

    @Test
    public void testAgregarVertice() {
        grafo.agregarVertice(new Vertice("D"));

        assertEquals(4, grafo.cantidadVertices());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoAgregarVerticeDuplicado() {
        grafo.agregarVertice(new Vertice("A"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoAgregarVerticeNull() {
        grafo.agregarVertice(null);
    }

    @Test
    public void testObtenerVecinos() {
        List<Vertice> vecinos = grafo.obtenerVecinos(b);

        assertEquals(2, vecinos.size());
        assertTrue(vecinos.contains(a));
        assertTrue(vecinos.contains(c));
    }

    @Test
    public void testAristaNoDirigida() {
        assertTrue(grafo.obtenerVecinos(a).contains(b));
        assertTrue(grafo.obtenerVecinos(b).contains(a));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoAgregarAristaConVerticeSinDefinir() {  
        Vertice d = new Vertice("D");

        grafo.agregarArista(a, d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoAgregarAristaDuplicada() {
        grafo.agregarArista(b, a);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoAgregarAristaAlMismoVertice() {
        grafo.agregarArista(a, a);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoObtenerVecinosDeVerticeInexistente() {
        grafo.obtenerVecinos(new Vertice("D"));
    }

    @Test
    public void testObtenerVertices() {
        List<Vertice> vertices = grafo.obtenerVertices();

        assertEquals(3, vertices.size());
        assertTrue(vertices.contains(a));
        assertTrue(vertices.contains(b));
        assertTrue(vertices.contains(c));
    }

    @Test
    public void testModificarListaDeVecinosNoModificaGrafo() {
        List<Vertice> vecinos = grafo.obtenerVecinos(a);

        vecinos.clear();

        assertFalse(grafo.obtenerVecinos(a).isEmpty());
    }
}

