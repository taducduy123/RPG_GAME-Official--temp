package CHARACTER;

import ITEM.*;
import MAP.Map;


public class Player extends Character
{
    private final String mark = "X";
    private Weapon weapon  = null;
    private Armor armor = null;

//-------------------------------------------

    //Constructor #1
    public Player(String name, int maxhp, int atk, int def, int range, int x, int y)
    {
        super(name, maxhp, atk, def, range, x, y);
    }

    //Constructor #2: Basic properties of player is set by default
    public Player()
    {
        super("Hero", 100, 10, 10, 1, 0, 0);
    }

    //Copy Constructor 
    public Player(Player p)
    {
        super(p.Name, p.maxHP, p.Attack, p.Defense, p.Range, p.x, p.y);
        this.weapon = new Weapon(p.weapon);
        this.armor = new Armor(p.armor);      
    }

//---------------------------------------- Getter Methods ---------------------------------------

    public String getCurrentWeapon(){
        if(weapon == null){
            return "No weapon";
        }
        else return this.weapon.toString();
    }

    public String getCurrentArmor(){
        if(armor == null){
            return "No weapon";
        }
        else return this.armor.toString();
    }

    public Weapon getWeapon()
    {
        Weapon clone = null;
        if(this.weapon != null)
            clone = new Weapon(this.weapon);
        return clone; 
    }

    public Armor getArmor()
    {
        Armor clone = null;
        if(this.armor != null)
            clone = new Armor(this.armor);
        return clone;
    }

    @Override
    public String getMark()
    {return this.mark;}


//------------------------------------- Show State --------------------------------------------
    //Show state of player
    public void showState()
    {
        System.out.println("\n-------------------- State of Hero ---------------------");
        System.out.print("HP/maxHP: " + this.getHP() + "/" + this.getMaxHp() +  " | ");
        System.out.print("Attack: " + this.getAttack() + " | ");
        System.out.print("Defense: " + this.getDefense() + " | ");
        System.out.println("Range: " + this.getRange());
        System.out.println("Weapon: " + (weapon != null ? weapon.getName() : "None"));
        System.out.println("Armor: " + (armor != null ? armor.getName() : "None"));
        System.out.println("Position: (" + this.getX() + ", " + this.getY() + ")");
    }

//----------------------------------- Check Collision with Monster -------------------------------

    public boolean collideMonster(Monster monster)
    {
        boolean collision = false;
        int max_X = this.getX() + this.getRange();
        int min_X = this.getX() - this.getRange();
        int max_Y = this.getY() + this.getRange();
        int min_Y = this.getY() - this.getRange();
        if(min_X <= monster.getX() && monster.getX() <= max_X && min_Y <= monster.getY() && monster.getY() <= max_Y)
            collision = true;
        return collision;
    }
    

//---------------------------------------- Unequip Methods ---------------------------------------

    //How to unequip weapon from player
    public void unequipWeapon()
    {
        if(this.weapon != null)
        {
            //Remove current weapon (if exist) with its effect and state of use
            this.weapon.setInUse(false);
            this.weapon.unapplyEffectTo(this);
            this.weapon = null;
        }
    }

    //How to unequip armor from player
    public void unequipArmor()
    {
        if(this.armor != null)
        {
            //Remove current armor (if exist) with its effect and state of use
            this.armor.setInUse(false);
            this.armor.unapplyEffectTo(this);       
            this.armor = null;
            //System.out.println("NOW WE ARE UNEQUIPING");
        }
    }


//------------------------------------------ Equip Methods ---------------------------------------

    //How to equip potion to heal
    public void equipPotion(Item itemToEquip)
    {      
        if (itemToEquip instanceof Potion){
            itemToEquip.setInUse(true);
            itemToEquip.applyEffectTo(this);
        }
    }
  
    
    public void equipWeapon(Item itemToEquip)
    {
        //Identify which type of item
        boolean isWeapon = false;
        if(itemToEquip.getType() == 1)
        {
            isWeapon = true;
        }

        //Equip Item
        if(!isWeapon)
        {
            System.out.println("ERROR: Invalid Item To Equip");
        }
        else
        {
             //1. Remove current attack weapon with corresponding effect
             this.unequipWeapon();

             //2. Equip new attack weapon with corresponding effect and change state of use
             itemToEquip.setInUse(true);
             this.weapon = (Weapon)itemToEquip;
             this.weapon.applyEffectTo(this);           
        }
    } 


    public void equipArmor(Item itemToEquip)
    {
        //Identify which type of item
        boolean isArmor = false;
        if(itemToEquip.getType() == 2)
        {
            isArmor = true;
        }

        //Equip Item
        if(!isArmor)
        {
            System.out.println("ERROR: Invalid Item To Equip");
        }
        else
        {
             //1. Remove current attack weapon with corresponding effect
             this.unequipArmor();

             //2. Equip new attack weapon with corresponding effect and change state of use
             itemToEquip.setInUse(true);
             this.armor = (Armor)itemToEquip;
             this.armor.applyEffectTo(this); 
             
             //System.out.println("NOW WE ARE EQUIP");
        }
    } 

//------------------------------------------- ---------------------------------------------------------

    public int detectMonsters(Map map)
    {
        int count = 0;
        for(int i = 0; i < map.numberOfMonsters(); i++)
        {
            if(this.collideMonster(map.getMonsterAtIndex(i)))
            {
                count++;
            }
        }
        return count; 
    }


   
    
    //Embedded Main
    public static void main(String[] args) 
    {
        Player player = new Player(null);
        player.showState();

        System.out.println();
        Armor a1 = new Armor("Armor",10,10,3,0);
        Armor a2 = new Armor("G Armor",10,10,4,0);
        Armor a3 = new Armor("G Armor",10,10,2,0);

        System.out.println("After 1st time equip:");
        player.equipArmor(a1);
        player.showState();
        System.out.println();

        System.out.println("After 2nd time equip:");
        player.equipArmor(a2);
        player.showState();
        System.out.println();

        System.out.println("After 3rd time equip:");
        player.equipArmor(a3);
        player.showState();
        
    }


    
}

