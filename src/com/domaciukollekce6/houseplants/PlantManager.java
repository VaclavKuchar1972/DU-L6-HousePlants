package com.domaciukollekce6.houseplants;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.domaciukollekce6.houseplants.Settings.delimiter;

public class PlantManager {

    private List<Plant> plantList;

    // HashSet<LocalDate> uniquePlantingDates = new HashSet<>();


    // Toto taky nefunguje i s tím níže zakomentovaným v inicializaci a to co by dělal nový kód v addPlant také
    // zakomentovaný v této sérii poznámek, tak to nechci, myslím, že můžu mít doma stejnou rostlinu a odlišit jí jen
    // např. poznámkou tím umístením, Bobkovej list, je stále Bobkovej list, jen je v jiným květináči

    // private HashSet<String> uniquePlantNames; // HashSet pro kontrolu unikátnosti názvů rostlin
    //public void addPlant(Plant plant) {
    //    if (uniquePlantNames.add(plant.getPlantName())) { // Kontrola unikátnosti názvu před přidáním
    //        plantList.add(plant);
    //    }
    //}


    public PlantManager() {
        this.plantList = new ArrayList<>();
        /* this.uniquePlantNames = new HashSet<>(); // Inicializace HashSetu */
    }
    
    public void addPlant(Plant plant) {plantList.add(plant);}

    public void removePlant(Plant plant) {plantList.remove(plant);}

    public void removePlantByName(String plantName) {
        plantList.removeIf(plant -> plant.getPlantName().equals(plantName));
    }

    // DUPLICITAMI MYSLÍM, že po vySortování list vypadal takto:
    // 	Fialka 1	Popis fialky - je fialová a hezká	3	12.5.2021	1.1.2021
    //	Fialka 1	Popis fialky - je fialová a hezká	3	12.5.2021	1.1.2021
    //	Jahodník	na zábradlí balkónu	3	15.7.2023	15.7.2023
    //	Jahodník	na zábradlí balkónu	3	15.7.2023	15.7.2023
    //	Mochíto Máta	na balkóně	2	15.7.2023	15.7.2023
    //	Mochíto Máta	na balkóně	2	15.7.2023	15.7.2023
    //	Vánoční hvězda bez poznámky		4	10.5.2021	1.4.2021
    //	Vánoční hvězda bez poznámky		4	10.5.2021	1.4.2021
    // Duplicity názvů samotných rostlin bych spíše uvítal a jejich skutečnou duplicitu vychytal plantNote
    // nebo něčím jiným

    // Nový kód na řazení s odstraněním duplicit - ALE TO SE MI FAKT NELÍBÍ - Již to funguje jak má a po sřazení nejsou
    // rostliny duplicitní, ale podle mě to není dobře!!! - ALE nevím co s tím jiného. :-(
    private void removeDuplicatePlants() {
        Set<Plant> uniquePlants = new LinkedHashSet<>(plantList); plantList.clear(); plantList.addAll(uniquePlants);}
    //Takto to jde taky, ale je to zbytečně komplikované a vliv na duplicity to nemá. Kéž by mělo!!!
    //public void sortPlantsByName() {plantList.sort(Comparator.comparing(Plant::getPlantName));
    // removeDuplicatePlants();}

    public void sortPlantsByName() {Collections.sort(plantList); removeDuplicatePlants();}
    //public void sortPlantsByLastWateringDate() {Collections.sort(plantList); /*removeDuplicatePlants();*/}
    public void sortPlantsByLastWateringDate() {plantList.sort(new PlantLastWateringDateComparator());}


    public void printUniquePlantingDates() {
        HashSet<LocalDate> uniquePlantingDates = new HashSet<>();
        for (Plant plant : plantList) {uniquePlantingDates.add(plant.getPlantPlantingDate());}
        System.out.println("Výpis dnů, kdy byla zasazena alespoň jedna rostlina:");
        for (LocalDate date : uniquePlantingDates) {
            System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
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

    public void saveDataPlantsToNewFile(String fileName, List<Plant> plants) throws PlantException {
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

// MEMENTO po 4 hodinách pokusů se Sortováním v PlantManager místo v HousePlant - skvělé! :-(

// Pomoc při vývoji, abych věděl co se děje
//public void printPlants() {
//    for (Plant plant : plantList) {
//        System.out.println(plant.getPlantName());
//    }
//}

//HashSet<Plant> sortPlantsByName = new HashSet<>();

// public void sortPlantsByName() {
//    plantList.sort(Comparator.comparing(Plant::getPlantName));
//}
