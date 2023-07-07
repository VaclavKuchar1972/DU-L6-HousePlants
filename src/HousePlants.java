import com.domaciukollekce6.houseplants.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.domaciukollekce6.houseplants.Settings.delimiter;

public class HousePlants {

    // I když jsi mi psal, že to mám dát do třídy Plant, napsal jsi mi i přesně to jak to beru, ano jsou to z mého
    // pohledu čistě pomocné metody pro třídu HousePlants, čistě z toho důvodu, aby se mi neopakoval stejný kód na více
    // místech, nic víc, žádná atomová věda. :-) Děkuji. V žádných jiných třídách tyto metody nejsou a nebudou potřeba.
    private static void printPlantsPeopleDateOutput(Plant plant) {
        System.out.println(delimiter() + plant.getPlantName() + delimiter() + plant.getPlantNote() + delimiter()
                + plant.getPlantNormalWateringFrequency() + delimiter()
                + plant.getPlantLastWateringDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")) + delimiter()
                + plant.getPlantPlantingDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")));
    }
    private static void printPlantsComputerOutput(Plant plant) {
        System.out.println(delimiter() + plant.getPlantName() + delimiter() + plant.getPlantNote() + delimiter()
                + plant.getPlantNormalWateringFrequency() + delimiter() + plant.getPlantLastWateringDate()
                + delimiter() + plant.getPlantPlantingDate());
    }
    public static void main(String[] args) throws PlantException {

        PlantManager plantManager = new PlantManager();

        try {plantManager.loadDataPlantsFromFile(Settings.fileNamePrimary(), delimiter());}
        catch (PlantException e) {
            System.err.println("Nepodařilo se načíst data ze souboru " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        // Toto musí být až po natažení dat, jinak je list prázdný,
        // narozdíl od ostatních tříd se Main evidentně chová striktně chronologicky!!! (to je pro mě)
        List<Plant> plantList = plantManager.getPlantList();

        System.out.println();
        System.out.println("Surová data ze VSTUPNÍHO souboru DB-ListOfPlantsPrimary.txt:");
        for (Plant plant : plantList) {printPlantsComputerOutput(plant);}

        System.out.println();
        System.out.println("Informace o zálivce dle zadání domácího úkolu v bodě 13:");
        for (Plant plant : plantList) {printPlantsComputerOutput(plant);}

        System.out.println();
        System.out.println("Informace o zálivce dle zadání domácího úkolu v bodě 13 z GETTERu ve třídě Plant:");
        for (Plant plant : plantList) {
            System.out.println(plant.getWateringInfo());
        }

        try {
            plantList.add(new Plant("Jahodník", "na zábradlí balkónu", LocalDate.now(),
                    LocalDate.now(),
                    3));
            plantList.add(new Plant("Mochíto Máta", "na balkóně", LocalDate.now(),
                    LocalDate.now(), 2));
        }
        catch (PlantException e) {
            System.err.print("Nastala chyba při vytváření nových rostlin" + e.getLocalizedMessage());
        }

        System.out.println();
        System.out.println("Aktualizovaný seznam rostlin po přidání dvou rostlin dle zadání domácího úkolu v bodě 14:");
        for (Plant plant : plantList) {printPlantsPeopleDateOutput(plant);}

        plantList.removeIf(plant -> plant.getPlantName().equals("Sukulent v koupelně"));
        System.out.println();
        System.out.println
                ("Aktualizovaný seznam rostlin po odebrání jedné rostliny dle zadání domácího úkolu v bodě 14:");
        for (Plant plant : plantList) {printPlantsPeopleDateOutput(plant);}

        System.out.println();
        try {
            plantManager.saveDataPlantsToNewFile(Settings.fileNameAfterChanges(), plantList);
            System.out.println("Aktualizovaný seznam rostlin byl uložen do souboru: "
                    + Settings.fileNameAfterChanges());
        } catch (PlantException e) {
            System.err.println("Chyba při ukládání dat: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try {plantManager.loadDataPlantsFromFile(Settings.fileNameAfterChanges(), delimiter());}
        catch (PlantException e) {
            System.err.println("Nepodařilo se načíst data ze souboru " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println("Surová data z VÝSTUPNÍHO souboru DB-ListOfPlantsAfterChanges.txt:");
        for (Plant plant : plantList) {printPlantsComputerOutput(plant);}

        // Seřazení rostlin dle názvu od A do Z a zobrazení - domácí úkol lekce 6 bod 3
        Collections.sort(plantList);
        System.out.println();
        System.out.println("Seřazený seznam rostlin dle názvu rostliny:");
        for (Plant plant : plantList) {printPlantsPeopleDateOutput(plant);}

        // Seřazení rostlin dle data poslední zálivky od nejstaršího po nejnovější - domácí úkol lekce 6 bod 3b, 4 a 5
        Collections.sort(plantList, new PlantLastWateringDateComparator());
        System.out.println();
        System.out.println("Seřazený seznam rostlin dle data poslední zálivky od rostliny, " +
                "která byla zalita před nejdelší dobou:");
        for (Plant plant : plantList) {printPlantsPeopleDateOutput(plant);}

    }

}