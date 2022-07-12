package com.deane.atmapp.exception;

import java.time.LocalDateTime;

public class ErrorResponseDetails {

	private LocalDateTime timeDate;
	private String message;
	private int status ;

	public ErrorResponseDetails(LocalDateTime timeDate, String message, int status) {
		this.timeDate = timeDate;

		this.message = message;

		this.status = status;
	}

	public LocalDateTime getTimeDate() {
		return timeDate;
	}

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}

}
