package pacman.algo;

import static pacman.algo.Direction.*;


public class Position {
    public int row;
    public int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position of(int row, int column) {
        return new Position(row, column);
    }

    public Direction dirTo(Position pos) {
        if (row == pos.row) {
            if (col < pos.col) {
                return RIGHT;
            } else if (col > pos.col) {
                return LEFT;
            } else {
                error();
            }
        } else if (col == pos.col) {
            if (row < pos.row) {
                return DOWN;
            } else {
                return UP;
            }
        } else {
            error();
        }
        return null;
    }

    private void error() {
        throw new RuntimeException();
    }
}
