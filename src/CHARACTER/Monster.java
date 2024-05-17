package CHARACTER;

import java.util.LinkedList;
import java.util.List;
import ITEM.Item;
import MAP.Map;

public abstract class Monster extends Character
{
    protected List<Item> itemsToDrop;
    protected double lootRate;
    

//------------------------------------------

    //Constructor
    public Monster(String name, int maxhp, int atk, int def, int range, int x, int y) 
    {
        super(name, maxhp, atk, def, range, x, y);
        itemsToDrop = new LinkedList<>();
    }



//------------------------------ Getter Method ------------------------------------
    
    

//------------------------------ Check Collison with player -----------------------

    public boolean collidePlayer(Player p)
    {
        boolean collision = false;
        int max_X = this.getX() + this.getRange();
        int min_X = this.getX() - this.getRange();
        int max_Y = this.getY() + this.getRange();
        int min_Y = this.getY() - this.getRange();
        if(min_X <= p.getX() && p.getX() <= max_X && min_Y <= p.getY() && p.getY() <= max_Y)
            collision = true;
        return collision;
    }
    
//------------------------------ Abstract Methods ---------------------------------

    public abstract void setItemToDrop();
    public abstract Item lootItem();
    public abstract void doWork(Player p, Map m);
    
    
}