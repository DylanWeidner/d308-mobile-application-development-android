package com.zybooks.vacationapp.entitites;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excusrionId;

    private String excursionTitle;

    private String excursionDate;

    private int vacationId;

    public Excursion(int excusrionId, String excursionTitle, String excursionDate, int vacationId) {
        this.excusrionId = excusrionId;
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.vacationId = vacationId;
    }

    public int getExcusrionId() {
        return excusrionId;
    }

    public void setExcusrionId(int excusrionId) {
        this.excusrionId = excusrionId;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}
