package ru.academit.school.myskin.minesweeper.gui;

import ru.academit.school.myskin.minesweeper.model.HighScoresReader;
import ru.academit.school.myskin.minesweeper.user.User;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Password {
    private JPanel passwordPanel;
    private HighScoresReader reader;

    private List<JButton> buttons = new LinkedList<>();

    private final JTextField nickNameField;
    private final JPasswordField passwordField;

    Password(HighScoresReader reader) {
        createButtons();
        this.reader = reader;

        passwordPanel = new JPanel();

        passwordField = new JPasswordField(10);
        nickNameField = new JTextField(10);

        JLabel enterPassword = new JLabel("Password: ");
        enterPassword.setLabelFor(passwordField);

        JLabel enterNickName = new JLabel("Login: ");
        enterNickName.setLabelFor(nickNameField);

        JComponent buttonPanel = createButtonPanel();

        JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        textPane.add(enterNickName);
        textPane.add(nickNameField);

        textPane.add(enterPassword);
        textPane.add(passwordField);

        passwordPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        passwordPanel.add(textPane, constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(25, 5, 5, 5);

        passwordPanel.add(buttonPanel, constraints);
    }

    private JComponent createButtonPanel() {
        GridLayout gridLayout = new GridLayout(0, 3);
        gridLayout.setHgap(30);

        JPanel panel = new JPanel(gridLayout);

        buttons.forEach(panel::add);

        return panel;
    }

    boolean checkPassword() {
        List<User> users = reader.getUsersList();

        char[] input = passwordField.getPassword();
        String login = nickNameField.getText();
        List<User> ourUser;

        if (users.size() == 0) {
            JOptionPane.showMessageDialog(passwordPanel, "Invalid password or login. Try again.", "Error Message", JOptionPane.ERROR_MESSAGE);

            Arrays.fill(input, '0');

            passwordField.selectAll();
            resetFocus();

            return false;
        }

        ourUser = users.stream().filter(x -> x.getName().equals(login)).filter(x -> x.checkPassword(input)).collect(Collectors.toList());

        if (ourUser.size() != 0) {
            if (ourUser.get(0).checkPassword(input)) {
                reader.setCurrentUser(ourUser.get(0));

                return true;
            }
        } else {
            JOptionPane.showMessageDialog(passwordPanel, "Invalid password. Try again.", "Error Message", JOptionPane.ERROR_MESSAGE);

            Arrays.fill(input, '0');

            passwordField.selectAll();
            resetFocus();

            return false;
        }

        return false;
    }

    private void resetFocus() {
        passwordField.requestFocusInWindow();
    }

    private void createButtons() {
        JButton okButton = new JButton("OK");
        JButton newUser = new JButton("NEW USER");
        JButton menu = new JButton("MENU");

        okButton.setActionCommand("TO OPTIONS FROM PASSWORD");
        newUser.setActionCommand("TO NEW PASSWORD FROM PASSWORD");
        menu.setActionCommand("TO MENU FROM PASSWORD");

        buttons.add(okButton);
        buttons.add(newUser);
        buttons.add(menu);
    }

    List<JButton> getButtons() {
        return buttons;
    }

    JPanel getPasswordPanel() {
        return passwordPanel;
    }

    void setDefault() {
        nickNameField.setText("");
        passwordField.setText("");
    }
}
