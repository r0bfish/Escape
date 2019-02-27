package com.robin.escape;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robin.escape.states.GameStateManager;
import com.robin.escape.states.MenuState;

public class escape extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	// Constants
	public static final int TILEWIDTH = 16;
	public static final int TILEHEIGHT = 16;
	public static final int TILEPADDING = 2;

	public static final String TITLE = "Lost Penguin";

	private GameStateManager gsm;
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void resize(int width, int height){
		gsm.resize(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.08235f, 0.28627f, 0.49412f, 1);
		//Gdx.gl.glClearColor(0.227f, 0.565f, 0.914f, 1);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
