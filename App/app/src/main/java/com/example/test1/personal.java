package com.example.test1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class personal extends Activity
{
    public String User_id=MainActivity.User_id;
    private communication com=new communication();
    private SearchAdapter searchAdapter;
    private String title_;
    private RecyclerView mRV;
    private List<String> values;
    private List<Drawable> images=new ArrayList<>();
    private List<String> contents=new ArrayList<>();
    private  List<String> times=new ArrayList<>();
    private List<String> uid=new ArrayList<>();
    private List<String> vid=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal);
        TextView username=findViewById(R.id.username);
        username.setText(MainActivity.User_name);

        class GetUserVideoThread extends Thread{
            @Override
            public void run() {
                try {
                    List<String> Urls=com.GetUserVideo();

                    List<Drawable> images_=new ArrayList<>();
                    List<String> contents_=new ArrayList<>();
                    List<String> times_=new ArrayList<>();
                    List<String> uid_=new ArrayList<>();
                    List<String> vid_=new ArrayList<>();

                    for(int i=0;i<Urls.size();i+=5)
                    {
                        images_.add(i/5,com.GetImage(Urls.get(i)));
                        contents_.add(i/5,Urls.get(i+1));
                        times_.add(i/5,Urls.get(i+2));
                        uid_.add(i/5,Urls.get(i+3));
                        vid_.add(i/5,Urls.get(i+4));
                    }
                    images=images_;
                    contents=contents_;
                    times=times_;
                    uid=uid_;
                    vid=vid_;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        GetUserVideoThread GetUser= new GetUserVideoThread();
        GetUser.start();
        try {
            GetUser.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //利用LinearLayout控件的id获得此控件的对象
        mRV = findViewById(R.id.rv);

        mRV.setLayoutManager(new LinearLayoutManager(personal.this));

        searchAdapter=
                (new SearchAdapter(personal.this,new SearchAdapter.OnItemClickListener(){
                    @Override
                    public void onClick(int pos){
                        Toast.makeText(personal.this,"click..."+pos,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(personal.this,VideoPlay.class);

                        String videoid=vid.get(pos);
                        intent.putExtra("videoid",vid.get(pos));

                        if(videoid!="")startActivity(intent);
                        else Toast.makeText(personal.this,"该视频不存在",Toast.LENGTH_SHORT).show();

                    }
                },images,contents,times));
        mRV.setAdapter(searchAdapter);

        EditText title=new EditText(personal.this);

        ImageButton GetVideoBtn=findViewById(R.id.uploadvideo);
        GetVideoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(personal.this);
                builder.setTitle("请输入视频标题");
                builder.setView(title);
                builder.setPositiveButton("是",listener);
                builder.setNegativeButton("否",listener);
                builder.show();


            }

            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    switch (which)
                    {
                        case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                            String title_text=title.getText().toString();
                            title_=title_text;
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
                            startActivityForResult(intent, 1);
                            System.out.println(title_text);
                            break;
                        case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                            break;
                        default:
                            break;
                    }
                }
            };
        });
    }

    class UploadThread extends Thread
    {
        public String path_;
        public int operation_;
        UploadThread(String path,int operation){
            path_=path;
            operation_=operation;
        }
        @Override
        public void run()
        {
            try {
                if(operation_==0) values=com.GetUrl(title_);
                System.out.println(values.get(0));
                System.out.println(values.get(1));

                Socket socket=new Socket(values.get(0),Integer.parseInt(values.get(1)));
                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                out.writeInt(operation_);
                out.writeLong(Integer.parseInt(values.get(2)));
                out.writeLong(Integer.parseInt(values.get(3)));

                File file = new File(path_);
                out.writeLong(file.length());
                try
                {
                    FileInputStream inputStream = new FileInputStream(file);

                    byte[] buffer=new byte[1024];
                    int len = -1;

                    while((len=inputStream.read(buffer))!=-1){
                        out.write(buffer,0,len);
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e);
                }

                socket.close();
                System.out.println("文件发送成功！！");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            if(resultCode==RESULT_OK){
                Uri uri = data.getData();
                String path_=getRealPathFromURI(uri);

                UploadThread upload=new UploadThread(path_,0);
                upload.start();

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        }
        else if (requestCode==2)
        {
            if(resultCode==RESULT_OK) {
                Uri uri = data.getData();
                String path_ = getRealPathFromURI(uri);

                UploadThread upload = new UploadThread(path_,1);
                upload.start();
            }
        }
    }
}
