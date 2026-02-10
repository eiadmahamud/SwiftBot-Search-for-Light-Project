package initialization.decision.movement;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class AnalyzeBrightness {
	
	static double thresholdLeft = -1;
	static double thresholdCentre = -1;
	static double thresholdRight = -1;
	
	public static void main(String[] args) throws Exception {

		BufferedImage img = ImageIO.read(new File("C:/CS/draft/TestImage.jpg"));

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
		double AvgLeft = LeftBrightness / LeftCount;
		double AvgCentre = CentreBrightness / CentreCount;
		double AvgRight = RightBrightness / RightCount;
		
		System.out.println("Left: " + AvgLeft);
		System.out.println("Centre: " + AvgCentre);
		System.out.println("Right: " + AvgRight);
		
		if (thresholdLeft == -1) {
			thresholdLeft = AvgLeft;
			thresholdCentre = AvgCentre;
			thresholdRight = AvgRight;
			System.out.println("Initial intensities stored as threshold intensities");
		}
	}
}
