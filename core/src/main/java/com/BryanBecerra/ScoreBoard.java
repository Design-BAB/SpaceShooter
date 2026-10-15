package com.BryanBecerra;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

/** Final-score panel shown after the rocket is destroyed. */
public final class ScoreBoard extends Actor {
    private final BitmapFont font;
    private final Texture panel;
    private final int score;

    ScoreBoard(BitmapFont font, Texture panel, int score) {
        this.font = font;
        this.panel = panel;
        this.score = score;
        setSize(580, 250);
        setPosition((Space.WORLD_SIZE - getWidth()) / 2,
            (Space.WORLD_SIZE - getHeight()) / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(panel, getX(), getY(), getWidth(), getHeight());
        font.setColor(Color.WHITE);
        font.getData().setScale(2.8f);
        font.draw(batch, "Game Over", getX() + 150, getY() + 177);
        font.getData().setScale(1.8f);
        font.draw(batch, "Score: " + score, getX() + 145, getY() + 115);
        font.getData().setScale(1.2f);
        font.setColor(0.62f, 0.83f, 0.96f, 1);
        font.draw(batch, "PRESS R OR ENTER TO PLAY AGAIN", getX() + 114, getY() + 57);
        font.getData().setScale(1);
        font.setColor(Color.WHITE);
    }
}
