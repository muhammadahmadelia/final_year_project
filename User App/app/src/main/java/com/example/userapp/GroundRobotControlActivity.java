package com.example.userapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GroundRobotControlActivity extends AppCompatActivity {

    //public static String WifiModuleIp = "192.168.43.166";
    public static String WifiModuleIp = "192.168.43.95";
    public static int WifiModulePort = 21567;
    public static String CMD ;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CMD = "close";
        Log.i("Button Click", "close");
        sendCommand();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_robot_control);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Controls of Robot");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B58A3F")));
        }

        //WifiModuleIp = ServerIPAddress.getInstance().getServerIPAddress();

        final Switch LED = findViewById(R.id.ledSwitch);
        LED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LED.isChecked()){
                    CMD = "LED ON";
                    Log.i("Switch Click", "LED ON");
                    sendCommand();
                    Toast.makeText(getApplicationContext(), "LED is on", Toast.LENGTH_SHORT).show();
                } else {
                    CMD = "LED OFF";
                    Log.i("Switch Click", "LED OFF");
                    sendCommand();
                    Toast.makeText(getApplicationContext(), "LED is off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void upButton_Click(View view){
        CMD = "Up";
        Log.i("Button Click", "Up");
        sendCommand();
    }

    public void downButton_Click(View view){
        CMD = "Down";
        Log.i("Button Click", "Down");
        sendCommand();
    }

    public void leftButton_Click(View view){
        CMD = "Left";
        Log.i("Button Click", "Left");
        sendCommand();
    }

    public void rightButton_Click(View view){
        CMD = "Right";
        Log.i("Button Click", "Right");
        sendCommand();
    }


    public void stopButton_Click(View view){
        CMD = "Stop";
        Log.i("Button Click", "Stop");
        sendCommand();
    }

    private void sendCommand() {
        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
        cmd_increase_servo.execute();
    }


    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void>{
        Socket socket;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                InetAddress inetAddress = InetAddress.getByName(GroundRobotControlActivity.WifiModuleIp);
                socket = new java.net.Socket(inetAddress, GroundRobotControlActivity.WifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                //Log.i("Control", CMD);
                dataOutputStream.close();
                socket.close();
            } catch (UnknownHostException e){
                e.printStackTrace();
            } catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }
    }


}
