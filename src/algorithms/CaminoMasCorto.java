package algorithms;

import grafo.Grafo;
import models.Nodo;
import java.util.*;

/**
 * Clase que implementa el algoritmo BFS (Breadth-First Search) para encontrar el camino más corto.
 * 
 * ALGORITMO BFS - BÚSQUEDA EN AMPLITUD:
 * BFS es un algoritmo de exploración de grafos que garantiza encontrar el camino más CORTO
 * entre dos nodos en términos de cantidad de aristas (pasos).
 * 
 * CARACTERÍSTICAS:
 * - Usa una cola (Queue) para explorar nodos por niveles
 * - Garantiza encontrar el camino más corto (número mínimo de pasos)
 * - Complejidad temporal: O(V + E) donde V=nodos, E=aristas
 * - Complejidad espacial: O(V) para la cola y el conjunto de visitados
 * 
 * CÓMO FUNCIONA:
 * 1. Comienza en el nodo origen
 * 2. Agrega a una cola todos los vecinos no visitados
 * 3. Procesa nodos de la cola por orden FIFO (primero en entrar, primero en salir)
 * 4. Registra el "padre" de cada nodo (de dónde vino) para reconstruir el camino
 * 5. Al encontrar el destino, reconstruye el camino desde origen hasta destino
 * 6. Si no hay camino, retorna una lista vacía
 */
public class CaminoMasCorto {
    // Referencia al grafo del laberinto
    private Grafo grafo;

    /**
     * Constructor que inicializa el algoritmo con un grafo específico.
     * 
     * @param grafo el grafo donde buscar caminos
     */
    public CaminoMasCorto(Grafo grafo) {
        this.grafo = grafo;
    }

    /**
     * Encuentra el camino más corto entre dos nodos usando BFS.
     * 
     * PROCESO:
     * 1. Inicializar estructuras:
     *    - cola: almacena nodos a explorar en orden FIFO
     *    - padres: mapea cada nodo visitado a su predecesor en el árbol de búsqueda
     *    - visitados: conjunto para evitar revisar el mismo nodo dos veces
     * 
     * 2. Iniciar desde el origen:
     *    - Agregar origen a la cola
     *    - Marcar como visitado
     *    - Registrar que el origen no tiene padre (-1)
     * 
     * 3. Exploración (mientras la cola no esté vacía):
     *    - Sacar el primer nodo de la cola
     *    - Si es el destino, reconstruir el camino
     *    - Si no, agregar todos sus vecinos no visitados a la cola
     * 
     * 4. Si se vacía la cola sin encontrar destino, no hay camino
     * 
     * @param idOrigen ID del nodo inicial
     * @param idDestino ID del nodo final
     * @return lista de IDs del camino más corto (vacía si no hay camino)
     */
    public List<Integer> encontrarCaminoMasCorto(int idOrigen, int idDestino) {
        // Cola para implementar BFS (procesa nodos por niveles)
        Queue<Integer> cola = new LinkedList<>();
        // Mapa que almacena el nodo anterior en el camino (para reconstruir después)
        Map<Integer, Integer> padres = new HashMap<>();
        // Conjunto de nodos ya visitados (evita ciclos)
        Set<Integer> visitados = new HashSet<>();

        // Inicializar con el nodo origen
        cola.offer(idOrigen);
        visitados.add(idOrigen);
        padres.put(idOrigen, -1); // El origen no tiene padre

        // Explorar nodos por niveles (BFS)
        while (!cola.isEmpty()) {
            // Sacar el siguiente nodo a procesar
            int idActual = cola.poll();

            // Si encontramos el destino, reconstruir y retornar el camino
            if (idActual == idDestino) {
                return reconstruirCamino(padres, idDestino);
            }

            // Explorar todos los vecinos del nodo actual
            for (Integer idVecino : grafo.getAdyacentes(idActual)) {
                // Si no hemos visitado este vecino
                if (!visitados.contains(idVecino)) {
                    visitados.add(idVecino);
                    // Guardar que el vecino vino desde idActual
                    padres.put(idVecino, idActual);
                    // Agregar a la cola para procesarlo después
                    cola.offer(idVecino);
                }
            }
        }

        // Si llegamos aquí, no hay camino entre origen y destino
        return new ArrayList<>();
    }

    /**
     * Reconstruye el camino desde el origen hasta el destino usando el mapa de padres.
     * 
     * PROCESO:
     * 1. Comenzar en el nodo destino
     * 2. Seguir el camino hacia atrás usando el mapa de padres
     * 3. Agregar cada nodo al inicio de la lista (para que quede en orden correcto)
     * 4. Parar cuando se alcance el origen (padre = -1)
     * 
     * EJEMPLO:
     * Si padres es: {5->3, 3->1, 1->-1} (camino 1->3->5)
     * Comenzando desde 5:
     * - Agregar 5: [5]
     * - Ir a padre (3): [3, 5]
     * - Ir a padre (1): [1, 3, 5]
     * - Ir a padre (-1): parar
     * 
     * @param padres mapa que relaciona cada nodo con su predecesor
     * @param destino nodo final del camino
     * @return lista de IDs del camino desde origen hasta destino
     */
    private List<Integer> reconstruirCamino(Map<Integer, Integer> padres, int destino) {
        List<Integer> camino = new ArrayList<>();
        int actual = destino;

        // Seguir el camino hacia atrás desde el destino hasta el origen
        while (actual != -1) {
            // Agregar al inicio para mantener el orden correcto
            camino.add(0, actual);
            // Moverse al nodo anterior en el camino
            actual = padres.get(actual);
        }

        return camino;
    }

    /**
     * Imprime el camino más corto encontrado de forma legible.
     * Muestra:
     * - Si hay camino: la longitud (número de pasos) y la ruta visual
     * - Si no hay camino: un mensaje indicándolo
     * 
     * La ruta visual usa:
     * - 'A' para el nodo de inicio
     * - 'B' para el nodo de fin
     * - '·' para nodos intermedios
     * - ' -> ' como separador
     * 
     * @param camino la lista de IDs del camino a imprimir
     */
    public void imprimirCamino(List<Integer> camino) {
        if (camino.isEmpty()) {
            System.out.println("No hay camino entre A y B");
            return;
        }

        System.out.println("\n=== CAMINO MÁS CORTO ===");
        // La longitud en pasos es cantidad de nodos - 1
        System.out.println("Longitud del camino: " + (camino.size() - 1) + " pasos");
        System.out.print("Camino: ");
        
        // Imprimir cada nodo del camino
        for (int i = 0; i < camino.size(); i++) {
            int id = camino.get(i);
            Nodo nodo = grafo.getNodo(id);
            if (i > 0) System.out.print(" -> ");
            // Mostrar 'A', 'B', o '·' según el tipo de nodo
            System.out.print(nodo.getTipo() == ' ' ? "·" : nodo.getTipo());
        }
        System.out.println();
    }

    /**
     * Genera una versión visual del mapa con el camino marcado.
     * Crea una copia del mapa original y marca los nodos del camino con '·',
     * excepto por el origen (A) y destino (B) que permanecen con sus símbolos.
     * 
     * Esto permite visualizar la solución del laberinto sobre el mapa original.
     * 
     * @param mapa matriz 2D original del laberinto
     * @param camino lista de IDs del camino a marcar
     */
    public void imprimirMapaConCamino(char[][] mapa, List<Integer> camino) {
        if (camino.isEmpty()) {
            return;
        }

        // Crear una copia del mapa original para no modificar el original
        char[][] mapaConCamino = new char[mapa.length][];
        for (int i = 0; i < mapa.length; i++) {
            mapaConCamino[i] = mapa[i].clone();
        }

        // Marcar el camino en el mapa (excepto origen y destino)
        // Saltamos el primer nodo (origen) e índice -1 (destino)
        for (int i = 1; i < camino.size() - 1; i++) {
            int id = camino.get(i);
            Nodo nodo = grafo.getNodo(id);
            // Usar '·' para marcar el camino
            mapaConCamino[nodo.getX()][nodo.getY()] = '·';
        }

        // Imprimir el mapa con el camino marcado
        System.out.println("\n=== MAPA CON CAMINO MARCADO ===");
        for (int i = 0; i < mapaConCamino.length; i++) {
            for (int j = 0; j < mapaConCamino[i].length; j++) {
                System.out.print(mapaConCamino[i][j]);
            }
            System.out.println();
        }
    }
}
