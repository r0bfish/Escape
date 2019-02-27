package com.robin.escape.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.escape;
import com.robin.escape.utilities.FontHandler;

public class ButtonMeny extends Clickable{
    private Vector3 layoutPosition;
    private Vector3 position;
    private Texture TxtUnPressed;
    private Texture TxtUnlocked;
    private Texture TxtLocked;
    private Texture TxtStar;
    private Rectangle bounds;
    private boolean isUnlocked;
    private int stars;
    private String textLevel;
    private String nextLevel;
    private String levelName;
    BitmapFont font;


    public ButtonMeny(Vector3 position, Texture unPressed, Texture unlocked, Texture locked, Texture star,
                      BitmapFont font, int stars, boolean isUnlocked, String levelName, String textLevel){
        super(position, null, null);
        this.font = font;
        this.position = new Vector3(position.x - locked.getWidth()/2, position.y, 0);;
        this.layoutPosition = new Vector3(position.x - locked.getWidth()/2, position.y, 0);
        this.isUnlocked = isUnlocked;
        this.stars = stars;
        this.textLevel = textLevel;
        this.levelName = levelName;
        this.TxtUnPressed = unPressed;
        this.TxtLocked = locked;
        this.TxtUnlocked = unlocked;
        this.TxtStar = star;
        bounds = new Rectangle(position.x  - locked.getWidth()/2, position.y, TxtLocked.getWidth(), TxtLocked.getHeight());
    }

    public boolean isTouched(float mouseX, float mouseY){
        if(bounds.contains(mouseX, mouseY) && isUnlocked)
            return true;
        return false;
    }

    public void updateBounds(){
        bounds.set(position.x, position.y, TxtLocked.getWidth(), TxtLocked.getHeight());
    }
    public Vector3 getPosition(){return position;}
    public Vector3 getLayoutPosition(){return layoutPosition;}
    public float getWidth(){
        return TxtLocked.getWidth();
    }

    public String getLevel(){ return textLevel; }

    public void render(SpriteBatch sb){
        if(isUnlocked) {
            if(isPressed)
                sb.draw(TxtUnlocked, position.x, position.y);
            else
                sb.draw(TxtUnPressed, position.x, position.y);
        }
        else
            sb.draw(TxtLocked, position.x, position.y);

        if(stars > 0)
            sb.draw(TxtStar, position.x + (TxtLocked.getWidth() /5), position.y + (TxtLocked.getHeight()/4));
        if(stars > 1)
            sb.draw(TxtStar, position.x + (TxtLocked.getWidth() /5) + TxtStar.getWidth(), position.y + (TxtLocked.getHeight()/4));
        if(stars > 2)
            sb.draw(TxtStar, position.x + (TxtLocked.getWidth() /5) + (TxtStar.getWidth()*2), position.y + (TxtLocked.getHeight()/4));
        font.draw(sb, levelName, position.x + TxtLocked.getWidth()/5, position.y + (3* TxtLocked.getHeight()/4));
    }

    public void dispose(){
        TxtStar.dispose();
        TxtLocked.dispose();
        TxtUnlocked.dispose();
        //font.dispose();
    }
}
