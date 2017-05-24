package hr.fer.zemris.nos.gui.buttons;

import hr.fer.zemris.nos.gui.AOSFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by mihael on 10.05.17..
 */
public class OpenFileButton extends JButton {

    private static final String OPEN_LABEL = "Open";
    private AOSFrame parentFrame;
    private SelectButton selectButton;

    public OpenFileButton(AOSFrame parentFrame, SelectButton selectButton) {
        openFileAction.putValue(AbstractAction.NAME, OPEN_LABEL);
        this.setAction(openFileAction);
        this.parentFrame = parentFrame;
        this.selectButton = selectButton;
    }

    private AbstractAction openFileAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (Desktop.isDesktopSupported()) {
                try {
                    ProcessBuilder pb = new ProcessBuilder("subl", selectButton.getSelectedFilePath().toAbsolutePath().toString());
                    pb.start();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            parentFrame,
                            "Pogreška prilikom otvaranja datoteke " + selectButton.getSelectedFilePath().toAbsolutePath()
                                    + " u editoru!",
                            "Pogreška",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            else {
                JOptionPane.showMessageDialog(
                        parentFrame,
                        "Pogreška prilikom otvaranja datoteke " + selectButton.getSelectedFilePath().toAbsolutePath()
                        + " u editoru!",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    };
}
