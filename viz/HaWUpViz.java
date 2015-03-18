package hawup.viz;

import hawup.core.Node;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class HaWUpViz extends JFrame {


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HaWUpViz frame = new HaWUpViz();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public HaWUpViz() {
		this(genNodes(5),10);
	}
	
	private static Node[] genNodes(int num) {
		Node[] ans = new Node[num];
		for (int i=0; i < num; ++i) {
			ans[i] = new Node(i, 5);
		}
		return ans;
	}

	/**
	 * Create the frame.
	 */
	public HaWUpViz(Node[] nodes, int maxQueueSize) {
	}
	
}
