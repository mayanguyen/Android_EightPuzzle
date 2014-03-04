/*
 * Van Mai Nguyen Thi <vn4720@bard.edu>
 * CMSC 374: Assignment 2: Eight Puzzle
 * due date: 24 February, 2014
 */

package edu.bard.eightpuzzle;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

public class EightPuzzle extends Activity {
	public static final ArrayList<Button> buttons = new ArrayList<Button>();
	public static int blank;
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eight_puzzle);

		buttons.add( (Button) findViewById(R.id.button0) );
		buttons.add( (Button) findViewById(R.id.button1) );
		buttons.add( (Button) findViewById(R.id.button2) );
		buttons.add( (Button) findViewById(R.id.button3) );
		buttons.add( (Button) findViewById(R.id.button4) );
		buttons.add( (Button) findViewById(R.id.button5) );
		buttons.add( (Button) findViewById(R.id.button6) );
		buttons.add( (Button) findViewById(R.id.button7) );
		buttons.add( (Button) findViewById(R.id.button8) );
		blank = 0;		// buttons.get(blank) == current blank tile

		shuffle();

		// create handler for: button clicked
		View.OnClickListener handler = new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				// set dialog message
				builder.setMessage("You won!")
				.setCancelable(false)
				.setPositiveButton("Play again",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked,
						// close the dialog box and re-shuffle
						dialog.cancel();
						shuffle();
					}
				})
				.setNegativeButton("Quit",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						finish();
						System.exit(0);
					}
				});;

				// create alert dialog that notifies that game is won
				AlertDialog youWonDialog = builder.create();

				if (isSwapable( (Button)v )) {
					swap( (Button)v );
					if (isGoalState()) 
						youWonDialog.show();
				}
			}
		};

		// set handler to every button
		for (int i=0; i<9; i++) {
			buttons.get(i).setOnClickListener(handler);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.eight_puzzle, menu);
		return true;
	}

	/**
	 * shuffle the buttons, maintaining "reachability" of the goal state
	 */
	public void shuffle() {
		int count = 0;
		int random;

		while (count < 31) {
			random = (int)Math.round(Math.random()*8);
			if ( isSwapable(buttons.get(random)) ) {
				swap(buttons.get(random));
				count++;
			}
		}
	}

	/**
	 * @return true if goal state is reached
	 * goal state is having the blank tile in the center
	 * & numbers 1..8 in order starting from top left corner clockwise
	 */
	public boolean isGoalState() {
		if (buttons.get(0).getText() != getString(R.string.button0))
			return false;
		if (buttons.get(1).getText() != getString(R.string.button1))
			return false;
		if (buttons.get(2).getText() != getString(R.string.button2))
			return false;
		if (buttons.get(3).getText() != getString(R.string.button3))
			return false;
		if (buttons.get(4).getText() != getString(R.string.button4))
			return false;
		if (buttons.get(5).getText() != getString(R.string.button5))
			return false;
		if (buttons.get(6).getText() != getString(R.string.button6))
			return false;
		if (buttons.get(7).getText() != getString(R.string.button7))
			return false;
		if (buttons.get(8).getText() != getString(R.string.button8))
			return false;
		return true;
	}

	/**
	 * @return true if input (button) is adjacent to the blank button.
	 * @param button to be checked for "swapability"
	 */ 
	public boolean isSwapable(Button b) {
		Button b0 = (Button) buttons.get(blank); // blank button
		if (b.getId() == b0.getId()) 
			return false;

		int[] pos0 = new int[2];
		int[] pos1 = new int[2];
		b0.getLocationOnScreen(pos0);
		b.getLocationOnScreen(pos1);

		if (pos0[0] == pos1[0]) { // same column
			if (Math.abs(pos0[1]-pos1[1]) <= 25+(Math.max(b0.getHeight(), b.getHeight()))) 
				return true;
			else
				return false;
		}
		if (pos0[1] == pos1[1]) { // same row
			if (Math.abs(pos0[0]-pos1[0]) <= 25+(Math.max(b0.getWidth(), b.getWidth()))) 
				return true;
			else 
				return false;
		}
		return false;
	}

	/**
	 * swap text and background of input button with those of the blank button
	 * @param button to be swapped
	 */ 
	public void swap(Button b) {
		Button b0 = (Button) buttons.get(blank); // blank button

		CharSequence b_text = b.getText();
		Drawable b_background = b.getBackground();

		b.setText(b0.getText());
		b.setBackground(b0.getBackground());
		b0.setText(b_text);
		b0.setBackground(b_background);
		blank = buttons.indexOf(b); // keep record of the index of blank button
	}
}
