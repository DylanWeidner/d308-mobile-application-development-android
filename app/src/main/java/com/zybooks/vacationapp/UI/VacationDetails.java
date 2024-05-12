package com.zybooks.vacationapp.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.net.ParseException;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.vacationapp.R;
import com.zybooks.vacationapp.database.Repository;
import com.zybooks.vacationapp.entitites.Excursion;
import com.zybooks.vacationapp.entitites.Vacation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VacationDetails extends AppCompatActivity {

    int vacationId;
    String title;
    String hotel;
    String startDate;
    String endDate;
    TextView vacationIdTextView;
    EditText vacationTitle;
    EditText vacationHotel;
    EditText vacationStartDate;
    EditText vacationEndDate;


    Repository repository;

    DatePickerDialog.OnDateSetListener starterDate;
    DatePickerDialog.OnDateSetListener enderDate;
    final Calendar myCalendar=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        // get extras from intent
        vacationId = getIntent().getIntExtra("vacationId", 0);
        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        // get the edit text views and text view
        vacationIdTextView = findViewById(R.id.TextID);
        vacationTitle = findViewById(R.id.editText2);
        vacationHotel = findViewById(R.id.editText3);
        vacationStartDate = findViewById(R.id.editText4);
        vacationEndDate = findViewById(R.id.editText5);

        // apply the values from the intents to the views
        vacationIdTextView.setText(String.valueOf(vacationId));
        vacationTitle.setText(title);
        vacationHotel.setText(hotel);
        vacationStartDate.setText(startDate);
        vacationEndDate.setText(endDate);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("ExcursionVacationId", vacationId);
                intent.putExtra("VacationEndDate", endDate);
                intent.putExtra("VacationStartDate", startDate);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.vacationDetailsRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter adapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getAllExcursions()) {
            if (excursion.getVacationId() == vacationId) {
                filteredExcursions.add(excursion);
            }
        }
        adapter.setExcursions(filteredExcursions);

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        String currentDate=sdf.format(new Date());
        //vacationStartDate.setText(currentDate);
        //vacationEndDate.setText(currentDate);
        vacationStartDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Date date;
                String info=vacationStartDate.getText().toString();
                try{
                    myCalendar.setTime(sdf.parse(info));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this,starterDate,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        vacationEndDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Date date;
                String info=vacationEndDate.getText().toString();
                try{
                    myCalendar.setTime(sdf.parse(info));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this,enderDate,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        starterDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateStartLabel();
            }

        };

        enderDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateEnderLabel();
            }

        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.vacationDetailsRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter adapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getAllExcursions()) {
            if (excursion.getVacationId() == vacationId) {
                filteredExcursions.add(excursion);
            }
        }
        adapter.setExcursions(filteredExcursions);
    }

    private void updateStartLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        vacationStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEnderLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        vacationEndDate.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);



        if (item.getItemId() == R.id.vacationsave) {
            Vacation vacation;

            try {
                Date vacayStart = sdf.parse(vacationStartDate.getText().toString());
                Date vacayEnd = sdf.parse(vacationEndDate.getText().toString());

                if (vacayStart.after(vacayEnd))  {
                    Toast.makeText(this, "Start date needs to be before end date", Toast.LENGTH_LONG).show();
                    return false;
                }
                else {
                    if (vacationId == 0) {
                        if (repository.getAllVacations().isEmpty()) vacationId = 1;
                        else
                            vacationId = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationId() + 1;
                        vacation = new Vacation(vacationId, vacationTitle.getText().toString(), vacationHotel.getText().toString(), vacationStartDate.getText().toString(), vacationEndDate.getText().toString());
                        repository.insertVacation(vacation);
                        Toast.makeText(this, "Vacation Saved", Toast.LENGTH_LONG).show();
                        this.finish();
                    }
                    else {

                        vacation = new Vacation(vacationId, vacationTitle.getText().toString(), vacationHotel.getText().toString(), vacationStartDate.getText().toString(), vacationEndDate.getText().toString());
                        repository.updateVacation(vacation);
                        Toast.makeText(this, "Vacation Saved", Toast.LENGTH_LONG).show();
                        this.finish();
                    }
                }

            } catch (Exception e) {
                Toast.makeText(this, "Invalid data entered.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        // delete vacation, only if there are no excursions attached to it.
        else if (item.getItemId() == R.id.vacationdelete) {
            if (!repository.getAllVacations().isEmpty() && vacationId != 0){
                Vacation vacation = new Vacation(vacationId, vacationTitle.getText().toString(), vacationHotel.getText().toString(), vacationStartDate.getText().toString(), vacationEndDate.getText().toString());
                List<Excursion> excursions = repository.getAssociatedExcursions(vacation.getVacationId());
                if(excursions.isEmpty()) {
                    repository.deleteVacation(vacation);
                    this.finish();
                    Toast.makeText(this, "Vacation Deleted", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Vacation has excursions, delete excursions first", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "Cannot delete an unsaved vacation.", Toast.LENGTH_LONG).show();
            }
        }
        // back button wasn't working so I added this
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        else if (item.getItemId() == R.id.Share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Vacation Details");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Title: " + vacationTitle.getText().toString() + "\n" + "Hotel: " + vacationHotel.getText().toString() + "\n" + "Start Date: " + vacationStartDate.getText().toString() + "\n" + "End Date: " + vacationEndDate.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        else if (item.getItemId() == R.id.SetVacationAlerts){

            String startDate = vacationStartDate.getText().toString();
            String endDate = vacationEndDate.getText().toString();

            // already defined up top
            //String dateFormat = "MM/dd/yy";
            //SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            //String currentDate=sdf.format(new Date());
            Date startDateNotify = null;
            Date endDateNotify = null;

            try {
                // turn strings to dates
                startDateNotify = sdf.parse(startDate);
                endDateNotify = sdf.parse(endDate);

                // get the time in milliseconds
                Long startTriggerTime = startDateNotify.getTime();
                Long endTriggerTime = endDateNotify.getTime();

                // create the intent for start date notification
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.setAction("ACTION_START_VACATION");
                intent.putExtra("vacationTitle", vacationTitle.getText().toString() + " is starting today.");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, startTriggerTime, sender);

                // create the intent for end date notification
                Intent intent2 = new Intent(VacationDetails.this, MyReceiver.class);
                intent2.setAction("ACTION_END_VACATION");
                intent2.putExtra("vacationTitle", vacationTitle.getText().toString() + " is ending today.");
                PendingIntent endSender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent2, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, endTriggerTime, endSender);

            } catch (Exception e) {

                // notify user if there is an error+
                e.printStackTrace();
                Toast.makeText(this, "Check Your Dates and try again.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return true;
    }




}