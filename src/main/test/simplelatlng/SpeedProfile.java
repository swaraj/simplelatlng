package simplelatlng;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import simplelatlng.util.LengthUnit;
import simplelatlng.window.CircularWindow;
import simplelatlng.window.RectangularWindow;

/**
 * Basic benchmarking test. Repeats the critical functions
 * a bunch of times and reports how long that took. Also 
 * reports on approximate memory usage.
 * 
 * @author Tyler Coles
 */
public class SpeedProfile {

	private static final int NUMBER_OF_POINTS = 1000000;

	private static final NumberFormat integer = NumberFormat.getInstance();
	private static final NumberFormat decimal = new DecimalFormat("0.00");

	public static void main(String[] args) {
		SpeedProfile p = new SpeedProfile();

		System.out.println();

		p.profileEquals();
		p.profileDistance();
		p.profileRectangularWindow();
		p.profileCircularWindow();
	}

	private LatLng[] points;

	public SpeedProfile() {
		long memStart = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		points = new LatLng[NUMBER_OF_POINTS];
		for (int i = 0; i < points.length; i++) {
			points[i] = LatLng.random();
		}
		long memEnd = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();

		double mem = (double) (memEnd - memStart);

		System.out.printf("All test run against %s points.\n", integer
				.format(NUMBER_OF_POINTS));
		System.out.printf("Storage approximately %s MB.\n", decimal
				.format(mem / 1048576.0));
		System.out.printf("Averages %s bytes per LatLng.\n", decimal.format(mem
				/ (double) NUMBER_OF_POINTS));
	}

	private void profileEquals() {
		LatLng testPoint = LatLng.random();

		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			points[i].equals(testPoint);
		}
		Date end = new Date();

		System.out.printf("Tested equality in %s ms.\n", integer.format(end
				.getTime()
				- start.getTime()));
	}

	private void profileDistance() {
		LatLng testPoint = LatLng.random();

		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			LatLngTool.distance(testPoint, points[i], LengthUnit.KILOMETER);
		}
		Date end = new Date();

		System.out.printf("Calculated kilometer distances in %s ms.\n", integer
				.format(end.getTime() - start.getTime()));

		start = new Date();
		for (int i = 0; i < points.length; i++) {
			LatLngTool.distance(testPoint, points[i], LengthUnit.MILE);
		}
		end = new Date();

		System.out.printf("Calculated mile distances in %s ms.\n", integer
				.format(end.getTime() - start.getTime()));
	}

	private void profileRectangularWindow() {
		RectangularWindow window = new RectangularWindow(new LatLng(0, 0), 10, 10);

		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			window.contains(points[i]);
		}
		Date end = new Date();

		System.out.printf("RectangularWindow tested contains in %s ms.\n",
				integer.format(end.getTime() - start.getTime()));
	}

	private void profileCircularWindow() {
		CircularWindow window = new CircularWindow(new LatLng(0, 0), 10);

		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			window.contains(points[i]);
		}
		Date end = new Date();

		System.out.printf("CircularWindow tested contains in %s ms.\n", integer
				.format(end.getTime() - start.getTime()));
	}
}
