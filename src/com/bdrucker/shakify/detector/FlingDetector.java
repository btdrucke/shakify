package com.bdrucker.shakify.detector;


import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class FlingDetector
{
	public interface OnFlingListener
	{
		public boolean onFlingLeft();
		public boolean onFlingRight();
		public boolean onFlingUp();
		public boolean onFlingDown();
	}

	private GestureDetector mDetector;
	private final Context mContext;

	public <T extends Context & OnFlingListener> FlingDetector (T contextAndListener)
	{
		this.mContext = contextAndListener;
		mDetector = new GestureDetector(mContext, new MyGestureListener(contextAndListener));
	}

	public FlingDetector (Context context, OnFlingListener listener)
	{
		this.mContext = context;
		mDetector = new GestureDetector(mContext, new MyGestureListener(listener));
	}

    public boolean onTouchEvent(MotionEvent event)
    {
    	return (mDetector == null) ? null : mDetector.onTouchEvent(event);
    }
	
	private static class MyGestureListener extends GestureDetector.SimpleOnGestureListener
	{
		private static final float SWIPE_THRESHOLD_VELOCITY = 200;
		private static final double SWIPE_MIN_DISTANCE = 120;
		private static final double SWIPE_MAX_OFF_ANGLE = 22.5;
		
		private OnFlingListener mListener;
		
		public MyGestureListener(OnFlingListener listener)
		{
			this.mListener = listener; 
		}
		
		private static boolean isCloseToAngle(double angle, double targetAngle)
		{
			return Math.abs(targetAngle - angle) < SWIPE_MAX_OFF_ANGLE;
		}
		
		@Override
		public boolean onFling(MotionEvent startEvent, MotionEvent finishEvent, float xVelocity, float yVelocity)
		{
			// Ignore flings that are not fast enough
			if (Math.abs(xVelocity) <= SWIPE_THRESHOLD_VELOCITY)
				return false;
			
			final float xDelta = finishEvent.getX() - startEvent.getX();
			final float yDelta = startEvent.getY() - finishEvent.getY(); // if motion is down, startY > finishY
			final double distance = Math.hypot(xDelta, yDelta);

			// Ignore flings that are not long enough
			if (distance <= SWIPE_MIN_DISTANCE)
				return false;

			final double angle = Math.toDegrees(Math.atan2(yDelta, xDelta));  // -180..180

			if (isCloseToAngle(angle, 0))
				return mListener.onFlingRight();
			else if (isCloseToAngle(angle, 90))
				return mListener.onFlingUp();
			else if (isCloseToAngle(angle, 180) || isCloseToAngle(angle, -180))
				return mListener.onFlingLeft();
			else if (isCloseToAngle(angle, -90))
				return mListener.onFlingDown();
			else 
				return false;
		}
	}
}