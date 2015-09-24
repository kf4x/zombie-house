package controller;


import model.House;
import model.Move;
import model.SuperZombie;
import model.Tile;

import java.util.ArrayList;
import java.util.List;

public class SuperZombieController extends AbstractCharacterController<SuperZombie>
{
  private boolean initial = true; // for initial scare
  private int secIncrement = 0;
  private List<Tile> tiles;
  public SuperZombieController (House house, SuperZombie mover)
  {
    super(house, mover);
    initial = true;
  }

  @Override
  public boolean checkCollision(Move moveToCheck)
  {
    if (super.checkCollision(moveToCheck))
    {
      mover.setSpeed(0);
    }
    else
    {
      mover.move(moveToCheck.col, moveToCheck.row);
    }
    return false;
  }

  @Override
  public void update (float deltaTime)
  {
    secIncrement++;
    if (initial || secIncrement == 200)
    {

      mover.getStrategy().find(house, house.getCharacterTile(house.getSuperZombie()), house
              .getCharacterTile(house.getPlayer()));
      tiles = new ArrayList<>(50);
      tiles = mover.getStrategy().getPath();
      new Thread(() -> {
        while(tiles.size() > 0)
        {

          Tile t = tiles.remove(0);
          mover.move(t.getX(), t.getY());

          try
          {
            Thread.sleep(140);
          }
          catch (InterruptedException e)
          {

            e.printStackTrace();
          }
        }
        // house.slowReset();
      }).start();

      initial = false;
      secIncrement = 0;

    }
  }
}