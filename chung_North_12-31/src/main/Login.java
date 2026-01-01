package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
public class Login extends JFrame{
	
	JTextField id = new JTextField() {{setPreferredSize(new Dimension(250, 25));}};
	JTextField pw = new JPasswordField(){{setPreferredSize(new Dimension(250, 25));}};
	JButton login = new JButton("로그인") {{
		setPreferredSize(new Dimension(80, 25));
		setForeground(Color.white);
		setBackground(Color.blue);
	}};
	Login(){
		add(new JLabel("로그인") {{
			setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black), BorderFactory.createEmptyBorder(10, 10, 0, 0)));
			setFont(new Font("맑은 고딕", 1, 26));
		}}, BorderLayout.NORTH);
		setFrame();
		setLoginPanel();
		setAction();
	}
	
	private void setLoginPanel() {
		JPanel borderPanel = new JPanel(new BorderLayout());
		borderPanel.setBorder(BorderFactory.createEmptyBorder(10,10,30,10));
		JPanel ID = new JPanel(new BorderLayout());
		JPanel PW = new JPanel(new BorderLayout());
		JPanel ip = new JPanel(new GridLayout(0, 1, 10, 10));
		ID.add(new JLabel("아이디") {{
			setFont(new Font("맑은 고딕", 1, 16));
		}}, BorderLayout.WEST);
		ID.add(id, BorderLayout.EAST);
		PW.add(new JLabel("비밀번호") {{
			setFont(new Font("맑은 고딕", 1, 16));
		}}, BorderLayout.WEST);
		PW.add(pw, BorderLayout.EAST);
		ip.add(ID);
		ip.add(PW);
		borderPanel.add(ip);
		add(borderPanel);
		add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
			add(login);
		}}, BorderLayout.SOUTH);
	}
	private void setFrame() {
		setTitle("로그인");
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		int w = 400 + 16; int h = 225 + 39;
		setBounds(960 - (w / 2), 540 - (h / 2), w, h);
		setVisible(true);
	}
	
	private void setAction() {
		login.addActionListener(e->{
			String i = id.getText();
			String p = pw.getText();
			if(i.isEmpty() || p.isEmpty()) {
				JOptionPane.showMessageDialog(null, "입력하지 않은 항목이 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(i.equals("admin") && p.equals("1234")) {
				JOptionPane.showMessageDialog(null, "관리자님 환영합니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				new movieSerch(-1);
				dispose();
				return;
			}
			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
				PreparedStatement ps = c.prepareStatement("select * from user where u_id = ? and u_pw = ?");
				ps.setObject(1, i);
				ps.setObject(2, p);				
				ResultSet re = ps.executeQuery();
				if(!re.next()) {
					JOptionPane.showMessageDialog(null, "존재하는 회원이 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					id.setText("");
					pw.setText("");
					return;
				}
				JOptionPane.showMessageDialog(null, re.getString(2)+"회원님 환영합니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				new Main(re.getInt(1));
				dispose();
				return;
			} catch (Exception e2) {
			}
		});
	}
	public static void main(String[] args) {
		new Login();
	}
}
