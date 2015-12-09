package edu.wm.cs.cs301.kelvinabrokwa.falstad;

import edu.wm.cs.cs301.kelvinabrokwa.falstad.Robot.Turn;
import android.os.Handler;
import android.util.Log;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.Robot.Direction;

public class WallFollower implements RobotDriver {

	Robot robot;
	Handler handler = new Handler();
	boolean paused = false;
	
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
	private boolean needsToMove = false;

	/**
	 * while the robot is in the maze and not at the goals, if the robot has 
	 * a wall on its left and can go forward, step 1 forward. If the robot 
	 * has no wall on its left, turn left and move one forward until it has 
	 * a wall on its left (rounding corners). Exit the maze when it 
	 * gets to the goal position.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		if (paused) {
			pause();
			return true;
		}
		if (!hasExited() && !robot.isAtGoal()) {
			if (needsToMove)
			{
				move(1);
				needsToMove = false;
				return true;
			}
			if (robot.distanceToObstacle(Direction.LEFT) > 0 && !hasExited())
			{
				rotate(Turn.LEFT);
				needsToMove = true;
				return true;
			}
			if (robot.distanceToObstacle(Direction.FORWARD) == 0)
			{
				rotate(Turn.RIGHT);
				return true;
			}
			if (robot.distanceToObstacle(Direction.FORWARD) > 0 && !hasExited())
			{
				move(1);
			}
		}
		else if (!hasExited() && robot.isAtGoal()) {
			if (robot.distanceToObstacle(Direction.FORWARD) != Integer.MAX_VALUE)
			{
				rotate(Turn.LEFT);
				return true;
			}
			else if (robot.distanceToObstacle(Direction.FORWARD) == Integer.MAX_VALUE)
			{
				move(1);
			}
		}
		return true;
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

	@Override
	public void togglePaused() {
		paused = !paused;
	}

}
