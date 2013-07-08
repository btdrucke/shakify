package com.bdrucker.shakify.detector;

interface DetectorBaseInterface
{
	public boolean isSupported();
	public void onResume();
	public void onPause();
}
