package com.robin.escape.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.Game;
import com.robin.escape.managers.StyleManager;
import com.robin.escape.sprites.Clickable;
import com.robin.escape.sprites.GameObject;

public class StatsState extends State{
    private StyleManager styleManager;
    private Vector3 mousePos;
    private GameObject background;
    private Clickable btnReturn;
    private Viewport viewport;
    private BitmapFont font;
    private Preferences prefs;
    private boolean leftKeyDown;


    public StatsState(GameStateManager gsm){
        super(gsm);
        prefs = Gdx.app.getPreferences("GameProgress");
        styleManager = new StyleManager();

        font = new BitmapFont(Gdx.files.internal("font/GillSansMedium.fnt"), Gdx.files.internal("font/GillSansMedium.png"), false);
        font.setColor(0.392f, 0.675f, 0.808f, 1.0f);

        background = new GameObject(new Vector3(0,0,0), styleManager.getStyle() + "/levelselect.png");
        btnReturn = new Clickable(new Vector3(3 * Game.WIDTH/6, 4* Game.HEIGHT/8 ,0), styleManager.getStyle()+ "/button/menyReturnUnPressed.png", styleManager.getStyle()+ "/button/menyReturn.png");
        btnReturn.getPosition().x -= btnReturn.getSprite().getTexture().getWidth()/2;

        camera.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        viewport = new StretchViewport(Game.WIDTH, Game.HEIGHT, camera);
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
        btnReturn.render(sb);
        font.draw(sb, "Total jumps: " +  prefs.getInteger("totalJumps", 0) +
                "\nTotal restarts: " + prefs.getInteger("totalRestarts", 0),
                2 * Game.WIDTH/7, 7* Game.HEIGHT/8);
        sb.end();
    }

    @Override
    public void dispose() {
        background.getSprite().getTexture().dispose();
        btnReturn.getSprite().getTexture().dispose();
    }
}
