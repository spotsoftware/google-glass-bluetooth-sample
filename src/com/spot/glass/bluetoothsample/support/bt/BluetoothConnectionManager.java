package com.spot.glass.bluetoothsample.support.bt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.spot.glass.bluetoothsample.support.bt.listeners.IBluetoothConnectionListener;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * This class represents a thread which runs during all over the connection with a remote device. It handles all
 * incoming and outgoing transmissions.
 */
public class BluetoothConnectionManager extends Thread {

	private static final String TAG = BluetoothConnectionManager.class.getName();

	private final IBluetoothConnectionListener mListener;
	private final BluetoothSocket mSocket;
	private final InputStream mInStream;
	private final OutputStream mOutStream;
	private boolean mIsRunning;

	// { Construction

	protected BluetoothConnectionManager(final BluetoothSocket socket, final IBluetoothConnectionListener listener) {
		Log.d(TAG, "create ConnectedThread");

		this.mListener = listener;

		mSocket = socket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;

		// Get the BluetoothSocket input and output streams
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
			Log.e(TAG, "temp sockets not created", e);
		}

		mInStream = tmpIn;
		mOutStream = tmpOut;
		mIsRunning = true;
	}

	public static BluetoothConnectionManager create(final BluetoothSocket socket,
			final IBluetoothConnectionListener listener) {
		return new BluetoothConnectionManager(socket, listener);
	}

	// }

	public void run() {
		byte[] buffer = new byte[1024];

		while (mIsRunning) {
			try {
				int bytes = mInStream.read(buffer);
				this.mListener.onMessageReceived(bytes, buffer);
			} catch (Exception e) {
				this.mListener.onConnectionLost();
				break;
			}
		}
	}

	/**
	 * Write to the connected OutStream.
	 * 
	 * @param buffer
	 *            The bytes to write
	 */
	public void write(byte[] buffer) {
		try {
			mOutStream.write(buffer);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

	public void write(int out) {
		try {
			mOutStream.write(out);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

	public void cancel() {
		try {
			Log.e("BTCMDSERVICE", "Closing streams");
			mIsRunning = false;
			mInStream.close();
			mOutStream.close();
			mSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "close() of connect socket failed", e);
		}
	}

}
