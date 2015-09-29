package view;

/**
 * @author Javier Chavez
 * @author Alex Baker
 * @author Erin Sosebee
 * <p>
 * Date September 28, 2015
 * CS 351
 * Zombie House
 * <p>
 * This is the interface for Combustible objects
 */


import common.Direction;
import common.Size;
import model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;


public class ZombieRenderer extends Renderer
{

  private final House house;
  private final ZombieSprite zombieSprite = new ZombieSprite();
  private final SuperZombieSprite superZombieSprite = new SuperZombieSprite();

  private final BufferedImage[] superWalkingWest = {
          SuperZombieSprite.getSprite(0, 3),
          SuperZombieSprite.getSprite(1, 3),
          SuperZombieSprite.getSprite(2, 3)};

  private final BufferedImage[] superWalkingEast = {
          SuperZombieSprite.getSprite(0, 1),
          SuperZombieSprite.getSprite(1, 1),
          SuperZombieSprite.getSprite(2, 1)};

  private final BufferedImage[] superWalkingNorth = {
          SuperZombieSprite.getSprite(0, 2),
          SuperZombieSprite.getSprite(1, 2),
          SuperZombieSprite.getSprite(2, 2)};

  private final BufferedImage[] superWalkingSouth = {
          SuperZombieSprite.getSprite(0, 0),
          SuperZombieSprite.getSprite(1, 0),
          SuperZombieSprite.getSprite(2, 0)};


  private final BufferedImage[] walkingWest = {
          ZombieSprite.getSprite(0, 3),
          ZombieSprite.getSprite(1, 3),
          ZombieSprite.getSprite(2, 3)};

  private final BufferedImage[] walkingEast = {
          ZombieSprite.getSprite(0, 1),
          ZombieSprite.getSprite(1, 1),
          ZombieSprite.getSprite(2, 1)};

  private final BufferedImage[] walkingNorth = {
          ZombieSprite.getSprite(0, 2),
          ZombieSprite.getSprite(1, 2),
          ZombieSprite.getSprite(2, 2)};

  private final BufferedImage[] walkingSouth = {
          ZombieSprite.getSprite(0, 0),
          ZombieSprite.getSprite(1, 0),
          ZombieSprite.getSprite(2, 0)};


  private final Animation west = new Animation(walkingWest, 1);
  private final Animation east = new Animation(walkingEast, 1);
  private final Animation north = new Animation(walkingNorth, 1);
  private final Animation south = new Animation(walkingSouth, 1);

  private final Animation superWest = new Animation(superWalkingWest, 1);
  private final Animation superEast = new Animation(superWalkingEast, 1);
  private final Animation superNorth = new Animation(superWalkingNorth, 1);
  private final Animation superSouth = new Animation(superWalkingSouth, 1);


  // check direction... need a AnimationFactoryClass
  private Animation animation;
  private Animation animationSuper;
  // final Sound2D sound2D;


  public ZombieRenderer (House house)
  {
    this.house = house;
    //    sound2D = new Sound2D();


  }

  @Override
  public void render (Graphics2D g2)
  {
    List<Zombie> zombies = house.getZombies();
    Player player = house.getPlayer();

    for (int i = 0; i < zombies.size(); i++)
    {
      Zombie zombie = zombies.get(i);

      float x = zombie.getCurrentX();
      float y = zombie.getCurrentY();

      if (!player.senseSight(house.getTile((int) y, (int) x)))
      {
        continue;
      }

      setAnimation(zombie);
      g2.drawImage(animation.getSprite(), (int) ((x * Size.TILE)),
                   (int) ((y * Size.TILE)), null);
      if (zombie.getVolume() > 0)
      {
        //        Point p = new Point(((int) zombie.getX()), ((int) zombie.getY()));
        //        Player pl = house.getPlayer();
        //        Point p2 = new Point(((int) pl.getX()), ((int) pl.getY()));
        //        sound2D.playDistSound(p, p2, (int) pl.getRotation(), true);

      }
    }
    SuperZombie superZombie = house.getSuperZombie();
    setSuperAnimation(superZombie);
    g2.drawImage(animationSuper.getSprite(),
                 (int) ((superZombie.getCurrentX() * Size.TILE)),
                 (int) ((superZombie.getCurrentY() * Size.TILE)), null);

    if (animation != null)
    {
      animation.start();
      animation.update();
    }
    if (animationSuper != null)
    {
      animationSuper.start();
      animationSuper.update();
    }

  }

  private void setAnimation (Zombie zombie)
  {
    switch ((int) zombie.getRotation())
    {
      case (int) Direction.EAST:
        animation = east;
        break;
      case (int) Direction.NORTH:
        animation = south;
        break;
      case (int) Direction.SOUTH:
        animation = north;
        break;
      case (int) Direction.WEST:
        animation = west;
        break;
    }
  }

  private void setSuperAnimation (SuperZombie zombie)
  {
    switch ((int) zombie.getRotation())
    {
      case (int) Direction.EAST:
        animationSuper = superEast;
        break;
      case (int) Direction.NORTH:
        animationSuper = superSouth;
        break;
      case (int) Direction.SOUTH:
        animationSuper = superNorth;
        break;
      case (int) Direction.WEST:
        animationSuper = superWest;
        break;
    }
  }


}
