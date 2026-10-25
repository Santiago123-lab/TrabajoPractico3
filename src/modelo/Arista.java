package modelo;

public class Arista {

private Vertice origen;
private Vertice destino;

    public Arista(Vertice origen, Vertice destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public Vertice getOrigen() {
        return origen;
    }

    public Vertice getDestino() {
        return destino;
    }
}

