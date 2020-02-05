package ru.academit.school.myskin.minesweeper.gui;

import ru.academit.school.myskin.minesweeper.model.GameField;
import ru.academit.school.myskin.minesweeper.model.HighScoresReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

class BattleField {
    private GameField gameField;
    private int currentScore;
    private int cellsCount;
    private boolean gameOver;
    private JPanel battleFieldPanel;

    private JButton menu;
    private JButton topPanelNewGame;
    private List<JButton> buttons = new LinkedList<>();

    private final HighScoresReader reader;
    private final BufferedImage cellImage;
    private final BufferedImage crossImage;
    private final BufferedImage flagImage;
    private final JLabel score;
    private final JPanel gamePanel;
    private final JPanel topPanel;
    private final JLabel[][] cellLabels;
    private static final String IMAGES_DIRECTORY = "Minesweeper/src/ru/academit/school/myskin/minesweeper/resources/battlefield/";

    BattleField(int width, int height, int mines, HighScoresReader reader) {
        createButtons();
        this.reader = reader;
        this.battleFieldPanel = new JPanel();
        this.cellsCount = (width * height) - mines;

        topPanel = new JPanel();
        gamePanel = new JPanel();

        score = new JLabel("Your score: ");

        GridBagConstraints constraints1 = new GridBagConstraints();

        topPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints2 = new GridBagConstraints();
        constraints2.insets = new Insets(0, 5, 1, 0);
        constraints2.weightx = 4;
        constraints2.weighty = 1;
        constraints2.anchor = GridBagConstraints.WEST;

        constraints2.gridx = 0;

        topPanel.add(new JLabel(reader.getCurrentUser().getName()), constraints2);

        constraints2.weightx = 10;
        constraints2.gridx = 5;
        topPanel.add(score, constraints2);

        constraints2.insets = new Insets(0, 70, 0, 0);
        constraints2.fill = GridBagConstraints.BOTH;
        constraints2.gridx = 9;
        topPanel.add(topPanelNewGame, constraints2);

        constraints2.gridx = 10;
        topPanel.add(menu, constraints2);

        constraints1.weightx = 1;
        constraints1.weighty = 1;

        gamePanel.setLayout(new GridBagLayout());

        constraints1.anchor = GridBagConstraints.CENTER;
        constraints1.insets = new Insets(1, 1, 1, 1);
        cellLabels = new JLabel[height][width];

        String cellSkinPass = IMAGES_DIRECTORY + "cellSkin.jpg";

        cellImage = getBufferedImage(cellSkinPass);

        for (int i = 0; i < height; i++) {
            constraints1.gridy = i;

            for (int j = 0; j < width; j++) {
                constraints1.gridx = j;

                assert cellImage != null;
                cellLabels[i][j] = createCellLabel(cellImage);

                gamePanel.add(cellLabels[i][j], constraints1);
            }
        }

        crossImage = getBufferedImage(IMAGES_DIRECTORY + "notMine.png");
        flagImage = getBufferedImage(IMAGES_DIRECTORY + "flag.png");

        gamePanel.addMouseListener(new MouseAdapter() {
            boolean fieldCreated;

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                int i = mouseEvent.getY() / (gamePanel.getHeight() / height);
                int j = mouseEvent.getX() / (gamePanel.getWidth() / width);

                if (!gameOver) {
                    if (!fieldCreated) {
                        gameField = new GameField(height, width, mines, j, i);
                        fieldCreated = true;
                    }

                    LinkedList<Integer[]> cellCoordinatesList = new LinkedList<>();

                    if (i >= 0 && j >= 0 && i < cellLabels.length && j < cellLabels[i].length) {
                        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                            cellCoordinatesList = gameField.openCell(i, j);
                        } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                            gameField.markCell(i, j);
                            cellCoordinatesList.add(new Integer[]{i, j});
                        } else if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
                            cellCoordinatesList = gameField.massPush(i, j);
                        }
                    }

                    if (cellCoordinatesList != null) {
                        paintField(cellCoordinatesList);
                    }
                }
            }
        });

        battleFieldPanel.setVisible(true);

        gamePanel.setBackground(Color.black);

        battleFieldPanel.setLayout(new BorderLayout());
        battleFieldPanel.add(gamePanel, BorderLayout.CENTER);
        battleFieldPanel.add(topPanel, BorderLayout.NORTH);

        battleFieldPanel.repaint();
    }

    private void paintField(LinkedList<Integer[]> cellCoordinatesList) {
        while (!cellCoordinatesList.isEmpty()) {
            Integer[] coordinates = cellCoordinatesList.removeFirst();

            int height = coordinates[0];
            int width = coordinates[1];

            if (!gameField.cellIsHiddenCheck(height, width)) {
                if (gameField.cellIsMineCheck(height, width)) {
                    String pentagramPass = IMAGES_DIRECTORY + "mine.gif";
                    setGifIcon(cellLabels[height][width], pentagramPass);

                    gameOver = true;
                } else {
                    paintCrossCell(height, width);
                }
            } else {
                if (!gameField.cellIsMarkedCheck(height, width)) {
                    setIcon(cellLabels[height][width], flagImage);
                } else {
                    setIcon(cellLabels[height][width], cellImage);
                }
            }
        }

        currentScore = gameField.getScore();
        score.setText("Score " + currentScore);

        if (gameOver) {
            topPanel.setVisible(false);

            battleFieldPanel.remove(gamePanel);
            battleFieldPanel.add(lostPanel(), BorderLayout.CENTER);
        } else if (currentScore == cellsCount) {
            topPanel.setVisible(false);

            battleFieldPanel.remove(gamePanel);
            battleFieldPanel.add(winPanel(), BorderLayout.CENTER);

            gameOver = true;
        }
    }

    private void paintCrossCell(int i, int j) {
        assert crossImage != null;
        setIcon(cellLabels[i][j], crossImage);
        int nearMinesCount = gameField.getNearMines(i, j);

        if (nearMinesCount > 0) {
            JLabel centerLabel = new JLabel(String.valueOf(nearMinesCount), JLabel.CENTER);

            cellLabels[i][j].add(centerLabel);
            cellLabels[i][j].getComponent(0).setForeground(Color.RED);
            cellLabels[i][j].updateUI();
            cellLabels[i][j].setBackground(Color.BLACK);
        }
    }

    private JLabel createCellLabel(BufferedImage img) {
        JLabel newLabel = new JLabel();

        newLabel.setSize(40, 40);
        newLabel.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon(img.getScaledInstance(newLabel.getWidth(), newLabel.getHeight(), BufferedImage.SCALE_DEFAULT));
        newLabel.setIcon(icon);

        return newLabel;
    }

    private void setIcon(JLabel label, BufferedImage img) {
        ImageIcon icon = new ImageIcon(img.getScaledInstance(label.getWidth(), label.getHeight(), BufferedImage.SCALE_DEFAULT));
        label.setIcon(icon);
    }

    private void setGifIcon(JLabel label, String pass) {
        Image img = Toolkit.getDefaultToolkit().getImage(pass);
        ImageIcon icon = new ImageIcon(img);

        icon.setImageObserver(label);
        label.setIcon(icon);
    }

    private JPanel winPanel() {
        JPanel winPanel = new JPanel();

        JLabel image = new JLabel();
        image.setSize(gamePanel.getWidth(), gamePanel.getHeight());
        setGifIcon(image, IMAGES_DIRECTORY + "winImage.jpg");

        JLabel centerLabel = new JLabel("YOU WIN! SCORE: " + gameField.getScore(), JLabel.CENTER);
        centerLabel.setFont(new Font("Arial Black", Font.BOLD, 20));

        centerLabel.setSize(600, 200);
        centerLabel.setForeground(Color.BLACK);

        image.add(centerLabel);

        winPanel.add(image);
        winPanel.setBackground(Color.RED);
        image.updateUI();

        winPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                topPanel.setVisible(true);
                battleFieldPanel.remove(winPanel);

                battleFieldPanel.add(gamePanel, BorderLayout.CENTER);
                gamePanel.updateUI();

                try {
                    reader.updatePlayerData(currentScore);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(battleFieldPanel, "Cnt save ur score! Try to reboot game :(",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return winPanel;
    }

    private JPanel lostPanel() {
        battleFieldPanel.remove(gamePanel);
        JPanel lostPanel = new JPanel();

        JLabel image = new JLabel();
        image.setSize(gamePanel.getWidth(), gamePanel.getHeight());
        String cry = IMAGES_DIRECTORY + "looseImage.jpg";
        setGifIcon(image, cry);

        JLabel centerLabel = new JLabel("                          YOU LOST", JLabel.CENTER);
        centerLabel.setFont(new Font("Arial Black", Font.BOLD, 40));

        centerLabel.setSize(600, 200);
        centerLabel.setForeground(Color.RED);

        image.add(centerLabel);

        lostPanel.add(image);
        lostPanel.setBackground(Color.BLACK);
        image.updateUI();

        lostPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                topPanel.setVisible(true);
                battleFieldPanel.remove(lostPanel);

                battleFieldPanel.add(gamePanel, BorderLayout.CENTER);
                gamePanel.updateUI();
            }
        });

        return lostPanel;
    }

    private BufferedImage getBufferedImage(String pass) {
        try {
            return ImageIO.read(new File(pass));
        } catch (IOException ignored) {
        }

        return null;
    }

    List<JButton> getButtons() {
        return buttons;
    }

    private void createButtons() {
        menu = new JButton("MENU");
        menu.setActionCommand("BACK TO MENU FROM BATTLEFIELD");
        menu.setForeground(Color.BLACK);

        topPanelNewGame = new JButton("NEW GAME");
        topPanelNewGame.setActionCommand("BACK TO OPTIONS FROM BATTLEFIELD");

        buttons.add(menu);
        buttons.add(topPanelNewGame);
    }

    JPanel getBattleFieldPanel() {
        return battleFieldPanel;
    }
}

