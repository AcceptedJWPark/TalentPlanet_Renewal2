package accepted.talentplanet_renewal2.Messanger.Chatting;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import accepted.talentplanet_renewal2.MyFirebaseMessagingService;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;


/**
 * Created by Accepted on 2018-03-06.
 */

public class MainActivity extends AppCompatActivity implements MyFirebaseMessagingService.MessageReceivedListener {

    private Context mContext;
    private ListView listView;
    private Adapter adapter;
    private ArrayList<ListItem> arrayList;
    private LinearLayout ll_SendBtnContainer;
    private ScrollView sv_Messanger_Chatting;
    private EditText et_ChattingTxt;
    LinearLayout ll_EditTxtContainer;
    private TextView tv_User;

    final int interval = 100;

    final int maxInterval = 5000;
    public static String receiverID;
    private String receiverName;
    public int roomID;

    private boolean isTimeChanged;
    private boolean isDateChanged;
    private int messageType; // Send : 1, Get : 2
    private boolean isPicture;

    // SQLite 설정
    public SQLiteDatabase sqliteDatabase = null;

    public Thread thread1;
    public boolean running = true;

    public String lastMessageID = "0";
    private Bitmap picture = null;
    private TimeZone time= TimeZone.getTimeZone("Asia/Seoul");
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messanger_chatting);

        mContext = getApplicationContext();
        receiverID = getIntent().getStringExtra("userID");
        roomID = getIntent().getIntExtra("roomID", 0);
        receiverName = getIntent().getStringExtra("userName");



        ((ImageView)findViewById(R.id.img_back_toolbar_talentlist)).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                finish();
            }
        });

        ((TextView)findViewById(R.id.tv_toolbar_talentlist)).setText(getIntent().getStringExtra("userName"));
        ((ImageView)findViewById(R.id.iv_toolbar_search_talentlist)).setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(255, 102, 102));
        }



        Log.d("receiverID : ",receiverID);
        Log.d("receiverID : ", String.valueOf(roomID));
        Log.d("receiverID : ",receiverName);

        String dbName = "/accepted.db";
        try {
            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + dbName, null);

            String getStartMessageID = "SELECT START_MESSAGE_ID FROM TB_CHAT_ROOM WHERE ROOM_ID = " + roomID;
            Cursor cursor = sqliteDatabase.rawQuery(getStartMessageID, null);
            cursor.moveToFirst();
            lastMessageID = cursor.getString(0);

        } catch (SQLiteException e) {
            e.printStackTrace();
        }catch (CursorIndexOutOfBoundsException e2)
        {
            e2.printStackTrace();
        }




        arrayList = new ArrayList<>();
        adapter = new Adapter(arrayList, MainActivity.this, "NODATA", receiverID);

        sv_Messanger_Chatting = (ScrollView) findViewById(R.id.sv_Messanger_Chatting);

        listView = (ListView) findViewById(R.id.list_Chatting_Messanger);
        listView.requestFocusFromTouch();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view.getLastVisiblePosition() == (totalItemCount-1))
                {
                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                }else
                {
                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                }
            }
        });



        ll_SendBtnContainer = (LinearLayout) findViewById(R.id.ll_SendBtnContainer_Chatting_Messanger);
        et_ChattingTxt = (EditText) findViewById(R.id.et_Chatting_Messanger);
        et_ChattingTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputMethodManager =(InputMethodManager)MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });



        ll_EditTxtContainer = (LinearLayout) findViewById(R.id.ll_EditTxtContainer_Chatting_Messanger);


        ll_SendBtnContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String chattingEditTxt = et_ChattingTxt.getText().toString();

                if(chattingEditTxt.isEmpty()){
                    Toast.makeText(mContext, "메세지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMessage(chattingEditTxt);
                et_ChattingTxt.setText("");

            }
        });


        getPicture();

    }


    public static String getReceiverID()
    {
        return receiverID;
    }

    public boolean refreshChatLog(){
        String selectBasicChat = "SELECT USER_ID, CONTENT, CREATION_DATE, POINT_MSG_FLAG, POINT_SEND_FLAG, MESSAGE_ID FROM TB_CHAT_LOG WHERE ROOM_ID = " + roomID +" AND MASTER_ID = '"+ SaveSharedPreference.getUserId(mContext) +"' AND MESSAGE_ID > "+lastMessageID+"";
        Cursor cursor = sqliteDatabase.rawQuery(selectBasicChat, null);
        cursor.moveToFirst();
        boolean isRunning = false;
        while(!cursor.isAfterLast()){
            isRunning = true;

            String sender = cursor.getString(0);
            String content = cursor.getString(1);
            String creationDate = cursor.getString(2);
            String[] nowDateTemp = creationDate.split(",");
            boolean isPoint = Integer.parseInt(cursor.getString(3)) > 0;
            boolean isCompleted = Integer.parseInt(cursor.getString(4)) > 0;
            int messageID = Integer.parseInt(String.valueOf(cursor.getLong(5)));
            final String nowDate = nowDateTemp[0];
            final String nowTime = nowDateTemp[1].substring(0, 8);

            if(sender.equals(receiverID)) {
                messageType = 2;
                isPicture = true;
            }
            else{
                messageType = 1;
                isPicture = false;
            }

            if(arrayList.size() == 0){
                isTimeChanged = true;
                isDateChanged = true;
                ListItem item = new ListItem(R.drawable.picure_basic, content, creationDate, messageType, isPicture, isTimeChanged, isDateChanged);
                item.setPointSend(isPoint);
                item.setCompleted(isCompleted);
                item.setTargetID(receiverID);
                item.setTargetName(receiverName);
                item.setMessageID(messageID);
                arrayList.add(item);
            }else{
                int prevPosition = arrayList.size() - 1;
                ListItem temp = arrayList.get(prevPosition);
                String prevDate = temp.getDate();
                String prevTime;
                String[] dateTemp = prevDate.split(",");
                prevDate = dateTemp[0];
                prevTime = dateTemp[1].substring(0, 8);
                if(prevDate.equals(nowDate)){
                    isDateChanged = false;
                    if(temp.getMessageType() != messageType){
                        isTimeChanged = true;
                    }else {
                        if(prevTime.equals(nowTime)){
                            if(messageType == 2){
                                isPicture = false;
                            }
                            temp.setTimeChanged(false);
                            arrayList.set(prevPosition, temp);
                        }
                    }
                }else{
                    isDateChanged = true;
                    isTimeChanged = true;
                }

                ListItem item = new ListItem(R.drawable.picure_basic, content, creationDate, messageType, isPicture, isTimeChanged, isDateChanged);
                item.setPointSend(isPoint);
                item.setCompleted(isCompleted);
                item.setTargetID(receiverID);
                item.setTargetName(receiverName);
                item.setMessageID(messageID);
                arrayList.add(item);

            }
            cursor.moveToNext();
        }



        if(isRunning){
            cursor.moveToPrevious();
            lastMessageID = String.valueOf(cursor.getInt(0));
            Log.d("lastMessageID", lastMessageID);

            String updateSql = "UPDATE TB_CHAT_LOG SET READED_FLAG = 'Y' WHERE ROOM_ID = " + roomID;
            sqliteDatabase.execSQL(updateSql);
        }

        cursor.close();

        return isRunning;
    }

    public void sendMessage(final String content){


        final Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
        simpleDateFormat.setTimeZone(time);
        final String nowDateStr = simpleDateFormat.format(date);
        String[] nowDateTemp = nowDateStr.split(",");
        final String nowDate = nowDateTemp[0];
        final String nowTime = nowDateTemp[1].substring(0, 8);

        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Chat/sendMessage.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.d("result = ", response);
                    JSONObject obj = new JSONObject(response);
                    SimpleDateFormat sdf = new SimpleDateFormat("a hh:mm");
                    sdf.setTimeZone(time);
                    String dateStr = sdf.format(date);
                    if(obj.getString("MESSAGE_ID") != null && !obj.getString("MESSAGE_ID").isEmpty()){
                        sqliteDatabase.execSQL("INSERT INTO TB_CHAT_LOG(MESSAGE_ID, ROOM_ID, MASTER_ID, USER_ID, CONTENT, CREATION_DATE) VALUES (" + obj.getString("MESSAGE_ID") + ","+roomID+" ,'" + SaveSharedPreference.getUserId(mContext) + "', '"+SaveSharedPreference.getUserId(mContext)+"','"+content+"','"+nowDateStr+"')");
                    }else{
                        Toast.makeText(mContext, "메세지 전송 실패.", Toast.LENGTH_SHORT).show();
                    }
                    if(refreshChatLog()){
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(adapter.getCount() - 1);
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("senderID", SaveSharedPreference.getUserId(mContext));
                params.put("receiverID", receiverID);
                params.put("content", content);
                params.put("sendDate", nowDateStr);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    @Override
    public void onResume(){
        super.onResume();
        MyFirebaseMessagingService.setOnMessageReceivedListener(this);
    }

    public void getPicture(){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Login/getMyPicture.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.d("result = ", response);
                    JSONObject obj = new JSONObject(response);
                    if(!obj.getString("S_FILE_PATH").equals("NODATA")){
                        sqliteDatabase.execSQL("UPDATE TB_CHAT_ROOM SET FILE_PATH = '" + obj.getString("S_FILE_PATH") + "' WHERE ROOM_ID = " + roomID);
                        adapter = new Adapter(arrayList, MainActivity.this, obj.getString("S_FILE_PATH"), receiverID);
                        Log.d("getFilePath = ", obj.getString("S_FILE_PATH") + ", " + "UPDATE TB_CHAT_ROOM SET FILE_PATH = '" + obj.getString("S_FILE_PATH") + "' WHERE ROOM_ID = " + roomID);
                    }
                    if (refreshChatLog()) {
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(adapter.getCount() - 1);
                    }

                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("userID", receiverID);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);

    }

    protected void onPause(){
        super.onPause();
        MyFirebaseMessagingService.setOnMessageReceivedListener(null);
    }

    Handler m_hd = new Handler(){
        public void handleMessage(Message msg){
            Bundle bd = msg.getData();

            boolean isRunning = bd.getBoolean("arg", false);
            if(isRunning){
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        running = false;
        if(thread1 != null)
            thread1.interrupt();
        sqliteDatabase.close();
    }

    @Override
    public void onMessageRecieved(){
        Bundle bd = new Bundle();
        if(refreshChatLog()){
            bd.putBoolean("arg", true);
        }else{
            bd.putBoolean("arg", false);
        }

        Message msg = m_hd.obtainMessage();
        msg.setData(bd);

        m_hd.sendMessage(msg);
    }





}
