package com.mygdx.game.spaceshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by patryk on 2016-04-01.
 */
public class EnemyController {
    private Texture enemyAvatar;
    private Array<Rectangle> enemies;

    public EnemyController() {
        enemies = new Array<>();
        enemyAvatar = null;
    }

    public void setEnemyAvatar( String path){
        enemyAvatar = new Texture(path);
    }

    public Texture getEnemyAvatar() {
        return enemyAvatar;
    }

    public void addEnemy( int x, int y) {
        enemies.add(new Rectangle(x, y, 50, 50));
    }

    public Array<Rectangle> getEnemies() {
        return enemies;
    }

    public Rectangle getEnemy(int index) {
        return enemies.get(index);
    }

    public void die(int index){
        enemies.removeIndex(index);
    }

    public void dispose(){
        enemyAvatar.dispose();
    }
}
