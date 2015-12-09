package edu.wm.cs.cs301.kelvinabrokwa.ui;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AMazeActivity extends ActionBarActivity {

	private Button startBtn;
	private SeekBar skillSelect;
	private Spinner mazeGenSpinner;
	private TextView skillSelected;
	
	final public static String BACKTRACKING = "Backtracking",
		                	   ELLER = "Eller",
		                	   PRIM = "Prim",
		                	   FILE = "Load From File";

	private String generator = BACKTRACKING;
	
	final public static String SKILL = "SKILL",
							   ALGORITHM = "ALGORITHM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amaze);

		startBtn = ((Button) findViewById(R.id.start_button));
		skillSelect = (SeekBar) findViewById(R.id.skill_select_seek_bar);
		mazeGenSpinner = (Spinner) findViewById(R.id.maze_gen_spinner);
		skillSelected = (TextView) findViewById(R.id.skill_selected);
	
		skillSelect.setMax(15);
	
		populateSpinner();
		setSkillOnChangeListener();
	}

	/**
	 * populate the maze generation algorithm spinner
	 */
	private void populateSpinner() {
		List<String> options = new ArrayList<String>();
		options.add(BACKTRACKING);
		options.add(ELLER);
		options.add(PRIM);
		options.add(FILE);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mazeGenSpinner.setAdapter(adapter);
	}
	
	/**
	 * listen for slider change and update the text view representation
	 */
	public void setSkillOnChangeListener() {
		skillSelect.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				skillSelected.setText(Integer.toString(progress));
				Log.v("skill change", ""+progress);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
	}
	
	/**
	 * listen for the start button click and move to the generating activity
	 * @param view
	 */
	public void moveToGenerating(View view) {
		Intent i = new Intent(this, GeneratingActivity.class);
		generator = ((Spinner) findViewById(R.id.maze_gen_spinner)).getSelectedItem().toString();
		i.putExtra(SKILL, skillSelect.getProgress());
		i.putExtra(ALGORITHM, generator);
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.amaze, menu);
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
