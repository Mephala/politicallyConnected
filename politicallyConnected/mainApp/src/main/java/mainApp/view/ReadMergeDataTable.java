package mainApp.view;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class ReadMergeDataTable extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ReadMergeDataTable(String mergeAsHtml) {
		setType(Type.POPUP);
		setTitle("MERGE DATA");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 701);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setBounds(10, 11, 772, 652);
		contentPane.add(scrollPane);

		JEditorPane editorPane = new JEditorPane();
		editorPane.setContentType("text/html");
		editorPane.setText(mergeAsHtml);
		scrollPane.setViewportView(editorPane);
	}
}
