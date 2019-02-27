package com.robin.escape.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {
    private Stack<State> states;
    private boolean renderPrev;

    public GameStateManager(){
        states = new Stack<>();
        renderPrev = false;
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }

    public void set(State state){
        states.pop();
        states.push(state);

    }

    public void resize(int width, int height){
        if(!states.empty())
            states.peek().resize(width, height);
    }

    public State peek(){ return states.peek();}

    public void update(float dt){
        //Gdx.app.log("FPS", "" + Gdx.graphics.getFramesPerSecond());
        if(!states.empty())
            states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        renderPrevState(sb);

        if(!states.empty())
            states.peek().render(sb);
    }

    public void setRenderPrev(boolean b){ renderPrev = b; }
    public boolean getRenderPrev(){ return renderPrev; }

    public void disposePrevState(){
        if (states.size() > 2)
            states.get(states.size() - 2).dispose();
    }
    private void renderPrevState(SpriteBatch sb){
        if(renderPrev) {
            if (states.size() > 2)
                states.get(states.size() - 2).render(sb);
        }
    }
}
