package hr.fer.zemris.nos.gui.buttons;

import hr.fer.zemris.nos.gui.AOSFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mihael on 10.05.17..
 */
public class SelectButton extends JButton {

    private static final String SELECT_LABEL = "Select";

    private String selectedText;
    private Path selectedFilePath;
    private AOSFrame parentFrame;
    private JTextField inputField;

    public SelectButton(AOSFrame parentFrame, JTextField inputField) {
        openFileAction.putValue(AbstractAction.NAME, SELECT_LABEL);
        this.setAction(openFileAction);
        this.parentFrame = parentFrame;
        this.inputField = inputField;
        selectedFilePath = Paths.get(inputField.getText().trim());
    }

    public String getSelectedText() {
        openSelectedFile(selectedFilePath);
        return selectedText;
    }

    public Path getSelectedFilePath() {
        return selectedFilePath;
    }

    public final AbstractAction openFileAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if(fc.showOpenDialog(parentFrame) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();

            openSelectedFile(filePath);
            selectedFilePath = filePath;
            inputField.setText(selectedFilePath.toAbsolutePath().toString());

        }
    };

    private void openSelectedFile(Path filePath) {
        if(!Files.isReadable(filePath)) {
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Datoteka " + filePath.toAbsolutePath() + " ne postoji!",
                    "Pogreška",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(filePath);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Pogreška prilikom čitanja datoteke " + filePath.toAbsolutePath(),
                    "Pogreška",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        selectedText = new String(bytes, StandardCharsets.UTF_8);
    }


}
