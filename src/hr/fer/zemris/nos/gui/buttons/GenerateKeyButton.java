package hr.fer.zemris.nos.gui.buttons;

import hr.fer.zemris.nos.asymmetric.rsa.RSAKeyGen;
import hr.fer.zemris.nos.gui.AOSFrame;
import hr.fer.zemris.nos.symmetric.aes.AESKeyDescription;
import hr.fer.zemris.nos.system.ICryptoSystem;
import hr.fer.zemris.nos.utils.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;


/**
 * Created by mihael on 10.05.17..
 */
public class GenerateKeyButton extends JButton {

    private static final String KEY_LABEL = "Generate key/s";
    private AOSFrame parentFrame;
    private SelectButton privateKeySelectButton;
    private SelectButton publicKeySelectButton;
    private ICryptoSystem cryptoSystem;

    public GenerateKeyButton(AOSFrame parentFrame, SelectButton privateKeySelectButton,
                             SelectButton publicKeySelectButton, ICryptoSystem cryptoSystem) {
        generateAction.putValue(AbstractAction.NAME, KEY_LABEL);
        this.setAction(generateAction);
        this.parentFrame = parentFrame;
        this.privateKeySelectButton = privateKeySelectButton;
        this.publicKeySelectButton = publicKeySelectButton;
        this.cryptoSystem = cryptoSystem;
    }

    public GenerateKeyButton(AOSFrame parentFrame, SelectButton privateKeySelectButton, ICryptoSystem cryptoSystem) {
        this(parentFrame, privateKeySelectButton, null, cryptoSystem);
    }



    private AbstractAction generateAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                if(cryptoSystem.getKeyGenerator().hasPublicKey()) {
                    Pair<PrivateKey, PublicKey> keys = cryptoSystem.getKeyGenerator()
                            .generateKeys(RSAKeyGen.DEFAULT_KEY_SIZE);

                    cryptoSystem.getKeyGenerator().storePrivateKey(keys.getX(), privateKeySelectButton.getSelectedFilePath().toAbsolutePath().toString());
                    cryptoSystem.getKeyGenerator().storePublicKey(keys.getY(), publicKeySelectButton.getSelectedFilePath().toAbsolutePath().toString());
                } else {
                    PrivateKey privateKey = cryptoSystem.getKeyGenerator().generatePrivateKey(AESKeyDescription.AES_128.getKeySize()); // AES 128 - 16 bytes
                    cryptoSystem.getKeyGenerator().storePrivateKey(privateKey, privateKeySelectButton.getSelectedFilePath().toAbsolutePath().toString());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        parentFrame,
                        "Pogreška prilikom otvaranja datoteke kljuca! ",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Keys are successfully generated! ",
                    "Success!",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

}