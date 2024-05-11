package com.zybooks.vacationapp.UI;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

import java.util.List;

public class VacationList extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                intent.putExtra("vacationId", 0);
                intent.putExtra("title", "New Vacation");
                intent.putExtra("hotel", "Enter a Hotel");
                intent.putExtra("startDate", "Enter Start Date");
                intent.putExtra("endDate", "Enter End Date");
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.vacationListRecyclerView);
        repository = new Repository(getApplication());
        List<Vacation> vacations = repository.getAllVacations();
        final VacationAdapter adapter = new VacationAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setVacations(vacations);

        // Test to see if the intent is working
        //System.out.println(getIntent().getStringExtra("test"));
    }

    @Override
    protected void onResume(){
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.vacationListRecyclerView);
        repository = new Repository(getApplication());
        List<Vacation> vacations = repository.getAllVacations();
        final VacationAdapter adapter = new VacationAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setVacations(vacations);
        // make a toast to display a message to the user
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        repository = new Repository(getApplication());
        int id = item.getItemId();
        if (id == R.id.SampleItem){
           // Toast.makeText(VacationList.this, "Put in Sample Code Here", Toast.LENGTH_LONG).show();
            Vacation vacation = new Vacation(0, "Vacation 1", "Hotel 1", "2021-01-01", "2021-01-07");
            repository.insertVacation(vacation);
            Excursion excursion = new Excursion(0, "Excursion 1", "2021-01-01", 1 );
            repository.insertExcursion(excursion);
            onResume();
            Toast.makeText(VacationList.this, "New Blank Vacation Added", Toast.LENGTH_LONG).show();
            return true;
        }
        if (item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return true;
    }
}