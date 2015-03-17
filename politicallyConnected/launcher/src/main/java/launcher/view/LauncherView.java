package launcher.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LauncherView extends JFrame {

	private final LoadingData loadingData;
	private final LauncherView launcherView;
	private final JProgressBar progressBar;
	private final JLabel progressLabel;

	public LauncherView(LoadingData loadingData) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		this.launcherView = this;
		this.loadingData = loadingData;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		this.setTitle("Politically Connected App Loader  ---- Gökhan Özgözen");
		this.setSize(700, 400);
		JPanel panel = new JPanel();
		this.add(panel);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		progressBar = new JProgressBar(0, 100);
		progressLabel = new JLabel();
		progressLabel.setSize(300, 200);
		progressLabel.setText("Checking internet connection to load the app...");
		panel.add(progressLabel);
		progressBar.setVisible(true);
		progressBar.setStringPainted(true);
		panel.add(progressBar);
		this.pack();
		this.setVisible(true);
		Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {

			public void run() {
				updateProgress();
			}
		}, 0, 50, TimeUnit.MILLISECONDS);
	}

	public synchronized JProgressBar getProgressBar() {
		return progressBar;
	}

	public synchronized JLabel getProgressLabel() {
		return progressLabel;
	}

	public synchronized LoadingData getLoadingData() {
		return loadingData;
	}

	private void updateProgress() {
		if (loadingData.getDownloadStarted().get())
			progressBar.setValue(loadingData.getLoadPercentage());
		progressLabel.setText(loadingData.getInfo());
	}

}
