package com.example.notepad;

import static android.content.Context.MODE_PRIVATE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    TextView readingView;
    TextView appRestartsView;
    private static final String NUMBER_OF_TIMES_RUN_KEY = "NUMBER_OF_TIMES_RUN_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readingView = findViewById(R.id.displayView);
        appRestartsView = findViewById(R.id.applicationRestarts);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        // Read
        int defaultValue = 0;
        //All this is for that counting thing on how many times a app has been opened.
        int howManyTimesBeenRun = 0;
        howManyTimesBeenRun = sharedPreferences.getInt(NUMBER_OF_TIMES_RUN_KEY, defaultValue);

        // First time message
        if (howManyTimesBeenRun == 0){
            Toast.makeText(MainActivity.this, "Welcome to your new Magic Notepad", Toast.LENGTH_SHORT).show();
        }
        howManyTimesBeenRun++;

        // Write
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NUMBER_OF_TIMES_RUN_KEY, howManyTimesBeenRun);
        editor.commit();
        appRestartsView.setText(String.valueOf(howManyTimesBeenRun));

        // Now the code is to read and write file.
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(MainActivity.this, "On Pause Method", Toast.LENGTH_SHORT).show();
        try {
            saveTextFile(readingView.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "On Resume Method", Toast.LENGTH_SHORT).show();
        readingView.setText(getTextFile());
    }

    private static final String DATA_FILE = "my_file";
    //Reading and Writing
    public String getTextFile() {
        FileInputStream fileInputStream = null;
        String fileData = null;
        Toast.makeText(this, "Get Text File Started", Toast.LENGTH_SHORT).show();
        try {
            fileInputStream = openFileInput(DATA_FILE);
            int size = fileInputStream.available();
            byte buffer[] = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            fileData = new String(buffer, "UTF-8");
        } catch (Exception e){
            Log.d("FILE","Exception "+e);
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, "Data Returned", Toast.LENGTH_SHORT).show();
        return fileData;
    }
    public void saveTextFile(String content) throws IOException {
        FileOutputStream fileOutputStream = null;
        Toast.makeText(this, "Save Text File Method", Toast.LENGTH_SHORT).show();
        try {
            fileOutputStream = openFileOutput(DATA_FILE, MODE_PRIVATE);
            fileOutputStream.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(MainActivity.this, "File Saved", Toast.LENGTH_SHORT).show();
    }


}