package ru.nsu.fit.g14201.lipatkin.gui;

import ru.nsu.fit.g14201.lipatkin.core.DatabaseEnterable;
import ru.nsu.fit.g14201.lipatkin.core.WrongUsernamePasswordException;

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

    public LoginFrame(final DatabaseEnterable dbEnter) {
        super("Oracle Database Enter");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension size = new Dimension(500, 300);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setBounds((width - size.width) / 2, (height - size.height) / 2, size.width, size.height);

        final JFrame currentFrame = this;
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dbEnter.enter(textField1.getText(), new String(passwordField1.getPassword()));
                } catch(WrongUsernamePasswordException exp) {
                    JOptionPane.showMessageDialog(currentFrame,
                            "You entered wrong username or password");
                }
            }
        });
    }
}
