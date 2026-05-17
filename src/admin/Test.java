package admin;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Test {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Owner Panel Test");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1200, 760);
			frame.setLocationRelativeTo(null);
			frame.setContentPane(new AdminPanelGUI());
			frame.setVisible(true);
		});
	}
}

