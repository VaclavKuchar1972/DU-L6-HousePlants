import com.domaciukollekce6.houseplants.Plant;
import com.domaciukollekce6.houseplants.PlantException;
import com.domaciukollekce6.houseplants.PlantManager;
import com.domaciukollekce6.houseplants.Settings;

import java.time.LocalDate;
import java.util.List;

public class HousePlants {
    public static void main(String[] args) throws PlantException {

        PlantManager plantManager = new PlantManager();

        try {plantManager.loadDataPlantsFromFilePM(Settings.fileNamePrimary(), Settings.delimiter());}
        catch (PlantException e) {
            System.err.println("Nepodařilo se načíst data ze souboru " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        // Toto musí být až po natažení dat, jinak je list prázdný,
        // narozdíl od ostatních tříd se Main evidentně chová striktně chronologicky!!! (to je pro mě)
        List<Plant> plantListPM = plantManager.getPlantList();

        System.out.println();
        System.out.println("Surová data ze VSTUPNÍHO souboru DB-ListOfPlantsPrimary.txt:");
        for (Plant plant : plantListPM) {Settings.printPlantsComputerOutput(plant);}

        System.out.println();
        System.out.println("Informace o zálivce dle zadání domácího úkolu v bodě 13:");
        for (Plant plant : plantListPM) {Settings.printPlantsComputerOutput(plant);}


        try {
            plantListPM.add(new Plant("Jahodník", "na zábradlí balkónu", LocalDate.now(),
                    LocalDate.now(),
                    3));
            plantListPM.add(new Plant("Mochíto Máta", "na balkóně", LocalDate.now(),
                    LocalDate.now(), 2));
        }
        catch (PlantException e) {
            System.err.print("Nastala chyba při vytváření nových rostlin" + e.getLocalizedMessage());
        }


        System.out.println();
        System.out.println("Aktualizovaný seznam rostlin po přidání dvou rostlin dle zadání domácího úkolu v bodě 14:");
        for (Plant plant : plantListPM) {Settings.printPlantsPeopleDateOutput(plant);}

        plantListPM.removeIf(plant -> plant.getPlantName().equals("Sukulent v koupelně"));
        System.out.println();
        System.out.println
                ("Aktualizovaný seznam rostlin po odebrání jedné rostliny dle zadání domácího úkolu v bodě 14:");
        for (Plant plant : plantListPM) {Settings.printPlantsPeopleDateOutput(plant);}


        System.out.println();
        try {
            plantManager.saveDataPlantsToNewFile(Settings.fileNameAfterChanges(), plantListPM);
            System.out.println("Aktualizovaný seznam rostlin byl uložen do souboru: "
                    + Settings.fileNameAfterChanges());
        } catch (PlantException e) {
            System.err.println("Chyba při ukládání dat: " + e.getMessage());
            throw new RuntimeException(e);
        }


        try {plantManager.loadDataPlantsFromFilePM(Settings.fileNameAfterChanges(), Settings.delimiter());}
        catch (PlantException e) {
            System.err.println("Nepodařilo se načíst data ze souboru " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println("Surová data z VÝSTUPNÍHO souboru DB-ListOfPlantsAfterChanges.txt:");
        for (Plant plant : plantListPM) {Settings.printPlantsComputerOutput(plant);}

    }

}