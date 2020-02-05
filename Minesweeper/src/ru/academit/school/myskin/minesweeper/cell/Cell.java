package ru.academit.school.myskin.minesweeper.cell;

public class Cell {
    private boolean isMine;
    private boolean isHidden;
    private boolean isMarked;
    private int nearMinesCount;

    public Cell(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isMine() {
        return isMine;
    }

    public void increaseNearMinesCount(int count) {
        nearMinesCount += count;
    }

    public void setAsMine() {
        this.isMine = true;
    }

    public int getMines() {
        return nearMinesCount;
    }

    public boolean isHidden() {
        return !isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
        setMarked(false);
    }

    public void setMarked(boolean mark) {
        isMarked = mark;
    }

    public boolean isMarked() {
        return !isMarked;
    }
}
