package com.linzhi.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends Activity {
    TextView score_A;
    TextView score_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score_A = (TextView) findViewById(R.id.score_A);
        score_B = (TextView) findViewById(R.id.score_B);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea=score_A.getText().toString();
        String scoreb=score_B.getText().toString();
        outState.putString("score_A",scorea);
        outState.putString("score_B",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea=savedInstanceState.getString("score_A");
        String scoreb=savedInstanceState.getString("score_B");
        score_A.setText(scorea);
        score_B.setText(scoreb);
    }

    public void btnAdd1(View btn){
        if(btn.getId()==R.id.btn_score_1_A){
            showScore_A(1);
        }else{
            showScore_B(1);
        }
    }

    public void btnAdd2(View btn){
        if(btn.getId()==R.id.btn_score_2_A){
            showScore_A(2);
        }else{
            showScore_B(2);
        }
    }

    public void btnAdd3(View btn){
        if(btn.getId()==R.id.btn_score_3_A){
            showScore_A(3);
        }else{
            showScore_B(3);
        }
    }

    public void btnReset(View btn){
        score_A.setText("0");
        score_B.setText("0");
    }
    private void showScore_A(int inc){
        Log.i("show","inc="+inc);
        String oldScore = (String) score_A.getText();
        int newScore = Integer.parseInt(oldScore) + inc;
        score_A.setText(""+newScore);
    }
    private void showScore_B(int inc){
        Log.i("show","inc="+inc);
        String oldScore = (String) score_B.getText();
        int newScore = Integer.parseInt(oldScore) + inc;
        score_B.setText(""+newScore);
    }
}
