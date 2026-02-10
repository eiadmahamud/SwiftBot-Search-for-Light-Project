package initialization;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BrightnessComparison {
	public static void main(String[] args) throws Exception {
		
		BufferedImage img = ImageIO.read(new File("/data/home/pi/TestImage.jpg"));
		
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int pixels = img.getRGB(x, y);
			}
		}

	}
}
