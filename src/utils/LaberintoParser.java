package utils;

import models.Nodo;
import grafo.Grafo;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Clase responsable de leer el archivo del laberinto y construir el grafo.
 */
public class LaberintoParser {
    private char[][] mapa;
    private Map<String, Integer> posicionANodo; // Mapea "x,y" al ID del nodo
    private Grafo grafo;
    private int filas;
    private int columnas;
    private int contadorNodos = 0;

    /**
     * Lee el archivo del laberinto y retorna el mapa de caracteres
     */
    public void leerArchivo(String rutaArchivo) throws IOException {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));
            
            // Encontrar la máxima longitud para ajustar columnas
            int maxLongitud = 0;
            for (String linea : lineas) {
                maxLongitud = Math.max(maxLongitud, linea.length());
            }

            this.filas = lineas.size();
            this.columnas = maxLongitud;
            this.mapa = new char[filas][columnas];

            // Inicializar con espacios y llenar con datos
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    mapa[i][j] = ' ';
                }
            }

            // Llenar con datos del archivo
            for (int i = 0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                for (int j = 0; j < linea.length(); j++) {
                    mapa[i][j] = linea.charAt(j);
                }
            }

            validarMapa();
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Valida que el mapa contenga los puntos A y B
     */
    private void validarMapa() throws IOException {
        boolean tieneA = false;
        boolean tieneB = false;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (mapa[i][j] == 'A') tieneA = true;
                if (mapa[i][j] == 'B') tieneB = true;
            }
        }

        if (!tieneA || !tieneB) {
            throw new IOException("El laberinto debe contener un punto A (inicio) y un punto B (fin)");
        }
    }

    /**
     * Construye el grafo a partir del mapa
     */
    public Grafo construirGrafo() {
        this.grafo = new Grafo();
        this.posicionANodo = new HashMap<>();

        // Paso 1: Crear nodos para todas las celdas transitables
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                char celda = mapa[i][j];
                if (celda != '*') { // Transitable
                    Nodo nodo = new Nodo(contadorNodos, i, j, celda);
                    grafo.agregarNodo(nodo);
                    posicionANodo.put(i + "," + j, contadorNodos);
                    contadorNodos++;
                }
            }
        }

        // Paso 2: Conectar nodos adyacentes (arriba, abajo, izquierda, derecha)
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                char celda = mapa[i][j];
                if (celda != '*') {
                    Integer idActual = posicionANodo.get(i + "," + j);
                    
                    // Verificar vecinos
                    int[][] direcciones = {
                        {i - 1, j}, // Arriba
                        {i + 1, j}, // Abajo
                        {i, j - 1}, // Izquierda
                        {i, j + 1}  // Derecha
                    };

                    for (int[] dir : direcciones) {
                        int ni = dir[0];
                        int nj = dir[1];
                        if (ni >= 0 && ni < filas && nj >= 0 && nj < columnas && 
                            mapa[ni][nj] != '*') {
                            Integer idVecino = posicionANodo.get(ni + "," + nj);
                            grafo.agregarArista(idActual, idVecino);
                        }
                    }
                }
            }
        }

        return grafo;
    }

    public char[][] getMapa() {
        return mapa;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public Grafo getGrafo() {
        return grafo;
    }

    public Map<String, Integer> getPosicionANodo() {
        return posicionANodo;
    }

    /**
     * Imprime el mapa de forma legible
     */
    public void imprimirMapa() {
        System.out.println("\n=== MAPA DEL LABERINTO ===");
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(mapa[i][j]);
            }
            System.out.println();
        }
    }
}
