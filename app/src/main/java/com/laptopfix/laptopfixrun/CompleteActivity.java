package com.laptopfix.laptopfixrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegisterComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        btnRegisterComplete = findViewById(R.id.btnRegisterComplete);
        btnRegisterComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegisterComplete:
                Intent intent = new Intent(CompleteActivity.this, HomeCustomer.class);
                intent.putExtra("section", R.id.nav_establecimiento);
                startActivity(intent);
                finish();
                break;
        }
    }
}
