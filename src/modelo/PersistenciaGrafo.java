package modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersistenciaGrafo {

	/**
     * Clase interna DTO (Data Transfer Object) para mapear el JSON.
     * Al ser pública y con atributos simples, Gson la parsea automáticamente.
     */
    public static class GrafoDTO {
        public List<String> vertices = new ArrayList<>();
        public List<String[]> aristas = new ArrayList<>();
    }

    public static void guardarGrafo(Grafo grafo, String rutaArchivo) 
    {
        GrafoDTO dto = crearDTO(grafo);
        escribirArchivoJson(dto, rutaArchivo);
    }

    private static GrafoDTO crearDTO(Grafo grafo) 
    {
        GrafoDTO dto = new GrafoDTO();
        extraerVertices(grafo, dto);
        extraerAristas(grafo, dto);
        return dto;
    }
    
    private static void extraerVertices(Grafo grafo, GrafoDTO dto) 
    {
        for (Vertice v : grafo.obtenerVertices()) 
        {
            dto.vertices.add(v.getNombre());
        }
    }
    
    private static void extraerAristas(Grafo grafo, GrafoDTO dto) 
    {
        Set<String> aristasVistas = new HashSet<>();
        for (Vertice v : grafo.obtenerVertices()) {
            for (Vertice vecino : grafo.obtenerVecinos(v)) {
                agregarAristaSiEsNueva(v, vecino, aristasVistas, dto);
            }
        }
    }
    
    private static void agregarAristaSiEsNueva(Vertice v1, Vertice v2, Set<String> aristasVistas, GrafoDTO dto) 
    {
        String clave = generarClaveUnicaArista(v1, v2);
        if (aristasVistas.add(clave)) 
        {
            dto.aristas.add(new String[]{v1.getNombre(), v2.getNombre()});
        }
    }
    
    private static String generarClaveUnicaArista(Vertice v1, Vertice v2) 
    {
        return v1.getNombre().compareTo(v2.getNombre()) < 0
                ? v1.getNombre() + "-" + v2.getNombre()
                : v2.getNombre() + "-" + v1.getNombre();
    }
    
    
    private static void escribirArchivoJson(GrafoDTO dto, String rutaArchivo) 
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(rutaArchivo)) 
        {
            writer.write(gson.toJson(dto));
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    
    
    public static Grafo cargarGrafo(String rutaArchivo) 
    {
        GrafoDTO dto = leerDTODesdeArchivo(rutaArchivo);

        if (dto == null) 
        {
            return new Grafo();
        }

        return reconstruirGrafo(dto);
    }

    private static GrafoDTO leerDTODesdeArchivo(String rutaArchivo) 
    {
        Gson gson = new Gson();
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) 
        {
            return gson.fromJson(br, GrafoDTO.class);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }

	private static Grafo reconstruirGrafo(GrafoDTO dto) 
	{
		Grafo grafo = new Grafo();
        
		for (String nombreV : dto.vertices) 
		{
            grafo.agregarVertice(new Vertice(nombreV));
        }

        List<Vertice> verticesActuales = grafo.obtenerVertices();
        
        for (String[] arista : dto.aristas) 
        {
            Vertice origen = buscarVertice(verticesActuales, arista[0]);
            Vertice destino = buscarVertice(verticesActuales, arista[1]);
            
            if (origen != null && destino != null) 
                grafo.agregarArista(origen, destino);
        }
        
        return grafo;
	}

    private static Vertice buscarVertice(List<Vertice> vertices, String nombre) {
        for (Vertice v : vertices) {
            if (v.getNombre().equals(nombre))
                return v;
        }
        return null;
    }
}