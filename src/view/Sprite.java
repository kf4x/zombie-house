package view;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Sprite
{
  private static final int TILE_SIZE = 40;

  /**
   * Get the sprite
   * @param fileName name of the image file
   * @param x this is a multiplier in x direction i.e. x*TILE_SIZE(40)
   * @param y this is a multiplier in y direction i.e. y*TILE_SIZE(40)
   * @return image cropped to sprite
   */
  public static BufferedImage getSprite (String fileName, int x, int y)
  {
    BufferedImage image = loadSprite(fileName);
    return image.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
                             TILE_SIZE);
  }

  private static BufferedImage loadSprite (String file)
  {
    BufferedImage sprite = null;

    try
    {
       sprite = ImageIO.read(new File("resources/" + file));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return sprite;
  }
}