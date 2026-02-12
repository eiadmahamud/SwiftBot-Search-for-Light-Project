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
		ImageIO.write(img, "jpg", new File("/cs/fs/fw"));
		
		
	}
}
