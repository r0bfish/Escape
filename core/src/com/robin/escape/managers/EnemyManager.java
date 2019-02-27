package com.robin.escape.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.robin.escape.sprites.Enemy;
import com.robin.escape.utilities.AnimationHandler;
import com.robin.escape.utilities.Collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnemyManager {
    private AnimationHandler animationHandler;
    private List<Enemy> enemies;
    private HashMap<Float, Vector2> map;
    private Rectangle rect;
    private Rectangle startRegion;
    private Rectangle endRegion;
    private Texture text;
    private String style;
    private int enemyAmount;
    private double spawnFreq;
    private Random random;
    private float elapsedTime;
    private List<Float> keysToRemove;

    public EnemyManager(Rectangle rect, int enemyAmount, double spawnFreq, String style, Texture text){
        animationHandler = new AnimationHandler();
        animationHandler.addAnimation("teleport", style + "/animation/enemy/teleport.png", 40, 40, 4);

        map = new HashMap<>();
        keysToRemove = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.rect = rect;
        if(rect.width > rect.height) {
            startRegion = new Rectangle(rect.x, rect.y, 1, rect.height-1);
            endRegion = new Rectangle(rect.x+rect.width, rect.y, 1, rect.height-1);
        }
        else{
            startRegion = new Rectangle(rect.x, rect.y, rect.width, 1);
            endRegion = new Rectangle(rect.x, rect.y +rect.height, rect.width, 1);
        }
        this.text = text;
        this.style = style;
        this.enemyAmount = enemyAmount;
        this.spawnFreq = spawnFreq;
        this.random = new Random(System.currentTimeMillis());
        this.elapsedTime = 0;
    }

    public Rectangle getSpawnRegion(){return rect;}
    public double getSpawnFreq(){return spawnFreq;}
    public int getSpawnAmount(){return enemyAmount;}
    public List<Enemy> getEnemies(){return enemies;}
    public void update(float dt){
        elapsedTime += dt;

        //Gdx.app.log("Monsters", ""+enemies.size());
        if(enemies.size() < enemyAmount) {
            if ((elapsedTime % spawnFreq) < ((elapsedTime - dt) % spawnFreq)) {
                int startOffsetWidth = random.nextInt(Math.round(startRegion.width));
                int endOffsetHeight = random.nextInt(Math.round(endRegion.height));
                enemies.add(new Enemy(
                        startRegion.x + startOffsetWidth - text.getWidth()/4,
                        startRegion.y + endOffsetHeight  - text.getHeight()/4,
                        text,
                        endRegion.x + startOffsetWidth - text.getWidth()/4,
                        endRegion.y + endOffsetHeight  - text.getHeight()/4,
                        style,
                        new Rectangle(5, 6, -29,-32)));
                map.put(elapsedTime, new Vector2(
                        startRegion.x + startOffsetWidth - enemies.get(0).getBounds().width,
                        startRegion.y + endOffsetHeight - enemies.get(0).getBounds().height));

            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(dt);
            if (Collision.isCollsionDetected(endRegion, enemies.get(i).getBounds())) {
                int startOffsetWidth = random.nextInt(Math.round(startRegion.width));
                int endOffsetHeight = random.nextInt(Math.round(endRegion.height));
                // Teleport animation from old pos
                map.put(elapsedTime, new Vector2(
                                enemies.get(i).getPosition().x - enemies.get(i).getBounds().width/2,
                                enemies.get(i).getPosition().y  - enemies.get(i).getBounds().height/2));
                // Teleport animation from new pos
                map.put(elapsedTime+0.00001f, new Vector2(
                        startRegion.x + startOffsetWidth - enemies.get(i).getBounds().width,
                        startRegion.y + endOffsetHeight - enemies.get(i).getBounds().height));
                // Teleport enemy
                enemies.get(i).getPosition().set(
                        startRegion.x + startOffsetWidth - text.getWidth()/4,
                        startRegion.y + endOffsetHeight  - text.getHeight()/4, 0);
                // Set target for enemy
                enemies.get(i).getPatrolPos1().set(
                        endRegion.x + startOffsetWidth - text.getWidth()/4,
                        endRegion.y + endOffsetHeight  - text.getHeight()/4 , 0);
            }
        }
    }

    public void render(SpriteBatch sb){
        //for(int i=0; i<enemies.size(); i++)
        //    enemies.get(i).render(sb);

        for(Map.Entry<Float, Vector2> entry : map.entrySet()) {
            Float f = entry.getKey();
            Vector2 vec2 = entry.getValue();
            if(!animationHandler.getAnimation("teleport").isAnimationFinished((elapsedTime - f)*3))
                sb.draw((TextureRegion) animationHandler.getAnimation("teleport").getKeyFrame((elapsedTime - f)*3, false), vec2.x, vec2.y);
            if(animationHandler.getAnimation("teleport").isAnimationFinished((elapsedTime - f)*3)) {
                keysToRemove.add(f);
            }
        }
        for(int i=0; i<keysToRemove.size(); i++)
            map.remove(keysToRemove.get(i));
        keysToRemove.clear();


    }
}
