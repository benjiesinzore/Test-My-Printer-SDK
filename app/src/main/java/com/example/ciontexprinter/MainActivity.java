package com.example.ciontexprinter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;

import vpos.apipackage.PosApiHelper;
import vpos.apipackage.PrintInitException;

public class MainActivity extends AppCompatActivity {

    public static String[] MY_PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"};

    PosApiHelper posApiHelper = PosApiHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, "ciontek.permission.sdcard") != 0) {
            ActivityCompat.requestPermissions(this, MY_PERMISSIONS_STORAGE, 1);
        }

        Button print = findViewById(R.id.print);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testApiSimple();
            }
        });
    }

    void testApiSimple(){

        Print_Thread printThread = null;

        printThread = new Print_Thread();
        printThread.start();


    }

    public class Print_Thread extends Thread {

        public Print_Thread() {}

        public void run() {
            try {
                int ret = posApiHelper.PrintInit(2, 24, 24, 0x33);
                posApiHelper.PrintSetVoltage(75);
                posApiHelper.PrintSetGray (2);
                posApiHelper.PrintSetFont((byte)16, (byte)16, (byte)0x33);

                Log.d("Init", "" + ret);
                if (ret != 0) {
                    return;
                }
                posApiHelper.PrintStr("Print Tile\n");
                posApiHelper.PrintStr("- - - - - - - - - - - - - - - - - - - - - - - -\n");
                posApiHelper.PrintStr(" Print Str2 \n");
                posApiHelper.PrintBarcode("123456789", 360, 120, BarcodeFormat.CODE_128);
                posApiHelper.PrintBarcode("123456789", 240, 240, BarcodeFormat.QR_CODE);
                posApiHelper.PrintStr("CODE_128 : " + "123456789" + "\n\n");
                posApiHelper.PrintStr("QR_CODE : " + "123456789" + "\n\n");
                posApiHelper.PrintStr(" \n");
                Log.d("Init 2", "" + ret);
                int start = posApiHelper.PrintStart();
                Log.d("Printer Start", "" + start);
            }
            catch (Exception e){
                Log.d("Exception", e.toString());
            }
        }
    }
}