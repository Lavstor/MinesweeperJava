package ru.academit.school.myskin.minesweeper.gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

class GameSettings {
    private JPanel gameSettingsPanel;
    private JButton okButton;
    private JButton defaultButton;
    private JButton customButton;
    private JButton cancel;
    private JButton backToBattlefield;
    private List<JButton> buttons = new LinkedList<>();

    private final JTextField heightField;
    private final JTextField weightField;
    private final JTextField mines;
    private final JRadioButton easy;
    private final JRadioButton normal;
    private final JRadioButton high;
    private final JPanel customPane;
    private final JPanel defaultPane;

    GameSettings() {
        createButtons();
        gameSettingsPanel = new JPanel();

        weightField = new JTextField(3);
        heightField = new JTextField(3);
        mines = new JTextField(2);

        easy = new JRadioButton("Easy", false);
        normal = new JRadioButton("Normal", true);
        high = new JRadioButton("High", false);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(easy);
        radioGroup.add(normal);
        radioGroup.add(high);

        JLabel weight = new JLabel("Enter width: ");
        weight.setLabelFor(weightField);

        JLabel height = new JLabel("Enter height: ");
        height.setLabelFor(heightField);

        JLabel mines = new JLabel("Enter mines count: ");
        mines.setLabelFor(this.mines);

        JComponent buttonPane = createButtonPanel();

        customPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        defaultPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        defaultPane.add(easy);
        defaultPane.add(normal);
        defaultPane.add(high);

        customPane.add(height);
        customPane.add(heightField);

        customPane.add(weight);
        customPane.add(weightField);

        customPane.add(mines);
        customPane.add(this.mines);

        gameSettingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        gameSettingsPanel.add(customPane);

        c.gridy = 1;
        c.insets = new Insets(25, 5, 5, 5);

        gameSettingsPanel.add(defaultPane);

        customPane.setVisible(false);

        gameSettingsPanel.add(buttonPane);
    }

    private JComponent createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 15, 5, 5);
        constraints.weighty = 1;

        constraints.gridx = 0;

        panel.add(customButton, constraints);
        panel.add(defaultButton, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 1;
        constraints.insets = new Insets(5, 5, 5, 5);

        panel.add(okButton, constraints);

        constraints.gridx = 2;

        defaultButton.setVisible(false);
        panel.add(cancel, constraints);
        constraints.gridx = 3;
        panel.add(backToBattlefield, constraints);

        return panel;
    }

    int[] createMap() {
        if (defaultPane.isVisible()) {
            if (easy.isSelected()) {
                return new int[]{9, 9, 10};
            }

            if (normal.isSelected()) {
                return new int[]{15, 15, 30};
            }

            if (high.isSelected()) {
                return new int[]{20, 20, 50};
            }
        } else {
            try {
                int weight = Integer.parseInt(weightField.getText());
                int height = Integer.parseInt(heightField.getText());
                int mines = Integer.parseInt(this.mines.getText());

                if (weight > 0 && weight < 25 && height > 0 && height < 25 && mines > 0 && mines <= (weight * height) * 0.5) {
                    return new int[]{Integer.parseInt(weightField.getText()), Integer.parseInt(heightField.getText()),
                            Integer.parseInt(this.mines.getText())};
                } else {
                    JOptionPane.showMessageDialog(gameSettingsPanel, "Error! Wrong options! Count of " +
                            "weight/height must be less then 25 and count of mines must be less then 50% of " +
                            "cells count", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(gameSettingsPanel, "Error! Use numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return null;
    }

    void customSetup() {
        defaultPane.setVisible(false);
        customPane.setVisible(true);

        defaultButton.setVisible(true);
        customButton.setVisible(false);
    }

    void defaultSetup() {
        defaultPane.setVisible(true);
        customPane.setVisible(false);

        customButton.setVisible(true);
        defaultButton.setVisible(false);
    }

    List<JButton> getButtons() {
        return buttons;
    }

    private void createButtons() {
        okButton = new JButton("OK");
        cancel = new JButton("CANCEL");
        customButton = new JButton("CUSTOM");
        defaultButton = new JButton("DEFAULT");
        backToBattlefield = new JButton("BACK");

        backToBattlefield.setVisible(false);

        buttons.add(okButton);
        buttons.add(cancel);
        buttons.add(customButton);
        buttons.add(defaultButton);
        buttons.add(backToBattlefield);

        okButton.setActionCommand("CREATE BATTLEFIELD");
        customButton.setActionCommand("CUSTOM");
        defaultButton.setActionCommand("DEFAULT");
        cancel.setActionCommand("BACK TO PASSWORD FROM OPTIONS");
        backToBattlefield.setActionCommand("BACK TO BATTLEFIELD FROM OPTIONS");
    }

    void setHideCancel(boolean hide) {
        if (hide) {
            cancel.setText("NEW PLAYER");
            backToBattlefield.setVisible(true);
        } else {
            cancel.setText("CANCEL");
            backToBattlefield.setVisible(false);
        }
    }

    JPanel getGameSettingsPanel() {
        return gameSettingsPanel;
    }
}
