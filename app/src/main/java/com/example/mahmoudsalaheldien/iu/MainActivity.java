package com.example.mahmoudsalaheldien.iu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText editText;
    private String password = "";
    private boolean checkHide = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        editText = findViewById(R.id.password);
        List<String> list = new ArrayList<>();
        list.add("Boy");
        list.add("Girl");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void loveChat(View view) {
        String gender = spinner.getSelectedItem().toString();
        if (editText.getText() != null)
            password = editText.getText().toString();
        Intent intent = new Intent(this, LoveChatActivity.class);
        intent.putExtra("gender", gender);
        intent.putExtra("password", password);
        if (!password.equals("")) {
            editText.setText("");
            startActivity(intent);
            finish();
        } else
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
    }

    public void showPassword(View view) {
        if (checkHide) {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            checkHide = false;
        }else {
            editText.setTransformationMethod(null);
            checkHide = true;
        }
    }
}
