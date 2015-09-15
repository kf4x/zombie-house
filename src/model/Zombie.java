package model;



public class Zombie implements Deadly, Mover
{
  protected float
          zombieDecisionRate,
          rotation,
          smell,
          speed,
          x, y;

  protected FindStrategy findStrategy;

  public Zombie()
  {
    zombieDecisionRate = 2f;
    rotation = 0;
    smell = 7f;
    speed = .5f;
    x = 0;
    y = 0;
    findStrategy = new BFSFindStrategy();
  }

  /**
   * Set the type of findStrategy this zombie should use
   *
   * @param findStrategy the algorithm the zombie shall use for finding Character
   */
  public Zombie(FindStrategy<Tile> findStrategy)
  {
    zombieDecisionRate = 2f;
    rotation = 0;
    smell = 7f;
    speed = .5f;
    x = 0;
    y = 0;
    this.findStrategy = findStrategy;
  }

  @Override
  public void setTakePoints (float points)
  {
    return;
  }

  @Override
  public float getTakePoints ()
  {
    return 0;
  }

  @Override
  public float getCurrentX ()
  {
    return 0;
  }

  @Override
  public float getCurrentY ()
  {
    return 0;
  }

  @Override
  public float getSpeed ()
  {
    return 0;
  }

  @Override
  public float getRotation ()
  {
    return 0;
  }

  @Override
  public void move (float x, float y)
  {
    return;
  }

  @Override
  public float setSpeed (float speed)
  {
    return 0;
  }

  @Override
  public float setRotation (float rotation)
  {
    return 0;
  }

  @Override
  public float getStamina() { return 0; }

  @Override
  public float setStamina(float stamina)
  {
    return 0;
  }

  public float getSmell()
  {
    return smell;
  }
}
