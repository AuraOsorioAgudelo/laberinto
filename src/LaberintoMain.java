import algorithms.CaminoMasCorto;
import algorithms.Recorridos;
import grafo.Grafo;
import models.Nodo;
import utils.LaberintoParser;
import utils.MatrizesGrafo;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.awt.HeadlessException;
import java.awt.GraphicsEnvironment;

/**
 * Clase principal del sistema de resolución de laberintos.
 * 
 * PROPÓSITO:
 * Proporciona una interfaz interactiva (menú en consola) para:
 * 1. Cargar laberintos desde archivos de texto
 * 2. Visualizar información del grafo construido
 * 3. Encontrar el camino más corto de A a B (usando BFS)
 * 4. Ejecutar diferentes tipos de recorridos del grafo
 * 5. Visualizar matrices de adyacencia e incidencia
 * 
 * FLUJO PRINCIPAL:
 * 1. El programa muestra un menú con opciones
 * 2. El usuario selecciona una opción
 * 3. Se ejecuta la acción correspondiente
 * 4. Se vuelve al menú (bucle)
 * 
 * DEPENDENCIAS:
 * - LaberintoParser: Carga y valida laberintos
 * - Grafo: Estructura de datos del laberinto
 * - CaminoMasCorto: Busca el camino más corto (BFS)
 * - Recorridos: Ejecuta distintos tipos de recorridos
 * - MatrizesGrafo: Genera y muestra matrices
 */
public class LaberintoMain {
    // Parser para leer archivos de laberinto
    private LaberintoParser parser;
    // Grafo construido a partir del archivo de laberinto
    private Grafo grafo;
    // Algoritmo para buscar el camino más corto
    private CaminoMasCorto caminoMasCorto;
    // Recorridos del grafo (DFS, BFS, etc.)
    private Recorridos recorridos;
    // Generador de matrices del grafo
    private MatrizesGrafo matrices;

    /**
     * Punto de entrada del programa.
     * Crea una instancia de LaberintoMain y comienza la ejecución.
     * 
     * @param args argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {
        LaberintoMain app = new LaberintoMain();
        app.ejecutar();
    }

    /**
     * Método principal que ejecuta el bucle de menú.
     * 
     * FLUJO:
     * 1. Mostrar menú con opciones
     * 2. Leer opción del usuario
     * 3. Ejecutar la acción correspondiente
     * 4. Repetir hasta que el usuario seleccione salir
     * 
     * OPCIONES DEL MENÚ:
     * 1. Cargar archivo de laberinto
     * 2. Mostrar información del grafo
     * 3. Encontrar camino más corto (A -> B)
     * 4. Ejecutar recorridos del grafo
     * 5. Mostrar matrices (adyacencia e incidencia)
     * 6. Salir del programa
     */
    private void ejecutar() {
        // Scanner para leer entrada del usuario
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            // Mostrar el menú
            mostrarMenu();
            // Pedir al usuario que ingrese una opción
            System.out.print("\nSeleccione una opción: ");
            String opcion = scanner.nextLine().trim();

            // Procesar la opción seleccionada
            switch (opcion) {
                case "1":
                    // Cargar un archivo de laberinto
                    cargarArchivo(scanner);
                    break;
                case "2":
                    // Mostrar información del grafo cargado
                    if (grafo != null) {
                        mostrarInfoGrafo();
                    } else {
                        System.out.println("Primero debe cargar un archivo de laberinto.");
                    }
                    break;
                case "3":
                    // Encontrar el camino más corto de A a B
                    if (grafo != null) {
                        encontrarCaminoMasCorto();
                    } else {
                        System.out.println("Primero debe cargar un archivo de laberinto.");
                    }
                    break;
                case "4":
                    // Ejecutar todos los tipos de recorridos
                    if (grafo != null) {
                        ejecutarRecorridos();
                    } else {
                        System.out.println("Primero debe cargar un archivo de laberinto.");
                    }
                    break;
                case "5":
                    // Mostrar las matrices de adyacencia e incidencia
                    if (grafo != null) {
                        mostrarMatrices();
                    } else {
                        System.out.println("Primero debe cargar un archivo de laberinto.");
                    }
                    break;
                case "6":
                    // Salir del programa
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    // Opción no válida
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }

        // Cerrar el scanner
        scanner.close();
    }

    /**
     * Muestra el menú principal en la consola.
     * Presenta las opciones disponibles para el usuario.
     */
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

    /**
     * Carga un archivo de laberinto desde el sistema de archivos.
     * 
     * PROCESO:
     * 1. Intentar abrir un selector de archivos gráfico (JFileChooser)
     * 2. Si el entorno no es gráfico (headless), pedir la ruta por consola
     * 3. Leer el archivo usando LaberintoParser
     * 4. Construir el grafo
     * 5. Inicializar los algoritmos (CaminoMasCorto, Recorridos, MatrizesGrafo)
     * 6. Mostrar información del laberinto cargado
     * 
     * MANEJO DE ERRORES:
     * - Si el usuario cancela el selector gráfico, se usa fallback por consola
     * - Si hay error al leer el archivo, se muestra el error
     * 
     * @param scanner scanner para leer entrada del usuario si es necesario
     */
    private void cargarArchivo(Scanner scanner) {
        // Array para almacenar la ruta seleccionada (usado en runnable)
        final String[] rutaSeleccionada = new String[1];

        // Runnable que abre el selector de archivos
        Runnable abrirSelector = () -> {
            try {
                // Crear un frame temporal para asegurar que el diálogo se muestre correctamente
                JFrame frame = new JFrame();
                frame.setAlwaysOnTop(true);
                frame.setUndecorated(true);
                frame.setSize(0, 0);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Crear y mostrar el selector de archivos
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Seleccione el archivo del laberinto");
                System.out.println("[DEBUG] Mostrando JFileChooser...");
                
                // Mostrar el diálogo y capturar la selección
                int resultado = chooser.showOpenDialog(frame);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    // Usuario seleccionó un archivo
                    File file = chooser.getSelectedFile();
                    rutaSeleccionada[0] = file.getAbsolutePath();
                    System.out.println("[DEBUG] Ruta seleccionada por GUI: " + rutaSeleccionada[0]);
                } else {
                    // Usuario canceló la selección
                    rutaSeleccionada[0] = null;
                    System.out.println("[DEBUG] Usuario canceló el selector GUI o no se seleccionó archivo.");
                }

                // Limpiar el frame temporal
                frame.dispose();
            } catch (Exception ex) {
                System.out.println("[DEBUG] Excepción en abrirSelector: " + ex.getMessage());
                ex.printStackTrace();
                rutaSeleccionada[0] = null;
            }
        };

        // Intentar abrir selector gráfico si el entorno lo permite
        System.out.println("[DEBUG] GraphicsEnvironment.isHeadless(): " + GraphicsEnvironment.isHeadless());
        try {
            if (!GraphicsEnvironment.isHeadless()) {
                // Entorno gráfico disponible
                System.out.println("[DEBUG] Intentando abrir selector gráfico...");
                if (SwingUtilities.isEventDispatchThread()) {
                    // Ya estamos en el Event Dispatch Thread
                    abrirSelector.run();
                } else {
                    // Ejecutar en el Event Dispatch Thread
                    SwingUtilities.invokeAndWait(abrirSelector);
                }
            } else {
                // Entorno sin gráficos
                System.out.println("[DEBUG] Entorno headless detectado.");
                rutaSeleccionada[0] = null;
            }
        } catch (HeadlessException he) {
            // Excepción de headless
            System.out.println("Entorno sin soporte gráfico. Usando fallback por consola.");
            he.printStackTrace();
            rutaSeleccionada[0] = null;
        } catch (Exception e) {
            // Otra excepción
            System.out.println("✗ Error al abrir el selector de archivos: " + e.getMessage());
            e.printStackTrace();
            rutaSeleccionada[0] = null;
        }

        String ruta = rutaSeleccionada[0];
        
        // Si el selector gráfico falló o fue cancelado, pedir ruta por consola
        if (ruta == null) {
            System.out.print("Ingrese la ruta del archivo del laberinto (fallback): ");
            ruta = scanner.nextLine().trim();
            if (ruta.isEmpty()) {
                System.out.println("No se proporcionó ninguna ruta. Operación cancelada.");
                return;
            }
        }

        // Intentar cargar el archivo
        try {
            // Crear parser y leer archivo
            parser = new LaberintoParser();
            parser.leerArchivo(ruta);
            
            // Construir el grafo desde el laberinto
            grafo = parser.construirGrafo();
            
            // Inicializar los algoritmos con el grafo cargado
            caminoMasCorto = new CaminoMasCorto(grafo);
            recorridos = new Recorridos(grafo);
            matrices = new MatrizesGrafo(grafo);

            // Mostrar confirmación y detalles del laberinto
            System.out.println("\n✓ Archivo cargado exitosamente.");
            parser.imprimirMapa();
            System.out.println("\nGrafo construido:");
            System.out.println("- Nodos: " + grafo.getCantidadNodos());
            System.out.println("- Aristas: " + grafo.getCantidadAristas());
            System.out.println("- Punto de inicio (A): " + grafo.getNodoA());
            System.out.println("- Punto de fin (B): " + grafo.getNodoB());

        } catch (IOException e) {
            // Error al leer o procesar el archivo
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    /**
     * Muestra información detallada del grafo cargado.
     * Incluye:
     * - Cantidad de nodos y aristas
     * - Posiciones de los puntos A y B
     * - Visualización del mapa
     * 
     * Solo se puede ejecutar si hay un laberinto cargado.
     */
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

    /**
     * Encuentra y muestra el camino más corto de A a B.
     * 
     * PROCESO:
     * 1. Obtener los IDs de los nodos A y B
     * 2. Usar el algoritmo BFS para encontrar el camino más corto
     * 3. Imprimir el camino
     * 4. Mostrar el mapa con el camino marcado
     * 
     * ALGORITMO USADO: BFS (Breadth-First Search)
     * Garantiza encontrar el camino con el mínimo número de pasos.
     */
    private void encontrarCaminoMasCorto() {
        // Obtener los nodos de inicio y fin
        Nodo nodoA = grafo.getNodoA();
        Nodo nodoB = grafo.getNodoB();

        // Buscar el camino más corto usando BFS
        List<Integer> camino = caminoMasCorto.encontrarCaminoMasCorto(nodoA.getId(), nodoB.getId());

        // Mostrar el camino
        caminoMasCorto.imprimirCamino(camino);
        // Mostrar el mapa con el camino marcado
        caminoMasCorto.imprimirMapaConCamino(parser.getMapa(), camino);
    }

    /**
     * Ejecuta todos los tipos de recorridos del grafo.
     * 
     * RECORRIDOS EJECUTADOS:
     * 1. DFS Preorden - Procesa nodo antes de vecinos
     * 2. DFS Inorden - Procesa nodo entre vecinos
     * 3. DFS Postorden - Procesa nodo después de vecinos
     * 4. BFS - Búsqueda en amplitud (por niveles)
     * 5. Greedy Best-First Search - Búsqueda heurística hacia B
     * 
     * Cada recorrido comienza desde el nodo A (punto de inicio).
     */
    private void ejecutarRecorridos() {
        // Obtener nodos de inicio y fin
        Nodo nodoA = grafo.getNodoA();
        Nodo nodoB = grafo.getNodoB();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("EJECUTANDO RECORRIDOS DEL GRAFO");
        System.out.println("=".repeat(50));

        // DFS Preorden
        // Procesa cada nodo ANTES de explorar sus vecinos
        List<Integer> dfsPreorden = recorridos.dfsPreorden(nodoA.getId());
        recorridos.imprimirRecorrido("DFS - PREORDEN", dfsPreorden);

        // DFS Inorden
        // Procesa cada nodo EN MEDIO de explorar sus vecinos
        List<Integer> dfsInorden = recorridos.dfsInorden(nodoA.getId());
        recorridos.imprimirRecorrido("DFS - INORDEN", dfsInorden);

        // DFS Postorden
        // Procesa cada nodo DESPUÉS de explorar sus vecinos
        List<Integer> dfsPostorden = recorridos.dfsPostorden(nodoA.getId());
        recorridos.imprimirRecorrido("DFS - POSTORDEN", dfsPostorden);

        // BFS (Breadth-First Search)
        // Explora el grafo por niveles (distancia desde A)
        List<Integer> bfs = recorridos.bfs(nodoA.getId());
        recorridos.imprimirRecorrido("BFS (AMPLITUD)", bfs);

        // Greedy Best-First Search
        // Búsqueda heurística que intenta alcanzar B más rápido
        List<Integer> greedy = recorridos.greedyBestFirstSearch(nodoA.getId(), nodoB.getId());
        recorridos.imprimirRecorrido("GREEDY BEST-FIRST SEARCH (HEURÍSTICO)", greedy);
    }

    /**
     * Muestra las matrices de adyacencia e incidencia del grafo.
     * 
     * MATRICES MOSTRADAS:
     * 1. Matriz de Adyacencia (V x V)
     *    - Muestra qué nodos están conectados
     *    - Entrada [i,j] = 1 si hay arista entre i y j
     * 
     * 2. Matriz de Incidencia (V x E)
     *    - Muestra qué nodos son incidentes a qué aristas
     *    - Entrada [i,j] = 1 si nodo i está conectado a arista j
     * 
     * DISPLAY:
     * Si el entorno permite gráficos, muestra las matrices en una ventana.
     * Si no, las muestra en la consola.
     */
    private void mostrarMatrices() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("MATRICES DEL GRAFO");
        System.out.println("=".repeat(50));

        // Intentar mostrar en ventana gráfica si es posible
        if (!GraphicsEnvironment.isHeadless()) {
            try {
                // Redirigir System.out para capturar la salida de las matrices
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream oldOut = System.out;
                System.setOut(ps);
                try {
                    // Generar las matrices
                    matrices.imprimirMatrizAdyacencia();
                    matrices.imprimirMatrizIncidencia();
                } finally {
                    // Restaurar System.out
                    System.out.flush();
                    System.setOut(oldOut);
                    ps.close();
                }

                // Obtener el contenido capturado
                String contenido = baos.toString();
                final String textoMostrar = contenido;

                // Mostrar en una ventana Swing
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Matrices del Grafo");
                    JTextArea area = new JTextArea(textoMostrar);
                    area.setEditable(false);
                    JScrollPane scroll = new JScrollPane(area);
                    frame.getContentPane().add(scroll);
                    frame.setSize(800, 600);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                });

            } catch (Exception e) {
                System.out.println("✗ Error mostrando matrices en GUI: " + e.getMessage());
                e.printStackTrace();
                // Fallback a consola
                matrices.imprimirMatrizAdyacencia();
                matrices.imprimirMatrizIncidencia();
            }
        } else {
            // Entorno headless - mostrar en consola
            matrices.imprimirMatrizAdyacencia();
            matrices.imprimirMatrizIncidencia();
        }
    }
}
