package ru.academit.school.myskin.minesweeper.gui;

import ru.academit.school.myskin.minesweeper.model.HighScoresReader;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MineSweeper {
    private JFrame mainFrame;
    private Menu menu;
    private Records records;
    private Password password;
    private NewPassword newPassword;
    private GameSettings gameSettings;
    private Info info;
    private JPanel battleFieldPanel;
    private HighScoresReader reader;

    private static final String FRAME_IMAGE_PASS = "Minesweeper/src/ru/academit/school/myskin/minesweeper/resources/minesweeperFrame/";
    private static final ImageIcon IMAGE_ICON = new ImageIcon(FRAME_IMAGE_PASS + "exitGame.gif");

    public MineSweeper(String userListPass) {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            mainFrame.setBounds((screenSize.width - 900) / 2, (screenSize.height - 700) / 2, 900, 700);

            mainFrame.setLayout(new BorderLayout());
            customUI();

            mainFrame.setVisible(true);
            Image img = Toolkit.getDefaultToolkit().getImage(FRAME_IMAGE_PASS + "frameIcon.jpg");
            mainFrame.setIconImage(img);
            mainFrame.setTitle("Minesweeper");

            menu = new Menu();
            info = new Info();

            gameSettings = new GameSettings();
            gameSettings.getButtons().forEach(b -> b.addActionListener(this::actionPerformed));

            menu = new Menu();
            mainFrame.add(menu.getMenuPanel(), BorderLayout.CENTER);
            menu.getMenuPanel().updateUI();

            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            getUserList(userListPass);
            password = new Password(reader);
            newPassword = new NewPassword(reader);
            records = new Records(reader);
            getButtons();
        });
    }

    private void getUserList(String pass) {
        try {
            reader = new HighScoresReader(pass);
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            if (JOptionPane.showConfirmDialog(mainFrame, "Wrong file pass or data type! Create default save file?", "ERROR",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, IMAGE_ICON) == JOptionPane.YES_OPTION) {
                try {
                    reader = new HighScoresReader();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Game crushes :(", "Error Message", JOptionPane.ERROR_MESSAGE);
                    mainFrame.dispose();
                }
            } else {
                mainFrame.dispose();
            }
        }
    }

    private void customUI() {
        ArrayList<Object> gradients = new ArrayList<>();
        gradients.add(0.3);
        gradients.add(0.0);
        gradients.add(Color.RED);
        gradients.add(Color.RED);
        gradients.add(Color.BLACK);

        UIManager.put("RadioButton.background", new ColorUIResource(Color.RED));
        UIManager.put("RadioButton.foreground", new ColorUIResource(Color.BLACK));
        UIManager.put("RadioButton.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("Button.select", Color.RED);
        UIManager.put("Button.gradient", gradients);
        UIManager.put("Button.foreground", new ColorUIResource(Color.BLACK));
        UIManager.put("OptionPane.background", new ColorUIResource(Color.BLACK));
        UIManager.put("OptionPane.noButtonText", "NOPE");
        UIManager.put("OptionPane.yesButtonText", "YUP");
        UIManager.put("OptionPane.massage", new ColorUIResource(Color.RED));
        UIManager.put("Panel.background", new ColorUIResource(Color.BLACK));
        UIManager.put("Label.foreground", new ColorUIResource(Color.RED));
    }

    private void getButtons() {
        Arrays.stream(menu.getButtons()).forEach(b -> b.addActionListener(this::actionPerformed));
        password.getButtons().forEach(b -> b.addActionListener(this::actionPerformed));
        newPassword.getButtons().forEach(b -> b.addActionListener(this::actionPerformed));
        info.getButton().addActionListener(this::actionPerformed);
    }

    private void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("EXIT")) {
            if (JOptionPane.showConfirmDialog(mainFrame, "           YOU SHURE?", "EXIT",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, IMAGE_ICON) == JOptionPane.YES_OPTION) {
                mainFrame.dispose();
            }
        }

        if (command.equals("RECORDS")) {
            mainFrame.remove(menu.getMenuPanel());

            records = new Records(reader);
            records.getBackButton().addActionListener(this::actionPerformed);

            mainFrame.add(records.getRecordsPanel(), BorderLayout.CENTER);
            records.getRecordsPanel().setVisible(true);

            mainFrame.repaint();
            records.getRecordsPanel().updateUI();
        }

        if (command.equals("INFO")) {
            mainFrame.remove(menu.getMenuPanel());
            mainFrame.add(info.getPanel(), BorderLayout.CENTER);

            info.setDefault();
            mainFrame.repaint();
            info.getPanel().updateUI();
        }

        if (command.equals("NEW GAME")) {
            mainFrame.remove(menu.getMenuPanel());
            password.setDefault();
            mainFrame.add(password.getPasswordPanel(), BorderLayout.CENTER);
            password.getPasswordPanel().setVisible(true);
            mainFrame.repaint();
            password.getPasswordPanel().updateUI();
        }

        if (command.equals("TO MENU FROM PASSWORD")) {
            mainFrame.remove(password.getPasswordPanel());
            menu.setDefault();

            mainFrame.add(menu.getMenuPanel(), BorderLayout.CENTER);
            menu.getMenuPanel().updateUI();
            mainFrame.repaint();
        }

        if (command.equals("BACK TO MENU FROM NEW PASSWORD")) {
            mainFrame.remove(newPassword.getNewPasswordPanel());
            menu.setDefault();

            mainFrame.add(menu.getMenuPanel(), BorderLayout.CENTER);
            menu.getMenuPanel().updateUI();
            mainFrame.repaint();
        }

        if (command.equals("BACK TO MENU FROM INFO")) {
            mainFrame.remove(info.getPanel());
            menu.setDefault();

            mainFrame.add(menu.getMenuPanel(), BorderLayout.CENTER);
            menu.getMenuPanel().updateUI();
            mainFrame.repaint();
        }

        if (command.equals("BACK TO MENU FROM RECORDS")) {
            mainFrame.remove(records.getRecordsPanel());
            menu.setDefault();

            mainFrame.add(menu.getMenuPanel(), BorderLayout.CENTER);
            menu.getMenuPanel().updateUI();
            mainFrame.repaint();
        }

        if (command.equals("BACK TO PASSWORD")) {
            mainFrame.remove(newPassword.getNewPasswordPanel());
            password.setDefault();
            newPassword.setDefault();
            mainFrame.add(password.getPasswordPanel(), BorderLayout.CENTER);
            password.getPasswordPanel().updateUI();
        }

        if (command.equals("BACK TO PASSWORD FROM OPTIONS")) {
            mainFrame.remove(gameSettings.getGameSettingsPanel());
            password.setDefault();
            mainFrame.add(password.getPasswordPanel(), BorderLayout.CENTER);
            password.getPasswordPanel().updateUI();
        }

        if (command.equals("TO NEW PASSWORD FROM PASSWORD")) {
            mainFrame.remove(password.getPasswordPanel());
            mainFrame.add(newPassword.getNewPasswordPanel(), BorderLayout.CENTER);
            newPassword.getNewPasswordPanel().updateUI();
        }

        if (command.equals("TO OPTIONS FROM PASSWORD")) {
            if (password.checkPassword()) {
                mainFrame.remove(password.getPasswordPanel());

                gameSettings.defaultSetup();
                gameSettings.setHideCancel(false);

                mainFrame.add(gameSettings.getGameSettingsPanel(), BorderLayout.CENTER);
                gameSettings.getGameSettingsPanel().updateUI();
            }
        }

        if (command.equals("CHECK DATA")) {
            if (newPassword.checkData()) {
                password.setDefault();
                mainFrame.remove(newPassword.getNewPasswordPanel());
                mainFrame.add(password.getPasswordPanel(), BorderLayout.CENTER);
                password.getPasswordPanel().updateUI();
                newPassword.setDefault();
            }
        }

        if (command.equals("CREATE BATTLEFIELD")) {
            int[] fieldOptions = gameSettings.createMap();

            if (fieldOptions != null) {
                BattleField battlefield = new BattleField(fieldOptions[0], fieldOptions[1], fieldOptions[2], reader);
                battleFieldPanel = battlefield.getBattleFieldPanel();
                battlefield.getButtons().forEach(b -> b.addActionListener(this::actionPerformed));

                mainFrame.remove(gameSettings.getGameSettingsPanel());
                mainFrame.add(battleFieldPanel, BorderLayout.CENTER);
                battleFieldPanel.updateUI();
            }
        }

        if (command.equals("DEFAULT")) {
            gameSettings.defaultSetup();
        }

        if (command.equals("CUSTOM")) {
            gameSettings.customSetup();
        }

        if (command.equals("BACK TO MENU FROM BATTLEFIELD")) {
            mainFrame.remove(battleFieldPanel);
            menu.setDefault();
            mainFrame.add(menu.getMenuPanel(), BorderLayout.CENTER);
            menu.continueButton(true);
            menu.getMenuPanel().updateUI();
            mainFrame.repaint();
        }

        if (command.equals("CONTINUE")) {
            mainFrame.remove(menu.getMenuPanel());
            mainFrame.add(battleFieldPanel, BorderLayout.CENTER);
            battleFieldPanel.updateUI();
            menu.continueButton(false);
            mainFrame.repaint();
        }

        if (command.equals("BACK TO OPTIONS FROM BATTLEFIELD")) {
            mainFrame.remove(battleFieldPanel);

            mainFrame.add(gameSettings.getGameSettingsPanel(), BorderLayout.CENTER);
            gameSettings.setHideCancel(true);
            mainFrame.repaint();
            gameSettings.getGameSettingsPanel().updateUI();
        }

        if (command.equals("BACK TO BATTLEFIELD FROM OPTIONS")) {
            mainFrame.remove(gameSettings.getGameSettingsPanel());

            mainFrame.add(battleFieldPanel, BorderLayout.CENTER);
            gameSettings.setHideCancel(false);
            mainFrame.repaint();
        }
    }
}
