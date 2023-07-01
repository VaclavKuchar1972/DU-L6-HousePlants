package com.domaciukollekce6.houseplants;

import java.time.format.DateTimeFormatter;

public class Settings {
    public static String fileNamePrimaryS() {return "DB-ListOfPlantsPrimary.txt";}
    public static String fileNameAfterChangesS() {return "DB-ListOfPlantsAfterChanges.txt";}
    public static String delimiterS () {return "\t";}

    public static void printPlantsPeopleDateOutput(Plant plant) {
        System.out.println("\t" + plant.getPlantNameP() + delimiterS () + plant.getPlantNoteP() + delimiterS ()
                + plant.getPlantNormalWateringFrequencyP() + delimiterS ()
                + plant.getPlantLastWateringDateP().format(DateTimeFormatter.ofPattern("d.M.yyyy")) + delimiterS ()
                + plant.getPlantPlantingDateP().format(DateTimeFormatter.ofPattern("d.M.yyyy")));
    }

    public static void printPlantsComputerOutput(Plant plant) {
        System.out.println("\t" + plant.getPlantNameP() + delimiterS () + plant.getPlantNoteP() + delimiterS ()
                + plant.getPlantNormalWateringFrequencyP() + delimiterS () + plant.getPlantLastWateringDateP()
                + delimiterS () + plant.getPlantPlantingDateP());
    }

}