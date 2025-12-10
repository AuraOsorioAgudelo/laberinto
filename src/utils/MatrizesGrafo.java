package utils;

import grafo.Grafo;
import models.Nodo;
import java.util.*;

/**
 * Genera matrices de adyacencia e incidencia para el grafo.
 */
public class MatrizesGrafo {
    private Grafo grafo;
    private int[][] matrizAdyacencia;
    private int[][] matrizIncidencia;
    private int cantidadNodos;
    private int cantidadAristas;
    private Map<Integer, Integer> mapaIdAIndice;

    public MatrizesGrafo(Grafo grafo) {
        this.grafo = grafo;
        this.cantidadNodos = grafo.getCantidadNodos();
        this.cantidadAristas = grafo.getCantidadAristas();
        this.mapaIdAIndice = new HashMap<>();
        construirMapaIndices();
    }

    /**
     * Construye un mapeo de ID de nodo a índice de matriz
     */
    private void construirMapaIndices() {
        int indice = 0;
        for (Nodo nodo : grafo.getTodosNodos()) {
            mapaIdAIndice.put(nodo.getId(), indice);
            indice++;
        }
    }

    /**
     * Genera la matriz de adyacencia
     */
    public int[][] generarMatrizAdyacencia() {
        matrizAdyacencia = new int[cantidadNodos][cantidadNodos];

        Map<Integer, List<Integer>> listaAdyacencia = grafo.getListaAdyacencia();
        for (Integer idOrigen : listaAdyacencia.keySet()) {
            int i = mapaIdAIndice.get(idOrigen);
            for (Integer idDestino : listaAdyacencia.get(idOrigen)) {
                int j = mapaIdAIndice.get(idDestino);
                matrizAdyacencia[i][j] = 1;
            }
        }

        return matrizAdyacencia;
    }

    /**
     * Genera la matriz de incidencia
     */
    public int[][] generarMatrizIncidencia() {
        matrizIncidencia = new int[cantidadNodos][cantidadAristas];
        int indexArista = 0;
        Set<String> aristasAgregadas = new HashSet<>();

        Map<Integer, List<Integer>> listaAdyacencia = grafo.getListaAdyacencia();
        for (Integer idOrigen : listaAdyacencia.keySet()) {
            int i = mapaIdAIndice.get(idOrigen);
            for (Integer idDestino : listaAdyacencia.get(idOrigen)) {
                // Para grafo no dirigido, solo procesamos cada arista una vez
                String aristaKey = Math.min(idOrigen, idDestino) + "-" + Math.max(idOrigen, idDestino);
                if (!aristasAgregadas.contains(aristaKey)) {
                    int j = mapaIdAIndice.get(idDestino);
                    matrizIncidencia[i][indexArista] = 1;
                    matrizIncidencia[j][indexArista] = 1;
                    aristasAgregadas.add(aristaKey);
                    indexArista++;
                }
            }
        }

        return matrizIncidencia;
    }

    /**
     * Imprime la matriz de adyacencia
     */
    public void imprimirMatrizAdyacencia() {
        if (matrizAdyacencia == null) {
            generarMatrizAdyacencia();
        }

        System.out.println("\n=== MATRIZ DE ADYACENCIA ===");
        System.out.println("Dimensión: " + cantidadNodos + "x" + cantidadNodos);
        
        // Encabezados
        System.out.print("    ");
        for (int j = 0; j < cantidadNodos; j++) {
            System.out.print(String.format("%3d ", j));
        }
        System.out.println();

        // Datos
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.print(String.format("%3d ", i));
            for (int j = 0; j < cantidadNodos; j++) {
                System.out.print(String.format("%3d ", matrizAdyacencia[i][j]));
            }
            System.out.println();
        }
    }

    /**
     * Imprime la matriz de incidencia
     */
    public void imprimirMatrizIncidencia() {
        if (matrizIncidencia == null) {
            generarMatrizIncidencia();
        }

        System.out.println("\n=== MATRIZ DE INCIDENCIA ===");
        System.out.println("Dimensión: " + cantidadNodos + "x" + cantidadAristas);
        
        // Encabezados
        System.out.print("    ");
        for (int j = 0; j < cantidadAristas; j++) {
            System.out.print(String.format("%3d ", j));
        }
        System.out.println();

        // Datos
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.print(String.format("%3d ", i));
            for (int j = 0; j < cantidadAristas; j++) {
                System.out.print(String.format("%3d ", matrizIncidencia[i][j]));
            }
            System.out.println();
        }
    }

    public int[][] getMatrizAdyacencia() {
        if (matrizAdyacencia == null) {
            generarMatrizAdyacencia();
        }
        return matrizAdyacencia;
    }

    public int[][] getMatrizIncidencia() {
        if (matrizIncidencia == null) {
            generarMatrizIncidencia();
        }
        return matrizIncidencia;
    }

    public Map<Integer, Integer> getMapaIdAIndice() {
        return new HashMap<>(mapaIdAIndice);
    }
}
