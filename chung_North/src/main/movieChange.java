package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class movieChange extends BaseFrame{

	JPanel borderPanel = new cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	JPanel inforPanel = new cp(new BorderLayout(10,10), null, null);
	JButton change = new cb("수정").BackColor(sp.color).fontColor(Color.white).size(100, 30);
	JComboBox<String> category = new JComboBox<String>() {{
		for(Row row : Query.select("SELECT * FROM moviedb.genre;")) {
			addItem(row.getString(1));
		}
	}};
	Row movie;
	JComboBox<String> age = new JComboBox<String>("ALL, 12, 15, 19".split(", "));
	int mno = 0;
	JTextField name = new JTextField();
	JTextArea infor = new ca("").setting();
	JPanel 설명 = new cp(new BorderLayout(), sp.com(sp.em(5, 1, 1, 0), sp.line), null) {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.clearRect(10, 0, 25, 15);
			g.setColor(Color.black);
			g.setFont(sp.font(1, 10));
			g.drawString("설명", 10, 10);
			
		}
	};
	
	movieChange(int mno){
		this.mno = mno;
		movie = Query.select("SELECT * FROM moviedb.movie where m_no = ?;", mno).get(0);
		setFrame("영화수정", 600 + 16, 300 + 39, ()->{});
	}
	
	@Override
	protected void desing() {
		category.setSelectedIndex(movie.getInt(5) - 1);
		borderPanel.add(new cp(new FlowLayout(), sp.em(3, 0, 0, 0), null) {{
			add(new cl(sp.getImg("datafiles/movies/" + mno + ".jpg", 140, 200)) {{
				setVerticalAlignment(JLabel.TOP);
			}}.setBorders(sp.line));
		}}, sp.w);
		borderPanel.add(inforPanel);
		name.setPreferredSize(new Dimension(name.getX(), 35));
		name.setBorder(sp.line);
		name.setText(movie.getString(1));
		inforPanel.add(name, sp.n);
		
		inforPanel.add(설명);
		JPanel southP = new cp(new BorderLayout(), null, null);
		southP.add(change, sp.e);
		borderPanel.add(southP, sp.s);
		add(borderPanel);
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new movieChange(1);
	}
}
