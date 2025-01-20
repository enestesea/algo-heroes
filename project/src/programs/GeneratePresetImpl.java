package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    /**
     * Сортировка: O(n log n)
     * Выбор юнитов и добавление в армию: O(n)
     * Назначение координат: O(n)
     * Общая сложность алгоритма — O(n log n) (n — количество юнитов)
     **/
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Создание новой армии
        final Army computerArmy = new Army();
        final List<Unit> selectedUnits = new ArrayList<>();

        // Сортировка юнитов по эффективности (по соотношению атаки + здоровья к стоимости)
        sortUnitsByEffectiveness(unitList);

        int currentPoints = 0;

        // Добавляем юнитов, пока есть доступные очки
        for (Unit unit : unitList) {
            // Определяем количество юнитов, которое можно добавить для данного типа
            int unitsToAdd = calculateMaxUnitsToAdd(unit, maxPoints, currentPoints);
            // Добавляем юнитов в армию
            addUnitsToArmy(unit, unitsToAdd, selectedUnits);
            // Увеличиваем потраченные очки
            currentPoints += unitsToAdd * unit.getCost();
        }

        // Назначаем случайные координаты юнитам
        assignCoordinates(selectedUnits);

        // Устанавливаем юниты в армию и меняем наше количесвто очков
        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(currentPoints);

        return computerArmy;
    }

    // Сортировка юнитов по их эффективности (по соотношению атаки + здоровья к стоимости)
    private void sortUnitsByEffectiveness(List<Unit> units) {
        units.sort(Comparator.comparingDouble(unit ->
                -((double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost())
        ));
    }

    // Вычисление максимально возможного количества юнитов для данного типа
    private int calculateMaxUnitsToAdd(Unit unit, int maxPoints, int currentPoints) {
        return Math.min(11, (maxPoints - currentPoints) / unit.getCost());
    }

    // Добавление юнитов в армию с уникальными именами
    private void addUnitsToArmy(Unit unit, int unitsToAdd, List<Unit> selectedUnits) {
        for (int i = 0; i < unitsToAdd; i++) {
            selectedUnits.add(createNewUnit(unit, i));
        }
    }

    // Создание нового юнита с уникальным именем
    private Unit createNewUnit(Unit unit, int index) {
        final Unit newUnit = new Unit(unit.getName(), unit.getUnitType(), unit.getHealth(),
                unit.getBaseAttack(), unit.getCost(), unit.getAttackType(),
                unit.getAttackBonuses(), unit.getDefenceBonuses(), -1, -1);
        // Имя юнита = тип юнита с индексом
        newUnit.setName(unit.getUnitType() + " " + index);
        return newUnit;
    }

    // Назначение случайных координат юнитам с учетом занятых клеток
    private void assignCoordinates(List<Unit> units) {
        final Set<String> occupiedCoords = new HashSet<>();
        final Random random = new Random();

        for (Unit unit : units) {
            assignRandomCoordinates(unit, occupiedCoords, random);
        }
    }

    // Присвоение юниту случайных координат и проверка на занятость
    private void assignRandomCoordinates(Unit unit, Set<String> occupiedCoords, Random random) {
        int coordX;
        int coordY;
        do {
            coordX = random.nextInt(3);  // Два возможных значения для оси X (0, 1, 2)
            coordY = random.nextInt(21); // Для оси Y (0-20)
        } while (occupiedCoords.contains(coordX + "," + coordY)); // Проверка на занятость
        occupiedCoords.add(coordX + "," + coordY);
        unit.setxCoordinate(coordX);
        unit.setyCoordinate(coordY);
    }
}
