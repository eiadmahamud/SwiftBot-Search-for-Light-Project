package initialization.decision.movement;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import swiftbot.*;

public class Pixel_brightness {

	// These will store the brightness values after analysis
	public static double AvgLeft;
	public static double AvgCentre;
	public static double AvgRight;
	
	public static double Highest;
	public static double SecondHighest;
	public static double Lowest;

	public static void pixelAnalysis() throws IOException {

		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		BufferedImage img = sb.takeStill(ImageSize.SQUARE_720x720);
		ImageIO.write(img, "jpg", new File("c/cs/draft/Teasimage.jpg"));

		int width = img.getWidth();
		int height = img.getHeight();
		int columnwidth = width / 3;

		double LeftBrightness = 0;
		double CentreBrightness = 0;
		double RightBrightness = 0;

		int LeftCount = 0;
		int CentreCount = 0;
		int RightCount = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int pixels = img.getRGB(x, y);

				int r = (pixels >> 16) & 0xFF;
				int g = (pixels >> 8) & 0xFF;
				int b = pixels & 0xFF;

				double Brightness = 0.2126*r + 0.7152*g + 0.0722*b;

				if (x < columnwidth) {
					LeftBrightness += Brightness;
					LeftCount++;
				} else if (x < columnwidth * 2) {
					CentreBrightness += Brightness;
					CentreCount++;
				} else {
					RightBrightness += Brightness;
					RightCount++;
				}
			}
		}

		// Store the averages in the static variables
		AvgLeft = LeftBrightness / LeftCount;
		AvgCentre = CentreBrightness / CentreCount;
		AvgRight = RightBrightness / RightCount;
		
		if (AvgLeft > AvgCentre && AvgLeft > AvgRight) {
			Highest = AvgLeft;
			if (AvgCentre > AvgRight) {
				SecondHighest = AvgCentre;
				Lowest = AvgRight;
			} else {
				SecondHighest = AvgRight;
				Lowest = AvgCentre;
			}
		} else if (AvgCentre > AvgLeft && AvgCentre > AvgRight) {
			Highest = AvgCentre;
			if (AvgLeft > AvgRight) {
				SecondHighest = AvgLeft;
				Lowest = AvgRight;
			} else {
				SecondHighest = AvgRight;
				Lowest = AvgLeft;
			}
		} else {
			Highest = AvgRight;
			if (AvgCentre > AvgLeft) {
				SecondHighest = AvgCentre;
				Lowest = AvgLeft;
			} else {
				SecondHighest = AvgLeft;
				Lowest = AvgCentre;
			}
		}
	}
}