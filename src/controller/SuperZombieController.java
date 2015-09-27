package controller;


import common.Speed;
import model.House;
import model.Move;
import model.SuperZombie;
import model.Tile;

import java.util.ArrayList;
import java.util.List;

public class SuperZombieController extends AbstractCharacterController<SuperZombie>
{
  private int secIncrement = 0;
  private List<Tile> tiles = new ArrayList<>();
  public SuperZombieController (House house, SuperZombie mover)
  {
    super(house, mover);
  }

  @Override
  public boolean checkCollision(Move moveToCheck)
  {
    if (super.checkCollision(moveToCheck))
    {
      mover.setSpeed(0);
      return true;
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
    if (secIncrement == 200)
    {

      mover.getStrategy().find(house, house.getCharacterTile(house.getSuperZombie()), house
              .getCharacterTile(house.getPlayer()));
      tiles.clear();
      tiles = mover.getStrategy().getPath();
      secIncrement = 0;
    }

    Tile current = house.getCharacterTile(mover);
    tiles.remove(current);
    mover.setSpeed(Speed.WALK+.5f * deltaTime);
    if (tiles.size() > 0)
    {
      Tile next = tiles.get(0);

      float xDir = (current.getX() - next.getX()) * -1;
      float yDir = (current.getY() - next.getY()) * -1;
      if (xDir == 0 && yDir == 0)
      {
        stopMoving();
      }
      else if(xDir == 0 && yDir > 0)
      {
        moveUp();
      }
      else if (xDir == 0 && yDir < 0)
      {
        moveDown();
      }
      else if (xDir < 0 && yDir == 0)
      {
        moveLeft();
      }
      else if (xDir > 0 && yDir == 0)
      {
        moveRight();
      }
      else if (xDir < 0 && yDir > 0)
      {
        moveUpLeft();
      }
      else if (xDir > 0 && yDir > 0)
      {
        moveUpRight();
      }
      else if (xDir < 0 && yDir < 0)
      {
        moveDownLeft();
      }
      else if (xDir > 0 && yDir < 0)
      {
        moveDownRight();
      }
      else
      {
        stopMoving();
      }
    }
    else
    {
      stopMoving();
    }

    float y = (float) (mover.getCurrentY() + mover.getSpeed() * Math.sin(Math.toRadians(mover.getRotation())));
    float x = (float) (mover.getCurrentX() + mover.getSpeed() * Math.cos(Math.toRadians(mover.getRotation())));
    if(checkCollision(new Move(x, y, mover.getRotation())))
    {
      mover.move((int)current.getX(), (int)current.getY());
    }
  }
}
