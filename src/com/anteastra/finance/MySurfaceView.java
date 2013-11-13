package com.anteastra.finance;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{

	private DrawSurfaceThread drawThread = null;
	
	public MySurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {		
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		drawThread = new  DrawSurfaceThread(getHolder());
		drawThread.setRunning(true);
		drawThread.start();		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		drawThread.setRunning(false);
		boolean retry = true;
		while (retry){
			try{
				drawThread.join();
				retry = false;				
				
			} catch(Exception e){
				
			}
		}
	}
	
	public void stopDraw(){
		drawThread.setRunning(false);
		boolean retry = true;
		while (retry){
			try{
				drawThread.join();
				retry = false;
				
			} catch(Exception e){
				
			}
		}
	}
}
