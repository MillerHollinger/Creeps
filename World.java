//This is the world in the game Creeps.
import java.util.Random;
public class World
{
   //The world, comprised of 25,000 tiles.
   public Tile[][] world = new Tile[300][300];
   
   //Empty constructor. This is the only constructor because there are no arguments. Randomly generates the world.
   public World()
   {
      for (int i = 0; i < world.length; i++)
         for (int j = 0; j < world[0].length; j++)
         {
            //Default is plains.
            world[i][j] = new Tile(".");
            //Add interesting things that can appear more than once.
            if (checkChance(8))
               world[i][j] = new Tile("#");
            else if (checkChance(150))
               world[i][j] = new Tile("^");
            else if (checkChance(70))
               world[i][j] = new Tile("@");
            else if (checkChance(1))
               world[i][j] = new Tile("!");
            else if (checkChance(75))
               world[i][j] = new Tile("&");
            else if (checkChance(20))
               world[i][j] = new Tile("%");
         }
      //Add in battle towers.
      world[randTo(300)][randTo(300)] = new Tile("1");
      world[randTo(300)][randTo(300)] = new Tile("2");
      world[randTo(300)][randTo(300)] = new Tile("3");
      world[randTo(300)][randTo(300)] = new Tile("4");
      world[randTo(300)][randTo(300)] = new Tile("5");
      world[randTo(300)][randTo(300)] = new Tile("6");
      world[randTo(300)][randTo(300)] = new Tile("7");
      world[randTo(300)][randTo(300)] = new Tile("8");
      world[149][149] = new Tile("9"); // 9 is always in the center.
      world[49][49].contents = ".";
   }
   
   //Gets a random number up to the given number. Merely shorthand for rn.nextInt();
   private int randTo(int pick)
   {
      Random rn = new Random();
      return rn.nextInt(pick);
   }
   
   //Prints out the local area -- 5 blocks in each direction, to make a 11x11 square.
   public void printLocal (int row, int col)
   {
      for (int i = row - 5; i <= row + 5; i++)
      {
         for (int j = col - 5; j <= col + 5; j++)
         {
            if (!(i == row && j == col))
               try
               {
                  System.out.print(world[i][j]);
               }
               catch (Exception e)
               {
                  System.out.print("|");
               }
            else
               System.out.print("+");
         }
         System.out.println();
      }
   }
   
   //Checks for a ofThousand/1000 chance.
   private boolean checkChance(int ofThousand)
   {
      Random rn = new Random();
      return (rn.nextInt(1000) <= ofThousand-1);
   }
}