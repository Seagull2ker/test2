package chapter.android.aweme.ss.com.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class chatroom extends AppCompatActivity {
    private static final String TAG = "liuzhengqin";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "1111: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        Intent intent = getIntent();
        String message = intent.getStringExtra("user");
        TextView textView = findViewById(R.id.tv_content_info);
        textView.setText(message);
    }
}
