package com.linzhi.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity extends Activity implements Runnable{
    private final String TAG = "Rate";
    private float rateDollar=1/6.7f;
    private float rateEuro=1/11f;
    private float rateWon=500f;
    private String updateDate="";

    EditText rmb;
    TextView show;
    Handler handler;

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
        updateDate=share.getString("update_date","");
        //获取当前系统时间
        Date today= Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today);

        Log.i(TAG,"onCreate:sp rateDollar="+rateDollar);
        Log.i(TAG,"onCreate:sp rateEuro="+rateEuro);
        Log.i(TAG,"onCreate:sp rateWon="+rateWon);
        Log.i(TAG, "onCreate: sp updateDate="+updateDate);
        //判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG, "onCreate: 需要更新");
            //开启子线程
            Thread t=new Thread(this);
            t.start();
        }else {
            Log.i(TAG, "onCreate: 不需要更新");
        }
        
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==5){
                    Bundle bdl=(Bundle) msg.obj;
                    rateDollar=bdl.getFloat("rate_dollar");
                    rateEuro=bdl.getFloat("rate_euro");
                    rateWon=bdl.getFloat("rate_won");
                    Log.i(TAG, "handleMessage: dollar:"+rateDollar);
                    Log.i(TAG, "handleMessage: euro:"+rateEuro);
                    Log.i(TAG, "handleMessage: won:"+rateWon);
                    //保存更新的日期
                    SharedPreferences share=getSharedPreferences("myrate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor=share.edit();
                    editor.putFloat("rate_dollar",rateDollar);
                    editor.putFloat("rate_euro",rateEuro);
                    editor.putFloat("rate_won",rateWon);
                    editor.putString("update_date",todayStr);
                    editor.apply();
                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                    /*Log.i(TAG, "handleMessage: getMessage msg="+str);
                    show.setText(str);*/
                }
                super.handleMessage(msg);
            }
        };
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
        }else if(item.getItemId()==R.id.menu_openList1){
            Intent list=new Intent(this,RateListActivity.class);
            startActivity(list);
        }else {
            Intent list=new Intent(this,MyList2Activity.class);
            startActivity(list);
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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //用于保存获取的汇率
        Bundle bundle;

        //获取网络数据
        /*URL url= null;
        try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http= (HttpURLConnection) url.openConnection();
            InputStream in=http.getInputStream();
            String html=inputStream2String(in);
            Log.i(TAG, "run: html="+html);
            Document doc=Jsoup.parse(html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        bundle=getFromBOC();
        //获取msg对象，用于返回主线程
        Message msg=handler.obtainMessage(5);
        //msg.what=5;
        //msg.obj="Hello from run()";
        msg.obj=bundle;
        handler.sendMessage(msg);

        //bundle中保存获取的汇率
    }

    private Bundle getFromBOC() {
        Bundle bundle=new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables=doc.getElementsByTag("table");
            /*for(Element table:tables){
                Log.i(TAG, "run: table["+i+"]="+table);
                i++;
            }*/
            Element table6=tables.get(0);
            //Log.i(TAG, "run: table6="+table6);
            //获取TD中的数据
            Elements tds=table6.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i(TAG, "run: "+td1.text()+"==>"+td2.text());
                String str1=td1.text();
                String val=td2.text();
                if("美元".equals(str1)){
                    bundle.putFloat("rate_dollar",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("rate_euro",100f/Float.parseFloat(val));
                }else if("韩元".equals(str1)){
                    bundle.putFloat("rate_won",100f/Float.parseFloat(val));
                }
            }
            /*for(Element td:tds){
                Log.i(TAG, "run: td="+td);
                Log.i(TAG, "run: text="+td.text());
                Log.i(TAG, "run: html="+td.html());
            }*/
            /*Elements newsHeadlines = doc.select("#mp-itn b a");
            for (Element headline : newsHeadlines) {
                Log.i(TAG, "%s\n\t%s" + headline.attr("title") + headline.absUrl("href"));
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer=new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        for(;;){
            int rsz=((InputStreamReader) in).read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}
