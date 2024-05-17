package CHARACTER;
import java.util.Random;

import javax.swing.JOptionPane;

import ITEM.Armor;
import ITEM.Item;
import ITEM.Potion;
import ITEM.Weapon;
import MAP.Map;


public class RegularMonster extends Monster
{




//-----------------------------------------------------------

    //Constructor
    public RegularMonster(String name, int maxHP, int atk, int def, int x, int y) 
    {
        super(name, maxHP, atk, def, 4, x, y); 
        this.setItemToDrop(); 
        this.lootRate = 0.6;
       
    }

    //Copy Constructor
    public RegularMonster(RegularMonster rm)
    {
        super(rm.Name, rm.maxHP, rm.Attack, rm.Defense, rm.Range, rm.x, rm.y);
        this.HP = rm.HP;
        this.lootRate = rm.lootRate;

        for(Item i : rm.itemsToDrop)
        {
            this.itemsToDrop.add(i);
        }
    }

//----------------------------- Override Methods -------------------------------------------------
    @Override
    public String getMark() 
    {
        return "RM";
    }
    

    @Override
    public void setItemToDrop() 
    {
        this.itemsToDrop.add(new Weapon("Lethal Axe", 10, 1, this.getX(), this.getY()));
        this.itemsToDrop.add(new Weapon("Golden Sword", 20, 1, this.getX(), this.getY()));
        this.itemsToDrop.add(new Armor("Shield", 10, 10, this.getX(), this.getY()));
        this.itemsToDrop.add(new Potion("Apple Potion", 10, this.getX(), this.getY()));
    }


    @Override
    public Item lootItem() 
    {
        Random random = new Random();
        int ranNum = random.nextInt(100) + 1;       //1,2,3,....,100

        Item itemToLoot = null;
        //Loot root = 60%
        if(ranNum <= 100 * this.lootRate)
        {
            ranNum = random.nextInt(itemsToDrop.size()) + 1;            //1,2,3, .... size of itemToDrop
            itemToLoot = itemsToDrop.get(ranNum - 1);
            itemToLoot.setXY(this.getX(), this.getY());                  //Make position of monster and item align
        }   
        return itemToLoot;     
    }


    @Override
    public void doWork(Player p, Map m) 
    {
        if(this.getHP() > 0)    //if monster is still alive
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
                this.randomMove(m);
            }
        }
        else
        {
            m.addItem(this.lootItem());
            m.removeMonsterHavingPosition(this.getX(), this.getY());
        }
    }

//----------------------------------------- Move -------------------------------------------------

    public void randomMove(Map map)
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


    //Embedded Main
    public static void main(String[] args) 
    {
        RegularMonster monster = new RegularMonster("monster", 0, 0, 0, 0, 0);
        
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
