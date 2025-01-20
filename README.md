# algo-heroes/project/src/programs/

1. ### GeneratePresetImpl.java — Генерация пресета армии

   **Общая сложность алгоритма — O(n log n)(n — количество юнитов)**

   **Задача:** Генерирует армию для компьютерного противника на основе списка юнитов и максимального числа очков

   **Ключевые методы:**
    - `generate(List<Unit> unitList, int maxPoints)`: Генерирует армию с учетом максимальных очков
    - `sortUnitsByEffectiveness(List<Unit> units)`: Сортирует юнитов по эффективности
    - `assignCoordinates(List<Unit> units)`: Присваивает случайные координаты

2. ### SimulateBattleImpl.java — Симуляция Боя

   **Общая сложность алгоритма O(n * m) (n - количество юнитов в армии игрока, m - количество юнитов в армии компьютера)**

   **Задача:** Реализует пошаговую симуляцию сражения между армиями игрока и противника

   **Ключевые методы:**
    - `simulate(Army playerArmy, Army computerArmy)`: Запускает симуляцию боя
    - `executeAttacks(Set<Unit> attackingUnits, Set<Unit> defendingUnits)`: Выполняет атаки юнитов

3. ### SuitableForAttackUnitsFinderImpl.java — Поиск Подходящих Юнитов для Атаки

   **Общая сложность алгоритма O(n * m) (n - количество строк, m - количество юнитов в строке)**

   **Задача:** Находит юнитов, которые могут атаковать цель в зависимости от их позиции в армии

   **Ключевые методы:**
    - `getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget)`: Находит юнитов для атаки
    - `findSuitableUnitsInRow(List<Unit> row, boolean isLeftArmyTarget)`: Ищет подходящих юнитов в ряду

4. ### UnitTargetPathFinderImpl.java — Поиск Пути к Цели

   **Общая сложность алгоритма: O(n * m * log(n * m)) (n - ширина, m - высота поля)**

   **Задача:** Реализует алгоритм поиска пути для юнита к цели, учитывая препятствия.

   **Ключевые методы:**
    - `getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList)`: Находит путь к цели
    - `initializeDistanceArray()`: Инициализирует массив расстояний
    - `exploreNeighbors(EdgeDistance current, Set<String> occupiedCells, int[][] distance, Edge[][] previous, PriorityQueue<EdgeDistance> queue)`: Исследует соседей

