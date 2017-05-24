package hr.fer.zemris.nos.gui;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoSystem;
import hr.fer.zemris.nos.asymmetric.rsa.RSASystem;
import hr.fer.zemris.nos.envelope.EnvelopeSystem;
import hr.fer.zemris.nos.gui.buttons.*;
import hr.fer.zemris.nos.gui.panels.ActionPanel;
import hr.fer.zemris.nos.gui.panels.InputPanel;
import hr.fer.zemris.nos.gui.textfield.FrozenTextField;
import hr.fer.zemris.nos.signature.SignatureSystem;
import hr.fer.zemris.nos.stamp.StampSystem;
import hr.fer.zemris.nos.symmetric.SymmetricCryptoSystem;
import hr.fer.zemris.nos.symmetric.aes.AESSystem;
import hr.fer.zemris.nos.system.ICryptoSystem;
import hr.fer.zemris.nos.utils.Pair;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mihael on 08.05.17..
 */
public class AOSFrame extends JFrame {

    final static String BUTTONPANEL = "Tab with JButtons";
    final static String TEXTPANEL = "Tab with JTextField";
    final static int extraWindowWidth = 100;

    // file constants
    private static final String INPUT_FILE = "ulaz.txt";
    private static final String OUTPUT_FILE = "izlaz.txt";
    private static final String OUTPUT_ADDITIONAL = "additional_signature.txt";
    private static final String ANA_PRIVATE_KEY = "ana_private_key.txt";
    private static final String ANA_PUBLIC_KEY = "ana_public_key.txt";
    private static final String BRANKO_PRIVATE_KEY = "branko_private_key.txt";
    private static final String BRANKO_PUBLIC_KEY = "branko_public_key.txt";
    // label constants
    private static final String ANA_PRIVATE_KEY_LABEL = "Ana's private key:";
    private static final String ANA_PUBLIC_KEY_LABEL = "Ana's public key:";
    private static final String BRANKO_PRIVATE_KEY_LABEL = "Branko's private key:";
    private static final String BRANKO_PUBLIC_KEY_LABEL = "Branko's public key:";
    private static final String INPUT_FILE_LABEL = "Input file:";
    private static final String OUTPUT_FILE_LABEL = "Output file:";
    private static final String ADDITIONAL_SIGNATURE_LABEL = "Signature file:";


    private JTabbedPane tabbedPane;
    private List<ICryptoSystem> cryptoSystemList;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new AOSFrame().setVisible(true);
        });
    }

    public AOSFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(20, 20);
        //setSize(1280, 720);
        setTitle("NOS labos 3 i 4");
        initCryptoSystem();
        initGUI();
        pack();
    }

    public void initCryptoSystem() {
        cryptoSystemList = new ArrayList<>();
        RSASystem rsaSystem = new RSASystem();
        AESSystem aesSystem = new AESSystem();
        EnvelopeSystem envelopeSystem = new EnvelopeSystem(aesSystem, rsaSystem);
        SignatureSystem signatureSystem = new SignatureSystem(rsaSystem);
        StampSystem stampSystem = new StampSystem(envelopeSystem, signatureSystem);
        cryptoSystemList.addAll(Arrays.asList(rsaSystem, aesSystem, envelopeSystem, signatureSystem, stampSystem));
    }

    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        //addComponentToPane(getContentPane());
        initTabbedPane(getContentPane());
        //initRSAPane();
        intCryptoSystemsPanes();
    }

    private void intCryptoSystemsPanes() {
        cryptoSystemList.forEach(cryptoSystem -> {
            JTextArea decryptArea = new JTextArea();
            decryptArea.setLineWrap(true);
            decryptArea.setEditable(false);
            decryptArea.setRows(15);
            JPanel cryptoPanel = new JPanel();
            cryptoPanel.setLayout(new BoxLayout(cryptoPanel, BoxLayout.Y_AXIS));
            // intialize input/output text row
            List<SelectButton> inputOutputSelectButton = initInputFile(cryptoPanel,
                    cryptoSystem);
            // initalize key rows
            Pair<SelectButton, SelectButton> anaKeyButtons = initKeys(cryptoPanel, cryptoSystem, ANA_PRIVATE_KEY_LABEL, ANA_PUBLIC_KEY_LABEL,
                    ANA_PRIVATE_KEY, ANA_PUBLIC_KEY);
            Pair<SelectButton, SelectButton> brankoKeyButtons = null;
            if(cryptoSystem.getSignatureChecker() != null && cryptoSystem.getCryptoAlgorithm() != null) {
                brankoKeyButtons = initKeys(cryptoPanel, cryptoSystem, BRANKO_PRIVATE_KEY_LABEL, BRANKO_PUBLIC_KEY_LABEL,
                        BRANKO_PRIVATE_KEY, BRANKO_PUBLIC_KEY);
            }
            // initialize encrypt/create row
            initEncryptDecryptRow(cryptoPanel, decryptArea, cryptoSystem, inputOutputSelectButton, anaKeyButtons, brankoKeyButtons);
            if(cryptoSystem.getCryptoAlgorithm() != null) {
                addAsBoxRow(cryptoPanel, decryptArea);
            }
            addAsRow(cryptoSystem.getName(), cryptoPanel);
        });

    }

    private void initEncryptDecryptRow(JPanel cryptoPanel, JTextArea decryptArea, ICryptoSystem cryptoSystem, List<SelectButton> inputOutputSelectButton, Pair<SelectButton, SelectButton> anaKeyButtons, Pair<SelectButton, SelectButton> brankoKeyButtons) {
        EncryptButton encryptButton = new EncryptButton(this, cryptoSystem, inputOutputSelectButton, anaKeyButtons, brankoKeyButtons);
        DecryptButton decryptButton = new DecryptButton(this, decryptArea, cryptoSystem, inputOutputSelectButton, anaKeyButtons, brankoKeyButtons);
        addAsBoxRow(cryptoPanel, new ActionPanel(encryptButton, decryptButton));
    }


    private List<SelectButton> initInputFile(JPanel cryptoPanel, ICryptoSystem cryptoSystem) {
        boolean hasAdditionalSignature = cryptoSystem.getCryptoAlgorithm() != null && cryptoSystem.getSignatureChecker() != null;
        boolean isAsymOrSym = cryptoSystem instanceof SymmetricCryptoSystem || cryptoSystem instanceof AsymmetricCryptoSystem;

        JTextField inputTextField = new FrozenTextField(getPath(INPUT_FILE));
        SelectButton inputSelectButton = new SelectButton(this, inputTextField);
        OpenFileButton openFileButton = new OpenFileButton(this, inputSelectButton);

        JTextField outputTextField = new FrozenTextField(getPath(OUTPUT_FILE));
        SelectButton outputSelectButton = new SelectButton(this, outputTextField);
        OpenFileButton outputFileButton = new OpenFileButton(this, outputSelectButton);

        addAsBoxRow(cryptoPanel, new InputPanel(INPUT_FILE_LABEL, inputTextField, inputSelectButton, openFileButton));
        String outputLabel = isAsymOrSym ? OUTPUT_FILE_LABEL : cryptoSystem.getName() + ":";
        addAsBoxRow(cryptoPanel, new InputPanel(outputLabel, outputTextField, outputSelectButton, outputFileButton));

        if(hasAdditionalSignature) {
            JTextField sigTextField = new FrozenTextField(getPath(OUTPUT_ADDITIONAL));
            SelectButton sigSelectButton = new SelectButton(this, sigTextField);
            OpenFileButton sigFileButton = new OpenFileButton(this, sigSelectButton);

            addAsBoxRow(cryptoPanel, new InputPanel(ADDITIONAL_SIGNATURE_LABEL, sigTextField, sigSelectButton, sigFileButton));
            return new ArrayList<>(Arrays.asList(inputSelectButton, outputSelectButton, sigSelectButton));
        }

        return new ArrayList<>(Arrays.asList(inputSelectButton, outputSelectButton, null));
    }

    private Pair<SelectButton, SelectButton> initKeys(JPanel cryptoPanel, ICryptoSystem cryptoSystem, String privateKeyLabel, String publicKeyLabel,
                          String privateKeyFile, String publicKeyFile) {
        GenerateKeyButton generateKeyButton;

        //private button
        JTextField privateTextField = new FrozenTextField(getPath(privateKeyFile));
        SelectButton privateSelectButton = new SelectButton(this, privateTextField);
        OpenFileButton privateOpenFileButton = new OpenFileButton(this, privateSelectButton);

        //public button
        if(cryptoSystem.getKeyGenerator().hasPublicKey()) {
            JTextField publicTextField = new FrozenTextField(getPath(publicKeyFile));
            SelectButton publicSelectButton = new SelectButton(this, publicTextField);
            OpenFileButton publicOpenFileButton = new OpenFileButton(this, publicSelectButton);

            generateKeyButton = new GenerateKeyButton(this, privateSelectButton, publicSelectButton, cryptoSystem);

            addAsBoxRow(cryptoPanel, new InputPanel(privateKeyLabel, privateTextField,
                    privateSelectButton, privateOpenFileButton, generateKeyButton));
            addAsBoxRow(cryptoPanel, new InputPanel(publicKeyLabel, publicTextField,
                    publicSelectButton, publicOpenFileButton));
            return Pair.createPair(privateSelectButton, publicSelectButton);
        } else {
            generateKeyButton = new GenerateKeyButton(this, privateSelectButton, cryptoSystem);

            addAsBoxRow(cryptoPanel, new InputPanel(privateKeyLabel, privateTextField,
                    privateSelectButton, privateOpenFileButton, generateKeyButton));
            return Pair.createPair(privateSelectButton, null);
        }

    }

    public void addAsBoxRow(JPanel boxPanel, Component component) {
        boxPanel.add(component);
        boxPanel.add(Box.createRigidArea(new Dimension(0,20)));
    }


    public void initTabbedPane(Container pane) {
        tabbedPane = new JTabbedPane();
        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    public static JButton createButton(String name, AbstractAction action) {
        JButton jButton = new JButton(name);
        jButton.addActionListener(action);
        return jButton;
    }

    public void addAsRow(String name, Component component) {
        JPanel row = new JPanel(new BorderLayout());
        row.add(component, BorderLayout.NORTH);
        row.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabbedPane.add(name, row);
    }

    private static String getPath(String path) {
        return Paths.get(".", "data_files", path).normalize().toAbsolutePath().toString();
    }




}
