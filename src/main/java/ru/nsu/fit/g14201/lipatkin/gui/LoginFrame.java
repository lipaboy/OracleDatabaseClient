package ru.nsu.fit.g14201.lipatkin.gui;

import ru.nsu.fit.g14201.lipatkin.network.NetworkController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * Created by SPN on 30.04.2017.
 */
public class LoginFrame extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton enterButton;

    private NetworkController networkController;

    public LoginFrame() {
        super("Oracle Database Enter");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension size = new Dimension(500, 300);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setBounds((width - size.width) / 2, (height - size.height) / 2, size.width, size.height);

        networkController = new NetworkController();
        final JFrame currentFrame = this;
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(currentFrame, "Eggs are not supposed to be green.");
                //Connection connection =
            }
        });
    }
}
