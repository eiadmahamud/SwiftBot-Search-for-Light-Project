package initialization;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BrightnessComparison {
	public static void main(String[] args) throws Exception {

		BufferedImage img = ImageIO.read(new File("/data/home/pi/TestImage.jpg"));

		int width = img.getWidth();
		int height = img.getHeight();

		int columnwidth = width / 3;

		int LeftBrightness = 0;
		int CentreBrightness = 0;
		int RightBrightness = 0;

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
		double Avgleft = LeftBrightness / LeftCount;
		double AvgCentre = CentreBrightness / CentreCount;
		double AvgRight = RightBrightness / RightCount;
	}
}
