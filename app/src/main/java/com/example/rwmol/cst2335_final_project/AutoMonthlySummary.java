package com.example.rwmol.cst2335_final_project;

public class AutoMonthlySummary {
    private String date;
    private double cost;
    private double kilo;
    private double litre;

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setCost(Double cost){
        this.cost = cost;
    }

    public double getCost(){
        return cost;
    }

    public void setKilo(Double kilo){
        this.kilo = kilo;
    }

    public double getKilo(){
        return kilo;
    }

    public void setLitre(Double litre){
        this.litre = litre;
    }

    public double getLitre(){
        return litre;
    }
}
