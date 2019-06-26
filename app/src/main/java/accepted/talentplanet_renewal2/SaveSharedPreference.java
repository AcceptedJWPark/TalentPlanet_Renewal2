package accepted.talentplanet_renewal2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kwonhong on 2017-10-14.
 */

public class SaveSharedPreference{
    static final String PREF_USER_NAME = "username";
    static final String PREF_USER_ID = "userid";
    static final String PREF_USER_PW = "userpw";

    static final String PREF_USER_GENDER = "usergender";
    static final String PREF_USER_BIRTH= "userbirth";
    static final String PREF_USER_GP_LAT= "userGP_LAT";
    static final String PREF_USER_GP_LNG= "userGP_LNG";

    static final String SERVER_IP2 = "https://13.209.191.97/Accepted/";
    static final String SERVER_IP = "http://175.213.4.39/Accepted/";
    static final String IMAGE_URI = "http://13.209.191.97/Accepted/";
    static final String IMAGE_URI2 = "http://175.213.4.39/Accepted/";
    static final String PREF_GIVE_DATA = "giveData";
    static final String PREF_TAKE_DATA = "takeData";
    static final String PREF_GEO_POINT = "geoPoint";
    static final String PREF_FRIEND_ARRAY = "friendList";
    static final String PREF_TALENT_POINT = "talentPoint";
    static final String PREF_FCM_TOKEN = "fcmToken";
    static final String PREF_ALARM_ARRAY = "alarmArray";
    static final String PREF_MESSAGE_PUSH_GRANT = "messagePushGrant";
    static final String PREF_CONDITION_PUSH_GRANT = "conditionPushGrant";
    static final String PREF_ANSWER_PUSH_GRANT = "answerPushGrant";
    static String myPicturePath = null;
    static String myThumbPicturePath = null;
    static String fcmToken = null;
    static final String PREF_FIRST_LOADING = "firstLoading";
    public static final String WIFI_STATE = "WIFI";
    public static final String MOBILE_STATE = "MOBILE";
    public static final String NONE_STATE = "NONE";

    public static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";

    static DrawerLayout slidingMenuDL;
    static View drawerView;


    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }



    public static void setPrefUsrName(Context ctx, String userName){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setPrefUsrPw(Context ctx, String usePw){
        Log.d("perf userpw", usePw);
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PW, usePw);
        editor.commit();
    }

    public static void setPrefUsrId(Context ctx, String useId){
        Log.d("perf userid", useId);
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, useId);
        editor.commit();
    }

    public static void setPrefTalentPoint(Context ctx, int point){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_TALENT_POINT, point);
        editor.commit();
    }

    public static void setFirstLoadingFlag(Context ctx, boolean flag){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_FIRST_LOADING, flag);
        editor.commit();
    }

    public static void setPrefUserBirth(Context ctx, String birth){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_BIRTH, birth);
        editor.commit();
    }

    public static void setPrefGender(Context ctx, String gender){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_GENDER, gender);
        editor.commit();
    }

    public static void setPrefUserGpLng(Context ctx, String lng){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_GP_LNG, lng);
        editor.commit();
    }

    public static void setPrefUserGpLat(Context ctx, String lat){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_GP_LAT, lat);
        editor.commit();
    }

    public static void setPrefFcmToken(Context ctx, String token){
//        SQLiteDatabase sqliteDatabase;
//        String dbName = "/accepted.db";
//
//        try{
//            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(ctx.getFilesDir() + dbName, null);
//            String sqlUpsert = "INSERT OR REPLACE INTO TB_FCM_TOKEN(TOKEN) VALUES ('"+token+"')";
//            sqliteDatabase.execSQL(sqlUpsert);
//
//            Log.d("insert friend", sqlUpsert);
//            sqliteDatabase.close();
//        }catch (SQLiteException e){
//            e.printStackTrace();
//        }
        fcmToken = token;
    }

    public static void removePrefFcmToken(Context ctx){
//        SQLiteDatabase sqliteDatabase;
//        String dbName = "/accepted.db";
//
//        try{
//            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(ctx.getFilesDir() + dbName, null);
//            String sqlUpsert = "DELETE FROM TB_FCM_TOKEN";
//            sqliteDatabase.execSQL(sqlUpsert);
//
//            sqliteDatabase.close();
//        }catch (SQLiteException e){
//            e.printStackTrace();
//        }
        fcmToken = null;
    }

    public static void setPrefPushGrant(Context ctx, boolean messageGrant, boolean conditionGrant, boolean answerGrant){
        SQLiteDatabase sqliteDatabase;
        String dbName = "/accepted.db";

        try{
            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(ctx.getFilesDir() + dbName, null);
            String sqlUpsert = "INSERT OR REPLACE INTO TB_GRANT(USER_ID, MESSAGE_GRANT, CONDITION_GRANT, ANSWER_GRANT) VALUES ('"+getUserId(ctx)+"', "+ ((messageGrant) ? 1 : 0) + ", " + ((conditionGrant) ? 1 : 0)  + ", " + ((answerGrant) ? 1 : 0)  + ")";
            sqliteDatabase.execSQL(sqlUpsert);

            Log.d("grant upsert = ", sqlUpsert);

            sqliteDatabase.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public static int getTalentPoint(Context ctx){
        return getSharedPreferences(ctx).getInt(PREF_TALENT_POINT, 0);
    }

    public static boolean getFirstLoadingFlag(Context ctx){
        return getSharedPreferences(ctx).getBoolean(PREF_FIRST_LOADING, true);
    }

    public static String getPrefUserPw(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_PW,"");
    }

    public static String getUserName(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getUserId(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    public static String getPrefUserGpLng(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_GP_LNG, "");
    }

    public static String getPrefUserGpLat(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_GP_LAT, "");
    }

    public static String getPrefUserBirth(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_BIRTH, "");
    }

    public static String getPrefUserGender(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_GENDER, "");
    }

    public static void clearUserInfo(Context ctx){
        myThumbPicturePath = null;
        myPicturePath = null;
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
        setFirstLoadingFlag(ctx, false);
    }

  public static String getServerIp(){
        return SERVER_IP;
  }

    public static Response.ErrorListener getErrorListener(final Context context){
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.d("res", res);
                        Toast.makeText(context, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();

                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        };

        return errorListener;
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setMyPicturePath(String path, String thumbPath){
        myPicturePath = path;
        myThumbPicturePath = thumbPath;
    }

    public static String getMyPicturePath(){
        return myPicturePath;
    }
    public static String getMyThumbPicturePath(){
        return myThumbPicturePath;
    }

    public static String getImageUri(){
        return IMAGE_URI2;
    }

    public static int makeChatRoom(Context ctx, String userID, String userName, String filePath){
        SQLiteDatabase sqliteDatabase;
        String dbName = "/accepted.db";

        final Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
        final String nowDateStr = simpleDateFormat.format(date);

        try {
            int roomID;
            int startMessageID;
            String creationDate;
            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(ctx.getFilesDir() + dbName, null);

            String test = "SELECT IFNULL(MAX(A.START_MESSAGE_ID), 0) AS START_MESSAGE_ID FROM TB_CHAT_ROOM A WHERE A.USER_ID = '" + userID+ "'";
            Cursor cursort = sqliteDatabase.rawQuery(test, null);
            cursort.moveToFirst();
            startMessageID = cursort.getInt(0);
            Log.d("start_message_id", "" + startMessageID);

            test = "SELECT IFNULL(MAX(B.ROOM_ID), (SELECT IFNULL(MAX(C.ROOM_ID) + 1, 1) FROM TB_CHAT_ROOM C)) AS MESSAGE_ID FROM TB_CHAT_ROOM B WHERE B.USER_ID = '" + userID + "' AND B.ACTIVATE_FLAG = 'Y'";
            cursort = sqliteDatabase.rawQuery(test, null);
            cursort.moveToFirst();
            roomID = cursort.getInt(0);
            Log.d("room_id", "" + roomID);

            test = "SELECT IFNULL(MAX(D.CREATION_DATE), '"+nowDateStr+"') AS CREATION_DATE FROM TB_CHAT_ROOM D WHERE D.USER_ID = '" + userID+ "'";
            cursort = sqliteDatabase.rawQuery(test, null);
            cursort.moveToFirst();
            creationDate = cursort.getString(0);
            Log.d("creation_date", "" + creationDate );

            String sqlUpsert = "INSERT OR REPLACE INTO TB_CHAT_ROOM(ROOM_ID, USER_ID, USER_NAME, MASTER_ID, START_MESSAGE_ID, CREATION_DATE, LAST_UPDATE_DATE, ACTIVATE_FLAG, FILE_PATH) VALUES ("+roomID+", '" + userID + "', '"+userName+"', '"+getUserId(ctx)+"', "+startMessageID+", '"+creationDate+"', '"+nowDateStr+"', 'Y', '"+ filePath + "')";
            sqliteDatabase.execSQL(sqlUpsert);

            Log.d("Insert SQL = ", sqlUpsert);
            sqliteDatabase.close();


            return roomID;


        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void setGiveTalentData(Context ctx, MyTalent Data){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        Gson gson = new Gson();
        String json = gson.toJson(Data);
        editor.putString(PREF_GIVE_DATA, json);
        editor.commit();
    }

    public static MyTalent getGiveTalentData(Context ctx){
        Gson gson = new Gson();
        String json = getSharedPreferences(ctx).getString(PREF_GIVE_DATA, "");
        MyTalent data = gson.fromJson(json, MyTalent.class);
        return data;
    }

    public static void setTakeTalentData(Context ctx, MyTalent Data){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        Gson gson = new Gson();
        String json = gson.toJson(Data);
        editor.putString(PREF_TAKE_DATA, json);
        editor.commit();
    }

    public static MyTalent getTakeTalentData(Context ctx){
        Gson gson = new Gson();
        String json = getSharedPreferences(ctx).getString(PREF_TAKE_DATA, "");
        MyTalent data = gson.fromJson(json, MyTalent.class);
        return data;
    }

    public static boolean getMessagePushGrant(Context ctx){
        SQLiteDatabase sqliteDatabase;
        String dbName = "/accepted.db";
        boolean pushGrant = true;
        try{
            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(ctx.getFilesDir() + dbName, null);
            String sqlSelect = "SELECT MESSAGE_GRANT FROM TB_GRANT WHERE USER_ID = '" + getUserId(ctx) + "'";
            Cursor cursor = sqliteDatabase.rawQuery(sqlSelect, null);
            cursor.moveToFirst();
            Log.d("message grant query = ", sqlSelect);
            Log.d("message grant = ", cursor.getInt(0) + "");

            pushGrant = (cursor.getInt(0) > 0);

            cursor.close();

            sqliteDatabase.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return pushGrant;
    }

    public static boolean getConditionPushGrant(Context ctx){
        SQLiteDatabase sqliteDatabase;
        String dbName = "/accepted.db";
        boolean pushGrant = true;
        try{
            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(ctx.getFilesDir() + dbName, null);
            String sqlSelect = "SELECT CONDITION_GRANT FROM TB_GRANT WHERE USER_ID = '" + getUserId(ctx) + "'";
            Cursor cursor = sqliteDatabase.rawQuery(sqlSelect, null);
            cursor.moveToFirst();

            pushGrant = (cursor.getInt(0) > 0);

            cursor.close();

            sqliteDatabase.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return pushGrant;
    }

    public static boolean getAnswerPushGrant(Context ctx){
        SQLiteDatabase sqliteDatabase;
        String dbName = "/accepted.db";
        boolean pushGrant = true;
        try{
            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(ctx.getFilesDir() + dbName, null);
            String sqlSelect = "SELECT ANSWER_GRANT FROM TB_GRANT WHERE USER_ID = '" + getUserId(ctx) + "'";
            Cursor cursor = sqliteDatabase.rawQuery(sqlSelect, null);
            cursor.moveToFirst();

            pushGrant = (cursor.getInt(0) > 0);

            cursor.close();

            sqliteDatabase.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return pushGrant;
    }
}
