package applicationLogic;

import org.controlsfx.control.Notifications;

import javafx.util.Duration;

public class ErrorNotification {

	public ErrorNotification(String errorDescription, String advancedDescription, Exception e) {
		Notifications.create()
        .title("Es ist ein Fehler aufgetreten")
        .text(errorDescription)
        .hideAfter(Duration.seconds(20))
        .onAction(event1 -> new ExceptionDialog(advancedDescription, e))
        .showError();
	}
}
