package com.mygdx.game.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by patryk on 2016-03-30.
 */
class PlayerController {

    private Texture playerAvatar;
    private Rectangle player;

    PlayerController() {

        player = null;
        playerAvatar = null;
    }

    void move(Vector2 move) {

        player.setPosition( player.getX() + move.x, player.getY() + move.y);
    }

    void setPlayerAvatar(String path) {

        playerAvatar = new Texture(path);
    }

    void setPlayer( float x, float y, float width, float height) {

        player = new Rectangle(x, y, width, height);
    }

    public void setPlayer( Rectangle rectangle) {

        player = rectangle;
    }

    Rectangle getPlayer() {
        return player;
    }

    Texture getPlayerAvatar() {
        return playerAvatar;
    }

    boolean isPlayerHit(Rectangle missile) {

        return  missile.overlaps(player);
    }

    public void die() {
        player = null;
    }

    void dispose() {

        playerAvatar.dispose();
    }

    void restrain( float width, float height) {

        if (player.getX() > width - player.width) {
            player.x = width - player.width;
        }
        else if (player.getX() < 0) {
            player.x = 0;
        }

        if (player.getY() > height - player.height) {
            player.y = height - player.height;
        }
        else if (player.getY() > height) {
            player.y = 0;
        }
    }
}
