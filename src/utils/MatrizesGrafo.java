package utils;

import grafo.Grafo;
import models.Nodo;
import java.util.*;

/**
 * Clase que genera y manipula matrices de representación de grafos.
 * 
 * REPRESENTACIONES DE GRAFOS:
 * Un grafo puede representarse de varias formas, cada una con ventajas y desventajas.
 * Las dos más comunes son:
 * 
 * 1. MATRIZ DE ADYACENCIA (V x V):
 *    - Una matriz cuadrada de tamaño V x V (V = número de vértices/nodos)
 *    - Entrada (i,j) = 1 si existe arista de i a j, 0 si no
 *    - Para grafos no dirigidos: matriz simétrica (si hay i->j, hay j->i)
 *    - VENTAJA: Búsqueda rápida O(1) de si dos nodos están conectados
 *    - DESVENTAJA: Usa mucho espacio O(V²), incluso con pocos edges
 *    
 *    EJEMPLO para grafo con 4 nodos donde 0-1, 1-2, 2-3:
 *      0 1 2 3
 *    0 0 1 0 0
 *    1 1 0 1 0
 *    2 0 1 0 1
 *    3 0 0 1 0
 * 
 * 2. MATRIZ DE INCIDENCIA (V x E):
 *    - Una matriz de tamaño V x E (V = nodos, E = aristas)
 *    - Entrada (i,j) = 1 si nodo i es incidente a arista j, 0 si no
 *    - Para cada arista, hay exactamente 2 unos (uno en cada extremo)
 *    - Para grafos no dirigidos: cada columna tiene exactamente 2 unos
 *    - VENTAJA: Usa menos espacio con pocos edges O(V*E)
 *    - DESVENTAJA: Búsqueda menos directa
 *    
 *    EJEMPLO para el mismo grafo (aristas: 0-1, 1-2, 2-3):
 *      E0 E1 E2
 *    0  1  0  0
 *    1  1  1  0
 *    2  0  1  1
 *    3  0  0  1
 */
public class MatrizesGrafo {
    // Referencia al grafo a representar
    private Grafo grafo;
    // Matriz de adyacencia (inicializada bajo demanda)
    private int[][] matrizAdyacencia;
    // Matriz de incidencia (inicializada bajo demanda)
    private int[][] matrizIncidencia;
    // Cantidad total de nodos en el grafo
    private int cantidadNodos;
    // Cantidad total de aristas en el grafo
    private int cantidadAristas;
    // Mapeo de ID de nodo a índice en la matriz (para búsqueda rápida)
    private Map<Integer, Integer> mapaIdAIndice;

    /**
     * Constructor que inicializa con un grafo específico.
     * Calcula el mapeo de IDs a índices de matriz.
     * 
     * @param grafo el grafo a representar como matrices
     */
    public MatrizesGrafo(Grafo grafo) {
        this.grafo = grafo;
        this.cantidadNodos = grafo.getCantidadNodos();
        this.cantidadAristas = grafo.getCantidadAristas();
        this.mapaIdAIndice = new HashMap<>();
        construirMapaIndices();
    }

    /**
     * Construye un mapeo de IDs de nodos a índices de matriz.
     * 
     * Necesario porque los IDs de nodos pueden no ser secuenciales (0, 1, 2, ...),
     * pero los índices de matriz deben serlo.
     * 
     * EJEMPLO:
     * Si los IDs de nodos son {0, 5, 10}, se mapean a índices {0, 1, 2}
     * 
     * PROCESO:
     * - Iterar sobre todos los nodos del grafo
     * - Asignar un índice secuencial a cada nodo
     * - Guardar la relación ID -> Índice
     */
    private void construirMapaIndices() {
        int indice = 0;
        // Iterar sobre cada nodo del grafo
        for (Nodo nodo : grafo.getTodosNodos()) {
            // Mapear ID del nodo a índice secuencial
            mapaIdAIndice.put(nodo.getId(), indice);
            indice++;
        }
    }

    /**
     * Genera la matriz de adyacencia para el grafo.
     * 
     * PROCESO:
     * 1. Crear matriz de tamaño cantidadNodos x cantidadNodos
     * 2. Inicializar toda con ceros (grafo sin conexiones)
     * 3. Para cada arista (i,j) en el grafo:
     *    - Obtener índices i', j' usando el mapa
     *    - Establecer matriz[i'][j'] = 1
     * 
     * RESULTADO:
     * Una matriz simétrica (para grafo no dirigido) donde:
     * - matriz[i][j] = 1 si hay arista entre nodo i y nodo j
     * - matriz[i][j] = 0 si no hay arista
     * 
     * CARACTERÍSTICAS:
     * - Diagonal principal será 0 (sin auto-loops)
     * - Simétrica: matriz[i][j] = matriz[j][i]
     * 
     * @return la matriz de adyacencia generada
     */
    public int[][] generarMatrizAdyacencia() {
        // Crear matriz cuadrada de nodos x nodos
        matrizAdyacencia = new int[cantidadNodos][cantidadNodos];
        // Inicializar con ceros (ya lo hace por defecto en Java)

        // Obtener la lista de adyacencia del grafo
        Map<Integer, List<Integer>> listaAdyacencia = grafo.getListaAdyacencia();
        
        // Para cada nodo origen y sus conexiones
        for (Integer idOrigen : listaAdyacencia.keySet()) {
            // Convertir ID a índice de matriz
            int i = mapaIdAIndice.get(idOrigen);
            
            // Para cada nodo destino conectado a idOrigen
            for (Integer idDestino : listaAdyacencia.get(idOrigen)) {
                // Convertir ID a índice de matriz
                int j = mapaIdAIndice.get(idDestino);
                // Marcar que existe la arista
                matrizAdyacencia[i][j] = 1;
            }
        }

        return matrizAdyacencia;
    }

    /**
     * Genera la matriz de incidencia para el grafo.
     * 
     * PROCESO:
     * 1. Crear matriz de tamaño cantidadNodos x cantidadAristas
     * 2. Inicializar toda con ceros
     * 3. Para cada arista en el grafo:
     *    a. Obtener los dos nodos extremos
     *    b. Marcar con 1 en ambas filas de esa columna
     * 4. Evitar duplicados usando un Set de aristas ya procesadas
     *    (importante para grafos no dirigidos)
     * 
     * RESULTADO:
     * Una matriz donde cada columna representa una arista.
     * Cada columna tendrá exactamente 2 unos (los nodos incidentes).
     * 
     * EJEMPLO (grafo con aristas 0-1, 1-2):
     *      A0 A1
     *    0  1  0
     *    1  1  1
     *    2  0  1
     * 
     * NOTA: Se usa un Set para garantizar que cada arista se procesa una sola vez,
     * ya que en un grafo no dirigido, la arista (1,2) es la misma que (2,1).
     * 
     * @return la matriz de incidencia generada
     */
    public int[][] generarMatrizIncidencia() {
        // Crear matriz de nodos x aristas
        matrizIncidencia = new int[cantidadNodos][cantidadAristas];
        // Inicializar con ceros
        
        // Índice para la columna de aristas (va de 0 a cantidadAristas-1)
        int indexArista = 0;
        // Set para evitar contar la misma arista dos veces en grafos no dirigidos
        Set<String> aristasAgregadas = new HashSet<>();

        // Obtener la lista de adyacencia del grafo
        Map<Integer, List<Integer>> listaAdyacencia = grafo.getListaAdyacencia();
        
        // Para cada nodo origen
        for (Integer idOrigen : listaAdyacencia.keySet()) {
            int i = mapaIdAIndice.get(idOrigen);
            
            // Para cada nodo destino conectado
            for (Integer idDestino : listaAdyacencia.get(idOrigen)) {
                // Crear una clave única para la arista
                // Usar min-max para garantizar que (1,2) y (2,1) tienen la misma clave
                String aristaKey = Math.min(idOrigen, idDestino) + "-" + Math.max(idOrigen, idDestino);
                
                // Si no hemos procesado esta arista
                if (!aristasAgregadas.contains(aristaKey)) {
                    int j = mapaIdAIndice.get(idDestino);
                    // Marcar ambos nodos como incidentes a esta arista
                    matrizIncidencia[i][indexArista] = 1;
                    matrizIncidencia[j][indexArista] = 1;
                    // Registrar que hemos procesado esta arista
                    aristasAgregadas.add(aristaKey);
                    // Pasar a la siguiente arista
                    indexArista++;
                }
            }
        }

        return matrizIncidencia;
    }

    /**
     * Imprime la matriz de adyacencia en consola de forma legible.
     * 
     * FORMATO:
     * - Primera fila: encabezados con números de columnas
     * - Primero column: números de filas
     * - Cuerpo: valores 0 o 1
     * - Cada valor ocupa 3 espacios para alineación
     * 
     * INTERPRETACIÓN:
     * - Leer fila i, columna j
     * - Si valor es 1, existe arista entre nodo i y nodo j
     * - Si valor es 0, no existe conexión
     */
    public void imprimirMatrizAdyacencia() {
        // Generar si no existe aún
        if (matrizAdyacencia == null) {
            generarMatrizAdyacencia();
        }

        System.out.println("\n=== MATRIZ DE ADYACENCIA ===");
        System.out.println("Dimensión: " + cantidadNodos + "x" + cantidadNodos);
        
        // Imprimir encabezado con números de columnas
        System.out.print("    ");
        for (int j = 0; j < cantidadNodos; j++) {
            System.out.print(String.format("%3d ", j));
        }
        System.out.println();

        // Imprimir cada fila con su número
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.print(String.format("%3d ", i));
            for (int j = 0; j < cantidadNodos; j++) {
                System.out.print(String.format("%3d ", matrizAdyacencia[i][j]));
            }
            System.out.println();
        }
    }

    /**
     * Imprime la matriz de incidencia en consola de forma legible.
     * 
     * FORMATO:
     * - Primera fila: encabezados con números de aristas
     * - Primera columna: números de nodos
     * - Cuerpo: valores 0 o 1
     * - Cada valor ocupa 3 espacios para alineación
     * 
     * INTERPRETACIÓN:
     * - Leer fila i (nodo i), columna j (arista j)
     * - Si valor es 1, el nodo i es incidente a la arista j
     * - Cada columna debería tener exactamente 2 unos (una arista no dirigida)
     */
    public void imprimirMatrizIncidencia() {
        // Generar si no existe aún
        if (matrizIncidencia == null) {
            generarMatrizIncidencia();
        }

        System.out.println("\n=== MATRIZ DE INCIDENCIA ===");
        System.out.println("Dimensión: " + cantidadNodos + "x" + cantidadAristas);
        
        // Imprimir encabezado con números de aristas
        System.out.print("    ");
        for (int j = 0; j < cantidadAristas; j++) {
            System.out.print(String.format("%3d ", j));
        }
        System.out.println();

        // Imprimir cada fila con su número de nodo
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.print(String.format("%3d ", i));
            for (int j = 0; j < cantidadAristas; j++) {
                System.out.print(String.format("%3d ", matrizIncidencia[i][j]));
            }
            System.out.println();
        }
    }

    /**
     * Obtiene la matriz de adyacencia generada.
     * Genera si no ha sido creada aún.
     * 
     * @return la matriz de adyacencia
     */
    public int[][] getMatrizAdyacencia() {
        if (matrizAdyacencia == null) {
            generarMatrizAdyacencia();
        }
        return matrizAdyacencia;
    }

    /**
     * Obtiene la matriz de incidencia generada.
     * Genera si no ha sido creada aún.
     * 
     * @return la matriz de incidencia
     */
    public int[][] getMatrizIncidencia() {
        if (matrizIncidencia == null) {
            generarMatrizIncidencia();
        }
        return matrizIncidencia;
    }

    /**
     * Obtiene una copia del mapeo de IDs a índices de matriz.
     * 
     * @return mapa de ID -> Índice
     */
    public Map<Integer, Integer> getMapaIdAIndice() {
        return new HashMap<>(mapaIdAIndice);
    }
}
