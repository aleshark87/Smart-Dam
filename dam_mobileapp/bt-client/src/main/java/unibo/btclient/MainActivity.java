package unibo.btclient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import unibo.btlib.BluetoothChannel;
import unibo.btlib.BluetoothUtils;
import unibo.btlib.ConnectToBluetoothServerTask;
import unibo.btlib.ConnectionTask;
import unibo.btlib.RealBluetoothChannel;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;
import unibo.btclient.utils.C;

public class MainActivity extends AppCompatActivity {

    private BluetoothChannel btChannel;
    private int state = 0;
    private boolean blockSeekBar = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter != null && !btAdapter.isEnabled()){
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), C.bluetooth.ENABLE_BT_REQUEST);
        }

        initUI();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initUI() {
        findViewById(R.id.connectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    connectToBTServer();
                } catch (BluetoothDeviceNotFound bluetoothDeviceNotFound) {
                    bluetoothDeviceNotFound.printStackTrace();
                }
            }
        });

        final SeekBar bar = findViewById(R.id.seekBar);
        bar.setMax(100); bar.setMin(0);
        bar.setProgress(0);

        bar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(blockSeekBar == false) {
                    ((TextView) findViewById(R.id.debugText)).setText("sending " + bar.getProgress());
                    btChannel.sendMessage(Integer.toString(bar.getProgress()));
                }
                return blockSeekBar;
            }
        });

        findViewById(R.id.manualButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == 2) {
                    setUpSeekBarAndToast();
                    //send message via bt
                    btChannel.sendMessage("MANUAL");
                }
                else{
                    errorToast();
                }
            }
        });
    }

    private void errorToast(){
        Toast toast = Toast.makeText(getApplicationContext(),
                "Can't switch, the state isn't ALARM !",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void setUpSeekBarAndToast(){
        Toast toast;
        if(blockSeekBar){
            toast = Toast.makeText(getApplicationContext(),
                    "Switching to manual mode",
                    Toast.LENGTH_SHORT);
            blockSeekBar = false;
        }
        else{
            toast = Toast.makeText(getApplicationContext(),
                    "Switching to auto mode",
                    Toast.LENGTH_SHORT);
            blockSeekBar = true;
        }
        toast.show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        btChannel.close();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        if(requestCode == C.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_OK){
            Log.d(C.APP_LOG_TAG, "Bluetooth enabled!");
        }

        if(requestCode == C.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_CANCELED){
            Log.d(C.APP_LOG_TAG, "Bluetooth not enabled!");
        }
    }

    private void connectToBTServer() throws BluetoothDeviceNotFound {
        final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByName(C.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);

        final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();
        //final UUID uuid = BluetoothUtils.generateUuidFromString(C.bluetooth.BT_SERVER_UUID);

        new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {

                ((TextView) findViewById(R.id.statusLabel)).setText(String.format("Status : connected to server on device %s",
                        serverDevice.getName()));

                findViewById(R.id.connectBtn).setEnabled(false);

                btChannel = channel;
                btChannel.registerListener(new RealBluetoothChannel.Listener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        try {
                            JSONObject res = new JSONObject(receivedMessage);
                            state = res.getInt("state");
                            int damOpening = res.getInt("damOpening");
                            double distance = res.getDouble("distance");
                            ((TextView) findViewById(R.id.stateTextView)).setText("State: " + getStateName(state));
                            if(distance == 0.0) {
                                ((TextView) findViewById(R.id.levelTextView)).setText("Last Level Detected: " + "> 20.0");
                            }
                            else{
                                ((TextView) findViewById(R.id.levelTextView)).setText("Last Level Detected: " + String.format("%,.2f", distance));
                            }
                            //nodamupdate
                            if(damOpening != -1) {
                                SeekBar bar = findViewById(R.id.seekBar);
                                bar.setProgress(damOpening, true);
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {

                    }
                });
            }

            @Override
            public void onConnectionCanceled() {
                ((TextView) findViewById(R.id.statusLabel)).setText(String.format("Status : unable to connect, device %s not found!",
                        C.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME));
            }
        }).execute();
    }
    private String getStateName(int intState){
        if(intState == 0) {
            return "NORMAL";
        }
        else{
            if(intState == 1){
                return "PREALARM";
            }
            else{
                return "ALARM";
            }
        }
    }
}
