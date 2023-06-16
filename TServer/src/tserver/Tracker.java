package tserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Tracker {

	public static void main(String[] args) {
        //保存客户端处理的线程
        Vector<UserThread> vector = new Vector <>();
        //固定大小的线程池，用来处理不同客户端
        ExecutorService es = Executors.newFixedThreadPool(1000);
        //创建服务器端的Socket
        try {
            ServerSocket server = new ServerSocket(9999);
            System.out.println("服务器以启动，正在等待连接...");
            while(true){
                //接受客户端的Socket，若没有，阻塞在这
                Socket socket = server.accept();
                //每来一个客户端，创建一个线程处理它
                System.out.println("get one client");
                UserThread user = new UserThread(socket);
                vector.add(user);
                es.execute(user);  //开启线程
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
/**
 * 客户端处理线程：
 */
class UserThread implements Runnable{
	
    private Socket socket; //当前客户端的Socket
    private DataInputStream oIn = null;    //输入流
    private DataOutputStream oOut = null;  //输出流
    private mysql sql;
    private Statement sqlST;
    long uid;
    long vid;
    long sid;
    long size;
    String operation;
        
    public UserThread(Socket socket) {
        this.socket = socket;
        sql = new mysql();
        sql.getConnection();
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			sqlST = sql.con.createStatement();
			sqlST.execute("use datas");
			oIn = new DataInputStream(socket.getInputStream());
			oOut = new DataOutputStream((socket.getOutputStream()));
			operation = oIn.readUTF();
			Send();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void Send() throws SQLException, IOException {
		switch (operation){
		case "upload": {
			System.out.println("upload");
			QueryMinStorage();
			
		}
		case "apply": {
			QueryStorage();
		}
		case "finish": {
			Modify();
		}
		case "update": {
			UpdateIP();
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + operation);
		}
	}
	
	private void QueryMinStorage() throws SQLException, IOException {
		String statement = "select ip, iport from storages where size = (select min(size) from storages);";
		ResultSet res = sqlST.executeQuery(statement);
		SendIP(res);
	}
	
	private void QueryStorage() throws SQLException, IOException {
		uid = oIn.readLong();
		vid = oIn.readLong();
		int type = oIn.readInt();
		String statement = "select url from urls where userId = '"
				+ uid
				+ "' and videoId = '"
				+ vid
				+ "' and filetype = "
				+ type + ";";
		ResultSet res = sqlST.executeQuery(statement);
		SendURL(res);
	}
	
	void SendURL(ResultSet res) throws SQLException, IOException {
		String url = "";
		while (res.next()){
			url = res.getString(1);
        }
        res.close();
        oOut.writeUTF(url);
        System.out.println("Send: " + url);
	}
	
	void SendIP(ResultSet res) throws SQLException, IOException {
		String ip = "";
		while (res.next()){
			ip = res.getString(1);
        }
        res.close();
        oOut.writeUTF(ip);
        System.out.println("Send: " + ip);
	}
	
	public void Modify() throws SQLException, IOException {
		
		uid = oIn.readLong();
		//System.out.println(uid);
		vid = oIn.readLong();
		//System.out.println(vid);
		int op = oIn.readInt();
		//System.out.println(op);
		String url = oIn.readUTF();
		//System.out.println(url);
		sid = oIn.readLong();
		//System.out.println(sid);
		size = oIn.readLong();
		//System.out.println(size);
		String statement;
		
		statement = "insert into urls value('"
				+ uid
				+ "', '"
				+ vid
				+ "', "
				+ op
				+ ", '"
				+ url
				+ "');";
		
		statement = "insert into urls value('"
				+ uid + "', '"
				+ vid + "', "
				+ op + ",'"
				+ url + "');";
		sqlST.execute(statement);
			
		statement = "update Storages set size = size + "
				+ size
				+ " where storageId = '"
				+ sid
				+ "';";
		sqlST.execute(statement);
	}
	
	public void UpdateIP() throws IOException, SQLException
	{
		sid = oIn.readLong();
		String ip = oIn.readUTF();
		String statement = "update Storages set ip = '"
				+ ip + "'"
				+ " where storageId = '"
				+ sid
				+ "';";
		sqlST.execute(statement);
	}
}



