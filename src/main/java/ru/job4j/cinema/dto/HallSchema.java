package ru.job4j.cinema.dto;

/**
 * Scheme occupied places. Occupied place = true, unoccupied place = false;
 */
public class HallSchema {
    private final String name;
    private final boolean[][] arr;

    public HallSchema(String name, int row, int places) {
        this.name = name;
        this.arr = new boolean[row][places];
    }

    public String getName() {
        return name;
    }

    public boolean getValue(int x, int y) {
        return arr[x - 1][y - 1];
    }

    public void setValue(int x, int y, boolean value) {
        arr[x - 1][y - 1] = value;
    }
}
