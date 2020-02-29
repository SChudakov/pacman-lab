package pacman.algorithm;

import static pacman.algorithm.Direction.*;


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

    public Direction directionTo(Position pos) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (row != position.row) return false;
        return col == position.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}
