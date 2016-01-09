package tw.jojordan.drone.qrocde;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static  String positives ;
    private static  String total;
    private TextView myinfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView myResultTextView = (TextView) findViewById(R.id.result_textview) ;
        myinfoTextView = (TextView) findViewById(R.id.info_textView) ;
        Button  myScanButton = (Button) findViewById(R.id.scan_button) ;
        Button  mySendButton = (Button) findViewById(R.id.send_button) ;
        Button  myClearButton = (Button) findViewById(R.id.clear_button) ;

        myScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                if(getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size()==0)
                {
                    // ���w��
                    Toast.makeText(getApplicationContext(), "Pleas download ZXing in Google Play", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // SCAN_MODE, �i�P�O�Ҧ��䴩�����X
                    // QR_CODE_MODE, �u�P�O QRCode
                    // PRODUCT_MODE, UPC and EAN �X
                    // ONE_D_MODE, 1 �����X
                    intent.putExtra("SCAN_MODE", "SCAN_MODE");

                    // �I�sZXing Scanner�A�����ʧ@��^�� 1 �� onActivityResult �� requestCode �Ѽ�
                    startActivityForResult(intent, 1);
                }
            }
        });

        mySendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = myResultTextView.getText().toString();
                PostTask task = new PostTask();
                task.execute(link) ;
            }
        });

        myClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myResultTextView.setText("");
                myinfoTextView.setText("");
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                String contents = intent.getStringExtra("SCAN_RESULT");
                TextView resulttextView = (TextView) findViewById(R.id.result_textview);
                resulttextView.setText(contents.toString());
            }
            else
            if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this, "Scan Cancel", Toast.LENGTH_LONG).show();
            }
        }
    }

    class PostTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            try {
                String response = HTTPUtility.getInstance().sendPost(params[0].toString());
                JSONObject obj = new JSONObject(response) ;
                positives = obj.getString("positives") ;
                total = obj.getString("total") ;
                System.out.println(positives+"/"+total);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            myinfoTextView.setText("查詢結果：\n"+positives+"/"+total);

        }

    }

}
