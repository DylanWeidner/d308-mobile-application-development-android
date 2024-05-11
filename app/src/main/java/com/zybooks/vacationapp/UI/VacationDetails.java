package com.zybooks.vacationapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.vacationapp.R;
import com.zybooks.vacationapp.database.Repository;
import com.zybooks.vacationapp.entitites.Excursion;
import com.zybooks.vacationapp.entitites.Vacation;

import java.util.ArrayList;
import java.util.List;


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
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacationsave) {
            Vacation vacation;
            if (vacationId == 0) {
                if (repository.getAllVacations().isEmpty()) vacationId = 1;
                else
                    vacationId = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationId() + 1;
                vacation = new Vacation(vacationId, vacationTitle.getText().toString(), vacationHotel.getText().toString(), vacationStartDate.getText().toString(), vacationEndDate.getText().toString());
                repository.insertVacation(vacation);
                this.finish();
                Toast.makeText(this, "Vacation Saved", Toast.LENGTH_LONG).show();
            }
            else {

                vacation = new Vacation(vacationId, vacationTitle.getText().toString(), vacationHotel.getText().toString(), vacationStartDate.getText().toString(), vacationEndDate.getText().toString());
                repository.updateVacation(vacation);
                this.finish();
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
        return true;
    }

}