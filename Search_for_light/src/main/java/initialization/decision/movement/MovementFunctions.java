package initialization.decision.movement;
import swiftbot.*;

public class MovementFunctions {
	
	// Function to move left
	public static void left() {
		SwiftBotAPI sb1 = swiftbot.SwiftBotAPI.INSTANCE;
		int[] green = {0, 255, 0};
		sb1.setUnderlight(Underlight.FRONT_LEFT, green);
		sb1.setUnderlight(Underlight.FRONT_RIGHT, green);
		sb1.setUnderlight(Underlight.BACK_LEFT, green);
		sb1.setUnderlight(Underlight.BACK_RIGHT, green);
		sb1.move(-30, 30, 300);
		sb1.move(50, 50, 1000);
		sb1.disableUnderlights();
	}
	//Function to move straight
	public static void straight() {
		SwiftBotAPI sb2 = swiftbot.SwiftBotAPI.INSTANCE;int[] green = {0, 255, 0};
		sb2.setUnderlight(Underlight.FRONT_LEFT, green);
		sb2.setUnderlight(Underlight.FRONT_RIGHT, green);
		sb2.setUnderlight(Underlight.BACK_LEFT, green);
		sb2.setUnderlight(Underlight.BACK_RIGHT, green);
		sb2.move(50, 50, 1000);
		sb2.disableUnderlights();
	}
	// Function to move right
	public static void right() {
		SwiftBotAPI sb3 = swiftbot.SwiftBotAPI.INSTANCE;
		int[] green = {0, 255, 0};
		sb3.setUnderlight(Underlight.FRONT_LEFT, green);
		sb3.setUnderlight(Underlight.FRONT_RIGHT, green);
		sb3.setUnderlight(Underlight.BACK_LEFT, green);
		sb3.setUnderlight(Underlight.BACK_RIGHT, green);
		sb3.move(30, -30, 300);
		sb3.move(50, 50, 1000);
		sb3.disableUnderlights();
	}
}
