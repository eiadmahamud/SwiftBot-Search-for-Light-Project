package initialization.decision.movement;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class AnalyzeBrightness {
	
	// Variables declared out of main method to store threshold intensities
	static double thresholdLeft = -1;
	static double thresholdCentre = -1;
	static double thresholdRight = -1;
	
	public static void main(String[] args) throws Exception {
		
		BufferedImage img = ImageIO.read(new File("C:/CS/draft/TestImage.jpg"));

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
		
		System.out.println("Left: " + AvgLeft);
		System.out.println("Centre: " + AvgCentre);
		System.out.println("Right: " + AvgRight);
		
		// Used to store threshold values
		if (thresholdLeft == -1) {
			thresholdLeft = AvgLeft;
			thresholdCentre = AvgCentre;
			thresholdRight = AvgRight;
			System.out.println("Initial intensities stored as threshold intensities");
		}
		// Choose the highest direction with the highest intensity
		if (AvgLeft > AvgCentre && AvgLeft > AvgRight) {
			System.out.println("Avearge Light Intensities");
			System.out.println("Left: " + AvgLeft);
			System.out.println("Centre: " + AvgCentre);
			System.out.println("Right: " + AvgRight);
			System.out.println("Chosen Direction: Left");
			System.out.println("Movement Speed: ");
			MovementFunctions.left();
			
		} else if (AvgCentre > AvgLeft && AvgCentre > AvgRight) {
			System.out.println("Avearge Light Intensities");
			System.out.println("Left: " + AvgLeft);
			System.out.println("Centre: " + AvgCentre);
			System.out.println("Right: " + AvgRight);
			System.out.println("Chosen Direction: Centre");
			System.out.println("Movement Speed");
			MovementFunctions.straight();
		} else {
			System.out.println("Avearge Light Intensities");
			System.out.println("Left: " + AvgLeft);
			System.out.println("Centre: " + AvgCentre);
			System.out.println("Right: " + AvgRight);
			System.out.println("Chosen Direction: Right");
			System.out.println("Movement Speed: ");
			MovementFunctions.right();
		}
	}
}
