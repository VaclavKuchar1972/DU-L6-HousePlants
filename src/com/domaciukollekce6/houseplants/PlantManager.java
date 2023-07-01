package com.domaciukollekce6.houseplants;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.domaciukollekce6.houseplants.Settings.delimiterS;

public class PlantManager {
    // POMOCNÝ PROMĚNNÝ JIŽ NIKDY NEAKTIVOVAT NA ZAĆÁTKU PROGRAMU nebo Třídy!!!
    // Jejich aktivace (alokace v paměti) patří přímo do dané metody, kde je budu potřebovat, protože po skončení metody
    // se z paměti ihned uvolní. (to je pro mě)

    private List<Plant> plantListPM = new ArrayList<>();

    // Předání kopie seznamu tak, aby ho nikdo zvenčí nemohl nikdo měnit (přikázal lektor Martin)
    public List<Plant> getPlantListPM() {return new ArrayList<>(plantListPM);}

    public void addPlantPM(Plant plant) {plantListPM.add(plant);}
    public void removePlantPM(Plant plant) {plantListPM.remove(plant);}

    public Plant getPlantFromIdexPM(int indexPM) {return plantListPM.get(indexPM);}

    public void loadDataPlantsFromFilePM (String fileNamePrimaryPM, String delimiterPM) throws PlantException {
        int helpLineNumberPM = 0; int helpBadDateIdentifokatorPM = 3;
        int plantNormalWateringFrequencyPM = 0;
        String linePM = ""; String plantNamePM = ""; String plantNotePM = "";
        String[] itemsPM = new String[0];
        LocalDate plantLastWateringDatePM = null; LocalDate plantPlantingDatePM = null;
        try (Scanner scannerLoadDataPM = new Scanner(new BufferedReader(new FileReader(fileNamePrimaryPM)))) {
            while (scannerLoadDataPM.hasNextLine()) {
                helpLineNumberPM = helpLineNumberPM + 1;
                linePM = scannerLoadDataPM.nextLine();
                // Oddělení jednotlivých dat stažených ze souboru (teď máme tabulátor, kterej se mi vůbec nelíbí)
                itemsPM = linePM.split(delimiterPM);
                if (itemsPM.length != 5) {
                    throw new PlantException(
                            "Chyba - špatný počet položek na řádku: " + helpLineNumberPM + ": " + linePM);
                }
                plantNamePM = itemsPM[0];
                plantNotePM = itemsPM[1];
                plantNormalWateringFrequencyPM = Integer.parseInt(itemsPM[2]);
                plantLastWateringDatePM = LocalDate.parse(itemsPM[3]);

                // Toto ošetření nad rámec domácího úkolu jsem si po konzultaci,
                // kde jsem zase otravoval nejvíc ze všech, nemohl odpustit... ...to bych Tě urazil. :-)
                // Funguje to teda nějak divně!!! :D ten výpis chyby jsem nepochopil, ALE funguje dostatečně na to,
                // aby frajer (specialista), kterýmu to tuto chybu zahlásí, věděl ihned, kde je problém
                // jestli budeme mít spolu nějakou další konzultaci, tak Tě ohleděně tohoto opět vyslechnu :-)
                helpBadDateIdentifokatorPM = 4;

                plantPlantingDatePM = LocalDate.parse(itemsPM[4]);
                Plant newPlantPM = new Plant(plantNamePM, plantNotePM, plantPlantingDatePM, plantLastWateringDatePM,
                        plantNormalWateringFrequencyPM);
                plantListPM.add(newPlantPM);
            }
        } catch (FileNotFoundException e) {
            throw new PlantException("Soubor " + fileNamePrimaryPM + "nebyl nalezen! " + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new PlantException("Chyba - v databázi není číslo: " + itemsPM[2]
                    + " na řádku: " + helpLineNumberPM + ": " + linePM);
        } catch (DateTimeParseException e) {
            throw new PlantException("Chyba - v databázi není datum: " + itemsPM[helpBadDateIdentifokatorPM]
                    + " na řádku: " + helpLineNumberPM + ": " + linePM);
        }
    }
    public void saveDataPlantsToNewFilePM(String fileName, List<Plant> plantList) throws PlantException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Plant plant : plantList) {
                writer.write(plant.getPlantNameP() + delimiterS () + plant.getPlantNoteP() + delimiterS ()
                        + plant.getPlantNormalWateringFrequencyP() + delimiterS ()
                        + plant.getPlantLastWateringDateP() + delimiterS ()
                        + plant.getPlantPlantingDateP());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PlantException("Chyba při ukládání dat do souboru: " + e.getMessage());
        }
    }

}
