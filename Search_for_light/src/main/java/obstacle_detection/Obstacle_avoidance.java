package obstacle_detection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import swiftbot.*;

public class Obstacle_avoidance {
	public static void avoidObstacle() throws IOException {

		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		BufferedImage img = sb.takeStill(ImageSize.SQUARE_720x720);
		ImageIO.write(img, "jpg", new File("/cs/fs/fw.jpg"));

		BufferedImage img1 = img;
		int height = img1.getHeight();
		int width = img1.getWidth();
		int widthColumn = width/3;

		int leftCount = 0;
		int rightCount = 0;

		double leftBrightness = 0;
		double rightBrightness = 0;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pixel = img1.getRGB(x, y);

				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = pixel & 0xFF;
				double brightness = 0.2126*r + 0.7152*g + 0.0722*b;

				if (x < widthColumn) {
					leftBrightness+=brightness;
					leftCount++;
				}
				if (x > 2 * widthColumn) {
					rightBrightness+=brightness;
					rightCount++;
				}
			}
		}
		double avgLeft = leftBrightness/leftCount;
		double avgRight = rightBrightness/rightCount;

		if (avgLeft > avgRight) {
			int[] green = {0, 255, 0};
			sb.setUnderlight(Underlight.FRONT_LEFT, green);
			sb.setUnderlight(Underlight.FRONT_RIGHT, green);
			sb.setUnderlight(Underlight.BACK_LEFT, green);
			sb.setUnderlight(Underlight.BACK_RIGHT, green);
			sb.move(-30, 30, 300);
			sb.move(50, 50, 1000);
			sb.disableUnderlights();
		} else {
			int[] green = {0, 255, 0};
			sb.setUnderlight(Underlight.FRONT_LEFT, green);
			sb.setUnderlight(Underlight.FRONT_RIGHT, green);
			sb.setUnderlight(Underlight.BACK_LEFT, green);
			sb.setUnderlight(Underlight.BACK_RIGHT, green);
			sb.move(30, -30, 300);
			sb.move(50, 50, 1000);
			sb.disableUnderlights();
		}
	}
}

