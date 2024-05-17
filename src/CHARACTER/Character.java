package CHARACTER;

import java.io.Serializable;
import MAP.Map;


public abstract class Character implements Serializable
{
    protected String Name;
    protected int HP, maxHP;
    protected int Attack;
    protected int Defense;
    protected int Range;
    protected int x;
    protected int y;

//------------------------------------------------------

    //Constructor
    public Character(String name, int maxhp, int atk, int def, int range, int x, int y)
    {
        this.Name = name;
        this.maxHP = maxhp;
        this.HP = this.maxHP;
        this.Attack = atk;
        this.Defense = def;
        this.Range = range;
        this.x = x;
        this.y = y;
    }


//--------------------------------------- Getter Methods --------------------------------------------------
    public int getX()
    {return this.x;}
    public int getY()
    {return this.y;}
    public int getMaxHp()
    {return this.maxHP;}
    public int getHP()
    {return this.HP;}
    public int getRange()
    {return this.Range;}
    public int getAttack()
    {return this.Attack;}
    public int getDefense()
    {return this.Defense;}
    public String getName()
    {return this.Name;}

//--------------------------------------- Setter Methods --------------------------------------------------
    
    public void setXY(int x, int y, Map map)
    {
        if(!map.isSolidAt(x, y) && !map.containMonsterAt(x, y) && !map.outOfBorder(x, y))
        {
            this.x = x;
            this.y = y;
        }
    } 
    public void setRange(int range)
    {
        if(range < 0)
        {
            this.Range = 0;
        }
        else
        {
            this.Range = range;
        }
    }
    public void setAttack(int attack)
    {
        if(attack < 0)
        {
            this.Attack = 0;
        }
        else
        {
            this.Attack = attack;
        }
    }
    public void setDefense(int defense)
    {
        if(defense < 0)
        {
            this.Defense = 0;
        }
        else
        {
            this.Defense = defense;
        }
    }
    public void setMaxHp(int maxHPtoSet)
    {                                                               
        if(maxHPtoSet < this.getHP())
        {
            this.HP = maxHPtoSet;
            this.maxHP = maxHPtoSet;
        }
        else
        {
            this.maxHP = maxHPtoSet;
        }

        //System.out.println("setMaxHp using: current HP - max HP = " + this.getHP() + "---" + this.getMaxHp());
    }
    public void setName(String name)
    {this.Name = name;}


//--------------------------------------- Other Useful Methods -------------------------------------
    public void heal(int hpToHeal)
    {
        int HP_afterHeal = this.HP + hpToHeal;

        if(HP < maxHP)          
        {
            if(HP_afterHeal >= maxHP)
            {
                HP = maxHP;
            }
            else
            {
                HP = HP_afterHeal;
            }
        }
    }
    public int takeDamage(int dmg)
    {
        int trueDamage = dmg - this.Defense;
        if(trueDamage > 0)
        {
            int remainingHP = this.HP - trueDamage;
            if(remainingHP <= 0)
            {
                this.HP = 0;
            }
            else
            {
                this.HP = remainingHP;
            }
        }
        else
        {
            trueDamage = 0;
        }
        return trueDamage;
    }
    

//----------------------------------------- Abstract Methods -----------------------------------
    public abstract String getMark();
    

//----------------------------------------- Moving Methods -----------------------------------------
//These methods let character actually move when character doesn't collide solid area or out of border after moving

    public void moveUp(Map map)
    {
        int dx = 0;
        int dy = - 1;

        if(map.validMove(this, dx, dy))
        {
            this.x += dx;
            this.y += dy;
        }      
    }

    public void moveDown(Map map)
    {
        int dx = 0;
        int dy = 1;

        if(map.validMove(this, dx, dy))
        {
            this.x += dx;
            this.y += dy;
        }  
    }
    
    public void moveLeft(Map map)
    {
        int dx = -1;
        int dy = 0;

        if(map.validMove(this, dx, dy))
        {
            this.x += dx;
            this.y += dy;
        }  
    }

    public void moveRight(Map map)
    {
        int dx = 1;
        int dy = 0;

        if(map.validMove(this, dx, dy))
        {
            this.x += dx;
            this.y += dy;
        }     
    }
    

    

   
    
}