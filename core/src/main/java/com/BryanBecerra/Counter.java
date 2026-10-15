package com.BryanBecerra;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

/** A compact score display that eases its visible value toward the target. */
public final class Counter extends Actor {
    private final BitmapFont font;
    private int value;
    private int target;
    private String prefix = "Score: ";

    Counter(BitmapFont font) {
        this.font = font;
        this.setSize(230, 40);
        this.setPosition(24, 24);
    }

    void add(int amount) {
        target += amount;
    }

    int getValue() {
        return target;
    }

    void setValue(int value) {
        this.value = value;
        target = value;
    }

    void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (value < target) {
            value = Math.min(target, value + Math.max(1, (int) (delta * 90)));
        } else if (value > target) {
            value = Math.max(target, value - Math.max(1, (int) (delta * 90)));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.setColor(Color.WHITE);
        font.getData().setScale(1.7f);
        font.draw(batch, prefix + value, getX(), getY() + getHeight());
        font.getData().setScale(1);
    }
}
