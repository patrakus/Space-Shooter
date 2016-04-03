package com.mygdx.game.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by patryk on 2016-03-26.
 * Bardzo Prosta klasa definiująca zachowanie komputera
 */
public class SimpleAI {

    private Array<Rectangle> listOfNPC;
    private Array<Vector2> listOfNPCPositions;
    private Array<Vector2> listOfNPCNewPositions;
    private Array<Direction> listOfNPCDirections;
    private float distanceFromPosition;
    private float timeToMove, currentTime;
    private float timeToNextShoot, currentTimeFromLastShoot;


    public SimpleAI( Array<Rectangle> listOfNPC, float distanceFromPosition, float timeToNove) throws Exception {

        this.listOfNPC = listOfNPC;
        this.distanceFromPosition = distanceFromPosition;
        this.timeToMove = timeToNove;
        this.currentTime = 0;
        this.timeToNextShoot = 1;
        currentTimeFromLastShoot = 0;
        createListOfPositions();
        createListOfDirections();
        createListOfNPCNewPositions();

    }

    public void moveNPC() {

        currentTime += Gdx.graphics.getDeltaTime();

        float alpha = currentTime/ timeToMove;
        Iterator<Rectangle> iterator = listOfNPC.iterator();
        Iterator<Vector2> iterator1 = listOfNPCNewPositions.iterator();
        Vector2 presentPosition = new Vector2();

        while (iterator.hasNext()) {

            Rectangle npc = iterator.next();
            npc.getPosition(presentPosition);
            npc.setPosition(presentPosition.lerp(iterator1.next(), alpha));
        }
    }

    private void restrainNPC (float min, float max) {

    }

    public void reverseDirectionList() {

        Array<Direction> newListOfNPCDirections = new Array<>();
        Iterator<Direction> iterator = listOfNPCDirections.iterator();
        Direction dir;

        while (iterator.hasNext()) {

            dir = iterator.next();

            switch (dir) {
                case LEFT:
                    newListOfNPCDirections.add(Direction.RIGHT);
                    break;
                case RIGHT:
                    newListOfNPCDirections.add(Direction.LEFT);
            }
        }

        listOfNPCDirections = newListOfNPCDirections;
    }

    public boolean isMoveFinished() {

        return (currentTime >= timeToMove);
    }

    public void resetCurrentTime() {
        currentTime = 0;
        try {
            createListOfNPCNewPositions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAllLists( int index) {

        listOfNPCPositions.removeIndex(index);
        listOfNPCNewPositions.removeIndex(index);
        listOfNPCDirections.removeIndex(index);
    }

    //Return tue if shooted - przemyśleć
    public boolean shoot( MissileController missileController) {

        currentTimeFromLastShoot += Gdx.graphics.getDeltaTime();
        if (currentTimeFromLastShoot / timeToNextShoot  >=1) {
            RandomXS128 randomXS128 = new RandomXS128();
            Vector2 pos = new Vector2();
            listOfNPC.get(randomXS128.nextInt(listOfNPC.size)).getPosition(pos);
            missileController.shoot(pos);

            currentTimeFromLastShoot = 0;
            return true;
        }

        return false;
    }

    private void createListOfPositions() {

        listOfNPCPositions = new Array<>();

        for (Rectangle aListOfNPC : listOfNPC) {
            Vector2 position = new Vector2();
            aListOfNPC.getPosition(position);
            listOfNPCPositions.add(position);
        }
    }

    private void createListOfDirections() {

        listOfNPCDirections = new Array<>();
        RandomXS128 random = new RandomXS128();

        for ( int i = 0; i< listOfNPC.size; ++i) {

            int randomizedNumber = random.nextInt(2);

            switch ( randomizedNumber) {
                case 0:
                    listOfNPCDirections.add(Direction.LEFT);
                    break;
                case 1:
                    listOfNPCDirections.add(Direction.RIGHT);
                    break;
            }
        }
    }

    private void createListOfNPCNewPositions() throws Exception {

        if (listOfNPCDirections.size != listOfNPCPositions.size) {
            throw new Exception("listOfNPCDirections i listOfNPCPositions nie są równe");
        }

        listOfNPCNewPositions = new Array<>();
        Vector2 newPosition;
        Iterator<Direction> iteratorOfNPCDirections = listOfNPCDirections.iterator();
        Direction direction;

        if (!iteratorOfNPCDirections.hasNext())  {
            throw new NoSuchElementException("Brak elementów");
        }

        for ( Vector2 position: listOfNPCPositions) {

            direction = iteratorOfNPCDirections.next();

            if (direction == Direction.LEFT) {

                newPosition = new Vector2( position.x - distanceFromPosition, position.y);
            }
            else {

                newPosition = new Vector2( position.x + distanceFromPosition, position.y);
            }

            listOfNPCNewPositions.add(newPosition);
        }
    }
}
