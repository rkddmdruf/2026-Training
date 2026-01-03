package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import main.Connections.data;

public class movieChange extends JFrame{
	Connections c = new Connections();
	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JComboBox<String> category = new JComboBox<String>() {{
		for(List<Object> list : c.getData(2)) addItem(list.get(1).toString());
	}};
	JComboBox<String> age = new JComboBox<String>() {{
		for(List<Object> list : c.getData(4)) addItem(list.get(1).toString());
	}};
	JTextField name = new JTextField();
	JTextArea infor = new JTextArea() {{
		setBorder(BorderFactory.createLineBorder(Color.black));
	}};
	TitledBorder inforBorder = new TitledBorder(BorderFactory.createLineBorder(Color.black), "설명", TitledBorder.LEFT,TitledBorder.TOP);
	JScrollPane sc = new JScrollPane(infor) {{
		setBorder(BorderFactory.createCompoundBorder(inforBorder, BorderFactory.createEmptyBorder(5,2,2,2)));
	}};
	JButton change = new JButton("수정") {{
		setPreferredSize(new Dimension(100, 30));
		setForeground(Color.white);
		setBackground(Color.blue);
	}};
	List<data> list = new ArrayList<>();
	int m_no = 0;
	movieChange(int m_no){
		this.m_no = m_no;
		list = c.getData("select * from movie where m_no = ?", m_no);
		new Z_setFrame(this, "영화 수정", 700, 350);
		age.setSelectedIndex(Integer.parseInt(list.get(0).get(2).toString())-1);
		category.setSelectedIndex(Integer.parseInt(list.get(0).get(5).toString())-1);
		name.setText(list.get(0).get(1).toString());
		infor.setText(list.get(0).get(4).toString());
		
		borderPanel.add(new JLabel(new ImageIcon(new ImageIcon("datafiles/movies/" + m_no + ".jpg").getImage().getScaledInstance(200, 275, Image.SCALE_SMOOTH))) {{
			setVerticalAlignment(JLabel.TOP);
			setPreferredSize(new Dimension(200, 275));
		}},BorderLayout.WEST);
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(name, BorderLayout.NORTH);
		panel.add(sc);
		panel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			add(category);add(age);
		}}, BorderLayout.SOUTH);
		borderPanel.add(panel);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			add(change);
		}}, BorderLayout.SOUTH);
		add(borderPanel);
		setAction();
		revalidate();
		repaint();
		
	}
	
	
	private void setAction() {
		change.addActionListener(e->{
			if(name.getText() == list.get(0).get(1).toString() && infor.getText() == list.get(0).get(4).toString() 
					&& category.getSelectedIndex() == Integer.parseInt(list.get(0).get(5).toString()) && age.getSelectedIndex() == Integer.parseInt(list.get(0).get(2).toString())) {
				JOptionPane.showMessageDialog(null, "수정된 부분이 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(name.getText().isEmpty() || infor.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "빈칸이 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String[] str = "시발,개새끼,존나,병신".split(",");
			for(int i = 0; i < 4; i++) {
				if(infor.getText().contains(str[i])) {
					JOptionPane.showMessageDialog(null, "욕설을 포함하고 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			JOptionPane.showMessageDialog(null, "정보가 수정되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			c.getData("update movie set m_name = ? , l_no = ? , m_plot = ? , g_no = ? where m_no = ?;", name.getText(), age.getSelectedIndex(), infor.getText(), category.getSelectedIndex(), m_no);
			new movieSerch(-1);
			dispose();
			return;
		});
	}
	
	public static void main(String[] args) {
		new movieChange(1);
	}
}
