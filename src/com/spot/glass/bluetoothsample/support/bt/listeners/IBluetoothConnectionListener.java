package com.spot.glass.bluetoothsample.support.bt.listeners;

public interface IBluetoothConnectionListener {

	/**
	 * Indicates that the connection was lost and notifies it to the UI Thread manager.
	 */
	public void onConnectionLost();

	/**
	 * Sends the obtained bytes to the UI Activity.
	 * 
	 * @param bytes
	 *            the number of bytes that need to be read from the buffer
	 * @param buffer
	 *            the source which contains the message
	 * */
	public void onMessageReceived(final int bytes, final byte[] buffer);

}
