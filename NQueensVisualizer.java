import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * NQueensVisualizer
 * ------------------
 * Juego/visualizador educativo para el problema de las N-Reinas.
 * - Implementa backtracking clásico guardando pasos para visualización.
 * - Permite avanzar/retroceder paso a paso, auto-play con velocidad ajustable,
 *   resaltar backtracks y mostrar soluciones encontradas.
 * - Interfaz elegante y didáctica inspirada en el visualizador previo de permutaciones.
 *
 * Compilar: javac NQueensVisualizer.java
 * Ejecutar: java NQueensVisualizer
 *
 * Requiere Java 8+.
 */
public class NQueensVisualizer extends JFrame {
    // UI
    private JPanel topPanel, boardPanel, rightPanel, controlsPanel;
    private JSpinner nSpinner;
    private JButton generateButton, stepForwardButton, stepBackButton, autoButton, resetButton;
    private JSlider speedSlider;
    private JCheckBox showAttacksCheckbox, highlightBacktrackCheckbox;
    private JTextArea logArea;
    private DefaultListModel<String> solutionsModel;
    private JList<String> solutionsList;
    private JLabel statusLabel;

    // Algorithm state
    private int N = 8;
    private int[] board; // board[row] = col or -1
    private boolean[] cols;
    private boolean[] diag1; // row+col
    private boolean[] diag2; // row-col+N-1

    // Visualization steps
    private List<StepNode> steps;
    private int stepIndex;
    private boolean isAuto;
    private javax.swing.Timer autoTimer;

    // UI constants
    private static final int MIN_N = 4;
    private static final int MAX_N = 14; // keep reasonable for visualization

    public NQueensVisualizer() {
        super("N-Reinas� Mi Visualizador Educativo @fernando_potenciano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        initComponents();
        layoutComponents();
        attachListeners();
        initializeAlgorithmState();
    }

    private void initComponents() {
        // Controls
        topPanel = new JPanel(new BorderLayout());
        controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

        nSpinner = new JSpinner(new SpinnerNumberModel(N, MIN_N, MAX_N, 1));
        generateButton = new JButton("Generate Steps");
        stepForwardButton = new JButton("Step Forward�");
        stepBackButton = new JButton("Step Back");
        autoButton = new JButton("Auto");
        resetButton = new JButton("Reset");
        speedSlider = new JSlider(50, 1200, 500); // ms
        speedSlider.setToolTipText("Auto-play speed (ms)");

        showAttacksCheckbox = new JCheckBox("Show attacked squares", true);
        highlightBacktrackCheckbox = new JCheckBox("Highlight backtracks", true);

        controlsPanel.add(new JLabel("N:"));
        controlsPanel.add(nSpinner);
        controlsPanel.add(generateButton);
        controlsPanel.add(stepBackButton);
        controlsPanel.add(stepForwardButton);
        controlsPanel.add(autoButton);
        controlsPanel.add(new JLabel("Speed:"));
        controlsPanel.add(speedSlider);
        controlsPanel.add(resetButton);
        controlsPanel.add(showAttacksCheckbox);
        controlsPanel.add(highlightBacktrackCheckbox);

        // Board panel
        boardPanel = new BoardPanel();
        boardPanel.setPreferredSize(new Dimension(700, 650));
        boardPanel.setBackground(Color.WHITE);

        // Right panel: log + solutions
        rightPanel = new JPanel(new BorderLayout(6,6));
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(350, 300));

        solutionsModel = new DefaultListModel<>();
        solutionsList = new JList<>(solutionsModel);
        JScrollPane solScroll = new JScrollPane(solutionsList);
        solScroll.setPreferredSize(new Dimension(350, 300));

        statusLabel = new JLabel("Ready");

        rightPanel.add(logScroll, BorderLayout.NORTH);
        rightPanel.add(solScroll, BorderLayout.CENTER);
        rightPanel.add(statusLabel, BorderLayout.SOUTH);

        // Timer
        autoTimer = new javax.swing.Timer(speedSlider.getValue(), new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!stepForward()) {
                    autoTimer.stop();
                    isAuto = false;
                    autoButton.setText("Auto");
                }
            }
        });
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(8,8));
        add(controlsPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void attachListeners() {
        generateButton.addActionListener(e -> generateSteps());
        stepForwardButton.addActionListener(e -> stepForward());
        stepBackButton.addActionListener(e -> stepBack());
        autoButton.addActionListener(e -> toggleAuto());
        resetButton.addActionListener(e -> reset());

        speedSlider.addChangeListener((ChangeEvent) -> {
            int delay = speedSlider.getValue();
            autoTimer.setDelay(delay);
        });

        nSpinner.addChangeListener((ChangeEvent) -> {
            N = (Integer) nSpinner.getValue();
        });

        solutionsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int i = solutionsList.getSelectedIndex();
                if (i >= 0) {
                    // jump to solution visualization (optional)
                    String s = solutionsModel.get(i);
                    logArea.append("Selected solution: " + s + "\n");
                }
            }
        });
    }

    private void initializeAlgorithmState() {
        board = new int[N];
        Arrays.fill(board, -1);
        cols = new boolean[N];
        diag1 = new boolean[2*N];
        diag2 = new boolean[2*N];
        steps = new ArrayList<>();
        stepIndex = 0;
        isAuto = false;
    }

    private void generateSteps() {
        // Initialize structures for the chosen N
        N = (Integer) nSpinner.getValue();
        board = new int[N];
        Arrays.fill(board, -1);
        cols = new boolean[N];
        diag1 = new boolean[2*N];
        diag2 = new boolean[2*N];
        steps.clear();
        solutionsModel.clear();
        logArea.setText("");
        stepIndex = 0;

        // Run backtracking but record steps (on EDT — OK for small N; for large N, run in background)
        logArea.append("Generating steps for N=" + N + "...\n");
        long t0 = System.currentTimeMillis();
        backtrackRecord(0);
        long t1 = System.currentTimeMillis();
        logArea.append("Done. Steps recorded: " + steps.size() + ", solutions: " + solutionsModel.size() + ". Time: " + (t1-t0) + " ms\n");
        statusLabel.setText("Steps: " + steps.size() + " | Solutions: " + solutionsModel.size());
        boardPanel.repaint();
    }

    // Standard backtracking but record nodes for visualization
    private void backtrackRecord(int row) {
        if (row == N) {
            // Found solution
            String sol = formatSolution(board);
            solutionsModel.addElement(sol);
            // Record a step: solution reached (no placement in this step, mark as solution)
            steps.add(new StepNode(deepCopy(board), -1, -1, StepType.SOLUTION));
            return;
        }

        for (int col = 0; col < N; col++) {
            if (isSafe(row, col)) {
                // Place queen
                placeQueen(row, col);
                // Record placement step
                steps.add(new StepNode(deepCopy(board), row, col, StepType.PLACE));

                backtrackRecord(row + 1);

                // Backtrack: remove queen
                removeQueen(row, col);
                // Record backtrack step
                steps.add(new StepNode(deepCopy(board), row, col, StepType.BACKTRACK));
            } else {
                // Optional: record attempted but invalid placement (visually helpful)
                steps.add(new StepNode(deepCopy(board), row, col, StepType.INVALID));
            }
        }
    }

    private boolean isSafe(int row, int col) {
        if (cols[col]) return false;
        if (diag1[row+col]) return false;
        if (diag2[row - col + N]) return false;
        return true;
    }

    private void placeQueen(int row, int col) {
        board[row] = col;
        cols[col] = true;
        diag1[row+col] = true;
        diag2[row - col + N] = true;
    }

    private void removeQueen(int row, int col) {
        board[row] = -1;
        cols[col] = false;
        diag1[row+col] = false;
        diag2[row - col + N] = false;
    }

    private int[] deepCopy(int[] arr) {
        return Arrays.copyOf(arr, arr.length);
    }

    private String formatSolution(int[] b) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < b.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(b[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    // Step navigation
    private boolean stepForward() {
        if (steps.isEmpty() || stepIndex >= steps.size()) {
            statusLabel.setText("Finished (" + (steps.size()) + " steps)");
            return false;
        }
        StepNode node = steps.get(stepIndex);
        applyNodeToLiveBoard(node);
        logNode(node, stepIndex+1);
        stepIndex++;
        statusLabel.setText(String.format("Step %d/%d | Solutions: %d", stepIndex, steps.size(), solutionsModel.size()));
        boardPanel.repaint();
        return true;
    }

    private boolean stepBack() {
        if (steps.isEmpty() || stepIndex <= 0) {
            return false;
        }
        stepIndex--;
        StepNode node = steps.get(stepIndex);
        applyNodeToLiveBoard(node);
        // When stepping back we present the state at that node; it's OK for learning purposes
        logArea.append("Step back to " + (stepIndex+1) + ": " + node.type + "\n");
        statusLabel.setText(String.format("Step %d/%d | Solutions: %d", stepIndex, steps.size(), solutionsModel.size()));
        boardPanel.repaint();
        return true;
    }

    private void applyNodeToLiveBoard(StepNode node) {
        // Copy node.board snapshot to live board
        if (node.boardSnapshot != null) {
            // Make sure board length equals current N; if N changed, ignore
            if (node.boardSnapshot.length == N) {
                board = deepCopy(node.boardSnapshot);
            }
        }
    }

    private void logNode(StepNode node, int index) {
        switch (node.type) {
            case PLACE:
                logArea.append(String.format("%03d: PLACE   row=%d col=%d\n", index, node.row, node.col));
                break;
            case BACKTRACK:
                if (highlightBacktrackCheckbox.isSelected())
                    logArea.append(String.format("%03d: BACKTRACK row=%d col=%d\n", index, node.row, node.col));
                else
                    logArea.append(String.format("%03d: backtrack\n", index));
                break;
            case INVALID:
                logArea.append(String.format("%03d: INVALID  try row=%d col=%d\n", index, node.row, node.col));
                break;
            case SOLUTION:
                logArea.append(String.format("%03d: SOLUTION  %s\n", index, formatSolution(node.boardSnapshot)));
                break;
        }
    }

    private void toggleAuto() {
        if (isAuto) {
            autoTimer.stop();
            isAuto = false;
            autoButton.setText("Auto");
        } else {
            if (steps.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No steps recorded. Click 'Generate Steps' first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            autoTimer.setDelay(speedSlider.getValue());
            autoTimer.start();
            isAuto = true;
            autoButton.setText("Stop");
        }
    }

    private void reset() {
        autoTimer.stop();
        isAuto = false;
        autoButton.setText("Auto");
        initializeAlgorithmState();
        steps.clear();
        solutionsModel.clear();
        logArea.setText("");
        statusLabel.setText("Reset");
        boardPanel.repaint();
    }

    // --- Data structures for steps ---
    private enum StepType { PLACE, BACKTRACK, INVALID, SOLUTION }

    private static class StepNode {
        int[] boardSnapshot; // snapshot of board after the action
        int row, col; // the row/col attempted or affected
        StepType type;

        StepNode(int[] boardSnapshot, int row, int col, StepType type) {
            this.boardSnapshot = boardSnapshot;
            this.row = row;
            this.col = col;
            this.type = type;
        }
    }

    // --- Board drawing ---
    private class BoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 40;
            int margin = 20;
            int x0 = (getWidth() - size) / 2;
            int y0 = margin;
            int cell = size / N;

            // Draw checkerboard
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    boolean dark = (r + c) % 2 == 0;
                    g2.setColor(dark ? new Color(235,235,235) : Color.WHITE);
                    g2.fillRect(x0 + c*cell, y0 + r*cell, cell, cell);
                }
            }

            // If showAttacks, compute attacked cells from current board
            boolean[][] attacked = new boolean[N][N];
            if (showAttacksCheckbox.isSelected()) {
                for (int r = 0; r < N; r++) {
                    int qc = board[r];
                    if (qc >= 0) {
                        // mark row/col/diagonals
                        for (int k = 0; k < N; k++) attacked[r][k] = true;
                        for (int k = 0; k < N; k++) attacked[k][qc] = true;
                        for (int d=-N; d<=N; d++) {
                            int rr = r + d; int cc = qc + d;
                            if (0<=rr && rr<N && 0<=cc && cc<N) attacked[rr][cc] = true;
                            rr = r + d; cc = qc - d;
                            if (0<=rr && rr<N && 0<=cc && cc<N) attacked[rr][cc] = true;
                        }
                    }
                }
            }

            // Draw attacked overlay
            if (showAttacksCheckbox.isSelected()) {
                g2.setColor(new Color(255, 200, 200, 100));
                for (int r = 0; r < N; r++) {
                    for (int c = 0; c < N; c++) {
                        if (attacked[r][c]) g2.fillRect(x0 + c*cell, y0 + r*cell, cell, cell);
                    }
                }
            }

            // Draw queens
            for (int r = 0; r < N; r++) {
                int c = board[r];
                if (c >= 0) {
                    drawQueen(g2, x0 + c*cell, y0 + r*cell, cell);
                }
            }

            // Draw grid lines
            g2.setColor(Color.DARK_GRAY);
            for (int i = 0; i <= N; i++) {
                g2.drawLine(x0, y0 + i*cell, x0 + N*cell, y0 + i*cell);
                g2.drawLine(x0 + i*cell, y0, x0 + i*cell, y0 + N*cell);
            }

            // Draw legend and last action highlight
            drawLegend(g2, x0 + N*cell + 10, y0);

            // Highlight placement/backtrack from current stepIndex-1
            if (stepIndex > 0 && stepIndex <= steps.size()) {
                StepNode node = steps.get(stepIndex-1);
                if (node.type == StepType.PLACE) {
                    highlightCell(g2, x0 + node.col*cell, y0 + node.row*cell, cell, new Color(100, 200, 100, 140));
                } else if (node.type == StepType.BACKTRACK && highlightBacktrackCheckbox.isSelected()) {
                    highlightCell(g2, x0 + node.col*cell, y0 + node.row*cell, cell, new Color(220, 100, 100, 160));
                } else if (node.type == StepType.INVALID) {
                    highlightCell(g2, x0 + node.col*cell, y0 + node.row*cell, cell, new Color(255, 180, 50, 140));
                }
            }
        }

        private void drawQueen(Graphics2D g2, int x, int y, int cell) {
            int padding = cell/8;
            int size = cell - 2*padding;
            g2.setColor(new Color(40,40,90));
            g2.fillOval(x + padding, y + padding, size, size);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x + padding, y + padding, size, size);
            // small crown
            int cx = x + cell/2;
            int cy = y + padding + size/3;
            int crownW = size/2;
            int crownH = size/4;
            Polygon crown = new Polygon();
            crown.addPoint(cx - crownW/2, cy + crownH/2);
            crown.addPoint(cx - crownW/4, cy - crownH/2);
            crown.addPoint(cx, cy + crownH/2);
            crown.addPoint(cx + crownW/4, cy - crownH/2);
            crown.addPoint(cx + crownW/2, cy + crownH/2);
            g2.setColor(new Color(255,215,0,180));
            g2.fill(crown);
        }

        private void highlightCell(Graphics2D g2, int x, int y, int cell, Color color) {
            g2.setColor(color);
            g2.fillRect(x, y, cell, cell);
            // redraw queen if present (so it stays visible)
        }

        private void drawLegend(Graphics2D g2, int x, int y) {
            g2.setColor(Color.BLACK);
            g2.drawString("Legend:", x, y + 12);
            g2.setColor(new Color(100,200,100,140));
            g2.fillRect(x, y + 18, 12, 12);
            g2.setColor(Color.BLACK);
            g2.drawString("Last placement", x + 18, y + 30);

            g2.setColor(new Color(220,100,100,160));
            g2.fillRect(x, y + 36, 12, 12);
            g2.setColor(Color.BLACK);
            g2.drawString("Last backtrack", x + 18, y + 48);

            g2.setColor(new Color(255,200,200,100));
            g2.fillRect(x, y + 54, 12, 12);
            g2.setColor(Color.BLACK);
            g2.drawString("Attacked squares", x + 18, y + 66);
        }
    }

    // --- Main ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) { /* ignore */ }
            NQueensVisualizer app = new NQueensVisualizer();
            app.setVisible(true);
        });
    }
}
