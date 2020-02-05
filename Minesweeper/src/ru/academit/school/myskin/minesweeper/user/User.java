package ru.academit.school.myskin.minesweeper.user;

import java.io.Serializable;

public interface User extends Serializable {
    boolean checkPassword(char[] password);

    String getName();

    double getScore();

    void setScore(double score);
}
