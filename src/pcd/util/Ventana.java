package pcd.util;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Ventana extends JFrame {
		JScrollPane scrollPane;
		JTextArea textArea;
		public Ventana(String title){
			BorderLayout bl = new BorderLayout();
			this.getContentPane().setLayout(bl);
			scrollPane = new JScrollPane();
			this.getContentPane().add(scrollPane, BorderLayout.CENTER);
			textArea = new JTextArea();
			textArea.setMargin( new Insets(7,7,7,7) );
			scrollPane.setViewportView(textArea);
			setSize(500, 400);
			setTitle(title);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setVisible(true);
		}
        public void addText(String str) {
        	textArea.append("\n " + str);
        }
}