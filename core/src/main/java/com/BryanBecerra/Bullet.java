package com.BryanBecerra;

import com.badlogic.gdx.scenes.scene2d.Actor;

/** A short-lived projectile that damages the first asteroid it hits. */
public final class Bullet extends SmoothMover {
    private static final int DAMAGE = 1;
    private static final float LIFETIME = 1.8f;
    private final Space space;
    private float remainingLife = LIFETIME;

    Bullet(Space space, double x, double y, Vector velocity) {
        super(space.getTexture("bullet.png"), 18, 18);
        this.space = space;
        setCenter(x, y);
        setVelocity(velocity);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (space.isGameOver()) {
            return;
        }
        remainingLife -= delta;
        if (remainingLife <= 0) {
            remove();
            return;
        }
        move(delta);
        for (Actor actor : space.getStage().getActors()) {
            if (actor instanceof Asteroid asteroid
                && getBounds().overlaps(asteroid.getBounds())) {
                remove();
                asteroid.hit(DAMAGE);
                return;
            }
        }
    }
}
