package launcher.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import launcher.main.Loader;

public class LaunchMainAppView extends JPanel implements ActionListener, PropertyChangeListener {
	private static JFrame frame;
	private JProgressBar progressBar;
	private JButton startButton;
	private JTextArea taskOutput;
	private Task task;
	private static Loader loader;

	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			LoadingData loadingData = loader.getLoadingData();
			// Initialize progress property.
			int progress = 0;
			setProgress(progress);
			while (progress < 100) {
				progress = loadingData.getLoadPercentage();
				setProgress(progress);
			}
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			startButton.setEnabled(true);
			setCursor(null); // turn off the wait cursor
			taskOutput.append("Done!\n");
			frame.setVisible(false);
			frame.dispose();
		}
	}

	public LaunchMainAppView() {
		super(new BorderLayout());
		// Create the demo's UI.
		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		taskOutput = new JTextArea(5, 20);
		taskOutput.setSize(300, 300);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);

		JPanel panel = new JPanel();
		// panel.add(startButton);
		panel.add(progressBar);
		panel.setSize(400, 200);
		add(panel, BorderLayout.PAGE_START);
		JScrollPane scrollPane = new JScrollPane(taskOutput);
		scrollPane.setSize(200, 300);
		add(scrollPane, BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(70, 70, 70, 70));
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(new Runnable() {

			public void run() {
				loader.load();

			}
		});
		task = new Task();
		task.addPropertyChangeListener(this);

		task.execute();
	}

	/**
	 * Invoked when the user presses the start button.
	 */
	public void actionPerformed(ActionEvent evt) {
		startButton.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// Instances of javax.swing.SwingWorker are not reusuable, so
		// we create new instances as needed.
		// task = new Task();
		// task.addPropertyChangeListener(this);
		// task.execute();
	}

	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			taskOutput.append(loader.getLoadingData().getInfo());
		}
	}

	/**
	 * Create the GUI and show it. As with all GUI code, this must run on the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("ProgressBarDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(1980, 1080);
		// frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
		// dim.height / 2 - frame.getSize().height / 2);
		frame.setTitle("PCA Loader  ---- Gökhan Özgözen");
		// Create and set up the content pane.
		JComponent newContentPane = new LaunchMainAppView();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
		loader = new Loader();
		while (!loader.getDone().get())
			;
		System.out.println("Closing loader...");
		System.exit(0);
	}
}
