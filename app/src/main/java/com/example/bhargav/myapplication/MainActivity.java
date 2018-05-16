package com.example.bhargav.myapplication;

import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jingxun.meshlibtelink.TelinkLightService;
import com.telink.TelinkApplication;
import com.telink.bluetooth.event.ServiceEvent;
import com.telink.bluetooth.light.LeScanParameters;
import com.telink.bluetooth.light.Manufacture;
import com.telink.util.Event;
import com.telink.util.EventListener;

public class MainActivity extends AppCompatActivity implements EventListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LeScanParameters params = LeScanParameters.create();
                /*params.setMeshName("92296174ujmv");//Manufacture.getDefault().getFactoryName());
               // params.setOutOfMeshName(Manufacture.getDefault().getFactoryName());
                params.setTimeoutSeconds(500);
                params.setScanMode(true);*/

                params.setMeshName(Manufacture.getDefault().getFactoryName());
                params.setOutOfMeshName("out_of_mesh");
                params.setScanMode(false);
                params.setTimeoutSeconds(100);

                TelinkLightService.Instance().startScan(params);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        TelinkApplication.getInstance().doDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TelinkApplication.getInstance().doInit(getApplicationContext(), TelinkLightService.class);
    }

    @Override
    public void performed(Event event) {
        if (event instanceof ServiceEvent) {
            ServiceEvent serviceEvent = (ServiceEvent) event;
            IBinder iBinder = serviceEvent.getArgs();

        }

    }
}
