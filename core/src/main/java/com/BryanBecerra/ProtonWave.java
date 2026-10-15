package com.BryanBecerra;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/** An expanding area attack that can damage each asteroid only once. */
public final class ProtonWave extends Actor {
    private static final int DAMAGE = 3;
    private static final float MAX_RADIUS = 205;
    private static final float GROWTH_SPEED = 510;
    private final Space space;
    private final Texture texture;
    private final double centerX;
    private final double centerY;
    private final Array<Asteroid> hitAsteroids = new Array<>();
    private float radius = 18;

    ProtonWave(Space space, double x, double y) {
        this.space = space;
        texture = space.getTexture("wave.png");
        centerX = x;
        centerY = y;
        setSize(radius * 2, radius * 2);
        setPosition((float) x - radius, (float) y - radius);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (space.isGameOver()) {
            return;
        }
        checkCollision();
        radius = Math.min(MAX_RADIUS, radius + GROWTH_SPEED * delta);
        setSize(radius * 2, radius * 2);
        setPosition((float) centerX - radius, (float) centerY - radius);
        if (radius >= MAX_RADIUS) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float alpha = Math.max(0.12f, 0.7f * (1 - radius / MAX_RADIUS));
        batch.setColor(1, 1, 1, alpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        batch.setColor(1, 1, 1, 1);
    }

    private void checkCollision() {
        Array<Asteroid> asteroids = new Array<>();
        for (Actor actor : space.getStage().getActors()) {
            if (actor instanceof Asteroid asteroid) {
                asteroids.add(asteroid);
            }
        }
        for (Asteroid asteroid : asteroids) {
            if (hitAsteroids.contains(asteroid, true)) {
                continue;
            }
            double distance = Math.hypot(asteroid.getX() + asteroid.getWidth() / 2.0 - centerX,
                asteroid.getY() + asteroid.getHeight() / 2.0 - centerY);
            if (distance <= radius + asteroid.getWidth() / 2.0) {
                hitAsteroids.add(asteroid);
                asteroid.hit(DAMAGE);
            }
        }
    }
}
