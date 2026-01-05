package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static javax.swing.BorderFactory.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Main extends JFrame{
	Connections c = new Connections();
	JLabel logo = new JLabel(new ImageIcon(new ImageIcon("datafiles/로고1.jpg").getImage().getScaledInstance(125, 50, Image.SCALE_SMOOTH)));
	Font font = f(0, 12);
	JButton Login = new JButton("로그인") {{
		setFont(font);
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JButton movieSerch = new JButton("영화 검색") {{
		setFont(font);
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JButton foodKiosc = new JButton("영화 전체보기") {{
		setFont(f(0,10));
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JButton movieAllShow = new JButton("먹거리키오스크") {{
		setFont(f(0,10));
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10,10,10,10));
	}};
	JScrollPane sc1, sc2;
	JPanel reOrder;
	JPanel reviewOrder;
	JLabel[] img = new JLabel[5];
	JLabel[] movieName = new JLabel[5];
	JLabel[] moviePD = new JLabel[5];
	
	int n = 0;
	List<JLabel> 예약순 = new ArrayList<>();
	List<JLabel> review = new ArrayList<>();
	List<data> list;
	List<data> list2;
	int[] adver = {6, 2, 32, 9, 18};
	int u_no = 0, start = 0, x = 0, start1 = 0, x1 = 0;
	int m1 = -1, m2 = -1, m3 = -1, m4 = -1;
	int sss = 0;
	private Font f(int font, int size) {
		return new Font("맑은 고딕", font, size);
	}
	
	Main(int u_no){
		this.u_no = u_no;
		if(u_no != 0) {
			Login.setText("내정보");
		}
		setNorthPanel();
		setCenterPanel();
		setSouthPanel();
		setAction();
		add(borderPanel);
		new Z_setFrame(this, "메인", 600, 510);
	}
	
	
	
	
	private void setSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(0,2,10,10));
		JPanel wPanel = new JPanel(new BorderLayout()) {{
			setBorder(createLineBorder(Color.black));
		}};
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		reOrder = new JPanel(new GridLayout(1,0,15,15));
		reOrder.setPreferredSize(new Dimension((100 * 10) + (10 * 15), 160));
		butPanel.add(foodKiosc);
		butPanel.add(movieAllShow);
		wPanel.add(butPanel, BorderLayout.NORTH);
		wPanel.add(sc1 = new JScrollPane(reOrder, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{ 
			setBorder(createEmptyBorder(10, 10, 10, 10));
			setPreferredSize(new Dimension(280, 180));
		}}, BorderLayout.WEST);
		
		list = c.select(" SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
				+ "join movie on movie.m_no = reservation.m_no\r\n"
				+ "group by movie.m_no order by c desc, m_no limit 10");
		for(int i = 0; i < 10; i++) {final int index = i;
			JPanel p = new JPanel(new BorderLayout());
			p.setPreferredSize(new Dimension(100, 160));
			int movieN = Integer.parseInt(list.get(index).getString(0));
			JLabel l = new JLabel(getImage("datafiles/movies/" + (movieN) + ".jpg", 100, 145)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(new Font("맑은 고딕", 1, 30));
					g.drawString(index + 1 + "", 0, 30);
				}
			};
			p.add(l);
			p.add(new JLabel(list.get(i).getString(1)) {{
				setHorizontalAlignment(JLabel.CENTER);
			}}, BorderLayout.SOUTH);
			reOrder.add(p);
		}
		southPanel.add(wPanel);
		
		JPanel ePanel = new JPanel(new BorderLayout());
		ePanel.setBorder(createLineBorder(Color.black));
		reviewOrder = new JPanel(new GridLayout(1, 0, 15, 15));
		reviewOrder.setPreferredSize(new Dimension((5 * 120) + (5 * 15), 200));
		list2 = c.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review \r\n"
				+ "join movie on movie.m_no = review.m_no\r\n"
				+ "group by review.m_no order by a desc, review.m_no limit 5;");
		for(int i = 0; i < 5; i++) {
			JPanel p = new JPanel(new BorderLayout());
			p.setBorder(createEmptyBorder(10, 10, 10, 10));
			p.setBounds((i * 120) + 10, 10, 110, 190);
			JLabel stars = new JLabel(list2.get(i).get(list2.get(0).size()-1).toString().substring(0, 3)) {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					int xp = 36;
					g2.setColor(Color.yellow);
					int[] x = {5+xp, 25+xp, 45+xp};
					int[] y = {5, 15, 5};
					g2.fillPolygon(x, y, 3);
					int[] x2 = {5+xp, 25+xp, 35+xp};
					int[] y2 = {25, 0, 10};
					g2.fillPolygon(x2, y2, 3);
					int[] x3 = {40+xp, 25+xp, 15+xp};
					int[] y3 = {25, 0, 10};
					g2.fillPolygon(x3, y3, 3);
				}
			};
			
			JLabel img = new JLabel(getImage("datafiles/movies/" + list2.get(i).get(0) + ".jpg", 110, 150));
			JLabel name = new JLabel(list2.get(i).get(1).toString());
			stars.setFont(new Font("맑은 고딕", 1, 20));
			stars.setHorizontalAlignment(JLabel.RIGHT);
			p.add(stars, BorderLayout.NORTH);
			img.setPreferredSize(new Dimension(110, 130));
			p.add(img);
			p.add(name, BorderLayout.SOUTH);
			reviewOrder.add(p);
		}
		sc2 = new JScrollPane(reviewOrder, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{ 
			setBorder(null);
			setPreferredSize(new Dimension(280, 200));
		}};
		sc2.getHorizontalScrollBar().setValue(20);
		ePanel.add(sc2, BorderLayout.WEST);
		southPanel.add(ePanel);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		setSCAction();
	}
	
	private void setSCAction() {
		MouseAdapter mouseA = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				start = e.getX();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				x += start - e.getX();
				sc1.getHorizontalScrollBar().setValue(x);
				start = e.getX();
			}
		};
		sc1.addMouseListener(mouseA);
		sc1.addMouseMotionListener(mouseA);
		for(int i = 0; i < 10; i++) {
			int index = i;
			reOrder.getComponent(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					start = e.getX();
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					new movieInfor(Integer.parseInt(list.get(index).getString(0)), u_no);
					dispose();
				}
			});
			reOrder.getComponent(i).addMouseMotionListener(mouseA);
		}
		
		MouseAdapter mouseB = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				start1 = e.getX();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				x1 += start1 - e.getX();
				sc2.getHorizontalScrollBar().setValue(x1);
				start1 = e.getX();
			}
		};
		sc2.addMouseListener(mouseB);
		sc2.addMouseMotionListener(mouseB);
		for(int i = 0; i < 5; i++) {
			int index = i;
			reviewOrder.getComponent(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					start1 = e.getX();
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					new movieInfor(Integer.parseInt(list2.get(index).getString(0)), u_no);
					dispose();
				}
			});
			reviewOrder.getComponent(i).addMouseMotionListener(mouseB);
		}
	}
	
	
	
	private void setNorthPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.add(logo, BorderLayout.WEST);
		JPanel gridPanel = new JPanel(new GridLayout(0, 2, 5,5));
		gridPanel.setBackground(Color.white);
		gridPanel.add(Login);
		gridPanel.add(movieSerch);
		panel.add(gridPanel, BorderLayout.EAST);
		borderPanel.add(panel, BorderLayout.NORTH);
	}
	
	private ImageIcon getImage(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	private void setCenterPanel() {
		List<data> list = new ArrayList<>();
		JPanel panel = new JPanel(null);
		for(int i = 0; i < 5; i++) {
			final int index = i;
			list.add(c.select("select * from movie where m_no = ?", adver[i]).get(0));
			img[i] = new JLabel(getImage("datafiles/advertising/" + (i+1) + ".jpg", 590, 200));;
			//메서드로 빼는게 제일 좋다고 생각합니다. *다른데에서도 사용가능하게*
			img[i].setBounds(i*590, 0, 590, 200);
			movieName[i] = new JLabel(list.get(i).getString(1));
			movieName[i].setFont(new Font("맑은 고딕", 1, 20));
			movieName[i].setForeground(Color.white);
			movieName[i].setBounds(i*30 +  (i == 0 ? 30 : 0), 80, 590, 50);
			moviePD[i] = new JLabel(list.get(i).getString(3));
			moviePD[i].setFont(new Font("맑은 고딕", 0, 16));
			moviePD[i].setForeground(Color.white);
			moviePD[i].setBounds(i*30 + (i == 0 ? 30 : 0), 100, 590, 50);
			
			panel.add(movieName[i]);
			panel.add(moviePD[i]);
			panel.add(img[i]);
		}
		
		borderPanel.add(panel);
		Timer t = new Timer(2000, e->{
			new Thread() {
				public void run() {
					for(int i = 0; i < 590; i++) {
						img[n].setBounds(-i, 0, 590, 200);
						img[n+1 >= 5 ? 0 : n+1].setBounds(590-i, 0, 590, 200);
						movieName[n].setBounds(-i + 30, 80, 590, 50);
						movieName[n+1 >= 5 ? 0 : n+1].setBounds(590-i + 30, 80, 590, 50);
						moviePD[n].setBounds(-i + 30, 100, 590, 50);
						moviePD[n+1 >= 5 ? 0 : n+1].setBounds(590-i + 30, 100, 590, 50);
						try {
							Thread.sleep(1);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
					n = n + 1;
				};
			}.start();
			if(n >= 5) n = 0;
		});
		t.start();
	}
	
	private void setAction() {
		for(int i = 0; i < 5; i++) {int index = i;
			MouseAdapter mouseAction = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new movieInfor(adver[index], u_no);
				}
			};
			moviePD[index].addMouseListener(mouseAction);
			img[index].addMouseListener(mouseAction);
			movieName[index].addMouseListener(mouseAction);
		}
		Login.addActionListener(e->{
			if(u_no == 0) {
				new Login();
				dispose();
			}
		});
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(u_no == 0) return;
				u_no = 0;
				JOptionPane.showMessageDialog(null, "로그아웃 되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				Login.setText("로그인");
			}
		});
	}
	public static void main(String[] args) {
		new Main(0);
	}
}
