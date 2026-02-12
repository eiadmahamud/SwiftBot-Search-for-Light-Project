package obstacle_detection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import swiftbot.*;

public class Obstacle_detection {
		static int count = 0;
	public static double ObsAvoid() {
		
		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;	
		double distance = 999;
		
		
		try {
			distance = sb.useUltrasound();
		} catch (Exception e) {
			System.out.println("Ultrasound Error");
		}
		if (distance <= 50) {
			
			BlinkRed();
			System.out.println("Obstacle detected at " + distance + " cm");
			try {
				StoreImage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} return distance;
	}
	
	// Function to blink under lights to red
	public static void BlinkRed() {	
		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		for (int i = 0; i <=4; i++) {
			
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
	}
	public static void StoreImage() throws IOException {
		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		count++;
		BufferedImage img = sb.takeStill(ImageSize.SQUARE_720x720);
		String filename = "/data/home/pi/TestImage" + count +".jpg";
		ImageIO.write(img, "jpg", new File(filename));
	}
}
