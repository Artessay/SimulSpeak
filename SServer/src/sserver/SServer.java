package sserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SServer {
	static long sid = 222;
    
	public static void main(String[] args) throws IOException {
		ExecutorService es = Executors.newFixedThreadPool(100);
		informTracker(getLANAddressOnWindows().getHostAddress());
		ServerSocket server = new ServerSocket(7777);
		System.out.println("s服务器以启动，正在等待连接...");
		while(true){
	          //接受客户端的Socket，若没有，阻塞在这
	        Socket socket = server.accept();
	        UStoreThread user = new UStoreThread(socket, sid);
	       	es.execute(user);
		}
    }
	

	public static InetAddress getLANAddressOnWindows() {
	    try {
	        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
	        while (nifs.hasMoreElements()) {
	            NetworkInterface nif = nifs.nextElement();
	 
	            if (nif.getName().startsWith("wlan")) {
	                Enumeration<InetAddress> addresses = nif.getInetAddresses();
	 
	                while (addresses.hasMoreElements()) {
	 
	                    InetAddress addr = addresses.nextElement();
	                    if (addr.getAddress().length == 4) { // 速度快于 instanceof
	                        return addr;
	                    }
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        ex.printStackTrace(System.err);
	    }
	    return null;
	}
	
	public static void informTracker(String ip) throws UnknownHostException, IOException {
		 Socket socket = new Socket("192.168.137.87", 9999);
		 DataInputStream in = new DataInputStream(socket.getInputStream());
         DataOutputStream out = new DataOutputStream(socket.getOutputStream());
         out.writeUTF("update");
         out.writeLong(sid);
         out.writeUTF(ip);
	}
	
}


class UStoreThread implements Runnable{
	
    private Socket socket; //当前客户端的Socket
    private DataInputStream In;
    private DataOutputStream Out;  //输出流
    private long userid;
    private long videoid;
    private long sid;
    private int operation;
    private long length;
    static String ip = "192.168.137.1";
    private String backname;
    
    public UStoreThread(Socket socket, long sid) {
        this.socket = socket;
        this.sid = sid;
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			In = new DataInputStream(socket.getInputStream());
			Out = new DataOutputStream(socket.getOutputStream());
			operation = In.readInt();
			userid = In.readLong();
			videoid = In.readLong();
			length = In.readLong();
			System.out.println(length);
			System.out.println("get one");
			storeData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void storeData() throws IOException {
		String path;
		if(operation == 0) {
			path = ".\\data\\" + "videos\\";
			backname = randomName();
			path = path + backname + ".mp4";
		}
		else if(operation == 1) {
			path = ".\\data\\" + "photos\\";
			backname = randomName();
			path = path + backname + ".jpg";
		}
		else {
			path = ".\\data\\" + "txtfiles\\";
			backname = randomName();
			path = path + backname + ".txt";
		}
		StoreData(path);
	}
	
	public void InfromTracker(Long size, String url, int operation) throws UnknownHostException, IOException {
		Socket socket1 = new Socket("192.168.137.87", 9999);
		DataOutputStream oOut = new DataOutputStream(socket1.getOutputStream());
		oOut.writeUTF("finish");
		oOut.writeLong(userid);
		oOut.writeLong(videoid);
		oOut.writeInt(operation);
		oOut.writeUTF(url);
		oOut.writeLong(sid);
		oOut.writeLong(size);
	}
	
	public String randomName() {
		String codes = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    Random random = new Random();
	    StringBuilder randomStr = new StringBuilder();
	    int n = random.nextInt(6) + 6;
	    for (int i = 0; i < n; i++) {
	        randomStr.append(codes.charAt(random.nextInt(62)));
	    }
	    return randomStr.toString();
		
	}
	
	public void StoreData(String path) throws IOException {
		FileOutputStream fileOutput = new FileOutputStream(path);
		if(operation == 0 || operation == 1) {
	         // 定义一个缓冲区，用于从输入流中读取数据
	         byte[] buffer = new byte[1024];
	         int len = -1;
	         // 从输入流中读取数据并写入文件输出流中，保存接收的数据
	         while ((len = In.read(buffer)) > 0) {
	             fileOutput.write(buffer, 0, len);
	         }
	    
		}
		else {
			String title = In.readUTF();
			fileOutput.write(title.getBytes());
			fileOutput.close();
		}
        fileOutput.close();
         if(operation == 0) {
        	 String soundaddr = ".\\data\\sounds\\" + backname + ".mp3";
             videoGetMp3(path, soundaddr);
         }
         System.out.println("!!1!");
         String url;
         if(operation == 0) {
        	 url = "http://" + ip + ":80/" + "videos/" + backname + ".mp4";
        	 System.out.println(url + length);
        	 InfromTracker(length, url, operation);
        	 url = "http://" + ip + ":80/" + "sounds/" + backname + ".mp3";
        	 InfromTracker((long) 0, url, 3);
         }
         else if(operation == 1) {
        	 url = "http://" + ip + ":80/" + "photos/" + backname + ".jpg";
        	 InfromTracker(length, url, operation);
         }
         else {
        	 url = "http://" + ip + ":80/" + "txtfiles/" + backname + ".txt";
        	 InfromTracker(length, url, operation);
         }
	}
	
	public void videoGetMp3(String videoAddr, String soundAddr){
        // 提取命令
        String commit = "$0 -i $1 -ss 00:00:01 -t 00:01:27.0 -vn -b:a 192k -ar 44100 -ac 2 -acodec libmp3lame -y $2";
        String ffmpegPath = ".\\ffmpeg\\ffmpeg.exe";
        String str = commit.replace("$0", ffmpegPath)
                .replace("$1", videoAddr)
                .replace("$2", soundAddr);
        System.out.println(str);
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proce = runtime.exec(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}