//Each attack has
//STR : Strength. A percent of the creep's PWR that it will do.
//EXH : Exhaustion. How much energy it uses.
//SPD : Speed. How fast the attack goes, and the chance of hitting.
import java.util.Random;
public class Attack
{
   //Stats. See above numbers.
   public int str;
   public int exh;
   public int spd;
   
   //The attack's level.
   public int level;
   
   //Rarity expressed as a number.
   public int rarity; //1-6
   
   //What the attack is called.
   public String name;
   
   //Loads in a save string to recreate this attack. Returns if the load was a success.
   public boolean load(String in)
   {
      try
      {
         in = in.substring(in.indexOf("/") + 1, in.length());
         name = in.substring(0, in.indexOf("/"));
         
         in = in.substring(in.indexOf("/") + 1, in.length());
         rarity = Integer.parseInt(in.substring(0, in.indexOf("/")));
         
         in = in.substring(in.indexOf("/") + 1, in.length());
         level = Integer.parseInt(in.substring(0, in.indexOf("/")));
         
         in = in.substring(in.indexOf("/") + 1, in.length());
         str = Integer.parseInt(in.substring(0, in.indexOf("/")));
         
         in = in.substring(in.indexOf("/") + 1, in.length());
         exh = Integer.parseInt(in.substring(0, in.indexOf("/")));
         
         in = in.substring(in.indexOf("/") + 1, in.length());
         spd = Integer.parseInt(in.substring(0, in.indexOf("/")));
         return true;
      }
      catch (Exception e)
      {
         return false;
      }
   }
   
   //The save data for this Attack.
   public String getSave()
   {
      return "$ATTACK/"+name+"/"+rarity+"/"+level+"/"+str+"/"+exh+"/"+spd+"/";
   }
   
   //Empty constructor.
   public Attack(){}
   
   //Stat-only constructor.
   public Attack(int st, int ex, int sp, int rar, int lvl)
   {
      str = st;
      exh = ex;
      spd = sp;
      rarity = rar;
      level = lvl;
      genName();
   }
   
   //Generates a random attack of the given level.
   public Attack(int lv, int creepRar)
   {
      level = lv;
      genRarity(creepRar);
      genStats(lv, rarity);
      genName();
   }
   
   //Generate stats.
   public void genStats(int lv, int rar)
   {
      Random rn = new Random();
      str = rn.nextInt(lv + (rar/2)) + lv + rar;
      exh = str * (rn.nextInt(lv) + 1) + 1;
      spd = exh - rn.nextInt(str + 1) + rar;
   }
   
   //Generate rarity.
   public void genRarity(int rar)
   {
      Random rn = new Random();
      if (rar < 6)
         if (rn.nextInt(10) <= 8)
            rarity = rar;
         else
            rarity = rar + 1;
      else
         rarity = 6;
   }
   
   //Generate name.
   public void genName()
   {
      Random rn = new Random();
      String[] first = 
         {"Terra","Super","Mega","Instant","Fast","Hyper","Iron","Poison","Venom","Light","Shadow","Volt",
         "Lightning","Flame","Jet","Jolt","Water","Hydra","Freeze","Magic","Flying","Spinning","Rocket",
         "Time","Blast","Backwards","Silent","Quick","Electric","Water","Knockout","Doom","Light","Dark", 
         "Stealthy"};
      String[] second =
         {"Strike","Blast","Punch","Flash","Slash","Burst","Star","Crash","Tackle","Fist","Kick","Dash","Roll",
         "Rumble","Stab","Explosion","Dash","Blast","Uppercut","Slap","Slice","Whip","Gash","Bash", "Ball", "Spike",
         "Burst","Lunge","Spin","Flip", "Lance", "Jab", "Beam"};
      String rarMod = "{ERROR}";
      switch (rarity)
      {
         case 1:
            rarMod = "Lesser";
            break;
         case 2:
            rarMod = "Mediate";
            break;
         case 3:
            rarMod = "Greater";
            break;
         case 4:
            rarMod = "Superior";
            break;
         case 5:
            rarMod = "Epic";
            break;
         case 6:
            rarMod = "Ultimate";
            break;
      }
      name = rarMod + " " + first[rn.nextInt(first.length)] + " " + second[rn.nextInt(second.length)];
   }
   
   //toString().
   public String toString()
   {
      return name + "- STR"+str+" EXH"+exh+" SPD"+spd; 
   }
}