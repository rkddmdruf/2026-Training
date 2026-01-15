package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connections {
	//모든 클래스에서 쓰기 때문에 제일 위에서 생성하고 쓸때마다 연결하고 끊고 사용
	//static을 사용하면 Connections.select("")이지만 생성하면 c.select("") - 줄어듬
	//import static 사용하면 static이 좋을거 같다. 
	private Connection c = null;
	private PreparedStatement ps = null;
	private ResultSet re = null;
	
	private final String[] Strings = {
			"SELECT * FROM moviedb.fb;",
			"SELECT * FROM moviedb.food;",
			"SELECT * FROM moviedb.genre;",
			"SELECT * FROM moviedb.movie;",
			"SELECT * FROM moviedb.movie_limit;",
	};
	private void connectionAndSetStatement(String query, Object...objects) throws SQLException{
		c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
		ps = c.prepareStatement(query);
		for(int i = 0; i < objects.length; i++) {
			ps.setObject(i+1, objects[i]);
		}
	}
	
	public void updateQuery(String string, Object...objects) {
		try {
			connectionAndSetStatement(string, objects);
			ps.executeUpdate();
			ps.close();
			c.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public List<data> select(String string, Object...objects){
		List<data> list = new ArrayList<>();
		try {
			connectionAndSetStatement(string, objects);
			re = ps.executeQuery();
			while(re.next()) {
				data d = new data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					d.add(re.getObject(i+1));
				}
				list.add(d);
			}
			re.close();
			ps.close();
			c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	public List<data> select(int index, Object...objects){
		List<data> list = select(Strings[index], objects);
		return list;
	}
}
class data extends ArrayList<Object>{  String getString(int index) { return this.get(index).toString(); }   }
