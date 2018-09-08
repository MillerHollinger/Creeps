//This is a Creep for the game Creeps.
//Stats:
//LV: Level. Higher means harder to level up and more powerful.
//Rarity:           Lesser Mediate Greater Superior Epic Ultimate (L., M-, G/, S!, E^, U@)
//     . Plains:     80%  | 15%   |  5%   |
//     ^ Mountains:  60%  | 25%   | 10%   |  5%    |    
//     @ Cave:       40%  | 35%   | 15%   | 10%    |    
//     # Town:                    | 90%   | 10%    |
//     % Dungeon:                 | 50%   | 40%    | 10%|
//     ! Danger:                                   | 60%| 40%    |
//HP: Health. Go longer without dying.
//PWR: Attack power. Hit harder.
//DEF: Defense. Take less damage.
//SPC: Special power. Hit harder with the special attack.
//ENG: Energy. Stronger attacks use this. When it runs out, the creep's health is set to 0.
//Attacks: Three.
import java.util.Random;
public class Creep
{
   public String name;
   public int rarity;
   public int level;
   public int hp;
   public int maxHP;
   public int pwr;
   public int def;
   public int eng;
   public int maxENG;
   public Attack[] attacks = new Attack[3];
   
   //Loads in data and returns if it was successful.
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
         maxHP = Integer.parseInt(in.substring(0, in.indexOf("/")));
         hp = maxHP;
         
         in = in.substring(in.indexOf("/") + 1, in.length());
         pwr = Integer.parseInt(in.substring(0, in.indexOf("/")));
         
         in = in.substring(in.indexOf("/") + 1, in.length());
         def = Integer.parseInt(in.substring(0, in.indexOf("/")));
         
         in = in.substring(in.indexOf("/") + 1, in.length());
         maxENG = Integer.parseInt(in.substring(0, in.indexOf("/")));
         eng = maxENG;
         
         in = in.substring(in.indexOf("[") + 1, in.length());
         String a0 = in.substring(0, in.indexOf("]"));
         
         in = in.substring(in.indexOf("[") + 1, in.length());
         String a1 = in.substring(0, in.indexOf("]"));
         
         in = in.substring(in.indexOf("[") + 1, in.length());
         String a2 = in.substring(0, in.indexOf("]"));
         
         return attacks[0].load(a0) && attacks[1].load(a1) && attacks[2].load(a2);
      }
      catch (Exception e)
      {
         return false;
      }
   }
   
   //Returns the savedata for this attack.
   public String getSave()
   {
      return "$CREEP/"+name+"/"+rarity+"/"+level+"/"+maxHP+"/"+pwr+"/"+def+"/"+maxENG+
         "/A0["+attacks[0].getSave()+"]/A1["+attacks[1].getSave()+"]/A2["+attacks[2].getSave()+"]/";
   }
   
   //Empty constructor
   public Creep(){}
   
   //Randomly generates a Creep based on the player's level and location.
   public Creep(String loc, int plyrLVL)
   {
      //Generate rarity.
      genRarity(loc);
      
      //Generate name.
      genName();
      
      //Generate stats and level.
      Random rn = new Random();
      if (loc.equals("C") || loc.equals("M") || loc.equals("#"))
         level = plyrLVL;
      else
         level = plyrLVL + rn.nextInt(3) - 1;
      if (level <= 0) level = 1;
      
      genStats();
      
      //Generate attacks.
      genAttacks();
      
   }
   
   public String rarString()
   {
      switch (rarity)
      {
         case 1:
            return "| RARITY: Lesser [1] \n";
         case 2:
            return "| RARITY: Mediate =2= \n";
         case 3:
            return "| RARITY: Greater +3+ \n";
         case 4:
            return "| RARITY: Superior }4{ \n";
         case 5:
            return "| RARITY: Epic -<5>- \n";
         case 6:
            return "| RARITY: Ultimate >!6!< \n";
      }
      return " | {ERROR} \n";
   }
   
   public String getCard()
   {
      String out = "";
      out += "#-----------------------------------------#\n";
      out += "| " + name + " | LV " + level + "\n";
      out += rarString();
      out += "#--------------------------------------------#\n";
      out += "| HP: "+hp+"/"+maxHP+"\n";
      out += "| ENG: "+eng+"/"+maxENG+"\n";
      out += "#--------------------------------------------#\n";
      out += "| PWR: "+pwr+"\n";
      out += "| DEF: "+def+"\n";
      out += "#--------------------------------------------#\n";
      out += "| "+attacks[0]+"\n";
      out += "| "+attacks[1]+"\n";
      out += "| "+attacks[2]+"\n";
      out += "#--------------------------------------------#\n";
      return out;
   }
   
   //Generates the attacks.
   public void genAttacks()
   {
      for (int i = 0; i < 3; i++)
         attacks[i] = new Attack(level, rarity);
   }
   
   //Generates the stats.
   public void genStats()
   {
      Random rn = new Random();
      maxHP = level * 7 + rn.nextInt(level * 3 + rarity) + 1;
      hp = maxHP;
      pwr = level + rn.nextInt(level / 3 + rarity) + 1;
      def = level / 2 + rn.nextInt(level / 2 + rarity) + 1;
      maxENG = hp * level + rn.nextInt(maxHP + level) + 1;
      eng = maxENG;
   }
   
   //Generates the name.
   public void genName()
   {
      //The name sections.
      String[] first = 
         {"Pha","Con","Ya","Sil","Gan","Illo","Apa","Dil","Bel","Xo","Kat","Cor","Sul","Eng","Uld","Uma","Eka","Sis","Ur","Pard","Sul","Kar","Bul","Ma","Tur","Ola","Sela","Terr","Yet","Ila","Dila","Amba","Usa","Mala","Kik"};
      String[] second = 
         {"man","al","an","est","he","de","ma","cor","del","abba","ja","a","e","i","o","u","vit","za","ka","ki","erin","opa","bes","tul","mac","gola","vol","vet","geva","jenu","jeri","caxi","turo","pon","xan"};
      String[] third = 
         {"ic","a","dum","ija","vun","ipa","na","t","la","iom","till","tela","ola","ra","sa","ba","io","uun","ca","ita","ium","iya","ista","un","eun","i","ji","li","quet","met","evo","con","fes","tur","ki"};
         
      Random rn = new Random();
      
      name = first[rn.nextInt(first.length)] + second[rn.nextInt(second.length)];
      
      if (rn.nextInt(2) == 0)
         name += third[rn.nextInt(third.length)];
   }
   
   //Generates the rarity.
   public void genRarity(String loc)
   {
      Random rn = new Random();
      int rnd = rn.nextInt(100)+1; //Random number 1 to 100.
      switch(loc)
      {
         case ".":
            if (rnd <= 80)
               rarity = 1;
            else if (rnd <= 95)
               rarity = 2;
            else
               rarity = 3;
            break;
         case "^":
            if (rnd <= 60)
               rarity = 1;
            else if (rnd <= 85)
               rarity = 2;
            else if (rnd <= 95)
               rarity = 3;
            else
               rarity = 4;
            break;
         case "@":
            if (rnd <= 40)
               rarity = 1;
            else if (rnd <= 75)
               rarity = 2;
            else if (rnd <= 90)
               rarity = 3;
            else
               rarity = 4;
            break;
         case "%":
            if (rnd <= 70)
               rarity = 4;
            else
               rarity = 5;
            break;
         case "!":
            rarity = 6;
            break;
         case "#":
            if (rnd <= 70)
               rarity = 4;
            else
               rarity = 5;
            break;
         case "&":
            if (rnd <= 50)
               rarity = 3;
            else
               rarity = 4;
            break;
         case "C":
            rarity = 5;
            break;
         case "M":
            rarity = 6;
            break;
      }
   }
}