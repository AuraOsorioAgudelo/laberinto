package algorithms;

import grafo.Grafo;
import models.Nodo;
import java.util.*;

/**
 * Implementa algoritmo BFS para encontrar el camino más corto entre dos nodos.
 */
public class CaminoMasCorto {
    private Grafo grafo;

    public CaminoMasCorto(Grafo grafo) {
        this.grafo = grafo;
    }

    /**
     * Encuentra el camino más corto usando BFS
     */
    public List<Integer> encontrarCaminoMasCorto(int idOrigen, int idDestino) {
        Queue<Integer> cola = new LinkedList<>();
        Map<Integer, Integer> padres = new HashMap<>();
        Set<Integer> visitados = new HashSet<>();

        cola.offer(idOrigen);
        visitados.add(idOrigen);
        padres.put(idOrigen, -1);

        while (!cola.isEmpty()) {
            int idActual = cola.poll();

            if (idActual == idDestino) {
                return reconstruirCamino(padres, idDestino);
            }

            for (Integer idVecino : grafo.getAdyacentes(idActual)) {
                if (!visitados.contains(idVecino)) {
                    visitados.add(idVecino);
                    padres.put(idVecino, idActual);
                    cola.offer(idVecino);
                }
            }
        }

        return new ArrayList<>(); // Sin camino
    }

    /**
     * Reconstruye el camino desde el nodo origen hasta el destino
     */
    private List<Integer> reconstruirCamino(Map<Integer, Integer> padres, int destino) {
        List<Integer> camino = new ArrayList<>();
        int actual = destino;

        while (actual != -1) {
            camino.add(0, actual);
            actual = padres.get(actual);
        }

        return camino;
    }

    /**
     * Imprime el camino más corto de forma legible
     */
    public void imprimirCamino(List<Integer> camino) {
        if (camino.isEmpty()) {
            System.out.println("No hay camino entre A y B");
            return;
        }

        System.out.println("\n=== CAMINO MÁS CORTO ===");
        System.out.println("Longitud del camino: " + (camino.size() - 1) + " pasos");
        System.out.print("Camino: ");
        for (int i = 0; i < camino.size(); i++) {
            int id = camino.get(i);
            Nodo nodo = grafo.getNodo(id);
            if (i > 0) System.out.print(" -> ");
            System.out.print(nodo.getTipo() == ' ' ? "·" : nodo.getTipo());
        }
        System.out.println();
    }

    /**
     * Retorna una versión del mapa con el camino marcado
     */
    public void imprimirMapaConCamino(char[][] mapa, List<Integer> camino) {
        if (camino.isEmpty()) {
            return;
        }

        // Crear copia del mapa
        char[][] mapaConCamino = new char[mapa.length][];
        for (int i = 0; i < mapa.length; i++) {
            mapaConCamino[i] = mapa[i].clone();
        }

        // Marcar el camino (excepto origen y destino)
        for (int i = 1; i < camino.size() - 1; i++) {
            int id = camino.get(i);
            Nodo nodo = grafo.getNodo(id);
            mapaConCamino[nodo.getX()][nodo.getY()] = '·';
        }

        System.out.println("\n=== MAPA CON CAMINO MARCADO ===");
        for (int i = 0; i < mapaConCamino.length; i++) {
            for (int j = 0; j < mapaConCamino[i].length; j++) {
                System.out.print(mapaConCamino[i][j]);
            }
            System.out.println();
        }
    }
}
