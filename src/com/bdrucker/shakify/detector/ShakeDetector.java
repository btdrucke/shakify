package com.bdrucker.shakify.detector;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements DetectorBaseInterface
{
	private final SensorManager mSensorManager;
	private final MySensorEventListener mEventListener;
	private final Sensor mAccelerometer;

	public interface OnShakeListener
	{
		public void onShake();
	}

	public <T extends Context & OnShakeListener> ShakeDetector (T contextAndListener)
	{
		mSensorManager = (SensorManager) contextAndListener.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = (mSensorManager == null) ? null : mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mEventListener = new MySensorEventListener(contextAndListener);
		onResume();
	}

	public ShakeDetector(Context context, OnShakeListener listener)
	{
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = (mSensorManager == null) ? null : mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mEventListener = new MySensorEventListener(listener);
		onResume();
	}

	@Override
	public boolean isSupported()
	{
		return (mAccelerometer != null);
	}
	
	@Override
	public void onResume()
	{
		if (mSensorManager != null)
			mSensorManager.registerListener(mEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onPause()
	{
		if (mSensorManager != null)
			mSensorManager.unregisterListener(mEventListener);
	}

	private static class MySensorEventListener implements SensorEventListener
	{
		private static final float FORCE_THRESHOLD = 350;
		private static final long TIME_THRESHOLD = 100;
		private static final long SHAKE_TIMEOUT = 500;
		private static final long SHAKE_DURATION = 1000;
		private static final int SHAKE_COUNT = 3;	
		
		private float mLastX = -1.0f;
		private float mLastY = -1.0f;
		private float mLastZ = -1.0f;
		private int mShakeCount = 0;
		private long mLastTime;
		private long mLastShake;
		private long mLastForce;
		private OnShakeListener mShakeListener;

		public MySensorEventListener(OnShakeListener listener)
		{
			mShakeListener = listener;
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
				return;
			
			long now = System.currentTimeMillis();
			if ((now - mLastForce) > SHAKE_TIMEOUT)
				mShakeCount = 0;

			if ((now - mLastTime) > TIME_THRESHOLD)
			{
				long diff = now - mLastTime;
				float speed = Math.abs(event.values[0] + event.values[1] + event.values[2] - mLastX - mLastY - mLastZ) / diff * 10000;
				if (speed > FORCE_THRESHOLD)
				{
					++mShakeCount;
					if ((mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION))
					{
						mLastShake = now;
						mShakeCount = 0;
						if (mShakeListener != null)
							mShakeListener.onShake();
					}
					mLastForce = now;
				}
				mLastTime = now;
				mLastX = event.values[0];
				mLastY = event.values[1];
				mLastZ = event.values[2];
			}
		}	
	}
}