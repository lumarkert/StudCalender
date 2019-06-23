package com.example.lukas.studcalender;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    Date currentDate;
    ArrayList<Kalender> kalenders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        ActionBar supportActionBar = getSupportActionBar();

        //Get current day and set it as title
        DateFormat dateFormat = new SimpleDateFormat("EE dd.MM.yyyy");
        currentDate = new Date();
        supportActionBar.setTitle(dateFormat.format(currentDate));


        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCalendarActivity();
            }
        });

        kalenders = new ArrayList<>();
        updateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("EE dd.MM.yyyy");
        ActionBar supportActionBar = getSupportActionBar();
        switch (item.getItemId()) {

            case R.id.action_add:
                return true;

            case R.id.action_showAll:

                return true;

            case R.id.action_nextDay:
                cal.setTime(currentDate);
                cal.add(Calendar.DAY_OF_YEAR, 1);
                currentDate = cal.getTime();
                supportActionBar.setTitle(dateFormat.format(currentDate));
                return true;

            case R.id.action_previousDay:
                cal.setTime(currentDate);
                cal.add(Calendar.DAY_OF_YEAR, -1);
                currentDate = cal.getTime();
                supportActionBar.setTitle(dateFormat.format(currentDate));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 69 && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                String profileName = resultData.getStringExtra("profile");
                Uri uri = Uri.parse(resultData.getStringExtra("result"));
                createCalendar(uri, profileName);
            }
        }
    }

    public void addCalendarActivity() {
        Intent intent = new Intent(this, AddCalendarActivity.class);
        startActivityForResult(intent, 69);
    }

    private void createCalendar(Uri uri, String profileName) {
        Kalender kalender = new Kalender(profileName);
        System.out.println("beginsparsing");
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            if (br.readLine().contains("BEGIN:VCALENDAR") && br.readLine().contains("VERSION:2.0")) {
                beginParsing(br, kalender);
            }
            kalenders.add(kalender);
            updateListView();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void beginParsing(BufferedReader br, Kalender kal) throws IOException {
        String line = null;
        Termin t = null;
        while ((line = br.readLine()) != null) {
            if (line.contains("BEGIN:VEVENT")) {
                t = parseTermin(br);
                kal.addTermin(t);
            }
        }
    }

    public void updateListView() {
        ListView listViewTermins = findViewById(R.id.listViewTermins);
        TerminArrayAdapter adapter = new TerminArrayAdapter(kalenders, this);
        listViewTermins.setAdapter(adapter);
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    private Termin parseTermin(BufferedReader br) throws IOException {
        String line = null;
        String description = null;
        String room = null;
        Boolean terminCompleted = false;
        String[] stringArray;
        int year, month, day, hrs, minutes;
        Date d = null;
        while (!terminCompleted && (line = br.readLine()) != null) {
            if (line.contains("SUMMARY")) {
                //parse Title of appointment
                stringArray = line.split("DE:");
                description = stringArray[1];
                if (!((line = br.readLine()).contains("DESCRIPTION"))) {
                    line = line.substring(1);
                    description = description + line;
                }
            } else if (line.contains("LOCATION")) {
                stringArray = line.split("DE:");
                room = stringArray[1];
            } else if (line.contains("DTSTART")) {

                stringArray = line.split("DTSTART:");
                stringArray = stringArray[1].split("T");

                line = stringArray[0];
                System.out.println(line);
                year = Integer.parseInt(line.substring(0, 4));
                month = Integer.parseInt(line.substring(4, 6));
                day = Integer.parseInt(line.substring(6, 8));

                line = stringArray[1];
                hrs = Integer.parseInt(line.substring(0, 2));
                System.out.println(hrs);
                minutes = Integer.parseInt(line.substring(2, 4));

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
                cal.set(Calendar.DAY_OF_MONTH, day);
                cal.set(Calendar.HOUR_OF_DAY, hrs + 2);
                cal.set(Calendar.MINUTE, minutes);
                cal.set(Calendar.SECOND, 0);
                d = cal.getTime();
                terminCompleted = true;

            }
        }
        return new Termin(d, description, room);
    }
}

//TODO: ARRAYADAPTER UND LISTVIEW: https://stackoverflow.com/questions/12405575/using-a-listadapter-to-fill-a-linearlayout-inside-a-scrollview-layout