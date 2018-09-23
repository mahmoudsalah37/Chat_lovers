package com.example.mahmoudsalaheldien.iu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.ArrayList;
import java.util.List;

public class LoveChatActivity extends AppCompatActivity {
    private EmojiEditText message_editText;
    private String password;
    private String gender;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Values> list;
    private ProgressBar progressBar;
    EmojiPopup emojiPopup;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                startActivity(new Intent(LoveChatActivity.this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmojiManager.install(new GoogleEmojiProvider());
        setContentView(R.layout.activity_love_chat);
        View decorView = getWindow().getDecorView();
        message_editText = findViewById(R.id.message_editText);
        emojiPopup = EmojiPopup.Builder.fromRootView(decorView).build(message_editText);
        list = new ArrayList<>();
        gender = getIntent().getExtras().getString("gender");
        password = getIntent().getExtras().getString("password");
        mRecyclerView = findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        FirebaseDatabase.getInstance().getReference().child(password).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("gender").getValue() != null && data.child("message").getValue() != null)
                        list.add(new Values(data.child("gender").getValue().toString(), data.child("message").getValue().toString()));
                }
                if (list.size() > 0)
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(View view) {
        if (message_editText.getText() != null && !message_editText.getText().toString().trim().equals("")) {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            String message = message_editText.getText().toString().trim();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(password).push();
            databaseReference.child("gender").setValue(gender);
            databaseReference.child("message").setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    message_editText.setText("");
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void emoji(View view) {
        if (emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        } else {
            emojiPopup.toggle();
        }
    }
}
