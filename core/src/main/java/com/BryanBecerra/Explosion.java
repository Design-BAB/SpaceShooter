package com.BryanBecerra;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/** A brief expanding and fading destruction effect. */
public final class Explosion extends Actor {
    private static final float DURATION = 0.55f;
    private final TextureRegion image;
    private final float centerX;
    private final float centerY;
    private float elapsed;

    Explosion(Space space, double x, double y, boolean large) {
        image = new TextureRegion(space.getTexture(large ? "explosion-big.png" : "explosion.png"));
        centerX = (float) x;
        centerY = (float) y;
        setSize(20, 20);
        setPosition(centerX - getWidth() / 2, centerY - getHeight() / 2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsed += delta;
        float progress = Math.min(1, elapsed / DURATION);
        setSize(24 + progress * 115, 24 + progress * 115);
        setPosition(centerX - getWidth() / 2, centerY - getHeight() / 2);
        if (elapsed >= DURATION) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float alpha = Math.max(0, 1 - elapsed / DURATION);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(image, getX(), getY(), getWidth(), getHeight());
        batch.setColor(1, 1, 1, 1);
    }
}
