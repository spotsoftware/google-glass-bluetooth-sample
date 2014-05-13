package com.spot.glass.bluetoothsample.support.bt.listeners;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public interface IBluetoothConnectionEstablishingListener {

	/**
	 * Indicates that the connection was lost and notifies it to the UI Thread manager.
	 */
	public void onConnectionFailed();

	/**
	 * Indicates that the connection was successful and notifies it to the UI Thread manager.
	 */
	public void onConnectionSucceded(final BluetoothSocket socket, final BluetoothDevice device);

}
