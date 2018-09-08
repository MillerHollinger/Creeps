//This game is effecively randomly generated open-world pokemon
//Monsters are called Creeps (Game is called Creeps). You can hold three and store infinity.
//Creeps have LV, HP, PWR, DEF, SPC, and three attacks, one of which is special. They also have rarity. Rarer ones have higher stats (For their level)
//LV: Level. Higher means harder to level up and more powerful.
//HP: Health. Go longer without dying.
//PWR: Attack power. Hit harder.
//DEF: Defense. Take less damage.
//SPC: Special power. Hit harder with the special attack.
//Player has a level, which determines how powerful randomly encountered creeps are.
//Player also has money, which they use to buy new, better creeps.
//The world has several blocks.
// # : Town. Heal creeps, sell creeps, and buy creeps.
// 1 : (1 to 9)Battle Tower. Creeps equivalent of a gym. Player can't pass this level * 5 until they beat this tower.
// . : Plains. You may encounter a weak creep here.
// ^ : Hills. You may encounter a creep here.
// @ : Cave. You may encounter a strong creep here.
// % : Dungeon. There is a very strong creep here as well as loot. Once cleared, it turns into a cave.
// ! : Danger. There is an extremely powerful creep here.
// & : Battle. There is another collector here who will fight you if you pass through. Once cleared, turns into a plains.
// + : The player.

/*
TO DO
-Playtest

EXTRA CONTENT - Post-completion
-Add easter eggs in Town choices
-Special random events (Make use of "?" Tile)
-"Hyper Ultimate" creeps that have special names and pre-programmed stats/attacks
-Switch creeps during battle
-Towns are actual places -- have sections like the map
-Cities with more things to do / battle towers are cities?
-Evolve rarity of a creep with [S]
-Lightly encrypt savegame
-Three specially made quests that give the player a second thign to do aside beating the battle towers
*/
import java.util.*;
public class CreepsGame
{
   //The highest beaten battle tower. Player can be up to (battle tower * 5) + 5 level, beat tower 9 to win
   static int towerTop = 0;
   
   //The total amount of moves made by the player.
   static int totalTurns = 0;
   
   //The world.
   static World world = new World();
      
   //The player's location.
   static int row = 49;
   static int col = 49;
   
   //The last visited town, respawn location
   static int resRow = 49;
   static int resCol = 49;
   
   //The player's lvl and xp.
   static int pLVL = 1;
   static int pXP = 1000;
   
   //The player's money, called shine or [S].
   static int shine = 0;
   
   //How many turns have happened since the last Town stock refresh.
   static int turns = 0;
      
   //Set to FALSE when the player quits.
   static boolean playing = true;
      
   //Creep storage and Creeps traveling along.
   static ArrayList<Creep> storage = new ArrayList<Creep>();
   static ArrayList<Creep> creeps = new ArrayList<Creep>();
      
   public static void main(String[] args)
   {
      Random rn = new Random();
      
      println("C--------------------------C");
      println("| ### ###  ### ### ### ### |");                                                                     
      println("| #   # #  ##  ##  # # #   |");                                                             
      println("| #   ###  #   #   ###  ## |");                                        
      println("| ### #  # ### ### #   ### |");                                                                      
      println("C-RANDOM--COLLECTING--GAME-C");
      println("");
      println("   CREEPS - An adventure by Miller Hollinger");
      
      mainMenu();
      
      println("Here are your creeps:");
      for (int i = 0; i < 3; i++)
      {
         println(creeps.get(i).getCard());
         nextLine();
      }
      println("Let's enter the Creep world.");
      nextLine();
      
      while(playing)
      {
         //Show world.
         world.printLocal(row, col);
         println("\nEnter a direction to move: WASD");
         String dir = userString(); //The user's direction of choice.
         while (!(dir.equalsIgnoreCase("W") || dir.equalsIgnoreCase("A") || dir.equalsIgnoreCase("S") || dir.equalsIgnoreCase("D")))
         {
            println("Enter WASD. Try again.");
            dir = userString();
         }
         turns++;
         totalTurns++;
         switch (dir)
         {
            case "W":
            case "w":
               if (row > 0)
                  row--;
               break;
            case "A":
            case "a":
               if (col > 0)
                  col--;
               break;
            case "S":
            case "s":
               if (row < 299)
                  row++;
               break;
            case "D":
            case "d":
               if (col < 299)
                  col++;
               break;
         }
         
         //Do something based on location.
         switch (world.world[row][col].contents)
         {
            case ".": //Plains: 1/10 battle chance
               if (rn.nextInt(10) == 0)
                  doBattle(".",true);
               break;
            case "^": //Hills: 1/3 battle chance
               if (rn.nextInt(3) == 0)
                  doBattle("^",true);
               break;
            case "@": //Caves: 1/2 battle chance
               if (rn.nextInt(2) == 0)
                  doBattle("@",true);
               break;
            case "%": //Dungeon: Guaranteed battle
               doBattle("%",true);
               world.world[row][col].contents = "@";
               break;
            case "!": //Danger: Guaranteed battle
               doBattle("!",true);
               world.world[row][col].contents = "^";
               break;
            case "#": //Town. Runs town().
               town();
               break;
            case "&":
               if (doBattle("&",false))
               {
                  int reward = pLVL + 5;
                  println("Victory! You are awarded " + reward + "[S].");
                  nextLine();
                  shine += reward;
                  world.world[row][col].contents = ".";
               }
               break;
            default:
               println("WARNING! You are entering the Circuit " +world.world[row][col].contents + " Battle Tower!");
               println("You will have to face the Challenger and the Master to beat the tower.");
               println("Are you sure you want to challenge this tower? (Y/N)");
               if (yes())
               {
                  println("You're challenging the Circuit " + world.world[row][col].contents + " Battle Tower.");
                  println("Remember -- you have to beat the tower in one go! No leaving after the first battle!");
                  println("First, face this tower's Challenger to prove yourself worthy of battling the Master.");
                  nextLine();
                  //Challenger battle
                  if (doBattle("C", false))
                  {
                     println("Challenger: Well done, collector. The master waits.");
                     println("Challenger: Now you must challenge this tower's Master!");
                     nextLine();
                     if (doBattle("M", false))
                     {
                        if (Integer.parseInt(world.world[row][col].contents) > towerTop)
                        {
                           towerTop = Integer.parseInt(world.world[row][col].contents);
                           println("Master: You have reached new heights.");
                           println("Master: Finally, you may gain power up to level " + (towerTop * 5 + 5) + ".");
                           println("Master: Find the next Battle Tower to increase your potential.");
                        }
                        else
                        {
                           println("Master: Honestly, your victory was assured, as you have beaten stronger towers.");
                           println("Master: But, you have still proven yourself. Good job.");
                        }
                        
                        nextLine();
                        println("You beat the Tower! You gain some rewards!");
                        println("You earned " + (Integer.parseInt(world.world[row][col].contents) * 30) + " XP!");
                        pXP += Integer.parseInt(world.world[row][col].contents) * 30;
                        println("You earned " + (Integer.parseInt(world.world[row][col].contents) * 40) + "[S]!");
                        shine += Integer.parseInt(world.world[row][col].contents) * 40;
                        
                        tryLevelUp();
                        
                        world.world[row][col].contents = ".";
                        
                        if (towerTop == 9)
                           win();
                     }
                     else
                        println("Master: You have come far, but not far enough. Return when your power has grown.");
                  }
                  else
                     println("Challenger: You are not prepared to challenge the Master. Come back when you are worthy.");
               }
               else
                  println("You walk past the tower. Return when you are ready to fight.");
               break;
         }
      }
   }
   
   //Handles town interactions.
   public static void town()
   {
      println("You enter a town.");
      println("You'll run back here if you get into trouble.");
      nextLine();
      resRow = row;
      resCol = col;
      while(true)
      {
         println("Welcome to the Town.");
         println("What would you like to do?");
         println("1 : Heal and Save");
         println("2 : Buy Creeps");
         println("3 : Sell Creeps");
         println("4 : Open Storage");
         println("5 : Exchange Creeps");
         println("6 : Get Directions");
         println("7 : Leave");
         switch (userInt())
         {
         //Heal------------------------------------
            case 1:
               println("Your creeps are being healed.");
               for (int i = 0; i < storage.size(); i++) //Heal storage
               {
                  storage.get(i).hp = storage.get(i).maxHP;
                  storage.get(i).eng = storage.get(i).maxENG;
               }
               for (int i = 0; i < creeps.size(); i++) //Heal on-hand
               {
                  creeps.get(i).hp = creeps.get(i).maxHP;
                  creeps.get(i).eng = creeps.get(i).maxENG;
               }
               println("Healing complete. Your creeps are ready for action.");   
               println("Show save data? (Y/N)");
               if(yes())
               {
                  if (creeps.size() == 3)
                  {
                     if (storage.size() == 0)
                     {
                        println("Fetching save. Please wait.");
                        println(getSave());
                     }
                     else
                     {
                        println("Your storage must be empty to save.");
                        println("Sell all the creeps in your storage and then save.");
                     }
                  }
                  else
                  {
                     println("You must have 3 creeps equipped to save.");
                     println("Equip three creeps and then save.");
                  }
               }
               else
                  println("Continuing.");            
               break;
         //Buy Creeps------------------------------------
            case 2:
               shop(true);
               break;
         //Sell Creeps------------------------------------
            case 3:
               if (creeps.size() == 3)
               {
                  println("Sell all stored creeps? (Y/N)");
                  if (yes())
                  {
                     println("Selling, please wait.");
                     while (storage.size() > 0)
                     {
                        Creep remv = storage.remove(0);
                        shine += remv.level * remv.rarity / 3 + 1;
                     }
                     println("Sold. Your wallet now has " + shine + "[S].");
                  }
               }
               else
                  println("You're not allowed to sell creeps unless your party is full.");
               break;
         //See Storage------------------------------------
            case 4:
               println("You have " + storage.size() + " creeps stored.");
               sort(); //sort the storage
               
               if (storage.size() > 0)
               {
                  println("Showing storage. Display entire cards? (Y/N)");
                  boolean yN = yes();
               
                  println("");
                  for (int i = 0; i < storage.size(); i++)
                     if (yN)
                        println(storage.get(i).getCard());
                     else
                        print(storage.get(i).name + " | " + storage.get(i).level + " " + storage.get(i).rarString());
               }
               else //0 creeps in storage 
               {
                  println("There is no storage to see. Go battle wild Creeps to get some!");
               }
               break;
         //Exchange Creeps------------------------------------
            case 5:
               while(creeps.size() > 0) //Move all creeps to storage for exchange
                  storage.add(creeps.remove(0));
                  
               println("Showing storage.");
               sort();
               nextLine();
               for (int i = 0; i < storage.size(); i++) //Print the storage
               {
                  println("#" + (i + 1) + "#");
                  println(storage.get(i).getCard());
               }
               
               println("Enter in the number of the creep to add. You can have 3."); //Add 3 creeps
               for (int i = 0; i < 3; i++)
               {
                  println("Enter the next Creep to add.");
                  int pick = userInt();
                  if (pick <= storage.size() && pick > 0) //Good number
                  {
                     creeps.add(storage.remove(pick-1));
                     println("Creep added.");
                     nextLine();
                     for (int j = 0; j < storage.size(); j++)
                     {
                        println("#" + (j + 1) + "#");
                        println(storage.get(j).getCard());
                     }
                  }
                  else //Bad number
                  {
                     println("Number is invalid. Try again.");
                     i--;
                  }
               }
               println("Roster filled. Exchange complete.");
               break;
         //Directions to next tower------------------------------------
            case 6:
               println("You want to go to the next battle tower?");
               
               for (int i = 0; i < world.world.length; i++)
               {
                  for (int j = 0; j < world.world.length; j++)
                  {
                     try
                     {
                        if (Integer.parseInt(world.world[i][j].contents) == towerTop + 1)
                        {
                           print("The one you need to go to, Circuit " + (towerTop + 1) + ", is to the ");
                           if (i > row)
                              print("south");
                           else
                              print("north");
                        
                           if (j > col)
                              println("east.");
                           else
                              println("west.");
                        
                           if (Math.abs(row - i) > 300 || Math.abs(col - j) > 300)
                           {
                              println("Many days will pass by the time you get there.");
                              println("[Over 300 blocks away]");
                           }
                           else if (Math.abs(row - i) > 200 || Math.abs(col - j) > 200)
                           {
                              println("Gosh, you'll be traveling a huge distance.");
                              println("[Over 200 blocks away]");
                           }
                           else if (Math.abs(row - i) > 100 || Math.abs(col - j) > 100)
                           {
                              println("It's very, very far away. Good luck on your travels.");
                              println("[Over 100 blocks away]");
                           }
                           else if (Math.abs(row - i) > 80 || Math.abs(col - j) > 80)
                           {
                              println("It's rather far away. You should buy some creeps to help on your journey.");
                              println("[80-100 blocks away]");
                           }
                           else if (Math.abs(row - i) > 70 || Math.abs(col - j) > 70)
                           {
                              println("It's a good distance away. Thanks for coming to our town.");
                              println("[70-80 blocks away]");
                           }
                           else if (Math.abs(row - i) > 60 || Math.abs(col - j) > 60)
                           {
                              println("You'll be going on a good hike. Be careful out there.");
                              println("[60-70 blocks away]");
                           }
                           else if (Math.abs(row - i) > 50 || Math.abs(col - j) > 50)
                           {
                              println("The tower isn't that far. Stay out of caves and keep safe.");
                              println("[50-60 blocks away]");
                           }
                           else if (Math.abs(row - i) > 40 || Math.abs(col - j) > 40)
                           {
                              println("I visit someone who lives there from time to time. It's a good walk.");
                              println("[40-50 blocks away]");
                           }
                           else if (Math.abs(row - i) > 30 || Math.abs(col - j) > 30)
                           {
                              println("You'll be there and back in a jiffy. Good luck on that tower.");
                              println("[30-40 blocks away]");
                           }
                           else if (Math.abs(row - i) > 20 || Math.abs(col - j) > 20)
                           {
                              println("It's right around the corner. Come back soon.");
                              println("[20-30 blocks away]");
                           }
                           else if (Math.abs(row - i) > 10 || Math.abs(col - j) > 10)
                           {
                              println("I can see it from here. It's pretty tall. Have fun on those steps.");
                              println("[10-20 blocks away]");
                           }
                           else
                           {
                              println("You shouldn't really need directions, it's in plain view.");
                              println("[Less than 10 blocks away]");
                           }
                        
                        }
                     }
                     catch (Exception e){}
                  }
               }
               break;
         //Leave------------------------------------
            case 7:
               println("Are you sure you want to leave the town? (Y/N)");
               if (yes()) //Yes, leave
               {
                  println("Bye. Thanks for stopping in.");
                  nextLine();
                  println("You head back out into the world.");
                  return;
               }
               else //Stay
                  println("Stay a bit longer, then.");
               break;
            default:
               println("That option does not exist.");
               break;
         }
         nextLine();
      }
   }
   
   //Does the shop. The boolean tells if this is the first time checking the shop (if this isn't shop() running shop())
   public static void shop(boolean orig)
   {
      if (orig)
      {
         if (world.world[row][col].metadata <= 0) //Reset stock if the user hasn't checked in for a while (>= 36 turns)
         {
            world.world[row][col].forSale = new ArrayList<Creep>();
            world.world[row][col].forSale.add(new Creep("#", pLVL));
            world.world[row][col].forSale.add(new Creep("#", pLVL + 1));
            world.world[row][col].forSale.add(new Creep("#", pLVL + 2));
            world.world[row][col].metadata = 36; //Check
            println("We have some new creeps in stock, check them out.");
         }
         else
         {
            world.world[row][col].metadata -= turns;
            turns = 0;
            println("Welcome back.");
         }
      }
      if (world.world[row][col].forSale.size() > 0)
      {
         println("You can buy one of the following creeps:");
         nextLine();
         for (int i = 0; i < world.world[row][col].forSale.size(); i++)
         {
            println(world.world[row][col].forSale.get(i).getCard());
            println("PRICE FOR THIS CREEP: " + (world.world[row][col].forSale.get(i).level * world.world[row][col].forSale.get(i).rarity * 5 / 3) + "[S]");
            println("---------------------------------------------");
         }
         println("Your wallet has: " + shine + "[S]");
         println("Enter (1 2 3) to buy that creep, or (Other) to leave.");
         int shop = userInt();
         if (shop > 0 && shop <= world.world[row][col].forSale.size())
         {
                  //Check price.
            if (shine >= world.world[row][col].forSale.get(shop-1).level * world.world[row][col].forSale.get(shop-1).rarity * 5 / 3)
            {
               println("Thank you for your purchase of this " + world.world[row][col].forSale.get(shop-1).name + ".");
               shine -= world.world[row][col].forSale.get(shop-1).level * world.world[row][col].forSale.get(shop-1).rarity * 5 / 3;
               storage.add(world.world[row][col].forSale.remove(shop-1));
               println("It's been sent to your personal storage.");
            }
            else
            {
               println("You can't afford that creep. Would you like to go back and see the others? (Y/N)");
               if (yes())    
                  shop(false);
               else
                  println("Come back in a while. We get new Creeps pretty often.");
            }     
         }
         else
         {
            println("Are you ready to leave? (Y/N)");
            if (yes())    
               println("Bye! If you ever want to upgrade your Creeps, remember us.");
            else
               shop(false);
         }
      }
      else
         println("We don't have any creeps in stock. Come back soon when we do.");
   }
   
   //Sort storage
   public static void sort()
   {
      ArrayList<Creep> fin = new ArrayList<Creep>();
      //Finds the lowest level and rarity
      int minLV = 51;
      
      
      while (storage.size() > 0)
      {
         minLV = 51;
         for (int i = 0; i < storage.size(); i++)
            if (storage.get(i).level < minLV)
               minLV = storage.get(i).level;
         
         for (int i = 0; i < storage.size(); i++)
            if (storage.get(i).level == minLV)
            {
               fin.add(storage.remove(i));
               i--;
            }
      }
      
      storage = fin;
   }
   
   //Tells the player they won.
   public static void win()
   {
      println("You have beaten the final tower. Circuit 9 is as high as it goes.");
      println("Good job! You're a Creep master.");
      nextLine();
      println("You beat Creeps in...");
      nextLine();
      println(totalTurns + " moves!");
      nextLine();
      println("The goal of any Collector is to defeat every Battle Tower as fast as possible.");
      println("Try playing again, but go faster! How quickly can you defeat the Battle Towers?");
      nextLine();
      println("You can keep playing. I'd suggest going to a town to save right now.");
      println("Level 50 is still the cap level, but you can still encounter creeps of a higher level than 50.");
      println("Thanks for playing Creeps. If you have any great ideas for updates, tell me, Miller Hollinger. Bye!");
      nextLine();
      //[!]GENERATE FINAL SCORE
      
      nextLine();
   }
   
   //Asks a yes or no question.
   public static boolean yes()
   {
      String in = "";
      while (true)
      {
         in = userString();
         if (in.equalsIgnoreCase("Y"))
            return true;
         if (in.equalsIgnoreCase("N"))
            return false;
         println("Enter Y for Yes or N for No.");
      }
      
   }
   
   //Do a battle in the specified location. Returns if it was won.
   public static boolean doBattle(String loc, boolean wild)
   {
      Random rn = new Random();
      
      Creep enemy = new Creep(loc, pLVL);
      
      if (wild)
         println("[!] A wild Creep jumps in front of you!");
      else if (!loc.equals("C") && !loc.equals("M")) //Not a battle tower (recognized as C for challenger or M for Master)
         println("[!] Another collector challenges you to a battle!");
      else if (loc.equals("C"))
      {
         println("[!] You must defeat the Challenger -- Prove yourself worthy of the Master!");
         enemy = new Creep(loc, Integer.parseInt(world.world[row][col].contents) * 5 + 1);
      }
      else
      {
         println("[!] This is it! Defeat the Master of this Battle Tower!");
         enemy = new Creep(loc, Integer.parseInt(world.world[row][col].contents) * 5 + 2);
      }
      
      if (creeps.size() != 0)
      {
         nextLine();
      
      //Battle loop.
         while (true)
         {
            println("X > ENEMY CREEP < X");
            println(enemy.getCard());
            println("");
            println("+ > READY CREEP < +");
            println(creeps.get(0).getCard());
            println("");
            println("#- (1 2 3): Attack | (0): Rest | (Other): Run -#");
         
            int pick = userInt();
         
         //Check for input
            if (pick >= 1 && pick <= 3) //Attack.
            {
            //Check if there is enough energy.
               if (creeps.get(0).attacks[pick-1].exh <= creeps.get(0).eng)
               {
                  creeps.get(0).eng -= creeps.get(0).attacks[pick-1].exh;
                  println(creeps.get(0).name + " uses " + creeps.get(0).attacks[pick-1].name + "!");
                  
               //Check for hit.
                  if (creeps.get(0).attacks[pick-1].spd > enemy.attacks[rn.nextInt(3)].spd || rn.nextInt(3) == 0)
                  {
                     int dmg = creeps.get(0).attacks[pick-1].str * creeps.get(0).pwr / 4 + 1;
                     println("Hit! The enemy " + enemy.name + " takes " + dmg + " damage!");
                     enemy.hp -= dmg;
                  }
                  else
                     println("The attack missed!");
                  nextLine();
               }
               else
               {
                  println(creeps.get(0).name + " is too tired! Use a turn to rest and regain energy!");
                  nextLine();
               }
            }
            else if (pick == 0) //Rest. Regain 10% of energy.
            {
               println("Your " + creeps.get(0).name + " backs up and rests. +" + (creeps.get(0).maxENG / 6 + creeps.get(0).def) + " Energy!");
               nextLine();
               creeps.get(0).eng += creeps.get(0).maxENG / 6 + creeps.get(0).def;
               if (creeps.get(0).eng > creeps.get(0).maxENG)
                  creeps.get(0).eng = creeps.get(0).maxENG; 
            }
            else //Attempt a run.
            {
               if (wild)
               {
                  println("You tried to run away...");
                  nextLine();
                  if (rn.nextInt(2) == 0)
                  {
                     println("The enemy " + enemy.name + " couldn't catch you. You escaped!");
                     nextLine();
                     return false;
                  }
                  else
                  {
                     println("But the enemy " + enemy.name + " corners you and attacks!");
                     nextLine();
                  }
               }
               else
               {
                  println("Tournament rules say no running from a collector-on-collector Creep battle!");
                  nextLine();
               }
            }
         
         //The enemy attacks.
         //Randomly pick an attack.
            int enmyAtk = rn.nextInt(3);
         //Check for energy and alive-ness.
            if (enemy.hp > 0)
            {
               if (enemy.eng > enemy.attacks[enmyAtk].exh)
               {
                  enemy.eng -= enemy.attacks[enmyAtk].exh;
                  println("The enemy " + enemy.name + " used " + enemy.attacks[enmyAtk].name + "!");
                  nextLine();
               
               //Check for hit.
                  if (enemy.attacks[enmyAtk].spd > creeps.get(0).attacks[rn.nextInt(3)].spd || rn.nextInt(4) == 0)
                  {
                     int dmg = enemy.attacks[enmyAtk].str * creeps.get(0).pwr / 4 + 1;
                     println("Your " + creeps.get(0).name + " is hit for " + dmg + " damage!");
                     creeps.get(0).hp -= dmg;
                     nextLine();
                  }
                  else
                  {
                     println("But your " + creeps.get(0).name + " got out of the way!");
                     nextLine();
                  }
               }
               else
               {
                  println("The enemy " + enemy.name + " rested to regain energy.");
                  enemy.eng += enemy.maxENG/6;
                  if (enemy.eng > enemy.maxENG)
                     enemy.eng = enemy.maxENG;
                  nextLine();
               }
            }
         //Check for creep KO.
            if (creeps.get(0).hp <= 0)
            {
               println("Argh! Your " + creeps.get(0).name + " was defeated!");
               println("It's been warped out to storage.");
               nextLine();
               storage.add(creeps.remove(0));
               if (creeps.size() == 0)
               {
                  println("You're out of Creeps! Oh no!!!");
                  println("You ran away as fast as you could!");
                  nextLine();
                  row = resRow;
                  col = resCol;
                  town(); 
                  return false;
               }
               else //Send out new creep.
               {
                  println("It's not over yet! To battle, " + creeps.get(0).name + "!");
                  println("Your " + creeps.get(0).name + " is ready to fight!");
                  println("You have " + creeps.size() + " more creeps.");
                  nextLine();
               }
            }
         
         //Win.
            if (enemy.hp <= 0)
            {
               println("KO! Good job, " + creeps.get(0).name + "!");
               if (wild)
               {
                  println("The defeated " + enemy.name + " was warped to storage. Cool!");
                  storage.add(enemy);
                  println("You now have " + storage.size() + " creeps in storage and " + creeps.size() + " with you.");
               }
            
               int earned = enemy.level;
               if (!wild) //Bonus XP.
                  earned *= 2;
               
               pXP += earned;
               println("You got " + earned + " XP! (" + pXP + "/" + pLVL * 15 + ")");
               tryLevelUp();
               
               println("Your " + creeps.get(0).name + " had its energy restored!");
               creeps.get(0).eng = creeps.get(0).maxENG;
               
               nextLine();
               tryLevelUp();
               
               return true;
            }
         }
      }
      else
      {
         println("But you can't fight! Run away!");
         nextLine();
         row = resRow;
         col = resCol;
         town();
      }
      return false;
   }
   
   //Tries to level up the user.
   public static void tryLevelUp()
   {
      while (pXP >= pLVL * 15)
      {
         if (pLVL + 1 > towerTop * 5 + 5)
         {
            println("You cannot level up until you beat the next Battle Tower.");
            println("Go to a town to get directions to the next one.");
            nextLine();
            return;
         }
         else
         {
            println("->> LEVEL UP! >>-");
            println("You are now level " + (pLVL+1) + "!");
            pLVL++;
            pXP -= (pLVL - 1) * 10;
            println("You will now encounter stronger creeps. Collect them all!");
            nextLine();
         }
      }
   }
   
   //Input getters.
   public static void nextLine()
   {
      Scanner userInput = new Scanner(System.in);
      userInput.nextLine();
   }
   public static int userInt()
   {
      Scanner userInput = new Scanner(System.in);
      int out;
      while (true)
      {
         try
         {
            out = userInput.nextInt();
            return out;
         }
         catch (Exception e)
         {
            println("Enter a number.");
            userInput.nextLine();
         }
      }
   }
   public static String userString()
   {
      Scanner userInput = new Scanner(System.in);
      String out = "";
      while (out.length() == 0)
         out = userInput.nextLine();
      return out;
   }
   
   //The main menu.
   public static void mainMenu()
   {
      while(true)
      {
         println("");
         println("C----------------C");
         println("| NEW GAME.....1 |");
         println("| LOAD GAME....2 |");
         println("| TUTORIAL.....3 |");
         println("C---MAIN--MENU---C");
         println("");
         switch(userInt())
         {
            case 1:
               println("Are you sure you want to begin a new game? (Y/N)");
               if (yes())
               {
                  println("Starting new game. Please wait.");
                  Creep test1 = new Creep("^",1);
                  Creep test2 = new Creep("^",1);
                  Creep test3 = new Creep("^",1);
                  creeps.add(test1);
                  creeps.add(test2);
                  creeps.add(test3);
                  println("Ready. Sending you to the Creep world.");
                  nextLine();
                  return;
               }
               else
                  println("Returning to main menu.");
               break;
            case 2:
               println("Are you sure you want to load a new game? (Y/N)");
               if (yes())
               {
                  println("Loading game. Enter your save data.");
                  if(load(userString()))
                  {
                     println("Load was a success. Starting game.");
                     nextLine();
                     return;
                  }
                  else
                  {
                     println("Failed to load. Make sure your data is exactly as it was printed out.");
                     println("Check your data and then hit RUN again.");
                  }   
               }
               else
                  println("Returning to main menu.");
               break;
            case 3:
               println("Begin tutorial? (Y/N)");
               if (yes())
               {
                  println("Alright! Let's get started!");
                  println("In Creeps, press ENTER to progress through text.");
                  println("This tutorial will be broken up into blocks to make it easier to read.");
                  println("So, try it -- press ENTER to continue.");
                  nextLine();
                  println("Good. In Creeps, you are a Collector. You collect Creeps!");
                  println("Creeps are randomly generated monsters.");
                  println("You get three free Creeps when you begin, but you can get more.");
                  println("If a wild Creep attacks you, and you beat it in battle, you'll keep it.");
                  nextLine();
                  println("Let's talk about battles. Every Creep has a variety of stats.");
                  println("HP is Health Points. When they are attacked, this goes down.");
                  println("Don't let it hit zero, or they'll be warped out.");
                  println("ENG is Energy, used to power attacks. Every attack has an EXH value");
                  println(" that tells you how much energy is needed to use the attack.");
                  nextLine();
                  println("Every Creep also has a PWR, Power, and DEF, Defense, value.");
                  println("Power increases damage and defense boosts regeneration.");
                  println("Attacks also have STR, Strength, and SPD, Speed.");
                  println("Stronger attacks hit harder and faster attacks hit more often.");
                  nextLine();
                  println("Creeps also have rarities. Rare creeps have slightly higher stats, but");
                  println(" rare creeps have highly powerful attacks that often use lots of ENG.");
                  println("There are six rarities: Lesser, Mediate, Greater, Superior, Epic, Ultimate.");
                  println("Get lots of rare creeps to win every battle!");
                  nextLine();
                  println("In a battle, just select what attack to use, or to rest and regain energy.");
                  println("You can also try to run from a battle if it's hard, but you can't run from other collectors.");
                  println("Battles sometimes happen when you walk onto a new area.");
                  nextLine();
                  println("When you win a battle, you, not your Creeps, get XP. Get enough XP and you level up.");
                  println("You'll encounter stronger Creeps in the wild when you level up, so fight a lot.");
                  nextLine();
                  println("But, you can't just rip through the levels. You need to beat Battle Towers.");
                  println("There are nine of them, and you can't pass a certain level until you beat one.");
                  println("Ask for directions at a town to find the next one.");
                  nextLine();
                  println("As for areas, the game takes place in a very large world made up of blocks.");
                  println("Every block is a character that tells you what is there.");
                  println("Enter a direction and hit ENTER to move. As for the blocks...");
                  nextLine();
                  println("Here they are, with descriptions.");
                  println(" . : Plains. Pretty safe. Weak creeps.");
                  println(" ^ : Mountains. Populated by Creeps. Good for getting new Creeps.");
                  println(" @ : Cave. Filled with Creeps. A good challenge.");
                  println(" % : Dungeon. A powerful Creep resides inside.");
                  println(" ! : Event. An extremely powerful Creep is waiting here.");
                  println(" # : Town. A place to heal, shop, get directions, and more.");
                  println(" & : Collector. Another Collector is here. Battle them for money!");
                  println(" 1 : Battle tower. There are nine, and they are shown as the numbers 1-9.");
                  println(" | : Valley. The Creep world is surrounded by a deep impassable valley.");
                  println(" + : You!");
                  nextLine();
                  println("The currency in the Creep world is Shine, or [S]. Buy creeps with it.");
                  println("Defeat Battle Towers and other Collectors to earn [S].");
                  println("The Creeps you can buy in towns are very strong, so it's worth your time");
                  println(" to earn a lot of [S] when you want to power up.");
                  println("You can also sell unwanted Creeps for a bit of money.");
                  nextLine();
                  println("And that's Creeps! Defeat the ninth Battle Tower, located in the center of");
                  println(" the world, to win. All the others are for you to find.");
                  println("Remember to save at towns so you don't lose your progress if you have to leave.");
                  println("The world is re-generated every time you play, but you only need to beat tower 9");
                  println(" to win, so don't worry about wasting time finding easy towers.");
                  println("Have fun!");
                  nextLine();
                  println(" CREEPS - A game by Miller Hollinger");
                  nextLine();
                  println("Returning to main menu.");
               }
               else
                  println("Returning to main menu.");
               break;
         }
         nextLine();
      }
   }
   
   //Load a save. Return if it was a success.
   public static boolean load(String in)
   {
      try
      {
         in = in.substring(in.indexOf("/") + 1, in.length());
         pLVL = Integer.parseInt(in.substring(0, in.indexOf("/")));
      
         in = in.substring(in.indexOf("/") + 1, in.length());
         pXP = Integer.parseInt(in.substring(0, in.indexOf("/")));
      
         in = in.substring(in.indexOf("/") + 1, in.length());
         shine = Integer.parseInt(in.substring(0, in.indexOf("/")));
      
         in = in.substring(in.indexOf("/") + 1, in.length());
         towerTop = Integer.parseInt(in.substring(0, in.indexOf("/")));
      
         in = in.substring(in.indexOf("<") + 1, in.length());
         String c0 = in.substring(0, in.indexOf(">"));
         
         in = in.substring(in.indexOf("<") + 1, in.length());
         String c1 = in.substring(0, in.indexOf(">"));
         
         in = in.substring(in.indexOf("<") + 1, in.length());
         String c2 = in.substring(0, in.indexOf(">"));
         
         creeps.add(new Creep(".",1)); //To be replaced by the load() functions.
         creeps.add(new Creep(".",1));
         creeps.add(new Creep(".",1));
         return creeps.get(0).load(c0) && creeps.get(1).load(c1) && creeps.get(2).load(c2);
      }
      catch (Exception e)
      {
         return false;
      }
   }
   
   //Get save data.
   public static String getSave()
   {
      return "$PLAYER/"+pLVL+"/"+pXP+"/"+shine+"/"+towerTop+"/C0<"+creeps.get(0).getSave()+">/C1<"
                          +creeps.get(1).getSave()+">/C2<"
                          +creeps.get(2).getSave()+">/";
   }
   
   //Printers.
   public static void print(Object o)
   {
      System.out.print(o.toString());
   }
   public static void println(Object o)
   {
      System.out.println(o.toString());
   }
}