package com.zybooks.vacationapp.database;


import android.app.Application;

import com.zybooks.vacationapp.dao.ExcursionDAO;
import com.zybooks.vacationapp.dao.VacationDAO;
import com.zybooks.vacationapp.entitites.Excursion;
import com.zybooks.vacationapp.entitites.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExcursionDAO mexcursionDAO;

    private VacationDAO mvacationDAO;

    private List<Vacation> mAllVacations;

    private List<Excursion> mAllExcursions;

    private static int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mvacationDAO = db.vacationDAO();
        mexcursionDAO = db.excursionDAO();
    }

    public List<Vacation> getAllVacations(){
        databaseExecutor.execute(() -> {
            mAllVacations = mvacationDAO.getAllVacations();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllVacations;
    }

    public void insertVacation(Vacation vacation){
        databaseExecutor.execute(() -> {
            mvacationDAO.insertVacation(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteVacation(Vacation vacation){
        databaseExecutor.execute(() -> {
            mvacationDAO.deleteVacation(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateVacation(Vacation vacation){
        databaseExecutor.execute(() -> {
            mvacationDAO.updateVacation(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Excursion> getAllExcursions(){
        databaseExecutor.execute(() -> {
            mAllExcursions = mexcursionDAO.getAllExcursions();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }
    public void insertExcursion(Excursion excursion){
        databaseExecutor.execute(() -> {
            mexcursionDAO.insertExcursion(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteExcursion(Excursion excursion){
        databaseExecutor.execute(() -> {
            mexcursionDAO.deleteExcursion(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateExcursion(Excursion excursion){
        databaseExecutor.execute(() -> {
            mexcursionDAO.updateExcursion(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Excursion> getAssociatedExcursions(int vac){
        databaseExecutor.execute(() -> {
            mAllExcursions = mexcursionDAO.getAssociatedExcursions(vac);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }

    public Vacation getVacationById(int vacationId) {
        return mvacationDAO.getVacationById(vacationId);
    }
    public Excursion getExcursionById(int excursionId) {
        return mexcursionDAO.getExcursionById(excursionId);
    }
}



