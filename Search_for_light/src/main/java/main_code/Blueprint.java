package main_code;
import java.util.*;
import initialization.decision.movement.MovementFunctions;
import initialization.decision.movement.Pixel_brightness;
import java.io.FileWriter;
import java.io.IOException;
import obstacle_detection.ObstacleHandling;
import swiftbot.*;

public class Blueprint {
	public static void main(String[] args) throws IOException {
		
		SwiftBotAPI sb = swiftbot.SwiftBotAPI.INSTANCE;
		long startTime = System.currentTimeMillis(); // Record the time when the program starts
		int objectCount = 0; // Variable to store number of objects detected
		final long five_Minutes = 5 * 60 * 1000; // Variable to store 5 minutes 
		
		System.out.println("+-------------------------------------------+");
		System.out.println("| Welcome to SwiftBot Light-Seeking Program |");
		System.out.println("+-------------------------------------------+");
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
			} // Small delay to avoid busy-waiting
		}
		
		System.out.println("Initiating Program.");
		
		// Storing initials intensities
		Pixel_brightness.pixelAnalysis();
		
		double initialLeftIntensity = Pixel_brightness.AvgLeft;
		double initialCentreIntensity = Pixel_brightness.AvgCentre;
		double initialRightIntensity = Pixel_brightness.AvgRight;
		
		// Variables used for comparison in wandering process
		double prevBrightest = Pixel_brightness.Highest;
		double initialBrightest = prevBrightest;
		boolean firstCycle = true;
		
		// Logging variables
		double brightestEver = prevBrightest;
		int lightDetectionCount = 0;
		String lightDetectionHistory = "";
		String movementHistory = "";
		String savedImageLocations = "";
		
		System.out.println("-----------------------------------------------");
		System.out.println("|          Initial Brightness Levels          |");
		System.out.println("-----------------------------------------------");
		System.out.println(" Left   : " + initialLeftIntensity + "       ");
		System.out.println(" Centre : " + initialCentreIntensity + "     ");
		System.out.println(" Right  : " + initialRightIntensity + "      ");
		System.out.println("-----------------------------------------------");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Main loop that continues until 5 objects detected within 5 minutes
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
			
			double currentBrightest = Pixel_brightness.Highest;
			
			// Obstacle handling
			double distance = sb.useUltrasound();
			if (distance < 50) {
				String imgPath = ObstacleHandling.HandleObstacle();
				savedImageLocations += imgPath + "\n";
				System.out.println("WARNING! Obstacle Ahead At: " + distance + " cm!");
				objectCount++;
				
				if (left > right) {
				    System.out.println("Turning Left");
				    MovementFunctions.left();
				    movementHistory += "Obstacle Avoidance: Left (turn + 1s forward)\n";
				} else if (right > left) {
				    System.out.println("Turning Right");
				    MovementFunctions.right();
				    movementHistory += "Obstacle Avoidance: Right (turn + 1s forward)\n";
				} else {
				    if (Math.random() < 0.5) {
				        System.out.println("Turning Left (Random)");
				        MovementFunctions.left();
				        movementHistory += "Obstacle Avoidance: Left (Random)\n";
				    } else {
				        System.out.println("Turning Right (Random)");
				        MovementFunctions.right();
				        movementHistory += "Obstacle Avoidance: Right (Random)\n";
				    }
				}
				continue;
			}
			// Light-Seeking
			if (firstCycle) { // This is for the first time
				firstCycle = false;
			} else {
				if (currentBrightest <= prevBrightest) {
					System.out.println("No brighter light detected — wandering");
					if (Math.random() < 0.5) {
						System.out.println("Wandering Left");
						MovementFunctions.left();
						movementHistory+="Wander Left (turn + 1 sec forward)\n"; // logging variable
					} else {
						System.out.println("Wandering Right");
						MovementFunctions.right();
						movementHistory+="Wander Right (Turn + 1 sec forward)\n"; // logging variable
					}
					MovementFunctions.straight();
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
				MovementFunctions.left();
				movementHistory += "Left (turn + 1s forward)\n";
				
				lightDetectionCount++;
				lightDetectionHistory += currentBrightest + "\n";
				
				if (currentBrightest > brightestEver) {
				    brightestEver = currentBrightest;
				}
			}
			else if (centre > left && centre > right) {
				System.out.println("Moving Forward (Brightest)");
				MovementFunctions.straight();
				movementHistory += "Straight (1s)\n";
				
				lightDetectionCount++;
				lightDetectionHistory += currentBrightest + "\n";
				
				if (currentBrightest > brightestEver) {
				    brightestEver = currentBrightest;
				}
			}
			else if (right > left && right > centre) {
				System.out.println("Moving Right (Brightest)");
				MovementFunctions.right();
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
		
		System.out.println("5 objects detected within 5 minutes");
		System.out.println("Enter TERMINATE to end program");
		
		try (Scanner sc = new Scanner(System.in)) {
			String input = sc.nextLine();
			
			while (!input.equals("TERMINATE")) {
				System.out.println("Invalid command. Please type TERMINATE");
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
		
		System.out.println("Program terminated");
	}
}
