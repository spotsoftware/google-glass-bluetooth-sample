package com.spot.glass.bluetoothsample.support;

import java.util.UUID;

public class Constants 
{
	// Unique UUID for this application
	public static final UUID BLUETOOTH_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
        
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
    // Key names received from the BluetoothCommandService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    
    public static final String AMERICAN_METRIC_SYSTEM = "US";
    public static final String INTERNATIONAL_METRIC_SYSTEM = "EUR";
    
    public static enum MetricSystemTypes {
    	AMERICAN,
    	INTERNATIONAL    	
    }
}
