package com.linzhi.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TempActivity extends Activity implements View.OnClickListener{
    EditText tempE;
    Button btn;
    TextView tempV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        tempE = (EditText)findViewById(R.id.tempEdit);
        tempV = (TextView)findViewById(R.id.tempView);

        btn = (Button)findViewById(R.id.tempButton);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String str = tempE.getText().toString();
        double d = Double.parseDouble(str);
        double fah1 = 32+d*1.8;
        String fah2 = String.format("%.2f",fah1);
        tempV.setText("结果为："+fah2+"℉");
        Log.i("main","finish");
    }
}
