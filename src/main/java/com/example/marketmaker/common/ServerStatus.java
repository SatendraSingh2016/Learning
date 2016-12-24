package com.example.marketmaker.common;

public enum ServerStatus {
	RUNNING(true);
	
	private volatile boolean running;
	
	ServerStatus(boolean running){
		this.running = running;
	}
	
	public boolean isRunning() {
		return this.running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
