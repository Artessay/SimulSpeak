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
        //���̳߳�
        ExecutorService es = Executors.newSingleThreadExecutor();
        try {
            //�����ͻ���
        //	Webcam webcam=Webcam.getDefault();
        //	webcam.open();
        //	BufferedImage imgs=webcam.getImage();


        	
//            Socket socket = new Socket("localhost", 9999);
//           // System.out.println("���������ӳɹ���");
//            //�������������
//            ObjectOutputStream oOut = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream oIn = new ObjectInputStream(socket.getInputStream());
//            //Packet(long id1, long id2, String type, byte[] instream, String ip, String port,String address)
//            Packet message = new Packet(222, 222, 0, 0, "apply", null, "", "", "");
//            //���͸�������
//            oOut.writeObject(message);
//            //���������� ��ӭ��Ϣ
//            message = (Packet) oIn.readObject();
//            //��ӡ���������ص���Ϣ
//            String a = message.GetIp();
//            String b = message.GetPort();
//            
//            System.out.println(a + " - " + b);
            
          //  int port = Integer.parseInt(b);
            Socket socket1 = new Socket("localhost", 8888);
            System.out.println("store ���������ӳɹ���");
            //�������������
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
//                DataInputStream inStream = null;// ������������������
//                if (imgFile != null) {
//                    lengths = imgFile.length();// ���ѡ��ͼƬ�Ĵ�С
//                    inStream = new DataInputStream(new FileInputStream(imgFile));// ��������������
//                } 
//                outputStream.writeLong(lengths);// ���ļ��Ĵ�Сд�������
//                byte[] bt = new byte[(int) lengths];// �����ֽ�����
//                int len = -1;
//                while ((len = inStream.read(bt)) != -1) {// ��ͼƬ�ļ���ȡ���ֽ�����
//                	outputStream.write(bt);
//                }
//                System.out.println("�ļ����ͳɹ�����");
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            String a = oIn1.readUTF();
//            System.out.println(a);
    	
    		
            long lengths = oIn1.readLong();
            System.out.println(lengths);
            if(lengths>0){
             //   String fileName = oIn1.readUTF();
                // �����ֽ�����
                byte[] bt = new byte[(int) lengths];
                System.out.println(lengths);
                for (int i = 0; i < bt.length; i++) {
              //  	System.out.println(lengths);
                    bt[i] = oIn1.readByte();// ��ȡ�ֽ���Ϣ���洢���ֽ�����
               //     System.out.println(lengths);
                }
                File img=new File("C:\\Users\\86173\\Desktop\\video1.mp4");
                OutputStream out=new DataOutputStream(new FileOutputStream(img));
                System.out.println(lengths);
                out.write(bt);
                System.out.println("�ļ����ճɹ�����");
            }
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	

}
