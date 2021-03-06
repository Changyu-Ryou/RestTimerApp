package com.example.resttimer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static com.example.resttimer.R.layout.support_simple_spinner_dropdown_item;

public class Frag3_Setting extends Fragment {

    Activity activity;

    private View view;
    private AdView mAdView;
    TextView widSize;
    TextView PopView;


    private View widview;
    Spinner spinner;
    RelativeLayout mpopView;
    Switch screenoffSwitch;

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

        view = inflater.inflate(R.layout.frag3,container,false);
        widSize = (TextView) view.findViewById(R.id.WidSize);
        screenoffSwitch= view.findViewById(R.id.switch1);
        // admob
        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");

        mAdView = view.findViewById(R.id.adView); // view.로 접근하여 findViewById를 해야함.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        PopView = (TextView) MyService.mView.findViewById(R.id.textView); //오버레이

        final SeekBar sb  = (SeekBar) view.findViewById(R.id.seekBar);

        //sb.setProgress(5);
        widSize.setText("위젯 크기 : "+sb.getProgress());
        widview = (View) MyService.mView.findViewById(R.id.mPopView);

        if (MyService.selColor == 0 && MyService.progress == 0) {          //셋팅 없이 초기값인 경우

            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //System.out.println("stop");
                    //MyService.progress = progresses;
                    //mpopView.setLayoutParams(new WindowManager.LayoutParams(PopView.getWidth() + 20, PopView.getHeight()+5));
                    //widview.setLayoutParams(new RelativeLayout.LayoutParams(PopView.getWidth() + 20, PopView.getHeight() + 5));
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    int progress=sb.getProgress();
                    System.out.println("start");
                    widSize.setText("Widget Size : " + progress);

                    ViewGroup.LayoutParams wid = widview.getLayoutParams();
                    mpopView = (RelativeLayout) MyService.mView.findViewById(R.id.mPopView);

                    PopView = (TextView) MyService.mView.findViewById(R.id.textView); //오버레이
                    PopView.setTextSize(24 + progress);

                    mpopView.setLayoutParams(new WindowManager.LayoutParams(PopView.getWidth() + 20, PopView.getHeight()+5));
                    widview.setLayoutParams(new RelativeLayout.LayoutParams(PopView.getWidth() + 20, PopView.getHeight() + 5));
                }

                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    System.out.println("onchange");
                    widSize.setText("위젯 크기 : " + progress);
                    progresses = progress;

                    ViewGroup.LayoutParams wid = widview.getLayoutParams();
                    mpopView = (RelativeLayout) MyService.mView.findViewById(R.id.mPopView);

                    PopView = (TextView) MyService.mView.findViewById(R.id.textView); //오버레이
                    PopView.setTextSize(24 + progress);

                    mpopView.setLayoutParams(new WindowManager.LayoutParams(PopView.getWidth() + 20, PopView.getHeight()+5));
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
                    widview = (View) MyService.mView.findViewById(R.id.mPopView);
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
                        widview.setBackgroundResource(R.drawable.view_service_white);
                        break;
                    }
                    case 1: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_black)
                        );

                        widview.setBackgroundResource(R.drawable.view_service_black);
                        Toast.makeText(getActivity(), "selected Black", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 2: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_red)
                        );
                        Toast.makeText(getActivity(), "selected Red", Toast.LENGTH_SHORT).show();
                        widview.setBackgroundResource(R.drawable.view_service_red);
                        break;
                    }
                    case 3: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_blue)
                        );
                        Toast.makeText(getActivity(), "selected Blue", Toast.LENGTH_SHORT).show();
                        widview.setBackgroundResource(R.drawable.view_service_blue);
                        break;
                    }
                    case 4: {
                        getView().findViewById(R.id.Color_image).setBackground(
                                getResources().getDrawable(R.drawable.myshape_pink)
                        );
                        Toast.makeText(getActivity(), "selected Pink", Toast.LENGTH_SHORT).show();
                        widview.setBackgroundResource(R.drawable.view_service_pink);
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        screenoffSwitch.setOnCheckedChangeListener(new screenoffSwitchListener());






        return view;
    }

    class screenoffSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                MyService.screenFlag=0;
            else
                MyService.screenFlag=1;
        }
    }


}



