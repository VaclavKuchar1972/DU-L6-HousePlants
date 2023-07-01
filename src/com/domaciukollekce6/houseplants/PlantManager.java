package com.domaciukollekce6.houseplants;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.domaciukollekce6.houseplants.Settings.delimiter;

public class PlantManager {
    private List<Plant> plantList = new ArrayList<>();

    // Předání kopie seznamu tak, aby ho nikdo zvenčí nemohl nikdo měnit (přikázal lektor Martin)
    public List<Plant> getPlantList() {return new ArrayList<>(plantList);}

    public void addPlant(Plant plant) {
        plantList.add(plant);}
    public void removePlant(Plant plant) {
        plantList.remove(plant);}

    public Plant getPlantFromIdex(int indexPM) {return plantList.get(indexPM);}

    public void loadDataPlantsFromFilePM (String fileNamePrimaryPM, String delimiterPM) throws PlantException {
        int helpLineNumber = 0; int helpBadDateIdentifokator = 3;
        int plantNormalWateringFrequency = 0;
        String line = ""; String plantName = ""; String plantNote = "";
        String[] items = new String[0];
        LocalDate plantLastWateringDate = null; LocalDate plantPlantingDate = null;
        try (Scanner scannerLoadData = new Scanner(new BufferedReader(new FileReader(fileNamePrimaryPM)))) {
            while (scannerLoadData.hasNextLine()) {
                helpLineNumber = helpLineNumber + 1;
                line = scannerLoadData.nextLine();
                // Oddělení jednotlivých dat stažených ze souboru (teď máme tabulátor, kterej se mi vůbec nelíbí)
                items = line.split(delimiterPM);
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
                Plant newPlantPM = new Plant(plantName, plantNote, plantPlantingDate, plantLastWateringDate,
                        plantNormalWateringFrequency);
                plantList.add(newPlantPM);
            }
        } catch (FileNotFoundException e) {
            throw new PlantException("Soubor " + fileNamePrimaryPM + "nebyl nalezen! " + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new PlantException("Chyba - v databázi není číslo: " + items[2]
                    + " na řádku: " + helpLineNumber + ": " + line);
        } catch (DateTimeParseException e) {
            throw new PlantException("Chyba - v databázi není datum: " + items[helpBadDateIdentifokator]
                    + " na řádku: " + helpLineNumber + ": " + line);
        }
    }
    public void saveDataPlantsToNewFile(String fileName, List<Plant> plantList) throws PlantException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Plant plant : plantList) {
                writer.write(plant.getPlantName() + delimiter() + plant.getPlantNote() + delimiter()
                        + plant.getPlantNormalWateringFrequency() + delimiter()
                        + plant.getPlantLastWateringDate() + delimiter()
                        + plant.getPlantPlantingDate());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PlantException("Chyba při ukládání dat do souboru: " + e.getMessage());
        }
    }

}
