package robot;

import java.util.*;

public class World {

    private int[][] fieldTimes; // time to move to each field from a neighbor field
    private int[][] fieldMaterials; // number of materials on each field
    private Robot robot;
    private Factory factory;
    private long timePassed;

    public World(int n) {
        fieldTimes = new int[n][n];
        fieldMaterials = new int[n][n];
        factory = new Factory(n / 2, n - 1, this);
        robot = new Robot(factory.getX(), factory.getY(), this);
        Random randGen = new Random(42);
        for (int i = 0; i < 2 * n; ++i) {
            int x = randGen.nextInt(n);
            int y = randGen.nextInt(n);
            ++fieldMaterials[y][x];
        }
        fieldMaterials[factory.getY()][factory.getX()] = 0;
        randGen = new Random(24);
        for (int i = 0; i < 4 * n * n; ++i) {
            int x = randGen.nextInt(n);
            int y = randGen.nextInt(n);
            ++fieldTimes[y][x];
        }
        fieldTimes[factory.getY()][factory.getX()] = 0;
        System.out.println("Initial materials: " + getTotalMaterials());
    }

    public int getTotalMaterials() {
        int total = 0;
        for (int y = 0; y < fieldMaterials.length; ++y) {
            for (int x = 0; x < fieldMaterials[y].length; ++x) {
                total += fieldMaterials[y][x];
            }
        }
        return total;
    }

    int gatherMaterials(int x, int y, int amount) {
        int gathered = (int) Math.min(fieldMaterials[y][x], amount);
        fieldMaterials[y][x] -= gathered;
        return gathered;
    }

    public int getFieldTime(int x, int y) {
        return fieldTimes[y][x];
    }

    public int getFieldMaterials(int x, int y) {
        return fieldMaterials[y][x];
    }

    public int getN() { // world size is N times N
        return fieldTimes.length;
    }

    void travelToField(int x, int y) {
        timePassed += fieldTimes[y][x];
    }

    void increaseTimePassed(long time) {
        this.timePassed += time;
    }

    public long getTimePassed() {
        return timePassed;
    }

    public Robot getRobot() {
        return robot;
    }

    public Factory getFactory() {
        return factory;
    }

}
