package com.robin.escape.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.escape;
import com.robin.escape.managers.StyleManager;
import com.robin.escape.sprites.Clickable;
import com.robin.escape.sprites.GameObject;

public class ShopState extends State{
    private Preferences prefs;
    private StyleManager styleManager;
    private Vector3 mousePos;
    private GameObject background;
    private Clickable winter;
    private Clickable summer;
    private Clickable desert;
    private Clickable btnReturn;
    private Texture lockedSummer;
    private Viewport viewport;
    private boolean leftKeyDown;
    private int collectedStars;


    public ShopState(GameStateManager gsm){
        super(gsm);
        prefs = Gdx.app.getPreferences("GameProgress");
        styleManager = new StyleManager();
        leftKeyDown = false;

        for(int i=1; i<16; i++) {
            collectedStars += prefs.getInteger("starlevels/level" + i + ".json", 0);
            //Gdx.app.log("" + collectedStars, "");
        }

        background = new GameObject(new Vector3(0,0,0), styleManager.getStyle() + "/levelselect.png");
        winter = new Clickable(new Vector3(3 * escape.WIDTH/6, 7*escape.HEIGHT/8 ,0), "bg/normalStyleUnPressed.png", "bg/normalStyle.png");
        winter.getPosition().x -= winter.getSprite().getTexture().getWidth()/2;
        summer = new Clickable(new Vector3(3 * escape.WIDTH/6, winter.getPosition().y - winter.getSprite().getTexture().getHeight() ,0),"bg/rainbowStyleUnPressed.png",  "bg/rainbowStyle.png");
        summer.getPosition().x -= summer.getSprite().getTexture().getWidth()/2;
        desert = new Clickable(new Vector3(3 * escape.WIDTH/6, summer.getPosition().y - summer.getSprite().getTexture().getHeight() ,0),"bg/desertStyleUnPressed.png",  "bg/desertStyle.png");
        desert.getPosition().x -= summer.getSprite().getTexture().getWidth()/2;
        lockedSummer = new Texture("bg/rainbowStyleLocked.png");
        btnReturn = new Clickable(new Vector3(3 * escape.WIDTH/6, 4*escape.HEIGHT/8 ,0), styleManager.getStyle()+ "/button/menyReturnUnPressed.png", styleManager.getStyle()+ "/button/menyReturn.png");
        btnReturn.getPosition().x -= btnReturn.getSprite().getTexture().getWidth()/2;

        if(prefs.getString("style", "normal").equals("normal")) {
            winter.setPressed(true);
            summer.setPressed(false);
            desert.setPressed(false);
        }
        else if(prefs.getString("style", "normal").equals("summer")) {
            winter.setPressed(false);
            summer.setPressed(true);
            desert.setPressed(false);
        }
        else if(prefs.getString("style", "normal").equals("desert")) {
            winter.setPressed(false);
            summer.setPressed(false);
            desert.setPressed(true);
        }

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

    public void inputActions(){
        if(leftKeyDown) {
            mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mousePos);
            if(btnReturn.getBounds().contains(mousePos.x, mousePos.y))
                btnReturn.setPressed(true);
            else if(winter.getBounds().contains(mousePos.x, mousePos.y)) {
                prefs.putString("style", "normal");
                prefs.flush();
                gsm.set(new ShopState(gsm));
            }
            else if(summer.getBounds().contains(mousePos.x, mousePos.y)) {
                if(collectedStars >= 20) {
                    prefs.putString("style", "summer");
                    prefs.flush();
                    gsm.set(new ShopState(gsm));
                }
            }
            else if(desert.getBounds().contains(mousePos.x, mousePos.y)) {
                if(collectedStars >= 20) {
                    prefs.putString("style", "desert");
                    prefs.flush();
                    //Gdx.app.log("desert","");
                    gsm.set(new ShopState(gsm));
                }
            }
            else
                btnReturn.setPressed(false);
        }else{
            if(mousePos != null) {
                if(btnReturn.getBounds().contains(mousePos.x, mousePos.y))
                    gsm.set(new MenuState(gsm));
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        inputActions();
    }

    @Override
    public void render(SpriteBatch sb) {
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background.getSprite(), 0, 0);
        winter.render(sb);
        if(collectedStars >= 20)
            summer.render(sb);
        else
            sb.draw(lockedSummer, summer.getPosition().x, summer.getPosition().y);
        desert.render(sb);

        btnReturn.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        background.getSprite().getTexture().dispose();
        winter.getSprite().getTexture().dispose();
        summer.getSprite().getTexture().dispose();
        desert.getSprite().getTexture().dispose();
        btnReturn.getSprite().getTexture().dispose();
    }
}
