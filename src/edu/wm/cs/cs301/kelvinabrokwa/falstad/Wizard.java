package edu.wm.cs.cs301.kelvinabrokwa.falstad;

import android.os.Handler;
import android.util.Log;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.Robot.Direction;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.Robot.Turn;

public class Wizard implements RobotDriver {

	Robot robot;
	Distance dist;
	Handler handler = new Handler();
	boolean needToMove = false;
	boolean paused = false;
	
	@Override
	public void setRobot(Robot r) {
		robot = r;
	}

	@Override
	public void setDimensions(int width, int height) { }

	@Override
	public void setDistance(Distance distance) {
		dist = distance;
	}

	/**
	 * The robot checks in all directions that it can go and chooses 
	 * to go in the direction with the least distance to the goal. 
	 * When it reaches the goal it finds the direction in which 
	 * the distance to obstacle is Integer.MAX_VALUE and steps in that direction.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		if (paused) {
			pause();
			return true;
		}
		if (needToMove) {
			move(1);
			needToMove = false;
			return true;
		}
		int min;
		int[] currPos, dirArr, nextCell = null;
		Direction absDir, nextDir = null;
		Direction[] robotDirs =  { Direction.LEFT, Direction.RIGHT, Direction.FORWARD, Direction.BACKWARD };
		if (!robot.isAtGoal()) {
			currPos = robot.getCurrentPosition();
			min = Integer.MAX_VALUE;
			for (int i = 0; i < robotDirs.length; i++) {
				if (robot.distanceToObstacle(robotDirs[i]) == 0) continue;
				absDir = ((BasicRobot)robot).normalizeDirection(robotDirs[i]);
				dirArr = ((BasicRobot)robot).dirs.get(absDir);
				nextCell = new int[] { currPos[0] + dirArr[0], currPos[1] + dirArr[1] };
				if (dist.getDistance(nextCell[0], nextCell[1]) < min) {
					min = dist.getDistance(nextCell[0], nextCell[1]);
					nextDir = robotDirs[i];
				}
			}
			if (nextDir == Direction.LEFT)
			{
				rotate(Turn.LEFT);
				needToMove = true;
			}
			else if (nextDir == Direction.RIGHT)
			{
				rotate(Turn.RIGHT);
				needToMove = true;
			}
			else if (nextDir == Direction.BACKWARD)
			{
				rotate(Turn.AROUND);
				needToMove = true;
			}
			else
			{
				move(1);
			}
		}
		else if (robot.isAtGoal()
				&& robot.distanceToObstacle(Direction.FORWARD) != Integer.MAX_VALUE) {
			rotate(Turn.LEFT);
		}
		else if (robot.isAtGoal()
				&& robot.distanceToObstacle(Direction.FORWARD) == Integer.MAX_VALUE) {
			move(1);
		}
		return false;
	}

	private void move(int n) {
		final int num = n;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					robot.move(num);
					drive2Exit();
				} catch (Exception e) {
					Log.v("Exception", e.toString());
				}
			}
		}, 500);
	}
	
	private void rotate(Turn t) {
		final Turn turn = t;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					robot.rotate(turn);
					drive2Exit();
				} catch (Exception e) {
					Log.v("Exception", e.toString());
				}
			}
		}, 500);
	}
	
	private void pause() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					drive2Exit();
				} catch (Exception e) {
					Log.v("Exception", e.toString());
				}
			}
		}, 200);
	}

	@Override
	public float getEnergyConsumption() {
		return ((BasicRobot)robot).INITIAL_BATTERY_LEVEL -  robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		return ((BasicRobot)robot).getPathLength();
	}

	@Override
	public void togglePaused() {
		paused = !paused;
	}

}
