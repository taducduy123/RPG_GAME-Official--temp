package CHARACTER;

import MAP.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import ITEM.*;


public class Boss extends Monster
{
    private final String mark = "BO";
    private int collisionCount;
    private boolean inSecondPhase;
//--------------------------------------------------

    //Constructor
    public Boss(String name, int maxhp, int atk, int def, int range, int x, int y, boolean state) {
        super(name, maxhp, atk, def, range, x, y);
        this.setItemToDrop();
        this.lootRate = 1;
        this.collisionCount = 0;
        this.inSecondPhase = state;
    }

    //Copy Constructor
    public Boss(Boss b)
    {
        super(b.Name, b.maxHP, b.Attack, b.Defense, b.Range, b.x, b.y);
        this.HP = b.HP;
        this.collisionCount = b.collisionCount;
        this.inSecondPhase = b.inSecondPhase;

        this.lootRate = b.lootRate;

        for(Item i : b.itemsToDrop)
        {
            this.itemsToDrop.add(i);
        }
    }
   

//-------------------------------------------------- Override Methods -------------------------------------
    @Override
    public void setItemToDrop() 
    {
        this.itemsToDrop.add(new Weapon("Glory Sword", 60, 3, this.getX(), this.getY()));
        this.itemsToDrop.add(new Armor("Sunfire Aegis", 10, 60, this.getX(), this.getY()));
        this.itemsToDrop.add(new Potion("Health Potion", 30, this.getX(), this.getY()));
    }

    @Override
    public Item lootItem() 
    {
        Random random = new Random();
        int ranNum = random.nextInt(100) + 1;       //1,2,3,....,100

        Item itemToLoot = null;
        //Loot root = 100%
        if(ranNum <= 100 * this.lootRate)
        {
            ranNum = random.nextInt(itemsToDrop.size()) + 1;            //1,2,3, .... size of itemToDrop
            itemToLoot = itemsToDrop.get(ranNum - 1);
            itemToLoot.setXY(this.getX(), this.getY());                 //Make position of monster and item align
        }
        
        return itemToLoot;  
    }

    @Override
    public String getMark() 
    {
        return this.mark;
    }
    
    @Override
    public void doWork(Player p, Map m) 
    {
        if(this.getHP() > 0)            //if boss is still alive
        {
            if(this.collidePlayer(p))
            {
                if(!this.inSecondPhase)         //if boss in phase 1
                {
                    JOptionPane.showMessageDialog(null, "WARNING: " 
                                                                + this.getName() 
                                                                + " attacked you. You lost " 
                                                                + p.takeDamage(this.getAttack()) 
                                                                + " HP!!!");
                }
                else                            //if boss in phase 2
                {
                    if(this.collisionCount % 5 == 0)
                    {
                        bossPush(m, p);
                        JOptionPane.showInternalMessageDialog(null, "WARNING: " + this.getName() 
                                                                                        + " pushed you far away!");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "WARNING: " 
                                                                + this.getName() 
                                                                + " attacked you. You lost " 
                                                                + p.takeDamage(this.getAttack()) 
                                                                + " HP!!!");
                    }
                    collisionCount++;
                }
            }
            else
            {
                this.bossMove(m, p);
            }
        }
        else                //if died
        {
            if(this.inSecondPhase)
                m.addItem(this.lootItem());
            m.removeMonsterHavingPosition(this.getX(), this.getY());
        }
    }

//----------------------------------------------- Special Skills --------------------------------------------
    public boolean lowerHalfHp()
    {
        boolean status = false;
        if(this.getHP() <= this.getMaxHp()/2)
        {
            status = true;
        }
        return status;
    }
    

    public void bossMove(Map map, Player player)
    {
        if(!this.inSecondPhase)             //if boss in phase 1 --> pursuing move
        {
            List<Pair> path = new LinkedList<>();
            int currentStep = 0;
            if(map.findPath_BFS_Between(this.getX(), this.getY(), player.getX(), player.getY(), path))
            {
                //Search the current position of monster, compared to the path
                for(int i = 0; i < path.size(); i++)
                {
                    if(path.get(i).getX() == this.getX() && path.get(i).getY() == this.getY())
                    {
                        currentStep = i;
                        break;
                    }
                }
                //Navigate monster to follow the correct path
                if(currentStep < path.size() - 1)       //if the monster does not reach target
                {
                    int dx = path.get(currentStep + 1).getX() - this.getX();
                    int dy = path.get(currentStep + 1).getY() - this.getY();
                    if(dx == 0 && dy == -1)
                    {
                        this.moveUp(map);
                    }
                    else if(dx == 0 && dy == 1)
                    {
                        this.moveDown(map);
                    }
                    else if(dx == -1 && dy == 0)
                    {
                        this.moveLeft(map);
                    }
                    else if(dx == 1 && dy == 0)
                    {
                        this.moveRight(map);
                    }
                }
            }
            else        //random move if it doesn't find path
            {
                Random random = new Random();
                int ranNum = random.nextInt(100) + 1;
        
                if(ranNum <= 25)
                {
                    this.moveUp(map);
                }
                else if (25 < ranNum && ranNum <= 50)
                {
                    this.moveDown(map);
                }
                else if (50 < ranNum && ranNum <= 75) 
                {
                    this.moveLeft(map);
                }
                else
                {
                    this.moveRight(map);
                }
            }
        }
        else                //if boss in phase 2 --> random move
        {
            Random random = new Random();
                int ranNum = random.nextInt(100) + 1;
        
                if(ranNum <= 25)
                {
                    this.moveUp(map);
                }
                else if (25 < ranNum && ranNum <= 50)
                {
                    this.moveDown(map);
                }
                else if (50 < ranNum && ranNum <= 75) 
                {
                    this.moveLeft(map);
                }
                else
                {
                    this.moveRight(map);
                }
        }
    }
   

    public void bossPush(Map map, Player player)
    {     
        int deltaX = player.getX() - this.getX();
        int deltaY = player.getY() - this.getY();

        if(Math.abs(deltaY) > Math.abs(deltaX))
        {
            if(player.getY() > this.getY())
            {
                player.setXY(player.getX(), map.getMaxTileRows() - 1, map);
            }
            else
            {
                player.setXY(player.getX(), 0, map);
            }
        }
        else if(Math.abs(deltaY) == Math.abs(deltaX))
        {
            if(deltaX > 0 && deltaY < 0)
                player.setXY(map.getMaxTileCols() - 1, 0, map);
            else if(deltaX < 0 && deltaY < 0)
                player.setXY(0 , 0, map);
            else if(deltaX < 0 && deltaY > 0)
                player.setXY(0 , map.getMaxTileRows() - 1, map);
            else if(deltaX > 0 && deltaY > 0)
                player.setXY(map.getMaxTileRows() - 1, map.getMaxTileCols() - 1, map);
        }
        else{
            if(player.getX() > this.getX())
            {
                player.setXY(map.getMaxTileCols() - 1, player.getY(), map);
            }
            else
            {
                player.setXY(0, player.getY(), map);
            }
        }
    }

   

}
