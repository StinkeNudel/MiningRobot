package robot;

public class Factory {

  private int x, y;
  private World world;
  private long timeStartedProcessing;
  private final long PROCESSING_DURATION;

  Factory(int x, int y, World world) {
    this.x = x;
    this.y = y;
    this.world = world;
    PROCESSING_DURATION = world.getN() / 2;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  void process(double materials) {
    waitTillFinished();
    timeStartedProcessing = world.getTimePassed();
  }

  public void waitTillFinished() {
    long currentTime = world.getTimePassed();
    long timeProcessing = currentTime - timeStartedProcessing;
    if (timeProcessing < PROCESSING_DURATION) {
      long waitingTime = PROCESSING_DURATION - timeProcessing;
      world.increaseTimePassed(waitingTime);
    }
  }

}
