package MAP.TILE;

import CHARACTER.Character;


public class WaterTile extends Tile
{
    private int hpToHeal = 5;
    private final String representation = "( )"; 

//---------------------------------------------------
    
    //Constructor
    public WaterTile()
    {   
        super(false);
    }

    //Copy Constructor
    public WaterTile(WaterTile wt)
    {
        super(wt.solid);
        this.hpToHeal = wt.hpToHeal;
    }



    @Override
    public void drawTile(String mark)
    {
        String[] token = this.representation.split(" ");
        System.out.printf(" " + token[0] + "%2s" + token[1] + " ", mark);
    }

    @Override
    public void applyEffectTo(Character character)
    {
        character.heal(hpToHeal);
    }    

    
}
