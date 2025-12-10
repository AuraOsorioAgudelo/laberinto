package utils;

import models.Nodo;
import grafo.Grafo;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Clase responsable de leer archivos de laberinto y construir el grafo correspondiente.
 * 
 * PROCESO DE CARGA:
 * 1. leerArchivo(): Lee el archivo de texto y crea una matriz 2D con el mapa
 * 2. validarMapa(): Verifica que existan los puntos A (inicio) y B (fin)
 * 3. construirGrafo(): Convierte el mapa 2D en una estructura de grafo
 *    - Cada celda transitable ('A', 'B' o ' ') se convierte en un nodo
 *    - Las celdas adyacentes se conectan con aristas
 *    - Las paredes ('*') se ignoran
 * 
 * FORMATO DE ENTRADA:
 * El archivo debe ser un laberinto textual donde:
 * - '*' representa una pared (no transitable)
 * - ' ' representa un espacio transitable
 * - 'A' representa el punto de inicio
 * - 'B' representa el punto de fin
 */
public class LaberintoParser {
    // Matriz 2D que almacena el mapa del laberinto (caracteres del archivo)
    private char[][] mapa;
    // Mapeo que relaciona posiciones (x,y) con IDs de nodos para búsquedas rápidas
    // Clave: "x,y" (string concatenado), Valor: ID del nodo
    private Map<String, Integer> posicionANodo;
    // Referencia al grafo construido a partir del mapa
    private Grafo grafo;
    // Dimensiones del mapa
    private int filas;
    private int columnas;
    // Contador para asignar IDs únicos a los nodos
    private int contadorNodos = 0;

    /**
     * Lee un archivo de texto que contiene el laberinto y lo almacena en memoria.
     * El archivo se convierte en una matriz 2D de caracteres.
     * Se rellena con espacios si las líneas tienen longitudes diferentes.
     * 
     * PROCESO:
     * 1. Lee todas las líneas del archivo
     * 2. Determina la máxima longitud (ancho del mapa)
     * 3. Crea una matriz rectangular de caracteres
     * 4. Llena la matriz con los datos del archivo
     * 5. Valida que contenga los puntos A y B
     * 
     * @param rutaArchivo la ruta completa al archivo del laberinto
     * @throws IOException si hay error al leer el archivo o validar el contenido
     */
    public void leerArchivo(String rutaArchivo) throws IOException {
        try {
            // Leer todas las líneas del archivo de una vez
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));
            
            // Encontrar la máxima longitud para ajustar el ancho de la matriz
            // Esto es importante si el laberinto tiene líneas de diferentes longitudes
            int maxLongitud = 0;
            for (String linea : lineas) {
                maxLongitud = Math.max(maxLongitud, linea.length());
            }

            // Establecer dimensiones del mapa
            this.filas = lineas.size();
            this.columnas = maxLongitud;
            this.mapa = new char[filas][columnas];

            // Inicializar toda la matriz con espacios (por defecto)
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    mapa[i][j] = ' ';
                }
            }

            // Llenar la matriz con los datos del archivo
            // Las líneas más cortas se rellenan con espacios automáticamente
            for (int i = 0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                for (int j = 0; j < linea.length(); j++) {
                    mapa[i][j] = linea.charAt(j);
                }
            }

            // Validar que el mapa sea válido (tiene A y B)
            validarMapa();
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Valida que el mapa del laberinto sea válido.
     * Verifica que exista exactamente un punto A (inicio) y un punto B (fin).
     * 
     * @throws IOException si falta A o B en el laberinto
     */
    private void validarMapa() throws IOException {
        boolean tieneA = false;
        boolean tieneB = false;

        // Escanear toda la matriz buscando 'A' y 'B'
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (mapa[i][j] == 'A') tieneA = true;
                if (mapa[i][j] == 'B') tieneB = true;
            }
        }

        // Si falta alguno, lanzar una excepción
        if (!tieneA || !tieneB) {
            throw new IOException("El laberinto debe contener un punto A (inicio) y un punto B (fin)");
        }
    }

    /**
     * Construye un grafo a partir del mapa del laberinto.
     * 
     * PROCESO EN DOS PASOS:
     * 
     * PASO 1 - Crear nodos:
     * - Itera por cada celda del mapa
     * - Si la celda NO es una pared ('*'), crea un nodo
     * - Asigna un ID único y almacena la relación posición -> ID
     * 
     * PASO 2 - Conectar nodos (crear aristas):
     * - Para cada nodo, busca sus 4 vecinos (arriba, abajo, izq, der)
     * - Si el vecino es transitable (no es '*'), crea una arista
     * - Esto forma la red del laberinto
     * 
     * @return el grafo construido desde el mapa
     */
    public Grafo construirGrafo() {
        // Inicializar grafo vacío
        this.grafo = new Grafo();
        this.posicionANodo = new HashMap<>();

        // ===== PASO 1: Crear nodos para todas las celdas transitables =====
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                char celda = mapa[i][j];
                // Crear nodo solo si NO es una pared
                if (celda != '*') {
                    // Crear nodo con el contador actual como ID
                    // El tipo será el carácter de la celda ('A', 'B', o ' ')
                    Nodo nodo = new Nodo(contadorNodos, i, j, celda);
                    grafo.agregarNodo(nodo);
                    // Guardar la relación posición -> ID para búsquedas posteriores
                    posicionANodo.put(i + "," + j, contadorNodos);
                    contadorNodos++;
                }
            }
        }

        // ===== PASO 2: Conectar nodos adyacentes =====
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                char celda = mapa[i][j];
                // Solo procesar si NO es una pared
                if (celda != '*') {
                    Integer idActual = posicionANodo.get(i + "," + j);
                    
                    // Definir las 4 direcciones: Arriba, Abajo, Izquierda, Derecha
                    // Formato: {nuevaFila, nuevaColumna}
                    int[][] direcciones = {
                        {i - 1, j}, // Arriba
                        {i + 1, j}, // Abajo
                        {i, j - 1}, // Izquierda
                        {i, j + 1}  // Derecha
                    };

                    // Revisar cada dirección
                    for (int[] dir : direcciones) {
                        int ni = dir[0];
                        int nj = dir[1];
                        // Verificar que el vecino esté dentro de límites y NO sea una pared
                        if (ni >= 0 && ni < filas && nj >= 0 && nj < columnas && 
                            mapa[ni][nj] != '*') {
                            Integer idVecino = posicionANodo.get(ni + "," + nj);
                            // Crear la arista bidireccional entre nodos
                            grafo.agregarArista(idActual, idVecino);
                        }
                    }
                }
            }
        }

        return grafo;
    }

    /**
     * Obtiene la matriz 2D del mapa del laberinto.
     * @return el mapa como matriz de caracteres
     */
    public char[][] getMapa() {
        return mapa;
    }

    /**
     * Obtiene la cantidad de filas del mapa.
     * @return número de filas
     */
    public int getFilas() {
        return filas;
    }

    /**
     * Obtiene la cantidad de columnas del mapa.
     * @return número de columnas
     */
    public int getColumnas() {
        return columnas;
    }

    /**
     * Obtiene el grafo construido.
     * @return el grafo del laberinto
     */
    public Grafo getGrafo() {
        return grafo;
    }

    /**
     * Obtiene el mapeo de posiciones a IDs de nodos.
     * Útil para búsquedas rápidas de nodos por coordenadas.
     * 
     * @return mapa de "x,y" -> ID del nodo
     */
    public Map<String, Integer> getPosicionANodo() {
        return posicionANodo;
    }

    /**
     * Imprime el mapa del laberinto en consola de forma legible.
     * Muestra el laberinto tal como fue leído del archivo.
     * Utiliza caracteres especiales:
     * - '*' para paredes
     * - ' ' para espacios transitables
     * - 'A' para punto de inicio
     * - 'B' para punto de fin
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
