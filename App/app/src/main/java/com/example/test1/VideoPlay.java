package com.example.test1;




import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.CellSignalStrengthGsm;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class VideoPlay extends AppCompatActivity {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public communication com=new communication();
    static boolean isPlay_p_s = true;
    static  boolean play_flag = false;
    static boolean isPlay_s_n = true;
    static boolean isrender = true;

    private Map<Integer,String> trans = new HashMap<>();
    private List<Drawable> images=new ArrayList<>();
    private List<String> contents=new ArrayList<>();
    private List<String> username=new ArrayList<>();
    public int timelong;
    public String url;
    static int page_num=1;
    static int iProgress = 0;
    int iMaxProgress;
    private VideoView vi = (VideoView) findViewById(R.id.video);
    private SeekBar bar1 = (SeekBar)findViewById(R.id.seekBar0);
    private ImageButton btn1 = (ImageButton) findViewById(R.id.btn2_0);
    private ImageButton btn2 = (ImageButton) findViewById(R.id.btn2_1);
    private ImageButton btn3 = (ImageButton) findViewById(R.id.btn2_2);
    private ImageButton btn4 = (ImageButton) findViewById(R.id.btn2_3);
    private Button btn_latter = (Button) findViewById(R.id.latter_btn);
    private Button btn_former = (Button) findViewById(R.id.former_btn);
    private Button send = (Button) findViewById(R.id.send);
    private TextView show_username = (TextView) findViewById(R.id.myname);
    private TextView title = (TextView) findViewById(R.id.titlename1);
    private TextView subtitle = (TextView) findViewById(R.id.subtitle);
    private TextView page_number = (TextView) findViewById(R.id.num);
    private EditText comm = (EditText) findViewById(R.id.comm);
    private EditText review = (EditText) findViewById(R.id.comm);
    private  NestedScrollView scroll = (NestedScrollView) findViewById(R.id.sroll);
    private Thread GetVideo;
    private Thread GetSubTitle;
    private Intent intent;
    public String vid;
    public String uid;
    public String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get info from the former page
        GetInfo();

        GetVideo = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    List<String> temp=com.GetVideo(vid);
                    url = temp.get(0);
                    timelong=Integer.parseInt(temp.get(1));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }});
        GetSubTitle = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    trans=com.GetSubTitle(vid);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }});
        Thread GetComments = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    List<String> Urls=com.GetComment(vid);
                    for(int i=0;i<Urls.size();i++)
                    {
                        System.out.println(Urls.get(i));
                    }
                    for(int i=0;i<Urls.size();i+=2)
                    {
                        //images.add(i/3,com.GetImage(Urls.get(i)));
                        contents.add(i/2,Urls.get(i));
                        username.add(i/2,Urls.get(i+1));
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        GetComments.start();
        try {
            GetComments.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //set subtitle
        SetSubtitle();

        //set video info
        show_username.setText(uid);
        title.setText(content);

        //layout
        setContentView(R.layout.videoplay);

        //video part
        Uri uri = Uri.parse(url);
        MediaController mediaController=new MediaController(this);
        mediaController.setAnchorView(vi);
        mediaController.setVisibility(View.INVISIBLE);
        vi.setMediaController(mediaController);
        vi.setVideoURI(uri);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(VideoPlay.this,RecommendActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay_p_s){
                    ((ImageButton)view).setBackground(getResources().getDrawable(R.drawable.pause));
                    play_flag = true;
                    vi.start();
                    autoIncrease();

                }
                else {
                    ((ImageButton)view).setBackground(getResources().getDrawable(R.drawable.start));
                    play_flag = false;
                    vi.pause();
                }
                isPlay_p_s=!isPlay_p_s;

            }
        });
        if(isPlay_p_s){
            btn2.setBackground(getResources().getDrawable(R.drawable.start));
        }
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay_s_n){
                    ((ImageButton)view).setBackground(getResources().getDrawable(R.drawable.nsound));

                }
                else {
                    ((ImageButton)view).setBackground(getResources().getDrawable(R.drawable.sound));
                }
                isPlay_s_n=!isPlay_s_n;
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isrender){
                    ((ImageButton)view).setBackground(getResources().getDrawable(R.drawable.s_render));
                    if(trans.containsKey(iProgress)){
                        //System.out.println(iProgress);
                        subtitle.setText(trans.get(iProgress));
                    }
                }
                else {
                    ((ImageButton)view).setBackground(getResources().getDrawable(R.drawable.render));
                    subtitle.setText("");
                }
                isrender=!isrender;
            }
        });
        btn_former.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num==1);
                else{
                    page_num--;
                }
                page_number.setText("第 "+Integer.toString(page_num)+" 页");
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.post(new Runnable() {
                            public void run() {
                                scroll.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                });

            }
        });
        btn_latter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num++;
                page_number.setText("第 "+Integer.toString(page_num)+" 页");
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.post(new Runnable() {
                            public void run() {
                                scroll.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                });
            }
        });

        //process bar
        bar1.setMax(timelong);
        this.iMaxProgress = timelong;
        bar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    iProgress = bar1.getProgress();
                    vi.seekTo(iProgress*1000);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //set comments
        SetComments();

        //send comment
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = review.getText().toString();
                Thread SendComment = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            com.SendComment(comment,vid);
                            review.setText("");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });
                SendComment.start();
            }
        });
    }

    void GetInfo(){
        intent = getIntent();
        String vid=intent.getStringExtra("videoid");
        String uid=intent.getStringExtra("userid");
        String content=intent.getStringExtra("title");
    }
    void SetSubtitle(){
        InputStream input=getResources().openRawResource(R.raw.deal);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String inputLine;
        int i1=0;
        int begin=0,end=0;
        while (true) {
            try {
                if (!((inputLine = reader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(i1%3==0)begin=Integer.parseInt(inputLine);
            else if(i1%3==1) end=Integer.parseInt(inputLine);
            else {
                for(int j=begin;j<end;j++)
                {
                    trans.put(j,inputLine);
                }
            }
            i1++;
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            GetVideo.start();
            //GetSubTitle.start();
            GetVideo.join();
            //GetSubTitle.join();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    void SetComments(){
        int rand;
        for(int i=0;i<contents.size();i++)
        {
            rand=new Random(i+System.currentTimeMillis()).nextInt();
            if(rand%3==0)images.add(getResources().getDrawable(R.drawable.head1));
            else if(rand%3==1) images.add(getResources().getDrawable(R.drawable.head2));
            else images.add(getResources().getDrawable(R.drawable.head3));
        }
        for(int i=0;i<(contents.size()>10?10:contents.size());i++){
            TextView name = (TextView) findViewById(R.id.comment_user_name0+i);
            TextView com = (TextView) findViewById(R.id.comment0+i);
            ImageView pic = (ImageView) findViewById(R.id.comment_user_pic0+i);
            name.setText(username.get(i));
            com.setText(contents.get(i));
            pic.setImageDrawable(images.get(i));
        }
    }

    void autoIncrease() {
        Thread mThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (iProgress<=iMaxProgress){
                    iProgress+=1;
                    if(iProgress>iMaxProgress) {
                        iProgress=iMaxProgress;
                        isPlay_p_s = true;
                        return;
                    }
                    if(!play_flag){
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bar1.setProgress(iProgress);
                        }
                    });
                    try{
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        );
        mThread.start();
    }
}
