import javax.crypto.Cipher;    
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEncryptorDecryptor extends JFrame implements ActionListener {
    private JTextField keyField;
    private JTextField inputFileField;
    private JTextField outputFileField;
    private JButton selectInputButton;
    private JButton selectOutputButton;
    private JButton encryptButton;
    private JButton decryptButton;

    public FileEncryptorDecryptor() {
        setTitle("File Encryptor/Decryptor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        keyField = new JTextField(20);
        inputFileField = new JTextField(20);
        outputFileField = new JTextField(20);
        
        selectInputButton = new JButton("Select Input File");
        selectOutputButton = new JButton("Select Output File");
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");

        selectInputButton.addActionListener(this);
        selectOutputButton.addActionListener(this);
        encryptButton.addActionListener(this);
        decryptButton.addActionListener(this);

        add(new JLabel("Key:"));
        add(keyField);
        add(selectInputButton);
        add(inputFileField);
        add(selectOutputButton);
        add(outputFileField);
        add(encryptButton);
        add(decryptButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectInputButton) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                inputFileField.setText(selectedFile.getAbsolutePath());
            }
        } else if (e.getSource() == selectOutputButton) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                outputFileField.setText(selectedFile.getAbsolutePath());
            }
        } else if (e.getSource() == encryptButton) {
            try {
                encrypt(keyField.getText(), inputFileField.getText(), outputFileField.getText());
                JOptionPane.showMessageDialog(this, "File encrypted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error during encryption: " + ex.getMessage());
            }
        } else if (e.getSource() == decryptButton) {
            try {
                decrypt(keyField.getText(), inputFileField.getText(), outputFileField.getText());
                JOptionPane.showMessageDialog(this, "File decrypted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error during decryption: " + ex.getMessage());
            }
        }
    }

    private void encrypt(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    private void decrypt(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private void doCrypto(int cipherMode, String key, String inputFile, String outputFile) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) new File(inputFile).length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileEncryptorDecryptor app = new FileEncryptorDecryptor();
            app.setVisible(true);
        });
    }
}