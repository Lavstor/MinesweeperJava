package ru.academit.school.myskin.minesweeper.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

class Info {
    private JPanel infoPanel;
    private JButton back;
    private JLabel[] exitGifArray;

    private static final String BUTTONS_IMAGES_PASS = "Minesweeper/src/ru/academit/school/myskin/minesweeper/resources/buttonsImages/";

    Info() {
        createButtons();

        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());

        String text = "<html>MINESWEEPER<br><br>Game was crated by Myshkin Nikita Alekseevich." +
                "<br><br>Some tips how to beat this:<br><br> 1:" +
                " Click a square, you get a number.<br> That number is the number " +
                "of how many mines<br> are surrounding it.<br><br>2: Mark all the mines<" +
                "that are downright OBVIOUS. <br>Such as eight 1's surrounding a unopened<br>square, it's obviously a mine." +
                "<br><br>3: Finding the mines in 1 blocks helps a lot,<br> because it opens many squares and<br>" +
                " good hints to 2's and 3's.<br><br> Have fun!</html>";

        infoPanel.add(new JLabel(text, JLabel.CENTER), BorderLayout.CENTER);
        infoPanel.add(returnPanel(), BorderLayout.SOUTH);
    }

    private JLabel createGifLabel() {
        String pass = BUTTONS_IMAGES_PASS + "pentagramGif.gif";
        ImageIcon icon = new ImageIcon(pass);

        JLabel newLabel = new JLabel();
        icon.setImageObserver(newLabel);

        newLabel.setVisible(false);
        newLabel.setIcon(icon);

        return newLabel;
    }

    private JPanel returnPanel() {
        ImageIcon icon = new ImageIcon(BUTTONS_IMAGES_PASS + "black.jpg");
        JPanel panelExit = new JPanel();

        panelExit.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        exitGifArray = new JLabel[]{createGifLabel(), createGifLabel()};
        JLabel[] blackLabel = {new JLabel(icon), new JLabel(icon)};

        constraints.gridy = 0;
        constraints.gridx = 0;

        panelExit.add(exitGifArray[0], constraints);
        panelExit.add(blackLabel[0], constraints);

        constraints.gridx = 1;
        panelExit.add(back, constraints);
        constraints.gridx = 2;

        panelExit.add(exitGifArray[1], constraints);
        panelExit.add(blackLabel[1], constraints);

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                exitGifArray[0].setVisible(true);
                exitGifArray[1].setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                exitGifArray[0].setVisible(false);
                exitGifArray[1].setVisible(false);
            }
        });

        infoPanel.setVisible(true);

        return panelExit;
    }

    JButton getButton() {
        return back;
    }

    JPanel getPanel() {
        return infoPanel;
    }

    private void createButtons() {
        back = new JButton("BACK");
        back.setBorderPainted(false);
        back.setActionCommand("BACK TO MENU FROM INFO");
    }

    void setDefault() {
        Arrays.stream(exitGifArray).forEach(e -> e.setVisible(false));
    }
}
