package com.robin.escape.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraStyles {
    public static void lockOnTarget(Camera camera, Vector3 target){
        Vector3 position = camera.position;
        position.x = target.x;
        position.y = target.y;
        camera.position.set(position);
        camera.update();
    }

    public static void lerpZoom(OrthographicCamera camera, float zoomLevel, float zoomSpeed){
        camera.zoom = camera.zoom + (zoomLevel - camera.zoom) * zoomSpeed;
        camera.update();
    }

    public static void lerpToTarget(Camera camera, Vector3 target){
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * .075f;
        position.y = camera.position.y + (target.y - camera.position.y) * .075f;
        camera.position.set(position);
        camera.update();
    }

    public static void boundary(Camera camera, float startX, float startY, float endX, float endY){
        Vector3 position = camera.position;

        if(position.x < startX) position.x = startX;
        if(position.y < startY) position.y = startY;
        if(position.x > startX + endX) position.x = startX + endX;
        if(position.y > startY + endY) position.y = startY + endY;

        camera.position.set(position);
        camera.update();
    }

    public static void unProject(Vector3 pos, int x, int y, int width, int height, Viewport viewport){
        float xPos = x;
        float yPos = y;
        float yR = viewport.getScreenHeight() / (yPos - viewport.getScreenY()); // the y ratio
        yPos = height / yR;

        float xR = viewport.getScreenWidth() / (xPos - viewport.getScreenY()); // the x ratio
        xPos = width / xR;

        pos.set(xPos, height - yPos, 0);
    }
}