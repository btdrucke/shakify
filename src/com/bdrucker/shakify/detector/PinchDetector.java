package com.bdrucker.shakify.detector;


import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class PinchDetector
{
	public interface OnPinchListener
	{
		public boolean onPinchIn();
		public boolean onPinchOut();
	}

	private ScaleGestureDetector mDetector;
	private final Context mContext;

	public <T extends Context & OnPinchListener> PinchDetector (T contextAndListener)
	{
		this.mContext = contextAndListener;
		mDetector = new ScaleGestureDetector(mContext, new MyGestureListener(contextAndListener));
	}

	public PinchDetector (Context context, OnPinchListener listener)
	{
		this.mContext = context;
		mDetector = new ScaleGestureDetector(mContext, new MyGestureListener(listener));
	}

    public boolean onTouchEvent(MotionEvent event)
    {
    	return (mDetector == null) ? false : mDetector.onTouchEvent(event);
    }
	
	private static class MyGestureListener implements ScaleGestureDetector.OnScaleGestureListener
	{
		private static final float MIN_SCALE = 0.1f;
		
		private OnPinchListener mListener;
		private float spanStart;
		
		public MyGestureListener(OnPinchListener listener)
		{
			this.mListener = listener; 
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector)
		{
			if (mListener == null)
				return false;
			
			spanStart = detector.getCurrentSpan();
			return (spanStart != 0);
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector)
		{
			if (mListener == null)
				return;	
			
			final float spanEnd = detector.getCurrentSpan();
			final float factor = (spanEnd - spanStart) / spanStart;
			
			if (Math.abs(factor) < MIN_SCALE)
				return;
			
			if (factor > 0)
				mListener.onPinchOut();
			else
				mListener.onPinchIn();
		}
	}
}