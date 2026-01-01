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
import java.awt.geom.Path2D;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Main extends JFrame{
	
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
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JLabel img1;
	JLabel img2;
	int n1 = 1, n2 = 2;
	List<JLabel> 예약순 = new ArrayList<>();
	List<JLabel> review = new ArrayList<>();
	List<List<Object>> list;
	List<List<Object>> list2;
	int[] adver = {6, 2, 32, 9, 18};
	int u_no = 0;
	Main(int u_no){
		this.u_no = u_no;
		if(u_no != 0) {
			Login.setText("내정보");
		}
		setNorthPanel();
		setCenterPanel();
		setSouthPanel();
		setFrame();
		setAction();
		
	}
	private Font f(int font, int size) {
		return new Font("맑은 고딕", font, size);
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
	
	private void setCenterPanel() {
		List<List<Object>> list = new ArrayList<>();
		getList(list, "select * from movie where m_no in (6, 2, 32, 9, 18);");
		JPanel panel = new JPanel(null);
		img1 = new JLabel(new ImageIcon(new ImageIcon("datafiles/advertising/" + n1 +".jpg").getImage().getScaledInstance(590, 200, Image.SCALE_SMOOTH)));
		img1.setBounds(0,0,590,200);
		img2 = new JLabel(new ImageIcon(new ImageIcon("datafiles/advertising/" + n2 + ".jpg").getImage().getScaledInstance(590, 200, Image.SCALE_SMOOTH)));
		img2.setBounds(590,0,590,200);
		panel.add(img1);
		panel.add(img2);
		borderPanel.add(panel);
		Timer t = new Timer(2000, e->{
			img1.setIcon(new ImageIcon(new ImageIcon("datafiles/advertising/" + n1 +".jpg").getImage().getScaledInstance(590, 200, Image.SCALE_SMOOTH)));
			img2.setIcon(new ImageIcon(new ImageIcon("datafiles/advertising/" + n2 +".jpg").getImage().getScaledInstance(590, 200, Image.SCALE_SMOOTH)));
			new Thread() {
				public void run() {
					for(int i = 0; i < 590; i++) {
						img1.setBounds(0-i, 0, 590, 200);
						img2.setBounds(590-i, 0, 590, 200);
						try {
							Thread.sleep(1);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			}.start();
			n1++;
			n2++;
			if(n2 == 2) {
				n1 = 1;
			}
			if(n2 == 6) {
				n2 = 1;
			}
		});
		t.start();
	}
	
	
	private void setSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		JPanel wPanel = new JPanel(new BorderLayout());
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel moviePanel = new JPanel(null);
		butPanel.add(foodKiosc);
		butPanel.add(movieAllShow);
		wPanel.add(butPanel, BorderLayout.NORTH);
		
		list = new ArrayList<>();
		getList(list, " SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
				+ "join movie on movie.m_no = reservation.m_no\r\n"
				+ "group by movie.m_no order by c desc, m_no limit 10");
		for(int i = 0; i < 10; i++) {final int index = i;
			JPanel p = new JPanel(new BorderLayout());
			JLabel img = new JLabel() {
				@Override
				public void paintComponent(Graphics g) {
					g.setFont(f(1, 20));
					g.setColor(Color.white);
					g.drawString(index+"", 10, 10);
					g.drawImage(new ImageIcon("datafiles/movies/" + list.get(index).get(0) + ".jpg").getImage(), 0, 0, 100, 140, null);
				}
			};
			예약순.add(img);
			p.add(예약순.get(예약순.size()-1));
			p.add(new JLabel(list.get(index).get(1).toString(), JLabel.CENTER) {{
				setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				setFont(f(0,11));
			}}, BorderLayout.SOUTH);
			
			p.setBounds((i * 115) + 15, 10, 100, 160);
			moviePanel.add(p);
		}
		wPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(2, 2, 0, 5)));
		wPanel.add(moviePanel);
		
		///////////////////////////////////////////
		
		list2 = new ArrayList<>();
		getList(list2, "SELECT movie.*, avg(re_star) as a FROM moviedb.review \r\n"
				+ "join movie on movie.m_no = review.m_no\r\n"
				+ "group by review.m_no order by a desc, review.m_no limit 5;");
		
		JPanel ePanel = new JPanel(null);
		ePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		for(int i = 0; i < 5; i++) {
			JPanel p = new JPanel(new BorderLayout());
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
			JLabel img = new JLabel(new ImageIcon(new ImageIcon("datafiles/movies/" + list2.get(i).get(0) + ".jpg").getImage().getScaledInstance(110, 130, Image.SCALE_SMOOTH)));
			review.add(img);
			JLabel name = new JLabel(list2.get(i).get(1).toString());
			stars.setFont(f(1, 20));
			p.add(review.get(review.size()-1));
			p.add(name, BorderLayout.SOUTH);
			stars.setHorizontalAlignment(JLabel.RIGHT);
			stars.setPreferredSize(new Dimension(0, 40));
			p.add(stars, BorderLayout.NORTH);
			ePanel.add(p);
		}
		
		southPanel.add(wPanel);
		southPanel.setPreferredSize(new Dimension(0, 210));
		southPanel.setBackground(Color.white);
		southPanel.add(ePanel);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	
	private void setFrame() {
		setTitle("메인");
		add(borderPanel);
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		int w = 600 + 16; int h = 500 + 39;
		setBounds(960 - (w / 2), 540 - (h / 2), w, h);
		setVisible(true);
	}
	
	private void getList(List<List<Object>> list, String string) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			Statement ps = c.createStatement();
			ResultSet re = ps.executeQuery(string);
			while(re.next()) {
				List<Object> l = new ArrayList<>();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					l.add(re.getObject(i+1));
				}
				list.add(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setAction() {
		for(int i = 0; i < 5; i++) {final int index = i;
			review.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(list2.get(index).get(0));
				}
			});
		}
		for(int i = 0; i < 10; i++) {final int index = i;
			예약순.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(list.get(index).get(0));
				}
			});
		}
		Login.addActionListener(e->{
			if(u_no == 0) {
				new Login();
				dispose();
			}
		});
		img1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(adver[n1-1]);
			}
		});
		img2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(adver[n2-1]);
			}
		});
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
