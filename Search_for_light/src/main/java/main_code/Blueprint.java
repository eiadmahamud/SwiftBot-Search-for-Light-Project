package main_code;
import java.util.*;
import initialization.decision.movement.Pixel_brightness;
import java.io.IOException;
import obstacle_detection.Obstacle_avoidance;
import obstacle_detection.Obstacle_detection;
import seek_light.Seek_light;
import swiftbot.*;

public class Blueprint {
	public static void main(String[] args) throws IOException {

		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		long startTime = System.currentTimeMillis(); // Record the time when the program starts
		int objectCount = 0; // Variable to store number of objects detected
		final long five_Minutes = 5 * 60 * 1000; // Variable to store 5 minutes 
		
		System.out.println("Welcome to SwiftBot Light-Seeking Program");
		System.out.println("Press Button A To Start");
		
		// Enabling button
		final boolean[] started = {false};
		sb.enableButton(Button.A, () -> {
		    System.out.println("Button A Pressed");
		    started[0] = true;
		});
		System.out.println("Waiting for user to press Button A...");
		while (!started[0]) {
		    try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // Small delay to avoid busy-waiting
		}

		
		System.out.println("Program starting...");
		System.out.println("Initiating Program.");
		
		// Storing initials intensities
		Pixel_brightness.pixelAnalysis();
		
		double initialLeftIntensity = Pixel_brightness.AvgLeft;
		double initialCentreIntensity = Pixel_brightness.AvgCentre;
		double initialRightIntensity = Pixel_brightness.AvgRight;

		System.out.println("Initial brightness values...");
		System.out.println("Left: " + initialLeftIntensity);
		System.out.println("Centre: " + initialCentreIntensity);
		System.out.println("Right: " + initialRightIntensity);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Main loop
		while (objectCount < 5 && (System.currentTimeMillis() - startTime) < five_Minutes) {
			
			// Capture and display intensities
			Pixel_brightness.pixelAnalysis();
			double left = Pixel_brightness.AvgLeft;
			double centre = Pixel_brightness.AvgCentre;
			double right = Pixel_brightness.AvgRight;
			System.out.println("Current Average Light Intensities In Each Column...");
			System.out.println("Left: " + left);
			System.out.println("Centre: " + centre);
			System.out.println("Right: " + right);
			
			

		} // main while loop ends here
		System.out.println("5 objects detected within 5 minutes");
		System.out.println("Enter TERMINATE to end program");

		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();

		while (!input.equals("TERMINATE")) {
			System.out.println("Invalid command. Please type TERMINATE");
			input = sc.nextLine();
		}
		System.out.println("Program terminated");
	}
}
