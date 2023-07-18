package com.domaciukollekce6.houseplants;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import static com.domaciukollekce6.houseplants.Settings.delimiter;

public class PlantManager {

    private List<Plant> plantList;

    public PlantManager() {this.plantList = new ArrayList<>();}
    
    public void addPlant(Plant plant) {plantList.add(plant);}

    public void removePlant(Plant plant) {plantList.remove(plant);}

    public void removePlantByName(String plantName) {
        plantList.removeIf(plant -> plant.getPlantName().equals(plantName));
    }

    public void sortPlantsByName() {Collections.sort(plantList);}
    public void sortPlantsByLastWateringDate() {plantList.sort(new PlantLastWateringDateComparator());}
    public void printUniquePlantingDates() {
        HashSet<LocalDate> uniquePlantingDates = new HashSet<>();
        for (Plant plant : plantList) {uniquePlantingDates.add(plant.getPlantPlantingDate());}
        System.out.println("Výpis dnů, kdy byla zasazena alespoň jedna rostlina:");
        for (LocalDate date : uniquePlantingDates) {
            System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }
    public void printRecentPlantings() {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneMonthAgo = currentDate.minus(1, ChronoUnit.MONTHS);
        System.out.println("Rostliny zasazené za poslední měsíc s tím, že když jich ve stejný den bylo zasazeno víc, "
                + "vypíše se jen jedno datum:");
        HashSet<LocalDate> uniqueDates = new HashSet<>();
        for (Plant plant : plantList) {
            LocalDate plantingDate = plant.getPlantPlantingDate();
            if (plantingDate.isAfter(oneMonthAgo) || plantingDate.isEqual(oneMonthAgo)) {uniqueDates.add(plantingDate);}
        }
        for (LocalDate date : uniqueDates) {System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));}
    }

    public void loadDataPlantsFromFile(String fileNamePrimary, String delimiter) throws PlantException {
        int helpLineNumber = 0; int helpBadDateIdentifokator = 3;
        int plantNormalWateringFrequency = 0;
        String line = ""; String plantName = ""; String plantNote = "";
        String[] items = new String[0];
        LocalDate plantLastWateringDate = null; LocalDate plantPlantingDate = null;
        try (Scanner scannerLoadData = new Scanner(new BufferedReader(new FileReader(fileNamePrimary)))) {
            while (scannerLoadData.hasNextLine()) {
                helpLineNumber = helpLineNumber + 1;
                line = scannerLoadData.nextLine();
                // Oddělení jednotlivých dat stažených ze souboru (teď máme tabulátor, kterej se mi vůbec nelíbí)
                items = line.split(delimiter);
                if (items.length != 5) {
                    throw new PlantException(
                            "Chyba - špatný počet položek na řádku: " + helpLineNumber + ": " + line);
                }
                plantName = items[0];
                plantNote = items[1];
                plantNormalWateringFrequency = Integer.parseInt(items[2]);
                plantLastWateringDate = LocalDate.parse(items[3]);
                helpBadDateIdentifokator = 4;
                plantPlantingDate = LocalDate.parse(items[4]);
                Plant newPlant = new Plant(plantName, plantNote, plantPlantingDate, plantLastWateringDate,
                        plantNormalWateringFrequency);
                plantList.add(newPlant);
            }
        } catch (FileNotFoundException e) {
            throw new PlantException("Soubor " + fileNamePrimary + "nebyl nalezen! " + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new PlantException("Chyba - v databázi není číslo: " + items[2]
                    + " na řádku: " + helpLineNumber + ": " + line);
        } catch (DateTimeParseException e) {
            throw new PlantException("Chyba - v databázi není datum: " + items[helpBadDateIdentifokator]
                    + " na řádku: " + helpLineNumber + ": " + line);
        }
    }

    public void saveDataPlantsToNewFile(String fileName) throws PlantException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Plant plant : plantList) {
                writer.write(plant.getPlantName() + delimiter() + plant.getPlantNote() + delimiter()
                        + plant.getPlantNormalWateringFrequency() + delimiter()
                        + plant.getPlantLastWateringDate() + delimiter()
                        + plant.getPlantPlantingDate());
                writer.newLine();
            }
        } catch (IOException e) {throw new PlantException("Chyba při ukládání dat do souboru: " + e.getMessage());}
    }

    // Předání kopie seznamu tak, aby ho nikdo zvenčí nemohl nikdo měnit (přikázal lektor Martin)
    public List<Plant> getPlantList() {return new ArrayList<>(plantList);}

}

