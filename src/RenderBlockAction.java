import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class RenderBlockAction extends RecursiveTask<List<RenderData>> {
	private static final long serialVersionUID = -8087072653338628291L;

	public static int TOTAL_WIDTH;

	public static int TOTAL_HEIGHT;

	public static int SQUARE_DIMENSION_TRESHOLD;

	private double xIncrement;
	
	private double yIncrement;
	
	private double xStart;

	private double xEnd;

	private double yStart;

	private double yEnd;

	private int pxXStart;

	private int pxXEnd;

	private int pxYStart;

	private int pxYEnd;

	private int width;

	private int height;

	public static double complexA = - 0.39;
	
	public static double complexB = 0.6;
	
	private List<RenderData> result = new ArrayList<>();

	private WritableImage wi;
	
	/**
	 * @param xStart
	 * @param xEnd
	 * @param yStart
	 * @param yEnd
	 * @param pxXStart
	 * @param pxXEnd
	 * @param pxYStart
	 * @param pxYEnd
	 * @param width
	 * @param height
	 */
	public RenderBlockAction(double xStart, double xEnd, double yStart,
			double yEnd, int pxXStart, int pxXEnd, int pxYStart, int pxYEnd,
			int width, int height, double xIncrement, double yIncrement) {
		super();
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
		this.pxXStart = pxXStart;
		this.pxXEnd = pxXEnd;
		this.pxYStart = pxYStart;
		this.pxYEnd = pxYEnd;
		this.width = width;
		this.height = height;
		this.xIncrement = xIncrement;
		this.yIncrement = yIncrement;
	}

	@Override
	protected List<RenderData> compute() {
		int squareDimXHigh = (int) Math.ceil(width / 2);
		int squareDimYHigh = (int) Math.ceil(height / 2);
		int squareDimXLow = (int) Math.floor(width / 2);
		int squareDimYLow = (int) Math.floor(height / 2);
		
		if (squareDimXHigh > SQUARE_DIMENSION_TRESHOLD
				|| squareDimYHigh > SQUARE_DIMENSION_TRESHOLD) {
			List<RenderBlockAction> renderActions = new ArrayList<>();

			double calcWidth = Math.abs(xEnd - xStart);
			double calcHeight = Math.abs(yEnd - yStart);
			
			// lo
			renderActions.add(new RenderBlockAction(xStart, xStart + calcWidth / 2, yStart, yStart + calcHeight / 2, pxXStart,
					pxXStart + squareDimXLow, pxYStart, pxYStart
							+ squareDimYLow, squareDimXLow, squareDimYLow, xIncrement, yIncrement));
			// ro
			renderActions.add(new RenderBlockAction(xStart + calcWidth / 2, xEnd, yStart, yStart + calcHeight / 2, pxXStart
					+ squareDimXHigh, pxXEnd, pxYStart, pxYStart
					+ squareDimYLow, squareDimXHigh, squareDimYLow, xIncrement, yIncrement));
			// lu
			renderActions.add(new RenderBlockAction(xStart, xStart + calcWidth / 2, yStart + calcHeight / 2, yEnd, pxXStart,
					pxXStart + squareDimXLow, pxYStart + squareDimYHigh,
					pxYEnd, squareDimXLow, squareDimYHigh, xIncrement, yIncrement));
			// ru
			renderActions.add(new RenderBlockAction(xStart + calcWidth / 2, xEnd, yStart + calcHeight / 2, yEnd, pxXStart
					+ squareDimXHigh, pxXEnd, pxYStart + squareDimYHigh,
					pxYEnd, squareDimXHigh, squareDimYHigh, xIncrement, yIncrement));
			
			invokeAll(renderActions);
			
			for(RenderBlockAction rba : renderActions){
				List<RenderData> join = rba.join();
				result.addAll(join);
			}
		} else {
			System.out.println(String.format("Thread %s calc for %d %d %d %d %f %f %f %f", Thread.currentThread().getId(), pxXStart,
					pxXEnd, pxYStart, pxYEnd, xStart, xEnd, yStart, yEnd));

			long currentTimeMillis = System.currentTimeMillis();
			int px = 0;
			int py = 0;
			
			double stepsX = Math.round(-1 * (xStart - xEnd) / xIncrement);
			double stepsY = Math.round(-1 * (yStart - yEnd) / yIncrement);
			
			wi = new WritableImage(width, height);
			BigDecimal bxStart = new BigDecimal(xStart);
			BigDecimal bxEnd = new BigDecimal(xEnd);
			BigDecimal byStart = new BigDecimal(yStart);
			BigDecimal byEnd = new BigDecimal(yEnd);
			BigDecimal bxIncrement = new BigDecimal(xIncrement);
			BigDecimal byIncrement = new BigDecimal(yIncrement);

			for (BigDecimal x = bxStart; x.compareTo(bxEnd) == -1; x = x.add(bxIncrement)) {
				for (BigDecimal y = byStart; y.compareTo(byEnd) == -1; y =  y.add(byIncrement)) {
					int[] z = calcZ(x.doubleValue(), y.doubleValue());
					if (z[1] > Juliamenge.border && px < stepsX && py < stepsY) {
						int loopCnt = z[0];
						wi.getPixelWriter().setColor(px, py, colorForLoopCnt(loopCnt));
					} else if(px < stepsX && py < stepsY){
						wi.getPixelWriter().setColor(px, py, Color.BLACK);
					} else {
						break;
					}
					py++;
				}
				py = 0;
				px++;
			}
			
			result.add(new RenderData(pxXStart, pxYStart, wi));
			System.out.println(String.format("Thread %s %dms %f stepsX %f stepsY %d resulting renderdata", Thread.currentThread().getId(), System.currentTimeMillis() - currentTimeMillis, stepsX, stepsY, result.size()));
		}

		return result;
	}

	private Color colorForLoopCnt(int loopCnt) {
		double block = 255 / Juliamenge.maxIterations;
		return Color.rgb((int) block * loopCnt, 0, 0);
	}

	private int[] calcZ(final double xOld, final double yOld) {
		int loopCnt = 0;
		int[] res = new int[2];
		double z = 0;
		double xNew = xOld;
		double yNew = yOld;
		do {
			double xTmp = Math.pow(xNew, 2) - Math.pow(yNew, 2) + complexA;
			double yTmp = 2 * xNew * yNew + complexB;

			xNew = xTmp;
			yNew = yTmp;
			z = Math.abs(Math.sqrt(Math.pow(xNew, 2) + Math.pow(yNew, 2)));

			loopCnt++;
			if ((loopCnt == Juliamenge.maxIterations && z <= Juliamenge.border)
					|| z > Juliamenge.border) {
				break;
			}
		} while (true);

		res[0] = loopCnt;
		res[1] = (int) z;
		return res;
	}
}