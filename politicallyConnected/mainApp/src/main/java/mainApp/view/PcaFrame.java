package mainApp.view;

import java.awt.EventQueue;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mainApp.utils.MainAppUtils;

public class PcaFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3418932629439881907L;

	/**
	 * Launch the application.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		if (args == null || args.length == 0) {
			JOptionPane.showMessageDialog(null, "Program doğru parametrelerle başlatılmadı.", "Olmadi Bu is", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} else {
			final String jarName = args[0];
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						System.setProperty("file.encoding", "UTF-8");
						Field charset = Charset.class.getDeclaredField("defaultCharset");
						charset.setAccessible(true);
						charset.set(null, null);
						final PcaFrame frame = new PcaFrame();
						frame.setTitle("Politically Connected Software --- Gokhan Ozgozen");
						ImageIcon img = MainAppUtils.createImageIcon("/Hero_Adelaide.png", "Adelaide");
						if (img != null)
							frame.setIconImage(img.getImage());
						frame.addWindowListener(new java.awt.event.WindowAdapter() {
							@Override
							public void windowClosing(java.awt.event.WindowEvent windowEvent) {
								Executors.newCachedThreadPool().submit(new Runnable() {
									public void run() {
										try {
											Runtime.getRuntime().exec("java -jar pcaLoader.jar " + jarName);
										} catch (IOException e) {
											JOptionPane.showMessageDialog(null, "Reeskont");
										}
									}
								});
							}
						});
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Create the frame.
	 */
	public PcaFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 550);
		JTabbedPane tabPane = new JTabbedPane();
		WordToExcelPanel wtep = WordToExcelPanel.getInstance();
		tabPane.add("Dökümandan Oluştur", wtep);
		CreateFromWebPanel cfwp = CreateFromWebPanel.getInstance();
		tabPane.add("Veritabanindan Oluştur", cfwp);
		UploadManagerView uploadManagerView = UploadManagerView.getInstance();
		tabPane.add("Manual Upload", uploadManagerView);
		SearchPcaDataView searchView = SearchPcaDataView.getInstance();
		tabPane.add("Arama", searchView);
		PandMAnalyzerView pAndManalyzerView = PandMAnalyzerView.getInstance();
		tabPane.add("Merging", pAndManalyzerView);
		ReadFromExcelPane readFromExcelPane = ReadFromExcelPane.getInstance();
		tabPane.add("Excelden Okuma", readFromExcelPane);
		add(tabPane);
	}
}
