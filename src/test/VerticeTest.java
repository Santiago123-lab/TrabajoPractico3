package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import modelo.Vertice;

public class VerticeTest {

    @Test
    public void testNombre() {
        Vertice vertice = new Vertice("Prueba");

        assertEquals("Prueba", vertice.getNombre());
    }

    @Test
    public void testDosVerticesConMismoNombre() {
        Vertice v1 = new Vertice("Prueba");
        Vertice v2 = new Vertice("Prueba");

        assertTrue(v1.equals(v2));
    }

    @Test
    public void testDosVerticesConDistintoNombre() {
        Vertice v1 = new Vertice("Prueba");
        Vertice v2 = new Vertice("Super Prueba");

        assertFalse(v1.equals(v2));
    }

    @Test
    public void testVerticeIgualAMismoObjeto() {
        Vertice vertice = new Vertice("Prueba");

        assertTrue(vertice.equals(vertice));
    }

    @Test
    public void testVerticeDistintoDeNull() {
        Vertice vertice = new Vertice("Prueba");

        assertFalse(vertice.equals(null));
    }

    @Test
    public void testHashCodeIgualParaVerticesIguales() {
        Vertice v1 = new Vertice("Prueba");
        Vertice v2 = new Vertice("Prueba");

        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testToString() {
        Vertice vertice = new Vertice("Prueba");

        assertEquals("Prueba", vertice.toString());
    }
}
