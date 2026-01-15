package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class tset extends JFrame{
	
	int start = 0;
	int x = 0;
	tset(){
		JPanel panel = new JPanel(null);
		new Z_setFrame(this, "sdfsd", 500, 500);
		JPanel gPanel = new JPanel(new GridLayout(1,0, 20,20));
		for(int i = 0; i < 5; i++) {int index = i;
			gPanel.add(new JPanel() {{setBackground(new Color(index * 50));}});
		}
		gPanel.setBounds(0, 0, 1000, 500);
		panel.add(gPanel);
		add(panel);
		
		gPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				start = e.getX();
			}
		});
		gPanel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});
	}
	
	public static void main(String[] args) {
		new tset();
	}
}
