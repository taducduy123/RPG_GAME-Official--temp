package MAP.TILE;

import CHARACTER.Character;


public class WallTile extends Tile
{
    private final String representation = "< >"; 
//-----------------------------------------

    //Constructor
    public WallTile()
    {   
        super(true);
    }

    //Copy Constructor
    public WallTile(WallTile wt)
    {
        super(wt.solid);
    }

    @Override
    public void drawTile(String mark)
    {
        String[] token = this.representation.split(" ");
        mark = "00";
        System.out.printf(" " + token[0] + "%2s" + token[1] + " ", mark);
    }

    @Override
    public void applyEffectTo(Character character) 
    {
        
    }
    
}
