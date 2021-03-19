package com.robin.escape.states;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.Game;
import com.robin.escape.managers.StyleManager;
import com.robin.escape.sprites.Clickable;
import com.robin.escape.sprites.GameObject;

public class MenuState extends State{
    private StyleManager styleManager;
    private GameObject background;
    //private Clickable statsButton;
    private Clickable playButton;
    private Clickable editorButton;
    //private Clickable shopButton;
    private Viewport viewport;
    private Vector3 mousePos;
    private boolean leftKeyDown = false;
    private boolean pressed = false;


    public MenuState(GameStateManager gsm){
        super(gsm);
        styleManager = new StyleManager();

        background = new GameObject(new Vector3(0,0,0), styleManager.getStyle()+ "/mainmeny.png");
        playButton = new Clickable(new Vector3(3 * Game.WIDTH/6, Game.HEIGHT/2,0), styleManager.getStyle()+ "/button/playbuttonUnPressed.png", styleManager.getStyle()+ "/button/playbutton.png");
        playButton.getPosition().x -= playButton.getSprite().getTexture().getWidth()/2;
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            editorButton = new Clickable(new Vector3(playButton.getPosition().x, Game.HEIGHT / 2, 0),styleManager.getStyle()+ "/button/editorbuttonUnPressed.png", styleManager.getStyle()+ "/button/editorbutton.png");
            editorButton.getPosition().x += playButton.getSprite().getTexture().getWidth();
            editorButton.getPosition().y += editorButton.getSprite().getTexture().getHeight();
        }
        //statsButton = new Clickable(new Vector3(playButton.getPosition().x + playButton.getSprite().getWidth(), escape.HEIGHT / 2, 0), styleManager.getStyle()+ "/button/statsbuttonUnPressed.png", styleManager.getStyle()+ "/button/statsbutton.png");
        //shopButton = new Clickable(new Vector3(playButton.getPosition().x, escape.HEIGHT / 2, 0), styleManager.getStyle()+ "/button/shopbuttonUnPressed.png", styleManager.getStyle()+ "/button/shopbutton.png");
        //shopButton.getPosition().x -= shopButton.getSprite().getTexture().getWidth();

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
            //Gdx.app.log(playButton.getBounds() +"    ", mousePos +"");
            viewport.unproject(mousePos);
            if(playButton.getBounds().contains(mousePos.x, mousePos.y)) {
                playButton.setPressed(true);
                //shopButton.setPressed(false);
                //statsButton.setPressed(false);
            }
            /*else if(shopButton.getBounds().contains(mousePos.x, mousePos.y)) {
                playButton.setPressed(false);
                shopButton.setPressed(true);
                statsButton.setPressed(false);
            }
            else if(statsButton.getBounds().contains(mousePos.x, mousePos.y)) {
                playButton.setPressed(false);
                shopButton.setPressed(false);
                statsButton.setPressed(true);
            }*/
            else{
                playButton.setPressed(false);
                //shopButton.setPressed(false);
                //statsButton.setPressed(false);
            }
            if(Gdx.app.getType() == Application.ApplicationType.Desktop)
                if(editorButton.getBounds().contains(mousePos.x, mousePos.y)){
                    gsm.set(new EditorState(gsm));
                    dispose();
                }
        }
        else{
            if(mousePos != null) {
                if (playButton.getBounds().contains(mousePos.x, mousePos.y)) {
                    gsm.set(new LevelSelectionState(gsm));
                    dispose();
                }
                /*else if (shopButton.getBounds().contains(mousePos.x, mousePos.y)) {
                    gsm.set(new ShopState(gsm));
                    dispose();
                }
                else if (statsButton.getBounds().contains(mousePos.x, mousePos.y)) {
                    gsm.set(new StatsState(gsm));
                    dispose();
                }*/
                if(Gdx.app.getType() == Application.ApplicationType.Desktop)
                    if(editorButton.getBounds().contains(mousePos.x, mousePos.y)){
                        gsm.set(new EditorState(gsm));
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
        sb.draw(background.getSprite(), 0, 0);
        playButton.render(sb);
        //shopButton.render(sb);
        //statsButton.render(sb);
        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
            editorButton.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        background.getSprite().getTexture().dispose();
        playButton.getSprite().getTexture().dispose();
        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
            editorButton.getSprite().getTexture().dispose();

        //shopButton.getSprite().getTexture().dispose();
        //statsButton.getSprite().getTexture().dispose();
    }
}
