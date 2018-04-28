package com.softechfoundation.municipal.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.softechfoundation.municipal.R;

public class Setting extends AppCompatActivity {
private Button saveVoiceSetting,defaultSetting;
private Switch voiceOnOffSwitch;
private SeekBar pitch,speed;
private  int pitcValue;
private int speedValue;
private boolean isOn=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        saveVoiceSetting=findViewById(R.id.save_voice_setting_btn);
        defaultSetting=findViewById(R.id.set_default_btn);
        voiceOnOffSwitch=findViewById(R.id.voice_on_off);
        pitch=findViewById(R.id.voice_pitch_seekBar);
        speed=findViewById(R.id.voice_speed_seekBar);

        final SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
        editor.apply();
        
        defaultSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceOnOffSwitch.setChecked(true);
                speed.setProgress(50);
                pitch.setProgress(50);
                SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
                editor.putFloat("pitch",(float)1.0);
                editor.putFloat("speed",(float)1.0);
                editor.putBoolean("switchState",true);
                editor.apply();
                editor.commit();
                Toast.makeText(Setting.this, "All set to Default", Toast.LENGTH_SHORT).show();
                defaultSetting.setEnabled(false);
            }
        });

        saveVoiceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float actualSpeed=(float)speedValue/50;
                float actualPitch=(float)pitcValue/50;
                SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
                editor.putFloat("speed",actualSpeed);
                editor.putFloat("pitch",actualPitch);
                editor.putBoolean("switchState",isOn);
                editor.apply();
                editor.commit();

                saveVoiceSetting.setEnabled(false);
                defaultSetting.setEnabled(true);
            }
        });

        voiceOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isOn=true;
                }
                else {
                    isOn=false;
                }
                saveVoiceSetting.setEnabled(true);
            }
        });

        pitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pitcValue=progress;
                saveVoiceSetting.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(Setting.this, "Pitch value set to "+pitcValue, Toast.LENGTH_SHORT).show();
                pitch.setProgress(pitcValue);
            }
        });

        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedValue=progress;
                saveVoiceSetting.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(Setting.this, "Pitch value set to "+speedValue, Toast.LENGTH_SHORT).show();
                speed.setProgress(speedValue);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        boolean switchValue=sharedPreferences.getBoolean("switchState",true);
        float pitches=sharedPreferences.getFloat("pitch",1);
        float speeds=sharedPreferences.getFloat("speed",1);
        int pitchValue= (int) (pitches*50);
        int speedValue=(int)(speeds*50);
        voiceOnOffSwitch.setChecked(switchValue);
        pitch.setProgress(pitchValue);
        speed.setProgress(speedValue);
        super.onStart();
    }
}
