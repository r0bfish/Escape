package com.robin.escape.states;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.escape;
import com.robin.escape.managers.StyleManager;
import com.robin.escape.sprites.ButtonMeny;
import com.robin.escape.sprites.Clickable;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.utilities.FontHandler;

import java.util.ArrayList;

public class LevelSelectionState extends State{
    private StyleManager styleManager;
    private Vector3 mousePos;
    private GameObject background;
    private ArrayList<ButtonMeny> buttons1;
    private ArrayList<ButtonMeny> buttons2;
    private ArrayList<ButtonMeny> buttons3;
    private int state = 0;
    private Clickable btnReturn;
    private Clickable btnLeft;
    private Clickable btnRight;
    private Viewport viewport;
    private Preferences prefs;
    private BitmapFont font;
    private boolean leftKeyDown;

    public LevelSelectionState(GameStateManager gsm){
        super(gsm);

        leftKeyDown = false;
        prefs = Gdx.app.getPreferences("GameProgress");
        state = prefs.getInteger("levelSelectionState", 0);
        styleManager = new StyleManager();
        font = new BitmapFont(Gdx.files.internal("font/GillSansSM.fnt"), Gdx.files.internal("font/GillSansSM.png"), false);
        font.setColor(0.392f, 0.675f, 0.808f, 1.0f);

        Texture btnLocked = new Texture(styleManager.getStyle()+ "/button/BLOCKEDsnowBtn.png");
        Texture btnUnlocked = new Texture(styleManager.getStyle()+ "/button/snowBtn.png");
        Texture btnUnPressed = new Texture(styleManager.getStyle()+ "/button/snowBtnUnPressed.png");
        Texture btnUnlockedPurp = new Texture(styleManager.getStyle()+ "/button/snowBtnPurp.png");
        Texture btnUnPressedPurp = new Texture(styleManager.getStyle()+ "/button/snowBtnUnPressedPurp.png");
        Texture btnUnlockedPink = new Texture(styleManager.getStyle()+ "/button/snowBtnPink.png");
        Texture btnUnPressedPink = new Texture(styleManager.getStyle()+ "/button/snowBtnUnPressedPink.png");
        Texture star = new Texture("star.png");

        buttons1 = new ArrayList<>();
        buttons1.add(new ButtonMeny(new Vector3(1 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level1.json", 0),                                          true, "Level: 1.1", "levels/level1.json"));
        buttons1.add(new ButtonMeny(new Vector3(3 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level2.json", 0), prefs.getBoolean("levels/level1.json", false), "Level: 1.2", "levels/level2.json"));
        buttons1.add(new ButtonMeny(new Vector3(5 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level3.json", 0), prefs.getBoolean("levels/level2.json", false), "Level: 1.3", "levels/level3.json"));
        buttons1.add(new ButtonMeny(new Vector3(1 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level4.json", 0), prefs.getBoolean("levels/level3.json", false), "Level: 1.4", "levels/level4.json"));
        buttons1.add(new ButtonMeny(new Vector3(3 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level5.json", 0), prefs.getBoolean("levels/level4.json", false), "Level: 1.5", "levels/level5.json"));
        buttons1.add(new ButtonMeny(new Vector3(5 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level6.json", 0), prefs.getBoolean("levels/level5.json", false), "Level: 1.6", "levels/level6.json"));
        buttons1.add(new ButtonMeny(new Vector3(1 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level7.json", 0), prefs.getBoolean("levels/level6.json", false), "Level: 1.7", "levels/level7.json"));
        buttons1.add(new ButtonMeny(new Vector3(3 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level8.json", 0), prefs.getBoolean("levels/level7.json", false), "Level: 1.8", "levels/level8.json"));
        buttons1.add(new ButtonMeny(new Vector3(5 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressed, btnUnlocked, btnLocked, star, font, prefs.getInteger("starlevels/level9.json", 0), prefs.getBoolean("levels/level8.json", false), "Level: 1.9", "levels/level9.json"));

        buttons2 = new ArrayList<>();
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 1 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level11.json", 0), prefs.getBoolean("levels/level9.json" , false), "Level: 2.1", "levels/level11.json"));
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 3 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level12.json", 0), prefs.getBoolean("levels/level11.json", false), "Level: 2.2", "levels/level12.json"));
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 5 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level13.json", 0), prefs.getBoolean("levels/level12.json", false), "Level: 2.3", "levels/level13.json"));
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 1 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level14.json", 0), prefs.getBoolean("levels/level13.json", false), "Level: 2.4", "levels/level14.json"));
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 3 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level15.json", 0), prefs.getBoolean("levels/level14.json", false), "Level: 2.5", "levels/level15.json"));
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 5 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level16.json", 0), prefs.getBoolean("levels/level15.json", false), "Level: 2.6", "levels/level16.json"));
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 1 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level17.json", 0), prefs.getBoolean("levels/level16.json", false), "Level: 2.7", "levels/level17.json"));
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 3 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level18.json", 0), prefs.getBoolean("levels/level17.json", false), "Level: 2.8", "levels/level18.json"));
        buttons2.add(new ButtonMeny(new Vector3(escape.WIDTH+ 5 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressedPurp, btnUnlockedPurp, btnLocked, star, font, prefs.getInteger("starlevels/level19.json", 0), prefs.getBoolean("levels/level18.json", false), "Level: 2.9", "levels/level19.json"));

        buttons3 = new ArrayList<>();
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 1 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level21.json", 0), prefs.getBoolean("levels/level19.json", false), "Level: 3.1", "levels/level21.json"));
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 3 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level22.json", 0), prefs.getBoolean("levels/level21.json", false), "Level: 3.2", "levels/level22.json"));
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 5 * escape.WIDTH/6, 7*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level23.json", 0), prefs.getBoolean("levels/level22.json", false), "Level: 3.3", "levels/level23.json"));
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 1 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level24.json", 0), prefs.getBoolean("levels/level23.json", false), "Level: 3.4", "levels/level24.json"));
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 3 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level25.json", 0), prefs.getBoolean("levels/level24.json", false), "Level: 3.5", "levels/level25.json"));
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 5 * escape.WIDTH/6, 6*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level26.json", 0), prefs.getBoolean("levels/level25.json", false), "Level: 3.6", "levels/level26.json"));
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 1 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level27.json", 0), prefs.getBoolean("levels/level26.json", false), "Level: 3.7", "levels/level27.json"));
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 3 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level28.json", 0), prefs.getBoolean("levels/level27.json", false), "Level: 3.8", "levels/level28.json"));
        buttons3.add(new ButtonMeny(new Vector3((2 * escape.WIDTH)+ 5 * escape.WIDTH/6, 5*escape.HEIGHT/8,0) , btnUnPressedPink, btnUnlockedPink, btnLocked, star, font, prefs.getInteger("starlevels/level29.json", 0), prefs.getBoolean("levels/level28.json", false), "Level: 3.9", "levels/level29.json"));

        for(ButtonMeny bm : buttons1) bm.getPosition().x += (((-escape.WIDTH * state) + (bm.getLayoutPosition().x) - bm.getPosition().x)) * 1.0f;
        for(ButtonMeny bm : buttons2) bm.getPosition().x += (((-escape.WIDTH * state) + (bm.getLayoutPosition().x) - bm.getPosition().x)) * 1.0f;
        for(ButtonMeny bm : buttons3) bm.getPosition().x += (((-escape.WIDTH * state) + (bm.getLayoutPosition().x) - bm.getPosition().x)) * 1.0f;

        background = new GameObject(new Vector3(0,0,0), styleManager.getStyle() + "/levelselect.png");
        btnReturn = new Clickable(new Vector3(3 * escape.WIDTH/6, 4*escape.HEIGHT/8 ,0), styleManager.getStyle()+ "/button/menyReturnUnPressed.png", styleManager.getStyle()+ "/button/menyReturn.png");
        btnReturn.getPosition().x -= btnReturn.getSprite().getTexture().getWidth()/2;
        btnLeft = new Clickable(new Vector3(btnReturn.getPosition().x, 4*escape.HEIGHT/8 ,0), styleManager.getStyle()+ "/button/arrowLeftUnPressed.png", styleManager.getStyle()+ "/button/arrowLeft.png");
        btnLeft.getPosition().x -= btnLeft.getBounds().width;
        btnRight = new Clickable(new Vector3(btnReturn.getPosition().x + btnReturn.getBounds().width, 4*escape.HEIGHT/8 ,0), styleManager.getStyle()+ "/button/arrowRightUnPressed.png", styleManager.getStyle()+ "/button/arrowRight.png");

        camera.setToOrtho(false, escape.WIDTH, escape.HEIGHT);
        viewport = new StretchViewport(escape.WIDTH, escape.HEIGHT, camera);
        viewport.apply();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


    @Override
    public void handleInput() {
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                leftKeyDown = true;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                leftKeyDown = false;
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                return false;
            }
            @Override
            public boolean keyUp(int keycode) {
                return false;
            }
            @Override
            public boolean keyTyped(char character) {
                return false;
            }
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }
            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });
    }

    public void inputActions() {
        if (leftKeyDown) {
            mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mousePos);
            boolean pressed = false;
            switch (state){
                case 0:
                    for(ButtonMeny bm : buttons1) {
                        if (bm.isTouched(mousePos.x, mousePos.y)) {
                            pressed = true;
                            for (ButtonMeny innerBm : buttons1)
                                innerBm.setPressed(false);
                            bm.setPressed(true);
                        }
                    }
                    if(!pressed)
                        for (ButtonMeny bm : buttons1)
                            bm.setPressed(false);
                    break;
                case 1:
                    for(ButtonMeny bm : buttons2) {
                        if (bm.isTouched(mousePos.x, mousePos.y)) {
                            pressed = true;
                            for (ButtonMeny innerBm : buttons2)
                                innerBm.setPressed(false);
                            bm.setPressed(true);
                        }
                    }
                    if(!pressed)
                        for (ButtonMeny bm : buttons2)
                            bm.setPressed(false);
                    break;
                case 2:
                    for(ButtonMeny bm : buttons3) {
                        if (bm.isTouched(mousePos.x, mousePos.y)) {
                            pressed = true;
                            for (ButtonMeny innerBm : buttons3)
                                innerBm.setPressed(false);
                            bm.setPressed(true);
                        }
                    }
                    if(!pressed)
                        for (ButtonMeny bm : buttons3)
                            bm.setPressed(false);
                    break;
            }
            if (btnReturn.getBounds().contains(mousePos.x, mousePos.y)) {
                btnReturn.setPressed(true);
                btnLeft.setPressed(false);
                btnRight.setPressed(false);
            } else if (btnLeft.getBounds().contains(mousePos.x, mousePos.y)) {
                btnReturn.setPressed(false);
                btnLeft.setPressed(true);
                btnRight.setPressed(false);
            } else if (btnRight.getBounds().contains(mousePos.x, mousePos.y)) {
                btnReturn.setPressed(false);
                btnLeft.setPressed(false);
                btnRight.setPressed(true);
            } else {
                btnReturn.setPressed(false);
                btnLeft.setPressed(false);
                btnRight.setPressed(false);
            }
        } else {
            for(ButtonMeny bm : buttons1)
                bm.setPressed(false);
            for(ButtonMeny bm : buttons2)
                bm.setPressed(false);
            for(ButtonMeny bm : buttons3)
                bm.setPressed(false);
            btnReturn.setPressed(false);
            btnLeft.setPressed(false);
            btnRight.setPressed(false);
            if (mousePos != null) {
                switch (state){
                    case 0:
                        for(ButtonMeny bm : buttons1)
                            if (bm.isTouched(mousePos.x, mousePos.y))
                                gsm.push(new PlayState(gsm, bm.getLevel()));
                        break;
                    case 1:
                        for(ButtonMeny bm : buttons2)
                            if (bm.isTouched(mousePos.x, mousePos.y))
                                gsm.push(new PlayState(gsm, bm.getLevel()));
                        break;
                    case 2:
                        for(ButtonMeny bm : buttons3)
                            if (bm.isTouched(mousePos.x, mousePos.y))
                                gsm.push(new PlayState(gsm, bm.getLevel()));
                        break;
                }

                if (btnReturn.getBounds().contains(mousePos.x, mousePos.y)) {
                    gsm.push(new MenuState(gsm));
                    dispose();
                } else if (btnLeft.getBounds().contains(mousePos.x, mousePos.y)) {
                    state -= 1;
                    mousePos.setZero();
                    if(state < 0) state = 0;
                    prefs.putInteger("levelSelectionState", state);
                    prefs.flush();
                } else if (btnRight.getBounds().contains(mousePos.x, mousePos.y)) {
                    state += 1;
                    mousePos.setZero();
                    if(state > 2) state = 2;
                    prefs.putInteger("levelSelectionState", state);
                    prefs.flush();
                }
            }
        }
    }

    public void transition() {
        for (ButtonMeny bm : buttons1) {
            bm.getPosition().x += (((-escape.WIDTH * state) + (bm.getLayoutPosition().x) - bm.getPosition().x)) * 0.15f;
            bm.updateBounds();
        }
        for (ButtonMeny bm : buttons2) {
            bm.getPosition().x += (((-escape.WIDTH * state) + (bm.getLayoutPosition().x) - bm.getPosition().x)) * 0.15f;
            bm.updateBounds();
        }
        for (ButtonMeny bm : buttons3) {
            bm.getPosition().x += (((-escape.WIDTH * state) + (bm.getLayoutPosition().x) - bm.getPosition().x)) * 0.15f;
            bm.updateBounds();
        }
    }

        @Override
    public void update(float dt) {
        transition();
        handleInput();
        inputActions();
    }

    @Override
    public void render(SpriteBatch sb) {

        camera.update();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background.getSprite(), 0, 0);
        for(ButtonMeny bm : buttons1)
            bm.render(sb);
        for(ButtonMeny bm : buttons2)
            bm.render(sb);
        for(ButtonMeny bm : buttons3)
            bm.render(sb);

        btnReturn.render(sb);
        btnLeft.render(sb);
        btnRight.render(sb);
        sb.end();

    }

    @Override
    public void dispose() {
        background.getSprite().getTexture().dispose();
        for(ButtonMeny bm : buttons1)
            bm.dispose();
        for(ButtonMeny bm : buttons2)
            bm.dispose();
        for(ButtonMeny bm : buttons3)
            bm.dispose();
        btnReturn.getSprite().getTexture().dispose();
        btnRight.getSprite().getTexture().dispose();
        font.dispose();
    }

}
