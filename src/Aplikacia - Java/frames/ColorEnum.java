package frames;

import java.awt.Color;

/**
 * enumeračný typ obsahujúci prvky slúžiace na uchovávanie aktuálnych farieb vykresľovania
 * @author Matej
 */
public enum ColorEnum {
    
    FREE(Color.WHITE),
    UNEXPLORED(Color.WHITE),
    SENSOR(Color.BLACK),
    BARRIERS(Color.RED);
    
    private Color color;
    private final Color defColor;
    
    private ColorEnum(Color color){
        this.color = color;
        this.defColor = color;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public void resetColor(){
        this.color = defColor;
    }
}
