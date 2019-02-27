package com.robin.escape.huds;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.sprites.GameObject;

public class EditorHud {
    public enum BUTTON{ EXPORT,IMPORT, TERRAIN, OBJECT, LOGIC, PROPS, RETURN }
    private GameObject btnExportLvl;
    private GameObject btnImportLvl;
    private GameObject btnToolboxTerrain;
    private GameObject btnToolboxObject;
    private GameObject btnToolboxLogic;
    private GameObject btnToolboxProps;
    private GameObject btnReturn;

    public EditorHud(int x, int y) {
        btnExportLvl = new GameObject(new Vector3(x,y,0), "button/exportLevelButton.png");
        btnImportLvl = new GameObject(new Vector3(Math.round(btnExportLvl.getPosition().x) + (btnExportLvl.getSprite().getTexture().getWidth()), y,0), "button/importLevelButton.png");
        btnToolboxTerrain = new GameObject(new Vector3(Math.round(btnImportLvl.getPosition().x) + (btnImportLvl.getSprite().getTexture().getWidth()), y,0), "button/btnToolboxTerrain.png");
        btnToolboxObject = new GameObject(new Vector3(Math.round(btnToolboxTerrain.getPosition().x) + (btnToolboxTerrain.getSprite().getTexture().getWidth()), y,0), "button/btnToolboxObjects.png");
        btnToolboxLogic = new GameObject(new Vector3(Math.round(btnToolboxObject.getPosition().x) + (btnToolboxObject.getSprite().getTexture().getWidth()), y,0), "button/btnToolboxLogic.png");
        btnToolboxProps = new GameObject(new Vector3(Math.round(btnToolboxLogic.getPosition().x) + (btnToolboxLogic.getSprite().getTexture().getWidth()), y,0), "button/btnToolboxProps.png");
        btnReturn = new GameObject(new Vector3(Math.round(btnToolboxProps.getPosition().x) + (btnToolboxProps.getSprite().getTexture().getWidth()), y,0), "button/returnButton.png");

    }


    public GameObject getButton(BUTTON button) {
        switch (button) {
            case EXPORT:
                return btnExportLvl;
            case IMPORT:
                return btnImportLvl;
            case TERRAIN:
                return btnToolboxTerrain;
            case OBJECT:
                return btnToolboxObject;
            case LOGIC:
                return btnToolboxLogic;
            case PROPS:
                return btnToolboxProps;
            case RETURN:
                return btnReturn;

        }
        return null;
    }

    public void render(SpriteBatch sb){
        sb.draw(btnExportLvl.getSprite(), btnExportLvl.getPosition().x, btnExportLvl.getPosition().y);
        sb.draw(btnImportLvl.getSprite(), btnImportLvl.getPosition().x, btnImportLvl.getPosition().y);
        sb.draw(btnToolboxTerrain.getSprite(), btnToolboxTerrain.getPosition().x, btnToolboxTerrain.getPosition().y);
        sb.draw(btnToolboxObject.getSprite(), btnToolboxObject.getPosition().x, btnToolboxObject.getPosition().y);
        sb.draw(btnToolboxLogic.getSprite(), btnToolboxLogic.getPosition().x, btnToolboxLogic.getPosition().y);
        sb.draw(btnToolboxProps.getSprite(), btnToolboxProps.getPosition().x, btnToolboxProps.getPosition().y);
        sb.draw(btnReturn.getSprite(), btnReturn.getPosition().x, btnReturn.getPosition().y);

    }

    public void dispose(){
        btnExportLvl.getSprite().getTexture().dispose();
        btnImportLvl.getSprite().getTexture().dispose();
        btnToolboxTerrain.getSprite().getTexture().dispose();
        btnToolboxObject.getSprite().getTexture().dispose();
        btnToolboxLogic.getSprite().getTexture().dispose();
        btnToolboxProps.getSprite().getTexture().dispose();
        btnReturn.getSprite().getTexture().dispose();
    }
}
