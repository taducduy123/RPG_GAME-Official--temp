package ITEM;

import CHARACTER.Character;


public class Weapon extends Item
{
    private final String mark = "wp";
    private int attack;
    private int range;

//-------------------------------------

    //Constructor
    public Weapon(String name, int attack, int range, int x, int y)
    {
        super(name, 1, x, y);
        this.attack = attack;
        this.range = range;
    }

    //Copy Constructor
    public Weapon(Weapon w)
    {
        super(w.name, w.type, w.x, w.y);
        this.inUse = w.inUse;
        this.attack = w.attack;
        this.range = w.range;
    }

    //Equal
    public boolean equal(Weapon w)
    {
        boolean equal = false;
        if(this.name == w.name && 
           this.type == w.type && 
           this.inUse == w.inUse && 
           this.attack == w.attack && 
           this.range == w.range)
        {
            equal = true;
        }
        return equal;
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


    @Override
    public void applyEffectTo(Character obj) 
    {
        obj.setAttack(obj.getAttack() + this.attack);       //new attack = current attack + attack of weapon
        obj.setRange(obj.getRange() + this.range);          //new reange = current range + range of weapon
    }


    @Override
    public void unapplyEffectTo(Character obj) 
    {
        obj.setAttack(obj.getAttack() - this.attack);        //new attack = current attack - attack of weapon
        obj.setRange(obj.getRange() - this.range);           //new reange = current range - range of weapon
    }


    @Override
    public String toString()
    {   
        //name [bonus Attack: , bonus Range:]
        String str =  super.getName() + " [bonus Attack: " + this.attack + ", bonus Range: " + this.range + "]";                              
        return str;
    }

}
