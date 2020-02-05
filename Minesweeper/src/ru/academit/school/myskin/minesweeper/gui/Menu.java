package ru.academit.school.myskin.minesweeper.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

class Menu {
    private JPanel menuPanel;
    private JButton[] buttons;
    private JLabel[] gifLabelArray;

    private static final String BUTTON_IMAGES_PASS = "Minesweeper/src/ru/academit/school/myskin/minesweeper/resources/buttonsImages/";

    Menu() {
        createButtons();

        menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 5;

        gifLabelArray = new JLabel[buttons.length * 2];
        JLabel[] blackLabelArray = new JLabel[gifLabelArray.length];

        ImageIcon blackIcon = new ImageIcon(BUTTON_IMAGES_PASS + "black.jpg");

        for (int i = 0; i < gifLabelArray.length; i++) {
            gifLabelArray[i] = createGifLabel();
            blackLabelArray[i] = new JLabel(blackIcon);
        }

        int j = 0;

        for (int i = 0; i < buttons.length; i++) {
            gridBagConstraints.gridy = i;

            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.EAST;

            menuPanel.add(gifLabelArray[j], gridBagConstraints);
            menuPanel.add(blackLabelArray[j], gridBagConstraints);
            j++;

            gridBagConstraints.gridx = 1;

            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            menuPanel.add(buttons[i], gridBagConstraints);

            gridBagConstraints.gridx = 2;

            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.WEST;

            menuPanel.add(gifLabelArray[j], gridBagConstraints);
            menuPanel.add(blackLabelArray[j], gridBagConstraints);
            j++;
        }

        menuPanel.setSize(500, 600);

        buttons[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                gifLabelArray[0].setVisible(true);
                gifLabelArray[1].setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                gifLabelArray[0].setVisible(false);
                gifLabelArray[1].setVisible(false);
            }
        });

        buttons[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                gifLabelArray[2].setVisible(true);
                gifLabelArray[3].setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                gifLabelArray[2].setVisible(false);
                gifLabelArray[3].setVisible(false);
            }
        });

        buttons[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                gifLabelArray[4].setVisible(true);
                gifLabelArray[5].setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                gifLabelArray[4].setVisible(false);
                gifLabelArray[5].setVisible(false);
            }
        });

        buttons[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                gifLabelArray[6].setVisible(true);
                gifLabelArray[7].setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                gifLabelArray[6].setVisible(false);
                gifLabelArray[7].setVisible(false);
            }
        });
    }

    private JLabel createGifLabel() {
        ImageIcon icon = new ImageIcon(BUTTON_IMAGES_PASS + "pentagramGif.gif");

        JLabel newLabel = new JLabel();
        icon.setImageObserver(newLabel);
        newLabel.setVisible(false);
        newLabel.setIcon(icon);

        return newLabel;
    }

    private void createButtons() {
        buttons = new JButton[]{new JButton("NEW GAME"), new JButton("RECORDS"), new JButton("INFO"), new JButton("EXIT")};

        Arrays.stream(buttons).forEach(b -> b.setActionCommand(b.getText()));
    }

    JButton[] getButtons() {
        return buttons;
    }

    void continueButton(boolean hide) {
        if (hide) {
            buttons[0].setActionCommand("CONTINUE");
            buttons[0].setText("CONTINUE");
        } else {
            buttons[0].setActionCommand("NEW GAME");
            buttons[0].setText("NEW GAME");
        }
    }

    JPanel getMenuPanel() {
        return menuPanel;
    }

    void setDefault() {
        Arrays.stream(gifLabelArray).forEach(gif -> gif.setVisible(false));
    }
}