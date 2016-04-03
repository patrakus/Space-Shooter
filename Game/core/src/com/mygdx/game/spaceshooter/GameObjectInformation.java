package com.mygdx.game.spaceshooter;

import com.badlogic.gdx.utils.Array;

/**
 * Created by patryk on 2016-04-01.
 */
public class GameObjectInformation {
    private boolean state;
    private Array<Integer> objectIndex;

    public GameObjectInformation() {
        state = false;
        objectIndex = new Array<>();
    }

    public void addObjectIndex(int index) {
        objectIndex.add(index);
        state = true;
    }

    public boolean isStateChanged() {
        return state;
    }

    public Array<Integer> getObjectIndex() {
        return objectIndex;
    }

    public void reset(){
        state = false;
        objectIndex.clear();
    }
}
