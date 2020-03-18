package pacman.algorithm;

import static pacman.algorithm.Direction.DOWN;
import static pacman.algorithm.Direction.LEFT;
import static pacman.algorithm.Direction.NONE;
import static pacman.algorithm.Direction.RIGHT;
import static pacman.algorithm.Direction.UP;


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

    public Position withDirection(Direction direction) {
        if (direction.equals(NONE)) {
            return this;
        } else if (direction.equals(LEFT)) {
            return Position.of(row, col - 1);
        } else if (direction.equals(UP)) {
            return Position.of(row - 1, col);
        } else if (direction.equals(RIGHT)) {
            return Position.of(row, col + 1);
        } else if (direction.equals(DOWN)) {
            return Position.of(row + 1, col);
        } else {
            error();
        }
        return null;
    }

    public Direction directionTo(Position position) {
        if (row == position.row) {
            if (col < position.col) {
                return RIGHT;
            } else if (col > position.col) {
                return LEFT;
            } else {
                error();
            }
        } else if (col == position.col) {
            if (row < position.row) {
                return DOWN;
            } else {
                return UP;
            }
        } else {
            System.out.println("this: " + this);
            System.out.println("to: " + position);
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

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
