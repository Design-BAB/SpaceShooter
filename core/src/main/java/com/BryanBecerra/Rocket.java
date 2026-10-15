package com.BryanBecerra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

/** Player-controlled ship and weapon cooldowns. */
public final class Rocket extends SmoothMover {
    private static final float GUN_RELOAD_TIME = 0.22f;
    private static final float PROTON_RELOAD_TIME = 5.5f;
    private static final float TURN_SPEED = 220f;
    private static final float ACCELERATION = 300f;
    private final Space space;
    private final Texture rocketTexture;
    private final Texture movingRocketTexture;
    private float gunCooldown;
    private float protonCooldown;

    Rocket(Space space) {
        super(space.getTexture("rocket.png"), 48, 48);
        this.space = space;
        rocketTexture = space.getTexture("rocket.png");
        movingRocketTexture = space.getTexture("rocketWithThrust.png");
        setCenter(Space.WORLD_SIZE / 2, Space.WORLD_SIZE / 2);
        setRotation(0);
        setVelocity(Vector.fromAngle(com.badlogic.gdx.math.MathUtils.random(360f), 12));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (space.isGameOver()) {
            return;
        }

        move(delta);
        checkKeys(delta);
        gunCooldown = Math.max(0, gunCooldown - delta);
        protonCooldown = Math.max(0, protonCooldown - delta);
        checkCollision();
    }

    private void checkKeys(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            rotateBy(TURN_SPEED * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            rotateBy(-TURN_SPEED * delta);
        }

        boolean isRocketMoving = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        setTexture(isRocketMoving ? movingRocketTexture : rocketTexture);
        if (isRocketMoving) {
            addToVelocity(Vector.fromAngle(getRotation(), ACCELERATION * delta));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            fire();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            startProtonWave();
        }
    }

    private void fire() {
        if (gunCooldown > 0) {
            return;
        }
        Vector direction = Vector.fromAngle(getRotation(), 410);
        Vector bulletVelocity = getVelocity().add(direction);
        double spawnX = getCenterX() + Math.cos(Math.toRadians(getRotation())) * 29;
        double spawnY = getCenterY() + Math.sin(Math.toRadians(getRotation())) * 29;
        space.getStage().addActor(new Bullet(space, spawnX, spawnY, bulletVelocity));
        gunCooldown = GUN_RELOAD_TIME;
    }

    private void startProtonWave() {
        if (protonCooldown > 0) {
            return;
        }
        space.getStage().addActor(new ProtonWave(space, getCenterX(), getCenterY()));
        protonCooldown = PROTON_RELOAD_TIME;
    }

    private void checkCollision() {
        for (Actor actor : space.getStage().getActors()) {
            if (actor instanceof Asteroid asteroid && this.getBounds().overlaps(asteroid.getBounds())) {
                space.addExplosion(getCenterX(), getCenterY(), true);
                remove();
                space.gameOver();
                return;
            }
        }
    }
}
