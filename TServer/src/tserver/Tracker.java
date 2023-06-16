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
        //����ͻ��˴�����߳�
        Vector<UserThread> vector = new Vector <>();
        //�̶���С���̳߳أ���������ͬ�ͻ���
        ExecutorService es = Executors.newFixedThreadPool(1000);
        //�����������˵�Socket
        try {
            ServerSocket server = new ServerSocket(9999);
            System.out.println("�����������������ڵȴ�����...");
            while(true){
                //���ܿͻ��˵�Socket����û�У���������
                Socket socket = server.accept();
                //ÿ��һ���ͻ��ˣ�����һ���̴߳�����
                System.out.println("get one client");
                UserThread user = new UserThread(socket);
                vector.add(user);
                es.execute(user);  //�����߳�
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
/**
 * �ͻ��˴����̣߳�
 */
class UserThread implements Runnable{
	
    private Socket socket; //��ǰ�ͻ��˵�Socket
    private DataInputStream oIn = null;    //������
    private DataOutputStream oOut = null;  //�����
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



