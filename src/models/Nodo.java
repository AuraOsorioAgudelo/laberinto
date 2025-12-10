package models;

import java.util.Objects;

/**
 * Clase que representa un nodo en el grafo del laberinto.
 * Cada nodo corresponde a una celda transitable (espacio, A o B).
 */
public class Nodo {
    private int x;
    private int y;
    private char tipo; // ' ' para espacio, 'A' para inicio, 'B' para fin
    private int id;    // Identificador único del nodo

    public Nodo(int id, int x, int y, char tipo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nodo nodo = (Nodo) o;
        return x == nodo.x && y == nodo.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Nodo{" + "id=" + id + ", x=" + x + ", y=" + y + ", tipo='" + tipo + '\'' + '}';
    }
}
