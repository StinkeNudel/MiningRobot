import robot.*;

class RobotController {
    private static char[][] field;

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
        field = getWorldField(world, robot, factory, n);

        drawMap(world, robot, factory, n);
        drawField(field, n);

        moveTo(0, 3, world, robot);

        drawMap(world, robot, factory, n);
        drawField(field, n);

        factory.waitTillFinished();
        System.out.println("Remaining materials: " + world.getTotalMaterials()); // should be 0
        System.out.println("Time passed: " + world.getTimePassed());
    }

    private static void moveTo(int x, int y, World world, Robot robot) {
        while(robot.getX() != x || robot.getY() != y){
            //move one field
            if (getVectorSize(robot.getX(), robot.getY(), x, y) >
                    getVectorSize(robot.getX(), robot.getY() - 1, x, y)) {
                robot.moveUp();
            } else if (getVectorSize(robot.getX(), robot.getY(), x, y) >
                    getVectorSize(robot.getX() - 1, robot.getY(), x, y)) {
                robot.moveLeft();
            } else if (getVectorSize(robot.getX(), robot.getY(), x, y) >
                    getVectorSize(robot.getX() + 1, robot.getY(), x, y)) {
                robot.moveRight();
            } else if (getVectorSize(robot.getX(), robot.getY(), x, y) >
                    getVectorSize(robot.getX(), robot.getY() + 1, x, y)) {
                robot.moveDown();
            }
            //collect and mark
            if (field[robot.getX()][robot.getY()] != 'v') {
                for (int i = 0; i < 3; i++) {
                    if (world.getFieldMaterials(robot.getX(), robot.getY()) > 0) {
                        robot.gatherMaterials();
                    } else {
                        field[robot.getX()][robot.getY()] = 'v';
                        break;
                    }
                }
            }
        }

    }

    private static double getVectorSize(int robotX, int yGhost, int targetX, int targetY) {
        return Math.sqrt((targetX - robotX) * (targetX - robotX) + (targetY - yGhost) * (targetY - yGhost));
    }

    private static char[][] getWorldField(World world, Robot robot, Factory factory, int n) {
        char[][] a = new char[n][n];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (world.getFieldMaterials(j, i) > 0)
                    a[j][i] = 'M';
                else
                    a[j][i] = '-';
            }
        }
        return a;
    }

    private static void drawField(char[][] a, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(a[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void drawMap(World world, Robot robot, Factory factory, int n) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
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
