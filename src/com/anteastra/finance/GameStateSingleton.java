package com.anteastra.finance;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class GameStateSingleton {

	private static Object syncObject = new Object();
	private static GameStateSingleton instance = null;
	private static Context context;
	private GameStateSingleton(){
		
	}
	
	public static GameStateSingleton getInstance(){
		if (instance == null){
			synchronized(syncObject){
				instance = new GameStateSingleton();
				instance.initialState();				
			}
		}		
		return instance;		
	}
	
	public int gameSpeed = InitialGameStateSingleton.gameSpeed;
	public final int SPEED_0 = InitialGameStateSingleton.SPEED_0;
	public final int SPEED_1 = InitialGameStateSingleton.SPEED_1;
	public final int SPEED_2 = InitialGameStateSingleton.SPEED_2;
	public final int SPEED_3 = InitialGameStateSingleton.SPEED_3;
	public final int DAY_DURATION = InitialGameStateSingleton.DAY_DURATION;
	public final int MAIN_TIMER_DURATION = InitialGameStateSingleton.MAIN_TIMER_DURATION;
	
	public int day = InitialGameStateSingleton.day;
	public int month = InitialGameStateSingleton.month;
	public int dayTimeLeft = InitialGameStateSingleton.dayTimeLeft;
	
	
	public int periodsCount = InitialGameStateSingleton.periodsCount;
	public int [] sellPrices = new int[periodsCount];
	public int [] buyPrices = new int[periodsCount];
	
	public int moneyAmount = InitialGameStateSingleton.moneyAmount;
	public int loadedSpace = InitialGameStateSingleton.loadedSpace;
	public int avaragePrice = InitialGameStateSingleton.avaragePrice;
	public int personnel = InitialGameStateSingleton.personnel;
	public int retails = InitialGameStateSingleton.retails;
	
	public int retailCost = InitialGameStateSingleton.retailCost;
	public int personnelPay = InitialGameStateSingleton.personnelPay;
	public int retailRent = InitialGameStateSingleton.retailRent;
	public int retailSpace = InitialGameStateSingleton.retailSpace;
	
	public boolean isCanSell = true;
	
	
	public static void setContext(Context con){
		context = con;
	}
	
	public void nextDay(){
		day++;
		//moneyAmount += 30;
		dayTimeLeft = DAY_DURATION;
		generatePrice();
		
		//count sellings
		if (isCanSell){
			Random rand = new Random();
			for(int i=0; i<personnel; i++){
				int canSell = rand.nextInt(10);
				int sells =0;
				if (canSell > loadedSpace){
					sells = loadedSpace;
				}else{
					sells = canSell;
				}
				moneyAmount += sells*getLastSellPrice();
				loadedSpace -= sells;
			}
		}
		
		if (day%30==1){
			month++;
			day = 1;
			moneyAmount = moneyAmount - retails * retailRent;
			moneyAmount = moneyAmount - personnel * personnelPay;
			
			if (moneyAmount <0){
				Intent myIntent = new Intent(context, LostActivity.class);
				context.startActivity(myIntent);
			}
			
			if (moneyAmount > 100000){
				Intent myIntent = new Intent(context, WinActivity.class);
				context.startActivity(myIntent);
			}
		}
		
	}
	
	private void generatePrice(){
		Random rand = new Random();
		int sellPrice = rand.nextInt(50)+20;
		int buyPrice = sellPrice - (rand.nextInt(20)-5);
		
		for (int i=0; i<(periodsCount-1); i++){
			sellPrices[i]= sellPrices[i+1];
			buyPrices[i]= buyPrices[i+1];
		}
		sellPrices[periodsCount-1] = sellPrice;
		buyPrices[periodsCount-1] = buyPrice;
	}
	
	private void initialGeneratePrice(){	
		
		for (int i=0; i<periodsCount; i++){
			generatePrice();
		}		
	}
	
	private void initialState(){
		initialGeneratePrice();
	}
	
	public int getTodayBuyPrice(){
		return buyPrices[periodsCount-1];
	}
	public int getLastSellPrice(){
		return sellPrices[periodsCount-2];
	}
	
	public static void reset(){
		instance = new GameStateSingleton();
	}
	
}
