package models;

import java.util.Objects;

/**
 * Clase que representa un nodo en el grafo del laberinto.
 * Cada nodo corresponde a una celda transitable (espacio, A o B).
 * 
 * Los nodos son los vértices del grafo que forma el laberinto. Cada nodo tiene:
 * - Una posición (x, y) en el mapa del laberinto
 * - Un identificador único (id) para referencias en el grafo
 * - Un tipo que puede ser: ' ' (espacio normal), 'A' (punto de inicio), 'B' (punto de fin)
 */
public class Nodo {
    // Coordenada X (fila) del nodo en el mapa del laberinto
    private int x;
    // Coordenada Y (columna) del nodo en el mapa del laberinto
    private int y;
    // Tipo de celda: ' ' para espacio transitable, 'A' para inicio, 'B' para fin
    private char tipo;
    // Identificador único del nodo en el grafo (asignado secuencialmente)
    private int id;

    /**
     * Constructor que inicializa un nodo con todos sus atributos.
     * 
     * @param id identificador único del nodo en el grafo
     * @param x coordenada fila en el mapa
     * @param y coordenada columna en el mapa
     * @param tipo tipo de celda (' ', 'A', 'B')
     */
    public Nodo(int id, int x, int y, char tipo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }

    /**
     * Obtiene el identificador único del nodo.
     * @return el ID del nodo
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene la coordenada X (fila) del nodo.
     * @return la coordenada X
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la coordenada Y (columna) del nodo.
     * @return la coordenada Y
     */
    public int getY() {
        return y;
    }

    /**
     * Obtiene el tipo de celda del nodo.
     * @return el tipo (' ', 'A', o 'B')
     */
    public char getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de celda del nodo.
     * @param tipo el nuevo tipo de celda
     */
    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    /**
     * Compara dos nodos por su posición (x, y).
     * Dos nodos son iguales si ocupan la misma celda en el mapa,
     * sin importar su ID o tipo.
     * 
     * @param o el objeto a comparar
     * @return true si los nodos están en la misma posición
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nodo nodo = (Nodo) o;
        return x == nodo.x && y == nodo.y;
    }

    /**
     * Genera el código hash basado en la posición (x, y) del nodo.
     * Esto permite usar nodos en HashSet y HashMap de manera correcta.
     * 
     * @return el código hash del nodo
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Representación en texto del nodo con todos sus atributos.
     * Útil para debugging y logging.
     * 
     * @return una cadena descriptiva del nodo
     */
    @Override
    public String toString() {
        return "Nodo{" + "id=" + id + ", x=" + x + ", y=" + y + ", tipo='" + tipo + '\'' + '}';
    }
}
