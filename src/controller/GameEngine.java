package controller;

import model.GameOptions;
import model.House;
import model.Player;
import view.*;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * Main Controller. Its job is to delegate actions to other controllers and
 * to send data to the renders as needed
 */
public class GameEngine implements KeyListener, MouseInputListener, GameController
{
  private final MenuRenderer menuRenderer;
  private House house;
  private Player player;
  private Renderer houseRenderer;
  private PlayerController controller;
  private ZombieController zombieController;
  private SuperZombieController ss;
  private MenuController menuController;
  private GameOptions options;

  private Renderer playerRenderer;
  private Renderer zombieRenderer;
  private Point2D dragFrom;
  private Converter converter;

  private boolean moving = false;
  private boolean upPressed = false;
  private boolean leftPressed = false;
  private boolean downPressed = false;
  private boolean rightPressed = false;
  private boolean pKeyPressed = false;

  private Rectangle2D viewPort;
  private boolean DEBUG = false;

  public GameEngine ()
  {
    player = new Player();
    house = new House(player);
    controller = new PlayerController(house);
    zombieController = new ZombieController(house);
    MenuController.setActive(true);
    options = new GameOptions();
    menuController = new MenuController(house, options);
    house.generateRandomHouse();
    ss = new SuperZombieController(house, house.getSuperZombie());
    playerRenderer = new PlayerRenderer(player);
    zombieRenderer = new ZombieRenderer(house);
    converter = new Converter(house);
    houseRenderer = new HouseRenderer(house, converter);
    menuRenderer = new MenuRenderer(options);

  }

  @Override
  public void update(float deltaTime)
  {
    if (MenuController.isActive())
    {
      menuController.update(deltaTime);
      return;
    }
    CombustibleController.getInstance().update(deltaTime);
    controller.update(deltaTime);
    zombieController.update(deltaTime);
    ss.update(deltaTime);

    if (pKeyPressed) controller.trapKeyPressed();
    if (!pKeyPressed) controller.trapKeyReleased();

    // Ordinal direction
    if (upPressed && rightPressed) controller.moveUpRight();
    else if (upPressed && leftPressed) controller.moveUpLeft();
    else if (downPressed && rightPressed) controller.moveDownRight();
    else if (downPressed && leftPressed) controller.moveDownLeft();

    // Cardinal directions
    else if (upPressed) controller.moveUp();
    else if (rightPressed) controller.moveRight();
    else if (downPressed) controller.moveDown();
    else if (leftPressed)controller.moveLeft();

    if (!moving) controller.idle();
  }

  public AffineTransform getTransform()
  {
    AffineTransform at = new AffineTransform();
    double shiftX = -player.getCurrentX() * 60;
    double shiftY = -player.getCurrentY() * 60;
//    at.scale(1 / 1.78, 1 / 1.78);
    at.translate(shiftX, shiftY);
    return at;
  }

  @Override
  public void render (Graphics2D graphics)
  {
    if (MenuController.isActive())
    {
      menuRenderer.render(graphics);
      return;
    }
    graphics.setTransform(getTransform());
    //houseRenderer.translateAbsolute(player.getCurrentX(), player.getCurrentY
      //    ());
    houseRenderer.render(graphics);
    playerRenderer.render(graphics);
    zombieRenderer.render(graphics);
  }

  @Override
  public void keyTyped (KeyEvent e) { }

  @Override
  public void keyPressed (KeyEvent e)
  {
    switch (e.getKeyCode())
    {
      // menu screen
      case KeyEvent.VK_SPACE:
        if (MenuController.isActive())
        {
          if (options.getState() == GameOptions.GAME_STATE.RESTART)
          {
            house.reset();
            MenuController.setActive(false);
          }
          else if (options.getState() == GameOptions.GAME_STATE.EXIT)
          {
            System.exit(0);
          }
          else if (options.getState() == GameOptions.GAME_STATE.LEVEL1)
          {
            house.generateRandomHouse(options.getState());
            MenuController.setActive(false);
          }
          else if (options.getState() == GameOptions.GAME_STATE.LEVEL2)
          {
            house.generateRandomHouse(options.getState());
            MenuController.setActive(false);
          }
          else if (options.getState() == GameOptions.GAME_STATE.LEVEL3)
          {
            house.generateRandomHouse(options.getState());
            MenuController.setActive(false);
          }
          else if (options.getState() == GameOptions.GAME_STATE.LEVEL4)
          {
            house.generateRandomHouse(options.getState());
            MenuController.setActive(false);
          }
          else if (options.getState() == GameOptions.GAME_STATE.LEVEL5)
          {
            house.generateRandomHouse(options.getState());
            MenuController.setActive(false);
          }
          else
          {
            MenuController.toggleActive();
          }
        }
        else
        {
          MenuController.toggleActive();

        }
        break;
      // Player movement direction
      case KeyEvent.VK_UP:
        upPressed = true;
        moving = true;
        break;
      case KeyEvent.VK_W:
        upPressed = true;
        moving = true;
        break;
      case KeyEvent.VK_DOWN:
        downPressed = true;
        moving = true;
        break;
      case KeyEvent.VK_S:
        downPressed = true;
        moving = true;
        break;
      case KeyEvent.VK_LEFT:
        leftPressed = true;
        moving = true;
        break;
      case KeyEvent.VK_A:
        leftPressed = true;
        moving = true;
        break;
      case KeyEvent.VK_RIGHT:
        rightPressed = true;
        moving = true;
        break;
      case KeyEvent.VK_D:
        rightPressed = true;
        moving = true;
        break;

      // Picking up/setting traps
      case KeyEvent.VK_P:
        pKeyPressed = true;
        break;

      // Running
      case KeyEvent.VK_R:
        if (moving)
        {
          if (DEBUG) System.out.println("Running");
          // Character can only run if they're actually moving
          controller.run();
        }
        break;
      default:
        moving = false;
        controller.idle();
    }

  }

  @Override
  public void keyReleased (KeyEvent e)
  {
    switch (e.getKeyCode())
    {
      // Player movement/directions
      case KeyEvent.VK_UP:
        upPressed = false;
        moving = false;
        break;
      case KeyEvent.VK_W:
        upPressed = false;
        moving = false;
        break;
      case KeyEvent.VK_DOWN:
        downPressed = false;
        moving = false;
        break;
      case KeyEvent.VK_S:
        downPressed = false;
        break;
      case KeyEvent.VK_LEFT:
        if (MenuController.isActive())
        {
          menuController.next();
          return;
        }
        leftPressed = false;
        moving = false;
        break;
      case KeyEvent.VK_A:
        leftPressed = false;
        moving = false;
        break;
      case KeyEvent.VK_RIGHT:
        if (MenuController.isActive())
        {
          menuController.next();
          return;
        }
        rightPressed = false;
        moving = false;
        break;
      case KeyEvent.VK_D:
        rightPressed = false;
        moving = false;
        break;

      // Player actions
      case KeyEvent.VK_R:
        if (moving)
        {
          if (DEBUG) System.out.println("Not running");
          controller.walk();
        }
        break;
      case KeyEvent.VK_P:
        pKeyPressed = false;
//        controller.trapInteraction();
        break;
      default:
        moving = false;
        controller.idle();

    }
  }

  @Override
  public void mouseClicked (MouseEvent e)
  {
    System.out.println(converter.pointToHouseTile(e.getPoint()));
  }

  @Override
  public void mousePressed (MouseEvent e)
  {
    dragFrom = e.getPoint();
  }

  @Override
  public void mouseReleased (MouseEvent e)
  {

  }

  @Override
  public void mouseEntered (MouseEvent e)
  {

  }

  @Override
  public void mouseExited (MouseEvent e)
  {

  }

  @Override
  public void mouseDragged (MouseEvent e)
  {
    double dx = -(e.getPoint().getX() - dragFrom.getX());
    double dy = -(e.getPoint().getY() - dragFrom.getY());
    dragFrom = e.getPoint();
  }

  @Override
  public void mouseMoved (MouseEvent e) { }

  public void setViewPort (Rectangle2D rectangle)
  {
    this.viewPort = rectangle;
     houseRenderer.setViewBounds(rectangle);

//    houseRenderer.translateAbsolute();
  }
}
