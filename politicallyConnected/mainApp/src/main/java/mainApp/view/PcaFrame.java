package mainApp.view;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mainApp.utils.AppUtils;

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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PcaFrame frame = new PcaFrame();
					frame.setTitle("Politically Connected Software --- Gokhan Ozgozen");
					ImageIcon img = new ImageIcon(AppUtils.getResourcePath("conversionIcon.png"));
					frame.setIconImage(img.getImage());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PcaFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		WordToExcelPanel wtep = WordToExcelPanel.getInstance();
		add(wtep);
	}
}
