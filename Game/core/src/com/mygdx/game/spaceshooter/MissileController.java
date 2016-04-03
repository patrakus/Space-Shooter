package com.mygdx.game.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by patryk on 2016-03-30.
 */
class MissileController {

    private Texture missileAvatar;
    private Array<Rectangle> missiles;

    MissileController() {

        missiles = new Array<>();
        missileAvatar = null;
    }

    // sprawdzić czy obiekty odnoszą się jeszcze gdzieś

    void setMissileAvatar( String path) {

        missileAvatar = new Texture(path);
    }

    public Texture getMissileAvatar() {
        return missileAvatar;
    }

    private void addAndSetMissile(float x, float y, float width, float height) {

        missiles.add(new Rectangle(x, y, width, height));
    }

    private void setMissile( Rectangle rectangle) {

        missiles.add(rectangle);
    }

    void shoot(Vector2 pos) {

        addAndSetMissile(pos.x, pos.y, 50,50);
    }

    void moveMissiles(float ySpeed) {
        Iterator<Rectangle> iteratorMissiles = missiles.iterator();

        while (iteratorMissiles.hasNext()) {
            Rectangle missile = iteratorMissiles.next();
            missile.y += ySpeed * Gdx.graphics.getDeltaTime();

             if (isMissileOutOfStage( missile)) {
                 destroyMissile(iteratorMissiles);
             }
        }
    }

    boolean isHit( Rectangle npc) {

        GameObjectInformation gmi = new GameObjectInformation();

        for ( Rectangle missile: missiles){
            if (missile.overlaps(npc))
                return true;
        }
        return false;
    }

    public Rectangle getMissile( int index) {
        return missiles.get(index);
    }

    Array<Rectangle> getMissiles() {
        return missiles;
    }

    void dispose() {

        missileAvatar.dispose();
    }

    void destroyMissile( int index) {

        missiles.removeIndex( index);
    }

    private void destroyMissile( Iterator iteratorMissile) {

        iteratorMissile.remove();
    }

    private boolean isMissileOutOfStage( Rectangle missile) {

        return ( missile.y - 50 > 800 || missile.y <0 - missile.height);
    }
}
