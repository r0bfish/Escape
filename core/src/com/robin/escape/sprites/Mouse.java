package com.robin.escape.sprites;

import com.badlogic.gdx.math.Vector3;

public class Mouse extends GameObject {
    public Mouse(int x, int y) {
        super(new Vector3(x, y,0), "circle.png");
    }
}
