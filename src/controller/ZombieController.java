package controller;

/**
 * @author Javier Chavez
 * @author Alex Baker
 * @author Erin Sosebee
 * <p>
 * Date September 28, 2015
 * CS 351
 * Zombie House
 * <p>
 * Zombie controller. This is the class for setting normal zombie speed and rotation
 */

import common.Direction;
import common.Duration;
import common.Speed;
import model.*;

import java.util.List;

@SuppressWarnings("unchecked")
public class ZombieController extends AbstractCharacterController<Zombie>
{
  // An incrementer to keep track of when zombie decision rate should fire
  private int time = 0;

  private final boolean DEBUG = false;

  public ZombieController (House house)
  {
    super(house);
  }

  @Override
  public void update (float deltaTime)
  {
    List<Zombie> zombies = house.getZombies();

    time++;
    for (int i = 0; i < zombies.size(); i++)
    {
      Zombie zombie;
      float zombieSpeed;

      // The values of the direction floats can be either -1, 0, or 1
      float xDir, yDir; // Depending on what their value is, the zombie will know which direction to go
      float x, y; // Zombie's position in the tile

      // Get player's current tile
      Tile playerTile = house.getCharacterTile(
              house.getPlayer());

      boolean isMoving = true;
      mover = zombie = zombies.get(i);
      if (zombie.getCombustedState() == Combustible.CombustedState.IGNITED)
      {
        zombies.remove(i);
        continue;
      }

      Tile zombieTile = house.getCharacterTile(zombie);
      if (zombieTile == null || zombieTile.getCombustedState() == Combustible.CombustedState.IGNITED)
      {
        zombies.remove(i);
        continue;
      }

      if (DEBUG)
      {
        System.out.println(
                "Zombie " + i + ": (" + zombie.getCurrentX() + ", " + zombie.getCurrentY() + ")");
      }

//      boolean playerDetected = zombie.sense(playerTile);
      if (isMoving)
      {
        boolean playerDetected = zombie.sense(playerTile);
        if (playerDetected)
        {
          house.getSuperZombie().setZombie(zombie);
          running = true;
          zombieSpeed = Speed.STAGGER_RUN;

          // Get path to player
          mover.getStrategy().find(house, house.getCharacterTile(zombie),
                                   house.getCharacterTile(house.getPlayer()));

          List<Tile> path = mover.getStrategy().getPath();

          if (DEBUG)
          {
            System.out.println(
                    "Zombie " + i + ": (" + zombie.getCurrentX() + ", " + zombie.getCurrentY() + ")");
          }

          zombie.setSpeed(zombieSpeed * deltaTime);
          Tile currentTile = getCurrentTile();
          path.remove(currentTile);
          if (path.size() > 0)
          {
            Tile nextTile = path.get(0);
            xDir = (currentTile.getX() - nextTile.getX()) * -1;
            yDir = (currentTile.getY() - nextTile.getY());

            zombieDirection(xDir, yDir);
            x = (float) (mover.getCurrentX() + mover.getSpeed() * Math.cos(
                    Math.toRadians(mover.getRotation())));
            y = (float) (mover.getCurrentY() + mover.getSpeed() * Math.sin(
                    Math.toRadians(mover.getRotation())));

            checkCollision(new Move(x, y, mover.getRotation()));
          }
        }
        else // If player is not detected
        {
          running = false;
          if (idling)
          {
            zombieSpeed = Speed.IDLE;
            if (DEBUG)
            {
              System.out.println("\tIdling");
            }
          }
          else
          {
            zombieSpeed = Speed.STAGGER;
          }

          float wanderXDir, wanderYDir;
          float wanderX, wanderY;

          zombie.setSpeed(zombieSpeed * deltaTime);
          wanderX = (float) (mover.getCurrentX() + zombie.getSpeed() * Math.cos(
                  Math.toRadians(mover.getRotation())));
          wanderY = (float) (mover.getCurrentY() + zombie.getSpeed() * Math.sin(
                  Math.toRadians(mover.getRotation())));

          if (zombie.getStrategy() instanceof RandomMoveStrategy) // Random Mover
          {
            if (DEBUG)
            {
              System.out.println("\tRandom zombie");
            }
            if (checkCollision(new Move(wanderX, wanderY, mover.getRotation())))
            {
              stopMoving();

              // If the random mover zombie hits an obstacle, it moves back a bit and stops moving
              if (mover.getRotation() == Direction.NORTH)
              {
                mover.move(wanderX, wanderY + 0.05f);
              }
              if (mover.getRotation() == Direction.SOUTH)
              {
                mover.move(wanderX, wanderY - 0.05f);
              }
              if (mover.getRotation() == Direction.EAST)
              {
                mover.move(wanderX - 0.05f, wanderY);
              }
              if (mover.getRotation() == Direction.WEST)
              {
                mover.move(wanderX + 0.05f, wanderY);
              }
            }

            // Random walker updates position every decision update (every 2 seconds by default)
            if (time == 0 || time % (Duration.ZOMBIE_UPDATE * 60) == 0)
            {
              Move move = zombie.getStrategy().getNextMove(house,
                                                           house.getCharacterTile(
                                                                   zombie));

              wanderXDir = move.col;
              wanderYDir = move.row;
              zombieDirection(wanderXDir, wanderYDir);
            }
          }
          else // Line Mover
          {
            if (DEBUG)
            {
              System.out.println("\tLine zombie");
            }
            idling = false;
            if (checkCollision(new Move(wanderX, wanderY, mover.getRotation())))
            {
              // Line mover turns around, then makes a decision
              changeDirection();
              Move move = zombie.getStrategy().getNextMove(house,
                                                           house.getCharacterTile(
                                                                   zombie));
              wanderXDir = move.col;
              wanderYDir = move.row;
              zombieDirection(wanderXDir, wanderYDir);
            }
          }
        }
        isMoving = false;
      }
    } // END FOR
  }

  @Override
  public boolean checkCollision (Move moveToCheck)
  {
    boolean collision = super.checkCollision(moveToCheck);
    if (collision)
    {
      if (DEBUG)
      {
        System.out.println("\tCollision");
      }
      stopMoving();
      return true;
    }
    else
    {
      mover.move(moveToCheck.col, moveToCheck.row);
    }
    return false;
  }

  /**
   * Gets the current tile that the zombie is on.
   * @return the zombie's current tile
   */
  private Tile getCurrentTile ()
  {
    int row = (int) mover.getCurrentY();
    int col = (int) mover.getCurrentX();
    if (mover.getRotation() == Direction.SOUTH)
    {
      if (!house.getTile(row, col).getBoundingRectangle().contains(
              mover.getBoundingRectangle()))
      {
        row = (int) Math.ceil(mover.getCurrentY());
      }
    }
    else if (mover.getRotation() == Direction.WEST)
    {
      if (!house.getTile(row, col).getBoundingRectangle().contains(
              mover.getBoundingRectangle()))
      {
        col = (int) Math.ceil(mover.getCurrentX());
      }
    }
    return house.getTile(row, col);
  }

  /**
   * Determines which way the zombie will move.
   */
  private void zombieDirection (float xDir, float yDir)
  {
    // Idling
    if (xDir == 0 && yDir == 0)
    {
      resting();
    }

    // Left cases
    if (xDir < 0 && yDir == 0)
    {
      moveLeft();
    }
    if (xDir < 0 && yDir < 0)
    {
      moveLeft();
    }
    if (xDir < 0 && yDir > 0)
    {
      moveLeft();
    }

    // Right cases
    if (xDir > 0 && yDir == 0)
    {
      moveRight();
    }
    if (xDir < 0 && yDir > 0)
    {
      moveRight();
    }
    if (xDir > 0 && yDir > 0)
    {
      moveRight();
    }

    // Up/down cases
    if (yDir > 0 && xDir == 0)
    {
      moveUp();
    }
    if (yDir < 0 && xDir == 0)
    {
      moveDown();
    }
  }

  /**
   * Changes a zombie's direction so that they don't constantly collide with wall and idle until the next update.
   */
  private void changeDirection ()
  {
    if (mover.getRotation() == Direction.EAST)
    {
      mover.setRotation(Direction.WEST);
    }
    else if (mover.getRotation() == Direction.WEST)
    {
      mover.setRotation(Direction.EAST);
    }
    else if (mover.getRotation() == Direction.NORTH)
    {
      mover.setRotation(Direction.SOUTH);
    }
    else if (mover.getRotation() == Direction.SOUTH)
    {
      mover.setRotation(Direction.NORTH);
    }
  }
}
