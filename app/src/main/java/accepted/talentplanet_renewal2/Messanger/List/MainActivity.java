package accepted.talentplanet_renewal2.Messanger.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import accepted.talentplanet_renewal2.MyFirebaseMessagingService;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements MyFirebaseMessagingService.MessageReceivedListener {

    Context mContext;
    ArrayList<ListItem> messanger_Arraylist;
    Adapter messanger_ArrayAdapter;
    ListView messanger_Listview;

    boolean deleteBtn_Clicked;

    SQLiteDatabase sqliteDatabase;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messanger_list);

        mContext = getApplicationContext();

        messanger_Listview = (ListView) findViewById(R.id.Messanger_List_ListView);
        messanger_Listview.setAdapter(messanger_ArrayAdapter);
        deleteBtn_Clicked = false;

        refreshChatLog();
    }

    @Override
    public void onResume(){
        super.onResume();
        MyFirebaseMessagingService.isNewMessageArrive = false;
        refreshChatLog();
        MyFirebaseMessagingService.setOnMessageReceivedListener(this);
    }

    public void refreshChatLog(){
        messanger_Arraylist = new ArrayList<>();
        messanger_ArrayAdapter = new Adapter(messanger_Arraylist, MainActivity.this);

        String dbName = "/accepted.db";
        try {
            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + dbName, null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        String selectBasicChat = "SELECT D01.ROOM_ID, D01.USER_NAME, D03.UNREADED_COUNT, D06.CONTENT, D06.CREATION_DATE, D01.USER_ID, D01.START_MESSAGE_ID, D01.FILE_PATH\n" +
                "FROM   TB_CHAT_ROOM D01\n" +
                "\t   LEFT OUTER JOIN (SELECT D02.ROOM_ID, COUNT(D02.ROOM_ID) AS UNREADED_COUNT\n" +
                "        FROM   TB_CHAT_LOG D02\n" +
                "        WHERE  D02.READED_FLAG = 'N'\n" +
                "        GROUP BY D02.ROOM_ID) D03 ON D01.ROOM_ID = D03.ROOM_ID\n" +
                "        LEFT OUTER JOIN (SELECT D04.ROOM_ID, D04.CONTENT, D04.CREATION_DATE\n" +
                "         FROM   TB_CHAT_LOG D04\n" +
                "         WHERE  D04.MESSAGE_ID IN (SELECT MAX(D05.MESSAGE_ID)\n" +
                "\t\t\t\t\t\t\t\t   FROM   TB_CHAT_LOG D05\n" +
                "                                   GROUP BY D05.ROOM_ID)) D06 ON D01.ROOM_ID = D06.ROOM_ID" +
                " WHERE D01.ACTIVATE_FLAG = 'Y'\n" +
                " AND   D01.MASTER_ID = '" + SaveSharedPreference.getUserId(MainActivity.this) + "'";
        Cursor cursor = sqliteDatabase.rawQuery(selectBasicChat, null);
        cursor.moveToFirst();


        boolean isRunning = false;

        final Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
        final String nowDateStr = simpleDateFormat.format(date);
        String[] nowDateTemp = nowDateStr.split(",");
        final String nowDate = nowDateTemp[0];
        while(!cursor.isAfterLast()){
            int roomID = cursor.getInt(0);
            String userName = cursor.getString(1);
            int unreadedCount = cursor.getInt(2);
            String lastMessage = (cursor.getString(3)==null) ? "NODATA" : cursor.getString(3);
            String lastDate = (cursor.getString(4) == null)? "NODATA" : cursor.getString(4);
            String userID = cursor.getString(5);
            String filePath = cursor.getString(7);

            if(!lastDate.equals("NODATA")) {
                String[] dateTemp = lastDate.split(",");
                lastDate = dateTemp[0];

                String dateTime = dateTemp[1].substring(0, 8);
                lastDate = (lastDate.equals(nowDate))?dateTime : lastDate;
            }

            Log.d("filePath Messanger = ", filePath);

            messanger_Arraylist.add(0,new ListItem(R.drawable.picure_basic, userName, userID, lastMessage ,lastDate, unreadedCount, false, roomID, filePath));
            cursor.moveToNext();
        }
        cursor.close();
        sqliteDatabase.close();
        messanger_Listview.setAdapter(messanger_ArrayAdapter);
        messanger_ArrayAdapter.notifyDataSetChanged();
    }


    @Override
    public void onMessageRecieved(){
        Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onPause(){
        super.onPause();
        MyFirebaseMessagingService.setOnMessageReceivedListener(null);
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            refreshChatLog();
        }
    };




}
