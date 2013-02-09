import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RenderBlockActionTest {
	public static void main(String[] args) {
		RenderBlockAction.SQUARE_DIMENSION_TRESHOLD = 14;
		RenderBlockAction.TOTAL_HEIGHT = 400;
		RenderBlockAction.TOTAL_WIDTH = 400;
		
		double xIncrement = -1 * (-1.0 - 1) / 400;
		double yIncrement = -1 * (-1.0 - 1) / 400;
		
		System.out.println(String.format("Increment x:%f y:%f", xIncrement, yIncrement));
		ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		List<RenderData> renderdata = fjp.invoke(new RenderBlockAction(-1.0, 1.0, -1.0, 1.0, 0, 400, 0, 400, 400, 400, xIncrement, yIncrement));
		System.out.println(renderdata.size());
	}
}
