package initialization.decision.movement;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import swiftbot.ImageSize;
import swiftbot.SwiftBotAPI;

public class Pixel_brightness {
	public static int pixelAnalysis() throws IOException {

		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		BufferedImage img = sb.takeStill(ImageSize.SQUARE_720x720);
		ImageIO.write(img, "jpg", new File("c/cs/draft/Teasimage.jpg"));

		int width = img.getWidth();
		int height = img.getHeight();

		//!!!!!!!!!!!!!!
		int columnwidth = width / 3;

		// Variables to store brightness of each pixel
		double LeftBrightness = 0;
		double CentreBrightness = 0;
		double RightBrightness = 0;

		// Variables to count how many times a column had brighter pixels
		int LeftCount = 0;
		int CentreCount = 0;
		int RightCount = 0;

		// Scan every pixel for brightness in each column
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
		// Variables to store average brightness
		double AvgLeft = LeftBrightness / LeftCount;
		double AvgCentre = CentreBrightness / CentreCount;
		double AvgRight = RightBrightness / RightCount;
		
		// Determine which column is brightest
		if (AvgLeft > AvgCentre && AvgLeft > AvgRight) {
		    return 0;   // left
		} else if (AvgCentre > AvgLeft && AvgCentre > AvgRight) {
		    return 1;   // centre
		} else {
		    return 2;   // right
		}
	}
}
