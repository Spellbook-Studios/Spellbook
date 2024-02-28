package dk.sebsa.spellbook.util;

import lombok.Getter;

/**
 * Stores a type int in a 2d grid
 *
 * @author sebs
 * @since 1.0.0
 */
public class IntGrid {
    private final int[][] grid;
    @Getter
    private final int rows, cols;

    public IntGrid(int rows, int cols, int v) {
        this.rows = rows;
        this.cols = cols;

        grid = new int[rows][];
        for (int i = 0; i < rows; i++) {
            grid[i] = new int[cols];
            for (int j = 0; j < cols; j++) {
                grid[i][j] = v;
            }
        }
    }

    public void put(int row, int col, int v) {
        grid[Math.max(0, row)][Math.min(col, cols)] = v;
    }

    public int get(int row, int col) {
        return grid[Math.clamp(row, 0, rows)][Math.clamp(col, 0, cols)];
    }
}
