package com.example.magzoo.data;

import android.graphics.Bitmap;

public class Animal {
    private int id;
    private String buttonid;
    private String name;
    private String cientificName;
    private String risk;
    private String summary;
    private String classs;
    private String order;
    private String family;
    private String length;
    private String height;
    private String weightF;
    private String weightM;
    private String habSummary;
    private String habActivity;
    private String habSocialLife;
    private String habDiet;
    private String repSummary;
    private String repType;
    private String repEggsOffSpring;
    private String repIncubationGestation;
    private String repSexualMaturity;
    private String conservation;
    private String distHabSummary;
    private String distHabCoordinateX;
    private String distHabCoordinateY;
    private Bitmap icon;

    //construtor usado na classe Colection
    public Animal(int id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    //construtor usado na classe ColectionDetails
    public Animal(int id, String buttonid, String name, String cientificName, String risk, String summary, String classs, String order, String family, String length, String height, String weightF, String weightM, String habSummary, String habActivity, String habSocialLife, String habDiet, String repSummary, String repType, String repEggsOffSpring, String repIncubationGestation, String repSexualMaturity, String conservation, String distHabSummary, String distHabCoordinateX, String distHabCoordinateY, Bitmap icon) {
        this.id = id;
        this.buttonid = buttonid;
        this.name = name;
        this.cientificName = cientificName;
        this.risk = risk;
        this.summary = summary;
        this.classs = classs;
        this.order = order;
        this.family = family;
        this.length = length;
        this.height = height;
        this.weightF = weightF;
        this.weightM = weightM;
        this.habSummary = habSummary;
        this.habActivity = habActivity;
        this.habSocialLife = habSocialLife;
        this.habDiet = habDiet;
        this.repSummary = repSummary;
        this.repType = repType;
        this.repEggsOffSpring = repEggsOffSpring;
        this.repIncubationGestation = repIncubationGestation;
        this.repSexualMaturity = repSexualMaturity;
        this.conservation = conservation;
        this.distHabSummary = distHabSummary;
        this.distHabCoordinateX = distHabCoordinateX;
        this.distHabCoordinateY = distHabCoordinateY;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getButtonid() {
        return buttonid;
    }

    public void setButtonid(String buttonid) {
        this.buttonid = buttonid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCientificName() {
        return cientificName;
    }

    public void setCientificName(String cientificName) {
        this.cientificName = cientificName;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeightF() {
        return weightF;
    }

    public void setWeightF(String weightF) {
        this.weightF = weightF;
    }

    public String getWeightM() {
        return weightM;
    }

    public void setWeightM(String weightM) {
        this.weightM = weightM;
    }

    public String getHabSummary() {
        return habSummary;
    }

    public void setHabSummary(String habSummary) {
        this.habSummary = habSummary;
    }

    public String getHabActivity() {
        return habActivity;
    }

    public void setHabActivity(String habActivity) {
        this.habActivity = habActivity;
    }

    public String getHabSocialLife() {
        return habSocialLife;
    }

    public void setHabSocialLife(String habSocialLife) {
        this.habSocialLife = habSocialLife;
    }

    public String getHabDiet() {
        return habDiet;
    }

    public void setHabDiet(String habDiet) {
        this.habDiet = habDiet;
    }

    public String getRepSummary() {
        return repSummary;
    }

    public void setRepSummary(String repSummary) {
        this.repSummary = repSummary;
    }

    public String getRepType() {
        return repType;
    }

    public void setRepType(String repType) {
        this.repType = repType;
    }

    public String getRepEggsOffSpring() {
        return repEggsOffSpring;
    }

    public void setRepEggsOffSpring(String repEggsOffSpring) {
        this.repEggsOffSpring = repEggsOffSpring;
    }

    public String getRepIncubationGestation() {
        return repIncubationGestation;
    }

    public void setRepIncubationGestation(String repIncubationGestation) {
        this.repIncubationGestation = repIncubationGestation;
    }

    public String getRepSexualMaturity() {
        return repSexualMaturity;
    }

    public void setRepSexualMaturity(String repSexualMaturity) {
        this.repSexualMaturity = repSexualMaturity;
    }

    public String getConservation() {
        return conservation;
    }

    public void setConservation(String conservation) {
        this.conservation = conservation;
    }

    public String getDistHabSummary() {
        return distHabSummary;
    }

    public void setDistHabSummary(String distHabSummary) {
        this.distHabSummary = distHabSummary;
    }

    public String getDistHabCoordinateX() {
        return distHabCoordinateX;
    }

    public void setDistHabCoordinateX(String distHabCoordinateX) {
        this.distHabCoordinateX = distHabCoordinateX;
    }

    public String getDistHabCoordinateY() {
        return distHabCoordinateY;
    }

    public void setDistHabCoordinateY(String distHabCoordinateY) {
        this.distHabCoordinateY = distHabCoordinateY;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
