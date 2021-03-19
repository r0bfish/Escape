package com.robin.escape.managers;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.Game;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.sprites.Props;

import java.util.ArrayList;

public class PropsManager {
    public enum PROPS{ SNOWTREE, STONE, IGLOO, HORFENCE, FENCELEFT, FENCERIGHT }
    private PROPS selectedProp;
    private boolean isFocused;
    private Vector3 mousePos;
    private ArrayList<Props> props;
    private GameObject propsset;
    private Texture SNOWTREE;
    private Texture STONE;
    private Texture IGLOO;
    private Texture HORFENCE;
    private Texture FENCELEFT;
    private Texture FENCERIGHT;

    public PropsManager(String style, String propsset){
        isFocused = false;
        mousePos = new Vector3();
        props = new ArrayList<Props>();
        this.propsset = new GameObject(new Vector3(160,640,0),propsset);

        SNOWTREE = new Texture(style + "/props/snowtree.png");
        STONE = new Texture(style + "/props/stone.png");
        IGLOO = new Texture(style + "/props/igloo.png");
        HORFENCE = new Texture(style + "/props/horfence.png");
        FENCELEFT = new Texture(style + "/props/vertfenceLeft.png");
        FENCERIGHT = new Texture(style + "/props/vertfenceRight.png");

    }

    /**************************
     * Getters and Setters for this class
     * ************************
     */
    public GameObject getPropset(){return propsset;}
    public boolean getFocused(){
        return isFocused;
    }
    public ArrayList<Props> getProps(){return props;}
    public void setSelected(Vector3 mousePos){
        this.mousePos = mousePos;
        switch ((int)(Math.abs(propsset.getPosition().x - (mousePos.x - (mousePos.x % Game.TILEWIDTH)))/ Game.TILEWIDTH)){
            case 0:
            case 1:
                selectedProp = PROPS.SNOWTREE;
                break;
            case 2:
                selectedProp = PROPS.STONE;
                break;
            case 3:
            case 4:
                selectedProp = PROPS.IGLOO;
                break;
            case 5:
                selectedProp = PROPS.HORFENCE;
                break;
            case 6:
                selectedProp = PROPS.FENCELEFT;
                break;
            case 7:
                selectedProp = PROPS.FENCERIGHT;
                break;
        }

    }
    public PROPS getSelectedPropType(){return selectedProp;}
    public void setFocused(boolean b){ isFocused = b;}
    public Texture stringToTexture(String propType){
        if(propType.equals("SNOWTREE")) return SNOWTREE;
        else if(propType.equals("STONE")) return STONE;
        else if(propType.equals("IGLOO")) return IGLOO;
        else if(propType.equals("HORFENCE")) return HORFENCE;
        else if(propType.equals("FENCELEFT")) return FENCELEFT;
        else if(propType.equals("FENCERIGHT")) return FENCERIGHT;
        return null;
    }
    public static PROPS stringToPropType(String propType){
        if(propType.equals("SNOWTREE")) return PROPS.SNOWTREE;
        else if(propType.equals("STONE")) return PROPS.STONE;
        else if(propType.equals("IGLOO")) return PROPS.IGLOO;
        else if(propType.equals("HORFENCE")) return PROPS.HORFENCE;
        else if(propType.equals("FENCELEFT")) return PROPS.FENCELEFT;
        else if(propType.equals("FENCERIGHT")) return PROPS.FENCERIGHT;
        return null;
    }


    public void addProps(Vector3 worldPos, PropsManager.PROPS propType){
        if(propType != null) {
            switch (propType) {
                case SNOWTREE:
                    props.add(new Props(new Vector3(worldPos.x - (worldPos.x % Game.TILEWIDTH), worldPos.y - (worldPos.y % Game.TILEHEIGHT),0), SNOWTREE, propType,
                            new Rectangle(
                                    9,
                                    20,
                                    -40,
                                    -69
                            )));
                    break;
                case STONE:
                    props.add(new Props(new Vector3(worldPos.x, worldPos.y,0), STONE, propType,new Rectangle(
                            0,
                            2,
                            -3,
                            -14
                    )));
                    break;
                case IGLOO:
                    props.add(new Props(new Vector3(worldPos.x, worldPos.y,0), IGLOO, propType,
                            new Rectangle(
                            0,
                            0,
                            0,
                            0
                    )));
                    break;
                case HORFENCE:
                    props.add(new Props(new Vector3(worldPos.x - (worldPos.x % Game.TILEWIDTH), worldPos.y - (worldPos.y % Game.TILEHEIGHT),0), HORFENCE, propType,
                            new Rectangle(
                                    0,
                                    -5,
                                    0,
                                    -13
                            )));
                    break;
                case FENCELEFT:
                    props.add(new Props(new Vector3(worldPos.x - (worldPos.x % Game.TILEWIDTH), worldPos.y - (worldPos.y % Game.TILEHEIGHT),0), FENCELEFT, propType,
                            new Rectangle(
                                    -2,
                                    0,
                                    -8,
                                    -2
                    )));
                    break;
                case FENCERIGHT:
                    props.add(new Props(new Vector3(worldPos.x - (worldPos.x % Game.TILEWIDTH), worldPos.y - (worldPos.y % Game.TILEHEIGHT),0), FENCERIGHT, propType,
                            new Rectangle(
                                    12,
                                    0,
                                    -8,
                                    -9
                            )));
                    break;
            }
        }

    }

    public void render(SpriteBatch sb){
        for(Props p : props){
            p.render(sb);
            //sb.draw(propsset.getTexture(), t.getWorldPos().x,t.getWorldPos().y, (int)t.getTilesetPos().x, (int)t.getTilesetPos().y, escape.TILEWIDTH, escape.TILEHEIGHT);
        }
        if(isFocused) {
            sb.draw(propsset.getSprite(), propsset.getPosition().x, propsset.getPosition().y);
        }

    }
    public void dispose(){
        //for(int i=0; i<props.size(); i++)
        //    props.get(i).dispose();
        propsset.getSprite().getTexture().dispose();
    }
}
