package algorithms;

import grafo.Grafo;
import models.Nodo;
import java.util.*;

/**
 * Clase que implementa diferentes tipos de recorridos (traversals) en un grafo.
 * 
 * TIPOS DE RECORRIDOS IMPLEMENTADOS:
 * 
 * 1. DFS (DEPTH-FIRST SEARCH) - BÚSQUEDA EN PROFUNDIDAD
 *    - Explora lo más profundo posible antes de retroceder
 *    - Usa una pila (stack) recursivo
 *    - Se implementan 3 variantes según cuándo se procesa cada nodo:
 * 
 *    a) PREORDEN: Procesar nodo -> Explorar vecinos
 *       Orden: raíz, luego izquierdo, luego derecho
 *       Útil para: copiar árbol, crear expresión prefija
 * 
 *    b) INORDEN: Vecinos izq -> Procesar nodo -> Vecinos der
 *       En grafos generales, similar a preorden
 *       Útil para: árboles binarios ordenados
 * 
 *    c) POSTORDEN: Explorar vecinos -> Procesar nodo
 *       Útil para: limpiar memoria, expresión postfija, eliminación
 * 
 * 2. BFS (BREADTH-FIRST SEARCH) - BÚSQUEDA EN AMPLITUD
 *    - Explora todos los nodos a distancia k antes de explorar distancia k+1
 *    - Usa una cola (Queue)
 *    - Procesa por niveles (layers)
 * 
 * 3. GREEDY BEST-FIRST SEARCH - BÚSQUEDA HEURÍSTICA CODICIOSO
 *    - Utiliza heurística (distancia Manhattan) para priorizar vecinos
 *    - Usa una cola de prioridad (PriorityQueue)
 *    - No garantiza camino más corto, pero es más rápido en laberintos
 *    - Minimiza distancia estimada al destino
 * 
 * COMPLEJIDAD:
 * - DFS: O(V + E) en tiempo, O(V) en espacio (recursión)
 * - BFS: O(V + E) en tiempo, O(V) en espacio (cola)
 * - Greedy: O(V + E) en tiempo, O(V log V) en espacio (prioridad)
 */
public class Recorridos {
    // Referencia al grafo a recorrer
    private Grafo grafo;
    // Lista temporal para almacenar el resultado del recorrido
    private List<Integer> resultado;
    // Conjunto para rastrear nodos visitados y evitar ciclos
    private Set<Integer> visitados;

    /**
     * Constructor que inicializa los recorridos con un grafo específico.
     * 
     * @param grafo el grafo a recorrer
     */
    public Recorridos(Grafo grafo) {
        this.grafo = grafo;
    }

    // ============ DFS - PROFUNDIDAD ============

    /**
     * Recorrido DFS en PREORDEN.
     * Procesa cada nodo ANTES de explorar sus vecinos.
     * 
     * ORDEN DE PROCESAMIENTO: Nodo -> Vecinos
     * 
     * PROCESO:
     * 1. Marcar nodo como visitado
     * 2. Agregar nodo al resultado
     * 3. Recursivamente explorar cada vecino no visitado
     * 
     * EJEMPLO con grafo: 1-2-3
     * Orden: 1, 2, 3
     * 
     * @param idInicio ID del nodo donde comenzar
     * @return lista con el recorrido en preorden
     */
    public List<Integer> dfsPreorden(int idInicio) {
        resultado = new ArrayList<>();
        visitados = new HashSet<>();
        dfsPreordenHelper(idInicio);
        return resultado;
    }

    /**
     * Método auxiliar recursivo para DFS preorden.
     * 
     * @param id ID del nodo actual siendo procesado
     */
    private void dfsPreordenHelper(int id) {
        // Marcar como visitado
        visitados.add(id);
        // Procesar ANTES de explorar vecinos (PREORDEN)
        resultado.add(id);

        // Explorar recursivamente cada vecino no visitado
        for (Integer vecino : grafo.getAdyacentes(id)) {
            if (!visitados.contains(vecino)) {
                dfsPreordenHelper(vecino);
            }
        }
    }

    /**
     * Recorrido DFS en INORDEN.
     * Para grafos generales, es similar a preorden.
     * Para árboles binarios, procesa: izquierdo -> nodo -> derecho.
     * 
     * ORDEN DE PROCESAMIENTO: Mitad izq vecinos -> Nodo -> Mitad der vecinos
     * 
     * Implementación para grafos no binarios:
     * Procesa la mitad de los vecinos, luego el nodo, luego el resto.
     * 
     * @param idInicio ID del nodo donde comenzar
     * @return lista con el recorrido en inorden
     */
    public List<Integer> dfsInorden(int idInicio) {
        resultado = new ArrayList<>();
        visitados = new HashSet<>();
        dfsInordenHelper(idInicio, 0);
        return resultado;
    }

    /**
     * Método auxiliar recursivo para DFS inorden.
     * Implementa un inorden adaptado para grafos generales.
     * 
     * @param id ID del nodo actual siendo procesado
     * @param profundidad profundidad actual en el árbol de recursión
     */
    private void dfsInordenHelper(int id, int profundidad) {
        // Si ya fue visitado, no hacer nada (evitar ciclos)
        if (visitados.contains(id)) return;
        
        visitados.add(id);
        List<Integer> adyacentes = grafo.getAdyacentes(id);
        
        // Calcular punto medio de la lista de adyacentes
        int mid = adyacentes.size() / 2;
        
        // Procesar primera mitad de vecinos (izquierdo)
        for (int i = 0; i < mid; i++) {
            Integer vecino = adyacentes.get(i);
            if (!visitados.contains(vecino)) {
                dfsInordenHelper(vecino, profundidad + 1);
            }
        }
        
        // Procesar nodo (MEDIO)
        resultado.add(id);
        
        // Procesar segunda mitad de vecinos (derecho)
        for (int i = mid; i < adyacentes.size(); i++) {
            Integer vecino = adyacentes.get(i);
            if (!visitados.contains(vecino)) {
                dfsInordenHelper(vecino, profundidad + 1);
            }
        }
    }

    /**
     * Recorrido DFS en POSTORDEN.
     * Procesa cada nodo DESPUÉS de explorar todos sus vecinos.
     * 
     * ORDEN DE PROCESAMIENTO: Vecinos -> Nodo
     * 
     * PROCESO:
     * 1. Marcar nodo como visitado
     * 2. Recursivamente explorar cada vecino no visitado
     * 3. Agregar nodo al resultado (DESPUÉS de los vecinos)
     * 
     * EJEMPLO con grafo: 1-2-3
     * Orden: 3, 2, 1 (opuesto al preorden)
     * 
     * @param idInicio ID del nodo donde comenzar
     * @return lista con el recorrido en postorden
     */
    public List<Integer> dfsPostorden(int idInicio) {
        resultado = new ArrayList<>();
        visitados = new HashSet<>();
        dfsPostordenHelper(idInicio);
        return resultado;
    }

    /**
     * Método auxiliar recursivo para DFS postorden.
     * 
     * @param id ID del nodo actual siendo procesado
     */
    private void dfsPostordenHelper(int id) {
        // Marcar como visitado
        visitados.add(id);

        // Explorar vecinos ANTES de procesar el nodo (POSTORDEN)
        for (Integer vecino : grafo.getAdyacentes(id)) {
            if (!visitados.contains(vecino)) {
                dfsPostordenHelper(vecino);
            }
        }
        
        // Procesar DESPUÉS de explorar vecinos
        resultado.add(id);
    }

    // ============ BFS - AMPLITUD ============

    /**
     * Recorrido BFS (BÚSQUEDA EN AMPLITUD).
     * Explora el grafo por NIVELES: primero todos los vecinos del origen,
     * luego los vecinos de los vecinos, y así sucesivamente.
     * 
     * PROCESO:
     * 1. Inicializar una cola con el nodo origen
     * 2. Mientras la cola no esté vacía:
     *    a. Sacar el primer nodo de la cola
     *    b. Procesar el nodo
     *    c. Agregar todos sus vecinos no visitados a la cola
     * 
     * PROPIEDADES:
     * - Garantiza encontrar el camino más corto en grafos sin pesos
     * - Procesa nodos en orden de distancia desde el origen
     * - Usa cola (FIFO) en lugar de pila (LIFO)
     * 
     * COMPLEJIDAD:
     * - Tiempo: O(V + E)
     * - Espacio: O(V) para la cola
     * 
     * @param idInicio ID del nodo donde comenzar
     * @return lista con el recorrido en amplitud
     */
    public List<Integer> bfs(int idInicio) {
        List<Integer> resultado = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();
        // Cola para procesar nodos por niveles (FIFO)
        Queue<Integer> cola = new LinkedList<>();

        // Inicializar con el nodo origen
        cola.offer(idInicio);
        visitados.add(idInicio);

        // Procesar mientras haya nodos en la cola
        while (!cola.isEmpty()) {
            // Sacar el primer nodo (FIFO)
            int id = cola.poll();
            // Procesar nodo
            resultado.add(id);

            // Agregar vecinos no visitados a la cola
            for (Integer vecino : grafo.getAdyacentes(id)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.offer(vecino);
                }
            }
        }

        return resultado;
    }

    // ============ GREEDY BEST-FIRST SEARCH - HEURÍSTICO ============

    /**
     * Recorrido con Greedy Best-First Search (búsqueda codiciosa heurística).
     * 
     * ESTRATEGIA:
     * - Usa una cola de prioridad que ordena por distancia heurística al destino
     * - Siempre expande el nodo más cercano (según heurística) al destino
     * - Minimiza la distancia Manhattan estimada
     * 
     * VENTAJAS:
     * - Encuentra el destino más rápido que BFS puro
     * - Es más inteligente que BFS puro
     * - Útil para laberintos grandes
     * 
     * DESVENTAJAS:
     * - No garantiza el camino más corto (a diferencia de BFS)
     * - Puede ser ineficiente si la heurística es mala
     * 
     * COMPLEJIDAD:
     * - Tiempo: O((V + E) log V) por la cola de prioridad
     * - Espacio: O(V) para la cola
     * 
     * @param idInicio ID del nodo donde comenzar
     * @param idDestino ID del nodo destino (usado para calcular heurística)
     * @return lista con el recorrido usando greedy best-first search
     */
    public List<Integer> greedyBestFirstSearch(int idInicio, int idDestino) {
        List<Integer> resultado = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();
        // Cola de prioridad que ordena por distancia heurística al destino
        PriorityQueue<Integer> cola = new PriorityQueue<>((a, b) -> {
            // Comparar distancia de a vs b al destino
            int distA = calcularDistancia(a, idDestino);
            int distB = calcularDistancia(b, idDestino);
            return Integer.compare(distA, distB);
        });

        // Inicializar con el nodo origen
        cola.offer(idInicio);
        visitados.add(idInicio);

        // Explorar hasta encontrar destino o no haya más nodos
        while (!cola.isEmpty()) {
            // Sacar el nodo más cercano (según heurística) al destino
            int id = cola.poll();
            resultado.add(id);

            // Si encontramos el destino, podemos parar
            if (id == idDestino) {
                break;
            }

            // Agregar vecinos no visitados a la cola de prioridad
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
     * Calcula la heurística de distancia Manhattan entre dos nodos.
     * 
     * DISTANCIA MANHATTAN:
     * La suma de las diferencias absolutas de coordenadas.
     * Fórmula: |x1 - x2| + |y1 - y2|
     * 
     * EJEMPLO:
     * Nodo A en (0, 0), Nodo B en (3, 4)
     * Distancia = |0-3| + |0-4| = 3 + 4 = 7
     * 
     * Esta heurística es útil para laberintos porque:
     * - Es admisible (nunca sobrestima la distancia real)
     * - Es fácil de calcular
     * - Es consistente para movimientos en 4 direcciones
     * 
     * @param id1 ID del primer nodo
     * @param id2 ID del segundo nodo
     * @return distancia Manhattan entre los dos nodos
     */
    private int calcularDistancia(int id1, int id2) {
        Nodo nodo1 = grafo.getNodo(id1);
        Nodo nodo2 = grafo.getNodo(id2);
        return Math.abs(nodo1.getX() - nodo2.getX()) + Math.abs(nodo1.getY() - nodo2.getY());
    }

    /**
     * Imprime un recorrido de forma legible en consola.
     * 
     * INFORMACIÓN MOSTRADA:
     * - Nombre del recorrido (DFS-PREORDEN, BFS, etc.)
     * - Total de nodos visitados
     * - Visualización del recorrido (A, B, ·, etc.)
     * 
     * @param nombre nombre descriptivo del recorrido
     * @param recorrido lista de IDs de nodos en orden de recorrido
     */
    public void imprimirRecorrido(String nombre, List<Integer> recorrido) {
        System.out.println("\n=== RECORRIDO " + nombre + " ===");
        System.out.println("Total de nodos visitados: " + recorrido.size());
        System.out.print("Recorrido: ");
        
        // Mostrar cada nodo en orden
        for (int i = 0; i < recorrido.size(); i++) {
            int id = recorrido.get(i);
            Nodo nodo = grafo.getNodo(id);
            
            // Separar nodos con flechas
            if (i > 0) System.out.print(" -> ");
            
            // Mostrar representación visual:
            // - 'A' para nodo inicial
            // - 'B' para nodo final
            // - '·' para espacios normales
            System.out.print(nodo.getTipo() == ' ' ? "·" : nodo.getTipo());
        }
        System.out.println();
    }
}
