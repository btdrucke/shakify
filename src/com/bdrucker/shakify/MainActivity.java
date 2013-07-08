package com.bdrucker.shakify;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

import com.bdrucker.shakify.detector.FlingDetector;
import com.bdrucker.shakify.detector.PinchDetector;
import com.bdrucker.shakify.detector.ShakeDetector;

public class MainActivity extends Activity implements
		FlingDetector.OnFlingListener,
		ShakeDetector.OnShakeListener,
		PinchDetector.OnPinchListener
{	
	@SuppressWarnings("unused")
	private ShakeDetector shakeDetector;
	private FlingDetector flingDetector;
	private PinchDetector pinchDetector;
	private TextView logView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		logView = (TextView) findViewById(R.id.log);
		shakeDetector = new ShakeDetector(this);
		flingDetector = new FlingDetector(this);
		pinchDetector = new PinchDetector(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	return flingDetector.onTouchEvent(event) || pinchDetector.onTouchEvent(event);
    }
    
	@Override
	protected void onPause()
	{
		super.onPause();
		shakeDetector.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		shakeDetector.onResume();
	}

	@Override
	public void onShake()
	{
		final long now = System.currentTimeMillis();
		logView.append(String.format("\nShaken: %d", now)); //$NON-NLS-1$
	}

	@Override
	public boolean onFlingLeft()
	{
		final long now = System.currentTimeMillis();
		logView.append(String.format("\nLeft fling: %d", now)); //$NON-NLS-1$
		return true;
	}

	@Override
	public boolean onFlingRight()
	{
		final long now = System.currentTimeMillis();
		logView.append(String.format("\nRight fling: %d", now)); //$NON-NLS-1$
		return true;
	}

	@Override
	public boolean onFlingUp()
	{
		final long now = System.currentTimeMillis();
		logView.append(String.format("\nUp fling: %d", now)); //$NON-NLS-1$
		return true;
	}

	@Override
	public boolean onFlingDown()
	{
		final long now = System.currentTimeMillis();
		logView.append(String.format("\nDown fling: %d", now)); //$NON-NLS-1$
		return true;
	}

	@Override
	public boolean onPinchIn()
	{
		final long now = System.currentTimeMillis();
		logView.append(String.format("\nPinch in: %d", now)); //$NON-NLS-1$
		return true;
	}

	@Override
	public boolean onPinchOut()
	{
		final long now = System.currentTimeMillis();
		logView.append(String.format("\nPinch out: %d", now)); //$NON-NLS-1$
		return true;
	}
}
