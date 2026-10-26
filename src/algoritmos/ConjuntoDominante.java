package algoritmos;

import java.util.Set;

import modelo.Grafo;
import modelo.Vertice;
import java.util.HashSet;
import java.util.List;

public class ConjuntoDominante {
	
    public Set<Vertice> encontrarConjuntoDominante(Grafo grafo) {

        Set<Vertice> conjuntoDominante = new HashSet<>();
        Set<Vertice> verticesCubiertos = new HashSet<>();
        
        List<Vertice> vertices = grafo.obtenerVertices();
        
        while (verticesCubiertos.size() < vertices.size()) {
        
        	Vertice mejorVertice = buscarMejorVertice(grafo, conjuntoDominante, verticesCubiertos, vertices);
        	
        	conjuntoDominante.add(mejorVertice);

        	verticesCubiertos.add(mejorVertice);
        	verticesCubiertos.addAll(grafo.obtenerVecinos(mejorVertice));
        }

        return conjuntoDominante;
    }


	private Vertice buscarMejorVertice(Grafo grafo, Set<Vertice> conjuntoDominante, Set<Vertice> verticesCubiertos,
			List<Vertice> vertices) {
		
		Vertice mejorVertice = null;
		int mayorCantidad = -1;
      
		for (Vertice vertice : vertices) {
			
			if (conjuntoDominante.contains(vertice)) {
			    continue;
			}

			// por cada vertice, busco cuál es el mejor cantidato en base a la cantidad de
			// vertices/vecinos que cubre
			int cantidad = contarNuevosCubiertos(vertice, grafo, verticesCubiertos);

			if (cantidad > mayorCantidad) {
				mayorCantidad = cantidad;
				mejorVertice = vertice;
			}
		}
		return mejorVertice;
	}
    
    
    private int contarNuevosCubiertos(Vertice vertice,Grafo grafo, Set<Vertice> verticesCubiertos) {

        int cantidad = 0;
        
        //si el propio vertice no esta en la lista, sumo 1
        if (!verticesCubiertos.contains(vertice)) { 
            cantidad++;
        }
        
        //recorro los vecinos del vertice, si alguno no esta en la lista, sumo 1
        for (Vertice vecino : grafo.obtenerVecinos(vertice)) {
        	
            if (!verticesCubiertos.contains(vecino)) {
                cantidad++;
            }
        }
        return cantidad;
    }
    
}
