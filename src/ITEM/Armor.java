package ITEM;

import CHARACTER.Character;


public class Armor extends Item
{
    private final String mark = "ar";
    private int defense;
    private int HP;
    
//---------------------------------------

    //Constructor
    public Armor(String name, int defense, int HP, int x, int y)
    {
        super(name, 2, x, y);
        this.defense = defense;
        this.HP = HP;
    }

    //Copy Constructor
    public Armor(Armor ar)
    {
        super(ar.name, ar.type, ar.x, ar.y);
        this.inUse = ar.inUse;
        this.defense = ar.defense;
        this.HP = ar.HP;
    }

    @Override
    public String getMark()
    {      
        /*  
        //Return 2 first char of Name
        String mark = super.getName().substring(0, 2);
        return mark;
        */
        return this.mark;
    }

    //Equal
    public boolean equal(Armor ar)
    {
        boolean equal = false;
        if(this.name == ar.name && 
           this.type == ar.type && 
           this.inUse == ar.inUse && 
           this.defense == ar.defense && 
           this.HP == ar.HP)
        {
            equal = true;
        }
        return equal;
    }


    @Override
    public void applyEffectTo(Character obj)
    {
        obj.setDefense(obj.getDefense() + this.defense);        //new defense = current defense + defense of weapon
        obj.setMaxHp(obj.getMaxHp() + this.HP);                    //new maxHp = current maxHp + hp of weapon
    }


    @Override
    public void unapplyEffectTo(Character obj)
    {
        obj.setDefense(obj.getDefense() - this.defense);        //new defense = current defense - defense of weapon
        obj.setMaxHp(obj.getMaxHp() - this.HP);                    //new maxHp = current maxHp - hp of weapon      
    }  


    @Override
    public String toString()
    {   
        //name [bonus HP: , bonus Defense:]
        String str =  super.getName() + " [bonus HP: " + this.HP + ", bonus Defense: " + this.defense + "]";                              
        return str;
    }


    //Embedded Main
    public static void main(String[] args) 
    {
       
    }
}

