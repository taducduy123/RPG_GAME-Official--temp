package MAP.TILE;
import java.io.Serializable;

import CHARACTER.Character;

public abstract class Tile implements Serializable
{
    
    protected boolean solid;

//--------------------------------------------------

    //Constructor
    public Tile(boolean solid)
    {
        this.solid = solid;
    }

    //Getter Methods
    public boolean getSolid()
    {return this.solid;}

    //Abstract
    public abstract void drawTile(String mark);
    public abstract void applyEffectTo(Character character);

}
