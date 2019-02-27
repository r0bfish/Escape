package com.robin.escape.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Clickable extends GameObject {
    protected boolean isPressed;
    private Texture pressedTexture;

    public Clickable(Vector3 position, String unPressed, String Pressed) {
        super(position, unPressed);
        isPressed = false;
        if(Pressed != null)
            pressedTexture = new Texture(Pressed);
    }

    public void setPressed(boolean pressed) { isPressed = pressed; }
    public void update(float dt){}
    public void render(SpriteBatch sb){
        if(isPressed)
            sb.draw(pressedTexture, position.x, position.y);
        else
            sb.draw(sprite.getTexture(), position.x, position.y);
    }
}
