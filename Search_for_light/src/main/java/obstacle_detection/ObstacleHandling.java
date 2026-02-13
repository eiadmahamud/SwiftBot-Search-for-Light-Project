package obstacle_detection;
import swiftbot.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ObstacleHandling {

	static int Count = 0;

	public static void HandleObstacle() {

		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;

		// Blink under lights red
		for (int i = 0; i < 4; i++) {
			int[] red = {255, 0, 0};
			sb.setUnderlight(Underlight.MIDDLE_LEFT, red);
			sb.setUnderlight(Underlight.MIDDLE_RIGHT, red);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sb.disableUnderlights();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Capturing and storing the image
		BufferedImage img = sb.takeStill(ImageSize.SQUARE_720x720);
		Count++;
		String location = "/data/home/pi/TestImage" + Count +".jpg";
		try {
			ImageIO.write(img, "jpg", new File(location));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
