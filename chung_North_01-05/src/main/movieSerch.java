package main;


import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class movieSerch extends JFrame{
	Connections c = new Connections();
	JLabel logo = new JLabel(new ImageIcon(new ImageIcon("datafiles/로고1.jpg").getImage().getScaledInstance(125, 50, Image.SCALE_SMOOTH)));
	JPanel borderPanel = new JPanel(new BorderLayout(15, 15)) {{
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	Font font = new Font("맑은 고딕", 0, 12);
	JButton login = new JButton("로그인") {{
		setPreferredSize(new Dimension(80, 30));
		setFont(font);
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JButton movieSerch = new JButton("영화 검색") {{
		setFont(font);
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	
	JTextField serchText = new JTextField() {{
		setPreferredSize(new Dimension(200, 25));
	}};
	JButton serch = new JButton("검색") {{
		setPreferredSize(new Dimension(80, 25));
		setFont(new Font("맑은 고딕", 0, 10));
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JComboBox<String> order = new JComboBox<>() {{
		for(String string : "전체,에매순,평점순".split(",")) addItem(string);
	}};
	JComboBox<String> category = new JComboBox<String>(){{
		addItem("전체");
		for(List<Object> l : c.select(2)) addItem(l.get(1).toString());
	}};
	List<data> list = new ArrayList<>();
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 15, 15));
	JScrollPane sc = new JScrollPane(mainPanel) {{
		setPreferredSize(new Dimension(0, 275));
		setBorder(createLineBorder(Color.black));
	}};
	int u_no = 0;
	movieSerch(int u_no){
		this.u_no = u_no;
		if(u_no != 0) { 
			login.setText("내정보");
		}
		if(u_no == -1) {
			sc.setPreferredSize(new Dimension(0, 340));
		}
		add(borderPanel);
		borderPanel.add(sc, BorderLayout.SOUTH);
		new Z_setFrame(this, u_no == -1 ? "관리자 검색" : "영화 검색", 850, 400);
		setAction();
		setMainPanel();
		setSerch();
		if(u_no != -1) {
			setSouthPanel();
		}
		revalidate();
		repaint();
	}
	
	private void setSerch() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("검색창"));
		p.add(serchText);
		p.add(serch);
		p.add(order);
		p.add(category);
		p.setPreferredSize(new Dimension(0, 25));
		borderPanel.add(p);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		list = new ArrayList<>();
		int[] cno = {category.getSelectedIndex() == 0 ? 1 : category.getSelectedIndex(),
				category.getSelectedIndex() == 0 ? 10 : category.getSelectedIndex()};
		String s = "%" + serchText.getText() + "%";
		switch (order.getSelectedIndex()) {
			case 0: {list = c.select(3); break;}
			case 1: {list = c.select(" SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
					+ "right join movie on movie.m_no = reservation.m_no\r\n"
					+ " where g_no >= ? and g_no <= ? and m_name like ? group by movie.m_no order by c desc, m_no", cno[0], cno[1], s); break;}
			case 2: {list = c.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review \r\n"
					+ "RIGHT join movie on movie.m_no = review.m_no\r\n"
					+ " where g_no >= ? and g_no <= ? and m_name like ? group by review.m_no order by a desc, review.m_no ;", cno[0], cno[1], s); break;}
		}
		
		for(int i = 0; i < list.size(); i++) {final int index = i;
			JPanel p = new JPanel(new BorderLayout(1,1));
			if(order.getSelectedIndex() == 1 && i < 10) {
				setNo(p, index);
			}
			if(order.getSelectedIndex() == 2 && i < 5) {
				setNo(p, index);
			}
			p.setPreferredSize(new Dimension(0, 220));
			p.add(new JPanel() {{
				add(new JLabel(new ImageIcon(new ImageIcon("datafiles/limits/" + list.get(index).get(2) + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH))));
			}}, BorderLayout.WEST);
			p.add(new JLabel(new ImageIcon(new ImageIcon("datafiles/movies/" + list.get(index).get(0) + ".jpg").getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH))) {{
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(u_no != -1) {
							new movieInfor(Integer.parseInt(list.get(index).get(0).toString()), u_no);
							dispose();
							return;
						}
						new movieChange(Integer.parseInt(list.get(index).get(0).toString()));
						dispose();
					}
				});
				setBorder(createEmptyBorder(5,0,0,0));
				setHorizontalAlignment(JLabel.LEFT);
				setVerticalAlignment(JLabel.TOP);
			}});
			p.add(new JTextArea(list.get(index).get(1).toString() + "\n"
					+ "3.2% | 개봉일: " + list.get(index).get(6).toString()) {{
						setLineWrap(true);
						setFont(new Font("맑은 고딕", Font.BOLD, 11));
						setFocusable(false);
					}}, BorderLayout.SOUTH);
			mainPanel.add(p);
		}
		revalidate();
		repaint();
	}
	
	private void setNo(JPanel p, int index) {
		p.add(new JLabel("No." + (index + 1)) {{
			setHorizontalAlignment(JLabel.CENTER);
			setForeground(Color.white);
			setOpaque(true);
			setBackground(new Color(255,0,0,100));
		}}, BorderLayout.NORTH);
	}
	private void setSouthPanel() {
		JPanel sPanel = new JPanel(new BorderLayout());
		sPanel.add(logo, BorderLayout.WEST);
		sPanel.add(new JPanel(new GridLayout(0, 2,5,5)) {{
			add(login);
			add(movieSerch);
		}}, BorderLayout.EAST);
		borderPanel.add(sPanel, BorderLayout.NORTH);
	}
	
	private void setAction() {
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(u_no == 0) return;
				u_no = 0;
				JOptionPane.showMessageDialog(null, "로그아웃 되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				login.setText("로그인");
				revalidate();
				repaint();
			}
		});
		login.addActionListener(e->{
			if(u_no == 0) {
				new Login();
				dispose();
				return;
			}
		});
		serch.addActionListener(e->{
			setMainPanel();
		});
		category.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				setMainPanel();
			}
		});
		order.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				setMainPanel();
			}
		});
	}
	
	public static void main(String[] args) {
		new movieSerch(1);
	}
}
