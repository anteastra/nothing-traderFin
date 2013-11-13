package com.anteastra.finance;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GameActivity extends Activity {

	private Timer mainTimer;
	private TimerTask mainTimerTask;
	private UpdateUIThread myUpdateUIThread;
	
	private GameStateSingleton state;
	private MySurfaceView surface;
	
	private TextView TextViewDay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
						
		state = GameStateSingleton.getInstance();
		UpdateViews();
		
		Button speed0 = (Button) findViewById(R.id.buttonSpeed0);
		Button speed1 = (Button) findViewById(R.id.buttonSpeed1);
		Button speed2 = (Button) findViewById(R.id.buttonSpeed2);
		Button speed3 = (Button) findViewById(R.id.buttonSpeed3);
		
		speed0.setOnClickListener(new OnSpeedButtonListener());
		speed1.setOnClickListener(new OnSpeedButtonListener());
		speed2.setOnClickListener(new OnSpeedButtonListener());
		speed3.setOnClickListener(new OnSpeedButtonListener());
		
		((Button) findViewById(R.id.ButtonHirePersonal)).setOnClickListener(new OnControlButtonListener());
		((Button) findViewById(R.id.buttonBuyProd)).setOnClickListener(new OnControlButtonListener());
		((Button) findViewById(R.id.ButtonRentRetail)).setOnClickListener(new OnControlButtonListener());
		((Button) findViewById(R.id.toggleButtonSell)).setOnClickListener(new OnControlButtonListener());		
		
		
		SurfaceView surfaceViewMain = (SurfaceView)findViewById(R.id.surfaceViewMain);
		LinearLayout lay = (LinearLayout)findViewById(R.id.linearLayoutSurface);
		surface = new MySurfaceView(this);
		ViewGroup.LayoutParams p = surfaceViewMain.getLayoutParams();
		surface.setLayoutParams(p); 
		lay.addView(surface);
		lay.removeView(surfaceViewMain);
		
		myUpdateUIThread = new UpdateUIThread();
		mainTimerTask = new MainTimerTask();
		mainTimer = new Timer();
		mainTimer.schedule(mainTimerTask, state.MAIN_TIMER_DURATION, state.MAIN_TIMER_DURATION);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}
	
	public void onPause(){
		super.onPause();
		state.gameSpeed = state.SPEED_0;
	}
	
	public void onResume(){
		super.onResume();
		UpdateViews();
	}
		
	public void UpdateViews(){		
		((TextView) findViewById(R.id.textViewDayNumber)).setText(String.valueOf(state.day)+"."+String.valueOf(state.month));
		((TextView) findViewById(R.id.textViewBuyPrice)).setText(String.valueOf(state.buyPrices[state.periodsCount-1]));
		((TextView) findViewById(R.id.textViewSellPrice)).setText(String.valueOf(state.sellPrices[state.periodsCount-1]));
		((TextView) findViewById(R.id.textViewMoney)).setText(String.valueOf(state.moneyAmount));
		((TextView) findViewById(R.id.textViewSpeed)).setText(String.valueOf(state.gameSpeed));		
		((TextView) findViewById(R.id.textViewLoadedSpace)).setText(String.valueOf(state.loadedSpace)+"/"+String.valueOf(state.retails*state.retailSpace));
		((TextView) findViewById(R.id.textViewAvaragePrice)).setText(String.valueOf(state.avaragePrice));
		((TextView) findViewById(R.id.textViewRetails)).setText(String.valueOf(state.retails));
		((TextView) findViewById(R.id.textViewRetailsPay)).setText(String.valueOf(state.retailRent*state.retails));
		((TextView) findViewById(R.id.textViewPersonal)).setText(String.valueOf(state.personnel)+"/"+String.valueOf(state.retails*2));
		((TextView) findViewById(R.id.textViewPersonalPay)).setText(String.valueOf(state.personnel*state.personnelPay));
		
	}
	
	class UpdateUIThread implements Runnable{

		@Override
		public void run() {
			UpdateViews();			
		}
		
	}
	
	class MainTimerTask extends TimerTask{

		@Override
		public void run() {
			state.dayTimeLeft -= state.gameSpeed;
			if (state.dayTimeLeft<=0){
				state.nextDay();
				runOnUiThread(myUpdateUIThread);
			}	
		}
		
	}
	
	class OnSpeedButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (v.equals(findViewById(R.id.buttonSpeed0))){
				state.gameSpeed = state.SPEED_0;
			}
			if (v.equals(findViewById(R.id.buttonSpeed1))){
				state.gameSpeed = state.SPEED_1;
			}
			if (v.equals(findViewById(R.id.buttonSpeed2))){
				state.gameSpeed = state.SPEED_2;
			}
			if (v.equals(findViewById(R.id.buttonSpeed3))){
				state.gameSpeed = state.SPEED_3;
			}
			UpdateViews();
		}
		
	}
	
	class OnControlButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.buttonBuyProd){
				int needAmt = state.retailSpace*state.retails - state.loadedSpace;
				int addedAmt =needAmt;
				int initialLoadedSpace = state.loadedSpace;
				int initialAvgPrice = state.avaragePrice;
				if (state.moneyAmount >= needAmt * state.getTodayBuyPrice()){
					state.moneyAmount -= needAmt * state.getTodayBuyPrice();
					state.loadedSpace = state.loadedSpace+needAmt;
				} else{
					int canBuy = (state.moneyAmount / state.getTodayBuyPrice());
					addedAmt = canBuy;
					state.moneyAmount -= canBuy * state.getTodayBuyPrice();
					state.loadedSpace = state.loadedSpace+canBuy;
				}
				state.avaragePrice = (initialLoadedSpace*initialAvgPrice+addedAmt*state.getTodayBuyPrice())/(initialLoadedSpace+addedAmt);
			}
			
			if (v.getId() == R.id.ButtonHirePersonal){
				if (state.personnel< (state.retails*2))
					state.personnel++;
			}
			if (v.getId() == R.id.ButtonRentRetail){
				if (state.moneyAmount >= state.retailCost){
					state.moneyAmount = state.moneyAmount - state.retailCost;
					state.retails ++;
				}
			}
			if (v.getId() == R.id.toggleButtonSell){
				state.isCanSell = ((ToggleButton) v).isChecked();
			}			
			
			UpdateViews();
			
		}
		
	}

}
