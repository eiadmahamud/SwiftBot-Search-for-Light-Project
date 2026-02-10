package initialization.decision.movement;
import swiftbot.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageCapture  {
	public static void main(String[] args) throws Exception {
		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		BufferedImage img = sb.takeStill(ImageSize.SQUARE_720x720);
		ImageIO.write(img, "jpg", new File("/data/home/pi/TestImage.jpg"));
		
		System.exit(1);
	}
}
