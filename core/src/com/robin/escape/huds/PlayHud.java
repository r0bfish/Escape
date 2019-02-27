package com.robin.escape.huds;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.escape;
import com.robin.escape.managers.StyleManager;
import com.robin.escape.sprites.Clickable;
import com.robin.escape.sprites.GameObject;

import java.util.ArrayList;

public class PlayHud {
    public enum BUTTON{ PAUSE }
    private Clickable pauseButton;
    private Matrix4 uiMatrix;
    private GameObject hudBG;
    private BitmapFont font;
    private int time;
    private ArrayList<Integer> starCounter;
    private GameObject star;
    private GameObject starNO;
    private GlyphLayout initText;
    private GlyphLayout timeText;

    public PlayHud(ArrayList<Integer> starCounter, Camera camera) {
        uiMatrix = new Matrix4();
        uiMatrix = camera.combined.cpy();
        uiMatrix.setToOrtho2D(0, 0, escape.WIDTH, escape.HEIGHT);
        pauseButton = new Clickable(new Vector3(escape.WIDTH - 60,escape.HEIGHT - 60,0), "navigation.png", "navigation.png");
        this.starCounter = starCounter;
        hudBG = new GameObject(new Vector3(0,escape.HEIGHT-60,0), "bg/playHudBg.png");
        font = new BitmapFont(Gdx.files.internal("font/GillSansMedium.fnt"), Gdx.files.internal("font/GillSansMedium.png"), false);
        font.setColor(0.08235f, 0.28627f, 0.49412f, 1.0f);
        initText = new GlyphLayout(font, "Time left for ");
        timeText = new GlyphLayout(font, "");
        star = new GameObject(new Vector3(20 + initText.width,escape.HEIGHT - 42,0), "star.png");
        starNO = new GameObject(new Vector3(20 + initText.width,escape.HEIGHT - 42,0), "starNO.png");
    }

    public GameObject getButton(PlayHud.BUTTON button) {
        switch (button) {
            case PAUSE:
                return pauseButton;
        }
        return null;
    }

    public void setTime(int time){
        this.time = time;

        if(this.time <= starCounter.get(1))
            timeText.setText(font, Integer.toString(starCounter.get(1) - this.time));
        else if(this.time <= starCounter.get(0))
            timeText.setText(font, Integer.toString(starCounter.get(0) - this.time));
        else
            timeText.setText(font, " ");

    }

    public void render(SpriteBatch sb){
        sb.setProjectionMatrix(uiMatrix);
        sb.begin();
        sb.draw(hudBG.getSprite(), hudBG.getPosition().x, hudBG.getPosition().y);

        int fontHeight = 20;
        font.draw(sb, initText,  fontHeight, escape.HEIGHT - fontHeight);
        sb.draw(star.getSprite(), star.getPosition().x, star.getPosition().y);

        if(time <= starCounter.get(1)) {
            sb.draw(star.getSprite(), star.getPosition().x + (star.getBounds().width    ), star.getPosition().y);
            sb.draw(star.getSprite(), star.getPosition().x + (star.getBounds().width * 2), star.getPosition().y);
            //font.draw(sb, ": " + (starCounter.get(1) - Math.round(time)) + "s",  star.getPosition().x + (star.getBounds().width * 3), escape.HEIGHT - 10);
        }
        else if(time <= starCounter.get(0)) {
            sb.draw(star.getSprite(), star.getPosition().x + star.getBounds().width, star.getPosition().y);
            sb.draw(starNO.getSprite(), starNO.getPosition().x + (starNO.getBounds().width * 2), starNO.getPosition().y);
            //font.draw(sb, ": " + (starCounter.get(0) - Math.round(time)) + "s",  star.getPosition().x + (star.getBounds().width * 3), escape.HEIGHT - 10);

        }
        else {
            sb.draw(starNO.getSprite(), starNO.getPosition().x + starNO.getBounds().width, starNO.getPosition().y);
            sb.draw(starNO.getSprite(), starNO.getPosition().x + (starNO.getBounds().width * 2), starNO.getPosition().y);
            //font.draw(sb, ": ", star.getPosition().x + (star.getBounds().width * 3), escape.HEIGHT - 10);
        }
        font.draw(sb, ": ", star.getPosition().x + (star.getBounds().width * 3) + 6, escape.HEIGHT - fontHeight);
        font.draw(sb, timeText, star.getPosition().x + (star.getBounds().width * 3) + 12, escape.HEIGHT - fontHeight);
        font.draw(sb, "s", star.getPosition().x + (star.getBounds().width * 3) + timeText.width + 12, escape.HEIGHT - fontHeight);

        pauseButton.render(sb);
        sb.end();
    }

    public void dispose(){
        hudBG.getSprite().getTexture().dispose();
    }

}
