import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class mysql {
	Connection con; // ����Connection����
    public static String user;
    public static  String password;
    public Connection getConnection() { // ��������ֵΪConnection�ķ���
        try { // �������ݿ�������
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("���ݿ��������سɹ�");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        user = "root";//���ݿ��¼��
        password = "liu991698";//����
        try { // ͨ���������ݿ��URL��ȡ���ݿ����Ӷ���
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?user=root", user, password);
            System.out.println("���ݿ����ӳɹ�");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con; // ������Ҫ�󷵻�һ��Connection����
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
