package com.mygdx.game.spaceshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private ScreenHelper helper;
	private Sound playerShootSound;
	private Sound explosionSound;
	private Sound enemyShootSound;
	private SimpleAI AI;
	private PlayerController playerController;
	private MissileController playerMissileController;
	private EnemyController enemyController;
	private MissileController enemyMissileController;

	private final int SIZE_OF_ENEMY = 60;

	@Override
	public void create () {

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 640);

		helper = new ScreenHelper();

		playerShootSound = Gdx.audio.newSound(Gdx.files.internal("ShipShoot.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("myExplosion.wav"));
		enemyShootSound = Gdx.audio.newSound(Gdx.files.internal("enemy_shoot.wav"));

		playerController = new PlayerController();
		playerController.setPlayer( 800 / 2 - 50 / 2, 20, 50, 50);
		playerController.setPlayerAvatar("player.png");

		playerMissileController = new MissileController();
		playerMissileController.setMissileAvatar("bullet.png");

		enemyController = new EnemyController();
		enemyController.setEnemyAvatar("enemy.png");
		createEnemy(SIZE_OF_ENEMY);

		enemyMissileController = new MissileController();
		enemyMissileController.setMissileAvatar("bullet.png");

		try {
			AI = new SimpleAI(enemyController.getEnemies(), 10, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		String playerPos = "X: " + String.valueOf(playerController.getPlayer().x) + " Y: " + String.valueOf(playerController.getPlayer().y);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(playerController.getPlayerAvatar(), playerController.getPlayer().x, playerController.getPlayer().y);
		helper.font.draw(batch, playerPos, playerController.getPlayer().getX() ,  playerController.getPlayer().getY() + 70);

		for( Rectangle enemy : enemyController.getEnemies()) {
			batch.draw(enemyController.getEnemyAvatar(), enemy.x, enemy. y);
		}

		for (Rectangle bullet : playerMissileController.getMissiles()) {
			batch.draw(playerMissileController.getMissileAvatar(), bullet.x, bullet.y);
			String bulletPos = "X: " + String.valueOf(bullet.x) + " Y: " + String.valueOf(bullet.y);
			helper.font.draw(batch, bulletPos, bullet.x, bullet.y);
		}

		for (Rectangle bullet : enemyMissileController.getMissiles())
			batch.draw(playerMissileController.getMissileAvatar(), bullet.x, bullet.y);

		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			playerMissileController.shoot(new Vector2(playerController.getPlayer().getX(), playerController.getPlayer().getY() + 50));
			playerShootSound.play();
		}


		playerMissileController.moveMissiles(200);
		enemyMissileController.moveMissiles(-200);
		AI.moveNPC();
		if (AI.shoot(enemyMissileController)) { //TODO: PRZEMYSLEC TO!!!! NA PEWNO TAK NIE MOZE BYC!!! MUSI BYC VOID I JAKAS INNA METODA!!!
			enemyShootSound.play();
		}
		//AI.restrainNPC(0, 800);
		if (AI.isMoveFinished()) {
			AI.reverseDirectionList();
			AI.resetCurrentTime();
		}

		for (int i = 0; i < enemyController.getEnemies().size; i++) {
			if (playerMissileController.isHit(enemyController.getEnemy(i))) {
				enemyController.die(i);
				playerMissileController.destroyMissile(0);
				explosionSound.play();
				AI.updateAllLists(i);
			}
		}

		if (enemyMissileController.isHit(playerController.getPlayer())) {
			Gdx.app.exit();
		}

		// tu poprawic
		if (Gdx.input.isKeyPressed(Input.Keys.A)) playerController.move(new Vector2(- 300 * Gdx.graphics.getDeltaTime(), 0));
		if (Gdx.input.isKeyPressed(Input.Keys.D)) playerController.move(new Vector2(300 * Gdx.graphics.getDeltaTime(), 0));

		playerController.restrain( 800, 640);

	}

	@Override
	public void dispose() {


		playerController.dispose();
		enemyController.dispose();
		playerMissileController.dispose();
		enemyMissileController.dispose();
		batch.dispose();
		helper.font.dispose();
		playerShootSound.dispose();
		explosionSound.dispose();
		enemyShootSound.dispose();
	}

	private void createEnemy(int size) {

		int x = 1;
		int y = 640;
		int row = 1;
		int column = 1;

		for (int i = 0; i < size; i++) {
			if ( i > 0 && enemyController.getEnemy(i -1).x  > 640) {
				++row;
				column = 1;
			}

			enemyController.addEnemy((20 + 50) * column, y - (70 * row));
			++column;
		}
	}

	/*private void shoot() {

		Rectangle bullet = new Rectangle();
		bullet.width = 50;
		bullet. height = 50;
		bullet.setX( playerController.getPlayer().x);
		bullet.setY(playerController.getPlayer().y + 50);
		bullets.add(bullet);
	}

	private void moveBullet() {

		Iterator<Rectangle> iter = bullets.iterator();
		while (iter.hasNext()) {
			Rectangle bullet = iter.next();
			bullet.y += 200 * Gdx.graphics.getDeltaTime();

			if ( bullet.y - 50 > 800) iter.remove();
		}
	}

	private void killEnemy() {

		Iterator<Rectangle> iterBullet = bullets.iterator();

		while (iterBullet.hasNext()) {

			// Jeżeli przy jednym pociksu nie wykryje przeslonięcia to wtedy iterator jest na końcu i się nie resetuje po ponownym wejściu do pętli
			// dlatego trzeba go odnowa przypisać
			Iterator<Rectangle> iterEnemy = enemies.iterator();
			int index = 0;

			Rectangle bullet = iterBullet.next();
			while (iterEnemy.hasNext()) {

				try {
					if ( bullet.overlaps( iterEnemy.next())){
						iterEnemy.remove();
						explosionSound.play();
						iterBullet.remove();
						AI.updateAllLists(index);
						break;
					}
				}
				catch ( NoSuchElementException e) {
					System.out.println( e.getMessage());
				}
				++index;
			}
		}
	}*/
}
