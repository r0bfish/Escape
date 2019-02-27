package com.robin.escape.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.managers.PropsManager;

public class Props extends GameObject{
    private PropsManager.PROPS propType;

    public Props(Vector3 position, Texture texture, PropsManager.PROPS propType, Rectangle boxOffsetPos) {
        super(position, null);
        this.propType = propType;
        this.sprite = new Sprite(texture);
        bounds = new Rectangle(position.x, position.y, this.sprite.getWidth(), this.sprite.getHeight());
        if(boxOffsetPos != null)
            this.boxOffsetPos = boxOffsetPos;
        else
            this.boxOffsetPos = new Rectangle(0,0,0,0);
    }

    public PropsManager.PROPS getPropType(){ return propType; }
    public void render(SpriteBatch sb){
        sb.draw(sprite.getTexture(), position.x, position.y);
    }

}
