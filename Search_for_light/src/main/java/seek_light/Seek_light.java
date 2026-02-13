package seek_light;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import initialization.decision.movement.MovementFunctions;
import initialization.decision.movement.Pixel_brightness;
import swiftbot.*;
public class Seek_light {
	
	private static boolean firstrun = true;
	public static double initialLeft;
	public static double initialCentre;
	public static double initialRight;
	
	private static double prevLeft;
	private static double prevCentre;
	private static double prevRight;
	
	public static void seeklight() throws Exception {
		
		Pixel_brightness.pixelAnalysis();
		double left = Pixel_brightness.AvgLeft;
		double centre = Pixel_brightness.AvgCentre;
		double right = Pixel_brightness.AvgRight;
		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		
		if (firstrun) {
			initialLeft = left;
			initialCentre = centre;
			initialRight = right;
			
			prevLeft = left;
			prevCentre = centre;
			prevRight = right;
			
			firstrun = false;
			return;
		}
		
		boolean brighter = (left > prevLeft) || (centre > prevCentre) || (right > prevRight);
		
		if (brighter) {
			// Move towards brightest direction
			if (left > centre && left > right) {
				MovementFunctions.left();
			} else if (centre > left && centre > right) {
				MovementFunctions.straight();
			} else {
				MovementFunctions.right();
			}
		} else {
			// Wander around for light
			sb.move(40, -40, 1000);
		}
		// Update previous values
		prevLeft = left;
		prevCentre = centre;
		prevRight = right;
	}
}
