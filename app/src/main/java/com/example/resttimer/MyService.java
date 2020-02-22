package com.example.resttimer;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MyService extends Service {


    private float START_X, START_Y;
    private int PREV_X, PREV_Y;
    private int MAX_X = -1, MAX_Y = -1;

    static Context MainContext;

    private long btnPressTime = 0;        //더블클릭 확인용 변수
    static WindowManager wm;
    static View mView;
    static WindowManager.LayoutParams params;
    static TextView tView;
    static int widFlag = 3;     //위젯 백그라운드 작동 ( 0=실행 , 1=중지, 초기설정 = 3 )

    static int min=0;
    static int sec=0;

    static int progress=0;      //위젯 크기 설정 변수
    static int selColor=0;      //위젯 색상 설정 변수

    static TextView mtime;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        min = MainActivity.min;
        sec = MainActivity.sec;



        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        mView = inflate.inflate(R.layout.view_in_service, null);
        //팝업창에 대한 레이아웃설정
        params = new WindowManager.LayoutParams(
                /*ViewGroup.LayoutParams.MATCH_PARENT*/ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,        //WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT | Gravity.TOP;
        wm.addView(mView, params);


        mtime= (TextView) mView.findViewById(R.id.textView);
        //뷰 터치 리스너
        tView = (TextView) mView.findViewById(R.id.textView);

        final View view = (View) mView.findViewById(R.id.view);

        //view.setLayoutParams(new RelativeLayout.LayoutParams(tView.getWidth() + 20, tView.getHeight() + 5));

        view.setOnLongClickListener(mViewLongClickListener);
        view.setOnClickListener(mViewClickListener);
        view.setOnTouchListener(mViewTouchListener);

        mHandler.sendEmptyMessage(1);

    }

    public static void startWidget(Context context) {
        widFlag = 0;

        MainContext = context;
        try {
            mHandler.sendEmptyMessageDelayed(0, 100);
        } catch (Exception e) {
            System.out.println("waitLoading() 오류");
        }

    }



    static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (widFlag == 1) {
                mHandler.removeMessages(0);         //핸들러 종료
            }
            String mins ="";
            String secs="";

            if(min<10){
                mins="0"+min;
            }else
                mins=min+"";
            if(sec<10){
                secs="0"+sec;
            }else
                secs=sec+"";

            String timeset=mins+" : "+secs;
            mtime.setText(timeset);

            if(sec<=0){
                if(min<=0){
                    mHandler.removeMessages(0);
                    //종료 시간!
                }else{
                    min--;
                    sec=59;
                }
            }else{
                sec--;
            }

            System.out.println("위젯 스레드 작동중");


            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    public static void stopWidget() {
        widFlag = 1;
        mHandler.removeMessages(0);
    }


    private View.OnLongClickListener mViewLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(MyService.this, "Openning Rest Timer\nPlease, Wait a seconds",
                   Toast.LENGTH_SHORT).show();
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.resttimer");

            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            return false;
        }

    };


    //위젯 더블클릭시 메인 액티비티 실행
    private View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (System.currentTimeMillis() > btnPressTime + 1000) {
                btnPressTime = System.currentTimeMillis();
                return;
            }
            if (System.currentTimeMillis() <= btnPressTime + 1000) {    //서비스 시작
                Toast.makeText(MyService.this, "Openning Rest Timer\nPlease, Wait a seconds",
                        Toast.LENGTH_SHORT).show();

                Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.resttimer");
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }

    };


    //위젯 롱터치시 메인 액티비티 실행
    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (MAX_X == -1)
                        setMaxPosition();
                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    PREV_X = params.x;
                    PREV_Y = params.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = (int) (event.getRawX() - START_X);
                    int y = (int) (event.getRawY() - START_Y);

                    params.x = PREV_X + x;
                    params.y = PREV_Y + y;

                    optimizePosition();
                    wm.updateViewLayout(mView, params);
                    break;
            }
            return false;
        }
    };


    //위젯 이동 배치 출처
    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(matrix);

        MAX_X = matrix.widthPixels - mView.getWidth();
        MAX_Y = matrix.heightPixels - mView.getHeight();
    }

    private void optimizePosition() {
        if (params.x > MAX_X) params.x = MAX_X;
        if (params.y > MAX_Y) params.y = MAX_Y;
        if (params.x < 0) params.x = 0;
        if (params.y < 0) params.y = 0;
    }



    //종료
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wm != null) {
            if (mView != null) {
                wm.removeView(mView);
                mView = null;
            }
            wm = null;
        }
    }
}