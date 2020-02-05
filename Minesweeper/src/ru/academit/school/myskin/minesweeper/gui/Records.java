package ru.academit.school.myskin.minesweeper.gui;

import ru.academit.school.myskin.minesweeper.model.HighScoresReader;
import ru.academit.school.myskin.minesweeper.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Records {
    private JPanel recordsPanel;
    private JButton back;

    private final JLabel[] exitGifArray;
    private static final String IMAGES_DIRECTORY = "Minesweeper/src/ru/academit/school/myskin/minesweeper/resources/";

    Records(HighScoresReader reader) {
        createButtons();
        recordsPanel = new JPanel();

        String pentagram = IMAGES_DIRECTORY + "highScoresImages/pentagram.png";
        String first = IMAGES_DIRECTORY + "highScoresImages/first.png";
        String second = IMAGES_DIRECTORY + "highScoresImages/second.gif";
        String third = IMAGES_DIRECTORY + "highScoresImages/third.gif";
        String fourth = IMAGES_DIRECTORY + "highScoresImages/fourth.png";
        String fifth = IMAGES_DIRECTORY + "highScoresImages/fifth.png";
        String sixth = IMAGES_DIRECTORY + "highScoresImages/sixth.png";
        String seventh = IMAGES_DIRECTORY + "highScoresImages/seventh.png";
        String eighth = IMAGES_DIRECTORY + "highScoresImages/eighth.png";
        String ninth = IMAGES_DIRECTORY + "highScoresImages/ninth.png";
        String tenth = IMAGES_DIRECTORY + "highScoresImages/tenth.png";
        ImageIcon blackImage = new ImageIcon(IMAGES_DIRECTORY + "buttonsImages/black.jpg");

        String[] imagesPassArray = {first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth};
        JLabel[] topPanel = {new JLabel("###"), new JLabel("NAME"), new JLabel("SCORE"), createGifLabel(pentagram)};

        GridBagConstraints constraints = new GridBagConstraints();

        recordsPanel.setLayout(new GridBagLayout());

        constraints.fill = GridBagConstraints.CENTER;
        constraints.weighty = 1;
        constraints.weightx = 5;
        constraints.gridy = 0;

        for (int i = 0; i < 4; i++) {
            constraints.gridx = i;

            recordsPanel.add(topPanel[i], constraints);
        }

        List<User> userList = reader.getUsersList();

        userList = userList.stream().sorted(Comparator.comparingDouble(User::getScore).reversed()).collect(Collectors.toList());

        for (int i = 1; i <= 10; i++) {
            constraints.gridy = i;
            constraints.gridx = 0;

            recordsPanel.add(new JLabel("#" + i), constraints);

            constraints.gridx = 1;
            recordsPanel.add(new JLabel(userList.get(i - 1).getName()), constraints);

            constraints.gridx = 2;
            recordsPanel.add(new JLabel(String.valueOf(userList.get(i - 1).getScore())), constraints);

            constraints.gridx = 3;
            recordsPanel.add(createGifLabel(imagesPassArray[i - 1]), constraints);
        }

        constraints.gridy++;
        constraints.gridx = 0;

        exitGifArray = new JLabel[]{createGifLabel(), createGifLabel()};

        constraints.anchor = GridBagConstraints.EAST;

        recordsPanel.add(exitGifArray[0], constraints);
        recordsPanel.add(new JLabel(blackImage), constraints);
        constraints.gridx = 3;

        constraints.anchor = GridBagConstraints.WEST;
        recordsPanel.add(exitGifArray[1], constraints);
        recordsPanel.add(new JLabel(blackImage), constraints);
        constraints.gridx = 1;

        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        recordsPanel.add(back, constraints);

        recordsPanel.setSize(350, 700);
        recordsPanel.setMinimumSize(new Dimension(350, 700));

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

        recordsPanel.setVisible(false);
    }

    JButton getBackButton() {
        return back;
    }

    private JLabel createGifLabel() {
        ImageIcon icon = new ImageIcon(IMAGES_DIRECTORY + "buttonsImages/pentagramGif.gif");

        JLabel newLabel = new JLabel();
        icon.setImageObserver(newLabel);

        newLabel.setVisible(false);
        newLabel.setIcon(icon);

        return newLabel;
    }

    private JLabel createGifLabel(String pass) {
        ImageIcon icon = new ImageIcon(pass);

        JLabel newLabel = new JLabel();
        icon.setImageObserver(newLabel);

        newLabel.setIcon(icon);

        return newLabel;
    }

    private void createButtons() {
        back = new JButton("BACK");
        back.setBorderPainted(false);
        back.setActionCommand("BACK TO MENU FROM RECORDS");
    }

    JPanel getRecordsPanel() {
        return recordsPanel;
    }
}