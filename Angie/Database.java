import java.sql.*;

public class Database {
    private Connection conn;
    
    public Database(String host, String user, String password, String database, String charset) {
        try {
            host = "127.0.0.1";
            password = "qrh";
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?useUnicode=true&characterEncoding=" + charset, user, password);
        } catch (SQLException e) {
            System.out.println("[ERROR] database connection failed");
        }
    }
    
    public int insertAndGetIDsql(String sql) {
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            int id = -1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            conn.commit();
            stmt.close();
            return id;
        } catch (SQLException e) {
            System.out.println("[ERROR] database insert failed");
            return -1;
        }
    }
    
    public void updatesql(String sql) {
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.commit();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("[ERROR] database update failed");
        }
    }
    
    public void deletesql(String sql) {
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.commit();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("[ERROR] database delete failed");
        }
    }
}
