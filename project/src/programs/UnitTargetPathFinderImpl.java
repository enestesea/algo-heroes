package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Инициализация: O(n * m)
     * Поиск пути с использованием очереди с приоритетом: O((n * m) * log(n * m))
     * Восстановление пути: O(n * m)
     * Общая сложность алгоритма: O(n * m * log(n * m)) (n - ширина, m - высота поля)
     */
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        final int[][] distance = initializeDistanceArray();
        final boolean[][] visited = new boolean[WIDTH][HEIGHT];
        final Edge[][] previous = new Edge[WIDTH][HEIGHT];
        final Set<String> occupiedCells = getOccupiedCells(existingUnitList, attackUnit, targetUnit);

        final PriorityQueue<EdgeDistance> queue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));
        initializeStartPoint(attackUnit, distance, queue);

        // Выполняем алгоритм поиска пути
        while (!queue.isEmpty()) {
            final EdgeDistance current = queue.poll();
            if (visited[current.getX()][current.getY()]) continue;
            visited[current.getX()][current.getY()] = true;

            if (isTargetReached(current, targetUnit)) {
                break;
            }

            exploreNeighbors(current, occupiedCells, distance, previous, queue);
        }

        return constructPath(previous, attackUnit, targetUnit);
    }

    //Инициализируем массив растояний
    private int[][] initializeDistanceArray() {
        final int[][] distance = new int[WIDTH][HEIGHT];
        for (int[] row : distance) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        return distance;
    }

    //Получаем занятые клетки

    private Set<String> getOccupiedCells(List<Unit> existingUnitList, Unit attackUnit, Unit targetUnit) {
        final Set<String> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                occupiedCells.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }
        return occupiedCells;
    }

    //Инициализация стартовой точки
    private void initializeStartPoint(Unit attackUnit, int[][] distance, PriorityQueue<EdgeDistance> queue) {
        final int startX = attackUnit.getxCoordinate();
        final int startY = attackUnit.getyCoordinate();
        distance[startX][startY] = 0;
        queue.add(new EdgeDistance(startX, startY, 0));
    }

    // Проверка, что цель достигнута
    private boolean isTargetReached(EdgeDistance current, Unit targetUnit) {
        return current.getX() == targetUnit.getxCoordinate() && current.getY() == targetUnit.getyCoordinate();
    }

    // Исследуем соседей
    private void exploreNeighbors(EdgeDistance current, Set<String> occupiedCells, int[][] distance, Edge[][] previous, PriorityQueue<EdgeDistance> queue) {
        for (int[] dir : DIRECTIONS) {
            final int neighborX = current.getX() + dir[0];
            final int neighborY = current.getY() + dir[1];
            if (isValid(neighborX, neighborY, occupiedCells)) {
                int newDistance = distance[current.getX()][current.getY()] + 1;
                if (newDistance < distance[neighborX][neighborY]) {
                    distance[neighborX][neighborY] = newDistance;
                    previous[neighborX][neighborY] = new Edge(current.getX(), current.getY());
                    queue.add(new EdgeDistance(neighborX, neighborY, newDistance));
                }
            }
        }
    }

    // Проверка валидности координат
    private boolean isValid(int x, int y, Set<String> occupiedCells) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && !occupiedCells.contains(x + "," + y);
    }

    // Путь к цели
    private List<Edge> constructPath(Edge[][] previous, Unit attackUnit, Unit targetUnit) {
        List<Edge> path = new ArrayList<>();
        int pathX = targetUnit.getxCoordinate();
        int pathY = targetUnit.getyCoordinate();

        // Строим путь от цели к атакующему юниту
        while (pathX != attackUnit.getxCoordinate() || pathY != attackUnit.getyCoordinate()) {
            path.add(new Edge(pathX, pathY));
            Edge prev = previous[pathX][pathY];
            if (prev == null) {
                // Путь не найден
                return Collections.emptyList();
            }
            pathX = prev.getX();
            pathY = prev.getY();
        }

        // Добавляем начальную точку в путь
        path.add(new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()));
        Collections.reverse(path);
        return path;
    }
}
