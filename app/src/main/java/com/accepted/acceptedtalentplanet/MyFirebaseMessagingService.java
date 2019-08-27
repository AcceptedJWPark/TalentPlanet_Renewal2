package com.accepted.acceptedtalentplanet;

/**
 * Created by kwonhong on 2018-03-09.
 */

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.accepted.acceptedtalentplanet.Home.MainActivity;
import com.accepted.acceptedtalentplanet.JoinLogin.MainActivity_Login;
import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.TalentList.MainActivity_TalentList;

import static com.accepted.acceptedtalentplanet.Messanger.Chatting.MainActivity.receiverID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static final String GROUP_KEY_ALARM = "Alarm";
    private static final int MESSAGE_NOTIFICATION_ID = 1;
    private static final int CONDITION_NOTIFICATION_ID = 2;
    private static final int BOARD_NOTIFICATION_ID = 0;
    private static final int INTERESTING_NOTIFICATION_ID = 4;
    private static final int SUMMARY_NOTIFICATION_ID = 3;
    private static final String MY_CHANNEL_ID = "NOTIFICATION_CHANNEL_1";
    public static boolean isNewMessageArrive = false;

    private static NotificationManagerCompat notificationManagerCompat;

    private String alarmType = null;
    private String alarmTxt = null;

    private boolean messagePushGrant;
    private boolean conditionPushGrant;
    private boolean answerPushGrant;

    private Context mContext;


    private TimeZone time= TimeZone.getTimeZone("Asia/Seoul");

    private String topActivityName;

    private JSONObject userInfo;

    private Intent intent1 = null;

    private String datas = null;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        mContext = getApplicationContext();
        messagePushGrant = SaveSharedPreference.getMessagePushGrant(mContext);
        conditionPushGrant = SaveSharedPreference.getConditionPushGrant(mContext);
        answerPushGrant = SaveSharedPreference.getAnswerPushGrant(mContext);

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = am.getRunningTasks(1);
        ComponentName topActivity = info.get(0).topActivity;
        topActivityName = topActivity.getClassName();


        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //Log.d(TAG, "Message content: " + remoteMessage.getData().get("message"));
            if (remoteMessage.getData().get("type").equals("dupLogin")) {
                SaveSharedPreference.removePrefFcmToken(mContext);
                SaveSharedPreference.clearUserInfo(mContext);
                Intent i = new Intent(mContext, MainActivity_Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("dupFlag", true);
                mContext.startActivity(i);
            } else {
                if (remoteMessage.getData().get("type").equals("Message")) {
                    getMessage(remoteMessage.getData().get("datas"));
                }

                datas = remoteMessage.getData().get("datas");

                addNotificationList(remoteMessage.getData().get("type"));
//                addAlarmList(remoteMessage.getData().get("type"));

                if (mMessageReceivedListener != null) {
                    update();
                }

                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                    scheduleJob();
                } else {
                    // Handle message within 10 seconds
                    handleNow();
                }

                if (messagePushGrant || conditionPushGrant || answerPushGrant) {

                    // Check if message contains a notification payload.
                    if (remoteMessage.getNotification() != null) {
                        //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                    }

                    if (intent1 != null) {

                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder mBuilder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
                            NotificationChannel channel = new NotificationChannel(MY_CHANNEL_ID,
                                    "Channel human readable title",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            notificationManager.createNotificationChannel(channel);
                            mBuilder = new NotificationCompat.Builder(this, channel.getId());

                            Log.d("this is ", channel.getId());
                        } else {
                            mBuilder = new NotificationCompat.Builder(this, MY_CHANNEL_ID);

                        }
                        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle(alarmTxt)
                                .setAutoCancel(true)
                                .setVibrate(new long[]{1, 1000})
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(contentIntent);
                        notificationManagerCompat = NotificationManagerCompat.from(this);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Notification summaryNotification =
                                    new NotificationCompat.Builder(this, MY_CHANNEL_ID)
                                            .setContentTitle("새로운 알람이 있습니다.")
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .setStyle(new NotificationCompat.InboxStyle()
                                                    .setSummaryText("알람을 확인하세요!"))
                                            //specify which group this notification belongs to
                                            .setGroup(GROUP_KEY_ALARM)
                                            //set this notification as the summary for the group
                                            .setGroupSummary(true)
                                            .setGroupAlertBehavior(Notification.GROUP_ALERT_CHILDREN)
                                            .setAutoCancel(true)
                                            .build();
                            summaryNotification.defaults = 0;
                            notificationManagerCompat.notify(SUMMARY_NOTIFICATION_ID, summaryNotification);
                        }

                        mBuilder.setGroup(GROUP_KEY_ALARM);

                        if(Build.VERSION.SDK_INT <= 18){
                            NotificationManager nm = (NotificationManager) getSystemService(mContext.NOTIFICATION_SERVICE);
                            if (remoteMessage.getData().get("type").equals("Interest")) {
                                nm.notify(INTERESTING_NOTIFICATION_ID, mBuilder.build());
                            } else if (remoteMessage.getData().get("type").equals("Message")) {
                                nm.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
                            } else if (remoteMessage.getData().get("type").contains("Interesting")) {
                                nm.notify(CONDITION_NOTIFICATION_ID, mBuilder.build());
                            } else {
                                Log.d("asdfasa", "1234");
                                nm.notify(BOARD_NOTIFICATION_ID, mBuilder.build());
                            }
                        }else {
                            if (remoteMessage.getData().get("type").equals("Interest")) {
                                notificationManagerCompat.notify(INTERESTING_NOTIFICATION_ID, mBuilder.build());
                            } else if (remoteMessage.getData().get("type").equals("Message")) {
                                notificationManagerCompat.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
                            } else if (remoteMessage.getData().get("type").contains("Interesting")) {
                                notificationManagerCompat.notify(CONDITION_NOTIFICATION_ID, mBuilder.build());
                            } else {
                                Log.d("asdfasa", "1234123123");
                                notificationManagerCompat.notify(BOARD_NOTIFICATION_ID, mBuilder.build());
                            }
                        }

                    }
                }
            }
        }
    }




    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
//        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
//        // [END dispatch_job]
    }


//    private void addAlarmList(String type){
//        ArrayList<ListItem> arrayList = SaveSharedPreference.getPrefAlarmArry(mContext);
//        if (arrayList == null) {
//            arrayList = new ArrayList<>();
//        }
//
//        String userName = null;
//        String userId = null;
//        String unformatedDate = null;
//        int talentID = -1;
//        int talentType = -1;
//        int roomId = -1;
//
//        switch (type){
//            case "Message": {
//                try {
//                    JSONObject obj = new JSONObject(datas);
//                    userName = obj.getString("USER_NAME");
//                    userId = obj.getString("USER_ID");
//                    unformatedDate = obj.getString("CREATION_DATE_STRING");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                String formattedDate = dateFormat(unformatedDate);
//
//                int countMessage = 1;
//                ListItem listItem = new ListItem(R.drawable.picure_basic, userName, "새로운 메세지 1건이 있습니다.", 1, formattedDate, 6, R.drawable.icon_delete, false);
//                listItem.setUserId(userId);
//                listItem.setUserName(userName);
//                if(arrayList.size()>0) {
//                    arrayList.add(0, listItem);
//                    for (int i = 1; i < arrayList.size(); i++) {
//                        if(arrayList.get(i).getactivityChange_CODE() == 6) {
//                            if (arrayList.get(0).getUserId().equals(arrayList.get(i).getUserId())) {
//                                countMessage = arrayList.get(i).getCountMessage() + 1;
//                                arrayList.remove(i);
//                                arrayList.remove(0);
//                                listItem.settxt("새로운 메세지 " + countMessage + "건이 있습니다.");
//                                listItem.setCountMessage(countMessage);
//                                arrayList.add(0, listItem);
//                            }
//                        }
//                    }
//                }
//                else
//                {
//                    arrayList.add(0, listItem);
//                }
//                SaveSharedPreference.setPrefAlarmArray(mContext, arrayList);
//                break;
//            }
//            case "QNA": {
//
//                try {
//                    JSONObject obj = new JSONObject(datas);
//                    unformatedDate = obj.getString("CREATION_DATE_STRING");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String formatedDate = dateFormat(unformatedDate);
//                alarmTxt = "Q&A 질문에 대한 답변이 완료되었습니다.";
//                arrayList.add(0, new ListItem(R.drawable.logo_fakefile, "Talent Planet", alarmTxt, formatedDate, 4, R.drawable.icon_delete, false));
//                SaveSharedPreference.setPrefAlarmArray(mContext, arrayList);
//                Log.d("lastDate = ", unformatedDate);
//                break;
//            }
//            case "Claim": {
//
//                try {
//                    JSONObject obj = new JSONObject(datas);
//                    unformatedDate = obj.getString("CREATION_DATE_STRING");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String formatedDate = dateFormat(unformatedDate);
//                alarmTxt = "신고하기에 대한 조치가 완료되었습니다.";
//                arrayList.add(0, new ListItem(R.drawable.logo_fakefile, "Talent Planet", alarmTxt, formatedDate, 5, R.drawable.icon_delete, false));
//                SaveSharedPreference.setPrefAlarmArray(mContext, arrayList);
//                break;
//            }
//            case "Interest": {
//                try {
//                    JSONObject obj = new JSONObject(datas);
//                    Log.d("long time = ", obj.getLong("CREATION_DATE") + "");
//                    Date tempDate = new Date(obj.getLong("CREATION_DATE"));
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
//                    sdf.setTimeZone(time);
//                    unformatedDate = sdf.format(tempDate);
//                    Log.d("unformatedDate", unformatedDate);
//                    userName = obj.getString("USER_NAME");
//                    talentID = obj.getInt("TALENT_ID");
//                    talentType = (obj.getString("TALENT_FLAG").equals("Y"))? 2 : 1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String formatedDate = dateFormat(unformatedDate);
//                Log.d(formatedDate,"formatedDate");
//                arrayList.add(0, new ListItem(R.drawable.logo_fakefile, userName, talentID, "Talent Planet", formatedDate, 1, talentType, R.drawable.icon_delete, false));
//                SaveSharedPreference.setPrefAlarmArray(mContext, arrayList);
//                break;
//            }
//            case "InterestingMatching":{
//                try {
//                    JSONObject obj = new JSONObject(datas);
//                    Date tempDate = new Date(obj.getLong("LAST_UPDATE_DATE"));
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
//                    sdf.setTimeZone(time);
//                    unformatedDate = sdf.format(tempDate);
//                    userName = obj.getString("USER_NAME");
//                    talentID = obj.getInt("TALENT_ID");
//                    talentType = (obj.getString("TALENT_FLAG").equals("Y"))? 2 : 1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                String formatedDate = dateFormat(unformatedDate);
//                arrayList.add(0, new ListItem(R.drawable.logo_fakefile, userName, talentID, "Talent Planet", formatedDate, 1, talentType, R.drawable.icon_delete, false));
//                break;
//            }
//            case "InterestingCancel":{
//                try {
//                    JSONObject obj = new JSONObject(datas);
//                    Date tempDate = new Date(obj.getLong("LAST_UPDATE_DATE"));
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
//                    sdf.setTimeZone(time);
//                    unformatedDate = sdf.format(tempDate);
//                    userName = obj.getString("USER_NAME");
//                    talentID = obj.getInt("TALENT_ID");
//                    talentType = (obj.getString("TALENT_FLAG").equals("Y"))? 2 : 1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                String formatedDate = dateFormat(unformatedDate);
//                arrayList.add(0, new ListItem(R.drawable.logo_fakefile, userName, talentID, "Talent Planet", formatedDate, 1, talentType, R.drawable.icon_delete, false));
//                break;
//            }
//            case "InterestingComplete":{
//                try {
//                    JSONObject obj = new JSONObject(datas);
//                    Date tempDate = new Date(obj.getLong("LAST_UPDATE_DATE"));
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
//                    sdf.setTimeZone(time);
//                    unformatedDate = sdf.format(tempDate);
//                    userName = obj.getString("USER_NAME");
//                    talentID = obj.getInt("TALENT_ID");
//                    talentType = (obj.getString("TALENT_FLAG").equals("Y"))? 2 : 1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                String formatedDate = dateFormat(unformatedDate);
//                arrayList.add(0, new ListItem(R.drawable.logo_fakefile, userName, talentID, "Talent Planet", formatedDate, 1, talentType, R.drawable.icon_delete, false));
//                break;
//            }
//        }
//    }

    private void addNotificationList(String type){
        intent1 = null;
        Log.d("asdfasdf", type);
        switch (type) {
            case "Message": {
                String userId = null;
                String recieverID1 = null;
                try {
                    JSONObject obj = new JSONObject(datas);
                    String userName = obj.getString("USER_NAME");
                    userId = obj.getString("USER_ID");
                    String unformatedDate = obj.getString("CREATION_DATE_STRING");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (messagePushGrant) {

                    if (topActivityName.equals("MainActivity")) {
                        return;
                    } else if (topActivityName.equals("MainActivity") && userId.equals(receiverID)) {
                        return;
                    } else if(topActivityName.equals("com.accepted.acceptedtalentplanet.Messanger.Chatting.MainActivity") && receiverID.equals(userId)) {
                        return;
                    }
                    else {
                        alarmType = "Message";
                        alarmTxt = "새로운 메세지가 도착했습니다.";
                        intent1 = new Intent(this, com.accepted.acceptedtalentplanet.Messanger.List.MainActivity.class);
//                        intent1.putExtra("userID", );
//                        intent1.putExtra("roomID", );
                        intent1.putExtra("alarmType", "Message");
                    }
                }
                break;
            }
            case "QNA":
                if (answerPushGrant) {
                    alarmType = "QNA";
                    alarmTxt = "문의하신 Q&A의 답변 완료되었습니다.";
                    intent1 = new Intent(this, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.putExtra("alarmType", "QNA");
                }
                break;
            case "Claim":
                if (answerPushGrant) {
                    alarmType = "Claim";
                    alarmTxt = "신고하신 건의 조치가 완료되었습니다.";
                    intent1 = new Intent(this, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.putExtra("alarmType", "Claim");
                }
                break;
//            case "Interest": {
//                if(conditionPushGrant) {
//                    int talentType = -1;
//                    try {
//                        JSONObject obj = new JSONObject(datas);
//                        talentType = (obj.getString("TALENT_FLAG").equals("Y")) ? 2 : 1;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    if (talentType == 1) {
//                        alarmTxt = "멘티에게 요청이 왔습니다.";
//                        intent1 = new Intent(this, MainActivity_Condition.class);
//                        intent1.putExtra("ismyMentor", "2");
//                    } else {
//                        alarmTxt = "멘토에게 프로필이 왔습니다.";
//                    }
//                }
//                break;
//            }
            case "InterestingMatching": {

                int talentType = -1;
                try {
                    JSONObject obj = new JSONObject(datas);
                    talentType = (obj.getString("MatchedFlag").equals("Y")) ? 2 : 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyTalent mt = (talentType == 1)?SaveSharedPreference.getTakeTalentData(mContext):SaveSharedPreference.getGiveTalentData(mContext);

                mt.setStatus("M");
                if(talentType == 2){
                    SaveSharedPreference.setGiveTalentData(mContext, mt);
                }else{
                    SaveSharedPreference.setTakeTalentData(mContext, mt);
                }

//                if(conditionPushGrant) {
//
//                    intent1 = new Intent(this, com.accepted.acceptedtalentplanet.Condition.MainActivity_Condition.class);
//                    if (talentType == 2) {
//                        alarmTxt = "재능 드림 상태가 진행중으로 변경되었습니다.";
//                        intent1.putExtra("TalentCondition_TalentFlag", "Give");
//                    } else {
//                        alarmTxt = "관심 재능 상태가 진행중으로 변경되었습니다.";
//                        intent1.putExtra("TalentCondition_TalentFlag", "Take");
//                    }
//                    break;
//                }
            }

            case "InterestingCancel":
                {
                    int talentType = -1;
                    try {
                        JSONObject obj = new JSONObject(datas);
                        talentType = (obj.getString("TALENT_FLAG").equals("Y")) ? 2 : 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MyTalent mt = (talentType == 1)?SaveSharedPreference.getTakeTalentData(mContext):SaveSharedPreference.getGiveTalentData(mContext);

                    mt.setStatus("P");
                    if(talentType == 2){
                        SaveSharedPreference.setGiveTalentData(mContext, mt);
                    }else{
                        SaveSharedPreference.setTakeTalentData(mContext, mt);
                    }
                    if(conditionPushGrant) {

                        if (talentType == 2) {
                            alarmTxt = "진행 중인 재능 드림이 취소 되었습니다.";
                        } else {
                            alarmTxt = "진행 중인 관심 재능이 취소되었습니다.";
                        }
                        alarmType = "InterestingCancel";
                        intent1 = new Intent(this, MainActivity_TalentList.class);
                        intent1.putExtra("alarmType", alarmType);
                        break;
                    }
            }
                case "InterestingComplete": {
                    int talentType = -1;
                    try {
                        JSONObject obj = new JSONObject(datas);
                        talentType = (obj.getString("TALENT_FLAG").equals("Y")) ? 2 : 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MyTalent mt = (talentType == 1)?SaveSharedPreference.getTakeTalentData(mContext):SaveSharedPreference.getGiveTalentData(mContext);

                    mt.setStatus("C");
                    if(talentType == 2){
                        SaveSharedPreference.setGiveTalentData(mContext, mt);
                    }else{
                        SaveSharedPreference.setTakeTalentData(mContext, mt);
                    }
//                    if (conditionPushGrant) {
//
//                        intent1 = new Intent(this, com.accepted.acceptedtalentplanet.Condition.MainActivity_Condition.class);
//                        if (talentType == 2) {
//                            alarmTxt = "진행 중인 재능 드림이 완료되었습니다.";
//                            intent1.putExtra("TalentCondition_TalentFlag", "Give");
//
//                        } else {
//                            alarmTxt = "진행 중인 관심 재능이완료 되었습니다.";
//                            intent1.putExtra("TalentCondition_TalentFlag", "Take");
//                        }
//                        break;
//                    }
                }
                //
                case "Share":
                    intent1 = new Intent(this, com.accepted.acceptedtalentplanet.Home.MainActivity.class);

                    try {
                        JSONObject obj = new JSONObject(datas);
                        String isMentor = obj.getString("isMentor");
                        String mentorID = obj.getString("MentorID");
                        String menteeID = obj.getString("MenteeID");

                        if (isMentor.equals("Y")) {
                            alarmTxt = mentorID + " 님께서 평가를 하셨습니다!";

                            try {
                                userInfo = obj.getJSONObject("mentorInfo");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (isMentor.equals("N")) {
                            alarmTxt = menteeID + " 님께서 평가를 하셨습니다!";

                            try {
                                userInfo = obj.getJSONObject("menteeInfo");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        intent1.putExtra("pushFlag", true);
                        intent1.putExtra("isMentor", isMentor);
                        intent1.putExtra("MentorID", mentorID);
                        intent1.putExtra("MenteeID", menteeID);
                        intent1.putExtra("userName", userInfo.getString("USER_NAME"));
                        intent1.putExtra("userGender", userInfo.getString("GENDER"));
                        intent1.putExtra("userInfo", userInfo.getString("USER_BIRTH"));
                        intent1.putExtra("BIRTH_FLAG", userInfo.getString("BIRTH_FLAG"));
                        intent1.putExtra("userDescription", userInfo.getString("PROFILE_DESCRIPTION"));
                        intent1.putExtra("S_FILE_PATH", userInfo.getString("S_FILE_PATH"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

        }
    }

    private void getMessage(String datas){
        try {
            isNewMessageArrive = true;
            JSONObject obj = new JSONObject(datas);
            String dbName = "/accepted.db";
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + dbName, null);

            Log.d("TAG", "Datas: " + datas.toString());


            Log.d(TAG, obj.getString("S_FILE_PATH"));

            int roomID = SaveSharedPreference.makeChatRoom(mContext, obj.getString("USER_ID"), obj.getString("USER_NAME"), obj.getString("S_FILE_PATH"));
            sqLiteDatabase.execSQL("INSERT OR REPLACE INTO TB_CHAT_LOG(MESSAGE_ID, ROOM_ID, MASTER_ID, USER_ID, CONTENT, CREATION_DATE, READED_FLAG, POINT_MSG_FLAG) VALUES (" + obj.getString("MESSAGE_ID") + ", " + roomID + ",'" + SaveSharedPreference.getUserId(mContext) + "' , '" + obj.getString("USER_ID") + "','" + obj.getString("CONTENT").replace("'", "''") + "','" + obj.getString("CREATION_DATE_STRING") + "', 'N', '" + obj.getString("POINT_MSG_FLAG") + "')");
            sqLiteDatabase.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
//                        .setContentTitle("FCM Message")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private String dateFormat(String lastDate)
    {
        Date tempDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
        sdf.setTimeZone(time);
        String nowDateStr = sdf.format(tempDate);
        String[] nowDateTemp = nowDateStr.split(",");
        String nowDate = nowDateTemp[0];

        Log.d("LastDate, NowDate", lastDate + ", " + nowDateStr);

        String[] dateTemp = lastDate.split(",");
        lastDate = dateTemp[0];
        String dateTime = dateTemp[1].substring(0, 8);
        lastDate = (lastDate.equals(nowDate)) ? dateTime : lastDate;

        return lastDate;
    }

    private static MessageReceivedListener mMessageReceivedListener;
    public interface MessageReceivedListener{
        public void onMessageRecieved();
    }

    public static void setOnMessageReceivedListener(MessageReceivedListener listener){
        mMessageReceivedListener = listener;
    }

    private void update(){
        if(mMessageReceivedListener != null){
            mMessageReceivedListener.onMessageRecieved();
        }
    }


}
