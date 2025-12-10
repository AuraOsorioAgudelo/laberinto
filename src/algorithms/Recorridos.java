package algorithms;

import grafo.Grafo;
import models.Nodo;
import java.util.*;

/**
 * Implementa los diferentes tipos de recorridos del grafo.
 */
public class Recorridos {
    private Grafo grafo;
    private List<Integer> resultado;
    private Set<Integer> visitados;

    public Recorridos(Grafo grafo) {
        this.grafo = grafo;
    }

    /**
     * Recorrido en profundidad - PREORDEN
     */
    public List<Integer> dfsPreorden(int idInicio) {
        resultado = new ArrayList<>();
        visitados = new HashSet<>();
        dfsPreordenHelper(idInicio);
        return resultado;
    }

    private void dfsPreordenHelper(int id) {
        visitados.add(id);
        resultado.add(id);

        for (Integer vecino : grafo.getAdyacentes(id)) {
            if (!visitados.contains(vecino)) {
                dfsPreordenHelper(vecino);
            }
        }
    }

    /**
     * Recorrido en profundidad - INORDEN (para grafos generales, similar a preorden)
     */
    public List<Integer> dfsInorden(int idInicio) {
        // Para grafos no binarios, inorden es similar a preorden
        // Visitamos el nodo en medio del proceso
        resultado = new ArrayList<>();
        visitados = new HashSet<>();
        dfsInordenHelper(idInicio, 0);
        return resultado;
    }

    private void dfsInordenHelper(int id, int profundidad) {
        if (visitados.contains(id)) return;
        
        visitados.add(id);
        List<Integer> adyacentes = grafo.getAdyacentes(id);
        
        int mid = adyacentes.size() / 2;
        for (int i = 0; i < mid; i++) {
            Integer vecino = adyacentes.get(i);
            if (!visitados.contains(vecino)) {
                dfsInordenHelper(vecino, profundidad + 1);
            }
        }
        
        resultado.add(id);
        
        for (int i = mid; i < adyacentes.size(); i++) {
            Integer vecino = adyacentes.get(i);
            if (!visitados.contains(vecino)) {
                dfsInordenHelper(vecino, profundidad + 1);
            }
        }
    }

    /**
     * Recorrido en profundidad - POSTORDEN
     */
    public List<Integer> dfsPostorden(int idInicio) {
        resultado = new ArrayList<>();
        visitados = new HashSet<>();
        dfsPostordenHelper(idInicio);
        return resultado;
    }

    private void dfsPostordenHelper(int id) {
        visitados.add(id);

        for (Integer vecino : grafo.getAdyacentes(id)) {
            if (!visitados.contains(vecino)) {
                dfsPostordenHelper(vecino);
            }
        }

        resultado.add(id);
    }

    /**
     * Recorrido en amplitud - BFS
     */
    public List<Integer> bfs(int idInicio) {
        List<Integer> resultado = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> cola = new LinkedList<>();

        cola.offer(idInicio);
        visitados.add(idInicio);

        while (!cola.isEmpty()) {
            int id = cola.poll();
            resultado.add(id);

            for (Integer vecino : grafo.getAdyacentes(id)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.offer(vecino);
                }
            }
        }

        return resultado;
    }

    /**
     * Recorrido heurístico - Greedy Best-First Search hacia B
     */
    public List<Integer> greedyBestFirstSearch(int idInicio, int idDestino) {
        List<Integer> resultado = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();
        PriorityQueue<Integer> cola = new PriorityQueue<>((a, b) -> {
            int distA = calcularDistancia(a, idDestino);
            int distB = calcularDistancia(b, idDestino);
            return Integer.compare(distA, distB);
        });

        cola.offer(idInicio);
        visitados.add(idInicio);

        while (!cola.isEmpty()) {
            int id = cola.poll();
            resultado.add(id);

            if (id == idDestino) {
                break;
            }

            for (Integer vecino : grafo.getAdyacentes(id)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.offer(vecino);
                }
            }
        }

        return resultado;
    }

    /**
     * Calcula la distancia Manhattan entre dos nodos
     */
    private int calcularDistancia(int id1, int id2) {
        Nodo nodo1 = grafo.getNodo(id1);
        Nodo nodo2 = grafo.getNodo(id2);
        return Math.abs(nodo1.getX() - nodo2.getX()) + Math.abs(nodo1.getY() - nodo2.getY());
    }

    /**
     * Imprime un recorrido de forma legible
     */
    public void imprimirRecorrido(String nombre, List<Integer> recorrido) {
        System.out.println("\n=== RECORRIDO " + nombre + " ===");
        System.out.println("Total de nodos visitados: " + recorrido.size());
        System.out.print("Recorrido: ");
        for (int i = 0; i < recorrido.size(); i++) {
            int id = recorrido.get(i);
            Nodo nodo = grafo.getNodo(id);
            if (i > 0) System.out.print(" -> ");
            System.out.print(nodo.getTipo() == ' ' ? "·" : nodo.getTipo());
        }
        System.out.println();
    }
}
