package ru.academit.school.myskin.minesweeper.user;

import java.util.Arrays;

public class Player implements User {
    private double score;
    private char[] pass;

    static final long serialVersionUID = 1L;
    private final String name;

    public Player(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public Player(String name, char[] password) {
        this.name = name;
        this.pass = password;
    }

    public boolean checkPassword(char[] password) {
        return Arrays.equals(this.pass, password);
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        if (this.score < score) {
            this.score = score;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
