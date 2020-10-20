package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man ;
	int height;
	int width;

	int manState=0;
	int pause=0;

	float gravity = 0.2f;
	float velocity = 0;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,width,height);
		if(pause<5){
			pause++;
		}else{
			pause=0;
			if(manState<3){
				manState++;
			}else{
				manState=0;
			}
		}
		batch.draw(man[manState],width/2 - man[manState].getWidth()/2 , height/4 -man[manState].getHeight());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
