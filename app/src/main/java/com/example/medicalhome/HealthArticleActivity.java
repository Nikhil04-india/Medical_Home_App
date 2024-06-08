package com.example.medicalhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SimpleTimeZone;

public class HealthArticleActivity extends AppCompatActivity {

    private String [][] health_details =
            {
                    {"Walking Daily", "","","","Click more details"},
                    {"Home care of Covid-19" , "","","","Click more details"},
                    {"Stop smoking" , "","","","Click more details"},
                    {"Henstrual cramps" , "","","","Click more details"},
                    {"Healthy Gut" , "","","","Click more details"}

            };

    private int [] images ={
            R.drawable.walking,
            R.drawable.covid,
            R.drawable.health3,
            R.drawable.menstrual,
            R.drawable.healthy
    };

    HashMap<String , String> item;
    ArrayList list;
    SimpleAdapter sa;
    Button btnBack;
    ListView lst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_health_article);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lst = findViewById(R.id.listViewHA);
        btnBack =  findViewById(R.id.buttonHABack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HealthArticleActivity.this , HomeActivity.class));
            }
        });

        list = new ArrayList();
        for (int i =0 ; i<health_details.length; i++){
            item=new HashMap<String , String>();
            item.put("line1" , health_details[i][0]);
            item.put("line2" , health_details[i][1]);
            item.put("line3" , health_details[i][2]);
            item.put("line4" , health_details[i][3]);
            item.put("line5" , health_details[i][4]);
            list.add(item);
        }

        sa=new SimpleAdapter(this, list ,
                R.layout.multi_lines,
                new String[]{"line1","line2","line3","line4","line5",},
                new int[]{R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e,});

        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent it = new Intent(HealthArticleActivity.this , HealthArticleDetailsActivity.class);
                    it.putExtra("text1", health_details[i][0]);
                    it.putExtra("text2", images[i]);
                    startActivity(it);
            }
        });
    }
}