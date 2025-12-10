package grafo;

import models.Nodo;
import java.util.*;

/**
 * Clase que representa un grafo no dirigido implementado con lista de adyacencia.
 * 
 * ESTRUCTURA Y FUNCIONAMIENTO:
 * - El grafo almacena nodos (vértices) del laberinto en un HashMap
 * - Las conexiones entre nodos se representan mediante una lista de adyacencia
 * - Cada nodo puede conectarse con sus vecinos inmediatos (arriba, abajo, izquierda, derecha)
 * - Es un grafo NO DIRIGIDO, lo que significa que si hay una arista de A->B, también existe B->A
 * 
 * COMPLEJIDAD:
 * - Búsqueda de nodo: O(1) promedio
 * - Búsqueda de adyacentes: O(1) promedio
 * - Agregar nodo: O(1) promedio
 * - Agregar arista: O(1) promedio
 */
public class Grafo {
    // HashMap que almacena todos los nodos, con el ID del nodo como clave
    private Map<Integer, Nodo> nodos;
    // Lista de adyacencia: mapea cada ID de nodo a una lista de IDs de nodos adyacentes
    private Map<Integer, List<Integer>> listaAdyacencia;
    // Referencia al nodo inicial (marcado con 'A' en el laberinto)
    private Nodo nodoA;
    // Referencia al nodo final (marcado con 'B' en el laberinto)
    private Nodo nodoB;

    /**
     * Constructor que inicializa un grafo vacío.
     */
    public Grafo() {
        this.nodos = new HashMap<>();
        this.listaAdyacencia = new HashMap<>();
    }

    /**
     * Agrega un nodo al grafo.
     * Si el nodo es 'A', se almacena como nodoA.
     * Si el nodo es 'B', se almacena como nodoB.
     * Se evitan duplicados chequeando si el ID ya existe.
     * 
     * @param nodo el nodo a agregar al grafo
     */
    public void agregarNodo(Nodo nodo) {
        if (!nodos.containsKey(nodo.getId())) {
            // Agregar el nodo al mapa de nodos
            nodos.put(nodo.getId(), nodo);
            // Crear una lista vacía de adyacentes para este nodo
            listaAdyacencia.put(nodo.getId(), new ArrayList<>());
            
            // Guardar referencia especial si es el punto A (inicio) o B (fin)
            if (nodo.getTipo() == 'A') {
                this.nodoA = nodo;
            } else if (nodo.getTipo() == 'B') {
                this.nodoB = nodo;
            }
        }
    }

    /**
     * Agrega una arista (conexión) entre dos nodos.
     * Como el grafo es NO DIRIGIDO, se agregan conexiones en ambas direcciones:
     * si hay una arista (u -> v), entonces también existe (v -> u).
     * Se evitan aristas duplicadas usando contains().
     * 
     * @param idOrigen el ID del nodo origen
     * @param idDestino el ID del nodo destino
     */
    public void agregarArista(int idOrigen, int idDestino) {
        // Verificar que ambos nodos existan en el grafo
        if (nodos.containsKey(idOrigen) && nodos.containsKey(idDestino)) {
            // Agregar la arista en dirección origen -> destino
            List<Integer> adyacentes = listaAdyacencia.get(idOrigen);
            if (!adyacentes.contains(idDestino)) {
                adyacentes.add(idDestino);
            }
            
            // Para grafo no dirigido, agregar también la arista inversa (destino -> origen)
            adyacentes = listaAdyacencia.get(idDestino);
            if (!adyacentes.contains(idOrigen)) {
                adyacentes.add(idOrigen);
            }
        }
    }

    /**
     * Obtiene un nodo específico por su ID.
     * 
     * @param id el identificador del nodo
     * @return el nodo con ese ID, o null si no existe
     */
    public Nodo getNodo(int id) {
        return nodos.get(id);
    }

    /**
     * Obtiene la lista de nodos adyacentes a un nodo específico.
     * 
     * @param id el identificador del nodo
     * @return lista de IDs de nodos adyacentes (vacía si el nodo no existe)
     */
    public List<Integer> getAdyacentes(int id) {
        return listaAdyacencia.getOrDefault(id, new ArrayList<>());
    }

    /**
     * Obtiene todos los nodos del grafo.
     * 
     * @return colección con todos los nodos
     */
    public Collection<Nodo> getTodosNodos() {
        return nodos.values();
    }

    /**
     * Obtiene el nodo de inicio (marcado con 'A').
     * 
     * @return el nodo inicial del laberinto
     */
    public Nodo getNodoA() {
        return nodoA;
    }

    /**
     * Obtiene el nodo de fin (marcado con 'B').
     * 
     * @return el nodo final del laberinto
     */
    public Nodo getNodoB() {
        return nodoB;
    }

    /**
     * Obtiene la cantidad total de nodos en el grafo.
     * 
     * @return número de nodos
     */
    public int getCantidadNodos() {
        return nodos.size();
    }

    /**
     * Calcula la cantidad total de aristas en el grafo.
     * Nota: Se divide por 2 porque el grafo es no dirigido.
     * Ejemplo: si hay una arista A-B, aparece dos veces en la lista de adyacencia
     * (una en A y otra en B), pero es una sola arista.
     * 
     * @return número de aristas
     */
    public int getCantidadAristas() {
        int count = 0;
        // Contar todas las referencias en las listas de adyacencia
        for (List<Integer> adyacentes : listaAdyacencia.values()) {
            count += adyacentes.size();
        }
        return count / 2; // Dividir por 2 porque cada arista se cuenta dos veces
    }

    /**
     * Obtiene una copia de la lista de adyacencia completa.
     * Se devuelve una copia para evitar modificaciones externas.
     * 
     * @return mapa con la estructura completa de adyacencia
     */
    public Map<Integer, List<Integer>> getListaAdyacencia() {
        return new HashMap<>(listaAdyacencia);
    }

    /**
     * Representación en texto del grafo mostrando su estructura.
     * Útil para debugging y visualización rápida.
     * 
     * @return descripción del grafo con nodos y aristas
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo con ").append(nodos.size()).append(" nodos y ")
          .append(getCantidadAristas()).append(" aristas\n");
        // Para cada nodo, mostrar sus adyacentes
        for (Integer id : listaAdyacencia.keySet()) {
            Nodo nodo = nodos.get(id);
            sb.append("Nodo ").append(id).append(" (").append(nodo.getTipo()).append(") -> ")
              .append(listaAdyacencia.get(id)).append("\n");
        }
        return sb.toString();
    }
}
