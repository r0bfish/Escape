package com.robin.escape.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.escape;
import com.robin.escape.huds.EditorHud;
import com.robin.escape.managers.LevelManager;
import com.robin.escape.managers.ObjectManager;
import com.robin.escape.sprites.Enemy;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.sprites.MovingObject;
import com.robin.escape.sprites.Props;
import com.robin.escape.utilities.Collision;
import com.robin.escape.utilities.Textlistener;

public class EditorState extends State {
    private EditorHud hud;
    private LevelManager levelManager;
    private GameObject selectedGameobject;
    private boolean leftKeyDown;
    private boolean rightKeyDown;
    private boolean scrollDown;
    private boolean mouseMove;
    private Vector3 startMouse;
    private Vector3 endMouse;
    private Viewport viewport;
    private Textlistener textlistener;

    public EditorState(GameStateManager gsm) {
        super(gsm);
        levelManager = new LevelManager();
        hud = new EditorHud(0,0);
        textlistener = new Textlistener();

        leftKeyDown = false;
        rightKeyDown = false;
        scrollDown = false;

        camera.setToOrtho(false, escape.WIDTH*2, escape.HEIGHT);
        viewport = new ExtendViewport(escape.WIDTH *2, escape.HEIGHT, camera);
        Gdx.graphics.setWindowedMode(escape.WIDTH*2, escape.HEIGHT);
        viewport.apply();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    @Override
    public void handleInput() {
        Gdx.input.setInputProcessor(new InputProcessor(){
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
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT)
                    leftKeyDown = true;
                if (button == Input.Buttons.RIGHT) {
                    rightKeyDown = true;
                    startMouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    viewport.unproject(startMouse);
                    //Gdx.app.log(""+startMouse,"");
                }
                if (button == Input.Buttons.MIDDLE)
                    scrollDown = true;

                return true;
            }
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT)
                    leftKeyDown = false;
                if (button == Input.Buttons.RIGHT) {
                    rightKeyDown = false;
                    endMouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    viewport.unproject(endMouse);
                }
                if (button == Input.Buttons.MIDDLE)
                    scrollDown = false;

                return true;
            }
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(rightKeyDown) {
                    mouseMove = true;
                    endMouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    viewport.unproject(endMouse);
                    return true;
                }
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

        if (leftKeyDown) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mousePos);

            // Select tile/logic/props/object depending on the current selected manager
            levelManager.setPosOnSet(mousePos);

            // Export the level to json
            if(hud.getButton(EditorHud.BUTTON.EXPORT).getBounds().contains(mousePos.x, mousePos.y)){
                Gdx.input.getTextInput(textlistener, "Export level", null, "level.json");
                textlistener.setHeader("export");
                leftKeyDown = false;
                selectedGameobject = null;
            }
            // Import the level from json
            else if(hud.getButton(EditorHud.BUTTON.IMPORT).getBounds().contains(mousePos.x, mousePos.y)){
                Gdx.input.getTextInput(textlistener, "Import level", null, "level.json");
                textlistener.setHeader("import");
                leftKeyDown = false;
                selectedGameobject = null;
            }

            // Set terrain's as the current manager
            else if(hud.getButton(EditorHud.BUTTON.TERRAIN).getBounds().contains(mousePos.x, mousePos.y)){
                levelManager.setFocus(LevelManager.MANAGER.TERRAINMANAGER, mousePos);
                leftKeyDown = false;
                selectedGameobject = null;
            }

            // Set object's as the current manager
            else if(hud.getButton(EditorHud.BUTTON.OBJECT).getBounds().contains(mousePos.x, mousePos.y)){
                levelManager.setFocus(LevelManager.MANAGER.OBJECTMANAGER, mousePos);
                leftKeyDown = false;
                selectedGameobject = null;
            }

            // Set logic's as the current manager
            else if(hud.getButton(EditorHud.BUTTON.LOGIC).getBounds().contains(mousePos.x, mousePos.y)){
                levelManager.setFocus(LevelManager.MANAGER.LOGICMANAGER, mousePos);
                leftKeyDown = false;
                selectedGameobject = null;
            }

            // Set prop's as the current manager
            else if(hud.getButton(EditorHud.BUTTON.PROPS).getBounds().contains(mousePos.x, mousePos.y)){
                levelManager.setFocus(LevelManager.MANAGER.PROPSMANAGER, mousePos);
                leftKeyDown = false;
                selectedGameobject = null;
            }
            else if(hud.getButton(EditorHud.BUTTON.RETURN).getBounds().contains(mousePos.x, mousePos.y)){
                camera.setToOrtho(false, escape.WIDTH, escape.HEIGHT);
                viewport = new StretchViewport(escape.WIDTH, escape.HEIGHT, camera);
                Gdx.graphics.setWindowedMode(escape.WIDTH, escape.HEIGHT);
                viewport.apply();
                gsm.set(new MenuState(gsm));
            }
            else {
                // #############
                // Select enemy, for future patrol
                for (GameObject go : levelManager.getObjectManager().getObjects()) {
                    if (Collision.isPointBoundDetected(mousePos, go.getBounds())) {
                        if(go instanceof MovingObject)
                            if(go.getType() == ObjectManager.TYPE.ENEMY){
                                selectedGameobject = go;
                            }

                    }
                }
            }

        }

        else if(mouseMove && !rightKeyDown){
            if (startMouse != null && endMouse != null)
                levelManager.addOnLevel(startMouse, endMouse);

            mouseMove = false;
        }

        else if(rightKeyDown){
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mousePos);

            //editorCreator.objectAction(new Vector3(mousePos.x, mousePos.y,0));
            //##########
            // Check if enemy is selected, and make the unit patrol
            //##########

            switch (levelManager.getSelectedManager()){
                case OBJECTMANAGER:
                    rightKeyDown = false;
                    break;
                case PROPSMANAGER:
                    rightKeyDown = false;
                    break;
            }

            // Add stuff, depending on which manager selected
            if(selectedGameobject == null)
                levelManager.addOnLevel(mousePos, null);
            else
                ((Enemy)selectedGameobject).patrol(selectedGameobject.getPosition(),
                        new Vector3((mousePos.x - (mousePos.x % escape.TILEWIDTH)),
                                (mousePos.y - (mousePos.y % escape.TILEWIDTH)),
                                0));
        }
        else if(scrollDown){
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mousePos);
            //Gdx.app.log("" + mousePos, "");
            //scrollDown = false;
            switch (levelManager.getSelectedManager()){
                case TERRAINMANAGER:
                    for(GameObject a : levelManager.getTilesetManager().getTiles())
                        if(Collision.isPointBoundDetected(mousePos, new Rectangle(
                                a.getPosition().x, a.getPosition().y,
                                escape.TILEWIDTH, escape.TILEHEIGHT
                                ))) {
                            levelManager.getTilesetManager().getTiles().remove(a);
                            break;
                        }
                    break;
                case LOGICMANAGER:
                    for(GameObject a : levelManager.getLogicManager().getLogicAreas())
                        if(Collision.isPointBoundDetected(mousePos, a.getBounds())) {
                            levelManager.getLogicManager().getLogicAreas().remove(a);
                            break;
                        }
                    break;
                case OBJECTMANAGER:
                    for(GameObject go : levelManager.getObjectManager().getObjects())
                        if(Collision.isPointBoundDetected(mousePos, go.getBounds())) {
                            levelManager.getObjectManager().getObjects().remove(go);
                            break;
                        }
                    break;
                case PROPSMANAGER:
                    for(Props p : levelManager.getPropsManager().getProps())
                        if(Collision.isPointBoundDetected(mousePos, p.getBounds())) {
                            levelManager.getPropsManager().getProps().remove(p);
                            break;
                        }
                    break;
            }
        }
    }

    public void textHandler(){
        if (textlistener.getInputText() != null)
        {
            if(textlistener.getHeader().equals("import"))
                levelManager.importLevel(textlistener.getInputText());
            else
                levelManager.exportLevel(textlistener.getInputText());
            textlistener.resetInputText();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        inputActions();
        textHandler();
        for(GameObject obj : levelManager.getObjectManager().getObjects())
            obj.update(dt);
        levelManager.update(dt);
        //for(int i=0; i<levelManager.getEnemySpawns().size(); i++)
        //    levelManager.getEnemySpawns().get(i).update(dt);



    }
    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        levelManager.getTilesetManager().render(sb);
        levelManager.getLogicManager().render(sb);
        levelManager.getPropsManager().render(sb);
        levelManager.getObjectManager().render(sb);
        for(int i=0; i<levelManager.getEnemySpawns().size(); i++) {
            for (int j = 0; j < levelManager.getEnemySpawns().get(i).getEnemies().size(); j++)
                levelManager.getEnemySpawns().get(i).getEnemies().get(j).render(sb);
            levelManager.getEnemySpawns().get(i).render(sb);
        }

        hud.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        hud.dispose();
        levelManager.dispose();
    }
}
