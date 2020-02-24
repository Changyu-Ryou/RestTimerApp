package com.example.resttimer;


import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
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
    static int screenFlag = 1;      //위젯 백그라운드 작동 ( 0=실행 , 1=중지 )

    static int ori_min = 0;       //변하지 않는 분
    static int ori_sec = 0;   //변하지 않는 초

    static int min = 0;
    static int sec = 0;

    static int progress = 0;      //위젯 크기 설정 변수
    static int selColor = 0;      //위젯 색상 설정 변수

    static TextView mtime;
    IntentFilter intentFilter;

    static PowerManager manager;                //화면 on off check
    static PowerManager.WakeLock wl;            //화면 on off check


    private static final int SCREEN_OFF_TIME_OUT = 1000;         //화면 종료 대기 시간
    public static int mSystemScreenOffTimeOut;             //원래 설정값


    private static Context context;
    static int screenCheck = 3;//위젯 백그라운드 작동 ( 0=실행 , 1=중지, 초기설정 = 3 )

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        ori_min = MainActivity.min;
        ori_sec = MainActivity.sec;

        min = MainActivity.min;
        sec = MainActivity.sec;

        try {
            mSystemScreenOffTimeOut = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);     //원래 스크린 자동off 설정 시간가져오기
        } catch (Settings.SettingNotFoundException e) {
            System.out.println("가져오지 못함");
            e.printStackTrace();
        }

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


        //on off 확인
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);


        mtime = (TextView) mView.findViewById(R.id.textView);
        //뷰 터치 리스너
        tView = (TextView) mView.findViewById(R.id.textView);

      //  final View view = (View) mView.findViewById(R.id.view);

        //view.setLayoutParams(new RelativeLayout.LayoutParams(tView.getWidth() + 20, tView.getHeight() + 5));

        mView.setOnLongClickListener(mViewLongClickListener);
        mView.setOnClickListener(mViewClickListener);
        mView.setOnTouchListener(mViewTouchListener);

        manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "resttimer:WAKELOCK");

        screenCheck = 0;
        mHandler.sendEmptyMessage(0);



    }


    public static boolean checkScreen(Context context) {                //화면 on off check
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        return isScreenOn;

    }

    private static void setScreenOffTimeOut() {                 //time out을 1초로
        try {
            mSystemScreenOffTimeOut = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, SCREEN_OFF_TIME_OUT);
        } catch (Exception e) {
            //Utils.handleException(e);
        }
    }

    public static void restoreScreenOffTimeOut() {           //time out을 원래 유저 셋팅값으로
        if (mSystemScreenOffTimeOut == 0) return;
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, mSystemScreenOffTimeOut);
            System.out.println("restoreScreenOffTimeOut() = "+mSystemScreenOffTimeOut);
        } catch (Exception e) {
            //Utils.handleException(e);
            System.out.println("restoreScreenOffTimeOut() 오류");
        }
    }

    static Handler mHandler = new Handler() {                   //타이머 핸들러
        public void handleMessage(Message msg) {
            if (widFlag == 1) {
                mHandler.removeMessages(0);         //핸들러 종료
            }
            restoreScreenOffTimeOut();
            String mins = "";
            String secs = "";

            if (min < 10) {
                mins = "0" + min;
            } else
                mins = min + "";
            if (sec < 10) {
                secs = "0" + sec;
            } else
                secs = sec + "";

            String timeset = mins + " : " + secs;
            mtime.setText(timeset);

            if (sec <= 0) {
                if (min <= 0) {
                    mView.setLayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    mtime.setText("Time Out\nGo back to work!!!");
                    //mView.getBackground();
                    mView.setBackground(mView.getBackground());

                    System.out.println("Time out");

                    //MainActivity.wl.acquire();
                    //MainActivity.wl.release();
                    if(screenFlag==0)
                        setScreenOffTimeOut();
                    mHandler.removeMessages(0);

                    //return;
                    //종료 시간!
                } else {
                    min--;
                    sec = 59;
                }
            } else {
                sec--;
            }

            if (!checkScreen(context)) {
                min = ori_min;
                sec = ori_sec;
                mScreenHandler.sendEmptyMessage(0);

            } else {
                // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }

            System.out.println("위젯 스레드 작동중");


        }
    };


    static Handler mScreenHandler = new Handler() {                  //화면 off시 screen on check 핸들러
        public void handleMessage(Message msg) {
            if (checkScreen(context)) {
                System.out.println("screen check1");
                mHandler.sendEmptyMessage(0);
                restoreScreenOffTimeOut();
            }else{
                System.out.println("screen check2");
                mScreenHandler.sendEmptyMessageDelayed(0, 300);
            }

        }
    };


    private View.OnLongClickListener mViewLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(MyService.this, "Openning Rest Timer\nPlease, Wait a seconds",
                    Toast.LENGTH_SHORT).show();
            restoreScreenOffTimeOut();
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
            min = ori_min;
            sec = ori_sec;

            String mins="";
            String secs ="";

            if (min < 10) {
                mins = "0" + min;
            } else
                mins = min + "";
            if (sec < 10) {
                secs = "0" + sec;
            } else
                secs = sec + "";

            String timeset = mins + " : " + secs;
            mtime.setText(timeset);
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