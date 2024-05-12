package com.zybooks.vacationapp.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zybooks.vacationapp.R;
import com.zybooks.vacationapp.database.Repository;
import com.zybooks.vacationapp.entitites.Excursion;
import com.zybooks.vacationapp.entitites.Vacation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    int excursionId;
    String excursionTitle;
    String excursionDate;
    int vacationId;
    TextView excursionIDTextView;
    TextView excursionTitleTextView;
    EditText excursionDateEditText;
    EditText vacationIdEditText;
    String vacationEndDate;
    String vacationStartDate;
    DatePickerDialog.OnDateSetListener starterDate;
    final Calendar myCalendar = Calendar.getInstance();
    Repository repository = new Repository(getApplication());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);

        // get excursion values from intent
        excursionId = getIntent().getIntExtra("excursionId", 0);
        excursionTitle = getIntent().getStringExtra("excursionName");
        excursionDate = getIntent().getStringExtra("excursionDate");
        vacationId = getIntent().getIntExtra("ExcursionVacationId", 0);

        // set up text views
        excursionIDTextView = findViewById(R.id.TextID);
        excursionTitleTextView = findViewById(R.id.editText2);
        excursionDateEditText = findViewById(R.id.editText4);
        vacationIdEditText = findViewById(R.id.editText5);

        // set up vacation start and end date
        vacationStartDate = getIntent().getStringExtra("VacationStartDate");
        vacationEndDate = getIntent().getStringExtra("VacationEndDate");

        // set text views to excursion values
        excursionIDTextView.setText(String.valueOf(excursionId));
        excursionTitleTextView.setText(excursionTitle);
        excursionDateEditText.setText(excursionDate);
        vacationIdEditText.setText(String.valueOf(vacationId));


        // get vacation start and end date
        Repository repository = new Repository(getApplication());
        if (vacationId != 0) {
            Vacation vacation = repository.getVacationById(vacationId);
            vacationStartDate = vacation.getVacationStartDate();
            vacationEndDate = vacation.getVacationEndDate();
        }

        System.out.println("onCreate: Vacation Start Date: " + vacationStartDate + " Vacation End Date: " + vacationEndDate);
        //Toast.makeText(this, "Vacation Start Date: " + vacationStartDate + " Vacation End Date: " + vacationEndDate, Toast.LENGTH_LONG).show();


        /***************** Logic for date picker*******************/

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        String currentDate = sdf.format(new Date());
        //vacationStartDate.setText(currentDate);
        //vacationEndDate.setText(currentDate);
        excursionDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date date;
                String info = excursionDateEditText.getText().toString();
                try {
                    myCalendar.setTime(sdf.parse(info));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, starterDate, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        starterDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {


                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDateLabel();
            }

        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        if (item.getItemId() == R.id.excursionsave) {
            Vacation vacation;

            try {
                Date vacayStart = sdf.parse(vacationStartDate);
                Date vacayEnd = sdf.parse(vacationEndDate);

                Date excursionDate = sdf.parse(excursionDateEditText.getText().toString());

                if (excursionDate.after(vacayEnd) || excursionDate.before(vacayStart)) {
                    Toast.makeText(this, "Excursion date needs to be during the vacation", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    if (vacationId == 0) {
                        Toast.makeText(this, "Save Vacation before adding an excursion.", Toast.LENGTH_LONG).show();
                    }

                    if (repository.getExcursionById(excursionId) != null) {

                        Excursion excursion = new Excursion(excursionId, excursionTitleTextView.getText().toString(), excursionDateEditText.getText().toString(), vacationId);
                        repository.updateExcursion(excursion);
                        Toast.makeText(this, "Excursion Saved", Toast.LENGTH_LONG).show();
                        this.finish();
                    } else {
                        Excursion excursion = new Excursion(0, excursionTitleTextView.getText().toString(), excursionDateEditText.getText().toString(), vacationId);
                        repository.insertExcursion(excursion);
                        Toast.makeText(this, "Excursion Saved", Toast.LENGTH_LONG).show();
                        this.finish();
                    }

                }
            }
            catch (Exception e) {
                Toast.makeText(this, "Invalid data entered.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                Log.d("Dylan", e.toString());
            }
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            // go back to the vacation details activity
            onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.excursionDelete) {
            // delete the excursion
            if (excursionId == 0){
                Toast.makeText(this, "Nothing To Delete", Toast.LENGTH_LONG).show();
                this.finish();
            }
            else {
                Excursion excursion = new Excursion(excursionId, excursionTitleTextView.getText().toString(), excursionDateEditText.getText().toString(), vacationId);
                repository.deleteExcursion(excursion);
                Toast.makeText(this, "Excursion Deleted", Toast.LENGTH_LONG).show();
                this.finish();
            }
            return true;
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }


    private void updateDateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdfa = new SimpleDateFormat(myFormat, Locale.US);
        excursionDateEditText.setText((sdfa.format(myCalendar.getTime())).toString());
    }

}



