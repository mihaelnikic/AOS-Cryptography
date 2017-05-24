package hr.fer.zemris.nos.gui.textfield;

import javax.swing.*;

/**
 * Created by mihael on 10.05.17..
 */
public class FrozenTextField extends JTextField {

    public FrozenTextField(String s) {
        super(s);
        this.setEditable(false);
    }
}
