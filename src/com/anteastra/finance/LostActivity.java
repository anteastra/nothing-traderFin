package com.anteastra.finance;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LostActivity extends Activity{

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lost_layout);
		((Button) findViewById(R.id.buttonResetLost)).setOnClickListener(new OnControlButtonListener());
	}
	
	private class OnControlButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.buttonResetLost){
				GameStateSingleton.reset();
				Intent myIntent = new Intent(LostActivity.this, GameActivity.class);
				LostActivity.this.startActivity(myIntent);
				LostActivity.this.finish();
			}
			
		}
		
	}
}
