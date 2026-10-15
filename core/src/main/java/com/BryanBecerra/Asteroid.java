package com.BryanBecerra;

import com.badlogic.gdx.math.MathUtils;

/** A destructible rock that splits into smaller rocks when destroyed. */
public final class Asteroid extends SmoothMover {
    private static final float[] SIZES = {22, 38, 60, 86};
    private final Space space;
    private final int size;
    private int stability;

    Asteroid(Space space, int size, double x, double y) {
        this(space, size, x, y,
            Vector.fromAngle(MathUtils.random(360f), MathUtils.random(35f, 100f)));
    }

    Asteroid(Space space, int size, double x, double y, Vector velocity) {
        super(space.getTexture("rock.png"), SIZES[size], SIZES[size]);
        this.space = space;
        this.size = size;
        setCenter(x, y);
        setVelocity(velocity);
        setRotation(MathUtils.random(360f));
        stability = size;
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        if (space.isGameOver() == false) {
            move(delta);
            rotateBy(12 * delta);
        }
    }

    void hit(int damage) {
        if (stability <= 0 || space.isGameOver()) {
            return;
        }
        stability -= damage;
        if (stability <= 0) {
            breakUp();
        }
    }

    private void breakUp() {
        double x = getCenterX();
        double y = getCenterY();
        Vector velocity = getVelocity();
        remove();
        space.addScore(size * 10);
        space.addExplosion(x, y, size >= 2);

        if (size > 1) {
            int childSize = size - 1;
            Vector firstVelocity = velocity.copy().rotate(-35).add(Vector.fromAngle(
                velocity.getAngle() - 35, 28));
            Vector secondVelocity = velocity.copy().rotate(35).add(Vector.fromAngle(
                velocity.getAngle() + 35, 28));
            space.getStage().addActor(new Asteroid(space, childSize, x - 12, y, firstVelocity));
            space.getStage().addActor(new Asteroid(space, childSize, x + 12, y, secondVelocity));
        }
    }
}
