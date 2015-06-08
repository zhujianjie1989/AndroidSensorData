package barometer.iot.com.heightinformaton;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhujianjie on 21/1/2015.
 */
public class SqliteOperation {
    private static Context activity = null;
    private static SQLiteDatabase sqlite = null;


    /*
    获取数据库连接
     */
   static synchronized SQLiteDatabase getSqliteConn()
   {
       if (sqlite == null)
       {
           sqlite = activity.openOrCreateDatabase("barometer.db", Context.MODE_PRIVATE, null);
       }
       return  sqlite;
   }

    /*
    关闭数据库
     */
    static  void Close()
    {
        getSqliteConn().close();
    }

    /*
    清空数据库
     */
    public void clearDatabase()
    {

        SQLiteDatabase dbconn = getSqliteConn();
        dbconn.beginTransaction();
        dbconn.execSQL("delete from barometer_data;");
        Log.e("zhujianjie","delete barometer_data");
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();


        dbconn.beginTransaction();
        dbconn.execSQL("delete from accelerator_data;");
        Log.e("zhujianjie","delete accelerator_data");
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();

        dbconn.beginTransaction();
        dbconn.execSQL("delete from Magnetic_field;");
        Log.e("zhujianjie","delete Magnetic_field");
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();


        dbconn.beginTransaction();
        dbconn.execSQL("delete from Orientation;");
        Log.e("zhujianjie","delete Orientation");
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();


        dbconn.beginTransaction();
        dbconn.execSQL("delete from Gyroscope;");
        Log.e("zhujianjie","delete Gyroscope");
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();

    }

    public SqliteOperation( Context context)
    {
        if (activity !=null)
        {
            return;
        }
        activity = context;
        getSqliteConn();
        create_barometer_Table();
        create_accelerator_Table();
        create_Gyroscope_Table();
        create_Magnetic_field_Table();
        create_Orientation_Table();
        Log.e("zhujianjie","create table");

    }
    public void executeSQL(String sql)
    {
        try {
            SQLiteDatabase dbconn = getSqliteConn();
            dbconn.beginTransaction();
            dbconn.execSQL(sql);
            Log.e("zhujianjie",sql);
            dbconn.setTransactionSuccessful();
            dbconn.endTransaction();
        } catch (Exception e) {
            Log.e("zhujianjie","sqlite error");
        }
    }

    public void create_barometer_Table()
    {
        executeSQL("create table barometer_data(_id INTEGER PRIMARY KEY AUTOINCREMENT,datetime TEXT NOT NULL ,barometer TEXT ,description TEXT,footPoint_Count TEXT);" );
    }

    public void create_Magnetic_field_Table()
    {
        executeSQL("create table Magnetic_field(_id INTEGER PRIMARY KEY AUTOINCREMENT,datetime TEXT NOT NULL ,x TEXT ,y TEXT ,z TEXT ,description TEXT,footPoint_Count TEXT);" );
    }

    public void create_Orientation_Table()
    {
        executeSQL("create table Orientation(_id INTEGER PRIMARY KEY AUTOINCREMENT,datetime TEXT NOT NULL ,Azimuth TEXT ,Pitch TEXT ,Roll TEXT ,description TEXT,footPoint_Count TEXT);" );
    }

    public void create_Gyroscope_Table()
    {
        executeSQL("create table Gyroscope(_id INTEGER PRIMARY KEY AUTOINCREMENT,datetime TEXT NOT NULL ,x TEXT ,y TEXT ,z TEXT ,description TEXT,footPoint_Count TEXT);" );
    }

    public void create_accelerator_Table()
    {
        executeSQL("create table accelerator_data(_id INTEGER PRIMARY KEY AUTOINCREMENT,datetime TEXT NOT NULL ,accelerator_x TEXT ,accelerator_y TEXT ,accelerator_z TEXT ,description TEXT,footPoint_Count TEXT);" );
    }

    public synchronized  void intsertBarometer(String description,String footPoint_Count,String  barometer)
    {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String datetime = dateFormat.format( now );
        ContentValues values = new ContentValues();

        values.put("description", description);
        values.put("footPoint_Count",footPoint_Count);
        values.put("datetime",datetime);
        values.put("barometer", barometer);

        SQLiteDatabase dbconn = getSqliteConn();
        dbconn.beginTransaction();
        dbconn.insert("barometer_data","_id",values);
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();

   //     Log.e("zhujianjie","barometer");

    }


    public  synchronized  void intsertAccelerator(String description,String  accelarator_x,String  accelarator_y,String  accelarator_z)
    {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String datetime = dateFormat.format( now );
        ContentValues values = new ContentValues();

        values.put("description", description);
        values.put("datetime",datetime);
        values.put("accelerator_x",accelarator_x);
        values.put("accelerator_y",accelarator_y);
        values.put("accelerator_z",accelarator_z);


        SQLiteDatabase dbconn = getSqliteConn();
        dbconn.beginTransaction();
        dbconn.insert("accelerator_data","_id",values);
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();

     //   Log.e("zhujianjie","accelerator");

    }


    public synchronized   void intsertGyroscope(String description,String  x,String  y,String  z)
    {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String datetime = dateFormat.format( now );
        ContentValues values = new ContentValues();

        values.put("description", description);
        values.put("datetime",datetime);
        values.put("x",x);
        values.put("y",y);
        values.put("z",z);
        SQLiteDatabase dbconn = getSqliteConn();
        dbconn.beginTransaction();
        dbconn.insert("Gyroscope","_id",values);
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();

    }


    public synchronized   void intsertMagnetic_field(String description,String  x,String  y,String  z)
    {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String datetime = dateFormat.format( now );
        ContentValues values = new ContentValues();

        values.put("description", description);
        values.put("datetime",datetime);
        values.put("x",x);
        values.put("y",y);
        values.put("z",z);
        SQLiteDatabase dbconn = getSqliteConn();
        dbconn.beginTransaction();
        dbconn.insert("Magnetic_field","_id",values);
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();

    }
    public  synchronized  void intserOrientation(String description,String  Azimuth,String Pitch,String Roll)
    {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String datetime = dateFormat.format( now );
        ContentValues values = new ContentValues();

        values.put("description", description);
        values.put("datetime",datetime);
        values.put("Azimuth",Azimuth);
        values.put("Pitch",Pitch);
        values.put("Roll",Roll);
        SQLiteDatabase dbconn = getSqliteConn();
        dbconn.beginTransaction();
        dbconn.insert("Orientation","_id",values);
        dbconn.setTransactionSuccessful();
        dbconn.endTransaction();

    }
}
