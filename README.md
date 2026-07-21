SwiftBot Light-Seeking and Obstacle-Avoidance System:
This project implements an autonomous behaviour system for the SwiftBot robotics platform. The robot uses camera-based brightness analysis and ultrasound-based obstacle detection to navigate toward the brightest light source in a room while avoiding obstacles. The system logs all movement decisions, brightness readings, obstacle encounters, and captured images.

Overview:
The program runs for up to five minutes or until five obstacles have been detected. During execution, the robot continuously captures images, analyses brightness levels, moves toward the brightest region, and performs avoidance manoeuvres when obstacles are detected. A detailed log file is generated at the end of the run.

Features:
--Light Detection and Analysis
--Captures still images using the SwiftBot camera.
--Divides each image into left, centre, and right regions.
--Computes average brightness using a weighted RGB luminance formula.
--Determines the brightest direction and moves accordingly.
--Tracks:
----Highest brightness detected
----Per-cycle brightness values
----Historical brightness readings
--Autonomous Movement
----Moves left, right, or straight based on brightness.
----Implements wandering behaviour when no brighter light is detected.
----Uses underlights to indicate movement direction.
--Obstacle Detection and Handling
----Uses ultrasound to detect obstacles within 50 cm.
----Blinks red underlights when an obstacle is detected.
----Captures and stores an image of each obstacle.
----Performs avoidance manoeuvres (turn and forward movement).
----Logs obstacle count and image file locations.
--Execution Control
----Program begins when Button A is pressed.
----Program terminates only when the user enters the command TERMINATE.
--Logging
----A detailed log file is saved at:/data/home/pi/Log.txt
----The log includes:
------Initial brightness values
------Brightest light detected
------Movement history
------Light detection history
------Execution duration
------Number of obstacles detected
------Saved image locations

Core Logic:
--Startup
----Waits for Button A to be pressed.
----Captures initial brightness values.
----Sets timers and counters.
--Main Loop-Runs for a maximum of five minutes or until five obstacles are detected.

Each cycle:
--Capture image and compute brightness.
--Check ultrasound sensor.
----If an obstacle is detected, capture an image, perform avoidance, and log the event.
--Compare brightness values.
----If a brighter direction is found, move toward it.
----If not, perform wandering behaviour.
--Log all decisions and brightness readings.

Shutdown:
--Waits for the user to enter TERMINATE.
--Writes the full log file.
--Ends the program.

Requirements
--SwiftBot API (Java)
--Raspberry Pi 5 with SwiftBot hardware
--Java 17 or later
--Camera and ultrasound modules enabled
--Write permissions for /data/home/pi/

Running the Program
--Compile:javac MainCode.java
--Run:java MainCode
--Press Button A on the SwiftBot to begin.

Output Files:
Obstacle Images Stored as:
/data/home/pi/Object1.jpg
/data/home/pi/Object2.jpg
...

Brightness Test Image:
/data/home/pi/Light_test.jpg

Execution Log:
/data/home/pi/Log.txt

Terminal Heatmap Example:
The program prints a heatmap showing relative brightness values:

+============+============+============+
|    LEFT    |   CENTRE   |   RIGHT    |
+============+============+============+
|   ###      |     ###### |      ##    |
+  120.5     +   180.3    +   95.2     +
+============+============+============+

Purpose:
This project demonstrates autonomous decision-making, sensor integration, real-time image processing, robotics navigation strategies, and structured logging. It is suitable for coursework, robotics experimentation, and further development of autonomous robotic behaviours.