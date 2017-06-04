package com.example.android.group4;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.*;

/*
Author : Aamir Shaikh
Used GraphView.java from Blackboard codes to implement the grneration of graph elements for the heart monitoring app
 */

public class MainActivity extends AppCompatActivity {
    boolean myFlag=false;
    public GraphView graphView;
    public float newvalues[];
    public Timer updateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        float[] values = new float[] {};
        String[] vertical_axis = new String[] { "3000","2000","1000","0" };
        final LinearLayout linear1 = new LinearLayout(this);
        final LinearLayout linear2 = new LinearLayout(this);
        final LinearLayout linear3 = new LinearLayout(this);
        final LinearLayout linear4 = new LinearLayout(this);

        String[] horizontal_axis = new String[] { "0","1000", "2000", "3000", "4000"};
        linear1.setOrientation(LinearLayout.VERTICAL);
        newvalues = new float[30];
        ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        linear2.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams lview2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear1.addView(linear2);
        linear3.setOrientation(LinearLayout.HORIZONTAL);
        linear4.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams lview3 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear1.addView(linear3);
        linear4.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams lview4 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear1.addView(linear4);
        setContentView(linear1, linLayoutParam);

//Layout 2
        TextView patientID = new TextView(this);
        patientID.setText("Patient ID: ");
        patientID.setTextSize(17);
        patientID.setTextColor(Color.BLACK);
        EditText IDText = new EditText(this);
        IDText.setEms(2);
        IDText.setTextSize(15);
        TextView patientName = new TextView(this);
        patientName.setText("Name: ");
        patientName.setTextColor(Color.BLACK);
        patientName.setTextSize(17);
        EditText NameText = new EditText(this);
        NameText.setEms(5);
        NameText.setTextSize(15);
        patientID.setLayoutParams(lview2);
        linear2.addView(patientID);
        IDText.setLayoutParams(lview2);
        linear2.addView(IDText);
        patientName.setLayoutParams(lview2);
        linear2.addView(patientName);
        NameText.setLayoutParams(lview2);
        linear2.addView(NameText);

//Layout 3
        TextView ageT = new TextView(this);
        ageT.setText("Age: ");
        ageT.setTextSize(17);
        EditText ageText= new EditText(this);
        ageText.setTextSize(15);
        ageText.setEms(3);
        TextView sexT = new TextView(this);
        sexT.setText("Sex:");
        sexT.setTextSize(17);
        sexT.setTextColor(Color.BLACK);
        final RadioButton male=new RadioButton(this);
        male.setText("Male");
        male.setTextSize(16);
        final RadioButton female=new RadioButton(this);
        female.setText("Female");
        female.setTextSize(16);
        ageT.setLayoutParams(lview3);
        linear3.addView(ageT);
        ageText.setLayoutParams(lview3);
        linear3.addView(ageText);
        sexT.setLayoutParams(lview3);
        linear3.addView(sexT);
        male.setLayoutParams(lview3);
        linear3.addView(male);
        female.setLayoutParams(lview3);
        linear3.addView(female);

        male.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {if(female.isChecked()==true)
                female.setChecked(false);}
        });

        female.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {if(male.isChecked()==true)
                male.setChecked(false);}
        });

//Start button
        Button start = new Button(this);
        Button stop = new Button(this);

        start.setText("RUN");
        start.setWidth(100);
        start.setHeight(10);
        stop.setText("STOP");
        stop.setWidth(100);
        stop.setHeight(10);
        linear4.addView(start, lview4);

//Set GraphView parameters - used code provided in Blackboard.
        graphView = new GraphView(this, values, "",horizontal_axis, vertical_axis, GraphView.LINE);
        //graphView.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        graphView.setScaleX(1.0f);
        graphView.setScaleY(1.0f);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                for (int i=0;i<30;i++) {
                    newvalues[i] = (float)Math.random()%10;
                }
                if(myFlag == false) {
                    myFlag=true;
                    makeInterval();
                    graphView.invalidate();
                    graphView.setValues(newvalues);
                    startTimer();
                }
            }
        });

        linear4.addView(stop,lview4);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (myFlag == true)
                {
                    myFlag=false;
                    stopTimer();
                    float[] empty = new float[] {};
                    graphView.invalidate();
                    graphView.setValues(empty);
                }}
        });
        linear1.addView(graphView);
    }

    public  void startTimer(){

        updateTime = new Timer();
        updateTime.scheduleAtFixedRate(new TimerTask() {

                                                 @Override
                                                 public void run() {
                                                     makeInterval();
                                                     runOnUiThread(new Runnable() {
                                                         public void run() {
                                                             graphView.invalidate();
                                                             graphView.setValues(newvalues);
                                                         }
                                                     });
                                                 }

                                             },500,500);

    }

    public void makeInterval() {
        for(int i=0;i<29;i++) {
            newvalues[i] = newvalues[i + 1];
        }
        newvalues[29]=(float)(Math.random() % 10);
    }

    public  void stopTimer(){
        updateTime.cancel();
    }

}