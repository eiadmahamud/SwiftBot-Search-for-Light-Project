import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import swiftbot.*;

public class Blueprint {
	public static void main(String[] args) throws IOException {

		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		long startTime = System.currentTimeMillis(); // Record the time when the program starts
		int objectCount = 0; // Variable to store number of objects detected
		final long five_Minutes = 5 * 60 * 1000; // Variable to store 5 minutes 

		System.out.println("+===========================================+");
		System.out.println("| Welcome to SwiftBot Light-Seeking Program |");
		System.out.println("+===========================================+");
		System.out.println();
		System.out.println("Press Button \"A\" To Start");

		// Enabling button
		final boolean[] started = {false};
		sb.enableButton(Button.A, () -> {
			System.out.println("Button A Pressed");
			started[0] = true;
		});

		while (!started[0]) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Initiating Program...");

		// Storing initials intensities
		pixelAnalysis();

		double initialLeftIntensity = AvgLeft;
		double initialCentreIntensity = AvgCentre;
		double initialRightIntensity = AvgRight;

		// Variables used for comparison in wandering process
		double prevBrightest = Highest;
		double initialBrightest = prevBrightest;
		boolean firstCycle = true;

		// Logging variables
		double brightestEver = prevBrightest;
		int lightDetectionCount = 0;
		String lightDetectionHistory = "";
		String movementHistory = "";
		String savedImageLocations = "";

		System.out.println("===============================================");
		System.out.println("|          Initial Brightness Levels          |");
		System.out.println("===============================================");
		System.out.println(" Left   : " + initialLeftIntensity);
		System.out.println(" Centre : " + initialCentreIntensity);
		System.out.println(" Right  : " + initialRightIntensity);
		System.out.println("===============================================");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Main loop that continues until 5 objects detected within 5 minutes
		while (objectCount < 5 && (System.currentTimeMillis() - startTime) < five_Minutes) {

			// Capture and display intensities
			pixelAnalysis();
			double left = AvgLeft;
			double centre = AvgCentre;
			double right = AvgRight;
			System.out.println("Current Average Light Intensities In Each Column...");
			System.out.println("Left: " + left);
			System.out.println("Centre: " + centre);
			System.out.println("Right: " + right);

			double currentBrightest = Highest;

			// Obstacle handling
			try {
				double distance = sb.useUltrasound();
				if (distance < 50) {
					String imgPath = HandleObstacle();
					savedImageLocations += imgPath + "\n";
					System.out.println("========================================");
					System.out.println("       WARNING: OBSTACLE AT " + distance);
					System.out.println("========================================");
					objectCount++;

					if (left > right) {
						System.out.println("Turning Left");
						moveLeft(); // Function called to move left
						movementHistory += "Obstacle Avoidance: Left (turn + 1s forward)\n";
					} else if (right > left) {
						System.out.println("Turning Right");
						moveRight(); // Function to move right
						movementHistory += "Obstacle Avoidance: Right (turn + 1s forward)\n";
					} else {
						if (Math.random() < 0.5) {
							System.out.println("Turning Left (Random)");
							moveLeft();
							movementHistory += "Obstacle Avoidance: Left (Random)\n";
						} else {
							System.out.println("Turning Right (Random)");
							moveRight();
							movementHistory += "Obstacle Avoidance: Right (Random)\n";
						}
					}
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Light-Seeking
			if (firstCycle) { // This is for the first time
				firstCycle = false;
			} else {
				if (currentBrightest <= prevBrightest) {
					System.out.println("No brighter light detected — wandering");
					if (Math.random() < 0.5) {
						System.out.println("Wandering Left");
						moveLeft();
						movementHistory+="Wander Left (turn + 1 sec forward)\n"; // logging variable
					} else {
						System.out.println("Wandering Right");
						moveRight();
						movementHistory+="Wander Right (Turn + 1 sec forward)\n"; // logging variable
					}
					moveStraight();
					movementHistory+="Wander Straight (1 sec)\n"; // logging variable
					// Update previous and continue to next cycle
					if (currentBrightest > brightestEver) {
						brightestEver = currentBrightest;
					}

					prevBrightest = currentBrightest;
					continue;
				}
			}
			// if current brightness more than previous one
			if (left > centre && left > right) {
				System.out.println("Moving Left (Brightest)");
				moveLeft();
				movementHistory += "Left (turn + 1s forward)\n";

				lightDetectionCount++;
				lightDetectionHistory += currentBrightest + "\n";

				if (currentBrightest > brightestEver) {
					brightestEver = currentBrightest;
				}
			}
			else if (centre > left && centre > right) {
				System.out.println("Moving Forward (Brightest)");
				moveStraight();
				movementHistory += "Straight (1s)\n";

				lightDetectionCount++;
				lightDetectionHistory += currentBrightest + "\n";

				if (currentBrightest > brightestEver) {
					brightestEver = currentBrightest;
				}
			}
			else if (right > left && right > centre) {
				System.out.println("Moving Right (Brightest)");
				moveRight();
				movementHistory += "Right (turn + 1s forward)\n";

				lightDetectionCount++;
				lightDetectionHistory += currentBrightest + "\n";

				if (currentBrightest > brightestEver) {
					brightestEver = currentBrightest;
				}
			}

			// Update previous brightness for next cycle
			prevBrightest = currentBrightest;

		} // main while loop ends here

		long endTime = System.currentTimeMillis(); // logging variable
		long duration = endTime - startTime; // logging variable

		System.out.println("=============== ALERT ===============");
		System.out.println("   5 Objects Detected in 5 Minutes   ");
		System.out.println("=====================================");
		System.out.println("Enter \"TERMINATE\" to end program");

		try (Scanner sc = new Scanner(System.in)) {
			String input = sc.nextLine();

			while (!input.equals("TERMINATE")) {
				System.out.println("Invalid command. Please type \"TERMINATE\"");
				input = sc.nextLine();
			}
		}

		// Logging
		try {
			FileWriter writer = new FileWriter("/data/home/pi/Log.txt");

			writer.write("===== SwiftBot Journey Log =====\n\n");

			writer.write("Initial Brightest Intensity: " + initialBrightest + "\n");

			writer.write("Initial Left Intensity: " + initialLeftIntensity + "\n");

			writer.write("Initial Centre Intensity: " + initialCentreIntensity + "\n");

			writer.write("Initial Right Intensity: " + initialRightIntensity + "\n\n");

			writer.write("Brightest Light Detected: " + brightestEver + "\n\n");

			writer.write("Light Detections: " + lightDetectionCount + "\n");

			writer.write(lightDetectionHistory + "\n");

			writer.write("Execution Duration: " + (duration / 1000) + " seconds\n\n");

			writer.write("Movement History:\n");

			writer.write(movementHistory + "\n");

			writer.write("Objects Detected: " + objectCount + "\n\n");

			writer.write("Saved Image Locations:\n");

			writer.write(savedImageLocations + "\n");

			writer.close();
			System.out.println("Log file saved as Log.txt");

		} catch (IOException e) {
			System.out.println("Error Writing Log File");
		}

		System.out.println("=================================");
		System.out.println("|        Program Terminated     |");
		System.out.println("=================================");
	}

	// Function to move left
	public static void moveLeft() {
		SwiftBotAPI sb = SwiftBotAPI.INSTANCE;
		int[] green = {0, 255, 0};

		sb.setUnderlight(Underlight.FRONT_LEFT, green);
		sb.setUnderlight(Underlight.FRONT_RIGHT, green);
		sb.setUnderlight(Underlight.BACK_LEFT, green);
		sb.setUnderlight(Underlight.BACK_RIGHT, green);

		sb.move(-30, 30, 300);
		sb.move(10, 10, 1000);

		sb.disableUnderlights();
	}
	// Function to move straight
	public static void moveStraight() {
		SwiftBotAPI sb = SwiftBotAPI.INSTANCE;
		int[] green = {0, 255, 0};

		sb.setUnderlight(Underlight.FRONT_LEFT, green);
		sb.setUnderlight(Underlight.FRONT_RIGHT, green);
		sb.setUnderlight(Underlight.BACK_LEFT, green);
		sb.setUnderlight(Underlight.BACK_RIGHT, green);

		sb.move(50, 50, 1000);

		sb.disableUnderlights();
	}
	// Function to move right
	public static void moveRight() {
		SwiftBotAPI sb = SwiftBotAPI.INSTANCE;
		int[] green = {0, 255, 0};

		sb.setUnderlight(Underlight.FRONT_LEFT, green);
		sb.setUnderlight(Underlight.FRONT_RIGHT, green);
		sb.setUnderlight(Underlight.BACK_LEFT, green);
		sb.setUnderlight(Underlight.BACK_RIGHT, green);

		sb.move(30, -30, 300);
		sb.move(10, 10, 1000);

		sb.disableUnderlights();
	}
	// Function to blink under lights to red upon detecting obstacle and capturing image of obstacle
	static int Count = 0;

	public static String HandleObstacle() {

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
		String location = "/data/home/pi/Object" + Count + ".jpg";
		try {
			ImageIO.write(img, "jpg", new File(location));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return location;
	}
	// Function to take image and store average of each & peak throughout the program
	public static double AvgLeft;
	public static double AvgCentre;
	public static double AvgRight;

	public static double Highest;
	public static double SecondHighest;
	public static double Lowest;

	public static double HighestLeft = 0;
	public static double HighestCentre = 0;
	public static double HighestRight = 0;

	public static void pixelAnalysis() throws IOException {

		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		BufferedImage img = sb.takeStill(ImageSize.SQUARE_720x720);
		ImageIO.write(img, "jpg", new File("/data/home/pi/Light_test.jpg"));

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

		AvgLeft = LeftBrightness / LeftCount;
		AvgCentre = CentreBrightness / CentreCount;
		AvgRight = RightBrightness / RightCount;

		if (AvgLeft > AvgCentre && AvgLeft > AvgRight) {
			Highest = AvgLeft;
			if (AvgCentre > AvgRight) {
				SecondHighest = AvgCentre;
				Lowest = AvgRight;
			} else {
				SecondHighest = AvgRight;
				Lowest = AvgCentre;
			}
		} else if (AvgCentre > AvgLeft && AvgCentre > AvgRight) {
			Highest = AvgCentre;
			if (AvgLeft > AvgRight) {
				SecondHighest = AvgLeft;
				Lowest = AvgRight;
			} else {
				SecondHighest = AvgRight;
				Lowest = AvgLeft;
			}
		} else {
			Highest = AvgRight;
			if (AvgCentre > AvgLeft) {
				SecondHighest = AvgCentre;
				Lowest = AvgLeft;
			} else {
				SecondHighest = AvgLeft;
				Lowest = AvgCentre;
			}
		}

		if (AvgLeft > HighestLeft) {
			HighestLeft = AvgLeft;
		}
		if (AvgCentre > HighestCentre) {
			HighestCentre = AvgCentre;
		}
		if (AvgRight > HighestRight) {
			HighestRight = AvgRight;
		}
	}
}
