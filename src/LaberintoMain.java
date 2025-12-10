import algorithms.CaminoMasCorto;
import algorithms.Recorridos;
import grafo.Grafo;
import models.Nodo;
import utils.LaberintoParser;
import utils.MatrizesGrafo;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal del sistema de resolución de laberintos.
 */
public class LaberintoMain {
    private LaberintoParser parser;
    private Grafo grafo;
    private CaminoMasCorto caminoMasCorto;
    private Recorridos recorridos;
    private MatrizesGrafo matrices;

    public static void main(String[] args) {
        LaberintoMain app = new LaberintoMain();
        app.ejecutar();
    }

    private void ejecutar() {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            System.out.print("\nSeleccione una opción: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    cargarArchivo(scanner);
                    break;
                case "2":
                    if (grafo != null) {
                        mostrarInfoGrafo();
                    } else {
                        System.out.println("Primero debe cargar un archivo de laberinto.");
                    }
                    break;
                case "3":
                    if (grafo != null) {
                        encontrarCaminoMasCorto();
                    } else {
                        System.out.println("Primero debe cargar un archivo de laberinto.");
                    }
                    break;
                case "4":
                    if (grafo != null) {
                        ejecutarRecorridos();
                    } else {
                        System.out.println("Primero debe cargar un archivo de laberinto.");
                    }
                    break;
                case "5":
                    if (grafo != null) {
                        mostrarMatrices();
                    } else {
                        System.out.println("Primero debe cargar un archivo de laberinto.");
                    }
                    break;
                case "6":
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }

        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("       SISTEMA DE RESOLUCIÓN DE LABERINTOS");
        System.out.println("=".repeat(50));
        System.out.println("1. Cargar archivo de laberinto");
        System.out.println("2. Mostrar información del grafo");
        System.out.println("3. Encontrar camino más corto (A -> B)");
        System.out.println("4. Ejecutar recorridos del grafo");
        System.out.println("5. Mostrar matrices (adyacencia e incidencia)");
        System.out.println("6. Salir");
        System.out.println("=".repeat(50));
    }

    private void cargarArchivo(Scanner scanner) {
        System.out.print("Ingrese la ruta del archivo del laberinto: ");
        String ruta = scanner.nextLine().trim();

        try {
            parser = new LaberintoParser();
            parser.leerArchivo(ruta);
            grafo = parser.construirGrafo();
            caminoMasCorto = new CaminoMasCorto(grafo);
            recorridos = new Recorridos(grafo);
            matrices = new MatrizesGrafo(grafo);

            System.out.println("\n✓ Archivo cargado exitosamente.");
            parser.imprimirMapa();
            System.out.println("\nGrafo construido:");
            System.out.println("- Nodos: " + grafo.getCantidadNodos());
            System.out.println("- Aristas: " + grafo.getCantidadAristas());
            System.out.println("- Punto de inicio (A): " + grafo.getNodoA());
            System.out.println("- Punto de fin (B): " + grafo.getNodoB());

        } catch (IOException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void mostrarInfoGrafo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("INFORMACIÓN DEL GRAFO");
        System.out.println("=".repeat(50));

        Nodo nodoA = grafo.getNodoA();
        Nodo nodoB = grafo.getNodoB();

        System.out.println("Cantidad de nodos: " + grafo.getCantidadNodos());
        System.out.println("Cantidad de aristas: " + grafo.getCantidadAristas());
        System.out.println("\nPunto de inicio (A): Posición (" + nodoA.getX() + ", " + nodoA.getY() + ")");
        System.out.println("Punto de fin (B): Posición (" + nodoB.getX() + ", " + nodoB.getY() + ")");

        parser.imprimirMapa();
    }

    private void encontrarCaminoMasCorto() {
        Nodo nodoA = grafo.getNodoA();
        Nodo nodoB = grafo.getNodoB();

        List<Integer> camino = caminoMasCorto.encontrarCaminoMasCorto(nodoA.getId(), nodoB.getId());

        caminoMasCorto.imprimirCamino(camino);
        caminoMasCorto.imprimirMapaConCamino(parser.getMapa(), camino);
    }

    private void ejecutarRecorridos() {
        Nodo nodoA = grafo.getNodoA();
        Nodo nodoB = grafo.getNodoB();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("EJECUTANDO RECORRIDOS DEL GRAFO");
        System.out.println("=".repeat(50));

        // DFS Preorden
        List<Integer> dfsPreorden = recorridos.dfsPreorden(nodoA.getId());
        recorridos.imprimirRecorrido("DFS - PREORDEN", dfsPreorden);

        // DFS Inorden
        List<Integer> dfsInorden = recorridos.dfsInorden(nodoA.getId());
        recorridos.imprimirRecorrido("DFS - INORDEN", dfsInorden);

        // DFS Postorden
        List<Integer> dfsPostorden = recorridos.dfsPostorden(nodoA.getId());
        recorridos.imprimirRecorrido("DFS - POSTORDEN", dfsPostorden);

        // BFS
        List<Integer> bfs = recorridos.bfs(nodoA.getId());
        recorridos.imprimirRecorrido("BFS (AMPLITUD)", bfs);

        // Greedy Best-First Search
        List<Integer> greedy = recorridos.greedyBestFirstSearch(nodoA.getId(), nodoB.getId());
        recorridos.imprimirRecorrido("GREEDY BEST-FIRST SEARCH (HEURÍSTICO)", greedy);
    }

    private void mostrarMatrices() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("MATRICES DEL GRAFO");
        System.out.println("=".repeat(50));

        matrices.imprimirMatrizAdyacencia();
        matrices.imprimirMatrizIncidencia();
    }
}
