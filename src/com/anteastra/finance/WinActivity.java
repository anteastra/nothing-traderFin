package com.anteastra.finance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WinActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_layout);
		((Button) findViewById(R.id.buttonResetWin)).setOnClickListener(new OnControlButtonListener());
	}

	private class OnControlButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.buttonResetWin){
				GameStateSingleton.reset();
				Intent myIntent = new Intent(WinActivity.this, GameActivity.class);
				WinActivity.this.startActivity(myIntent);
				WinActivity.this.finish();
			}
			
		}
		
	}
}
