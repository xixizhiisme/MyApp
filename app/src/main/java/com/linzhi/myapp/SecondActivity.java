package com.linzhi.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends Activity implements View.OnClickListener {
    TextView out;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        out = (TextView)findViewById(R.id.inp);
        edit = (EditText)findViewById(R.id.inp);
        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i("main","onClick msg...");
        String str = edit.getText().toString();
        out.setText("Hello2"+str);

    }
}
