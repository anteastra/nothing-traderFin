package com.anteastra.finance;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class DrawSurfaceThread extends Thread{

	private boolean isRunning = false;
	private long prevTime = 0;
	private SurfaceHolder surfaceHolder;
	private GameStateSingleton state;
	
	private int x = 0;
	
	
	public DrawSurfaceThread(SurfaceHolder sh){
		surfaceHolder = sh;
		state = GameStateSingleton.getInstance();
	}
	
	@Override
	public void run() {
		Canvas canvas;		
		
		while (isRunning){
			long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;
            if (elapsedTime >50){
            	prevTime = now;
            	
            	if (x>100) x = 0;
            	x++;
            	
            	canvas = null;
                try {                    
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                    	drawGraph(canvas);                        
                    }
                } 
                finally {
                    if (canvas != null) {                        
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            
            
		}
		
	}
	
	public void drawGraph(Canvas canvas){
		
		Paint myPaint = new Paint();
		
		myPaint.setColor(Color.LTGRAY);
		myPaint.setStrokeWidth(2);		
		
		canvas.drawColor(Color.BLACK);
		
		
		for (int i=0; i < (state.periodsCount-1); i++){
        	float startY = canvas.getHeight()-(state.buyPrices[i]);
        	float stopY = canvas.getHeight()-(state.buyPrices[i+1]);
        	float startX = canvas.getWidth()/9*i;
        	float stopX = canvas.getWidth()/9*(i+1);
        	if (i == (state.periodsCount-1)){
        		stopX = canvas.getWidth();
        	}
        	canvas.drawLine(startX, startY, stopX, stopY, myPaint);
        }
		
		myPaint.setColor(Color.YELLOW);
        
        for (int i=0; i < (state.periodsCount-1); i++){
        	float startY = canvas.getHeight()-(state.sellPrices[i]);
        	float stopY = canvas.getHeight()-(state.sellPrices[i+1]);
        	float startX = canvas.getWidth()/9*i;
        	float stopX = canvas.getWidth()/9*(i+1);
        	if (i == (state.periodsCount-1)){
        		stopX = canvas.getWidth();
        	}
        	canvas.drawLine(startX, startY, stopX, stopY, myPaint);
        }
	}

	public void setRunning(boolean bool) {
		isRunning = bool;		
	}

}
