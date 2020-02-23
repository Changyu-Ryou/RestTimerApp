package com.example.resttimer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Frag1_Timer extends Fragment // Fragment 클래스를 상속받아야한다
{
    int minute;
    int second;

    private AdView mAdView;
    private View view;
    private onButtonClickListener mListener;
    TextView min;
    TextView sec;

    public void setOnButtonClickListener(onButtonClickListener listener) {
        mListener = listener;
    }

    interface onButtonClickListener {
        void onButtonClick(int Button);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup View = (ViewGroup) inflater.inflate(R.layout.frag1, container, false);

        // admob
        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");

        mAdView = View.findViewById(R.id.adView); // view.로 접근하여 findViewById를 해야함.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        Button btn1 = (Button) View.findViewById(R.id.startBtn);
        Button btn2 = (Button) View.findViewById(R.id.stopBtn);

        Button upmin = (Button) View.findViewById(R.id.up_min);
        Button upsec = (Button) View.findViewById(R.id.up_sec);
        Button downmin = (Button) View.findViewById(R.id.down_min);
        Button downsec = (Button) View.findViewById(R.id.down_sec);


        min = (TextView) View.findViewById(R.id.min);
        sec = (TextView) View.findViewById(R.id.sec);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClick(1);
                //getActivity().startService(new Intent(getActivity(), MyService.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClick(2);

            }
        });



        upmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minute = MainActivity.min;
                minute++;
                if(minute>=100)
                    minute=99;
                MainActivity.min = minute;

                min.setText(addZero(minute));

            }
        });

        upsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second = MainActivity.sec;
                second+=5;
                if(second>=60)
                    if(minute==99) {
                        second = 55;
                    }
                    else {
                        minute++;
                        second=0;
                    }
                MainActivity.min = minute;
                MainActivity.sec = second;

                min.setText(addZero(minute));
                sec.setText(addZero(second));

            }
        });

        downmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minute = MainActivity.min;
                minute--;
                if(minute<0)
                    minute=0;

                MainActivity.min = minute;

                min.setText(addZero(minute));

            }
        });

        downsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second = MainActivity.sec;
                second-=5;
                if(second<0){
                    if(minute<=0)
                        second=0;
                    else {
                        minute--;
                        second=55;
                    }
                }

                MainActivity.min = minute;
                MainActivity.sec = second;

                min.setText(addZero(minute));
                sec.setText(addZero(second));
            }
        });


        return View;

    }

    public void onResume() {

        super.onResume();
        min.setText(addZero(minute));
        sec.setText(addZero(second));

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try {
            mListener = (onButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(((Activity) context)
                    .getLocalClassName() + "");
        }
    }

    public String addZero(int a){               //0 추가해주는 작업
        String add="";
        if(a<10){
            add="0"+a;
        }else
            add=a+"";
        return add;
    }


}