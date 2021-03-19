package com.robin.escape.huds;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.Game;
import com.robin.escape.managers.StyleManager;
import com.robin.escape.sprites.Clickable;
import com.robin.escape.sprites.GameObject;

import java.util.ArrayList;

public class ScoreHud {
    public enum BUTTON{ NEXT, MENU, REPLAY }
    private Matrix4 uiMatrix;
    private GameObject scoreBG;
    private BitmapFont font;
    private int time;
    private Clickable nextButton;
    private Clickable menuButton;
    private Clickable replayButton;
    private ArrayList<Integer> starCounter;
    private GameObject star;
    private GameObject starNO;
    private GlyphLayout initText;
    private String pressToContinue;

    public ScoreHud(ArrayList<Integer> starCounter, Camera camera) {
        uiMatrix = camera.combined.cpy();
        uiMatrix.setToOrtho2D(0, 0, Game.WIDTH, Game.HEIGHT);
        this.starCounter = starCounter;
        scoreBG = new GameObject(new Vector3(0,0,0), "scoreBG.png");

        StyleManager styleManager = new StyleManager();

        replayButton = new Clickable(new Vector3(Game.WIDTH/2, 10,0), styleManager.getStyle()+ "/button/replaybuttonUnPressed.png", styleManager.getStyle()+ "/button/replaybutton.png");
        replayButton.getPosition().x -= replayButton.getSprite().getTexture().getWidth()/2;
        nextButton = new Clickable(new Vector3(Game.WIDTH/2 + replayButton.getBounds().width / 2, 10,0), styleManager.getStyle()+ "/button/nextbuttonUnPressed.png", styleManager.getStyle()+ "/button/nextbutton.png");
        menuButton = new Clickable(new Vector3(replayButton.getPosition().x, 10,0), styleManager.getStyle()+ "/button/menubuttonUnPressed.png", styleManager.getStyle()+ "/button/menubutton.png");
        menuButton.getPosition().x -= menuButton.getSprite().getTexture().getWidth();


        font = new BitmapFont(Gdx.files.internal("font/GillSansBig.fnt"), Gdx.files.internal("font/GillSansBig.png"), false);
        font.setColor(0.08235f, 0.28627f, 0.49412f, 1.0f);
        initText = new GlyphLayout(font, "Finished with ");
        star = new GameObject(new Vector3(10 + initText.width, Game.HEIGHT - 90,0), "star.png");
        starNO = new GameObject(new Vector3(10 + initText.width, Game.HEIGHT - 73,0), "starNO.png");
        pressToContinue = "Press to continue..";
    }

    public void setTime(int time){
        this.time = time;
    }

    public GameObject getButton(ScoreHud.BUTTON button) {
        switch (button) {
            case NEXT:
                return nextButton;
            case MENU:
                return menuButton;
            case REPLAY:
                return replayButton;
        }
        return null;
    }

    public void render(SpriteBatch sb){
        sb.setProjectionMatrix(uiMatrix);
        sb.begin();
        sb.draw(scoreBG.getSprite(), scoreBG.getPosition().x, scoreBG.getPosition().y);
        font.draw(sb, initText, 10, Game.HEIGHT - 50);
        sb.draw(star.getSprite(), star.getPosition().x, star.getPosition().y, star.getBounds().width*2, star.getBounds().height*2);
        if(time <= starCounter.get(0))
            sb.draw(star.getSprite(), star.getPosition().x + (star.getBounds().width * 2), star.getPosition().y, star.getBounds().width*2, star.getBounds().height*2);
        if(time <= starCounter.get(1))
            sb.draw(star.getSprite(), star.getPosition().x + (star.getBounds().width * 4) , star.getPosition().y, star.getBounds().width*2, star.getBounds().height*2);

        //font.draw(sb, pressToContinue, 10, 150);

        replayButton.render(sb);
        nextButton.render(sb);
        menuButton.render(sb);

        sb.end();

    }

    public void dispose(){
        scoreBG.getSprite().getTexture().dispose();
    }

}
