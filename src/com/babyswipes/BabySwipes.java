package com.babyswipes;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BabySwipes extends Activity implements OnClickListener {
    private static final String TAG = "BabySwipes";
    Button trainingButton;
    Button dataButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_swipes);
        
        trainingButton = (Button) findViewById(R.id.trainingButton);
        trainingButton.setOnClickListener(this);
        dataButton = (Button) findViewById(R.id.dataButton);
        dataButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_baby_swipes, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()) {
        case R.id.trainingButton:
            Log.d(TAG, "trainingButton onClicked");
            i = new Intent(this, TrainingActivity.class);
            startActivity(i);
            break;
        case R.id.dataButton:
            Log.d(TAG, "dataButton onCLicked");
            i = new Intent(this, DataActivity.class);
            startActivity(i);
            break;
        }
    }

    
}
