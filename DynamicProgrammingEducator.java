import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
//by @ferpotenUCLM

/**
 * Dynamic Programming Educational Program - Single File Version
 * Universidad Rey Juan Carlos - Algoritmos Avanzados
 * All visualizers, components, and algorithms in one Java file
 */
public class DynamicProgrammingEducator extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JComboBox<String> algorithmSelector;
    private JLabel titleLabel;
    
    public DynamicProgrammingEducator() {
        super("Dynamic Programming Educational Tool @F.Potenciano");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        titleLabel = new JLabel("Dynamic Programming Educational Tool", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectorPanel.add(new JLabel("Select Algorithm: "));
        
        String[] algorithms = {
            "SCML - Subsecuencia Común Más Larga",
            "Alineamiento de Secuencias (Edit Distance)", 
            "Alineamiento Global (Needleman-Wunsch)",
            "Mochila 0/1",
            "Cambio de Monedas",
            "Multiplicación Encadenada de Matrices",
            "Estructura Secundaria del ARN",
            "Grafo Multietapa"
        };
        
        algorithmSelector = new JComboBox<>(algorithms);
        algorithmSelector.addActionListener(e -> switchAlgorithm());
        selectorPanel.add(algorithmSelector);
        headerPanel.add(selectorPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(new SCMLVisualizer(), "SCML");
        mainPanel.add(new SequenceAlignmentVisualizer(), "ALIGNMENT");
        mainPanel.add(new GlobalAlignmentVisualizer(), "GLOBAL_ALIGN");
        mainPanel.add(new KnapsackVisualizer(), "KNAPSACK");
        mainPanel.add(new CoinChangeVisualizer(), "COIN_CHANGE");
        mainPanel.add(new MatrixChainVisualizer(), "MATRIX_CHAIN");
        mainPanel.add(new RNASecondaryVisualizer(), "RNA");
        mainPanel.add(new MultiStageGraphVisualizer(), "MULTISTAGE");
        
        add(mainPanel, BorderLayout.CENTER);
        
        cardLayout.show(mainPanel, "SCML");
        titleLabel.setText("SCML - Subsecuencia Común Más Larga");
    }
    
    private void switchAlgorithm() {
        String[] cardNames = {"SCML", "ALIGNMENT", "GLOBAL_ALIGN", "KNAPSACK", 
                            "COIN_CHANGE", "MATRIX_CHAIN", "RNA", "MULTISTAGE"};
        String[] titles = {
            "SCML - Subsecuencia Común Más Larga",
            "Alineamiento de Secuencias (Edit Distance)", 
            "Alineamiento Global (Needleman-Wunsch)",
            "Mochila 0/1",
            "Cambio de Monedas",
            "Multiplicación Encadenada de Matrices",
            "Estructura Secundaria del ARN",
            "Grafo Multietapa"
        };
        
        int index = algorithmSelector.getSelectedIndex();
        cardLayout.show(mainPanel, cardNames[index]);
        titleLabel.setText(titles[index]);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DynamicProgrammingEducator());
    }
    
    // ==================== VISUALIZERS ====================
    
    static class SCMLVisualizer extends JPanel {
        private JTextField seqXField, seqYField;
        private DPTablePanel tablePanel;
        private CodeViewer codeViewer;
        private ControlPanel controlPanel;
        private JLabel resultLabel;
        private JTextArea displayArea;
        private char[] x, y;
        private int[][] dpTable;
        
        public SCMLVisualizer() {
            setLayout(new BorderLayout());
            initUI();
            loadExample("babbc", "abbca");
        }
        
        private void initUI() {
            JPanel inputPanel = new JPanel(new GridBagLayout());
            inputPanel.setBorder(BorderFactory.createTitledBorder("Input Sequences"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            gbc.gridx = 0; gbc.gridy = 0;
            inputPanel.add(new JLabel("Sequence X:"), gbc);
            gbc.gridx = 1;
            seqXField = new JTextField(20);
            inputPanel.add(seqXField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            inputPanel.add(new JLabel("Sequence Y:"), gbc);
            gbc.gridx = 1;
            seqYField = new JTextField(20);
            inputPanel.add(seqYField, gbc);
            
            gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 2;
            JButton startButton = new JButton("Compute");
            startButton.addActionListener(e -> compute());
            inputPanel.add(startButton, gbc);
            
            add(inputPanel, BorderLayout.NORTH);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            tablePanel = new DPTablePanel();
            splitPane.setTopComponent(tablePanel);
            
            JPanel bottomPanel = new JPanel(new BorderLayout());
            displayArea = new JTextArea(6, 50);
            displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            displayArea.setEditable(false);
            bottomPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
            
            resultLabel = new JLabel("Result: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            bottomPanel.add(resultLabel, BorderLayout.SOUTH);
            
            splitPane.setBottomComponent(bottomPanel);
            splitPane.setDividerLocation(450);
            
            add(splitPane, BorderLayout.CENTER);
            
            controlPanel = new ControlPanel(this::stepExecution, this::resetExecution);
            add(controlPanel, BorderLayout.SOUTH);
            
            codeViewer = new CodeViewer();
            codeViewer.setCode(SCML_CODE);
            
            JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            rightSplit.setLeftComponent(splitPane);
            rightSplit.setRightComponent(codeViewer);
            rightSplit.setDividerLocation(850);
            
            add(rightSplit, BorderLayout.CENTER);
        }
        
        private void loadExample(String sx, String sy) {
            seqXField.setText(sx);
            seqYField.setText(sy);
            compute();
        }
        
        private void compute() {
            x = seqXField.getText().toCharArray();
            y = seqYField.getText().toCharArray();
            
            dpTable = new int[x.length + 1][y.length + 1];
            for (int i = x.length; i >= 0; i--) {
                for (int j = y.length; j >= 0; j--) {
                    if (i == x.length || j == y.length) {
                        dpTable[i][j] = 0;
                    } else if (x[i] == y[j]) {
                        dpTable[i][j] = 1 + dpTable[i + 1][j + 1];
                    } else {
                        dpTable[i][j] = Math.max(dpTable[i + 1][j], dpTable[i][j + 1]);
                    }
                }
            }
            
            StringBuilder lcs = new StringBuilder();
            int i = 0, j = 0;
            while (i < x.length && j < y.length) {
                if (x[i] == y[j]) {
                    lcs.append(x[i]);
                    i++; j++;
                } else if (dpTable[i + 1][j] > dpTable[i][j + 1]) {
                    i++;
                } else {
                    j++;
                }
            }
            
            tablePanel.displayTable(dpTable, x, y);
            resultLabel.setText("Result: LCS Length = " + dpTable[0][0] + 
                              ", Sequence = '" + lcs.toString() + "'");
            updateDisplay(lcs.toString());
        }
        
        private void updateDisplay(String lcs) {
            StringBuilder sb = new StringBuilder("Sequence Alignment:\n\n");
            sb.append("X: ").append(new String(x)).append("\n");
            sb.append("Y: ").append(new String(y)).append("\n");
            sb.append("LCS: ").append(lcs).append("\n\n");
            
            sb.append("Alignment visualization:\n");
            int xi = 0, yi = 0;
            for (char c : lcs.toCharArray()) {
                while (xi < x.length && x[xi] != c) {
                    sb.append("-").append(x[xi]);
                    xi++;
                }
                while (yi < y.length && y[yi] != c) {
                    sb.append(" ").append(y[yi]).append(" ");
                    yi++;
                }
                sb.append("[").append(c).append("]");
                xi++; yi++;
            }
            displayArea.setText(sb.toString());
        }
        
        private void stepExecution() {}
        private void resetExecution() { compute(); }
        
        private static final String SCML_CODE = """
            // Recursive forward version
            public static int SCML1(char[] x, char[] y) {
                return SCMLa(x, y, 0, 0);
            }
            
            private static int SCMLa(char[] x, char[] y, int i, int j) {
                if (i == x.length || j == y.length) return 0;
                if (x[i] == y[j]) return 1 + SCMLa(x, y, i+1, j+1);
                return Math.max(SCMLa(x, y, i+1, j), SCMLa(x, y, i, j+1));
            }
            
            // Recursive backward version
            private static int SCMLb(char[] x, char[] y, int i, int j) {
                if (i == -1 || j == -1) return 0;
                if (x[i] == y[j]) return 1 + SCMLb(x, y, i-1, j-1);
                return Math.max(SCMLb(x, y, i-1, j), SCMLb(x, y, i, j-1));
            }
            """;
    }
    
    static class SequenceAlignmentVisualizer extends JPanel {
        private JTextField seqXField, seqYField;
        private DPTablePanel tablePanel;
        private CodeViewer codeViewer;
        private JLabel resultLabel;
        private JTextArea alignmentArea;
        private char[] x, y;
        private int[][] dpTable;
        
        public SequenceAlignmentVisualizer() {
            setLayout(new BorderLayout());
            initUI();
            loadExample("abbc", "babb");
        }
        
        private void initUI() {
            JPanel inputPanel = new JPanel(new GridBagLayout());
            inputPanel.setBorder(BorderFactory.createTitledBorder("Input Sequences"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            gbc.gridx = 0; gbc.gridy = 0;
            inputPanel.add(new JLabel("Sequence X:"), gbc);
            gbc.gridx = 1;
            seqXField = new JTextField(20);
            inputPanel.add(seqXField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            inputPanel.add(new JLabel("Sequence Y:"), gbc);
            gbc.gridx = 1;
            seqYField = new JTextField(20);
            inputPanel.add(seqYField, gbc);
            
            gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 2;
            JButton startButton = new JButton("Compute");
            startButton.addActionListener(e -> compute());
            inputPanel.add(startButton, gbc);
            
            add(inputPanel, BorderLayout.NORTH);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            tablePanel = new DPTablePanel();
            splitPane.setTopComponent(tablePanel);
            
            JPanel bottomPanel = new JPanel(new BorderLayout());
            alignmentArea = new JTextArea(10, 50);
            alignmentArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            alignmentArea.setEditable(false);
            bottomPanel.add(new JScrollPane(alignmentArea), BorderLayout.CENTER);
            
            resultLabel = new JLabel("Minimum operations: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            bottomPanel.add(resultLabel, BorderLayout.SOUTH);
            
            splitPane.setBottomComponent(bottomPanel);
            splitPane.setDividerLocation(450);
            
            add(splitPane, BorderLayout.CENTER);
            
            codeViewer = new CodeViewer();
            codeViewer.setCode(ALIGNMENT_CODE);
            
            JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            rightSplit.setLeftComponent(splitPane);
            rightSplit.setRightComponent(codeViewer);
            rightSplit.setDividerLocation(850);
            
            add(rightSplit, BorderLayout.CENTER);
        }
        
        private void loadExample(String sx, String sy) {
            seqXField.setText(sx);
            seqYField.setText(sy);
        }
        
        private void compute() {
            x = seqXField.getText().toCharArray();
            y = seqYField.getText().toCharArray();
            
            dpTable = new int[x.length + 1][y.length + 1];
            
            for (int i = 0; i <= x.length; i++) {
                dpTable[i][0] = i;
            }
            for (int j = 0; j <= y.length; j++) {
                dpTable[0][j] = j;
            }
            
            for (int i = 1; i <= x.length; i++) {
                for (int j = 1; j <= y.length; j++) {
                    int matchCost = (x[i-1] == y[j-1]) ? 0 : 1;
                    dpTable[i][j] = Math.min(
                        dpTable[i-1][j] + 1,
                        Math.min(dpTable[i][j-1] + 1, dpTable[i-1][j-1] + matchCost)
                    );
                }
            }
            
            tablePanel.displayEditDistanceTable(dpTable, x, y);
            resultLabel.setText("Minimum operations: " + dpTable[x.length][y.length]);
            reconstructAlignment();
        }
        
        private void reconstructAlignment() {
            StringBuilder alignX = new StringBuilder();
            StringBuilder alignY = new StringBuilder();
            StringBuilder ops = new StringBuilder();
            
            int i = x.length, j = y.length;
            while (i > 0 || j > 0) {
                if (i > 0 && j > 0 && dpTable[i][j] == dpTable[i-1][j-1] + (x[i-1] == y[j-1] ? 0 : 1)) {
                    alignX.insert(0, x[i-1]);
                    alignY.insert(0, y[j-1]);
                    ops.insert(0, x[i-1] == y[j-1] ? "|" : "*");
                    i--; j--;
                } else if (i > 0 && dpTable[i][j] == dpTable[i-1][j] + 1) {
                    alignX.insert(0, x[i-1]);
                    alignY.insert(0, "-");
                    ops.insert(0, "D");
                    i--;
                } else {
                    alignX.insert(0, "-");
                    alignY.insert(0, y[j-1]);
                    ops.insert(0, "I");
                    j--;
                }
            }
            
            StringBuilder display = new StringBuilder("Optimal Alignment:\n\n");
            display.append("X: ").append(alignX.toString()).append("\n");
            display.append("   ").append(ops.toString()).append("\n");
            display.append("Y: ").append(alignY.toString()).append("\n\n");
            display.append("Legend: | = match, * = substitute, D = delete, I = insert");
            
            alignmentArea.setText(display.toString());
        }
        
        private void stepExecution() {}
        private void resetExecution() { compute(); }
        
        private static final String ALIGNMENT_CODE = """
            // Recursive version (backward formulation)
            private static int a(char[] x, char[] y, int i, int j) {
                if (j == 0) return i;  // Delete all remaining chars
                if (i == 0) return j;  // Insert all remaining chars
                
                int c = (x[i-1] == y[j-1]) ? 0 : 1; // 0 if match, 1 if substitute
                
                return Math.min(
                    a(x, y, i-1, j) + 1,     // Delete
                    Math.min(
                        a(x, y, i, j-1) + 1, // Insert
                        a(x, y, i-1, j-1) + c // Substitute or match
                    )
                );
            }
            """;
    }
    
    static class GlobalAlignmentVisualizer extends JPanel {
        private JTextField seqXField, seqYField;
        private DPTablePanel tablePanel;
        private CodeViewer codeViewer;
        private JLabel resultLabel;
        private JTextArea alignmentArea;
        private char[] x, y;
        private int[][] dpTable;
        
        public GlobalAlignmentVisualizer() {
            setLayout(new BorderLayout());
            initUI();
            loadExample("GGGCAT", "GGACA");
        }
        
        private void initUI() {
            JPanel inputPanel = new JPanel(new GridBagLayout());
            inputPanel.setBorder(BorderFactory.createTitledBorder("DNA Sequences"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            gbc.gridx = 0; gbc.gridy = 0;
            inputPanel.add(new JLabel("Sequence 1:"), gbc);
            gbc.gridx = 1;
            seqXField = new JTextField(20);
            inputPanel.add(seqXField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            inputPanel.add(new JLabel("Sequence 2:"), gbc);
            gbc.gridx = 1;
            seqYField = new JTextField(20);
            inputPanel.add(seqYField, gbc);
            
            gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 2;
            JButton startButton = new JButton("Compute");
            startButton.addActionListener(e -> compute());
            inputPanel.add(startButton, gbc);
            
            add(inputPanel, BorderLayout.NORTH);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            tablePanel = new DPTablePanel();
            splitPane.setTopComponent(tablePanel);
            
            JPanel bottomPanel = new JPanel(new BorderLayout());
            alignmentArea = new JTextArea(8, 50);
            alignmentArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            alignmentArea.setEditable(false);
            bottomPanel.add(new JScrollPane(alignmentArea), BorderLayout.CENTER);
            
            resultLabel = new JLabel("Maximum similarity: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            bottomPanel.add(resultLabel, BorderLayout.SOUTH);
            
            splitPane.setBottomComponent(bottomPanel);
            splitPane.setDividerLocation(450);
            
            add(splitPane, BorderLayout.CENTER);
            
            codeViewer = new CodeViewer();
            codeViewer.setCode(GLOBAL_ALIGN_CODE);
            
            JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            rightSplit.setLeftComponent(splitPane);
            rightSplit.setRightComponent(codeViewer);
            rightSplit.setDividerLocation(850);
            
            add(rightSplit, BorderLayout.CENTER);
        }
        
        private void loadExample(String sx, String sy) {
            seqXField.setText(sx);
            seqYField.setText(sy);
        }
        
        private void compute() {
            x = seqXField.getText().toUpperCase().toCharArray();
            y = seqYField.getText().toUpperCase().toCharArray();
            
            dpTable = new int[x.length + 1][y.length + 1];
            
            for (int i = 0; i <= x.length; i++) {
                dpTable[i][0] = -2 * i;
            }
            for (int j = 0; j <= y.length; j++) {
                dpTable[0][j] = -2 * j;
            }
            
            for (int i = 1; i <= x.length; i++) {
                for (int j = 1; j <= y.length; j++) {
                    int match = (x[i-1] == y[j-1]) ? 1 : -1;
                    dpTable[i][j] = Math.max(
                        Math.max(dpTable[i-1][j] - 2, dpTable[i][j-1] - 2),
                        dpTable[i-1][j-1] + match
                    );
                }
            }
            
            tablePanel.displayEditDistanceTable(dpTable, x, y);
            resultLabel.setText("Maximum similarity: " + dpTable[x.length][y.length]);
            reconstructGlobalAlignment();
        }
        
        private void reconstructGlobalAlignment() {
            StringBuilder align1 = new StringBuilder();
            StringBuilder align2 = new StringBuilder();
            StringBuilder match = new StringBuilder();
            
            int i = x.length, j = y.length;
            int score = 0, matches = 0, mismatches = 0, gaps = 0;
            
            while (i > 0 || j > 0) {
                if (i > 0 && j > 0 && dpTable[i][j] == dpTable[i-1][j-1] + (x[i-1] == y[j-1] ? 1 : -1)) {
                    align1.insert(0, x[i-1]);
                    align2.insert(0, y[j-1]);
                    if (x[i-1] == y[j-1]) {
                        match.insert(0, "|");
                        matches++;
                        score += 1;
                    } else {
                        match.insert(0, "*");
                        mismatches++;
                        score -= 1;
                    }
                    i--; j--;
                } else if (i > 0 && dpTable[i][j] == dpTable[i-1][j] - 2) {
                    align1.insert(0, x[i-1]);
                    align2.insert(0, "-");
                    match.insert(0, " ");
                    gaps++;
                    i--;
                    score -= 2;
                } else {
                    align1.insert(0, "-");
                    align2.insert(0, y[j-1]);
                    match.insert(0, " ");
                    gaps++;
                    j--;
                    score -= 2;
                }
            }
            
            StringBuilder sb = new StringBuilder("Optimal Global Alignment:\n\n");
            sb.append("S1: ").append(align1.toString()).append("\n");
            sb.append("    ").append(match.toString()).append("\n");
            sb.append("S2: ").append(align2.toString()).append("\n\n");
            sb.append("Score: ").append(score).append(" (Matches: +").append(matches)
              .append(", Mismatches: -").append(mismatches).append(", Gaps: -").append(gaps).append(")\n");
            
            alignmentArea.setText(sb.toString());
        }
        
        private static final String GLOBAL_ALIGN_CODE = """
            // Needleman-Wunsch algorithm (backward formulation)
            private static int alinRec(int i, int j, char[] s, char[] t) {
                if (i == 0 && j == 0) return 0;
                if (i == 0) return -2 * j;
                if (j == 0) return -2 * i;
                
                int match = (s[i-1] == t[j-1]) ? 1 : -1;
                return Math.max(
                    alinRec(i, j-1, s, t) - 2,     // Insert gap in S1
                    Math.max(
                        alinRec(i-1, j, s, t) - 2, // Delete from S1
                        alinRec(i-1, j-1, s, t) + match // Match/mismatch
                    )
                );
            }
            """;
    }
    
    static class KnapsackVisualizer extends JPanel {
        private JTextField weightsField, benefitsField, capacityField;
        private DPTablePanel tablePanel;
        private JLabel resultLabel;
        
        public KnapsackVisualizer() {
            setLayout(new BorderLayout());
            initUI();
            loadExample();
        }
        
        private void initUI() {
            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            inputPanel.setBorder(BorderFactory.createTitledBorder("Knapsack Parameters"));
            
            inputPanel.add(new JLabel("Weights (comma-separated):"));
            weightsField = new JTextField(20);
            inputPanel.add(weightsField);
            
            inputPanel.add(new JLabel("Benefits (comma-separated):"));
            benefitsField = new JTextField(20);
            inputPanel.add(benefitsField);
            
            inputPanel.add(new JLabel("Capacity:"));
            capacityField = new JTextField(10);
            inputPanel.add(capacityField);
            
            add(inputPanel, BorderLayout.NORTH);
            
            tablePanel = new DPTablePanel();
            add(tablePanel, BorderLayout.CENTER);
            
            resultLabel = new JLabel("Maximum benefit: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(resultLabel, BorderLayout.SOUTH);
            
            JButton computeButton = new JButton("Compute");
            computeButton.addActionListener(e -> compute());
            inputPanel.add(computeButton);
        }
        
        private void loadExample() {
            weightsField.setText("3,6,9,5");
            benefitsField.setText("7,2,8,4");
            capacityField.setText("15");
        }
        
        private void compute() {
            String[] wStr = weightsField.getText().split(",");
            String[] bStr = benefitsField.getText().split(",");
            int capacity = Integer.parseInt(capacityField.getText());
            
            int[] weights = new int[wStr.length];
            int[] benefits = new int[bStr.length];
            for (int i = 0; i < wStr.length; i++) {
                weights[i] = Integer.parseInt(wStr[i].trim());
                benefits[i] = Integer.parseInt(bStr[i].trim());
            }
            
            int n = weights.length;
            int[][] dp = new int[n + 1][capacity + 1];
            
            for (int i = n; i >= 0; i--) {
                for (int w = 0; w <= capacity; w++) {
                    if (i == n) {
                        dp[i][w] = 0;
                    } else if (w < weights[i]) {
                        dp[i][w] = dp[i + 1][w];
                    } else {
                        dp[i][w] = Math.max(dp[i + 1][w], dp[i + 1][w - weights[i]] + benefits[i]);
                    }
                }
            }
            
            char[] dummyX = new char[n];
            for (int i = 0; i < n; i++) dummyX[i] = (char)('1' + i);
            char[] dummyY = new char[capacity + 1];
            for (int j = 0; j <= capacity; j++) dummyY[j] = (char)('0' + j % 10);
            
            tablePanel.displayTable(dp, dummyX, dummyY);
            resultLabel.setText("Maximum benefit: " + dp[0][capacity]);
        }
    }
    
    static class CoinChangeVisualizer extends JPanel {
        private JTextField coinsField, amountField;
        private DPTablePanel tablePanel;
        private JLabel resultLabel;
        
        public CoinChangeVisualizer() {
            setLayout(new BorderLayout());
            initUI();
            loadExample();
        }
        
        private void initUI() {
            JPanel inputPanel = new JPanel(new GridLayout(2, 2));
            inputPanel.setBorder(BorderFactory.createTitledBorder("Coin Change Parameters"));
            
            inputPanel.add(new JLabel("Coin denominations:"));
            coinsField = new JTextField(20);
            inputPanel.add(coinsField);
            
            inputPanel.add(new JLabel("Amount:"));
            amountField = new JTextField(10);
            inputPanel.add(amountField);
            
            add(inputPanel, BorderLayout.NORTH);
            
            tablePanel = new DPTablePanel();
            add(tablePanel, BorderLayout.CENTER);
            
            resultLabel = new JLabel("Minimum coins: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(resultLabel, BorderLayout.SOUTH);
            
            JButton computeButton = new JButton("Compute");
            computeButton.addActionListener(e -> compute());
            inputPanel.add(computeButton);
        }
        
        private void loadExample() {
            coinsField.setText("7,5,1");
            amountField.setText("10");
        }
        
        private void compute() {
            String[] coinStr = coinsField.getText().split(",");
            int amount = Integer.parseInt(amountField.getText());
            
            int[] coins = new int[coinStr.length];
            for (int i = 0; i < coinStr.length; i++) {
                coins[i] = Integer.parseInt(coinStr[i].trim());
            }
            
            int n = coins.length;
            int[][] dp = new int[n][amount + 1];
            
            for (int x = 0; x <= amount; x++) {
                dp[n - 1][x] = x;
            }
            
            for (int i = n - 2; i >= 0; i--) {
                for (int x = 0; x <= amount; x++) {
                    dp[i][x] = dp[i + 1][x];
                    for (int j = 1; j <= x / coins[i]; j++) {
                        dp[i][x] = Math.min(dp[i][x], j + dp[i + 1][x - j * coins[i]]);
                    }
                }
            }
            
            char[] dummyX = new char[n];
            for (int i = 0; i < n; i++) dummyX[i] = String.valueOf(coins[i]).charAt(0);
            char[] dummyY = new char[amount + 1];
            for (int j = 0; j <= amount; j++) dummyY[j] = (char)('0' + j);
            
            tablePanel.displayTable(dp, dummyX, dummyY);
            resultLabel.setText("Minimum coins: " + dp[0][amount]);
        }
    }
    
    static class MatrixChainVisualizer extends JPanel {
        private JTextField dimsField;
        private DPTablePanel tablePanel;
        private JLabel resultLabel;
        private JTextArea parenthesizationArea;
        
        public MatrixChainVisualizer() {
            setLayout(new BorderLayout());
            initUI();
            loadExample();
        }
        
        private void initUI() {
            JPanel inputPanel = new JPanel(new FlowLayout());
            inputPanel.setBorder(BorderFactory.createTitledBorder("Matrix Dimensions"));
            
            inputPanel.add(new JLabel("Dimensions (e.g., 5,2,4,1,7):"));
            dimsField = new JTextField(30);
            inputPanel.add(dimsField);
            
            JButton computeButton = new JButton("Compute");
            computeButton.addActionListener(e -> compute());
            inputPanel.add(computeButton);
            
            add(inputPanel, BorderLayout.NORTH);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            tablePanel = new DPTablePanel();
            splitPane.setTopComponent(tablePanel);
            
            parenthesizationArea = new JTextArea(4, 50);
            parenthesizationArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            parenthesizationArea.setEditable(false);
            splitPane.setBottomComponent(new JScrollPane(parenthesizationArea));
            splitPane.setDividerLocation(500);
            
            add(splitPane, BorderLayout.CENTER);
            
            resultLabel = new JLabel("Minimum multiplications: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(resultLabel, BorderLayout.SOUTH);
        }
        
        private void loadExample() {
            dimsField.setText("5,2,4,1,7");
        }
        
        private void compute() {
            String[] dimStr = dimsField.getText().split(",");
            int[] dims = new int[dimStr.length];
            for (int i = 0; i < dimStr.length; i++) {
                dims[i] = Integer.parseInt(dimStr[i].trim());
            }
            
            int n = dims.length - 1;
            int[][] dp = new int[n][n];
            int[][] split = new int[n][n];
            
            for (int i = 0; i < n; i++) {
                dp[i][i] = 0;
            }
            
            for (int len = 2; len <= n; len++) {
                for (int i = 0; i <= n - len; i++) {
                    int j = i + len - 1;
                    dp[i][j] = Integer.MAX_VALUE;
                    for (int k = i; k < j; k++) {
                        int cost = dp[i][k] + dp[k+1][j] + dims[i] * dims[k+1] * dims[j+1];
                        if (cost < dp[i][j]) {
                            dp[i][j] = cost;
                            split[i][j] = k;
                        }
                    }
                }
            }
            
            char[] dummyX = new char[n];
            char[] dummyY = new char[n];
            for (int i = 0; i < n; i++) {
                dummyX[i] = (char)('A' + i);
                dummyY[i] = (char)('A' + i);
            }
            
            tablePanel.displayTable(dp, dummyX, dummyY);
            resultLabel.setText("Minimum multiplications: " + dp[0][n-1]);
            
            String paren = printParenthesization(split, 0, n-1);
            parenthesizationArea.setText("Optimal Parenthesization:\n" + paren);
        }
        
        private String printParenthesization(int[][] split, int i, int j) {
            if (i == j) return "M" + (i + 1);
            return "(" + printParenthesization(split, i, split[i][j]) + 
                   " × " + printParenthesization(split, split[i][j] + 1, j) + ")";
        }
    }
    
    static class RNASecondaryVisualizer extends JPanel {
        private JTextField sequenceField;
        private DPTablePanel tablePanel;
        private JLabel resultLabel;
        private JTextArea pairingArea;
        
        public RNASecondaryVisualizer() {
            setLayout(new BorderLayout());
            initUI();
            loadExample();
        }
        
        private void initUI() {
            JPanel inputPanel = new JPanel(new FlowLayout());
            inputPanel.setBorder(BorderFactory.createTitledBorder("RNA Sequence"));
            
            inputPanel.add(new JLabel("Sequence (A,G,C,U):"));
            sequenceField = new JTextField(30);
            inputPanel.add(sequenceField);
            
            JButton computeButton = new JButton("Compute");
            computeButton.addActionListener(e -> compute());
            inputPanel.add(computeButton);
            
            add(inputPanel, BorderLayout.NORTH);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            tablePanel = new DPTablePanel();
            splitPane.setTopComponent(tablePanel);
            
            pairingArea = new JTextArea(6, 50);
            pairingArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            pairingArea.setEditable(false);
            splitPane.setBottomComponent(new JScrollPane(pairingArea));
            splitPane.setDividerLocation(500);
            
            add(splitPane, BorderLayout.CENTER);
            
            resultLabel = new JLabel("Maximum base pairs: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(resultLabel, BorderLayout.SOUTH);
        }
        
        private void loadExample() {
            sequenceField.setText("AGGCCUUCCU");
        }
        
        private void compute() {
            String seq = sequenceField.getText().toUpperCase();
            char[] rna = seq.toCharArray();
            int n = rna.length;
            
            if (n < 4) {
                resultLabel.setText("Sequence too short (minimum 4 bases)");
                return;
            }
            
            int[][] dp = new int[n][n];
            
            for (int k = 4; k < n; k++) {
                for (int i = 0; i + k < n; i++) {
                    int j = i + k;
                    int max = dp[i][j-1];
                    
                    for (int t = i; t < j - 3; t++) {
                        if (isComplementary(rna[t], rna[j])) {
                            int val = 1 + (t > i ? dp[i][t-1] : 0) + (t+1 < j ? dp[t+1][j-1] : 0);
                            max = Math.max(max, val);
                        }
                    }
                    dp[i][j] = max;
                }
            }
            
            char[] dummyX = new char[n];
            char[] dummyY = new char[n];
            for (int i = 0; i < n; i++) {
                dummyX[i] = (char)('0' + i);
                dummyY[i] = (char)('0' + i);
            }
            
            tablePanel.displayTable(dp, dummyX, dummyY);
            resultLabel.setText("Maximum base pairs: " + dp[0][n-1]);
            
            String pairs = reconstructPairs(rna, dp, 0, n-1);
            pairingArea.setText("Optimal Base Pairings:\n" + pairs);
        }
        
        private boolean isComplementary(char a, char b) {
            return (a == 'A' && b == 'U') || (a == 'U' && b == 'A') ||
                   (a == 'G' && b == 'C') || (a == 'C' && b == 'G') ||
                   (a == 'G' && b == 'U') || (a == 'U' && b == 'G');
        }
        
        private String reconstructPairs(char[] rna, int[][] dp, int i, int j) {
            if (i >= j) return "";
            StringBuilder sb = new StringBuilder();
            if (dp[i][j] == dp[i][j-1]) {
                return reconstructPairs(rna, dp, i, j-1);
            }
            for (int t = i; t < j - 3; t++) {
                if (isComplementary(rna[t], rna[j])) {
                    int val = 1 + (t > i ? dp[i][t-1] : 0) + (t+1 < j ? dp[t+1][j-1] : 0);
                    if (dp[i][j] == val) {
                        sb.append(rna[t]).append("-").append(rna[j]).append(" ");
                        sb.append(reconstructPairs(rna, dp, i, t-1));
                        sb.append(reconstructPairs(rna, dp, t+1, j-1));
                        break;
                    }
                }
            }
            return sb.toString();
        }
    }
    
    static class MultiStageGraphVisualizer extends JPanel {
        private JTextField graphField;
        private DPTablePanel tablePanel;
        private JLabel resultLabel;
        private JTextArea pathArea;
        
        public MultiStageGraphVisualizer() {
            setLayout(new BorderLayout());
            initUI();
        }
        
        private void initUI() {
            JPanel inputPanel = new JPanel(new FlowLayout());
            inputPanel.setBorder(BorderFactory.createTitledBorder("Multistage Graph"));
            
            inputPanel.add(new JLabel("Example: 4-stage graph with nodes [0,1,2,3,4,5,6,7,8,9]"));
            inputPanel.add(new JLabel("Edges: 0->1(5), 0->2(7), 0->3(6), etc."));
            
            JButton computeButton = new JButton("Compute Example");
            computeButton.addActionListener(e -> computeExample());
            inputPanel.add(computeButton);
            
            add(inputPanel, BorderLayout.NORTH);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            tablePanel = new DPTablePanel();
            splitPane.setTopComponent(tablePanel);
            
            pathArea = new JTextArea(4, 50);
            pathArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            pathArea.setEditable(false);
            splitPane.setBottomComponent(new JScrollPane(pathArea));
            splitPane.setDividerLocation(500);
            
            add(splitPane, BorderLayout.CENTER);
            
            resultLabel = new JLabel("Minimum cost path: ");
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(resultLabel, BorderLayout.SOUTH);
        }
        
        private void computeExample() {
            // Example 4-stage graph from course materials
            int[][] graph = {
                {0, 5, 7, 6, 1000, 1000, 1000, 1000, 1000, 1000},
                {1000, 0, 1000, 1000, 8, 1000, 1000, 1000, 1000, 1000},
                {1000, 1000, 0, 1000, 1000, 3, 1000, 1000, 1000, 1000},
                {1000, 1000, 1000, 0, 1000, 1000, 4, 1000, 1000, 1000},
                {1000, 1000, 1000, 1000, 0, 1000, 1000, 2, 1000, 1000},
                {1000, 1000, 1000, 1000, 1000, 0, 1000, 1000, 9, 1000},
                {1000, 1000, 1000, 1000, 1000, 1000, 0, 1000, 1000, 3},
                {1000, 1000, 1000, 1000, 1000, 1000, 1000, 0, 1000, 5},
                {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 0, 2},
                {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 0}
            };
            
            int[] stages = {0, 1, 4, 7, 9}; // Start indices of each stage
            
            int n = graph.length;
            int[] dp = new int[n];
            int[] next = new int[n];
            
            dp[n-1] = 0;
            
            for (int i = n-2; i >= 0; i--) {
                dp[i] = Integer.MAX_VALUE;
                for (int j = i+1; j < n; j++) {
                    if (graph[i][j] < 1000 && dp[j] + graph[i][j] < dp[i]) {
                        dp[i] = dp[j] + graph[i][j];
                        next[i] = j;
                    }
                }
            }
            
            char[] dummyX = new char[n];
            char[] dummyY = new char[n];
            for (int i = 0; i < n; i++) {
                dummyX[i] = (char)('0' + i);
                dummyY[i] = (char)('0' + i);
            }
            
            tablePanel.displayTable(convertTo2D(dp), dummyX, dummyY);
            resultLabel.setText("Minimum cost path: " + dp[0]);
            
            StringBuilder path = new StringBuilder("Shortest path: 0");
            int node = 0;
            while (node != n-1) {
                node = next[node];
                path.append(" -> ").append(node);
            }
            pathArea.setText(path.toString());
        }
        
        private int[][] convertTo2D(int[] arr) {
            int[][] result = new int[arr.length][1];
            for (int i = 0; i < arr.length; i++) {
                result[i][0] = arr[i];
            }
            return result;
        }
    }
    
    // ==================== COMPONENTS ====================
    
    static class DPTablePanel extends JPanel {
        private int[][] table;
        private char[] x, y;
        private int highlightI = -1, highlightJ = -1;
        
        public DPTablePanel() {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
        }
        
        public void displayTable(int[][] table, char[] x, char[] y) {
            this.table = table;
            this.x = x;
            this.y = y;
            repaint();
        }
        
        public void displayEditDistanceTable(int[][] table, char[] x, char[] y) {
            this.table = table;
            this.x = x;
            this.y = y;
            repaint();
        }
        
        public void highlightCell(int i, int j) {
            this.highlightI = i;
            this.highlightJ = j;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (table == null) return;
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int cellWidth = 50;
            int cellHeight = 35;
            int startX = 100;
            int startY = 80;
            
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("DP Table", startX + table[0].length * cellWidth / 2 - 30, startY - 30);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            if (x != null) {
                for (int i = 0; i < x.length && i < table[0].length-1; i++) {
                    g2d.drawString(String.valueOf(x[i]), startX + (i + 1) * cellWidth + cellWidth / 2 - 5, startY - 10);
                }
            }
            if (y != null) {
                for (int j = 0; j < y.length && j < table.length-1; j++) {
                    g2d.drawString(String.valueOf(y[j]), startX + 10, startY + (j + 1) * cellHeight + cellHeight / 2 + 5);
                }
            }
            
            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    int cellX = startX + j * cellWidth;
                    int cellY = startY + i * cellHeight;
                    
                    if (i == highlightI && j == highlightJ) {
                        g2d.setColor(Color.YELLOW);
                        g2d.fillRect(cellX, cellY, cellWidth, cellHeight);
                    }
                    
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(cellX, cellY, cellWidth, cellHeight);
                    g2d.drawString(String.valueOf(table[i][j]), 
                                 cellX + cellWidth / 2 - 10, cellY + cellHeight / 2 + 5);
                }
            }
        }
    }
    
    static class ControlPanel extends JPanel {
        public ControlPanel(Runnable stepAction, Runnable resetAction) {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setBorder(BorderFactory.createTitledBorder("Execution Control"));
            
            JButton stepButton = new JButton("Step Forward");
            stepButton.addActionListener(e -> stepAction.run());
            add(stepButton);
            
            JButton resetButton = new JButton("Reset");
            resetButton.addActionListener(e -> resetAction.run());
            add(resetButton);
        }
    }
    
    static class CodeViewer extends JPanel {
        private JTextArea codeArea;
        
        public CodeViewer() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createTitledBorder("Algorithm Implementation"));
            
            codeArea = new JTextArea();
            codeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            codeArea.setEditable(false);
            codeArea.setBackground(new Color(240, 240, 240));
            
            add(new JScrollPane(codeArea), BorderLayout.CENTER);
        }
        
        public void setCode(String code) {
            codeArea.setText(code);
        }
    }
    
    static class AlgorithmState {
        public int[][] dpTable;
        public int[] currentIndices;
        public Stack<int[]> callStack;
        
        public AlgorithmState(int[][] table, int[] startIndices) {
            this.dpTable = table;
            this.currentIndices = startIndices;
            this.callStack = new Stack<>();
        }
    }
}
