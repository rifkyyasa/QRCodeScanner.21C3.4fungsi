package id.ac.qrcodescanner_21c3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //view Object
    private Button buttonScanning;
    private TextView textViewName,textViewClass,textViewId;
    //qr scanning object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //viewObject
        buttonScanning = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewNama);
        textViewClass =(TextView) findViewById(R.id.textViewKelas);
        textViewId = (TextView) findViewById(R.id.textViewNim);
        //inisialisasi scan object
        qrScan = new IntentIntegrator(this);

        //implementasi onClik listener
        buttonScanning.setOnClickListener(this);

    }


    @Override
    protected void onActivityResult (int requestCode,int resultCode,Intent data ){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // jika qr
            if (result.getContents() == null) {
                Toast.makeText(this, "hasil scanning tidak ada ", Toast.LENGTH_LONG).show();
            } else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);

            } else if (Patterns.EMAIL_ADDRESS.matcher(result.getContents()).matches()) {
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(result.getContents()));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"raihanadzuhri1@gmail.com"});
                intent.putExtra(Intent.EXTRA_CC, new String[]{"raihancahbekasi1@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Fungsi Email");
                intent.putExtra(Intent.EXTRA_TEXT, "Raihan Adzuhri-TI.21.C.3-312110374");
                try {
                    startActivity(Intent.createChooser(intent, "Ingin Mengirim Email ?"));
                } catch (android.content.ActivityNotFoundException ex) {
                }
            }else if (Patterns.WEB_URL.matcher(result.getContents()).matches()){
                String geoUri=result.getContents();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                //Set Package
                intent.setPackage("com.google.android.apps.maps");

                //Set Flag
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
            else if (Patterns.PHONE.matcher(result.getContents()).matches()) {
                String telp = String.valueOf(result.getContents());
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telp));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                try {
                    startActivity(Intent.createChooser(intent, "waiting.."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "no phone apk installed.", Toast.LENGTH_LONG);
                }
            }
            else {
                try {
                    //konversi datanya ke json
                    JSONObject obj = new JSONObject(result.getContents());
                    //diset nilai
                    textViewName.setText(obj.getString("nama"));
                    textViewId.setText(obj.getString("kelas"));
                    textViewClass.setText(obj.getString("nim"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(),
                            Toast.LENGTH_LONG).show();
                }
            } }

        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View v) {
        //inisialisasi qrcode scanning
        qrScan.initiateScan();


}}








