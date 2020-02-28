package pacman.graphics;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pacman.game.GameConfiguration;

public class Renderer {
	private Stage mainStage;
	private Scene mainScene;
	private Canvas canvas;
	private GraphicsContext graphics;
	private int width, height;

	public Renderer(GameConfiguration conf, Stage primaryStage) {
		mainStage = primaryStage;
		mainStage.setTitle("Pacman");

		Group root = new Group();
		mainScene = new Scene(root);

		canvas = new Canvas(conf.getWindowWidth(), conf.getWindowHeight());
		root.getChildren().add(canvas);

		mainStage.setScene(mainScene);

		mainStage.setResizable(false);
		mainStage.show();


		width = conf.getWindowWidth();
		height = conf.getWindowHeight();
		
		this.graphics = canvas.getGraphicsContext2D();
		setCurrentColor(0xffffffff);
	}

	private void setCurrentColor(int color) {
	    double a = Pixel.getAlpha(color);
        double r = Pixel.getRed(color);
        double g = Pixel.getGreen(color);
        double b = Pixel.getBlue(color);
        this.graphics.setFill(Color.color(r, g, b, a));
	}

	public void drawImage(Image image, double offX, double offY) {
	    this.graphics.drawImage(image, offX, offY);
	}
	
	public void drawImage(Image img, double sx, double sy, double sw, double sh, 
	        double dx, double dy, double dw, double dh) {
        this.graphics.drawImage(img, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public void drawRectangle(double x, double y, double width, double height){
		this.graphics.fillRect(x, y, width, height);
	}

	public void cleanUp() {
		mainStage.close();
	}
}
