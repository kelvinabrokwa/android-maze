package edu.wm.cs.cs301.kelvinabrokwa.falstad;

import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

import edu.wm.cs.cs301.kelvinabrokwa.ui.Globals;

public class MazeBuilderEller extends MazeBuilder implements Runnable {

	/**
	 * Build a new maze randomly using Eller's algorithm for Maze generation
	 */
	public MazeBuilderEller() {
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	/**
	 * Build a new maze determinstically using Eller's algorithm for Maze generation
	 * @param det if this argument is `true`, the maze will be built deterministically
	 */
	public MazeBuilderEller(boolean det) {
		super(det);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	private int[][] sets;
	
	/**
	 * This method is the entry point for creating a Maze using Eller's algorithm,
	 * however, most of the magic happens in `joinSets`, `generateNext`, and `joinLastRow`
	 */
	@Override
	protected void generatePathways() {
		sets = new int[width][height];
		for (int i = 0; i < width; i++) sets[i][0] = i + 1; // populate the first row
		joinSets(0);
		for (int i = 1; i < height - 1; i++) { // iterate over rows
			populateNext(i);
			joinSets(i);
		}
		populateNext(height - 1); // last row
		joinLastRow();
	}

	
	/**
	 * randomly join adjacent sets of a given row
	 * @param idx the current row of the maze
	 */
	private void joinSets(int idx) {
		Random random = new Random();
		for (int i = 0; i < width - 1; i++) {
			if ((random.nextBoolean() || Globals.cells.isInRoom(i + 1, idx)) && Globals.cells.canGo(i, idx, 1, 0))
				breakWallHorizontal(i, idx, 1);
			if (i != 0 && !Globals.cells.canGo(i, idx, 0, 1)) {
				breakWallHorizontal(i, idx, -1);
				breakWallHorizontal(i, idx, 1);
			}
		}
	}
	
	/**
	 * A wrapper around deleteWall that deletes the wall between a cell
	 * and the cell to its right or left, also updates the sets 2D array in this
	 * data structure
	 * @param x row index of the cell
	 * @param y column index of the cell
	 * @param d direction, either 1 or -1
	 */
	private void breakWallHorizontal(int x, int y, int d) {
		// don't break walls between cells in the same set
		if (sets[x][y] == sets[x + d][y]) return;

		int oldSet, newSet;
		if (sets[x][y] < sets[x + d][y]) {
			oldSet  =  sets[x + d][y];
			newSet = sets[x][y];
			sets[x + d][y] = sets[x][y];
		} else {
			oldSet = sets[x][y];
			newSet = sets[x + d][y];
			sets[x][y] = sets[x + d][y];
		}

		// update all other elements in that set on that row
		for (int i = 0; i < width; i++)
			if (sets[i][y] == oldSet) sets[i][y] = newSet;

		Globals.cells.deleteWall(x, y, d, 0);
	}
	
	/**
	 * (1) For each row, create a HashMap that maps set ids<Integers> to the locations of the members 
	 * if the previous row (array indices) for example, a row the looks like:
	 *     [ 1, 1, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 6 ]
	 * should have a HashMap that looks like:
	 * {
	 *     1: [ 0, 1 ],
	 *     2: [ 2 ],
	 *     3: [ 4, 5, 6 ],
	 *     4: [ 7, 8, 9 , 10 ],
	 *     5: [ 11, 12 ],
	 *     6: [ 13 ]
	 * }
	 * (2) Then, generate a list of indices who's bottom walls should be knocked down
	 *     - shuffle each value in the hash map
	 *     - take the first n members of the shuffled list for some 1 <= n <= list.length (n is generated randomly)
	 * (3) Ensure that if the cell below a given cell is in a room and there is no boundary between,
	 *     also add it to the `toRemoveBottomWall` list
	 * (4) Remove the bottom walls at the indices contained in the `toRemoveBottomWall` list
	 * @param idx the index of the row to populate
	 */
	private void populateNext(int idx) {
		int prevIdx = idx - 1;
		int[] prev = new int[width];

		// copy the row into a local array
		for (int i = 0; i < width; i++) prev[i] = sets[i][prevIdx];

		// create a hash map that maps sets to their locations in the row
		HashMap<Integer, List<Integer>> hmap = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < prev.length; i++) {
			if (hmap.get(prev[i]) == null)
				hmap.put(prev[i], new ArrayList<Integer>());
			if (Globals.cells.canGo(i, prevIdx, 0, 1))
				hmap.get(prev[i]).add(i);
		}
		
		// iterate over our hash map of sets and choose a smart subset of them
		Random random = new Random();
		Collection<Integer> keys = hmap.keySet();
		List<Integer> toRemove = new ArrayList<Integer>();
		for (Object key: keys) {
			List<Integer> idxs = hmap.get(key);
			Collections.shuffle(idxs);
			int take = random.nextInt(idxs.size()) + 1;
			toRemove.addAll(hmap.get(key).subList(0, take));
			for (int i = 0; i < idxs.size(); i++) {
				// break all breakable entrances to rooms
				if (Globals.cells.isInRoom(idxs.get(i), idx) && !toRemove.contains(idxs.get(i)))
					toRemove.add(idxs.get(i));
			}
		}
		
		// delete walls in our `toRemove` list and combine sets appropriately
		for (int i = 0; i < toRemove.size(); i++) {
			Globals.cells.deleteWall(toRemove.get(i), prevIdx, 0, 1);
			sets[toRemove.get(i)][idx] = sets[toRemove.get(i)][prevIdx];
		}
		
		// assign the empty spots to new sets
		int setMax = max(idx);
		for (int i = 0; i < width; i++)
			if (sets[i][idx] == 0) {
				if (!Globals.cells.hasWallOnLeft(i, idx) && sets[i - 1][idx] != 0)
					sets[i][idx] = sets[i - 1][idx];
				else
					sets[i][idx] = ++setMax;
			}
	}
	
	/**
	 * Combine adjacent, disjoint sets on the last row of the maze
	 */
	private void joinLastRow() {
		int y = height - 1;
		for (int x = 0; x < width - 1; x++) {
			if (sets[x][y] != sets[x + 1][y] && Globals.cells.canGo(x, y, 1, 0)) {
				Globals.cells.deleteWall(x, y, 1, 0);
				sets[x + 1][y] = sets[x][y];
			}
		}
	}
	
	/**
	 * Returns the largest set number for a given row
	 * @param idx row index
	 * @return the largest int in the row
	 */
	private int max(int idx) {
		int max = sets[0][idx];
		for (int i = 0; i < width; i++)
			if (sets[i][idx] > max) max = sets[i][idx];
		return max;
	}
	
}