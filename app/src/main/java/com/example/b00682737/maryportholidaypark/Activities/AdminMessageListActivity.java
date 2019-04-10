package com.example.b00682737.maryportholidaypark.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.ListView;

import com.example.b00682737.maryportholidaypark.Adapters.MessageListAdapter;
import com.example.b00682737.maryportholidaypark.FirebaseInfo;
import com.example.b00682737.maryportholidaypark.Models.MessageInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMessageListActivity extends BaseActivity {
    ListView lvData;
    ArrayList<MessageInfo> messageList = new ArrayList<>();
    MessageListAdapter messageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmessagelist);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        lvData = findViewById(R.id.lvData);
        messageListAdapter = new MessageListAdapter(mContext, messageList);
        lvData.setAdapter(messageListAdapter);

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseInfo.MESSAGES);

        showProgressDialog();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();

                // Read the Data
                messageList.clear();
                for (DataSnapshot infoSnip : dataSnapshot.getChildren()) {
                    if (infoSnip.exists()) {
                        MessageInfo request = infoSnip.getValue(MessageInfo.class);
                        String key = infoSnip.getKey();

                        if (request != null) {
                            request.key = key;
                            messageList.add(request);
                        }
                    }
                }

                messageListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                hideProgressDialog();
            }
        };

        mPostReference.addValueEventListener(postListener);
    }

    public void removeEvent(int position) {
        final MessageInfo eventInfo = messageList.get(position);

        // Remove message Data
        showProgressDialog();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(FirebaseInfo.MESSAGES).child(eventInfo.key).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                hideProgressDialog();

                if (databaseError == null) {
                    showAlert("Successfully removed message.");

                } else {
                    showToastMessage(databaseError.getMessage());
                }
            }
        });
    }
}
