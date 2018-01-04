package com.example.rwmol.cst2335_final_project;

/**
 * Stores the Purchase Information input by the user.
 *
 * @author Ryan Molitor
 * @version 1.2
 */
public class PurchaseInfo {

    private String date;
    private String cost;
    private String kilo;
    private String litre;

    /**
     * @return the local date String reference.
     */
    public String getDate(){
        return date;
    }

    /**
     * Sets the local date String reference to the name argument.
     * @param date the "date" value of the corresponding row in the Database.
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * @return the local cost String reference.
     */
    public String getCost(){
        return cost;
    }

    /**
     * Sets the local cost String reference to the name argument.
     * @param cost the "date" value of the corresponding row in the Database.
     */
    public void setCost(String cost){
        this.cost = cost;
    }

    /**
     * @return the local kilometers String reference.
     */
    public String getKilo(){
        return kilo;
    }

    /**
     * Sets the local kilo String reference to the name argument.
     * @param kilo the "kilo" value of the corresponding row in the Database.
     */
    public void setKilo(String kilo){
        this.kilo = kilo;
    }

    /**
     * @return the local litres String reference.
     */
    public String getLitre(){
        return litre;
    }

    /**
     * Sets the local litre String reference to the name argument.
     * @param litre the "litre" value of the corresponding row in the Database.
     */
    public void setLitre(String litre){
        this.litre = litre;
    }
}
