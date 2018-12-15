package com.example.roman.myapplication;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import org.w3c.dom.Text;

public class MyGraphics extends AppCompatActivity implements RadialTimePickerDialogFragment.OnTimeSetListener{
    TextView tvMonFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_graphics);
    }

    public void onClick(View v){
        int id = v.getId();
        tvMonFirst = (TextView)findViewById(id);
        String [] currentTime = tvMonFirst.getText().toString().split(":");
        int hours = Integer.parseInt(currentTime[0]);
        int minutes = Integer.parseInt(currentTime[1]);
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(MyGraphics.this)
                .setStartTime(hours, minutes)
                .setDoneText("Продолжить")
                .setCancelText("Отмена")
                .setThemeLight();
        rtpd.show(getSupportFragmentManager(), "tag");
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        tvMonFirst.setText(hourOfDay + ":" + (minute / 10) + "" + (minute % 10));
    }

    public void freeTimeSave(View v){
       for(int id = R.id.textView1, count = 0; count < 5; count++ ){

       }
    }
}
