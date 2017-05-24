package hr.fer.zemris.nos.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mihael on 08.05.17..
 */
public class InputPanel extends JPanel {

    private JLabel rowName;
    private JTextField inputTextRow;
    private List<JButton> buttons;

    public InputPanel(String rowName, JTextField textField, JButton ... buttons) {
        this.rowName = new JLabel(rowName);
        this.inputTextRow = textField;
        this.buttons = new ArrayList<>();
        this.buttons.addAll(Arrays.asList(buttons));
        initPanel();
    }

    private void initPanel() {
        this.setLayout(new BorderLayout(10, 1));
        this.add(rowName, BorderLayout.WEST);
        this.add(inputTextRow, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setLayout(buttonLayout);
        buttons.forEach(b -> { buttonPanel.add(b); buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));});
        this.add(buttonPanel, BorderLayout.EAST);
    }

    public JLabel getRowName() {
        return rowName;
    }

    public JTextField getInputTextRow() {
        return inputTextRow;
    }

    public List<JButton> getButtons() {
        return buttons;
    }
}
