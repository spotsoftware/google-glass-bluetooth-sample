package com.spot.glass.bluetoothsample.support.bt;

import com.spot.glass.bluetoothsample.support.bt.listeners.OnBluetoothConnectionEstablishedListener;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public interface IBluetoothCommandService {

	/**
	 * Start the {@link BluetoothConnectionManager} thread, specifically to begin a session in listening (server) mode.
	 */
	public void setup();

	/**
	 * Stops all threads
	 */
	public void tearDown();

	/**
	 * Starts the BluetoothConnectionManager aiming to the initialization of a connection to the given
	 * {@link BluetoothDevice}.
	 * 
	 * @param device
	 *            The BluetoothDevice to connect
	 */
	public void setupConnection(final BluetoothDevice device);

	/**
	 * Starts the {@link BluetoothConnectionManager} which will manage the Bluetooth connection.
	 * 
	 * @param socket
	 *            The BluetoothSocket on which the connection was made
	 * @param device
	 *            The BluetoothDevice that has been connected
	 */
	public void startListeningOverConnection(final BluetoothSocket socket, final BluetoothDevice device);

	/**
	 * Return the current connection state.
	 */
	public int getState();

	/**
	 * Set a {@link OnBluetoothConnectionEstablishedListener}.
	 */
	public void setListener(final OnBluetoothConnectionEstablishedListener listener);

	/**
	 * Write to the {@link BluetoothConnectionManager} in an unsynchronized manner.
	 * 
	 * @param out
	 *            The bytes to write
	 * @see BluetoothConnectionManager#write(byte[])
	 */
	public void write(final byte[] out);

	/**
	 * Write to the {@link BluetoothConnectionManager} in an unsynchronized manner.
	 * 
	 * @param value
	 *            The integer value to write
	 * @see BluetoothConnectionManager#write(int)
	 */
	public void write(final int value);

	/**
	 * Write to the {@link BluetoothConnectionManager} in an unsynchronized manner.
	 * 
	 * @param message
	 *            The String to write
	 * @see BluetoothConnectionManager#write(String)
	 */
	public void write(final String message);

}
