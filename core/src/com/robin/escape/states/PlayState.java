package com.robin.escape.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.escape;
import com.robin.escape.huds.PlayHud;
import com.robin.escape.huds.ScoreHud;
import com.robin.escape.managers.EnemyManager;
import com.robin.escape.managers.ObjectManager;
import com.robin.escape.managers.PropsManager;
import com.robin.escape.managers.StyleManager;
import com.robin.escape.sprites.Area;
import com.robin.escape.sprites.Clickable;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.sprites.MovingObject;
import com.robin.escape.sprites.Platform;
import com.robin.escape.sprites.Player;
import com.robin.escape.sprites.Props;
import com.robin.escape.sprites.SkidMark;
import com.robin.escape.sprites.Tile;
import com.robin.escape.utilities.CameraStyles;
import com.robin.escape.managers.LevelManager;
import com.robin.escape.utilities.Collision;
import com.robin.escape.utilities.LevelHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PlayState extends State {
    private LevelManager levelManager;
    private BitmapFont font;
    private PlayHud playHud;
    private ScoreHud scoreHud;
    private Rectangle camRect;
    private Player player;
    private GameObject scoreBG;
    private Viewport viewport;
    private List<GameObject> zSortTiles;
    private List<GameObject> zSort;
    private List<GameObject> zNotSortedTiles;
    private List<GameObject> notSorted;
    private Comparator<GameObject> comparator;
    private Vector3 mousePos;
    private Vector3 secondMousePos;
    private Preferences prefs;
    private Music penguinMusic;
    private Music winMusic;
    private float elapsedTime;
    private float deltaTime;
    private boolean pausedGame = false;
    private boolean leftKeyDown = false;
    private boolean playerJumped = false;
    private int skidCounter = 0;
    private Clickable pauseButton;

    public PlayState(GameStateManager gsm, String level) {
        super(gsm);
        pauseButton = new Clickable(new Vector3(escape.WIDTH - 38,escape.HEIGHT - 38,0), "navigation.png", "navigation.png");

        scoreBG = new GameObject(new Vector3(0,0,0), "scoreBG.png");
        font = new BitmapFont(Gdx.files.internal("font/GillSans.fnt"), Gdx.files.internal("font/GillSans.png"), false);
        font.setColor(0.08235f, 0.28627f, 0.49412f, 1.0f);

        mousePos = new Vector3(0,0,0);
        secondMousePos = new Vector3(0,0,0);
        comparator = new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                if(o1.getBounds().y < o2.getBounds().y) return 1;
                else if(o1.getBounds().y > o2.getBounds().y) return -1;
                else return 0;
            }
        };

        prefs = Gdx.app.getPreferences("GameProgress");
        penguinMusic = Gdx.audio.newMusic(Gdx.files.internal("penguinTHEME.ogg"));
        winMusic = Gdx.audio.newMusic(Gdx.files.internal("winTHEME.ogg"));
        penguinMusic.setLooping(true);
        penguinMusic.play();
        penguinMusic.setVolume(1.0f);


        levelManager = new LevelManager();
        levelManager.importLevel(level);
        player = levelManager.getPlayer();
        zSortTiles = new ArrayList<>();
        zSort = new ArrayList<>();
        zNotSortedTiles = new ArrayList<>();
        notSorted = new ArrayList<>();

        camera.setToOrtho(false, escape.WIDTH, escape.HEIGHT);
        CameraStyles.lockOnTarget(camera, player.getPosition());
        viewport = new ExtendViewport(escape.WIDTH/7*4, escape.HEIGHT/7*4, camera);
        viewport.apply();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camRect = new Rectangle(camera.position.x - camera.viewportWidth,camera.position.y - camera.viewportHeight,camera.viewportWidth * 2,camera.viewportHeight * 2);

        playHud = new PlayHud(levelManager.getStarTimes(), camera);
        scoreHud = new ScoreHud(levelManager.getStarTimes(), camera);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void handleInput() {
        Gdx.input.setInputProcessor(new InputProcessor() {
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
                if (button == Input.Buttons.LEFT) {
                    leftKeyDown = true;
                    player.jump();
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    leftKeyDown = false;
                }
                return true;
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
            camera.unproject(mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            CameraStyles.unProject(secondMousePos, Gdx.input.getX(), Gdx.input.getY(), escape.WIDTH, escape.HEIGHT, viewport);

            if(pausedGame) {
                if(scoreHud.getButton(ScoreHud.BUTTON.NEXT).getBounds().contains(secondMousePos.x, secondMousePos.y)) {
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.NEXT)).setPressed(true);
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.MENU)).setPressed(false);
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.REPLAY)).setPressed(false);
                }
                else if(scoreHud.getButton(ScoreHud.BUTTON.MENU).getBounds().contains(secondMousePos.x, secondMousePos.y)) {
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.NEXT)).setPressed(false);
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.MENU)).setPressed(true);
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.REPLAY)).setPressed(false);
                }
                else if(scoreHud.getButton(ScoreHud.BUTTON.REPLAY).getBounds().contains(secondMousePos.x, secondMousePos.y)) {
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.NEXT)).setPressed(false);
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.MENU)).setPressed(false);
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.REPLAY)).setPressed(true);
                }
                else{
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.NEXT)).setPressed(false);
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.MENU)).setPressed(false);
                    ((Clickable)scoreHud.getButton(ScoreHud.BUTTON.REPLAY)).setPressed(false);
                }
            }
            else {
                if(playHud.getButton(PlayHud.BUTTON.PAUSE).getBounds().contains(secondMousePos.x, secondMousePos.y)) {
                    leftKeyDown = false;
                    mousePos.setZero();
                    gsm.push(new PauseState(gsm, levelManager));
                }
                if (!mousePos.isZero())
                    player.moveTowards(mousePos);
            }
        }
        else{
            if(mousePos != null) {
                if(pausedGame) {
                    if (scoreHud.getButton(ScoreHud.BUTTON.NEXT).getBounds().contains(secondMousePos.x, secondMousePos.y)) {
                        mousePos.setZero();
                        String nextLevel = LevelHelper.getNextLevel(levelManager.getLevelName());
                        if (LevelHelper.isFinalLevel(nextLevel)) {
                            gsm.push(new MenuState(gsm));
                        } else {
                            gsm.push(new PlayState(gsm, LevelHelper.getNextLevel(levelManager.getLevelName())));
                        }
                    } else if (scoreHud.getButton(ScoreHud.BUTTON.MENU).getBounds().contains(secondMousePos.x, secondMousePos.y)) {
                        mousePos.setZero();
                        gsm.push(new MenuState(gsm));
                    } else if (scoreHud.getButton(ScoreHud.BUTTON.REPLAY).getBounds().contains(secondMousePos.x, secondMousePos.y)) {
                        mousePos.setZero();
                        gsm.push(new PlayState(gsm, levelManager.getLevelName()));
                    }

                }
            }
        }
    }

    public void handleCollision(){
        //------------------------------------
        // Skidmarks
        /*Rectangle tmpRect = new Rectangle();
        List<SkidMark> skidList = player.getSkidmarkManager().getSkidMarks();
         for (int k = 0; k < skidList.size(); k++) {
             boolean skidOnTile = false;
             for(int i=0; i<levelManager.getTilesetManager().getTiles().size(); i++) {
                 Tile tile = levelManager.getTilesetManager().getTiles().get(i);
                 tmpRect.set(skidList.get(k).getSprite().getX(), skidList.get(k).getSprite().getY(), 2, 2);
                 if (Collision.isCollsionDetected(tile.getBounds(), tmpRect)) {
                     skidOnTile = true;
                     break;
                 }
            }
            if(!skidOnTile)
                skidList.remove(skidList.get(k));
        }*/

        //-------------------------------------------------




        ArrayList<GameObject> tileToTest = new ArrayList<>();
        for(int i=0; i<levelManager.getTilesetManager().getTiles().size(); i++){
            Tile tile = levelManager.getTilesetManager().getTiles().get(i);
                if(Collision.isCollsionDetected(player.getBounds(), tile.getBounds()))
                    tileToTest.add(tile);
        }

        if(tileToTest.isEmpty()) {
            if (!player.playerInAir()) {
                player.resetPlayer(true);
                prefs.putInteger("totalRestarts", prefs.getInteger("totalRestarts", 0) + 1);
                elapsedTime = 0;
                deltaTime = 0;
            }
        }
        else{
            for(int i=0; i<tileToTest.size(); i++) {
                Tile tile = ((Tile)tileToTest.get(i));
                if(!player.playerInAir()) {
                    if ((tile.getTilesetPos().x == 0 && tile.getTilesetPos().y == 32) ||
                        (tile.getTilesetPos().x == 16 && tile.getTilesetPos().y == 32) ||
                        (tile.getTilesetPos().x == 32 && tile.getTilesetPos().y == 32)){
                        if (Collision.isPointBoundDetected(player.getBounds().x, player.getBounds().y, new Rectangle(tile.getBounds().x, tile.getBounds().y , tile.getBounds().width, tile.getBounds().height - 8))) {
                            player.resetPlayer(true);
                            prefs.putInteger("totalRestarts", prefs.getInteger("totalRestarts", 0) + 1);
                            elapsedTime = 0;
                            deltaTime = 0;
                        }
                    }
                    if(tile.getTilesetPos().x == 64 && tile.getTilesetPos().y == 0){
                        if (Collision.isPointBoundDetected(player.getBounds().x, player.getBounds().y, tile.getBounds())) {
                            player.resetPlayer(true);
                            prefs.putInteger("totalRestarts", prefs.getInteger("totalRestarts", 0) + 1);
                            elapsedTime = 0;
                            deltaTime = 0;
                        }
                    }
                }

            }
        }
        if(player.isDead()) {
            for (int i = 0; i < tileToTest.size(); i++) {
                Tile tile = ((Tile) tileToTest.get(i));
                if (tile.getTilesetPos().x == 0 && tile.getTilesetPos().y == 32) {
                    Collision.pushLeft(player, tile);
                    Collision.pushDown(player, tile);
                    tileToTest.remove(tile);
                }
                if (tile.getTilesetPos().x == 32 && tile.getTilesetPos().y == 32) {
                    Collision.pushRight(player, tile);
                    Collision.pushDown(player, tile);
                    tileToTest.remove(tile);
                }
                if ((tile.getTilesetPos().x == 16 && tile.getTilesetPos().y == 32)) {
                    Collision.pushDown(player, tile);
                    tileToTest.remove(tile);
                }
                if ((tile.getTilesetPos().x == 64 && tile.getTilesetPos().y == 0))
                    tileToTest.remove(tile);
            }


            List<GameObject> toRemove = new ArrayList<>();
            for(GameObject t : tileToTest)
                if (((Tile)t).getTilesetPos().x == 64 && ((Tile)t).getTilesetPos().y == 0)
                    toRemove.add(t);
            tileToTest.removeAll(toRemove);
            Collision.blockCollision(player, tileToTest);
        }




        // Collision with player and props
        ArrayList<GameObject> propsToTest = new ArrayList<>();
        for(int i=0; i<levelManager.getPropsManager().getProps().size(); i++) {
            Props prop = levelManager.getPropsManager().getProps().get(i);
            if( prop.getPropType() == PropsManager.PROPS.FENCELEFT ||
                prop.getPropType() == PropsManager.PROPS.FENCERIGHT ||
                prop.getPropType() == PropsManager.PROPS.HORFENCE) {
                if (Collision.isCollsionDetected(player.getBounds(), prop.getBounds()))
                    propsToTest.add(prop);
            }
            else
                Collision.blockCollision(player, prop);
        }
        if(!player.playerInAir())
            Collision.blockCollision(player, propsToTest);

        // Collision with player and tiles
        for(int i=0; i<levelManager.getLogicManager().getLogicAreas().size(); i++){
            Area logicArea = levelManager.getLogicManager().getLogicAreas().get(i);
            if (Collision.isCollsionDetected(player.getBounds(), logicArea.getBounds())) {
                switch (logicArea.getAttribute()){
                    case KILL:
                        if(!player.playerInAir()) {
                            player.resetPlayer(false);
                            prefs.putInteger("totalRestarts", prefs.getInteger("totalRestarts", 0) + 1);
                            elapsedTime = 0;
                            deltaTime = 0;
                        }
                        break;
                    case BLOCK:
                        Collision.blockCollision(player, logicArea);
                    case JUMPBLOCK:
                        if(!player.playerInAir())
                            Collision.blockCollision(player, logicArea);
                        break;
                    case HILLBLOCK1:
                        if(!player.playerInAir())
                            Collision.pushDown(player, logicArea);
                        break;
                    case HILLBLOCK2:
                            Collision.pushDown(player, logicArea);
                        break;
                    case HILLPUSHLEFT:
                        if(!player.playerInAir())
                            Collision.pushLeft(player, logicArea);
                        break;
                    case HILLPUSHRIGHT:
                        if(!player.playerInAir())
                            Collision.pushRight(player, logicArea);
                        break;
                    case HILLPUSHUP:
                        if(!player.playerInAir())
                            Collision.pushUp(player, logicArea);
                        break;
                    case WIN:
                        // Winning functions
                        if(!player.playerInAir()) {
                            prefs.putBoolean(levelManager.getLevelName(), true);
                            int previousTime = prefs.getInteger("time" + levelManager.getLevelName(), Integer.MAX_VALUE);
                            if (Math.round(elapsedTime) < previousTime)
                                prefs.putInteger("time" + levelManager.getLevelName(), Math.round(elapsedTime));

                            int previousStars = prefs.getInteger("star" + levelManager.getLevelName(), 0);
                            if (levelManager.getStarsFromTime(elapsedTime) > previousStars)
                                prefs.putInteger("star" + levelManager.getLevelName(), levelManager.getStarsFromTime(elapsedTime));
                            prefs.flush();
                            pausedGame = true;
                            player.setWinnableState(true);
                            penguinMusic.stop();
                            winMusic.play();
                            winMusic.setVolume(1.0f);
                        }

                        break;
                }
            }
            // Player-point against Tile-bound
            if(Collision.isPointBoundDetected(
                    (player.getBounds().x + player.getBounds().width/2), player.getBounds().y + player.getBounds().height/2, logicArea.getBounds())){
                switch (logicArea.getAttribute()){
                    case NORMAL:
                        if(!player.playerInAir())
                            player.slide(false, false);
                        break;
                    case SLIDE:
                        if(!player.playerInAir())
                            player.slide(true, false);
                        break;
                    case HARDSLIDE:
                        if(!player.playerInAir())
                            player.slide(true, true);
                        break;
                }
            }
        }


        // Player-bound against object-bound
        for(int i=0; i<levelManager.getObjectManager().getObjects().size(); i++){
            GameObject obj = levelManager.getObjectManager().getObjects().get(i);
            if (obj != player) {
                switch (obj.getType()) {
                    case PENGUIN:
                        if (Collision.isCollsionDetected(player.getBounds(), obj.getBounds()))
                            Collision.blockCollision(player, obj);
                        break;
                    case ENEMY:
                        if (Collision.isCollsionDetected(player.getBounds(), obj.getBounds())) {
                            player.resetPlayer(false);
                            prefs.putInteger("totalRestarts", prefs.getInteger("totalRestarts", 0) + 1);
                            elapsedTime = 0;
                            deltaTime = 0;
                        }
                        break;
                    case PLATFORM2:
                        if (Collision.isCollsionDetected(player.getBounds(), obj.getBounds())) {
                            if(!player.playerInAir()){
                                player.resetPlayer(false);
                                prefs.putInteger("totalRestarts", prefs.getInteger("totalRestarts", 0) + 1);
                                elapsedTime = 0;
                                deltaTime = 0;
                            }
                        }
                }
            }
        }

        // Player-bound against enemy-spawn
        for(int i=0; i<levelManager.getEnemySpawns().size(); i++) {
            EnemyManager em = levelManager.getEnemySpawns().get(i);
            for(int j=0; j<em.getEnemies().size(); j++){
                if (Collision.isCollsionDetected(player.getBounds(), em.getEnemies().get(j).getBounds())) {
                    player.resetPlayer(false);
                    prefs.putInteger("totalRestarts", prefs.getInteger("totalRestarts", 0) + 1);
                    elapsedTime = 0;
                    deltaTime = 0;
                }
            }
        }
    }

    public void flipPlatforms(){
        if(player != null) {
            if (player.playerInAir()) {
                if (!playerJumped) {
                    playerJumped = true;
                    for (GameObject obj : levelManager.getObjectManager().getObjects()) {

                        if (obj.getType() == ObjectManager.TYPE.PLATFORM ||
                            obj.getType() == ObjectManager.TYPE.PLATFORM2) {
                                ((Platform) obj).flip();
                        }
                    }
                }
            } else
                playerJumped = false;
        }
    }

    private void cullingAndSortPlayerDead(){
        zSortTiles.clear();
        zNotSortedTiles.clear();

        camRect.set(
                camera.position.x - camera.viewportWidth,
                camera.position.y - camera.viewportHeight,
                camera.viewportWidth * 2,
                camera.viewportHeight * 2);

        // Add all the game-objects that we want to z-sort for the drawing
        zSortTiles.add(player);
        for(int i=0; i<levelManager.getTilesetManager().getTiles().size(); i++){
            GameObject tile = levelManager.getTilesetManager().getTiles().get(i);
            if (Collision.isPointBoundDetected(tile.getPosition(), camRect)) {
                if (((Tile) tile).getTilesetPos().x == 64 && ((Tile) tile).getTilesetPos().y == 0) {
                    zNotSortedTiles.add(tile);
                } else {
                    zSortTiles.add(tile);
                }
            }
        }
        // Sort the array
        Collections.sort(zSortTiles, comparator);
    }

    private void cullingAndSort(){
        zSort.clear();
        notSorted.clear();

        camRect.set(
                camera.position.x - camera.viewportWidth,
                camera.position.y - camera.viewportHeight,
                camera.viewportWidth * 2,
                camera.viewportHeight * 2);

        // Add all the game-props that we want to z-sort for the drawing
        for(int i=0; i<levelManager.getPropsManager().getProps().size(); i++){
            GameObject props = levelManager.getPropsManager().getProps().get(i);
            if (Collision.isPointBoundDetected(props.getPosition(), camRect))
                if (((Props) props).getPropType() != PropsManager.PROPS.FENCELEFT &&
                        ((Props) props).getPropType() != PropsManager.PROPS.FENCERIGHT)
                    zSort.add(props);
                else
                    notSorted.add(props);
        }

        // Add all the objects we want to z-sort for drawing
        List<GameObject> zSortPlatform = new ArrayList();
        for(int i=0; i<levelManager.getObjectManager().getObjects().size(); i++){
            GameObject obj = levelManager.getObjectManager().getObjects().get(i);
            if (obj.getClass() != Platform.class) {
                if (Collision.isPointBoundDetected(obj.getPosition(), camRect))
                    zSort.add(obj);
                else
                    notSorted.add(obj);
            }
            else{
                zSortPlatform.add(obj);
            }
        }

        // Add all the objects we want to z-sort for drawing from the spawners
        for(int i=0; i<levelManager.getEnemySpawns().size(); i++){
            EnemyManager em = levelManager.getEnemySpawns().get(i);
            for(int j=0; j<em.getEnemies().size(); j++){
                if (Collision.isPointBoundDetected(em.getEnemies().get(j).getPosition(), camRect))
                    zSort.add(em.getEnemies().get(j));
                else
                    notSorted.add(em.getEnemies().get(j));
            }
            // Add the teleport effects
            //for(int k=0; k<em.)
        }

        if(player.isDead() && player.diedInWater)
            zSort.remove(player);
        // Sort the array
        Collections.sort(zSort, comparator);
        Collections.sort(zSortPlatform, comparator);
        zSort.addAll(0, zSortPlatform);
    }

    @Override
    public void update(float dt) {
        handleInput();
        inputActions();

        deltaTime += dt;

        //________________//__________________________
        // SKIDMARKS COLLISION CHECK
        if(Math.round((elapsedTime-dt) * 3) % 2 != Math.round((elapsedTime) * 3) % 2)
            skidCounter = 20;
        if(!player.playerInAir() && player.isOnIce()){
            if (Math.round(elapsedTime * 3) % 2 == 0 && skidCounter > 0) {
                Rectangle rect = new Rectangle(player.getPosition().x-player.getBounds().width, player.getPosition().y, 2, 2);
                for(int i=0; i<levelManager.getTilesetManager().getTiles().size(); i++)
                    if(Collision.isCollsionDetected(rect, levelManager.getTilesetManager().getTiles().get(i).getBounds())) {
                        player.getSkidmarkManager().newSkid(player.getPosition().x, player.getPosition().y, (float) player.getDirectionAngle(), true);
                        skidCounter--;
                    }
            }
            else if(Math.round(elapsedTime * 3) % 2 == 1 && skidCounter > 0) {
                Rectangle rect = new Rectangle(player.getPosition().x+player.getBounds().width, player.getPosition().y, 2, 2);
                for(int i=0; i<levelManager.getTilesetManager().getTiles().size(); i++)
                    if(Collision.isCollsionDetected(rect, levelManager.getTilesetManager().getTiles().get(i).getBounds())) {
                        player.getSkidmarkManager().newSkid(player.getPosition().x, player.getPosition().y, (float) player.getDirectionAngle(), false);
                        skidCounter--;
                    }
            }
        }
        //_____________________//_____________

        if(!pausedGame) {
            elapsedTime += dt;
            playHud.setTime(Math.round(elapsedTime));
            scoreHud.setTime(Math.round(elapsedTime));
            flipPlatforms();
            handleCollision();
            if(player.isDead() && player.diedInWater)
                cullingAndSortPlayerDead();
            cullingAndSort();
        }

        for(int i=0; i<levelManager.getObjectManager().getObjects().size(); i++)
            levelManager.getObjectManager().getObjects().get(i).update(dt);

        for(int i=0; i<levelManager.getEnemySpawns().size(); i++)
            levelManager.getEnemySpawns().get(i).update(dt);

        if(pausedGame)
            CameraStyles.lerpZoom(camera, 0.4f, 0.15f);

        CameraStyles.lerpToTarget(camera, player.getPosition());
    }

    @Override
    public void render(SpriteBatch sb) {
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        if(player.isDead() && player.diedInWater) {
            for(int i=0; i<zNotSortedTiles.size(); i++)
                zNotSortedTiles.get(i).render(sb);
            for(int j=0; j<zSortTiles.size(); j++)
                zSortTiles.get(j).render(sb);

            player.getSkidmarkManager().render(sb);
        }
        else {
            levelManager.getTilesetManager().render(sb);
            //player.getSkidmarkManager().render(sb);
        }
        for (int i = 0; i < notSorted.size(); i++)
            notSorted.get(i).render(sb);
        for (int j = 0; j < zSort.size(); j++)
            zSort.get(j).render(sb);

        for(int i=0; i<levelManager.getEnemySpawns().size(); i++)
            levelManager.getEnemySpawns().get(i).render(sb);

        sb.end();

        // Draw HUDS
        if(pausedGame)
            scoreHud.render(sb);
        else
            playHud.render(sb);
    }

    @Override
    public void dispose() {
        prefs.flush();
        levelManager.dispose();
        penguinMusic.dispose();
        winMusic.dispose();
        scoreBG.getSprite().getTexture().dispose();
        font.dispose();
    }
}
