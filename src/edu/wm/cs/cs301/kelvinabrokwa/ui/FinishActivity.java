package edu.wm.cs.cs301.kelvinabrokwa.ui;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.FileOutputStream;

import javax.xml.transform.stream.StreamResult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.Constants;
import edu.wm.cs.cs301.kelvinabrokwa.falstad.MazeFileWriter;

public class FinishActivity extends ActionBarActivity {

	private boolean won;
	private String reason;
	private int pl, eu;
	
	private TextView message, lossReason, pathLength, energyUsed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish);
		
		Bundle extras = getIntent().getExtras();
		won = extras.getBoolean(PlayActivity.WON);
		reason = extras.getString(PlayActivity.REASON);
		pl = extras.getInt(PlayActivity.PATH_LENGTH);
		eu = extras.getInt(PlayActivity.ENERGY_USED);
		
		message = (TextView) findViewById(R.id.won_message);
		lossReason = (TextView) findViewById(R.id.loss_reason);
		pathLength = (TextView) findViewById(R.id.path_length);
		energyUsed = (TextView) findViewById(R.id.energy_used);
		
		Log.v("game over", won ? "won" : "lost");
		setText();
	}
	
	/**
	 * Sets the messages displayed on the final screen
	 */
	private void setText() {
		message.setText(won ? "You won!" : "You Lost!");
		lossReason.setText(reason);
		pathLength.setText("Path Length: " + pl);
		energyUsed.setText("Energy Used: " + eu);
	}
	
	/**
	 * on click listener for "Save Maze To File" button
	 */
	public void saveMazeToFile(View v) {
		Log.v("Save maze", "Saving maze to file");
		String filename = getFilesDir().toString() + "/maze_" + Globals.skill;
		MazeFileWriter.store(filename,
							 Constants.SKILL_X[Globals.skill],
							 Constants.SKILL_Y[Globals.skill],
							 Constants.SKILL_ROOMS[Globals.skill],
							 Constants.SKILL_PARTCT[Globals.skill],
							 Globals.root,
							 Globals.cells,
							 Globals.dists.dists,
							 Globals.startx,
							 Globals.starty);
	}
	
	/**
	 * back button leads the user to the start screen
	 */
	public void onBackPressed() {
		Intent i = new Intent(this, AMazeActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finish, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
