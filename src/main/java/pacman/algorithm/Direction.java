package pacman.algorithm;

public enum Direction {
    UP {
        @Override
        Direction getOpposite() {
            return DOWN;
        }
    }, DOWN {
        @Override
        Direction getOpposite() {
            return UP;
        }
    }, RIGHT {
        @Override
        Direction getOpposite() {
            return LEFT;
        }
    }, LEFT {
        @Override
        Direction getOpposite() {
            return RIGHT;
        }
    }, NONE {
        @Override
        Direction getOpposite() {
            return NONE;
        }
    }, SAME {
        @Override
        Direction getOpposite() {
            return NONE;
        }
    };

    abstract Direction getOpposite();
}
