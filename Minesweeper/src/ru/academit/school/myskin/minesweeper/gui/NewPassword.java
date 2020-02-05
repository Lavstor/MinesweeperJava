package ru.academit.school.myskin.minesweeper.gui;

import ru.academit.school.myskin.minesweeper.model.HighScoresReader;
import ru.academit.school.myskin.minesweeper.user.Player;
import ru.academit.school.myskin.minesweeper.user.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class NewPassword {
    private HighScoresReader reader;
    private JPanel newPasswordPanel;
    private List<JButton> buttons = new LinkedList<>();

    private final JTextField nickNameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    NewPassword(HighScoresReader reader) {
        createButtons();
        this.reader = reader;

        newPasswordPanel = new JPanel();
        newPasswordPanel.setVisible(true);

        passwordField = new JPasswordField(10);
        confirmPasswordField = new JPasswordField(10);
        nickNameField = new JTextField(10);
        confirmPasswordField.setActionCommand("OK");

        JLabel enterPassword = new JLabel("Enter new password: ");
        enterPassword.setLabelFor(passwordField);

        JLabel confirmPassword = new JLabel("Confirm password: ");
        confirmPassword.setLabelFor(passwordField);

        JLabel enterNickName = new JLabel("Enter new Login: ");
        enterNickName.setLabelFor(nickNameField);

        JComponent buttonPane = createButtonPanel();

        JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        textPane.add(enterNickName);
        textPane.add(nickNameField);

        textPane.add(enterPassword);
        textPane.add(passwordField);

        textPane.add(confirmPassword);
        textPane.add(confirmPasswordField);

        newPasswordPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        newPasswordPanel.add(textPane, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(25, 5, 5, 5);

        newPasswordPanel.add(buttonPane, constraints);
    }

    private JComponent createButtonPanel() {
        GridLayout gridLayout = new GridLayout(0, 3);
        gridLayout.setHgap(30);

        JPanel panel = new JPanel(gridLayout);
        buttons.forEach(panel::add);

        return panel;
    }

    private void createButtons() {
        JButton okButton = new JButton("OK");
        JButton back = new JButton("BACK");
        JButton menu = new JButton("MENU");

        okButton.setActionCommand("CHECK DATA");
        back.setActionCommand("BACK TO PASSWORD");
        menu.setActionCommand("BACK TO MENU FROM NEW PASSWORD");

        buttons.add(back);
        buttons.add(okButton);
        buttons.add(menu);
    }

    boolean checkData() {
        char[] password = passwordField.getPassword();
        char[] confirmPassword = confirmPasswordField.getPassword();
        String login = nickNameField.getText();

        if (login.length() < 4 || password.length < 4) {
            JOptionPane.showMessageDialog(newPasswordPanel, "Too short login or password! Minimum" +
                    " length must be at least 4 chars", "Error Message", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        List<User> ourUser;
        ourUser = reader.getUsersList().stream().filter(x -> x.getName().equals(login)).collect(Collectors.toList());

        if (ourUser.size() != 0) {
            JOptionPane.showMessageDialog(newPasswordPanel, "This login already registered. Try another!",
                    "Error Message", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        if (!Arrays.equals(password, confirmPassword)) {
            JOptionPane.showMessageDialog(newPasswordPanel, "Passwords dnt match", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            Arrays.fill(password, '0');
            Arrays.fill(confirmPassword, '0');

            passwordField.selectAll();
            resetFocus();

            confirmPasswordField.selectAll();
            resetFocus();

            return false;
        }

        try {
            reader.writeUsers(new Player(login, password));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(newPasswordPanel, "Cnt save ur data! Reboot game!",
                    "Error Message", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void resetFocus() {
        passwordField.requestFocusInWindow();
    }

    void setDefault() {
        passwordField.setText("");
        confirmPasswordField.setText("");
        nickNameField.setText("");
    }

    List<JButton> getButtons() {
        return buttons;
    }

    JPanel getNewPasswordPanel() {
        return newPasswordPanel;
    }
}
