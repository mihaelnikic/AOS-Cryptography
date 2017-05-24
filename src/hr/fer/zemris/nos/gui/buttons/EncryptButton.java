package hr.fer.zemris.nos.gui.buttons;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoAlgorithm;
import hr.fer.zemris.nos.crypto.CryptoException;
import hr.fer.zemris.nos.gui.AOSFrame;
import hr.fer.zemris.nos.symmetric.SymmetricCryptoSystem;
import hr.fer.zemris.nos.system.ICryptoSystem;
import hr.fer.zemris.nos.utils.Pair;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 10.05.17..
 */
public class EncryptButton extends JButton {

   // private static final String ENCRYPT_LABEL = "Encrypt";
   private static final String ENCRYPT_ADD_SIGN = "Encrypt/Sign";
    private static final String ENCRYPT_NO_SIGN = "Encrypt";
    private static final String ENCRYPT_SIGN_ONLY = "Sign";

    private String selectedText;
    private Path selectedFilePath;
    private AOSFrame parentFrame;
    private Pair<SelectButton, SelectButton> brankoKeys;
    private Pair<SelectButton, SelectButton> anaKeys;
    private List<SelectButton> inputOutput;
    private ICryptoSystem cryptoSystem;

    public EncryptButton(AOSFrame parentFrame, ICryptoSystem cryptoSystem, List<SelectButton> inputOutput,
                         Pair<SelectButton, SelectButton> anaKeys, Pair<SelectButton, SelectButton> brankoKeys) {
        if(cryptoSystem.getCryptoAlgorithm() != null && cryptoSystem.getSignatureChecker() != null) {
            encryptAction.putValue(AbstractAction.NAME, ENCRYPT_ADD_SIGN);
        } else if(cryptoSystem.getCryptoAlgorithm() != null){
            encryptAction.putValue(AbstractAction.NAME, ENCRYPT_NO_SIGN);
        } else {
            encryptAction.putValue(AbstractAction.NAME, ENCRYPT_SIGN_ONLY);
        }
        this.setAction(encryptAction);
        this.parentFrame = parentFrame;
        this.brankoKeys = brankoKeys;
        this.anaKeys = anaKeys;
        this.inputOutput = inputOutput;
        this.cryptoSystem = cryptoSystem;
    }

    private AbstractAction encryptAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                String input = inputOutput.get(0).getSelectedText();
                String inputFileName = inputOutput.get(0).getSelectedFilePath().getFileName().toString();
                String outputPath = inputOutput.get(1).getSelectedFilePath().toAbsolutePath().toString();
                String additionalSigFile = null;

                //ana keys
                PrivateKey anaPrivateKey = cryptoSystem.getKeyGenerator().loadPrivateKey(
                        anaKeys.getX().getSelectedFilePath().toAbsolutePath().toString()
                );

                PublicKey anaPublicKey = null;

                //branko keys

                PrivateKey brankoPrivateKey = null;

                PublicKey brankoPublicKey = null;

                byte[] encryptedOrSign = null;
                byte[] additionalSig = null;

                if (cryptoSystem instanceof SymmetricCryptoSystem) {
                    encryptedOrSign = cryptoSystem.getCryptoAlgorithm().encrypt(input.getBytes(), anaPrivateKey);
                } else if(cryptoSystem.getSignatureChecker() != null  && cryptoSystem.getCryptoAlgorithm() != null) {
                    brankoPublicKey =  cryptoSystem.getKeyGenerator().loadPublicKey(
                            brankoKeys.getY().getSelectedFilePath().toAbsolutePath().toString()
                    );
                    brankoPrivateKey = cryptoSystem.getKeyGenerator().loadPrivateKey(
                            brankoKeys.getX().getSelectedFilePath().toAbsolutePath().toString()
                    );
                    anaPublicKey = cryptoSystem.getKeyGenerator().loadPublicKey(
                            anaKeys.getY().getSelectedFilePath().toAbsolutePath().toString()
                    );
                    encryptedOrSign = cryptoSystem.getCryptoAlgorithm().encrypt(input.getBytes(), brankoPublicKey);
                    additionalSig = cryptoSystem.getSignatureChecker().createDigest(encryptedOrSign, anaPrivateKey);
                    additionalSigFile = inputOutput.get(2).getSelectedFilePath().toAbsolutePath().toString();

                } else if(cryptoSystem.getSignatureChecker() != null) {
                    encryptedOrSign = cryptoSystem.getSignatureChecker().createDigest(input.getBytes(), anaPrivateKey);
                    anaPublicKey = cryptoSystem.getKeyGenerator().loadPublicKey(
                            anaKeys.getY().getSelectedFilePath().toAbsolutePath().toString()
                    );
                } else if(cryptoSystem.getSignatureChecker() == null) {
                    anaPublicKey = cryptoSystem.getKeyGenerator().loadPublicKey(
                            anaKeys.getY().getSelectedFilePath().toAbsolutePath().toString()
                    );
                    encryptedOrSign = cryptoSystem.getCryptoAlgorithm().encrypt(input.getBytes(), anaPublicKey);
                } else {
                    throw new CryptoException("Uknown crypto algorithm!");
                }

                cryptoSystem.storeFile(outputPath, inputFileName, anaPublicKey, anaPrivateKey, encryptedOrSign, additionalSigFile, additionalSig
                , brankoPublicKey, brankoPrivateKey);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        parentFrame,
                        "Encryption Error! ",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Successfully encrypted! ",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };
}
