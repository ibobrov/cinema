package ru.job4j.cinema.dto;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (HallSchema) o;
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        return Arrays.deepEquals(arr, that.arr);
    }

    @Override
    public int hashCode() {
        var result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.deepHashCode(arr);
        return result;
    }
}
