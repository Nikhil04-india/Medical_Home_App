package com.example.medicalhome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LabTestBookActivity extends AppCompatActivity {

    EditText edname, edaddress, edcontact, edpincode;
    Button btnbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lab_test_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edname = findViewById(R.id.editTextLTBFullName);
        edaddress = findViewById(R.id.editTextLTBAddress);
        edcontact = findViewById(R.id.editTextLTBContact);
        edpincode = findViewById(R.id.editTextLTBPinCode);
        btnbook = findViewById(R.id.buttonLTBBook);

        Intent intent = getIntent();
        String[] price = intent.getStringExtra("price").toString().split(java.util.regex.Pattern.quote(":"));
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedpreferences.getString("username", "").toString();

                if (username.isEmpty() || edname.getText().toString().isEmpty() || edaddress.getText().toString().isEmpty() || edcontact.getText().toString().isEmpty() || edpincode.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                Database db = new Database(getApplicationContext(), "medicalcare", null, 1);
                db.addOrder(username, edname.getText().toString(), edaddress.getText().toString(), edcontact.getText().toString(), Integer.parseInt(edpincode.getText().toString()), date.toString(), time.toString(), Float.parseFloat(price[1].toString()), "lab");
                db.removeCart(username, "lab");

                Toast.makeText(getApplicationContext(), "Your Booking is done Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LabTestBookActivity.this, HomeActivity.class));
            }
        });
    }
}
