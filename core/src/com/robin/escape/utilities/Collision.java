package com.robin.escape.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.sprites.Player;
import com.robin.escape.sprites.Tile;

import java.util.ArrayList;

public class Collision {
    public static void pushDown(Player a, GameObject b){
        Rectangle intersection = getIntersection(a, b);
        a.getPosition().y -= intersection.height;
    }
    public static void pushLeft(Player a, GameObject b){
        Rectangle intersection = getIntersection(a, b);
        a.getPosition().x -= intersection.width;
    }
    public static void pushRight(Player a, GameObject b){
        Rectangle intersection = getIntersection(a, b);
        a.getPosition().x += intersection.width;
    }
    public static void pushUp(Player a, GameObject b){
        Rectangle intersection = getIntersection(a, b);
        a.getPosition().y += intersection.height;
    }

    public static void specialEditionBlockCollision(Player a, ArrayList<Tile> bList){
        if (bList.isEmpty())
            return;

        boolean fromLeft = false, fromTop = false, fromRight = false, fromBottom = false;
        double biggestArea = 0;
        int biggestRect = 4;
        GameObject biggestObject = bList.get(0);

        Rectangle[] boxes = new Rectangle[4];
        for (Tile b : bList) {
            boxes[0] = new Rectangle(b.specialEditionBounds().x - b.specialEditionBounds().width, b.specialEditionBounds().y, b.specialEditionBounds().width, b.specialEditionBounds().height);//Left
            boxes[1] = new Rectangle(b.specialEditionBounds().x, b.specialEditionBounds().y + b.specialEditionBounds().height, b.specialEditionBounds().width, b.specialEditionBounds().height);//Top
            boxes[2] = new Rectangle(b.specialEditionBounds().x + b.specialEditionBounds().width, b.specialEditionBounds().y, b.specialEditionBounds().width, b.specialEditionBounds().height);//Right
            boxes[3] = new Rectangle(b.specialEditionBounds().x, b.specialEditionBounds().y - b.specialEditionBounds().height, b.specialEditionBounds().width, b.specialEditionBounds().height);//Bottom

            for (int i = 0; i < 4; i++) {
                Rectangle inter = new Rectangle();
                Collision.intersect(boxes[i], a.getBounds(), inter);
                if ((inter.width + inter.height) > biggestArea) {
                    biggestObject = b;
                    biggestArea = inter.width + inter.height;
                    biggestRect = i;
                }
            }
        }

        if (biggestRect == 0) fromLeft = true;
        if (biggestRect == 1) fromTop = true;
        if (biggestRect == 2) fromRight = true;
        if (biggestRect == 3) fromBottom = true;

        Rectangle intersection = new Rectangle();
        Collision.intersect(
                a.getBounds(),
                ((Tile)biggestObject).specialEditionBounds(),
                intersection); //  Get the intersection itself*/

        if (fromLeft){
            //Gdx.app.log("Left", "");
            a.getPosition().x -= intersection.width;
            if(a.getCurrentVelocity().x < 0)
                a.getPosition().x += intersection.width;
        }
        if(fromRight) {
            a.getPosition().x += intersection.width;
            if(a.getCurrentVelocity().x > 0)
                a.getPosition().x -= intersection.width;
        }
        if (fromTop) {
            //Gdx.app.log("   ", "Top");
            a.getPosition().y += intersection.height;
        }
        if (fromBottom) {
            a.getPosition().y -= intersection.height;
        }


        bList.remove(biggestObject);
        Collision.specialEditionBlockCollision(a, bList);
    }


    public static void blockCollision(Player a, GameObject b){
        boolean fromLeft = false, fromTop = false, fromRight = false, fromBottom = false;
        Rectangle[] boxes = new Rectangle[4];
        boxes[0] = new Rectangle(b.getBounds().x-b.getBounds().width,   b.getBounds().y,                        b.getBounds().width, b.getBounds().height);//Left
        boxes[1] = new Rectangle(b.getBounds().x,                       b.getBounds().y + b.getBounds().height, b.getBounds().width, b.getBounds().height);//Top
        boxes[2] = new Rectangle(b.getBounds().x+b.getBounds().width,   b.getBounds().y,                        b.getBounds().width, b.getBounds().height);//Right
        boxes[3] = new Rectangle(b.getBounds().x,                       b.getBounds().y - b.getBounds().height, b.getBounds().width, b.getBounds().height);//Bottom
        double biggestArea = 0;
        int biggestRect = 4;
        for(int i = 0; i<4; i++){
            Rectangle inter = new Rectangle();
            Collision.intersect(boxes[i], a.getBounds(), inter);
            if((inter.width + inter.height) > biggestArea) {
                biggestArea = inter.width + inter.height;
                biggestRect = i;
            }
        }
        if(biggestRect ==  0) fromLeft = true;
        if(biggestRect ==  1) fromTop = true;
        if(biggestRect ==  2) fromRight = true;
        if(biggestRect ==  3) fromBottom = true;

        Rectangle intersection = new Rectangle();
        Collision.intersect(
                a.getBounds(),
                new Rectangle(b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height),
                intersection); //  Get the intersection itself


        if (fromLeft){
            a.getPosition().x -= intersection.width;
            if(a.getCurrentVelocity().x < 0)
                a.getPosition().x += intersection.width;
        }
        if(fromRight) {
            a.getPosition().x += intersection.width;
            if(a.getCurrentVelocity().x > 0)
                a.getPosition().x -= intersection.width;
        }
        if(fromTop) {
            //if (a.getVelocity().y < 0) {
            a.getPosition().y += intersection.height;// move him back up
            //}
        }
        if(fromBottom) {
            //if (a.getVelocity().y > 0) {
            a.getPosition().y -= intersection.height;// move him back down
            //}
        }
    }

    public static void blockCollision(Player a, ArrayList<GameObject> bList) {
        if (bList.isEmpty())
            return;

        boolean fromLeft = false, fromTop = false, fromRight = false, fromBottom = false;
        double biggestArea = 0;
        int biggestRect = 4;
        GameObject biggestObject = bList.get(0);

        Rectangle[] boxes = new Rectangle[4];
        for (GameObject b : bList) {
            boxes[0] = new Rectangle(b.getBounds().x - b.getBounds().width, b.getBounds().y, b.getBounds().width, b.getBounds().height);//Left
            boxes[1] = new Rectangle(b.getBounds().x, b.getBounds().y + b.getBounds().height, b.getBounds().width, b.getBounds().height);//Top
            boxes[2] = new Rectangle(b.getBounds().x + b.getBounds().width, b.getBounds().y, b.getBounds().width, b.getBounds().height);//Right
            boxes[3] = new Rectangle(b.getBounds().x, b.getBounds().y - b.getBounds().height, b.getBounds().width, b.getBounds().height);//Bottom

            for (int i = 0; i < 4; i++) {
                Rectangle inter = new Rectangle();
                Collision.intersect(boxes[i], a.getBounds(), inter);
                if ((inter.width + inter.height) > biggestArea) {
                    biggestObject = b;
                    biggestArea = inter.width + inter.height;
                    biggestRect = i;
                }
            }
        }

        if (biggestRect == 0) fromLeft = true;
        if (biggestRect == 1) fromTop = true;
        if (biggestRect == 2) fromRight = true;
        if (biggestRect == 3) fromBottom = true;

        Rectangle intersection = new Rectangle();
        Collision.intersect(
                a.getBounds(),
                new Rectangle(biggestObject.getBounds().x, biggestObject.getBounds().y, biggestObject.getBounds().width, biggestObject.getBounds().height),
                intersection); //  Get the intersection itself

        if (fromTop)
            a.getPosition().y += intersection.height;
        if (fromBottom)
            a.getPosition().y -= intersection.height;
        if (fromLeft){
            a.getPosition().x -= intersection.width;
            if(a.getCurrentVelocity().x < 0)
                a.getPosition().x += intersection.width;
        }
        if(fromRight) {
            a.getPosition().x += intersection.width;
            if(a.getCurrentVelocity().x > 0)
                a.getPosition().x -= intersection.width;
        }


        bList.remove(biggestObject);
        Collision.blockCollision(a, bList);
    }

    public static boolean isCollsionDetected(Rectangle a, Rectangle b){
        if (Intersector.overlaps(a, b))
            return true;
        return false;
    }

    public static boolean isPointBoundDetected(Vector3 point, Rectangle bounds){
        if (bounds.contains(point.x, point.y))
            return true;
        return false;
    }
    public static boolean isPointBoundDetected(float x, float y, Rectangle bounds){
        if (bounds.contains(x, y))
            return true;
        return false;
    }

    static public boolean intersect(Rectangle rectangle1, Rectangle rectangle2, Rectangle intersection) {
        if (rectangle1.overlaps(rectangle2)) {
            intersection.x = Math.max(rectangle1.x, rectangle2.x);
            intersection.width = Math.min(rectangle1.x + rectangle1.width, rectangle2.x + rectangle2.width) - intersection.x;
            intersection.y = Math.max(rectangle1.y, rectangle2.y);
            intersection.height = Math.min(rectangle1.y + rectangle1.height, rectangle2.y + rectangle2.height) - intersection.y;
            return true;
        }
        return false;
    }

    static public Rectangle specialEditiongetIntersection(Player a, Tile b){
        Rectangle intersection = new Rectangle();
        Collision.intersect(
                a.getBounds(),
                b.specialEditionBounds(),
                intersection); //  Get the intersection itself
        return intersection;
    }

    static public Rectangle getIntersection(Player a, GameObject b){
        Rectangle intersection = new Rectangle();
        Collision.intersect(
                a.getBounds(),
                //new Rectangle(b.getPosition().x, b.getPosition().y, b.getBounds().width, b.getBounds().height),
                b.getBounds(),
                intersection); //  Get the intersection itself
        return intersection;
    }


}
