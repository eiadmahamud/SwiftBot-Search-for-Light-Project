package initialization.decision.movement;
import swiftbot.*;

public class MovementFunctions {
	// Function to move left
	public static void left() {
		SwiftBotAPI sb1 = swiftbot.SwiftBotAPI.INSTANCE;
		sb1.move(-30, 30, 300);
		sb1.move(50, 50, 1000);
	}
	//Function to move straight
	public static void straight() {
		SwiftBotAPI sb2 = swiftbot.SwiftBotAPI.INSTANCE;
		sb2.move(50, 50, 1000);
	}
	// Function to move right
	public static void right() {
		SwiftBotAPI sb3 = swiftbot.SwiftBotAPI.INSTANCE;
		sb3.move(30, -30, 300);
		sb3.move(50, 50, 1000);
	}
}
