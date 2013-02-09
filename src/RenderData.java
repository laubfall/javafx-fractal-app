import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


public class RenderData {
	private final int x;
	
	private final int y;
	
	private Color paint;

	private WritableImage renderedImage;
	
	/**
	 * @param x
	 * @param y
	 * @param paint
	 */
	public RenderData(int x, int y, Color paint) {
		super();
		this.x = x;
		this.y = y;
		this.paint = paint;
	}

	/**
	 * @param x
	 * @param y
	 * @param renderedImage
	 */
	public RenderData(int x, int y, WritableImage renderedImage) {
		super();
		this.x = x;
		this.y = y;
		this.renderedImage = renderedImage;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the paint
	 */
	public Color getPaint() {
		return paint;
	}

	/**
	 * @return the renderedImage
	 */
	public WritableImage getRenderedImage() {
		return renderedImage;
	}
}
