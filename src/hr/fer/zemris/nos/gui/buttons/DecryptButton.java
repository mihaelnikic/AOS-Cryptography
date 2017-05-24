package hr.fer.zemris.nos.gui.buttons;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoSystem;
import hr.fer.zemris.nos.crypto.CryptoException;
import hr.fer.zemris.nos.gui.AOSFrame;
import hr.fer.zemris.nos.symmetric.SymmetricCryptoSystem;
import hr.fer.zemris.nos.system.ICryptoSystem;
import hr.fer.zemris.nos.utils.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

/**
 * Created by mihael on 10.05.17..
 */
public class DecryptButton extends JButton {

    private static final String DECRYPT_ADD_SIGN = "Decrypt/Check";
    private static final String DECRYPT_NO_SIGN = "Decrypt";
    private static final String DECRYPT_SIGN_ONLY = "Check";

    private String selectedText;
    private Path selectedFilePath;
    private AOSFrame parentFrame;
    private Pair<SelectButton, SelectButton> brankoKeys;
    private Pair<SelectButton, SelectButton> anaKeys;
    private List<SelectButton> inputOutput;
    private ICryptoSystem cryptoSystem;
    private JTextArea decryptArea;

    public DecryptButton(AOSFrame parentFrame, JTextArea decryptArea, ICryptoSystem cryptoSystem, List<SelectButton> inputOutput,
                         Pair<SelectButton, SelectButton> anaKeys, Pair<SelectButton, SelectButton> brankoKeys) {

        if(cryptoSystem.getCryptoAlgorithm() != null && cryptoSystem.getSignatureChecker() != null) {
            encryptAction.putValue(AbstractAction.NAME, DECRYPT_ADD_SIGN);
        } else if(cryptoSystem.getCryptoAlgorithm() != null){
            encryptAction.putValue(AbstractAction.NAME, DECRYPT_NO_SIGN);
        } else {
            encryptAction.putValue(AbstractAction.NAME, DECRYPT_SIGN_ONLY);
        }
        this.setAction(encryptAction);
        this.parentFrame = parentFrame;
        this.brankoKeys = brankoKeys;
        this.anaKeys = anaKeys;
        this.inputOutput = inputOutput;
        this.cryptoSystem = cryptoSystem;
        this.decryptArea = decryptArea;
    }

    private AbstractAction encryptAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StringBuilder message = new StringBuilder();
            boolean valid = false;
            //ana keys
            try {
                PrivateKey anaPrivateKey = cryptoSystem.getKeyGenerator().loadPrivateKey(
                        anaKeys.getX().getSelectedFilePath().toAbsolutePath().toString()
                );
                PublicKey anaPublicKey = null;
                PrivateKey brankoPrivateKey = null;
                PublicKey brankoPublicKey = null;

                String originalText = inputOutput.get(0).getSelectedText();
                String encryptedFile = inputOutput.get(1).getSelectedFilePath().toAbsolutePath().toString();
                String additionalSig = inputOutput.get(2) != null ? inputOutput.get(2).getSelectedFilePath()
                        .toAbsolutePath().toString() : null;
                byte encrypted[] = cryptoSystem.loadEncryptedData(encryptedFile);

                if (cryptoSystem instanceof SymmetricCryptoSystem || cryptoSystem instanceof AsymmetricCryptoSystem
                        || cryptoSystem.getSignatureChecker() == null) {
                    byte decrypted[] = cryptoSystem.getCryptoAlgorithm().decrypt(encrypted, anaPrivateKey);
                    String dec = new String(decrypted);
                    decryptArea.setText(dec);
                    message.append("Jednak originalnom: ")
                            .append(originalText.trim().equalsIgnoreCase(dec.trim()));
                    valid = originalText.trim().equalsIgnoreCase(dec.trim());

                } else if(cryptoSystem.getSignatureChecker() != null  && cryptoSystem.getCryptoAlgorithm() != null) {
                    byte addSig[] = cryptoSystem.loadAddSig(additionalSig);
                    brankoPrivateKey = cryptoSystem.getKeyGenerator().loadPrivateKey(
                            brankoKeys.getX().getSelectedFilePath().toAbsolutePath().toString()
                    );
                    anaPublicKey = cryptoSystem.getKeyGenerator().loadPublicKey(
                            anaKeys.getY().getSelectedFilePath().toAbsolutePath().toString()
                    );

                    byte[] decrypted = cryptoSystem.getCryptoAlgorithm().decrypt(encrypted, brankoPrivateKey);
                    boolean decryptedSig = cryptoSystem.getSignatureChecker().checkDigest(encrypted, addSig, anaPublicKey);
                    String dec = new String(decrypted);
                    message.append("Jednak originalnom: ")
                            .append(originalText.trim().equalsIgnoreCase(dec.trim())).append("\n")
                            .append("Potpis ispravan: ").append(decryptedSig);
                    decryptArea.setText(dec);
                    valid = decryptedSig && originalText.trim().equalsIgnoreCase(dec.trim());

                } else if(cryptoSystem.getSignatureChecker() != null) {
                    anaPublicKey = cryptoSystem.getKeyGenerator().loadPublicKey(
                            anaKeys.getY().getSelectedFilePath().toAbsolutePath().toString()
                    );
                    boolean decryptedSig = cryptoSystem.getSignatureChecker().checkDigest(originalText.getBytes(), encrypted, anaPublicKey);
                    message.append("Potpis ispravan: ").append(decryptedSig);
                    valid = decryptedSig;

                } else {
                    throw new CryptoException("Uknown crypto algorithm!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        parentFrame,
                        "Decryption Error! ",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            JOptionPane.showMessageDialog(
                    parentFrame,
                    message.toString(),
                    "Success",
                    valid ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );

        }
    };

}
