package model;


import java.util.*;

public class AStarFindStrategy implements FindStrategy
{
  List<Tile> path;

  @Override
  public void find (House house, Tile start, Tile end)
  {
    Queue<TilePriority> frontier = new PriorityQueue<>();
    List<Tile> visited = new ArrayList<>();
    Map<Tile, Tile> cameFrom = new HashMap<>();
    Map<Tile, Integer> costSoFar = new HashMap<>();

    frontier.add(new TilePriority(start, 0));
    cameFrom.put(start, null);
    costSoFar.put(start, 0);

    while (frontier.size() > 0)
    {
      Tile current = frontier.poll().getTile();
      visited.add(current);

      if (current.equals(end))
      {
        break;
      }

      for (Tile next : house.neighbors(current))
      {
        if (visited.contains(next))
        {
          continue;
        }

        int new_cost = costSoFar.get(current) + next.getCost();
        if (!costSoFar.containsKey(next) || (new_cost < costSoFar.get(next)))
        {
          costSoFar.put(next, new_cost);
          int priority = (int) (new_cost+(Math.abs(end.getY()-next.getY())+Math.abs(end.getX()-next.getX())));
          frontier.add(new TilePriority(next, priority));
          cameFrom.put(next, current);
        }
      }
    }

    path = reconstructPath(cameFrom, start, end);
  }

  private List<Tile> reconstructPath (Map<Tile, Tile> cameFrom, Tile start, Tile end)
  {
    Tile current = end;
    List<Tile> path = new ArrayList<>();
    path.add(current);

    while (current != start)
    {
      current = cameFrom.get(current);
      if (current != null)
      {
        path.add(current);
      }
    }

    Collections.reverse(path);
    return path;
  }

  @Override
  public List getPath ()
  {
    return path;
  }

  private class TilePriority implements Comparable<TilePriority>
  {
    private Tile tile;
    private int cost;

    public TilePriority (Tile tile, int cost)
    {
      this.tile = tile;
      this.cost = cost;
    }

    public Tile getTile()
    {
      return tile;
    }

    public int getCost()
    {
      return cost;
    }

    public int compareTo (TilePriority obj)
    {
      if (this.getCost() < obj.getCost())
      {
        return -1;
      }
      else if (this.getCost() == obj.getCost())
      {
        return 0;
      }
      else
      {
        return 1;
      }
    }
  }
}