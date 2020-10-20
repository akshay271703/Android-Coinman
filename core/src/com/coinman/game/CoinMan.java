package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;


public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture dizzy;
	Texture[] man ;
	int height;
	int width;

	int score = 0;
	BitmapFont font;
	int gameState = 0;

	//Character
	int manState=0;
	int pause=0;
	int manY;
	Rectangle manRect;

	//Physics
	float gravity = 0.4f;
	float velocity = 0;

	boolean touchedBomb=false;

	//Coins and Bombs
	ArrayList<Integer> coinX = new ArrayList<>();
	ArrayList<Integer> coinY = new ArrayList<>();
	Texture coin;
	int coinCount;
	ArrayList<Rectangle> coinRect = new ArrayList<>();

	ArrayList<Integer> bombX = new ArrayList<>();
	ArrayList<Integer> bombY = new ArrayList<>();
	Texture bomb;
	int bombCount;
	ArrayList<Rectangle> bombRect = new ArrayList<>();

	Sound coinSound,endGame,jump;
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
		manRect = new Rectangle();

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		dizzy = new Texture("dizzy-1.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
		endGame = Gdx.audio.newSound(Gdx.files.internal("gameover.wav"));
		jump = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
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

		if(gameState==1){
			// Game is Live
			//Generate Coins on screen

			touchedBomb = false;

			if(coinCount < 150){
				coinCount++;
			}else{
				coinCount=0;
				genCoin();
			}
			coinRect.clear();
			for(int i=0;i<=coinX.size()-1;i++)
			{
				batch.draw(coin, coinX.get(i) , coinY.get(i));
				coinX.set(i, coinX.get(i)-4);
				coinRect.add(new Rectangle(coinX.get(i),coinY.get(i), coin.getWidth(), coin.getHeight()));
			}

			//Generate Bombs on screen
			if(bombCount < 250){
				bombCount++;
			}else{
				bombCount=0;
				genBomb();
			}
			bombRect.clear();;
			for(int i=0;i<=bombX.size()-1;i++)
			{
				batch.draw(bomb, bombX.get(i) , bombY.get(i));
				bombX.set(i, bombX.get(i)-4);
				bombRect.add(new Rectangle(bombX.get(i),bombY.get(i), bomb.getWidth(), bomb.getHeight()));
			}

			if(Gdx.input.justTouched()){
				velocity=-15;
				jump.play();
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
		}else if(gameState==0){
			//Starting Position
			if(Gdx.input.justTouched()){
				// Start The Game
				gameState=1;
			}
		}else if(gameState == 2){
			//Game Over
			gameState=0;
			velocity=0;
			score=0;
			coinCount=0;
			coinX.clear();
			coinY.clear();
			coinRect.clear();

			bombCount=0;
			bombX.clear();
			bombY.clear();
			bombRect.clear();
		}


		if(touchedBomb==true){
			batch.draw(dizzy,width/2 - dizzy.getWidth()/2 , height/2 - dizzy.getHeight()/2);
		}else{
			batch.draw(man[manState],width/2 - man[manState].getWidth()/2 , manY);
		}
		manRect = new Rectangle(width/2 - man[manState].getWidth()/2 , manY,man[manState].getWidth(), man[manState].getHeight());

		for(int x=0;x<=coinRect.size()-1;x++)
		{
			if(Intersector.overlaps(manRect,coinRect.get(x))){
				score++;
				coinSound.play();
				coinX.remove(x);
				coinY.remove(x);
				coinRect.remove(x);
				break;
			}
		}

		for(int x=0;x<=bombRect.size()-1;x++)
		{
			if(Intersector.overlaps(manRect,bombRect.get(x))){
				gameState=2;
				touchedBomb=true;
				endGame.play();
			}
		}


		font.draw(batch , String.valueOf(score) , 100 , 200);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
