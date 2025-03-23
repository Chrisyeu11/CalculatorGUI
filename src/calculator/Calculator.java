package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {

    JTextField inputField;
    JLabel operationLabel;
    JTextArea historyArea;
    JScrollPane historyScroll;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[9];
    JButton addButton, subButton, mulButton, divButton, decButton, eqButton;
    JButton clrButton, ceButton, delButton, darkModeButton;
    JPanel panel;

    Font font = new Font("Arial", Font.BOLD, 24);
    Font smallFont = new Font("Arial", Font.PLAIN, 16);

    double num1 = 0, num2 = 0, result = 0;
    char operator;
    boolean isDarkMode = false;

    public Calculator() {
        setTitle("Calculator");
        setSize(420, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Dark Mode button first
        darkModeButton = new JButton("Dark Mode");
        darkModeButton.setBounds(30, 5, 320, 30);
        darkModeButton.addActionListener(this);
        darkModeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        darkModeButton.setFocusable(false);
        add(darkModeButton);

        // History area next
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyScroll = new JScrollPane(historyArea);
        historyScroll.setBounds(30, 40, 320, 100);
        add(historyScroll);

        // Operation label and input field
        operationLabel = new JLabel("");
        operationLabel.setBounds(30, 145, 320, 20);
        operationLabel.setFont(smallFont);
        add(operationLabel);

        inputField = new JTextField();
        inputField.setBounds(30, 165, 320, 50);
        inputField.setFont(font);
        inputField.setEditable(false);
        add(inputField);

        // Function buttons
        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        eqButton = new JButton("=");
        clrButton = new JButton("C");
        ceButton = new JButton("CE");
        delButton = new JButton("\u2190");

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = eqButton;
        functionButtons[6] = clrButton;
        functionButtons[7] = ceButton;
        functionButtons[8] = delButton;

        for (JButton button : functionButtons) {
            button.addActionListener(this);
            button.setFont(font);
            button.setFocusable(false);
        }

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(font);
            numberButtons[i].setFocusable(false);
        }

        panel = new JPanel();
        panel.setBounds(30, 230, 320, 400);
        panel.setLayout(new GridLayout(5, 4, 10, 10));

        panel.add(clrButton);
        panel.add(ceButton);
        panel.add(delButton);
        panel.add(divButton);

        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);

        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);

        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(eqButton);
        panel.add(new JLabel());

        add(panel);
        setVisible(true);
        setupKeyBindings();
        applyTheme();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                inputField.setText(inputField.getText().concat(String.valueOf(i)));
                return;
            }
        }

        if (e.getSource() == decButton) {
            inputField.setText(inputField.getText().concat("."));
        }

        if (e.getSource() == addButton || e.getSource() == subButton || e.getSource() == mulButton || e.getSource() == divButton) {
            if (!inputField.getText().isEmpty()) {
                num1 = Double.parseDouble(inputField.getText());
                operator = ((JButton) e.getSource()).getText().charAt(0);
                inputField.setText("");
                operationLabel.setText(num1 + " " + operator);
            }
        }

        if (e.getSource() == eqButton) {
            try {
                String input = inputField.getText();
                if (input.isEmpty()) return;
                num2 = Double.parseDouble(input);

                String expression = num1 + " " + operator + " " + num2;
                switch (operator) {
                    case '+': result = num1 + num2; break;
                    case '-': result = num1 - num2; break;
                    case '*': result = num1 * num2; break;
                    case '/':
                        if (num2 == 0) {
                            inputField.setText("Cannot divide by zero");
                            operationLabel.setText("");
                            return;
                        } else {
                            result = num1 / num2;
                        }
                        break;
                }

                String resultStr = result == (long) result ? String.valueOf((long) result) : String.valueOf(result);
                inputField.setText(resultStr);
                operationLabel.setText("");
                historyArea.append(expression + " = " + resultStr + "\n");
                num1 = result;

            } catch (Exception ex) {
                inputField.setText("Error");
                operationLabel.setText("");
            }
        }

        if (e.getSource() == clrButton) {
            inputField.setText("");
            operationLabel.setText("");
            num1 = 0;
            num2 = 0;
            result = 0;
        }

        if (e.getSource() == ceButton) {
            performClearEntry();
        }

        if (e.getSource() == delButton) {
            performBackspace();
        }

        if (e.getSource() == darkModeButton) {
            isDarkMode = !isDarkMode;
            applyTheme();
        }
    }

    private void setupKeyBindings() {
        JComponent comp = this.getRootPane();
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = comp.getActionMap();

        for (int i = 0; i <= 9; i++) {
            final int num = i;
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, 0), "num" + i);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0 + i, 0), "num" + i);
            am.put("num" + i, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    inputField.setText(inputField.getText() + num);
                }
            });
        }

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), "decimal");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0), "decimal");
        am.put("decimal", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                decButton.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "backspace");
        am.put("backspace", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                delButton.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "clearEntry");
        am.put("clearEntry", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ceButton.doClick();
            }
        });

        bindKey(im, am, KeyEvent.VK_ADD, "+", addButton);
        bindKey(im, am, KeyEvent.VK_SUBTRACT, "-", subButton);
        bindKey(im, am, KeyEvent.VK_MULTIPLY, "*", mulButton);
        bindKey(im, am, KeyEvent.VK_DIVIDE, "/", divButton);
        bindKey(im, am, KeyEvent.VK_ENTER, "=", eqButton);
        bindKey(im, am, KeyEvent.VK_ESCAPE, "C", clrButton);
    }

    private void bindKey(InputMap im, ActionMap am, int keyCode, String name, JButton button) {
        im.put(KeyStroke.getKeyStroke(keyCode, 0), name);
        am.put(name, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        });
    }

    private void performBackspace() {
        String text = inputField.getText();
        if (text.equals("Error") || text.equals("Cannot divide by zero")) {
            inputField.setText("");
            return;
        }
        if (!text.isEmpty()) {
            inputField.setText(text.substring(0, text.length() - 1));
        }
    }

    private void performClearEntry() {
        inputField.setText("");
    }

    private void applyTheme() {
        Color bg = isDarkMode ? Color.decode("#1e1e1e") : Color.WHITE;
        Color fg = isDarkMode ? Color.WHITE : Color.BLACK;
        Color panelBg = isDarkMode ? Color.decode("#2e2e2e") : Color.LIGHT_GRAY;
        Color btnBg = isDarkMode ? Color.decode("#3a3a3a") : Color.WHITE;

        getContentPane().setBackground(bg);
        inputField.setBackground(bg);
        inputField.setForeground(fg);
        inputField.setCaretColor(fg);
        operationLabel.setForeground(fg);
        historyArea.setBackground(bg);
        historyArea.setForeground(fg);
        historyScroll.getViewport().setBackground(bg);
        panel.setBackground(panelBg);

        darkModeButton.setBackground(btnBg);
        darkModeButton.setForeground(fg);

        for (JButton b : functionButtons) {
            b.setBackground(btnBg);
            b.setForeground(fg);
        }
        for (JButton b : numberButtons) {
            b.setBackground(btnBg);
            b.setForeground(fg);
        }
    }
}