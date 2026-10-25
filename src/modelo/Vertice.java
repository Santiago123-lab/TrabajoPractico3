package modelo;

public class Vertice {

private String nombre;    

    public Vertice(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        StringBuilder st= new StringBuilder();
        
        st.append(nombre);
        
        return st.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Vertice otro = (Vertice) obj;

        if (nombre == null) {
            return otro.nombre == null;
        }

        return nombre.equals(otro.nombre);
    }


    @Override
    public int hashCode() {
    	if (nombre == null) {
    		return 0;
    	}

    	return nombre.hashCode();
    }
}
    



