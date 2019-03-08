package com.example.computer.moneymall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class CreditScore extends AppCompatActivity {
      private CardView home,personal,credit,other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_score);
        home = (CardView)findViewById(R.id.homes);
        personal = (CardView)findViewById(R.id.prsonal);
        credit = (CardView)findViewById(R.id.credit);
        other = (CardView)findViewById(R.id.card);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtoproduct();
            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtoproduct();
            }
        });
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtoproduct();
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtoproduct();
            }
        });
    }

    private void sendtoproduct() {
        Intent intent = new Intent(CreditScore.this,Product.class);
        startActivity(intent);
    }
}
