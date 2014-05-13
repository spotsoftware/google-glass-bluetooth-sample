package com.spot.glass.bluetoothsample.support.bt.listeners;

public interface OnBluetoothMessageReceivedListener extends OnBluetoothConnectionStateChangedListener {

	public void onMessageReceived(final String message);

	public void onConnectedDeviceNameReceived(final String deviceName);

	public void onJsonMessageReceived(final String jsonMessage);

}
