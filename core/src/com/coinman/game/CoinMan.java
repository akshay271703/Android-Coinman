package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Random;

import javax.xml.soap.Text;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man ;
	int height;
	int width;

	//Character
	int manState=0;
	int pause=0;
	int manY;

	//Physics
	float gravity = 0.4f;
	float velocity = 0;

	//Coins and Bombs
	ArrayList<Integer> coinX = new ArrayList<>();
	ArrayList<Integer> coinY = new ArrayList<>();
	Texture coin;
	ArrayList<Integer> bombX = new ArrayList<>();
	ArrayList<Integer> bombY = new ArrayList<>();
	Texture bomb;
	int coinCount;
	int bombCount;
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

		manY = height/2 -man[manState].getHeight();

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
	}

	public void genCoin()
	{
		Random random = new Random();
		float h = random.nextFloat()* height;
		coinY.add((int) h);
		coinX.add(width);
	}
	public void genBomb()
	{
		Random random = new Random();
		float h = random.nextFloat()* height;
		bombY.add((int) h);
		bombX.add(width);
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,width,height);

		//Generate Coins on screen
		if(coinCount < 150){
			coinCount++;
		}else{
			coinCount=0;
			genCoin();
		}

		for(int i=0;i<=coinX.size()-1;i++)
		{
			batch.draw(coin, coinX.get(i) , coinY.get(i));
			coinX.set(i, coinX.get(i)-4);
		}

		//Generate Bombs on screen
		if(bombCount < 250){
			bombCount++;
		}else{
			bombCount=0;
			genBomb();
		}

		for(int i=0;i<=bombX.size()-1;i++)
		{
			batch.draw(bomb, bombX.get(i) , bombY.get(i));
			bombX.set(i, bombX.get(i)-4);
		}

		if(Gdx.input.justTouched()){
			velocity=-15;
		}

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

		velocity+= gravity;
		manY-= velocity;

		if(manY<=0){
			manY=0;
		}
		if(manY == height-man[manState].getHeight()){
			manY=height-man[manState].getHeight();
		}

		batch.draw(man[manState],width/2 - man[manState].getWidth()/2 , manY);


		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
