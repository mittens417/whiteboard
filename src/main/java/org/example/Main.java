package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private WhiteboardPanel whiteboardPanel;
    private JTextArea notepadArea;
    private CalculatorPanel calculatorPanel;
    private boolean enableNotepad = false; // Set to false to disable notepad tab

    public Main() {
        setTitle("OCA 1Z0-808 Practice - Whiteboard & Notepad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        initializeComponents();
    }

    private void initializeComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        whiteboardPanel = new WhiteboardPanel();
        calculatorPanel = new CalculatorPanel();

        tabbedPane.addTab("Whiteboard", createWhiteboardTab());

        if (enableNotepad) {
            notepadArea = new JTextArea();
            notepadArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            notepadArea.setBackground(Color.WHITE);
            notepadArea.setTabSize(4);
            tabbedPane.addTab("Code Notepad", createNotepadTab());
        }

        tabbedPane.addTab("Calculator", calculatorPanel);

        add(tabbedPane);
    }

    private JPanel createWhiteboardTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.clearDrawing();
            }
        });

        JButton blackPen = new JButton("Black");
        blackPen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawingColor(Color.BLACK);
            }
        });

        JButton redPen = new JButton("Red");
        redPen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawingColor(Color.RED);
            }
        });

        JButton bluePen = new JButton("Blue");
        bluePen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawingColor(Color.BLUE);
            }
        });

        JButton greenPen = new JButton("Green");
        greenPen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setDrawingColor(Color.GREEN);
            }
        });

        JButton eraserButton = new JButton("Eraser");
        eraserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setEraserMode(true);
            }
        });

        JButton smallEraser = new JButton("Small Eraser");
        smallEraser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setEraserMode(true);
                whiteboardPanel.setEraserSize(10);
            }
        });

        JButton largeEraser = new JButton("Large Eraser");
        largeEraser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whiteboardPanel.setEraserMode(true);
                whiteboardPanel.setEraserSize(30);
            }
        });

        toolbar.add(new JLabel("Tools: "));
        toolbar.add(clearButton);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(new JLabel("Colors: "));
        toolbar.add(blackPen);
        toolbar.add(redPen);
        toolbar.add(bluePen);
        toolbar.add(greenPen);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(new JLabel("Eraser: "));
        toolbar.add(eraserButton);
        toolbar.add(smallEraser);
        toolbar.add(largeEraser);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(whiteboardPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNotepadTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton clearTextButton = new JButton("Clear Text");
        clearTextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                notepadArea.setText("");
            }
        });

        JButton javaTemplateButton = new JButton("Java Class Template");
        javaTemplateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertJavaTemplate();
            }
        });

        toolbar.add(new JLabel("Notepad Tools: "));
        toolbar.add(clearTextButton);
        toolbar.add(javaTemplateButton);

        JScrollPane scrollPane = new JScrollPane(notepadArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void insertJavaTemplate() {
        String template = "public class Practice {\n" +
                "    public static void main(String[] args) {\n" +
                "        // Your OCA practice code here\n" +
                "        \n" +
                "    }\n" +
                "}";
        notepadArea.setText(template);
        notepadArea.setCaretPosition(notepadArea.getText().indexOf("// Your OCA practice code here"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}

class WhiteboardPanel extends JPanel {
    private List<DrawingPath> paths;
    private DrawingPath currentPath;
    private Color currentColor;
    private boolean drawing;
    private boolean eraserMode;
    private int eraserSize;
    private Point lastMousePos;

    public WhiteboardPanel() {
        paths = new ArrayList<DrawingPath>();
        currentColor = Color.BLACK;
        drawing = false;
        eraserMode = false;
        eraserSize = 20;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (eraserMode) {
                    startErasing(e.getX(), e.getY());
                } else {
                    startDrawing(e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (eraserMode) {
                    stopErasing();
                } else {
                    stopDrawing();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (eraserMode) {
                    eraseAt(e.getX(), e.getY());
                } else if (drawing) {
                    addPointToCurrentPath(e.getX(), e.getY());
                }
                repaint();
            }

            public void mouseMoved(MouseEvent e) {
                lastMousePos = e.getPoint();
                if (eraserMode) {
                    repaint();
                }
            }
        });
    }

    private void startDrawing(int x, int y) {
        currentPath = new DrawingPath(currentColor, 2); // Normal pen stroke width
        currentPath.addPoint(x, y);
        drawing = true;
    }

    private void addPointToCurrentPath(int x, int y) {
        if (currentPath != null) {
            currentPath.addPoint(x, y);
        }
    }

    private void stopDrawing() {
        if (currentPath != null && currentPath.getPoints().size() > 0) {
            paths.add(currentPath);
        }
        currentPath = null;
        drawing = false;
        repaint();
    }

    public void clearDrawing() {
        paths.clear();
        currentPath = null;
        drawing = false;
        repaint();
    }

    public void setDrawingColor(Color color) {
        currentColor = color;
        eraserMode = false;
    }

    public void setEraserMode(boolean eraser) {
        eraserMode = eraser;
        if (eraser) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public void setEraserSize(int size) {
        eraserSize = size;
    }

    private void startErasing(int x, int y) {
        // Create a white path with eraser size as stroke width
        currentPath = new DrawingPath(getBackground(), eraserSize);
        currentPath.addPoint(x, y);
        drawing = true;
    }

    private void stopErasing() {
        if (currentPath != null && currentPath.getPoints().size() > 0) {
            paths.add(currentPath);
        }
        currentPath = null;
        drawing = false;
        repaint();
    }

    private void eraseAt(int x, int y) {
        if (currentPath != null) {
            currentPath.addPoint(x, y);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw all completed paths
        for (int i = 0; i < paths.size(); i++) {
            DrawingPath path = paths.get(i);
            g2d.setColor(path.getColor());
            g2d.setStroke(new BasicStroke(path.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            List<Point> points = path.getPoints();
            for (int j = 1; j < points.size(); j++) {
                Point p1 = points.get(j - 1);
                Point p2 = points.get(j);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Draw current path being drawn
        if (currentPath != null && drawing) {
            g2d.setColor(currentPath.getColor());
            g2d.setStroke(new BasicStroke(currentPath.getStrokeWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            List<Point> points = currentPath.getPoints();
            for (int j = 1; j < points.size(); j++) {
                Point p1 = points.get(j - 1);
                Point p2 = points.get(j);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Draw eraser cursor when in eraser mode
        if (eraserMode && lastMousePos != null) {
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawOval(lastMousePos.x - eraserSize/2, lastMousePos.y - eraserSize/2, eraserSize, eraserSize);

            // Draw crosshair in the center
            g2d.drawLine(lastMousePos.x - 5, lastMousePos.y, lastMousePos.x + 5, lastMousePos.y);
            g2d.drawLine(lastMousePos.x, lastMousePos.y - 5, lastMousePos.x, lastMousePos.y + 5);
        }
    }
}

class DrawingPath {
    private List<Point> points;
    private Color color;
    private float strokeWidth;

    public DrawingPath(Color color, float strokeWidth) {
        this.points = new ArrayList<Point>();
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    public List<Point> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }
}

class CalculatorPanel extends JPanel {
    private JTextField displayField;
    private JTextField expressionField;
    private String currentExpression;

    public CalculatorPanel() {
        currentExpression = "";

        setLayout(new BorderLayout());
        setupCalculator();
    }

    private void setupCalculator() {
        // Expression field to show the arithmetic statement
        expressionField = new JTextField("");
        expressionField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        expressionField.setHorizontalAlignment(JTextField.RIGHT);
        expressionField.setEditable(false);
        expressionField.setBackground(Color.LIGHT_GRAY);
        expressionField.setBorder(BorderFactory.createLoweredBevelBorder());
        expressionField.setPreferredSize(new Dimension(300, 30));

        // Display field for current number/result
        displayField = new JTextField("0");
        displayField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setBackground(Color.WHITE);
        displayField.setBorder(BorderFactory.createLoweredBevelBorder());
        displayField.setPreferredSize(new Dimension(300, 60));
        displayField.setMinimumSize(new Dimension(300, 60));

        displayField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKeyInput(e);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create buttons directly without array variable
        String[][] buttonLayout = {
                {"C", "CE", "±", "÷"},
                {"(", ")", "%", "×"},
                {"7", "8", "9", "-"},
                {"4", "5", "6", "+"},
                {"1", "2", "3", "="},
                {"0", ".", "00", ""}
        };

        for (int row = 0; row < buttonLayout.length; row++) {
            for (int col = 0; col < buttonLayout[row].length; col++) {
                String label = buttonLayout[row][col];
                // Skip empty cells
                if (label.isEmpty()) continue;
                JButton button = createCalculatorButton(label);
                buttonPanel.add(button);
            }
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Oracle Style Calculator", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.add(expressionField, BorderLayout.NORTH);
        displayPanel.add(displayField, BorderLayout.CENTER);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(displayPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setBackground(new Color(240, 240, 240));
    }

    private void handleKeyInput(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int keyCode = e.getKeyCode();

        if (Character.isDigit(keyChar)) {
            handleInput(String.valueOf(keyChar));
            e.consume();
        }
        else if (keyChar == '+') {
            handleInput("+");
            e.consume();
        }
        else if (keyChar == '-') {
            handleInput("-");
            e.consume();
        }
        else if (keyChar == '*') {
            handleInput("×");
            e.consume();
        }
        else if (keyChar == '/') {
            handleInput("÷");
            e.consume();
        }
        else if (keyChar == '%') {
            handleInput("%");
            e.consume();
        }
        else if (keyChar == '.') {
            handleInput(".");
            e.consume();
        }
        else if (keyChar == '(' || keyChar == ')') {
            handleInput(String.valueOf(keyChar));
            e.consume();
        }
        else if (keyChar == '=' || keyCode == KeyEvent.VK_ENTER) {
            handleEquals();
            e.consume();
        }
        else if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_DELETE) {
            handleClear();
            e.consume();
        }
        else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            handleBackspace();
            e.consume();
        }
        else if (!Character.isISOControl(keyChar)) {
            e.consume();
        }
    }

    private void handleBackspace() {
        if (!currentExpression.isEmpty()) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            displayField.setText(currentExpression.isEmpty() ? "0" : currentExpression);
            expressionField.setText(currentExpression);
        }
    }

    private JButton createCalculatorButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button.setPreferredSize(new Dimension(70, 50));

        if (text.matches("[0-9]") || text.equals(".") || text.equals("00")) {
            button.setBackground(new Color(230, 240, 255));
        } else if (text.matches("[+\\-×÷=%()]")) {
            button.setBackground(new Color(255, 240, 200));
        } else {
            button.setBackground(new Color(245, 245, 245));
        }

        button.addActionListener(new ButtonClickListener(text));
        return button;
    }

    private class ButtonClickListener implements ActionListener {
        private String buttonText;

        public ButtonClickListener(String text) {
            this.buttonText = text;
        }

        public void actionPerformed(ActionEvent e) {
            handleButtonClick(buttonText);
        }
    }

    private void handleButtonClick(String command) {
        try {
            if (command.equals("00")) {
                handleInput("0");
                handleInput("0");
            } else if (command.matches("[0-9+\\-×÷%.()]")) {
                handleInput(command);
            } else if (command.equals("=")) {
                handleEquals();
            } else if (command.equals("C")) {
                handleClear();
            } else if (command.equals("CE")) {
                handleClearEntry();
            } else if (command.equals("±")) {
                handlePlusMinus();
            }
        } catch (Exception ex) {
            displayField.setText("Error");
            currentExpression = "";
            expressionField.setText("");
        }
    }

    private void handleInput(String input) {
        if (displayField.getText().equals("Error")) {
            handleClear();
        }

        // Handle special cases for operators
        if (input.matches("[+\\-×÷%]")) {
            // Don't allow operator at the beginning unless it's minus
            if (currentExpression.isEmpty() && !input.equals("-")) {
                return;
            }
            // Don't allow consecutive operators (except for negative numbers)
            if (!currentExpression.isEmpty()) {
                char lastChar = currentExpression.charAt(currentExpression.length() - 1);
                if ((lastChar == '+' || lastChar == '×' || lastChar == '÷' || lastChar == '%') && !input.equals("-")) {
                    return;
                }
                if (lastChar == '-' && input.matches("[+\\-×÷%]")) {
                    return;
                }
            }
        }

        // Handle decimal point
        if (input.equals(".")) {
            // Find the last number in the expression
            String[] parts = currentExpression.split("[+\\-×÷%()]");
            if (parts.length > 0) {
                String lastNumber = parts[parts.length - 1];
                if (lastNumber.contains(".")) {
                    return; // Already has decimal
                }
            }
        }

        currentExpression += input;
        displayField.setText(currentExpression.isEmpty() ? "0" : currentExpression);
        expressionField.setText(currentExpression);
    }

    private void handleEquals() {
        if (currentExpression.isEmpty()) return;

        try {
            // Check for balanced parentheses
            if (!areParenthesesBalanced(currentExpression)) {
                displayField.setText("Error: Unbalanced parentheses");
                return;
            }

            double result = evaluateExpression(currentExpression);
            String resultStr = formatResult(result);

            expressionField.setText(currentExpression + " = " + resultStr);
            displayField.setText(resultStr);
            currentExpression = resultStr;

        } catch (Exception ex) {
            displayField.setText("Error");
            expressionField.setText("Invalid expression");
        }
    }

    private boolean areParenthesesBalanced(String expression) {
        int count = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') count++;
            else if (c == ')') count--;
            if (count < 0) return false;
        }
        return count == 0;
    }

    private double evaluateExpression(String expression) throws Exception {
        // Replace × and ÷ with * and /
        expression = expression.replace("×", "*").replace("÷", "/");

        // Simple recursive descent parser
        return parseExpression(expression, 0).value;
    }

    private class ParseResult {
        double value;
        int position;

        ParseResult(double value, int position) {
            this.value = value;
            this.position = position;
        }
    }

    private ParseResult parseExpression(String expr, int pos) throws Exception {
        ParseResult left = parseTerm(expr, pos);

        while (left.position < expr.length()) {
            char op = expr.charAt(left.position);
            if (op != '+' && op != '-') break;

            ParseResult right = parseTerm(expr, left.position + 1);
            if (op == '+') {
                left.value += right.value;
            } else {
                left.value -= right.value;
            }
            left.position = right.position;
        }

        return left;
    }

    private ParseResult parseTerm(String expr, int pos) throws Exception {
        ParseResult left = parseFactor(expr, pos);

        while (left.position < expr.length()) {
            char op = expr.charAt(left.position);
            if (op != '*' && op != '/' && op != '%') break;

            ParseResult right = parseFactor(expr, left.position + 1);
            if (op == '*') {
                left.value *= right.value;
            } else if (op == '/') {
                if (right.value == 0) throw new ArithmeticException("Division by zero");
                left.value /= right.value;
            } else { // %
                if (right.value == 0) throw new ArithmeticException("Modulo by zero");
                left.value %= right.value;
            }
            left.position = right.position;
        }

        return left;
    }

    private ParseResult parseFactor(String expr, int pos) throws Exception {
        if (pos >= expr.length()) throw new Exception("Unexpected end of expression");

        // Handle parentheses
        if (expr.charAt(pos) == '(') {
            ParseResult result = parseExpression(expr, pos + 1);
            if (result.position >= expr.length() || expr.charAt(result.position) != ')') {
                throw new Exception("Missing closing parenthesis");
            }
            result.position++; // Skip the ')'
            return result;
        }

        // Handle negative numbers
        boolean negative = false;
        if (expr.charAt(pos) == '-') {
            negative = true;
            pos++;
        }

        // Parse number
        int start = pos;
        while (pos < expr.length() && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.')) {
            pos++;
        }

        if (start == pos) throw new Exception("Expected number");

        double value = Double.parseDouble(expr.substring(start, pos));
        if (negative) value = -value;

        return new ParseResult(value, pos);
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%.8f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }

    private void handleClear() {
        displayField.setText("0");
        expressionField.setText("");
        currentExpression = "";
    }

    private void handleClearEntry() {
        displayField.setText("0");
        // Clear the last entered number or operator
        if (!currentExpression.isEmpty()) {
            // Find the last continuous sequence of digits/decimal
            int i = currentExpression.length() - 1;
            while (i >= 0 && (Character.isDigit(currentExpression.charAt(i)) || currentExpression.charAt(i) == '.')) {
                i--;
            }
            if (i < currentExpression.length() - 1) {
                // Found digits to remove
                currentExpression = currentExpression.substring(0, i + 1);
            } else if (i >= 0) {
                // Remove the last operator
                currentExpression = currentExpression.substring(0, i);
            }
            expressionField.setText(currentExpression);
            displayField.setText(currentExpression.isEmpty() ? "0" : currentExpression);
        }
    }

    private void handlePlusMinus() {
        if (currentExpression.isEmpty() || currentExpression.equals("0")) {
            currentExpression = "-";
        } else {
            // Toggle sign of the last number in the expression
            // This is simplified - in a full implementation, you'd need more complex logic
            if (currentExpression.startsWith("-")) {
                currentExpression = currentExpression.substring(1);
            } else {
                currentExpression = "-" + currentExpression;
            }
        }
        displayField.setText(currentExpression.isEmpty() ? "0" : currentExpression);
        expressionField.setText(currentExpression);
    }

    private void updateExpressionDisplay() {
        expressionField.setText(currentExpression);
    }
}