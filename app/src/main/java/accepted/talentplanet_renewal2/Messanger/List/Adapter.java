package accepted.talentplanet_renewal2.Messanger.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Profile.MainActivity_Profile;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

/**
 * Created by Accepted on 2018-03-05.
 */

public class Adapter extends BaseAdapter {

    private ArrayList<ListItem> messanger_Arraylist = new ArrayList<>();
    Context mContext;


    public Adapter(ArrayList<ListItem> messanger_Arraylist, Context mContext) {
        this.messanger_Arraylist = messanger_Arraylist;
        this.mContext = mContext;
    }

    public Adapter()
    {}

    @Override

    public int getCount() {
        return messanger_Arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return messanger_Arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void switchFlag_deleteBtn(boolean deleteBtn_Clicked) {
        if (deleteBtn_Clicked) {
            for (ListItem item : messanger_Arraylist) {
                item.setMessanger_DeleteBtn(true);

            }

        } else {
            for (ListItem item : messanger_Arraylist) {
                item.setMessanger_DeleteBtn(false);
            }
        }
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final int roomID = messanger_Arraylist.get(position).getRoomID();
        final String userID = messanger_Arraylist.get(position).getMessanger_userID();
        View view = convertView;
        ViewHolder holder;
        view = null;

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.messanger_list_bg, parent, false);
            holder = new ViewHolder();

            holder.messsanger_Pic = view.findViewById(R.id.iv_pictureContainer_List_Messanger);
            holder.messanger_Name = view.findViewById(R.id.ll_name_List_Messanger);
            holder.messanger_Content = view.findViewById(R.id.ll_txt_List_Messanger);
            holder.messanger_Count = view.findViewById(R.id.tv_count_List_Alarm);
            holder.Messanger_List_LL = view.findViewById(R.id.ll_Messanger_List);
            holder.messanger_Date = view.findViewById(R.id.tv_date_Alarm);
            holder.Messanger_List_DateLL = view.findViewById(R.id.ll_deleteContainer_Alarm);
            holder.Messanger_List_DeleteList = view.findViewById(R.id.iv_delete_List_Messanger);
            holder.ll_count_List_Alarm = view.findViewById(R.id.ll_count_List_Alarm);

            holder.ll_pictureContainer = view.findViewById(R.id.ll_pictureContainer_List_Messanger);
            holder.ll_txtContainer = view.findViewById(R.id.ll_txtContainer_List_Messanger);
            holder.rl_dateContainer = view.findViewById(R.id.rl_dateContainer_List_Messanger);

        view.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, accepted.talentplanet_renewal2.Messanger.Chatting.MainActivity.class);
                i.putExtra("roomID", roomID);
                i.putExtra("userID", userID);
                i.putExtra("userName", messanger_Arraylist.get(position).getMessanger_Name());
                mContext.startActivity(i);
            }
        });



        final View finalView = view;
        holder.ll_pictureContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getTalentID(messanger_Arraylist.get(position).getMessanger_userID(), finalView);
            }
        });


        holder.Messanger_List_DeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder AlarmDeleteDialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
                float textSize = mContext.getResources().getDimension(R.dimen.DialogTxtSize);
                AlarmDeleteDialog.setMessage("메시지를 삭제 하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SQLiteDatabase sqliteDatabase;
                                String dbName = "/accepted.db";
                                try {
                                    sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(mContext.getFilesDir() + dbName, null);
                                    String updateSql = "UPDATE TB_CHAT_LOG SET READED_FLAG = 'Y' WHERE ROOM_ID = " + messanger_Arraylist.get(position).getRoomID();
                                    sqliteDatabase.execSQL(updateSql);

                                    Log.d("update1", updateSql);

                                    String selectMaxMessageID = "SELECT MAX(MESSAGE_ID) FROM TB_CHAT_LOG WHERE ROOM_ID = "+ messanger_Arraylist.get(position).getRoomID();
                                    Cursor cursor = sqliteDatabase.rawQuery(selectMaxMessageID, null);
                                    cursor.moveToFirst();


                                    Log.d("select", selectMaxMessageID);

                                    String updateSql2 = "UPDATE TB_CHAT_ROOM SET ACTIVATE_FLAG = 'N', START_MESSAGE_ID = "+ cursor.getInt(0) + " WHERE ROOM_ID = "+ messanger_Arraylist.get(position).getRoomID();
                                    sqliteDatabase.execSQL(updateSql2);

                                    sqliteDatabase.close();

                                    Log.d("update2", updateSql2);
                                } catch (SQLiteException e) {
                                    e.printStackTrace();
                                }

                                messanger_Arraylist.remove(position);
                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = AlarmDeleteDialog.create();
                alertDialog.show();
                alertDialog.getButton((DialogInterface.BUTTON_POSITIVE)).setTextColor(finalView.getResources().getColor(R.color.loginPasswordLost));
                alertDialog.getButton((DialogInterface.BUTTON_NEGATIVE)).setTextColor(finalView.getResources().getColor(R.color.loginPasswordLost));
                TextView msgView = (TextView) alertDialog.findViewById(android.R.id.message);
                msgView.setTextSize(textSize);
            }
        });

        if(messanger_Arraylist.get(position).isMessanger_DeleteBtn())
        {
            holder.Messanger_List_DateLL.setVisibility(View.GONE);
            holder.Messanger_List_DeleteList.setVisibility(View.VISIBLE);
        }else
        {
            holder.Messanger_List_DateLL.setVisibility(View.VISIBLE);
            holder.Messanger_List_DeleteList.setVisibility(View.GONE);
        }

        if(messanger_Arraylist.get(position).getFilePath().equals("NODATA")) {
//            holder.messsanger_Pic.setBackgroundResource(messanger_Arraylist.get(position).getMesssanger_Pic());
        }
        else
        {
            Glide.with(mContext).load(SaveSharedPreference.getImageUri() + messanger_Arraylist.get(position).getFilePath()).into(holder.messsanger_Pic);
        }
        holder.messanger_Name.setText(messanger_Arraylist.get(position).getMessanger_Name());
        holder.messanger_Content.setText(messanger_Arraylist.get(position).getMessanger_Content());
        holder.messanger_Date.setText(messanger_Arraylist.get(position).getMessanger_Date());
        holder.messanger_Count.setText(String.valueOf(messanger_Arraylist.get(position).getMessanger_Count()));

        if(messanger_Arraylist.get(position).getMessanger_Count() == 0)
        {
            holder.ll_count_List_Alarm.setVisibility(View.GONE);
            holder.Messanger_List_LL.setBackgroundColor(0);
        }else
        {
            int messanger_Unread_Color = view.getResources().getColor(R.color.messangerList);
            holder.Messanger_List_LL.setBackgroundColor(messanger_Unread_Color);
        }

        return view;
    }

    static class ViewHolder
    {
        ImageView messsanger_Pic;
        TextView messanger_Name;
        TextView messanger_Content;
        TextView messanger_Date;
        TextView messanger_Count;
        LinearLayout Messanger_List_DateLL;
        LinearLayout Messanger_List_LL;
        ImageView Messanger_List_DeleteList;
        LinearLayout ll_count_List_Alarm;
        LinearLayout ll_pictureContainer;
        LinearLayout ll_txtContainer;
        RelativeLayout rl_dateContainer;
    }

    public void getTalentID(final String userID, final View finalView) {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Chat/getTalentID.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    final int giveTalentID = obj.getInt("GIVE_TALENT_ID");
                    final int takeTalentID = obj.getInt("TAKE_TALENT_ID");

                    final AlertDialog.Builder AlarmDeleteDialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));

                    AlarmDeleteDialog.setMessage("상대방 프로필 보기")
                            .setPositiveButton("Teacher", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, MainActivity_Profile.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("fromFriend", true);
                                    intent.putExtra("isMentor", true);
                                    mContext.startActivity(intent);

                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Student", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, MainActivity_Profile.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("fromFriend", true);
                                    intent.putExtra("isMentor", false);
                                    mContext.startActivity(intent);

                                    dialog.cancel();
                                }
                            })
                            .setNeutralButton("신고하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = AlarmDeleteDialog.create();
                    alertDialog.show();
                    alertDialog.getButton((DialogInterface.BUTTON_POSITIVE)).setTextColor(finalView.getResources().getColor(R.color.loginPasswordLost));
                    alertDialog.getButton((DialogInterface.BUTTON_NEGATIVE)).setTextColor(finalView.getResources().getColor(R.color.loginPasswordLost));
                    alertDialog.getButton((DialogInterface.BUTTON_NEUTRAL)).setTextColor(finalView.getResources().getColor(R.color.loginPasswordLost));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("userID", userID);
                return params;
            }
        };


        postRequestQueue.add(postJsonRequest);

    }
}
