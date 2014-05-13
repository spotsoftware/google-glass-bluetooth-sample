package com.spot.glass.bluetoothsample.support.bt;

public class BluetoothMessage {

	public static final String EQUIPMENT_SPEED = "Speed";
	public static final String EQUIPMENT_INCLINE = "Incline";
	public static final String EQUIPMENT_PACE = "Pace";
	public static final String TRAINING_TIME = "Time";
	public static final String EQUIPMENT_HR = "HR";
	
	private String messageType;
	private String contentProvider;
	private String property;
	private String propertyValue;
	
	/*
	 * Constructor - The class is built starting from a message in the following format: get&&{contentProvider}&&{property}&&{propertyValue}
	 * */
	public BluetoothMessage(String message) {
		
		String[] splittedMessage = message.split("&&");
		
		this.messageType = splittedMessage.length > 0 ? splittedMessage[0] : "";
		
		if(this.messageType.equals("getPace")) {
			this.propertyValue = splittedMessage.length > 1 ? splittedMessage[1] : "";
		}
		else {
			this.contentProvider = splittedMessage.length > 1 ? splittedMessage[1] : "";
			this.property = splittedMessage.length > 2 ? splittedMessage[2] : "";
			this.propertyValue = splittedMessage.length > 3 ? splittedMessage[3] : "";
		}
	}
	
	public String getMessageType() {
		return this.messageType;
	}
	
	public String getContentProvider() {
		return this.contentProvider;
	}
	
	public String getProperty() {
		return this.property;
	}
	
	public String getPropertyValue() {
		return this.propertyValue;
	}
	
}
