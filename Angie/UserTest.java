// import java.sql.*;

// public class UserTest {
//     Database database;

//     public static void main(String[] args) {
//         Database database = new Database("172.17.0.1", "root", "Zju@20200512", "wenxue_db", "utf8");
//         insertTest(database);
//     }

//     public static void insertTest(Database database) {
//         int user_id = User.insertUser("一飞", "username", "一飞", "zju", database);
//         System.out.println(user_id);

//         String sql_select_info = "select * from user_info where user_id = " + user_id + ";";
//         ResultSet data = database.executesql(sql_select_info);
//         System.out.println(data);

//         String sql_select_auth = "select * from user_auth where user_id = " + user_id + ";";
//         data = database.executesql(sql_select_auth);
//         System.out.println(data);
//     }
// }