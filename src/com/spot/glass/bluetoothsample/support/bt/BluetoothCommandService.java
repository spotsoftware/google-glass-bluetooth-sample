package com.spot.glass.bluetoothsample.support.bt;

import com.spot.glass.bluetoothsample.support.Constants;
import com.spot.glass.bluetoothsample.support.bt.listeners.IBluetoothConnectionEstablishingListener;
import com.spot.glass.bluetoothsample.support.bt.listeners.IBluetoothConnectionListener;
import com.spot.glass.bluetoothsample.support.bt.listeners.OnBluetoothConnectionEstablishedListener;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BluetoothCommandService implements IBluetoothCommandService, IBluetoothConnectionListener,
		IBluetoothConnectionEstablishingListener {

	private static final String TAG = BluetoothCommandService.class.getName();

	private final BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private BluetoothConnectionEstablishingManager mConnectThread;
	private BluetoothConnectionManager mConnectedThread;
	private int mState;
	private OnBluetoothConnectionEstablishedListener mConnectedListener = null;

	/**
	 * Constructor. Prepares a new BluetoothChat session.
	 * 
	 * @param context
	 *            The UI Activity Context
	 * @param handler
	 *            A Handler to send messages back to the UI Activity
	 */
	public BluetoothCommandService(final Context context, final Handler handler) {
		this.mAdapter = BluetoothAdapter.getDefaultAdapter();
		this.mState = Constants.STATE_NONE;
		this.mHandler = handler;
	}

	// { Private and protected methods

	/**
	 * Set the current state of the bluetooth connection and notifies it to the UI Thread manager.
	 * 
	 * @param state
	 *            An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		this.mState = state;
		this.mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}

	private void resetConnectionThreads() {
		// Cancel any thread attempting to make a connection
		if (this.mConnectThread != null) {
			this.mConnectThread.cancel();
			this.mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (this.mConnectedThread != null) {
			this.mConnectedThread.cancel();
			this.mConnectedThread = null;
		}
	}

	/**
	 * Send the name of the connected device back to the UI Thread manager.
	 * */
	private void notifyConnectedDevice(final BluetoothDevice device) {
		final Message msg = this.mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
		final Bundle bundle = new Bundle();
		bundle.putString(Constants.DEVICE_NAME, device.getName());
		msg.setData(bundle);

		this.mHandler.sendMessage(msg);
	}

	private void notifyError() {
		final Message msg = this.mHandler.obtainMessage(Constants.MESSAGE_TOAST);
		final Bundle bundle = new Bundle();
		bundle.putString(Constants.TOAST, "Unable to connect device");
		msg.setData(bundle);

		this.mHandler.sendMessage(msg);
	}

	// }

	// { IBluetoothCommandService implementation

	@Override
	public void setListener(final OnBluetoothConnectionEstablishedListener listener) {
		this.mConnectedListener = listener;
	}

	@Override
	public synchronized int getState() {
		return this.mState;
	}

	@Override
	public synchronized void setup() {
		this.resetConnectionThreads();
		this.setState(Constants.STATE_LISTEN);
	}

	@Override
	public synchronized void setupConnection(final BluetoothDevice device) {
		this.resetConnectionThreads();

		// Cancel the discovery because it could slow down the connection process.
		this.mAdapter.cancelDiscovery();

		// Starts the thread to connect with the given device
		this.mConnectThread = BluetoothConnectionEstablishingManager.create(device, this);
		this.mConnectThread.start();
		this.setState(Constants.STATE_CONNECTING);
	}

	@Override
	public synchronized void startListeningOverConnection(BluetoothSocket socket, BluetoothDevice device) {
		this.resetConnectionThreads();

		// Start the thread to manage the connection and perform transmissions
		this.mConnectedThread = new BluetoothConnectionManager(socket, this);
		this.mConnectedThread.start();

		this.notifyConnectedDevice(device);

		this.setState(Constants.STATE_CONNECTED);

		this.mConnectedListener.onConnect();
	}

	@Override
	public synchronized void tearDown() {
		this.resetConnectionThreads();
		this.setState(Constants.STATE_NONE);
	}

	@Override
	public void write(byte[] out) {
		BluetoothConnectionManager r;
		synchronized (this) {
			if (this.mState != Constants.STATE_CONNECTED)
				return;
			r = this.mConnectedThread;
		}

		r.write(out);
	}

	@Override
	public void write(int out) {
		BluetoothConnectionManager r;
		synchronized (this) {
			if (this.mState != Constants.STATE_CONNECTED)
				return;
			r = this.mConnectedThread;
		}

		r.write(out);
	}

	@Override
	public void write(String message) {
		BluetoothConnectionManager r;
		synchronized (this) {
			if (this.mState != Constants.STATE_CONNECTED)
				return;
			r = this.mConnectedThread;
		}

		if (message.length() > 0) {
			byte[] send = message.getBytes();
			r.write(send);
		}
	}

	// }

	// { IBluetoothConnectionListener implementation

	@Override
	public void onConnectionLost() {
		this.setState(Constants.STATE_LISTEN);
	}

	@Override
	public void onMessageReceived(final int bytes, final byte[] buffer) {
		this.mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
	}

	// }

	// { IBluetoothConnectionEstablishingListener implementation

	@Override
	public void onConnectionFailed() {
		this.setState(Constants.STATE_LISTEN);

		this.notifyError();

		BluetoothCommandService.this.setup();
	}

	@Override
	public void onConnectionSucceded(BluetoothSocket socket, BluetoothDevice device) {
		synchronized (BluetoothCommandService.this) {
			this.mConnectThread = null;
		}

		startListeningOverConnection(socket, device);
	}

	// }

}