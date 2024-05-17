package MAP;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import CHARACTER.Boss;
import CHARACTER.Character;
import CHARACTER.Monster;
import CHARACTER.Player;
import CHARACTER.RegularMonster;
import CHARACTER.TargetMonster;
import GAMESTAGE.GameStage;
import ITEM.*;
import MAP.TILE.*;


public class Map  implements Serializable
{
    private final int maxTileCols = 20;
    private final int maxTileRows = 20;
    private final int maxPossibleTypesOfTile = 5;
    private String path;
    private int bossPhase;

    private Tile[] tile;
    private int[][] tileManager;
    private List<Item> items;
    private List<Monster> monsters;
    

//--------------------------------------------------

    //Constructor
    public Map(String mapFilePath)
    {
        //Store file path
        this.path = mapFilePath;

        //Intialize items and monsters
        this.items = new LinkedList<>();
        this.monsters = new LinkedList<>();

        //Define all possible types of tile on the map
        this.tile = new Tile[maxPossibleTypesOfTile];
        this.tile[0] = new WallTile();
        this.tile[1] = new LandTile();
        this.tile[2] = new WaterTile();
        this.tile[3] = new FireTile();
        this.tile[4] = new DoorTile();

        //Initialize tileManager
        this.tileManager = new int[maxTileRows][maxTileCols];

        //Loading Map
        loadMap(mapFilePath);
    }

//---------------------------------------------- Getter Methods ----------------------------------------------------------------
    
    public int numberOfMonsters()
    {return this.monsters.size();}

    public int numberOfItems()
    {return this.items.size();}

    public int numberOfPossibleTypeOfTile()
    {return this.maxPossibleTypesOfTile;}

    public Monster getMonsterAtIndex(int index)
    {
        Monster monsterToGet = null;
        if(index < 0)
        {
            System.out.println("ERROR: Invalid index!");
        }
        else
            monsterToGet = this.monsters.get(index);
        return monsterToGet;
    }
    
    public Item getItemAtIndex(int index)
    {
        Item itemToGet = null;
        if(index < 0)
        {
            System.out.println("ERROR: Invalid index!");
        }
        else
            itemToGet = this.items.get(index);
        return itemToGet;
    }
    
    public int getTileManager_RowCol(int x, int y)
    {
        int result = -1;
        if(!outOfBorder(y, x))
        {
            result = this.tileManager[x][y];
        }
        return result;
    }
    
    public Tile getTile(int index)
    {
        Tile tileToGet = null;
        if(0 <= index && index < maxPossibleTypesOfTile)
        {
            tileToGet = this.tile[index];
        }
        return tileToGet;
    }
    
    public int getMaxTileCols()
    {return this.maxTileCols;}
    
    public int getMaxTileRows()
    {return this.maxTileRows;}

    public int getBossPhase()
    {return this.bossPhase;}

//---------------------------------------------- Load Map from File ------------------------------------------------------------

    public void loadMap(String mapFilePath)
    {
    //Read Map From File
        File myFile = new File(mapFilePath);

        //Check if file does not exist
        if(!myFile.exists())
        {
            System.out.println("Unable to open file " + mapFilePath);
        }

        try 
        {
            //Open input file for reading
            Scanner inputFile = new Scanner(myFile);

            //1. Read Map
            String line;
            String[] numbers;
            for(int i = 0; i < maxTileRows; i++)            // i ~ y-coor of obj in xy plane
            {
                //Read every line in text file
                line = inputFile.nextLine();

                //Get tokens from the line
                numbers = line.split(" ");

                //Load all tokens into int[][] tileManager
                for(int j = 0; j < maxTileCols; j++)        //j ~ x-coor of obj in xy plane
                {
                    tileManager[i][j] = Integer.parseInt(numbers[j]);
                }
            }

            //2. Read Items
            String itemType = "";
            String[] info;
           
            while(inputFile.hasNext() && itemType.compareToIgnoreCase("END_ITEM") != 0)
            {
                line = inputFile.nextLine();
                info = line.split(",");
                itemType = info[0];
                switch (itemType) 
                {
                   case "Armor":
                                this.items.add(new Armor(info[1], Integer.parseInt(info[2]), 
                                                                  Integer.parseInt(info[3]), 
                                                                  Integer.parseInt(info[4]), 
                                                                  Integer.parseInt(info[5])));
                                break;
                    case "Weapon":
                                this.items.add(new Weapon(info[1], Integer.parseInt(info[2]), 
                                                                  Integer.parseInt(info[3]), 
                                                                  Integer.parseInt(info[4]), 
                                                                  Integer.parseInt(info[5])));
                                break;
                    case "Potion":
                                this.items.add(new Potion(info[1], Integer.parseInt(info[2]), 
                                                                  Integer.parseInt(info[3]), 
                                                                  Integer.parseInt(info[4])));
                                break;
                    default:
                                break;
                }
            } 

            //3. Read Monsters
            String monsterType = "";
            while(inputFile.hasNext() && monsterType.compareToIgnoreCase("END_MONSTER") != 0)
            {
                line = inputFile.nextLine();
                info = line.split(",");
                monsterType = info[0];
                switch (monsterType) 
                {
                   case "RegularMonster":
                                this.monsters.add(new RegularMonster(info[1], Integer.parseInt(info[2]), 
                                                                              Integer.parseInt(info[3]), 
                                                                              Integer.parseInt(info[4]), 
                                                                              Integer.parseInt(info[5]),
                                                                              Integer.parseInt(info[6])));
                                break;
                                
                    case "TargetMonster":
                                this.monsters.add(new TargetMonster(info[1], Integer.parseInt(info[2]), 
                                                                             Integer.parseInt(info[3]), 
                                                                             Integer.parseInt(info[4]), 
                                                                             Integer.parseInt(info[5]),
                                                                             Integer.parseInt(info[6])));
                                break;

                    case "Boss":
                                this.monsters.add(new Boss(info[1], Integer.parseInt(info[2]), 
                                                                    Integer.parseInt(info[3]), 
                                                                    Integer.parseInt(info[4]), 
                                                                    Integer.parseInt(info[5]),
                                                                    Integer.parseInt(info[6]),
                                                                    Integer.parseInt(info[7]),
                                                                    Boolean.parseBoolean(info[8])));
                                break;

                    default:
                                break;
                }
            }
            // 4. Read Boss Phase
            if(inputFile.hasNextLine())
            {
                String bossPhaseLine = inputFile.nextLine();
                try 
                {
                    this.bossPhase = Integer.parseInt(bossPhaseLine);
                } 
                catch (NumberFormatException e) 
                {
                    System.out.println("Invalid format for boss phase!");
                }
            }

            //Close the file
            inputFile.close();

        } 
        catch (Exception e) 
        {
            System.out.println("Fail to open file!");
        }
    }


//---------------------------------------------- Reset Map ---------------------------------------------------------------------

    public void resetMap()
    {
        if(this.path == null)
        {
            System.out.println("ERROR: Be ensure that map used to be created!");
        }
        else
        {
            //Clear map
            this.items.clear();
            this.monsters.clear();

            //Reload map
            this.loadMap(path);

            //Check door open
            if(this.checkDoorOpen() == true)
            {
                ((DoorTile)this.tile[4]).setDoorClosed(this);
            }
        }
    }


//-------------------------------- Diplaying Map + Related Objects on map ------------------------------------------------------

    //Draw Map (draw every tile + items + monsters + player)
    public void drawMap(Player player)
    {
        for(int i = 0; i < maxTileRows; i++)        // i ~ y-coordinate
        {
            for(int j = 0; j < maxTileCols; j++)    // j ~ x-coordinate
            {    
                if(player != null && i == player.getY() && j == player.getX() )             //1. Draw player first
                {
                    tile[tileManager[i][j]].drawTile(player.getMark());   
                }
                else if(containMonsterAt(j, i))                                             //2. Draw monsters
                {
                    tile[tileManager[i][j]].drawTile(correspondingMonsterAt(j, i).getMark());
                }
                else if(containItemAt(j, i))                                                //3. Draw items
                {
                    tile[tileManager[i][j]].drawTile(correspondingItemAt(j, i).getMark());      
                }
                else if(containDoorAt(j, i))                                                //4. Draw Door
                {
                    if(checkDoorOpen() == true)
                    {
                        if(player.getX() == j && player.getY() == i)    //if player now move into door
                        {
                            tile[tileManager[i][j]].drawTile(player.getMark()); 
                        }
                        else
                        {
                            tile[tileManager[i][j]].drawTile("--"); 
                        }
                    }
                    else
                    {
                        tile[tileManager[i][j]].drawTile("00");
                    }
                }
                else                                                                        //5. Draw other tiles
                {
                    tile[tileManager[i][j]].drawTile("");
                }
            }
            System.out.println("");
        }
    }

//---------------------------------------------- Searching Objects -------------------------------------------------------------

    //Check if position (x, y) contains any monster
    public boolean containMonsterAt(int x, int y)
    {
        boolean contain = false;
        for(int i = 0; i < this.monsters.size(); i++)
        {
            if(this.monsters.get(i).getX() == x && this.monsters.get(i).getY() == y)
            {
                contain = true;
                break;
            }
        }
        return contain;
    }
   
    
    //Find the monster in the list whose position is (x, y)
    public Monster correspondingMonsterAt(int x, int y)
    {
        Monster monsterToFind = null;
        for(int i = 0; i < monsters.size(); i++)
        {
            if(monsters.get(i).getX() == x && monsters.get(i).getY() == y)
            {
                monsterToFind = monsters.get(i);
                break;
            }
        }
        return monsterToFind;
    }


    //Check if position (x, y) contains any item
    public boolean containItemAt(int x, int y)
    {
        boolean contain = false;
        for(int i = 0; i < this.items.size(); i++)
        {
            if(this.items.get(i).getX() == x && this.items.get(i).getY() == y)
            {
                contain = true;
                break;
            }
        }
        return contain;
    }


    //Find the item in the list whose position is (x, y)
    public Item correspondingItemAt(int x, int y)
    {
        Item itemToFind = null;
        for(int i = 0; i < this.items.size(); i++)
        {
            if(this.items.get(i).getX() == x && this.items.get(i).getY() == y)
            {
                itemToFind = this.items.get(i);
                break;
            }
        }
        return itemToFind;
    }


    //Check if position (x, y) contains the door
    public boolean containDoorAt(int x, int y)
    {
        boolean contain = false;
        if(tile[tileManager[y][x]] instanceof DoorTile)
        {
            contain = true;
        }
        return contain;
    }


    //Check if the area at position (x, y) is solid
    public boolean isSolidAt(int x, int y)
    {
        boolean solid = false;
        if(this.tile[tileManager[y][x]].getSolid() == true)
        {
            solid = true;
        }
        return solid;
    }


    //Check if the object out of the border
    public boolean outOfBorder(int x, int y)
    {
        boolean out = true;
        if(0 <= x && x < maxTileCols && 0 <= y && y < maxTileRows)
        {
            out = false;
        }
        return out;
    }

//---------------------------------------------- Adding Objects ----------------------------------------------------------------

    //Adding a monster in the map
    public void addMonster(Monster monster)
    {
        if(!isSolidAt(monster.getX(), monster.getY()))
        {
            this.monsters.add(monster);
        }
        else
        {
            System.out.println("ERROR: You Cannot Set a Monster into a Solid Area!");
        }
    }


    //Adding an item in the map
    public void addItem(Item item)
    {
        if(item != null && !isSolidAt(item.getX(), item.getY()) && 0 <= item.getX() && item.getX() < maxTileCols && 0 <= item.getY() && item.getX() < maxTileRows)
        {
            this.items.add(item);
        }
        else if(item != null)
        {
            System.out.println("ERROR: You Cannot Set an Item into a Solid Area or Out Of Border!");
        }
    }


    //Remove a monster from map
    public boolean removeMonsterHavingPosition(int x, int y)
    {
        //Search monster having postion (x,y) in the list
        boolean found = false;
        for(Character monster: this.monsters)
        {
            if(monster != null && monster.getX() == x && monster.getY() == y)
            {
                found = true;
                this.monsters.remove(monster);
                break;
            }
        }

        if(!found)
        {
            System.out.println("ERROR: Not Found Corresponding Monster to Delete!");
        }     
        return found; 
    }


    //Remove an Item from map
    public boolean removeItemHavingPosition(int x, int y)
    {
       //Search item having postion (x,y) in the list
       boolean found = false;
       for(Item item: this.items)
       {
           if(item != null && item.getX() == x && item.getY() == y)
           {
               found = true;
               this.items.remove(item);
               break;
           }
       }    
       return found; 
    }

   

//---------------------------------------------- Check Validation of Moving ----------------------------------------------------

    public boolean validMove(Character obj, int dx, int dy)
   {
        int x_toCome = obj.getX() + dx;
        int y_toCome = obj.getY() + dy;

        if(x_toCome >= maxTileCols || x_toCome < 0 || y_toCome >= maxTileRows || y_toCome < 0)   //if obj move out of border
        {
            return false;
        }
        else if(this.tile[tileManager[y_toCome][x_toCome]].getSolid() == true)     //if obj collides solid tile after moving
        {
            return false;
        }
        else if(containMonsterAt(x_toCome, y_toCome))     //if obj collides a monster after moving
        {
            return false;
        }
        else
        {
            return true;
        }
   }
   

//---------------------------------------------- Find Shortest Path ------------------------------------------------------------

    //O(V + E)
    public boolean findPath_BFS_Between(int x_start, int y_start, int x_end, int y_end, List<Pair> path)
    {
        boolean[][] visited = new boolean[maxTileRows][maxTileCols];
        for(int i = 0; i < maxTileRows; i++)
        {
            for(int j = 0; j < maxTileCols; j++)
            {
                visited[i][j] = false;
            }
        }
        
    
        Pair parent[][] = new Pair[maxTileRows][maxTileCols];
        for(int i = 0; i < maxTileRows; i++)
        {
            for(int j = 0; j < maxTileCols; j++)
            {
                parent[i][j] = new Pair(-1, -1);
            }
        }

        //1. Initialize an empty queue
        LinkedList<Pair> queue = new LinkedList<>();

        //2. Put source (starting point) into the queue
        queue.add(new Pair(x_start, y_start));
        parent[y_start][x_start] = new Pair(x_start, y_start);  //parent of starting point is itself
        visited[y_start][x_start] = true;                       

        //3. While the queue is not empty, then do:
        Pair front, adjacent;   
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        int x_toCome, y_toCome;
        while(!queue.isEmpty())
        {
            //3.1.  Pop the front element from the queue
            front = new Pair(queue.get(0).getX(), queue.get(0).getY());   
            queue.removeFirst();

            //3.2.  Push all adjacent (unvisited) of that front into the queue
            for(int i = 0; i < 4; i++)              //4 possible adjacent (above, below, left, right vertex)
            {                                       //of that front
                x_toCome = front.getX() + dx[i];
                y_toCome = front.getY() + dy[i];
                
                //Check if an adjacent (among 4 possible cases) is actually an adjacent of the front
                if(0 <= x_toCome && x_toCome < maxTileCols && 0 <= y_toCome && y_toCome < maxTileRows && 
                    tile[tileManager[y_toCome][x_toCome]].getSolid() == false && visited[y_toCome][x_toCome] == false)
                {
                    adjacent = new Pair(x_toCome, y_toCome);
                    queue.add(adjacent);
                    parent[y_toCome][x_toCome] = new Pair(front.getX(), front.getY());      //parent of adjecent 
                    visited[y_toCome][x_toCome] = true;
                }
            }
        }

        //4. Tracing the path (if found)
        if(parent[y_end][x_end].getX() == -1 && parent[y_end][x_end].getY() == -1)  //not found path
        {
            System.out.println("DOES NOT EXIST PATH");
            return false;
        }
        else
        {
            /* 
            for(int i = 0; i < maxTileRows; i++)
            {
                for(int j = 0; j < maxTileCols; j++)
                {
                     System.out.println("Parent of " + j + " " + i + ":  " + parent[i][j].getX() + "<---->" + parent[i][j].getY());
                }
            }
            */
            
            //4.1 Tracing path using parent
            int tempX, tempY;
            while(x_end != x_start || y_end != y_start)     //while destination is not source
            {
                path.add(0, new Pair(x_end, y_end));        //Add at First
                //System.out.println("x_end = " + x_end + " y_end = " + y_end);
                //Swap replace destination cell by its parent

                //Update new destination = parent of old destination
                tempX = parent[y_end][x_end].getX();
                tempY = parent[y_end][x_end].getY();
                x_end = tempX;
                y_end = tempY;
            }
            path.add(0, new Pair(x_start, y_start));        //Add at First  
            return true;
        }
    }

   
//---------------------------------------------- Open/Close the door -----------------------------------------------------------

    public void openDoor()
    {
        if(this.monsters.size() == 0)
        {
            ((DoorTile)this.tile[4]).setDoorOpen(this);
            System.out.println("\n>> ATTENTION: Now The Door is Opening!\n");
        }
    }

    public void closeDoor()
    {
        ((DoorTile)this.tile[4]).setDoorClosed(this);  
    }

    public boolean checkDoorOpen()
    {return ((DoorTile)this.tile[4]).isOpen();}


//--------------------------------------------- Do Work ---------------------------------------------

    public void doWork(Player player, Inventory inventory, GameStage gs)
    {   
        //check when we should open the door
        if(this.numberOfMonsters() == 0 && 
            (this.getBossPhase() < 1 || this.getBossPhase() == gs.getMaxNumPhasesUsingEachStage()))
        {
            openDoor();
        }

        //apply effect when player moves into special area
        this.tile[tileManager[player.getY()][player.getX()]].applyEffectTo(player);

        //update the existence of items
        if(this.containItemAt(player.getX(), player.getY()))
        { 
            if(!inventory.isFull())
            {
                inventory.addItem(this.correspondingItemAt(player.getX(), player.getY()));
                this.removeItemHavingPosition(player.getX(), player.getY());
            }
            else 
                JOptionPane.showMessageDialog(null, "Inventory is full!!!");
        } 

        //update the existence of monsters
        for(int i = 0; i < this.monsters.size(); i++)
        {
            if(this.monsters.get(i).getHP() <= 0)
            {
                this.monsters.remove(i);
            }
        }
    }


   //Embedded Main
   public static void main(String[] args) 
   {
        
        Player hero = new Player("Hero", 0, 0, 0, 0, 0, 0);

        String path = "src/InputFile/map4.txt";
        Map map = new Map(path);
        map.addItem(new Weapon(path, 0, 0, 17, 17));
       
        map.drawMap(hero);
        System.out.println(map.getBossPhase());

        //map.resetMap();
        //map.drawMap(hero);
        //LinkedList<LinkedList<Integer>> vertice = new LinkedList<>();
        //map.to_EdgeList_Graph(vertice);
        //System.out.println(map.path(0, 0, 1, 0));
        
         /* 
        List<Pair> Tpath = new LinkedList<>();
        map.findPath_BFS_Between(0, 0, 19, 19, Tpath);

          
        for(Pair p : Tpath)
        {
            System.out.println(p.getX() + " ---- " + p.getY());
        }   
        */

        //System.out.println(map.checkDoorOpen());

        
   }
   
}








  
