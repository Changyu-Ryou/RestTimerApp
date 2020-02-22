package com.example.resttimer;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static com.example.resttimer.R.layout.support_simple_spinner_dropdown_item;

public class Frag3_Setting extends Fragment {

    Activity activity;

    private View view;
    private AdView mAdView;
    TextView widSize;
    TextView PopView;


    private View widview;
    Spinner spinner;
    Spinner TimeSpinner;
    RelativeLayout mpopView;

    int progresses = 0;
    int selColor = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //mpopView = (RelativeLayout) MyService.mView.findViewById(R.id.mPopView);
        view = inflater.inflate(R.layout.frag3,container,false);
        widSize = (TextView) view.findViewById(R.id.WidSize);
        // admob
        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");

        mAdView = view.findViewById(R.id.adView); // view.로 접근하여 findViewById를 해야함.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        PopView = (TextView) MyService.mView.findViewById(R.id.textView); //오버레이

        SeekBar sb  = (SeekBar) view.findViewById(R.id.seekBar);

        //sb.setProgress(5);
        widSize.setText("위젯 크기 : "+sb.getProgress());
        widview = (View) MyService.mView.findViewById(R.id.view);
/*
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                widSize.setText("위젯 크기 : "+seekBar.getProgress());
                PopView.setTextSize(24 + progress);
                MyService.progress = progresses;
                //mpopView.setLayoutParams(new WindowManager.LayoutParams(PopView.getWidth() + 5, PopView.getHeight()));
                widview.setLayoutParams(new RelativeLayout.LayoutParams(PopView.getWidth() + 20, PopView.getHeight() + 5));
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

*/



        if (MyService.selColor == 0 && MyService.progress == 0) {          //셋팅 없이 초기값인 경우

            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                    MyService.progress = progresses;
                    mpopView.setLayoutParams(new WindowManager.LayoutParams(PopView.getWidth() + 5, PopView.getHeight()));
                    widview.setLayoutParams(new RelativeLayout.LayoutParams(PopView.getWidth() + 20, PopView.getHeight() + 5));
                }

                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    widSize.setText("위젯 크기 : " + progress);
                    progresses = progress;

                    ViewGroup.LayoutParams wid = widview.getLayoutParams();
                    mpopView = (RelativeLayout) MyService.mView.findViewById(R.id.mPopView);

                    PopView = (TextView) MyService.mView.findViewById(R.id.textView); //오버레이
                    PopView.setTextSize(24 + progress);

                    mpopView.setLayoutParams(new WindowManager.LayoutParams(PopView.getWidth() + 5, PopView.getHeight()));
                    widview.setLayoutParams(new RelativeLayout.LayoutParams(PopView.getWidth() + 20, PopView.getHeight() + 5));
                }
            });


        } else {       //새롭게 정한 설정이 있는경우

            //System.out.println("selected set------");
            widSize.setText("위젯 크기 : " + MyService.progress);
            sb.setProgress(MyService.progress);

            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                    MyService.progress = progresses;
                    mpopView.setLayoutParams(new WindowManager.LayoutParams(PopView.getWidth() + 5, PopView.getHeight()));
                    widview.setLayoutParams(new RelativeLayout.LayoutParams(PopView.getWidth() + 20, PopView.getHeight() + 5));
                }

                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    widSize.setText("위젯 크기 : " + progress);
                    progresses = progress;
                    MyService.progress = progress;
                    widview = (View) MyService.mView.findViewById(R.id.view);
                    ViewGroup.LayoutParams wid = widview.getLayoutParams();
                    mpopView = (RelativeLayout) MyService.mView.findViewById(R.id.mPopView);

                    PopView = (TextView) MyService.mView.findViewById(R.id.textView); //오버레이
                    PopView.setTextSize(24 + progress);

                    mpopView.setLayoutParams(new WindowManager.LayoutParams(PopView.getWidth() + 5, PopView.getHeight()));
                    widview.setLayoutParams(new RelativeLayout.LayoutParams(PopView.getWidth() + 20, PopView.getHeight() + 5));
                }
            });

            spinner.setSelection(MyService.selColor);
        }



        spinner = (Spinner) view.findViewById(R.id.spinner);

        String[] colorList = new String[5];
        colorList[0] = "White";
        colorList[1] = "Black";
        colorList[2] = "Red";
        colorList[3] = "Blue";
        colorList[4] = "Pink";

        //using ArrayAdapter
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(getActivity(), support_simple_spinner_dropdown_item, colorList);
        spinner.setAdapter(spinnerAdapter);

        //event listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String aa = spinner.getSelectedItem().toString();
                selColor = spinner.getSelectedItemPosition();
                MyService.selColor = selColor;

                switch (selColor) {
                    case 0: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_white)
                        );
                        Toast.makeText(getActivity(), "selected White", Toast.LENGTH_SHORT).show();
                        widview.setBackgroundResource(R.color.White);
                        break;
                    }
                    case 1: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_black)
                        );

                        widview.setBackgroundResource(R.color.Black);
                        Toast.makeText(getActivity(), "selected Black", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 2: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_red)
                        );
                        Toast.makeText(getActivity(), "selected Red", Toast.LENGTH_SHORT).show();
                        widview.setBackgroundResource(R.color.Red);
                        break;
                    }
                    case 3: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_blue)
                        );
                        Toast.makeText(getActivity(), "selected Blue", Toast.LENGTH_SHORT).show();
                        widview.setBackgroundResource(R.color.Blue);
                        break;
                    }
                    case 4: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_pink)
                        );
                        Toast.makeText(getActivity(), "selected Pink", Toast.LENGTH_SHORT).show();
                        widview.setBackgroundResource(R.color.Pink);
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        return view;
    }
}



