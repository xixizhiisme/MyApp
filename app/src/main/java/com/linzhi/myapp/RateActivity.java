package com.linzhi.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends Activity implements Runnable{
    private final String TAG = "Rate";
    private float rateDollar=1/6.7f;
    private float rateEuro=1/11f;
    private float rateWon=500f;

    EditText rmb;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showOut);

        //获取sp里保存的数据
        SharedPreferences share=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        rateDollar=share.getFloat("rate_dollar",0.0f);
        rateEuro=share.getFloat("rate_euro",0.0f);
        rateWon=share.getFloat("rate_won",0.0f);

        Log.i(TAG,"onCreate:sp rateDollar="+rateDollar);
        Log.i(TAG,"onCreate:sp rateEuro="+rateEuro);
        Log.i(TAG,"onCreate:sp rateWon="+rateWon);

        //开启子线程
        Thread t=new Thread(this);
        t.start();

        Handler handler=new Handler();
    }

    public void onClick(View btn){
        Log.i(TAG,"onClick:");
        String str = rmb.getText().toString();
        Log.i(TAG,"onClick:get str="+str);
        float r = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG,"onClick:r="+r);
        if(btn.getId()==R.id.rate_dollar){
            show.setText(String.format("%.2f",r*rateDollar));
        }else if(btn.getId()==R.id.rate_euro){
            show.setText(String.format("%.2f",r*rateEuro));
        }else{
            show.setText(String.format("%.2f",r*rateWon));
        }
    }
    public void openOne(View btn){
        openConfig();
    }

    private void openConfig() {
        Intent config = new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",rateDollar);
        config.putExtra("euro_rate_key",rateEuro);
        config.putExtra("won_rate_key",rateWon);
        Log.i(TAG,"openOne:dollar_rate_key="+rateDollar);
        Log.i(TAG,"openOne:euro_rate_key="+rateEuro);
        Log.i(TAG,"openOne:won_rate_key="+rateWon);
        //startActivity(config);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==2){
            Bundle bundle=data.getExtras();
            rateDollar = bundle.getFloat("key_dollar",0.1f);
            rateEuro = bundle.getFloat("key_euro",0.1f);
            rateWon = bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult:rateDollar="+rateDollar);
            Log.i(TAG,"onActivityResult:rateEuro="+rateEuro);
            Log.i(TAG,"onActivityResult:rateWon="+rateWon);

            //将新设置的汇率写到sp里
            SharedPreferences share=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=share.edit();
            editor.putFloat("rate_dollar",rateDollar);
            editor.putFloat("rate_euro",rateEuro);
            editor.putFloat("rate_won",rateWon);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据已保存到share");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        Log.i(TAG, "run: run()……");
    }
}
