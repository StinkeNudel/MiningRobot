package robot;

public class Robot {

  private static final int MAX_CAPACITY = 3;
  private int x, y;
  private int materials;
  private World world;
  private int n;

  Robot(int x, int y, World world) {
    this.x = x;
    this.y = y;
    this.world = world;
    this.n = world.getN();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean moveLeft() {
    if (x > 0) {
      --x;
      world.travelToField(x, y);
      return true;
    } else {
      return false;
    }
  }
  
  public boolean moveRight() {
    if (x < n - 1) {
      ++x;
      world.travelToField(x, y);
      return true;
    } else {
      return false;
    }
  }

  public boolean moveUp() {
    if (y > 0) {
      --y;
      world.travelToField(x, y);
      return true;
    } else {
      return false;
    }
  }
  
  public boolean moveDown() {
    if (y < n - 1) {
      ++y;
      world.travelToField(x, y);
      return true;
    } else {
      return false;
    }
  }

  public void gatherMaterials() {
    if (materials < MAX_CAPACITY) {
      int freeCapacity = MAX_CAPACITY - materials;
      int gathered = world.gatherMaterials(x, y, freeCapacity);
      materials += gathered;
    } else {
      System.out.println("Tried to gather materials while having no space left!");
    }
  }

  public void unloadMaterials() {
    Factory factory = world.getFactory();
    if (x == factory.getX() && y == factory.getY()) {
      factory.process(materials);
      materials = 0;
    } else {
      System.out.println("Tried to unload while not on factory field!");
    }
  }

}
