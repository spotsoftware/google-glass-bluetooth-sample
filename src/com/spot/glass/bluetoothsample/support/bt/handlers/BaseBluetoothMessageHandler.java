package com.spot.glass.bluetoothsample.support.bt.handlers;

import com.google.gson.JsonSyntaxException;
import com.spot.glass.bluetoothsample.support.Constants;
import com.spot.glass.bluetoothsample.support.bt.listeners.OnBluetoothMessageReceivedListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public abstract class BaseBluetoothMessageHandler extends Handler {

	private final Context context;
	private final OnBluetoothMessageReceivedListener mListener;

	private String mConnectedDeviceName = null;

	// { Construction

	public BaseBluetoothMessageHandler(final Context context, final OnBluetoothMessageReceivedListener listener) {
		this.context = context;
		this.mListener = listener;
	}

	// }

	// { Private and protected methods

	private void notifyStateChanged(final int state) {
		if (this.mListener != null) {
			this.mListener.onConnectionStateChanged(state);
		}

		switch (state) {
			case Constants.STATE_CONNECTED:
				Log.w("TITLE", "Connected to: " + mConnectedDeviceName);
				break;
			case Constants.STATE_CONNECTING:
				Log.w("TITLE", "Connecting");
				break;
			case Constants.STATE_LISTEN:
			case Constants.STATE_NONE:
				Log.w("TITLE", "Not connected");
				break;
		}
	}

	private void notifyDeviceConnected(final String deviceName) {
		if (this.mListener != null) {
			this.mListener.onConnectedDeviceNameReceived(deviceName);
		}
		Log.w("TITLE", "MESSAGE_DEVICE_NAME --> Connected to " + deviceName);
		Toast.makeText(this.context, "Connected to " + deviceName, Toast.LENGTH_SHORT).show();
	}

	private void notifyToastMessage(final String message) {
		if (this.mListener != null) {
			this.mListener.onMessageReceived(message);
		}
		Log.w("TITLE", "MESSAGE_TOAST --> " + message);
		Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
	}

	private void notifyMessage(final int bytes, final byte[] buffer) {
		try {
			if (this.mListener != null) {
				final String jsonMessage = new String(buffer, 0, bytes);
				this.mListener.onJsonMessageReceived(jsonMessage);
			}
		} catch (JsonSyntaxException jse) {
			jse.printStackTrace();
		}
	}

	// }

	// { Handler methods overriding

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case Constants.MESSAGE_STATE_CHANGE:
				this.notifyStateChanged(msg.arg1);
				break;

			case Constants.MESSAGE_DEVICE_NAME:
				mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
				this.notifyDeviceConnected(msg.getData().getString(Constants.DEVICE_NAME));
				break;

			case Constants.MESSAGE_TOAST:
				this.notifyToastMessage(msg.getData().getString(Constants.TOAST));
				break;

			case Constants.MESSAGE_READ:
				final byte[] buffer = (byte[]) msg.obj;
				final int bytes = msg.arg1;
				this.notifyMessage(bytes, buffer);
				break;
		}
	}

	// }

};
