import robot.*;

class RobotController {

    private static int robotLoad = 0;
    private static int lastDir = 4;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java RobotController n");
            return;
        }
        int n = Integer.parseInt(args[0]);
        World world = new World(n);
        Robot robot = world.getRobot();
        Factory factory = world.getFactory();
        /* TODO */

        //drawMap(world, robot, factory);


        while (world.getTotalMaterials() > 0) {
            tick(world, factory, robot);
        }

        //last unload
        moveTo(factory.getX(), factory.getY(), world, robot);
        robot.unloadMaterials();

        factory.waitTillFinished();
        System.out.println("Remaining materials: " + world.getTotalMaterials()); // should be 0
        System.out.println("Time passed: " + world.getTimePassed());
    }

    private static void moveTo(int x, int y, World world, Robot robot) {
        while (robot.getX() != x || robot.getY() != y) {
            switch (getShortestDirection(x, y, world, robot)) {
                case 0 -> robot.moveUp();
                case 1 -> robot.moveDown();
                case 2 -> robot.moveRight();
                case 3 -> robot.moveLeft();
            }
            //drawMap(world, robot, world.getFactory());
        }
    }

    private static void tick(World world, Factory factory, Robot robot) {
        if (robotLoad >= 3) {
            moveTo(factory.getX(), factory.getY(), world, robot);
            robot.unloadMaterials();
            robotLoad = 0;
        } else {
            Cord nextOre = getNextOre(robot.getX(), robot.getY(), world);
            moveTo(nextOre.x(), nextOre.y(), world, robot);
            robot.gatherMaterials();
            robotLoad++;
        }
    }

    private static int getShortestDirection(int x, int y, World world, Robot robot) {
        //0 up | 1 down | 2 right | 3 left

        Vector up = new Vector(robot.getX(), robot.getY() - 1, x, y, 0, world);
        Vector down = new Vector(robot.getX(), robot.getY() + 1, x, y, 1, world);
        Vector right = new Vector(robot.getX() + 1, robot.getY(), x, y, 2, world);
        Vector left = new Vector(robot.getX() - 1, robot.getY(), x, y, 3, world);

        Vector minDistance;

        if(lastDir != 1){
            minDistance = up;
        }else {
            minDistance = down;
        }

        if (down.getLength() < minDistance.getLength() && lastDir != 0) {
            minDistance = down;
        }
        if (right.getLength() < minDistance.getLength() && lastDir != 3) {
            minDistance = right;
        }
        if (left.getLength() < minDistance.getLength() && lastDir != 2) {
            minDistance = left;
        }
        lastDir = minDistance.getDir();
        return minDistance.getDir();
    }

    static class Vector {
        World world;
        int targetX, targetY, startX, startY;
        int dir; // 0 up | 1 down | 2 right | 3 left

        public Vector(int targetX, int targetY, int startX, int startY, int dir, World world) {
            this.targetX = targetX;
            this.targetY = targetY;
            this.startX = startX;
            this.startY = startY;
            this.dir = dir;
            this.world = world;
        }

        public int getDir() {
            return dir;
        }

        public double getLength() {
            if (targetX < world.getN() && targetY < world.getN() && targetX >= 0 && targetY >= 0)
                return getVectorSize(startX, startY, targetX, targetY) * 8 + world.getFieldTime(targetX, targetY);
            else
                return getVectorSize(startX, startY, targetX, targetY) + 999999999;
        }
    }

    private static double getVectorSize(int startX, int startY, int targetX, int targetY) {
        return Math.sqrt((targetX - startX) * (targetX - startX) + (targetY - startY) * (targetY - startY));
    }


    record Cord(int x, int y) {
    }

    private static Cord getNextOre(int robotX, int robotY, World world) {
        Cord next = new Cord(0, 0);
        Cord c;
        for (int i = 0; i < world.getN(); i++) {
            for (int j = 0; j < world.getN(); j++) {
                if (world.getFieldMaterials(j, i) > 0) {
                    c = new Cord(j, i);
                    if (getVectorSize(robotX, robotY, next.x(), next.y()) > getVectorSize(robotX, robotY, c.x(), c.y())) {
                        next = c;
                    }
                }
            }
        }
        return next;
    }

    public static void drawMap(World world, Robot robot, Factory factory) {
        for (int i = 0; i < world.getN(); i++) {
            for (int j = 0; j < world.getN(); j++) {
                if (robot.getX() == j && robot.getY() == i) {
                    System.out.print("x");
                } else if (factory.getX() == j && factory.getY() == i) {
                    System.out.print("F");
                } else if (world.getFieldMaterials(j, i) > 0)
                    System.out.print("" + world.getFieldMaterials(j, i));
                else
                    System.out.print("-");
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
