package edu.wm.cs.cs301.kelvinabrokwa.falstad;

import edu.wm.cs.cs301.kelvinabrokwa.falstad.Robot.Turn;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.Robot.Direction;

public class WallFollower implements RobotDriver {

	Robot robot;
	
	@Override
	public void setRobot(Robot r) {
		robot = r;
	}

	@Override
	public void setDimensions(int w, int h) {
		//
	}

	@Override
	public void setDistance(Distance distance) {
		//
	}

	/**
	 * while the robot is in the maze and not at the goals, if the robot has 
	 * a wall on its left and can go forward, step 1 forward. If the robot 
	 * has no wall on its left, turn left and move one forward until it has 
	 * a wall on its left (rounding corners). Exit the maze when it 
	 * gets to the goal position.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		while (!hasExited() && !robot.isAtGoal()) {
			while (robot.distanceToObstacle(Direction.LEFT) > 0 && !hasExited()) {
				robot.rotate(Turn.LEFT);
				robot.move(1);
			}
			while (robot.distanceToObstacle(Direction.FORWARD) == 0) {
				robot.rotate(Turn.RIGHT);
			}
			if (robot.distanceToObstacle(Direction.FORWARD) > 0 && !hasExited()) {
				robot.move(1);
			}
		}

		if (!hasExited()) {
			while (robot.distanceToObstacle(Direction.FORWARD) != Integer.MAX_VALUE) {
				robot.rotate(Turn.LEFT);
			}
			robot.move(1);
		}
		return true;
	}
	
	private boolean hasExited() {
		Maze maze = ((BasicRobot)robot).getMaze();
		int[] pos = new int[] { maze.px, maze.py };
		return pos[0] < 0 || pos[0] >= maze.mazew || pos[1] < 0 || pos[1] >= maze.mazeh;
	}

	@Override
	public float getEnergyConsumption() {
		return ((BasicRobot)robot).INITIAL_BATTERY_LEVEL -  robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		return ((BasicRobot) robot).getPathLength();
	}

}
