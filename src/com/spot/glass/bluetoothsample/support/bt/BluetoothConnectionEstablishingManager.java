package com.spot.glass.bluetoothsample.support.bt;

import java.io.IOException;

import com.spot.glass.bluetoothsample.support.Constants;
import com.spot.glass.bluetoothsample.support.bt.listeners.IBluetoothConnectionEstablishingListener;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * This class represents a thread which runs while attempting to make an outgoing connection with a device. It runs
 * straight through; the connection either succeeds or fails.
 */
public class BluetoothConnectionEstablishingManager extends Thread {

	private static final String TAG = BluetoothConnectionEstablishingManager.class.getName();

	private final BluetoothSocket mSocket;
	private final BluetoothDevice mDevice;

	private final IBluetoothConnectionEstablishingListener mListener;

	// { Construction

	protected BluetoothConnectionEstablishingManager(final BluetoothDevice device,
			final IBluetoothConnectionEstablishingListener listener) {

		this.mListener = listener;

		this.mDevice = device;
		this.mSocket = this.initializeSocket();
	}

	public static BluetoothConnectionEstablishingManager create(final BluetoothDevice device,
			final IBluetoothConnectionEstablishingListener listener) {

		return new BluetoothConnectionEstablishingManager(device, listener);
	}

	// }

	// { Private and protected methods

	private BluetoothSocket initializeSocket() {
		try {
			return this.mDevice.createRfcommSocketToServiceRecord(Constants.BLUETOOTH_UUID);
		} catch (IOException e) {
			return null;
		}
	}

	// }

	public void run() {
		Log.i(TAG, "BEGIN mConnectThread");
		setName("ConnectThread");

		try {
			mSocket.connect();
		} catch (IOException e) {
			try {
				mSocket.close();
			} catch (IOException e2) {
				Log.e(TAG, "unable to close() socket during connection failure", e2);
			}

			this.mListener.onConnectionFailed();

			return;
		}

		this.mListener.onConnectionSucceded(this.mSocket, this.mDevice);
	}

	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "Failed the close() method call on socket.", e);
		}
	}

}
