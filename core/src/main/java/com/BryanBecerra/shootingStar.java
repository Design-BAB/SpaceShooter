/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.BryanBecerra;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Random;


public class shootingStar extends Actor{
    
    private Space space;
    private Texture texture;
    private Rectangle bounds;
    private Random randomer = new Random();
    
    shootingStar(Space space) {
        this.space = space;
        texture = space.getTexture("shootingStar.png");
        var x = 0;
        var y = randomer.nextInt((int)space.WORLD_SIZE);
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }
    
    @Override
    public void act(float delta) {
        if (space.isGameOver() == false) {
            if (bounds.x > space.WORLD_SIZE + 50) bounds.x = 0; 
            var howMuch = 100 * delta;
            bounds.x = bounds.x + howMuch;
        }
    }
    
    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, bounds.x, bounds.y);
    }
}
