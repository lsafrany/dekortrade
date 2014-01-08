package hu.dekortrade99.client;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Progressbar;

public class DisplayRequest {

	private static Progressbar progressBar = new Progressbar();
	private static int progressBarValue = ClientConstants.PROGRESS_STOP;
	private static Label statusLabel = new Label();

	private static int progressCount = 0;

	public static Progressbar getProgressBar() {
		return progressBar;
	}

	public static void setProgressBar(Progressbar progressBar) {
		DisplayRequest.progressBar = progressBar;
	}

	public static int getProgressBarValue() {
		return progressBarValue;
	}

	public static void setProgressBarValue(int progressBarValue) {
		DisplayRequest.progressBarValue = progressBarValue;
	}

	public static Label getStatusLabel() {
		return statusLabel;
	}

	public static void setStatusLabel(Label statusLabel) {
		DisplayRequest.statusLabel = statusLabel;
	}

	public static void counterInit() {
		progressCount = 0;
	}

	public static void startRequest() {
		progressCount++;
		progressBarValue = ClientConstants.PROGRESS_START;
	}

	public static void serverResponse() {
		progressCount--;
		if (progressCount < 1) {
			progressBarValue = ClientConstants.PROGRESS_STOP;
			progressBar.setPercentDone(0);
			statusLabel.setContents("&nbsp;");
		}
	}
}
