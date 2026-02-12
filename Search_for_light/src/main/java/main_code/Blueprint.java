package main_code;
import java.util.*;
import java.io.IOException;
import obstacle_detection.Obstacle_avoidance;
import obstacle_detection.Obstacle_detection;
import swiftbot.*;

public class Blueprint {
	public static void main(String[] args) {

		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		long startTime = System.currentTimeMillis(); // Record the time when the program starts
		int objectCount = 0; // Variable to store number of objects detected

		final long five_Minutes = 5 * 60 * 1000; // Variable to store 5 minutes 

		while (objectCount < 5 && (System.currentTimeMillis() - startTime) < five_Minutes) {
			// From here, the obstacle detection and avoidance program starts
			double distance = Obstacle_detection.ObsAvoid();
			while (distance <= 50) {
				objectCount++;
				try {
					Obstacle_avoidance.avoidObstacle();
				} catch (IOException e) {
					System.out.println("Error during obstacle avoidance" + e.getMessage());
				}
				distance = Obstacle_detection.ObsAvoid();
			}
			// The obstacle detection and avoidance program ends


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
