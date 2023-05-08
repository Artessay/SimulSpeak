import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class mysql {
	Connection con; // 声明Connection对象
    public static String user;
    public static  String password;
    public Connection getConnection() { // 建立返回值为Connection的方法
        try { // 加载数据库驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        user = "root";//数据库登录名
        password = "liu991698";//密码
        try { // 通过访问数据库的URL获取数据库连接对象
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?user=root", user, password);
            System.out.println("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con; // 按方法要求返回一个Connection对象
    }
    
    public static void main(String[] args) throws SQLException {
    	mysql sql = new mysql();
    	sql.getConnection();
    	Statement a = sql.con.createStatement();
    	String init = "use datas";
    	a.execute(init);
    	String statement = "select ip, iport from Storages\r\n"
				+ "where storageId in (\r\n"
				+ "select storageId from VideoD\r\n"
				+ "where userId = '" 
				+ "123" + "' and videoId = '" + "123" + "'\r\n"
						+ ");";
		ResultSet res = a.executeQuery(statement);
		while (res.next()){
            System.out.print(res.getString(1) + "\t");
            System.out.print(res.getString(2) + "\t");
            System.out.println();
        }
        res.close();
    }
}
