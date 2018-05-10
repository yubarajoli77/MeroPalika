package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softechfoundation.municipal.R;

public class Feedback extends AppCompatActivity {
    private EditText email,message;
    private Button feedBackSend;
    private String senderEmail,senderMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        initialize();
    }

    private void initialize() {
        email=findViewById(R.id.feedback_email);
        message=findViewById(R.id.feedback_message);
        feedBackSend=findViewById(R.id.feedback_submit);

        feedBackSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    senderEmail=email.getText().toString();
                    senderMessage=message.getText().toString();
                    sendMail(senderEmail,senderMessage);
                }

            }
        });

    }

    private void sendMail(String sender,String feedback) {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String[] recipients = new String[]{"yubarajoli77@gmail.com", "karkideependra58@gmail.com",};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback from "+sender);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, feedback);
        emailIntent.setType("text/plain");

        startActivity(Intent.createChooser(emailIntent, "Select mailing application"));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private boolean validate() {
        boolean validated = false;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Required");
            validated = false;
        } else if (TextUtils.isEmpty(message.getText().toString())) {
            message.setError("Required");
            validated = false;
        } else {
            validated = true;
        }
        return validated;
    }
}
