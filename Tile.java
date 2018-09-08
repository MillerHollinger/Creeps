//This is one block of the Creeps world.
//It has a level and contents.
import java.util.ArrayList;
public class Tile
{
   public String contents;
   
   //[!]It'd be better to put these variables in a separate class.
   public int metadata;
   public ArrayList<Creep> forSale;
   
   //Empty constructor.
   public Tile(){}
   
   //Full constructor.
   public Tile(String cont)
   {
      contents = cont;
      metadata = 0;
      forSale = new ArrayList<Creep>();
   }
   
   public String toString()
   {
      return contents;
   }
}