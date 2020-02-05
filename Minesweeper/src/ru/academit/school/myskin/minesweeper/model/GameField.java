package ru.academit.school.myskin.minesweeper.model;

import ru.academit.school.myskin.minesweeper.cell.Cell;

import java.util.LinkedList;
import java.util.Random;

public class GameField {
    private Cell[][] battlefieldMap;
    private int countOfMines;
    private int currentScore;

    private final int height;
    private final int width;

    public GameField(int height, int width, int countOfMines, int firstX, int firstY) {
        this.height = height;
        this.width = width;
        this.countOfMines = countOfMines;
        battlefieldMap = new Cell[height][width];

        generateCellMap(firstX, firstY);
    }

    private void generateCellMap(int firstX, int firstY) {
        Random rnd = new Random();

        battlefieldMap = new Cell[this.height][this.width];

        int minesCount = 0;

        int[][] counts = new int[this.height][this.width];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                battlefieldMap[y][x] = new Cell(false);
            }
        }

        while (minesCount != countOfMines) {
            int x = rnd.nextInt(this.width);
            int y = rnd.nextInt(this.height);

            if (minesCount != countOfMines && x != firstX && y != firstY && !battlefieldMap[y][x].isMine()) {
                minesCount++;

                battlefieldMap[y][x].setAsMine();

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (y + i < counts.length && y + i >= 0 && j + x < counts[0].length && j + x >= 0) {
                            counts[y + i][x + j]++;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                battlefieldMap[i][j].increaseNearMinesCount(counts[i][j]);
            }
        }
    }

    public LinkedList<Integer[]> openCell(int height, int width) {
        if (battlefieldMap[height][width].isHidden()) {
            if (battlefieldMap[height][width].getMines() == 0) {
                return openAllZero(height, width);
            }

            if (battlefieldMap[height][width].isMine()) {
                return showBombs();
            }

            battlefieldMap[height][width].setHidden(true);

            currentScore++;

            LinkedList<Integer[]> cellList = new LinkedList<>();
            cellList.add(new Integer[]{height, width});

            return cellList;
        }

        return null;
    }

    public LinkedList<Integer[]> massPush(int height, int width) {
        if (!battlefieldMap[height][width].isMarked() || battlefieldMap[height][width].getMines() == 0 || !isReadyToMassPush(height, width)) {
            return null;
        }

        LinkedList<Integer[]> cellList = new LinkedList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isOutOfBounds(i, j, height, width) && battlefieldMap[height + i][width + j].isMarked()
                        && battlefieldMap[height + i][width + j].isHidden()) {
                    cellList.addAll(openCell(height + i, width + j));
                }
            }
        }

        return cellList;
    }

    private boolean isReadyToMassPush(int height, int width) {
        int countOfMarkedCells = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isOutOfBounds(i, j, height, width) && !battlefieldMap[height + i][width + j].isMarked()) {
                    countOfMarkedCells++;
                }
            }
        }

        return countOfMarkedCells == battlefieldMap[height][width].getMines();
    }

    private boolean isOutOfBounds(int i, int j, int height, int width) {
        return width + j < battlefieldMap[0].length && i + height < battlefieldMap.length && j + width >= 0 && i + height >= 0;
    }

    public void markCell(int height, int width) {
        if (battlefieldMap[height][width].isMarked()) {
            battlefieldMap[height][width].setMarked(true);
        } else {
            battlefieldMap[height][width].setMarked(false);
        }
    }

    private LinkedList<Integer[]> showBombs() {
        LinkedList<Integer[]> cellList = new LinkedList<>();

        for (int i = 0; i < battlefieldMap.length; i++) {
            for (int j = 0; j < battlefieldMap[i].length; j++) {
                if (battlefieldMap[i][j].isMine()) {
                    battlefieldMap[i][j].setHidden(true);

                    cellList.add(new Integer[]{i, j});
                }
            }
        }

        return cellList;
    }

    private LinkedList<Integer[]> openAllZero(int height, int width) {
        int currentScore = 0;

        LinkedList<Integer[]> cellQueue = new LinkedList<>();

        cellQueue.add(new Integer[]{height, width});

        int currentListIndex = 0;

        while (currentListIndex < cellQueue.size()) {
            width = cellQueue.get(currentListIndex)[1];
            height = cellQueue.get(currentListIndex)[0];

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (isOutOfBounds(i, j, height, width)) {
                        if (battlefieldMap[height + i][width + j].isHidden()) {
                            if (battlefieldMap[height + i][width + j].getMines() == 0) {
                                cellQueue.add(new Integer[]{height + i, width + j});
                            } else {
                                cellQueue.addFirst(new Integer[]{height + i, width + j});
                                currentListIndex++;
                            }
                            currentScore++;
                        }

                        battlefieldMap[height + i][width + j].setHidden(true);
                    }
                }
            }

            currentListIndex++;
            battlefieldMap[height][width].setHidden(true);
        }

        this.currentScore += currentScore;
        return cellQueue;
    }

    public boolean cellIsMineCheck(int height, int width) {
        return battlefieldMap[height][width].isMine();
    }

    public boolean cellIsHiddenCheck(int height, int width) {
        return battlefieldMap[height][width].isHidden();
    }

    public boolean cellIsMarkedCheck(int height, int width) {
        return battlefieldMap[height][width].isMarked();
    }

    public int getNearMines(int height, int width) {
        return battlefieldMap[height][width].getMines();
    }

    public int getScore() {
        return currentScore;
    }
}
