package barometer.iot.com.heightinformaton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity {
    private  final int Length = 25;
    private  float range = 0.00f;
    private  SensorManager sensorManager = null;
    private  float[] press = new float[3];
    private  float[] accelerator = new float[3];
    private  int count = 100;
    private  Boolean flag =  false;
    private  float pre = 0;
    private  float curr = 0;
    CheckBox PRESSURE = null;
    CheckBox ACCELEROMETER = null;
    CheckBox GYROSCOPE = null;
    CheckBox ORIENTATION = null;
    CheckBox Megnetic_field = null;
    EditText server = null;
    EditText port = null;
    RadioButton wireless = null;
    RadioButton database = null;
    private int PORT = 3000;
    private String Server ="155.69.211.222";
    DatagramSocket dataSocket = null;
    private  SqliteOperation sqlite = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    EditText description = null;
    DecimalFormat df = new DecimalFormat("#0.000000");


    public  void UDPSend(String data) {

        try {

            // 指定端口号，避免与其他应用程序发生冲突
            byte[] sendDataByte = new byte[1024];
            sendDataByte = data.getBytes();
            DatagramPacket dataPacket = new DatagramPacket(sendDataByte, sendDataByte.length,
                    InetAddress.getByName(Server), PORT);
            dataSocket.send(dataPacket);
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }


    class  DealWithSensorEvent implements  Runnable
    {
        private SensorEvent sensorEvent = null;
        private float[] value = new float[3];
        public DealWithSensorEvent( SensorEvent sensorEvent)
        {
            this.sensorEvent= sensorEvent;
        }

        @Override
        public void run() {


            value = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE)
            {

                curr = value[0]/10;
                if( PRESSURE.isChecked())
                {

                    if (wireless.isChecked())
                    {
                        UDPSend("pre("+curr+",0,0)");
                    }
                    else  if (database.isChecked())
                    {
                        sqlite.intsertBarometer(description.getText().toString(),"",df.format(curr));
                    }

                }
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER  )
            {
                //Log.e("zhujianjie","CB_Accelerometer tttt");

                if( ACCELEROMETER.isChecked())
                {
                    if (wireless.isChecked())
                    {
                        UDPSend("acc("+df.format(value[0])+","+df.format(value[1])+","+df.format(value[2])+")");
                    }
                    else  if (database.isChecked())
                    {
                        sqlite.intsertAccelerator(description.getText().toString(),df.format(value[0]),df.format(value[1]),df.format(value[2]));
                    }

                }
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE  )
            {
                //Log.e("zhujianjie","CB_Gyroscope tttt");

                if( GYROSCOPE.isChecked())
                {
                    if (wireless.isChecked())
                    {
                        UDPSend("gyr("+df.format(value[0])+","+df.format(value[1])+","+df.format(value[2])+")");
                    }
                    else  if (database.isChecked())
                    {
                        sqlite.intsertGyroscope(description.getText().toString(),df.format(value[0]),df.format(value[1]),df.format(value[2]));
                    }

                }
            }else if ( sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION )
            {
                //Log.e("zhujianjie","CB_Orientation tttt");

                if( ORIENTATION.isChecked())
                {
                    if (wireless.isChecked())
                    {
                        UDPSend("ori("+df.format(value[0])+","+df.format(value[1])+","+df.format(value[2])+")");
                    }
                    else  if (database.isChecked())
                    {
                        sqlite.intserOrientation(description.getText().toString(),df.format(value[0]),df.format(value[1]),df.format(value[2]));
                    }

                }
            }else if ( sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD  )
            {
                //Log.e("zhujianjie","CB_Megnetic_field tttt");

                if( Megnetic_field.isChecked())
                {
                    if (wireless.isChecked())
                    {
                        UDPSend("mag("+df.format(value[0])+","+df.format(value[1])+","+df.format(value[2])+")");
                    }
                    else  if (database.isChecked())
                    {
                        sqlite.intsertMagnetic_field(description.getText().toString(),df.format(value[0]),df.format(value[1]),df.format(value[2]));
                    }

                }
            }


        }
    }
   /*
   传感器监听器
    */
    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {

            if (flag )
            {
                DealWithSensorEvent event = new DealWithSensorEvent(sensorEvent);
                Thread thread = new Thread(event);
                thread.start();
            }

        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
c初始化程序  绑定按钮点击事件
 */
    public void init()
    {
        sqlite = new SqliteOperation(this);
        PRESSURE =(CheckBox)findViewById(R.id.CB_Pressure);
        ACCELEROMETER =(CheckBox)findViewById(R.id.CB_Accelerometer);
        GYROSCOPE =(CheckBox)findViewById(R.id.CB_Gyroscope);
        ORIENTATION =(CheckBox)findViewById(R.id.CB_Orientation);
        Megnetic_field =(CheckBox)findViewById(R.id.CB_Megnetic_field);

        wireless = (RadioButton)findViewById(R.id.RB_Wireless);
        database = (RadioButton)findViewById(R.id.RB_Database);

        server = (EditText)findViewById(R.id.ET_Server);
        port = (EditText)findViewById(R.id.ET_PORT);
        Server = server.getText().toString();
        PORT = Integer.parseInt(port.getText().toString());

        description = (EditText)findViewById(R.id.ET_Description);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor  sensor_press =sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        Sensor  sensor_accelerator =sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor  sensor_ORIENTATION =sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Sensor  sensor_MAGNETIC_FIELD =sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor  sensor_GYROSCOPE =sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(sensor_press != null)
        {
            Log.e("sensor_press","support");
            sensorManager.registerListener(myListener,sensor_press , 50000);
        }

        if(sensor_accelerator!=null)
        {
            Log.e("sensor_accelerator","support");
            sensorManager.registerListener(myListener,sensor_accelerator ,50000);
        }

        if(sensor_GYROSCOPE!=null)
        {
            Log.e("sensor_accelerator","support");
            sensorManager.registerListener(myListener,sensor_GYROSCOPE ,50000);
        }


        if(sensor_MAGNETIC_FIELD!=null)
        {
            Log.e("sensor_accelerator","support");
            sensorManager.registerListener(myListener,sensor_MAGNETIC_FIELD ,50000);
        }

        if(sensor_ORIENTATION!=null)
        {
            Log.e("sensor_accelerator","support");
            sensorManager.registerListener(myListener,sensor_ORIENTATION ,50000);
        }


        try {
            dataSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
       /* Button button = (Button)findViewById(R.id.BT_SetRange);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textView = (EditText)findViewById(R.id.ET_Range);
                range = Float.parseFloat(textView.getText().toString());
            }
        });*/

        Button start = (Button)findViewById(R.id.BT_Start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (server.getText().toString().equals("")||port.getText().toString().equals(""))
                {
                    Toast toast = Toast.makeText(MainActivity.this,"服务器地址和端口不能为空",Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if (!flag)
                {
                    flag = true;
                    Server = server.getText().toString();
                    PORT = Integer.parseInt(port.getText().toString());
                    TextView state = (TextView)findViewById(R.id.TV_State);
                    state.setText("开始采集数据");
                }



            }
        });


        Button stop = (Button)findViewById(R.id.BT_Stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag)
                {

                    flag = false;
                    TextView state = (TextView)findViewById(R.id.TV_State);
                    state.setText("停止采集数据");
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UDPSend("stop");
                    }
                }).start();

            }
        });

        Button copy = (Button)findViewById(R.id.BT_Cpoy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = null;

                //   private boolean copy(String fileFrom, String fileTo) {
                try {
                    String fileFrom ="/data/data/barometer.iot.com.heightinformaton/databases/barometer.db";
                    String fileTo = "/storage/extSdCard/barometer.db";

                    File file = new File(fileTo);
                    if(file.exists())
                    {
                        file.delete();
                        toast =  Toast.makeText(MainActivity.this,"源文件删除成功",Toast.LENGTH_LONG);
                        toast.show();
                    }

                    FileInputStream in = new java.io.FileInputStream(fileFrom);
                    FileOutputStream out = new FileOutputStream(fileTo);
                    byte[] bt = new byte[1024];
                    int count;
                    while ((count = in.read(bt)) > 0) {
                        out.write(bt, 0, count);
                    }
                    in.close();
                    out.close();
                    toast = Toast.makeText(MainActivity.this,"复制成功",Toast.LENGTH_LONG);
                    toast.show();

                } catch (IOException ex) {

                    toast = Toast.makeText(MainActivity.this,"复制失败"+ex.getMessage(),Toast.LENGTH_LONG);
                    toast.show();
                }
                // }

            }
        });

        Button clear = (Button)findViewById(R.id.BT_Clear_Database);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                sqlite.clearDatabase();


                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                        Log.i("alertdialog", " 请保存数据！");
                    }
                }).show();//在按键响应事件中显示此对话框
            }


        });
    }

}
