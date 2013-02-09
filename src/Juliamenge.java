import java.util.List;
import java.util.concurrent.ForkJoinPool;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Juliamenge extends Application {
	public static int maxIterations = 200;

	public static int border = 10;

	private double canvasWidth = 600;

	private double canvasHeight = 600;

	private double lastXIncrement;

	private double lastYIncrement;

	private static final double XMIN = -1.5;

	private static final double XMAX = 1.5;

	private static final double YMIN = -1.5;

	private static final double YMAX = 1.5;

	private double xmin = XMIN;

	private double xmax = XMAX;

	private double ymin = YMIN;

	private double ymax = YMAX;

	@Override
	public void start(Stage primaryStage) {
		final Canvas c = new Canvas(canvasWidth, canvasHeight);
		c.setTranslateZ(3);

		final GuiController gui = new GuiController();
		gui.getRenderPane().getChildren().add(c);
		
		Group g1 = new Group();
		g1.getChildren().add(gui.getRoot());

		final Rectangle r = new Rectangle();
		r.setWidth(100);
		r.setHeight(100);
		r.setStroke(Color.RED);
		r.setStrokeWidth(4);
		r.setFill(Color.TRANSPARENT);
		r.setTranslateZ(10);
		r.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				r.setX(event.getX());
				r.setY(event.getY());
			}
		});
		
		gui.getRenderPane().getChildren().add(r);
		gui.getComplexA().setText(String.valueOf(RenderBlockAction.complexA));
		gui.getComplexB().setText(String.valueOf(RenderBlockAction.complexB));
		
		gui.getRender().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				PixelWriter pixelWriter = c.getGraphicsContext2D().getPixelWriter();
				
				try {
					Double.valueOf(gui.getComplexA().getText());
				} catch (Exception e){
					gui.getComplexA().setStyle("-fx-background-color: red;");
					return;
				}
				
				try {
					Double.valueOf(gui.getComplexB().getText());
				} catch (Exception e){
					gui.getComplexB().setStyle("-fx-background-color: red;");
					return;
				}
				
				RenderBlockAction.complexA = Double.valueOf(gui.getComplexA().getText());
				RenderBlockAction.complexB = Double.valueOf(gui.getComplexB().getText());
				gui.getComplexA().setStyle("");
				gui.getComplexB().setStyle("");
				maxIterations = Integer.decode(gui.getMaxIters().getText());
				border = Integer.decode(gui.getBorder().getText());
				paintFractale(pixelWriter, xmin, xmax, ymin, ymax, c.getGraphicsContext2D());
			}
		});
		
		gui.getZoom().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				// neu berechnen von xmin, xmax, ymin, ymax
				double txmin = lastXIncrement * (r.getX()) + xmin;
				double txmax = (lastXIncrement * (r.getX() + r.getWidth())) + xmin;
				double tymin = lastYIncrement * r.getY() + ymin;
				double tymax = (lastYIncrement * (r.getY() + r.getHeight())) + ymin;
				paintFractale(c.getGraphicsContext2D().getPixelWriter(), txmin,
						txmax, tymin, tymax, c.getGraphicsContext2D());
				
				xmax = txmax;
				xmin = txmin;
				ymax = tymax;
				ymin = tymin;
			}
		});

		gui.getReset().setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				xmin = XMIN;
				xmax = XMAX;
				ymin = YMIN;
				ymax = YMAX;
				paintFractale(c.getGraphicsContext2D().getPixelWriter(), XMIN,
						XMAX, YMIN, YMAX, c.getGraphicsContext2D());
			}
		});

		gui.getMaxIters().setText(String.valueOf(maxIterations));
		gui.getBorder().setText(String.valueOf(border));
		
		Scene s1 = new Scene(g1);
		primaryStage.setScene(s1);
		primaryStage.sizeToScene();
		primaryStage.show();
//		primaryStage.setResizable(false);
	}

	private void paintFractale(final PixelWriter pixelWriter,
			final double xmin, final double xmax, final double ymin,
			final double ymax, GraphicsContext graphicsContext) {
		double xIncrement = lastXIncrement = -1 * (xmin - xmax) / canvasWidth;
		double yIncrement = lastYIncrement = -1 * (ymin - ymax) / canvasHeight;

		ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime()
				.availableProcessors());
		RenderBlockAction.SQUARE_DIMENSION_TRESHOLD =48;
		RenderBlockAction.TOTAL_HEIGHT = (int) canvasHeight;
		RenderBlockAction.TOTAL_WIDTH = (int) canvasWidth;
		List<RenderData> invoke = fjp.invoke(new RenderBlockAction(xmin, xmax, ymin, ymax, 0, (int)canvasWidth, 0, (int)canvasHeight, (int)canvasWidth, (int)canvasHeight, xIncrement, yIncrement));
		
		for(RenderData rd : invoke){
			graphicsContext.drawImage(rd.getRenderedImage(), rd.getX(), rd.getY());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
