import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class GuiController {
	@FXML
	private Button reset;
	
	@FXML
	private Button zoom;
	
	@FXML
	private Button render;
	
	@FXML
	private TextField complexA;
	
	@FXML
	private TextField complexB;
	
	@FXML
	private Pane renderPane;
	
	@FXML
	private TextField maxIters;
	
	@FXML
	private TextField border;
	
//	@FXML
	private ProgressBar renderProgress;
	
	private Node root;
	
	public GuiController(){
		final FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
		loader.setController(this);
		try {
			root = (Node) loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Node getRoot() {
		return root;
	}
	
	/**
	 * @return the reset
	 */
	public Button getReset() {
		return reset;
	}

	/**
	 * @param reset the reset to set
	 */
	public void setReset(Button reset) {
		this.reset = reset;
	}

	/**
	 * @return the zoom
	 */
	public Button getZoom() {
		return zoom;
	}

	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(Button zoom) {
		this.zoom = zoom;
	}

	/**
	 * @return the complexA
	 */
	public TextField getComplexA() {
		return complexA;
	}

	/**
	 * @param complexA the complexA to set
	 */
	public void setComplexA(TextField complexA) {
		this.complexA = complexA;
	}

	/**
	 * @return the complexB
	 */
	public TextField getComplexB() {
		return complexB;
	}

	/**
	 * @param complexB the complexB to set
	 */
	public void setComplexB(TextField complexB) {
		this.complexB = complexB;
	}

	/**
	 * @return the renderPane
	 */
	public Pane getRenderPane() {
		return renderPane;
	}

	/**
	 * @param renderPane the renderPane to set
	 */
	public void setRenderPane(Pane renderPane) {
		this.renderPane = renderPane;
	}

	/**
	 * @return the render
	 */
	public Button getRender() {
		return render;
	}

	/**
	 * @param render the render to set
	 */
	public void setRender(Button render) {
		this.render = render;
	}

	/**
	 * @return the maxIters
	 */
	public TextField getMaxIters() {
		return maxIters;
	}

	/**
	 * @param maxIters the maxIters to set
	 */
	public void setMaxIters(TextField maxIters) {
		this.maxIters = maxIters;
	}

	/**
	 * @return the border
	 */
	public TextField getBorder() {
		return border;
	}

	/**
	 * @param border the border to set
	 */
	public void setBorder(TextField border) {
		this.border = border;
	}

	/**
	 * @return the renderProgress
	 */
	public ProgressBar getRenderProgress() {
		return renderProgress;
	}

	/**
	 * @param renderProgress the renderProgress to set
	 */
	public void setRenderProgress(ProgressBar renderProgress) {
		this.renderProgress = renderProgress;
	}
}
