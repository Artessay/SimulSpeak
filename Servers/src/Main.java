import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.StringRefAddr;

//import com.github.sarxos.webcam.Webcam;


public class Main {
	
	public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        //单线程池
        ExecutorService es = Executors.newSingleThreadExecutor();
        try {
            //创建客户端
        //	Webcam webcam=Webcam.getDefault();
        //	webcam.open();
        //	BufferedImage imgs=webcam.getImage();


        	
//            Socket socket = new Socket("localhost", 9999);
//           // System.out.println("服务器连接成功！");
//            //构建输出输入流
//            ObjectOutputStream oOut = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream oIn = new ObjectInputStream(socket.getInputStream());
//            //Packet(long id1, long id2, String type, byte[] instream, String ip, String port,String address)
//            Packet message = new Packet(222, 222, 0, 0, "apply", null, "", "", "");
//            //发送给服务器
//            oOut.writeObject(message);
//            //服务器返回 欢迎信息
//            message = (Packet) oIn.readObject();
//            //打印服务器返回的信息
//            String a = message.GetIp();
//            String b = message.GetPort();
//            
//            System.out.println(a + " - " + b);
            
          //  int port = Integer.parseInt(b);
            Socket socket1 = new Socket("localhost", 8888);
            System.out.println("store 服务器连接成功！");
            //构建输出输入流
           // ObjectOutputStream oOut1 = new ObjectOutputStream(socket1.getOutputStream());
            DataInputStream oIn1 = new DataInputStream(socket1.getInputStream());
            
            DataOutputStream outputStream = new DataOutputStream(socket1.getOutputStream());
           // Packet message1 = new Packet(1, 1, 0, 0, "storeVideo", null, "", "", "");
            outputStream.writeUTF("applyVideo");
            outputStream.writeLong(1);
            outputStream.writeLong(1);
        //    File imgFile = new File("C:\\Users\\86173\\Desktop\\test.mp4");
//    		long lengths = 0;
//            try {
//                DataInputStream inStream = null;// 定义数据输入流对象
//                if (imgFile != null) {
//                    lengths = imgFile.length();// 获得选择图片的大小
//                    inStream = new DataInputStream(new FileInputStream(imgFile));// 创建输入流对象
//                } 
//                outputStream.writeLong(lengths);// 将文件的大小写入输出流
//                byte[] bt = new byte[(int) lengths];// 创建字节数组
//                int len = -1;
//                while ((len = inStream.read(bt)) != -1) {// 将图片文件读取到字节数组
//                	outputStream.write(bt);
//                }
//                System.out.println("文件发送成功！！");
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            String a = oIn1.readUTF();
//            System.out.println(a);
    	
    		
            long lengths = oIn1.readLong();
            System.out.println(lengths);
            if(lengths>0){
             //   String fileName = oIn1.readUTF();
                // 创建字节数组
                byte[] bt = new byte[(int) lengths];
                System.out.println(lengths);
                for (int i = 0; i < bt.length; i++) {
              //  	System.out.println(lengths);
                    bt[i] = oIn1.readByte();// 读取字节信息并存储到字节数组
               //     System.out.println(lengths);
                }
                File img=new File("C:\\Users\\86173\\Desktop\\video1.mp4");
                OutputStream out=new DataOutputStream(new FileOutputStream(img));
                System.out.println(lengths);
                out.write(bt);
                System.out.println("文件接收成功！！");
            }
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	

}
