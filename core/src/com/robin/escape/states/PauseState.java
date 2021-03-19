package com.robin.escape.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.Game;
import com.robin.escape.managers.LevelManager;
import com.robin.escape.managers.StyleManager;
import com.robin.escape.sprites.Clickable;
import com.robin.escape.sprites.GameObject;


public class PauseState extends State {
    private LevelManager levelManager;

    private GameObject background;
    private Clickable continueButton;
    private Clickable menuButton;
    private Clickable replayButton;

    private BitmapFont font;
    private String pausedString = "Game paused";

    private Viewport viewport;
    private Vector3 mousePos;
    private boolean leftKeyDown = false;

    public PauseState(GameStateManager gsm, LevelManager levelManager) {
        super(gsm);
        gsm.setRenderPrev(true);
        this.levelManager = levelManager;
        StyleManager styleManager = new StyleManager();
        font = new BitmapFont(Gdx.files.internal("font/GillSansBig.fnt"), Gdx.files.internal("font/GillSansBig.png"), false);
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        background = new GameObject(new Vector3(0,0,0), "pausemeny.png");
        replayButton = new Clickable(new Vector3(Game.WIDTH/2, 10,0), styleManager.getStyle()+ "/button/replaybuttonUnPressed.png", styleManager.getStyle()+ "/button/replaybutton.png");
        replayButton.getPosition().x -= replayButton.getSprite().getTexture().getWidth()/2;
        continueButton = new Clickable(new Vector3(Game.WIDTH/2 + replayButton.getBounds().width / 2, 10,0), styleManager.getStyle()+ "/button/continueUnPressed.png", styleManager.getStyle()+ "/button/continue.png");
        menuButton = new Clickable(new Vector3(replayButton.getPosition().x, 10,0), styleManager.getStyle()+ "/button/menubuttonUnPressed.png", styleManager.getStyle()+ "/button/menubutton.png");
        menuButton.getPosition().x -= menuButton.getSprite().getTexture().getWidth();

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
            if(continueButton.getBounds().contains(mousePos.x, mousePos.y)) {
                continueButton.setPressed(true);
                replayButton.setPressed(false);
                menuButton.setPressed(false);
            }
            else if(replayButton.getBounds().contains(mousePos.x, mousePos.y)) {
                continueButton.setPressed(false);
                replayButton.setPressed(true);
                menuButton.setPressed(false);
            }
            else if(menuButton.getBounds().contains(mousePos.x, mousePos.y)) {
                continueButton.setPressed(false);
                replayButton.setPressed(false);
                menuButton.setPressed(true);
            }
            else{
                continueButton.setPressed(false);
                replayButton.setPressed(false);
                menuButton.setPressed(false);
            }
        }
        else{
            if(mousePos != null) {
                if (continueButton.getBounds().contains(mousePos.x, mousePos.y)) {
                    gsm.setRenderPrev(false);
                    gsm.pop();
                    dispose();
                }
                /*else if (replayButton.getBounds().contains(mousePos.x, mousePos.y)) {
                    gsm.setRenderPrev(false);
                    gsm.disposePrevState();
                    gsm.pop();
                    gsm.push(new PlayState(gsm, levelManager.getLevelName()));
                    dispose();
                }*/
                else if (menuButton.getBounds().contains(mousePos.x, mousePos.y)) {
                    gsm.setRenderPrev(false);
                    gsm.disposePrevState();
                    gsm.pop();
                    gsm.push(new MenuState(gsm));
                    dispose();
                }
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

        sb.draw(background.getSprite(), background.getPosition().x, background.getPosition().y);
        continueButton.render(sb);
        //replayButton.render(sb);
        menuButton.render(sb);

        font.draw(sb, pausedString, 100, Game.HEIGHT/2);

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        continueButton.dispose();
        replayButton.dispose();
        menuButton.dispose();
        font.dispose();
    }
}
