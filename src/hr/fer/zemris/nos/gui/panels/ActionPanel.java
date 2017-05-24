package hr.fer.zemris.nos.gui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mihael on 10.05.17..
 */
public class ActionPanel extends JPanel {

    private JButton encryptButton;
    private JButton decryptButton;

    public ActionPanel(JButton encryptButton, JButton decryptButton) {
        this.encryptButton = encryptButton;
        this.decryptButton = decryptButton;
        initPanel();
    }

    private void initPanel() {
        BoxLayout buttonLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(buttonLayout);
        this.add(encryptButton);
        this.add(Box.createRigidArea(new Dimension(5,0)));
        this.add(decryptButton);
    }

    public JButton getEncryptButton() {
        return encryptButton;
    }

    public JButton getDecryptButton() {
        return decryptButton;
    }
}
