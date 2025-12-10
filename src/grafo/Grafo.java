package grafo;

import models.Nodo;
import java.util.*;

/**
 * Clase que representa un grafo no dirigido basado en lista de adyacencia.
 */
public class Grafo {
    private Map<Integer, Nodo> nodos;
    private Map<Integer, List<Integer>> listaAdyacencia;
    private Nodo nodoA;
    private Nodo nodoB;

    public Grafo() {
        this.nodos = new HashMap<>();
        this.listaAdyacencia = new HashMap<>();
    }

    /**
     * Agrega un nodo al grafo
     */
    public void agregarNodo(Nodo nodo) {
        if (!nodos.containsKey(nodo.getId())) {
            nodos.put(nodo.getId(), nodo);
            listaAdyacencia.put(nodo.getId(), new ArrayList<>());
            
            if (nodo.getTipo() == 'A') {
                this.nodoA = nodo;
            } else if (nodo.getTipo() == 'B') {
                this.nodoB = nodo;
            }
        }
    }

    /**
     * Agrega una arista entre dos nodos (grafo no dirigido)
     */
    public void agregarArista(int idOrigen, int idDestino) {
        if (nodos.containsKey(idOrigen) && nodos.containsKey(idDestino)) {
            List<Integer> adyacentes = listaAdyacencia.get(idOrigen);
            if (!adyacentes.contains(idDestino)) {
                adyacentes.add(idDestino);
            }
            // Para grafo no dirigido, agregamos también la arista inversa
            adyacentes = listaAdyacencia.get(idDestino);
            if (!adyacentes.contains(idOrigen)) {
                adyacentes.add(idOrigen);
            }
        }
    }

    public Nodo getNodo(int id) {
        return nodos.get(id);
    }

    public List<Integer> getAdyacentes(int id) {
        return listaAdyacencia.getOrDefault(id, new ArrayList<>());
    }

    public Collection<Nodo> getTodosNodos() {
        return nodos.values();
    }

    public Nodo getNodoA() {
        return nodoA;
    }

    public Nodo getNodoB() {
        return nodoB;
    }

    public int getCantidadNodos() {
        return nodos.size();
    }

    public int getCantidadAristas() {
        int count = 0;
        for (List<Integer> adyacentes : listaAdyacencia.values()) {
            count += adyacentes.size();
        }
        return count / 2; // Dividimos por 2 porque es no dirigido
    }

    public Map<Integer, List<Integer>> getListaAdyacencia() {
        return new HashMap<>(listaAdyacencia);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo con ").append(nodos.size()).append(" nodos y ")
          .append(getCantidadAristas()).append(" aristas\n");
        for (Integer id : listaAdyacencia.keySet()) {
            Nodo nodo = nodos.get(id);
            sb.append("Nodo ").append(id).append(" (").append(nodo.getTipo()).append(") -> ")
              .append(listaAdyacencia.get(id)).append("\n");
        }
        return sb.toString();
    }
}
