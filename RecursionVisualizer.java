import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class RecursionVisualizer extends JFrame {
    private JTabbedPane tabbedPane;
    
    // Componentes para Fibonacci
    private JPanel fibonacciPanel;
    private JTextField fibInput;
    private JTextArea fibOutput;
    private JTable fibTable;
    private DefaultTableModel fibTableModel;
    
    // Componentes para Combinatoria
    private JPanel combinatoriaPanel;
    private JTextField combMInput, combNInput;
    private JTextArea combOutput;
    private JTable combTable;
    private DefaultTableModel combTableModel;
    
    // Componentes para Ackermann
    private JPanel ackermannPanel;
    private JTextField ackMInput, ackNInput;
    private JTextArea ackOutput;
    private JTable ackTable;
    private DefaultTableModel ackTableModel;
    
    // Componentes para Recursion Mutua
    private JPanel recursionMutuaPanel;
    private JTextField rmInput;
    private JTextArea rmOutput;
    
    // Mapas para memorizacion
    private Map<Integer, Integer> fibMemo = new HashMap<>();
    private Map<String, Integer> combMemo = new HashMap<>();
    private Map<String, Integer> ackMemo = new HashMap<>();
    
    // Contadores para llamadas recursivas
    private int fibCallCount = 0;
    private int combCallCount = 0;
    private int ackCallCount = 0;
    
    public RecursionVisualizer() {
        setTitle("Visualizador de Eliminacion de Recursividad Redundante");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Panel de Fibonacci
        fibonacciPanel = createFibonacciPanel();
        tabbedPane.addTab("Fibonacci", fibonacciPanel);
        
        // Panel de Combinatoria
        combinatoriaPanel = createCombinatoriaPanel();
        tabbedPane.addTab("Combinatoria", combinatoriaPanel);
        
        // Panel de Ackermann
        ackermannPanel = createAckermannPanel();
        tabbedPane.addTab("Ackermann", ackermannPanel);
        
        // Panel de Recursion Mutua
        recursionMutuaPanel = createRecursionMutuaPanel();
        tabbedPane.addTab("Recursion Mutua", recursionMutuaPanel);
    }
    
    private JPanel createFibonacciPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de entrada
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("n:"));
        fibInput = new JTextField(10);
        inputPanel.add(fibInput);
        
        JButton fibRecursiveBtn = new JButton("Recursivo");
        JButton fibMemoBtn = new JButton("Con Memorizacion");
        JButton fibTabulationBtn = new JButton("Tabulacion");
        JButton fibMinMemoryBtn = new JButton("Minima Memoria");
        
        inputPanel.add(fibRecursiveBtn);
        inputPanel.add(fibMemoBtn);
        inputPanel.add(fibTabulationBtn);
        inputPanel.add(fibMinMemoryBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        // Panel de salida
        fibOutput = new JTextArea(10, 50);
        fibOutput.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(fibOutput);
        panel.add(outputScroll, BorderLayout.CENTER);
        
        // Tabla para visualizar resultados
        String[] columnNames = {"Metodo", "n", "Resultado", "Llamadas", "Tiempo (ns)"};
        fibTableModel = new DefaultTableModel(columnNames, 0);
        fibTable = new JTable(fibTableModel);
        JScrollPane tableScroll = new JScrollPane(fibTable);
        panel.add(tableScroll, BorderLayout.SOUTH);
        
        // Action Listeners
        fibRecursiveBtn.addActionListener(e -> runFibonacciRecursive());
        fibMemoBtn.addActionListener(e -> runFibonacciMemoization());
        fibTabulationBtn.addActionListener(e -> runFibonacciTabulation());
        fibMinMemoryBtn.addActionListener(e -> runFibonacciMinMemory());
        
        return panel;
    }
    
    private JPanel createCombinatoriaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de entrada
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("m:"));
        combMInput = new JTextField(5);
        inputPanel.add(combMInput);
        
        inputPanel.add(new JLabel("n:"));
        combNInput = new JTextField(5);
        inputPanel.add(combNInput);
        
        JButton combRecursiveBtn = new JButton("Recursivo");
        JButton combMemoBtn = new JButton("Con Memorizacion");
        JButton combTabulationBtn = new JButton("Tabulacion");
        JButton combMinMemoryBtn = new JButton("Minima Memoria");
        
        inputPanel.add(combRecursiveBtn);
        inputPanel.add(combMemoBtn);
        inputPanel.add(combTabulationBtn);
        inputPanel.add(combMinMemoryBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        // Panel de salida
        combOutput = new JTextArea(10, 50);
        combOutput.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(combOutput);
        panel.add(outputScroll, BorderLayout.CENTER);
        
        // Tabla para visualizar resultados
        String[] columnNames = {"Metodo", "m", "n", "Resultado", "Llamadas", "Tiempo (ns)"};
        combTableModel = new DefaultTableModel(columnNames, 0);
        combTable = new JTable(combTableModel);
        JScrollPane tableScroll = new JScrollPane(combTable);
        panel.add(tableScroll, BorderLayout.SOUTH);
        
        // Action Listeners
        combRecursiveBtn.addActionListener(e -> runCombinatoriaRecursive());
        combMemoBtn.addActionListener(e -> runCombinatoriaMemoization());
        combTabulationBtn.addActionListener(e -> runCombinatoriaTabulation());
        combMinMemoryBtn.addActionListener(e -> runCombinatoriaMinMemory());
        
        return panel;
    }
    
    private JPanel createAckermannPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de entrada
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("m:"));
        ackMInput = new JTextField(3);
        inputPanel.add(ackMInput);
        
        inputPanel.add(new JLabel("n:"));
        ackNInput = new JTextField(3);
        inputPanel.add(ackNInput);
        
        JButton ackRecursiveBtn = new JButton("Recursivo");
        JButton ackMemoBtn = new JButton("Con Memorizacion");
        
        inputPanel.add(ackRecursiveBtn);
        inputPanel.add(ackMemoBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        // Panel de salida
        ackOutput = new JTextArea(10, 50);
        ackOutput.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(ackOutput);
        panel.add(outputScroll, BorderLayout.CENTER);
        
        // Tabla para visualizar resultados
        String[] columnNames = {"Metodo", "m", "n", "Resultado", "Llamadas", "Tiempo (ns)"};
        ackTableModel = new DefaultTableModel(columnNames, 0);
        ackTable = new JTable(ackTableModel);
        JScrollPane tableScroll = new JScrollPane(ackTable);
        panel.add(tableScroll, BorderLayout.SOUTH);
        
        // Action Listeners
        ackRecursiveBtn.addActionListener(e -> runAckermannRecursive());
        ackMemoBtn.addActionListener(e -> runAckermannMemoization());
        
        return panel;
    }
    
    private JPanel createRecursionMutuaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de entrada
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("n:"));
        rmInput = new JTextField(10);
        inputPanel.add(rmInput);
        
        JButton rmRecursiveBtn = new JButton("Calcular Recursion Mutua");
        inputPanel.add(rmRecursiveBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        // Panel de salida
        rmOutput = new JTextArea(15, 50);
        rmOutput.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(rmOutput);
        panel.add(outputScroll, BorderLayout.CENTER);
        
        // Action Listener
        rmRecursiveBtn.addActionListener(e -> runRecursionMutua());
        
        return panel;
    }
    
    private void setupLayout() {
        add(tabbedPane);
    }
    
    // ========== IMPLEMENTACIONES FIBONACCI ==========
    
    private void runFibonacciRecursive() {
        try {
            int n = Integer.parseInt(fibInput.getText());
            fibCallCount = 0;
            long startTime = System.nanoTime();
            int result = fibonacciRecursive(n);
            long endTime = System.nanoTime();
            
            fibOutput.setText("Fibonacci(" + n + ") = " + result + 
                             "\nLlamadas recursivas: " + fibCallCount +
                             "\nTiempo: " + (endTime - startTime) + " ns");
            
            fibTableModel.addRow(new Object[]{"Recursivo", n, result, fibCallCount, (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int fibonacciRecursive(int n) {
        fibCallCount++;
        if (n == 0 || n == 1) return 1;
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }
    
    private void runFibonacciMemoization() {
        try {
            int n = Integer.parseInt(fibInput.getText());
            fibMemo.clear();
            fibCallCount = 0;
            long startTime = System.nanoTime();
            int result = fibonacciMemoization(n);
            long endTime = System.nanoTime();
            
            fibOutput.setText("Fibonacci con Memorizacion(" + n + ") = " + result + 
                             "\nLlamadas recursivas: " + fibCallCount +
                             "\nTiempo: " + (endTime - startTime) + " ns");
            
            fibTableModel.addRow(new Object[]{"Memorizacion", n, result, fibCallCount, (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int fibonacciMemoization(int n) {
        fibCallCount++;
        if (n == 0 || n == 1) return 1;
        if (fibMemo.containsKey(n)) return fibMemo.get(n);
        
        int result = fibonacciMemoization(n - 1) + fibonacciMemoization(n - 2);
        fibMemo.put(n, result);
        return result;
    }
    
    private void runFibonacciTabulation() {
        try {
            int n = Integer.parseInt(fibInput.getText());
            long startTime = System.nanoTime();
            int result = fibonacciTabulation(n);
            long endTime = System.nanoTime();
            
            fibOutput.setText("Fibonacci con Tabulacion(" + n + ") = " + result + 
                             "\nTiempo: " + (endTime - startTime) + " ns");
            
            fibTableModel.addRow(new Object[]{"Tabulacion", n, result, "N/A", (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int fibonacciTabulation(int n) {
        if (n == 0 || n == 1) return 1;
        
        int[] fib = new int[n + 1];
        fib[0] = 1;
        fib[1] = 1;
        
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        
        return fib[n];
    }
    
    private void runFibonacciMinMemory() {
        try {
            int n = Integer.parseInt(fibInput.getText());
            long startTime = System.nanoTime();
            int result = fibonacciMinMemory(n);
            long endTime = System.nanoTime();
            
            fibOutput.setText("Fibonacci con Minima Memoria(" + n + ") = " + result + 
                             "\nTiempo: " + (endTime - startTime) + " ns");
            
            fibTableModel.addRow(new Object[]{"Minima Memoria", n, result, "N/A", (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int fibonacciMinMemory(int n) {
        if (n == 0 || n == 1) return 1;
        
        int prev1 = 1, prev2 = 1;
        int current = 0;
        
        for (int i = 2; i <= n; i++) {
            current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return current;
    }
    
    // ========== IMPLEMENTACIONES COMBINATORIA ==========
    
    private void runCombinatoriaRecursive() {
        try {
            int m = Integer.parseInt(combMInput.getText());
            int n = Integer.parseInt(combNInput.getText());
            combCallCount = 0;
            long startTime = System.nanoTime();
            int result = combinatoriaRecursive(m, n);
            long endTime = System.nanoTime();
            
            combOutput.setText("C(" + m + ", " + n + ") = " + result + 
                              "\nLlamadas recursivas: " + combCallCount +
                              "\nTiempo: " + (endTime - startTime) + " ns");
            
            combTableModel.addRow(new Object[]{"Recursivo", m, n, result, combCallCount, (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese numeros validos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int combinatoriaRecursive(int m, int n) {
        combCallCount++;
        if (n == 0 || m == n) return 1;
        return combinatoriaRecursive(m - 1, n) + combinatoriaRecursive(m - 1, n - 1);
    }
    
    private void runCombinatoriaMemoization() {
        try {
            int m = Integer.parseInt(combMInput.getText());
            int n = Integer.parseInt(combNInput.getText());
            combMemo.clear();
            combCallCount = 0;
            long startTime = System.nanoTime();
            int result = combinatoriaMemoization(m, n);
            long endTime = System.nanoTime();
            
            combOutput.setText("C con Memorizacion(" + m + ", " + n + ") = " + result + 
                              "\nLlamadas recursivas: " + combCallCount +
                              "\nTiempo: " + (endTime - startTime) + " ns");
            
            combTableModel.addRow(new Object[]{"Memorizacion", m, n, result, combCallCount, (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese numeros validos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int combinatoriaMemoization(int m, int n) {
        combCallCount++;
        if (n == 0 || m == n) return 1;
        
        String key = m + "," + n;
        if (combMemo.containsKey(key)) return combMemo.get(key);
        
        int result = combinatoriaMemoization(m - 1, n) + combinatoriaMemoization(m - 1, n - 1);
        combMemo.put(key, result);
        return result;
    }
    
    private void runCombinatoriaTabulation() {
        try {
            int m = Integer.parseInt(combMInput.getText());
            int n = Integer.parseInt(combNInput.getText());
            long startTime = System.nanoTime();
            int result = combinatoriaTabulation(m, n);
            long endTime = System.nanoTime();
            
            combOutput.setText("C con Tabulacion(" + m + ", " + n + ") = " + result + 
                              "\nTiempo: " + (endTime - startTime) + " ns");
            
            combTableModel.addRow(new Object[]{"Tabulacion", m, n, result, "N/A", (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese numeros validos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int combinatoriaTabulation(int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= Math.min(i, n); j++) {
                if (j == 0 || j == i) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
                }
            }
        }
        
        return dp[m][n];
    }
    
    private void runCombinatoriaMinMemory() {
        try {
            int m = Integer.parseInt(combMInput.getText());
            int n = Integer.parseInt(combNInput.getText());
            long startTime = System.nanoTime();
            int result = combinatoriaMinMemory(m, n);
            long endTime = System.nanoTime();
            
            combOutput.setText("C con Minima Memoria(" + m + ", " + n + ") = " + result + 
                              "\nTiempo: " + (endTime - startTime) + " ns");
            
            combTableModel.addRow(new Object[]{"Minima Memoria", m, n, result, "N/A", (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese numeros validos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int combinatoriaMinMemory(int m, int n) {
        if (n > m - n) {
            n = m - n;
        }
        
        int[] dp = new int[n + 1];
        dp[0] = 1;
        
        for (int i = 1; i <= m; i++) {
            for (int j = Math.min(i, n); j > 0; j--) {
                dp[j] = dp[j] + dp[j - 1];
            }
        }
        
        return dp[n];
    }
    
    // ========== IMPLEMENTACIONES ACKERMANN ==========
    
    private void runAckermannRecursive() {
        try {
            int m = Integer.parseInt(ackMInput.getText());
            int n = Integer.parseInt(ackNInput.getText());
            ackCallCount = 0;
            long startTime = System.nanoTime();
            int result = ackermannRecursive(m, n);
            long endTime = System.nanoTime();
            
            ackOutput.setText("Ackermann(" + m + ", " + n + ") = " + result + 
                             "\nLlamadas recursivas: " + ackCallCount +
                             "\nTiempo: " + (endTime - startTime) + " ns");
            
            ackTableModel.addRow(new Object[]{"Recursivo", m, n, result, ackCallCount, (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese numeros validos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (StackOverflowError ex) {
            JOptionPane.showMessageDialog(this, "Stack Overflow - Valores demasiado grandes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int ackermannRecursive(int m, int n) {
        ackCallCount++;
        if (m == 0) return n + 1;
        else if (n == 0) return ackermannRecursive(m - 1, 1);
        else return ackermannRecursive(m - 1, ackermannRecursive(m, n - 1));
    }
    
    private void runAckermannMemoization() {
        try {
            int m = Integer.parseInt(ackMInput.getText());
            int n = Integer.parseInt(ackNInput.getText());
            ackMemo.clear();
            ackCallCount = 0;
            long startTime = System.nanoTime();
            int result = ackermannMemoization(m, n);
            long endTime = System.nanoTime();
            
            ackOutput.setText("Ackermann con Memorizacion(" + m + ", " + n + ") = " + result + 
                             "\nLlamadas recursivas: " + ackCallCount +
                             "\nTiempo: " + (endTime - startTime) + " ns");
            
            ackTableModel.addRow(new Object[]{"Memorizacion", m, n, result, ackCallCount, (endTime - startTime)});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese numeros validos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (StackOverflowError ex) {
            JOptionPane.showMessageDialog(this, "Stack Overflow - Valores demasiado grandes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int ackermannMemoization(int m, int n) {
        ackCallCount++;
        String key = m + "," + n;
        if (ackMemo.containsKey(key)) return ackMemo.get(key);
        
        int result;
        if (m == 0) {
            result = n + 1;
        } else if (n == 0) {
            result = ackermannMemoization(m - 1, 1);
        } else {
            result = ackermannMemoization(m - 1, ackermannMemoization(m, n - 1));
        }
        
        ackMemo.put(key, result);
        return result;
    }
    
    // ========== IMPLEMENTACIoN RECURSIoN MUTUA ==========
    
    private void runRecursionMutua() {
        try {
            int n = Integer.parseInt(rmInput.getText());
            long startTime = System.nanoTime();
            int result = f(n);
            long endTime = System.nanoTime();
            
            rmOutput.setText("f(" + n + ") = " + result + 
                           "\nTiempo: " + (endTime - startTime) + " ns" +
                           "\n\nDescomposicion:" +
                           "\n  f(" + n + ") = bebes(" + n + ") + adultos(" + n + ")" +
                           "\n  bebes(" + n + ") = " + bebes(n) +
                           "\n  adultos(" + n + ") = " + adultos(n));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int f(int n) {
        return bebes(n) + adultos(n);
    }
    
    private int bebes(int n) {
        if (n == 0) return 1;
        else return adultos(n - 1);
    }
    
    private int adultos(int n) {
        if (n == 0) return 0;
        else return adultos(n - 1) + bebes(n - 1);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            RecursionVisualizer visualizer = new RecursionVisualizer();
            visualizer.setVisible(true);
        });
    }
}