package MAP.TILE;

import CHARACTER.Character;


public class LandTile extends Tile
{
    private final String representation = "[ ]"; 
//---------------------------------------------

    //Constructor
    public LandTile()
    {   
        super(false);
    }

    //Copy Constructor
    public LandTile(LandTile lt)
    {
        super(lt.solid);
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
        
    }


    //Embedded Main
    public static void main(String[] args) 
    {
        LandTile l = new LandTile();
        l.drawTile("X");
    }
    
}
