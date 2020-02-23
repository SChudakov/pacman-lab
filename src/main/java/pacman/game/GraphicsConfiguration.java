package pacman.game;

public class GraphicsConfiguration {
    private int windowWidth = 720;
    private int windowHeight = 396;
    private boolean clearScreen = true;
    private int rowNum = 11;
    private int columnNum = 20;

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public boolean isClearScreen() {
        return clearScreen;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public float getTileWidth() {
        return (float) windowWidth / columnNum;
    }

    public float getTileHeight() {
        return (float) windowHeight / rowNum;
    }

    public float getTileX(int columnNum) {
        return getTileWidth() * (columnNum - 1);
    }

    public float getTileY(int rowNum) {
        return getTileHeight() * (rowNum - 1);
    }
}
