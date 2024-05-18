package CHARACTER;

import java.util.LinkedList;
import java.util.List;
import MAP.Map;
import java.util.Random;
import javax.swing.JOptionPane;
import ITEM.*;
import MAP.Pair;


public class TargetMonster extends Monster
{
 
    private final String mark = "TM";


//-----------------------------------------------------------

    //Constructor
    public TargetMonster(String name, int maxHP, int atk, int def, int x, int y) 
    {
        super(name, maxHP, atk, def, 1, x, y); 
        this.setItemToDrop();   
        this.lootRate = 0.5;
    }

    //Copy Constructor
    public TargetMonster(TargetMonster tm)
    {
        super(tm.Name, tm.maxHP, tm.Attack, tm.Defense, tm.Range, tm.x, tm.y);
        this.HP = tm.HP;
        this.lootRate = tm.lootRate;

        for(Item i : tm.itemsToDrop)
        {
            this.itemsToDrop.add(i);
        }
    }

//----------------------------- Override Methods -------------------------------------------------
    @Override
    public String getMark() 
    {
        return this.mark;
    }


    @Override
    public void setItemToDrop() 
    {
       this.itemsToDrop.add(new Weapon("Kircheis Shard ", 20, 2, this.getX(), this.getY()));
        this.itemsToDrop.add(new Weapon("Serrated Dirk", 25, 1, this.getX(), this.getY()));
        this.itemsToDrop.add(new Potion("Small Potion", 30, this.getX(), this.getY()));
        this.itemsToDrop.add(new Armor("JakSho", 10, 30, this.getX(), this.getY()));
    }


    @Override
    public Item lootItem() 
    {
        Random random = new Random();
        int ranNum = random.nextInt(100) + 1;       //1,2,3,....,100

        Item itemToLoot = null;
        //Loot root = 50%
        if(ranNum <= 100 * this.lootRate)
        {
            ranNum = random.nextInt(itemsToDrop.size()) + 1;            //1,2,3, .... size of itemToDrop
            itemToLoot = itemsToDrop.get(ranNum - 1);
            itemToLoot.setXY(this.getX(), this.getY());                 //Make position of monster and item align
        }
        
        return itemToLoot;     
    }


    @Override
    public void doWork(Player p, Map m) 
    {
        if(this.getHP() > 0)        //if monster is still alive
        {
            if(this.collidePlayer(p))
            {
                JOptionPane.showMessageDialog(null, "WARNING: " 
                                                                + this.getName() 
                                                                + " attacked you. You lost " 
                                                                + p.takeDamage(this.getAttack()) 
                                                                + " HP!!!");
            }
            else
            {
                this.moveForwardTo(p, m);
            }
        }
        else                //if died
        {
            m.addItem(this.lootItem());
            m.removeMonsterHavingPosition(this.getX(), this.getY());
        }

    }

//-------------------------------------- Move ---------------------------------------------------------

    public void moveForwardTo(Character player, Map map)
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


    //Embedded Main
    public static void main(String[] args) 
    {
        TargetMonster monster = new TargetMonster("monster", 0, 0, 0, 0, 0);
        
        Item item = monster.lootItem();

        if(item == null)
        {
            System.out.println("null");
        }
        else
        {
            System.out.println(item);
        }
        
    }
}
