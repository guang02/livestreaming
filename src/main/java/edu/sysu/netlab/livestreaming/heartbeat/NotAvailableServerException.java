package edu.sysu.netlab.livestreaming.heartbeat;

public class NotAvailableServerException extends IllegalStateException {

	public NotAvailableServerException() {
		super("HAVE NO AVAILABLE SERVER!");
	}
}
