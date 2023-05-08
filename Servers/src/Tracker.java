import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
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
        ExecutorService es = Executors.newFixedThreadPool(10);
        //�����������˵�Socket
        try {
            ServerSocket server = new ServerSocket(9999);
            System.out.println("�����������������ڵȴ�����...");
            while(true){
                //���ܿͻ��˵�Socket����û�У���������
                Socket socket = server.accept();
                //ÿ��һ���ͻ��ˣ�����һ���̴߳�����
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
    private ObjectInputStream oIn;    //������
    private ObjectOutputStream oOut;  //�����
    private mysql sql;
    private Statement sqlST;
    
    static class parseRes{
    	long userID;
    	String videoID;
    	String type;
    }
    
    public UserThread(Socket socket) {
        this.socket = socket;
        sql = new mysql();
        sql.getConnection();
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		//	System.out.println("!!!!!");
			sqlST = sql.con.createStatement();
			sqlST.execute("use datas");
			oIn = new ObjectInputStream(socket.getInputStream());
			oOut = new ObjectOutputStream((socket.getOutputStream()));
			while(true) {
				Packet pack = (Packet)oIn.readObject();
			//	System.out.println("-----");
				Send(pack);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Send(Packet a) throws SQLException, IOException {
		switch (a.GetType()){
		case "upload": {
			QueryMinStorage();
		}
		case "apply": {
			QueryStorage(a.GetUseId(), a.GetVideoId());
		}
		case "finish": {
			Modify(a.GetUseId(), a.GetVideoId(), a.GetStorageId(), a.GetSize());
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + a.GetType());
		}
	}
	
	private void QueryMinStorage() throws SQLException, IOException {
		String statement = "select ip, iport from storages where size = (select min(size) from storages);";
		ResultSet res = sqlST.executeQuery(statement);
		SendIP(res);
	}
	
	//public Packet(long id1, long id2, String type, byte[] instream, String ip, int port)
	private void QueryStorage(Long userId, Long videoId) throws SQLException, IOException {
		String statement = "select ip, iport from Storages\r\n"
				+ "where storageId in (\r\n"
				+ "select storageId from VideoD\r\n"
				+ "where userId = '" 
				+ userId 
				+ "' and videoId = '" 
				+ videoId 
				+ "'\r\n" + ");";
		ResultSet res = sqlST.executeQuery(statement);
		SendIP(res);
	}
	
	void SendIP(ResultSet res) throws SQLException, IOException {
		String ip = "", iport = "";
		while (res.next()){
			ip = res.getString(1);
			iport = res.getString(2);
        }
        res.close();
        Packet backM = new Packet(0, 0, 0, 0, "", null, ip, iport, "");
        System.out.println(ip + " - " + iport);
        oOut.writeObject(backM);
	}
	
	public void Modify(long userId, long videoId, long storageId, long size) throws SQLException {
		String statement = "insert into VideoD value('"
				+ userId + "', '"
				+ videoId + "', '"
				+ storageId + "');";
		sqlST.execute(statement);
		
		statement = "update Storages set size = size + "
				+ size
				+ " where storageId = '222';";
		sqlST.execute(statement);
	}
}



