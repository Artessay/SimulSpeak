import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Storage {

	public static void main(String[] args) {
        //保存客户端处理的线程
        Vector<Usthread> vector = new Vector <>();
        //固定大小的线程池，用来处理不同客户端
        ExecutorService es = Executors.newFixedThreadPool(10);
        //创建服务器端的Socket
        try {
            ServerSocket server = new ServerSocket(8888);
            System.out.println("服务器以启动，正在等待连接...");
            while(true){
                //接受客户端的Socket，若没有，阻塞在这
                Socket socket = server.accept();
                //每来一个客户端，创建一个线程处理它
                System.out.println("yes");
                
                Usthread user = new Usthread(socket);
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
class Usthread implements Runnable{
	
    private Socket socket; //当前客户端的Socket
    private ObjectInputStream oIn;    //输入流
    private DataInputStream In;
    private DataOutputStream oOut;  //输出流
    private File imgFile = null;
    private int storageid = 1;
    
    public Usthread(Socket socket) {
        this.socket = socket;
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			In = new DataInputStream(socket.getInputStream()); 
			oOut = new DataOutputStream(socket.getOutputStream());
		//	while(true) {
				
				String type = "applyVideo";
				long userid = 1;
				long videoid = 1;
//				System.out.println("yes" + type + userid + videoid);
				
				InitAdress(userid, videoid);
				
				switch (type) {
				case "storeVideo": {
					StoreVideo(userid, videoid);
					break;
				}
                case "storeCover":{
                	StoreCover(userid, videoid);
                	break;
				}
                case "storeInfro":{
                	StoreInfro(userid, videoid);
                	break;
                }
                case "storeNewComment":{
                	StoreComment(userid, videoid);
                	break;
				}
				case "applyVideo": {
					ApplyVideo(userid, videoid);
					break;
				}
				case "applyPhoto": {
					ApplyPhoto(userid, videoid);
					break;
				}
							
				}
		//	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void InfromTracker(Long useId, Long videoId, Long size) throws UnknownHostException, IOException {
		Socket socket1 = new Socket("localhost", 9999);
		ObjectOutputStream oOut1 = new ObjectOutputStream(socket1.getOutputStream());
		Packet message1 = new Packet(useId, videoId, storageid, size, "finish", null, "", "", "");
        oOut1.writeObject(message1);
  //      socket1.close();
	}
	
	public void InitAdress(Long useId, Long videoId) throws IOException
	{
		File file = new File(".\\data\\" + useId);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(".\\data\\" + useId + "\\" + videoId);
        if(!file1 .exists()) {
            file1.mkdirs();//创建多层级目录
        }
	}
	
	public void StoreVideo(Long useId, Long videoId) throws IOException {
		String path = ".\\data\\"
				+ useId + "\\"
				+ videoId + "\\" 
				+ "video.mp4";
		StoreData(path);
	}
	
	public void StoreInfro(Long useId, Long videoId) throws IOException {
		String path = ".\\data\\"
				+ useId + "\\"
				+ videoId + "\\" 
				+ "infro.txt";
		StoreData(path);
	}
	
	public void StoreCover(Long useId, Long videoId) throws IOException {
		String path = ".\\data\\"
				+ useId + "\\"
				+ videoId + "\\" 
				+ "cover.jpg";
		StoreData(path);
	}
	
	public void StoreComment(Long useId, Long videoId) throws IOException {
		String path = ".\\data\\"
				+ useId + "\\"
				+ videoId + "\\" 
				+ "comment.txt";
		String comment = oIn.readUTF();
		FileWriter writer = new FileWriter(path, true);
        writer.write(comment);
        writer.close();
	}
	
	public void StoreData(String path) throws IOException {
		long lengths = In.readLong();
        if(lengths > 0){
            // 创建字节数组
            byte[] bt = new byte[(int) lengths];
            System.out.println("!!!!" + lengths);
            for (int i = 0; i < bt.length; i++) {
                bt[i] = In.readByte();// 读取字节信息并存储到字节数组
            }
            System.out.println("!!!!" + lengths);
            File img = new File(path);
            if(!img.exists()) img.createNewFile();
            OutputStream out = new DataOutputStream(new FileOutputStream(img));
            out.write(bt);
            System.out.println("文件接收成功！！");
        }
        oOut.writeUTF("ok!!");
	}
	
	
	
	public void ApplyVideo(Long useId, Long videoId) {
		String path = ".\\data\\"
				+ useId + "\\"
				+ videoId + "\\" 
				+ "video.mp4";
		System.out.println(path);
		sendData(path);
		
//		path = ".\\data\\"
//				+ useId + "\\"
//				+ videoId + "\\" 
//				+ "comment.txt";
//		sendData(path);
		
	}
	public void ApplyPhoto(Long useId, Long videoId) {
		String path = ".\\data\\"
				+ useId + "\\"
				+ videoId + "\\" 
				+ "infro.txt";
		sendData(path);
		path = ".\\data\\"
				+ useId + "\\"
				+ videoId + "\\" 
				+ "cover.jpg";
		System.out.println(path);
		sendData(path);
	}
	
	public void sendData(String path)
	{
		imgFile = new File(path);
		long lengths;
        try {
            DataInputStream inStream = null;// 定义数据输入流对象
            if (imgFile != null) {
                lengths = imgFile.length();// 获得选择图片的大小
                inStream = new DataInputStream(new FileInputStream(imgFile));// 创建输入流对象
            } else {
                System.out.println("没有选择文件。");
                return;
            }
            String head = "HTTP/1.1 200\n"
                    + "Content-Type: video/mpeg4\n"
            		+ "Content-Length: " + lengths
                    + "\n";
//            long headlen = head.getBytes().length;
//        //    oOut.writeLong(lengths + headlen);// 将文件的大小写入输出流
//         //   System.out.println(lengths + headlen);
            byte[] bt = new byte[(int) lengths];// 创建字节数组
            int len = -1;
            oOut.write(head.getBytes());
            while ((len = inStream.read(bt)) != -1) {// 将图片文件读取到字节数组
            	oOut.write(bt);
            }
            System.out.println("文件发送成功！！");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//		      throws ServletException, IOException {
//		    File videoFile = new File("/path/to/video.mp4"); // 视频文件位置
//		    long size = videoFile.length(); // 视频文件总大小
//		    String range = request.getHeader("Range"); // 从 Header 中获取 Range 字段，格式：bytes=0-100，表示需要传输的数据范围
//		    long start = 0, end = size - 1, contentLength = size;
//		    if (range != null && range.startsWith("bytes=") && range.contains("-")) {
//		      range = range.substring(6);
//		      String[] rangeValues = range.split("-");
//		      start = Long.valueOf(rangeValues[0]);
//		      end = rangeValues.length > 1 ? Long.valueOf(rangeValues[1]) : size - 1;
//		      contentLength = end - start + 1;
//		    }
//		    response.setContentType("video/mp4");
//		    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//		    response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + size);
//		    response.setContentLength((int) (end - start + 1));
//		    InputStream inputStream = new FileInputStream(videoFile);
//		    inputStream.skip(start);
//		    OutputStream outputStream = response.getOutputStream();
//		    byte[] buffer = new byte[4096];
//		    int bytesRead;
//		    while ((bytesRead = inputStream.read(buffer)) != -1 && contentLength > 0) {
//		      int maxWrite = Math.min(bytesRead, (int) contentLength);
//		      outputStream.write(buffer, 0, maxWrite);
//		      contentLength -= maxWrite;
//		    }
//		    inputStream.close();
//		    outputStream.close();
//		 }
//	public void play(String path, HttpServletRequest request, HttpServletResponse response) {
//        //获取视频文件流
//        FileInputStream fileInputStream = null;
//        OutputStream outputStream = null;
//        
//        outputStream = response.getOutputStream();
//        fileInputStream = new FileInputStream(new File(path));
//        byte[] cache = new byte[1024];
//        response.setHeader(HttpHeaders.CONTENT_TYPE, "video/mp4");
//        response.setHeader(HttpHeaders.CONTENT_LENGTH, fileInputStream.available() + "");
//        int flag;
//        while ((flag = fileInputStream.read(cache)) != -1) {
//            outputStream.write(cache, 0, flag);
//        }
//        outputStream.flush();
//        outputStream.close();
//        
//    }
	
}