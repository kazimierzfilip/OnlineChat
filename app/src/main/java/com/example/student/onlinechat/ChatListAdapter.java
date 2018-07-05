package com.example.student.onlinechat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Kazik on 2018-07-05.
 */

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mDataSnapshotList;

    private ChildEventListener mListener = new ChildEventListener() {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

            mDataSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String displayName) {
        mActivity = activity;
        mDisplayName = displayName;
        mDataSnapshotList = new ArrayList<>();
        mDatabaseReference = databaseReference.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mDataSnapshotList.size();
    }

    @Override
    public Message getItem(int position) {
        DataSnapshot snapshot = mDataSnapshotList.get(position);
        return snapshot.getValue(Message.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_row_chat, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.authorName = convertView.findViewById(R.id.author);
            holder.body = convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams)holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }

        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();

        boolean isMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe, holder);

        String author = message.getAuthor();
        holder.authorName.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);

        return convertView;
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder holder){

        if(isItMe){
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void cleanup(){
        mDatabaseReference.removeEventListener(mListener);
    }

}
