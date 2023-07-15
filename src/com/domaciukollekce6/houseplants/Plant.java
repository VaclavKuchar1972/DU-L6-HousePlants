package com.domaciukollekce6.houseplants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Úkol lekce 6 bod 2 - 1. Úprava třídy Plant pro řazení rostlin podle názvu pomocí Collections.sort
public class Plant implements Comparable<Plant> {

    private String plantName = "";
    private String plantNote = "";
    private LocalDate plantPlantingDate;
    private LocalDate plantLastWateringDate;
    private int plantNormalWateringFrequency;


    // NO NEVÍM, SNAD to chápu!? :D Možná.

    // Konstruktor 1 - se všemi adtibuty
    public Plant(String plantName, String plantNote, LocalDate plantPlantingDate, LocalDate plantLastWateringDate,
                 int plantNormalWateringFrequency) throws PlantException {
        this.plantName = plantName;
        this.plantNote = plantNote;
        this.plantPlantingDate = plantPlantingDate;
        this.setPlantLastWateringDate(plantLastWateringDate);
        this.setPlantNormalWateringFrequency(plantNormalWateringFrequency);
    }
    // Konstruktor 2 - "plantNote" nastaví jako prázdný řetězec a "plantLastWateringDate" nastaví jako aktuální datum

    // AHA!!! do atributů Plant píši ty který jsou vyjádřeny neurčitě názvem objktu (proměnné) a ty ktré určuji natvrdo
    // tam nepíšu!
    public Plant(String plantName, LocalDate plantPlantingDate, int plantNormalWateringFrequency)
            throws PlantException {
        this(plantName, "", plantPlantingDate, LocalDate.now(), plantNormalWateringFrequency);
    }
    // Konstruktor 3 - "plantNote" nastaví jako prázdný řetězec, "plantLastWateringDate" nastaví jako aktuální datum,
    // "normalWateringFrequencyP" nastaví na hodnotu 7 a "plantPlantingDate" nastaví jako aktuální datum
    public Plant(String plantName, LocalDate plantPlantingDate) throws PlantException {
        this(plantName, "", plantPlantingDate, LocalDate.now(), 7);
    }

    public String getWateringInfo () {
        return "   Název rostliny: " + plantName + "   Datum poslední zálivky: "
                + plantLastWateringDate.format(DateTimeFormatter.ofPattern("d.M.yyyy"))
                + "   Datum doporučené další zálivky: "
                + plantLastWateringDate.plusDays(plantNormalWateringFrequency)
                .format(DateTimeFormatter.ofPattern("d.M.yyyy"));
    }

    // Všechny přístupové metody
    public String getPlantName() {return plantName;}
    public void setPlantName(String plantName) {this.plantName = plantName;}
    public String getPlantNote() {return plantNote;}
    public void setPlantNote(String plantNote) {this.plantNote = plantNote;}
    public LocalDate getPlantPlantingDate() {return plantPlantingDate;}
    public void setPlantPlantingDate(LocalDate plantPlantingDate) {this.plantPlantingDate = plantPlantingDate;}
    public LocalDate getPlantLastWateringDate() {return plantLastWateringDate;}
    public void setPlantLastWateringDate(LocalDate plantLastWateringDate) throws PlantException {
        if (plantLastWateringDate.isBefore(plantPlantingDate)) {
            throw new PlantException("Chyba - datum poslední zálivky je před vysazením rostliny");
        }
        this.plantLastWateringDate = plantLastWateringDate;
    }
    public int getPlantNormalWateringFrequency() {return plantNormalWateringFrequency;}
    public void setPlantNormalWateringFrequency(int plantNormalWateringFrequency) throws PlantException {
        if (plantNormalWateringFrequency < 1) {
            throw new PlantException("Chyba - frekvence zálivky je menší než jeden den, její hodnota je nyní: "
                    + plantNormalWateringFrequency);
        }
        this.plantNormalWateringFrequency = plantNormalWateringFrequency;
    }

    // Úkol lekce 6 bod 2 - 2. Úprava třídy Plant pro řazení rostlin podle názvu pomocí Collections.sort
    @Override
    public int compareTo(Plant otherPlant) {return this.plantName.compareTo(otherPlant.getPlantName());}


}