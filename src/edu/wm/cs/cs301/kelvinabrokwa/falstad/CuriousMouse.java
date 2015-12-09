package edu.wm.cs.cs301.kelvinabrokwa.falstad;

import java.util.Random;
import android.os.Handler;
import android.util.Log;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.Robot.Direction;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.Robot.Turn;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.BasicRobot;

public class CuriousMouse implements RobotDriver{

	Robot robot;
	Handler handler = new Handler();
	int width = -1, height = -1;
	int[][] visits;
	boolean paused = false;
	
	@Override
	public void setRobot(Robot r) {
		robot = r;
	}

	@Override
	public void setDimensions(int w, int h) {
		visits = new int[w][h];
		width = w;
		height = h;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				visits[x][y] = 0;
			}
		}
	}

	@Override
	public void setDistance(Distance distance) { }
	
	boolean needsToMove = false;
	/**
	 * Creates a 2D array with the dimensions of the 
	 * maze and uses it to keep track of how many times the corresponding 
	 * cell is visited. It then chooses to go to the least visited cell.
	 */
	@Override
	public boolean drive2Exit() throws Exception
	{
		if (width == -1 || height == -1)
		{
			throw new Exception("Did not set maze dimensions.");
		}
		if (paused)
		{
			pause();
			return true;
		}
		if (needsToMove)
		{
			move(1);
			needsToMove = false;
			return true;
		}

		Random random = new Random();
		int[] currPos;
		int[] dirArr, nextCell = null;
		int min;
		Direction[] robotDirs =  { Direction.LEFT, Direction.RIGHT, Direction.FORWARD, Direction.BACKWARD };
		Direction absDir, nextDir = null;

		if (!robot.isAtGoal())
		{
			currPos = robot.getCurrentPosition();
			min = Integer.MAX_VALUE;
			for (int i = 0; i < robotDirs.length; i++)
			{
				if (robot.distanceToObstacle(robotDirs[i]) == 0) continue;
				absDir = ((BasicRobot)robot).normalizeDirection(robotDirs[i]);
				dirArr = ((BasicRobot)robot).dirs.get(absDir);
				nextCell = new int[] { currPos[0] + dirArr[0], currPos[1] + dirArr[1] };
				
				if (getVisits(nextCell) < min)
				{
					min = getVisits(nextCell);
					nextDir = robotDirs[i];
				}
				else if (getVisits(nextCell) == min && random.nextBoolean())
				{
					min = getVisits(nextCell);
					nextDir = robotDirs[i];
				}
			}
			if (nextDir == Direction.LEFT)
			{
				needsToMove = true;
				rotate(Turn.LEFT);
			}
			else if (nextDir == Direction.RIGHT)
			{
				needsToMove = true;
				rotate(Turn.RIGHT);
			}
			else if (nextDir == Direction.BACKWARD)
			{
				needsToMove = true;
				rotate(Turn.AROUND);
			} else {
				move(1);
			}
			visited(nextCell);
		}
		else if (robot.isAtGoal()
				&& robot.distanceToObstacle(Direction.FORWARD) != Integer.MAX_VALUE)
		{
			rotate(Turn.LEFT);
		}
		else if (robot.isAtGoal()
				&& robot.distanceToObstacle(Direction.FORWARD) == Integer.MAX_VALUE)
		{
			move(1);
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

	/**
	 * mark increment the number of times you've visited the given cell
	 * @param pos - position x, y
	 */
	private void visited(int[] pos) {
		visits[pos[0]][pos[1]] += 1;
	}
	
	/**
	 * Get the number of times you've been to a certain cell
	 * @param pos position
	 * @return number of times the robot has visited this given cell
	 */
	private int getVisits(int[] pos) {
		return visits[pos[0]][pos[1]];
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
