package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {

private Map<Vertice, List<Vertice>> adyacencias;

    public Grafo() {
        adyacencias = new HashMap<>();
    }

    public void agregarVertice(Vertice vertice) {
    	
        if (vertice == null) {
            throw new IllegalArgumentException("El vertice no puede ser null");
        }

        if (adyacencias.containsKey(vertice)) {
            throw new IllegalArgumentException("El vertice ya existe");
        }

        adyacencias.put(vertice, new ArrayList<>());
    }

    public void agregarArista(Vertice origen, Vertice destino) {
    	
        if (!adyacencias.containsKey(origen) || !adyacencias.containsKey(destino)) {        	
            throw new IllegalArgumentException("Ambos vértices deben existir en el grafo");
        }

        if (origen.equals(destino)) {    	
            throw new IllegalArgumentException("No se permiten aristas de un vertice a sí mismo");
        }

        if (adyacencias.get(origen).contains(destino)) {        	
            throw new IllegalArgumentException("La arista ya existe");
        }

        adyacencias.get(origen).add(destino);
        adyacencias.get(destino).add(origen);
    }

    public List<Vertice> obtenerVecinos(Vertice vertice) {
    	
        if (!adyacencias.containsKey(vertice)) {        	
            throw new IllegalArgumentException("El vertice no existe en el grafo");
        }
        
        return new ArrayList<>(adyacencias.get(vertice)); //no rompe el encapsulamiento
    }

    public List<Vertice> obtenerVertices() {    	
        return new ArrayList<>(adyacencias.keySet()); //no rompe el encapsulamiento
    }

    public int cantidadVertices() {    	
        return adyacencias.size();
    }

    /**
     * Elimina todos los vértices y aristas del grafo.
     */
    public void limpiar() {
        adyacencias.clear();
    }

    /**
     * Indica si ya existe un vértice con el nombre dado.
     */
    public boolean contieneVertice(String nombre) {
        return adyacencias.keySet().stream()
                .anyMatch(v -> v.getNombre().equals(nombre));
    }
}

