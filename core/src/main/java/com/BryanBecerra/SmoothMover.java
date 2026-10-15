package com.BryanBecerra;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/** An image actor with precise, center-based movement and screen wrap-around. */
public abstract class SmoothMover extends Actor {
    private final Rectangle bounds = new Rectangle();
    private final Vector velocity = new Vector();
    private double exactX;
    private double exactY;
    private Texture texture;

    protected SmoothMover(Texture texture, float width, float height) {
        this.texture = texture;
        setSize(width, height);
        setOrigin(width / 2, height / 2);
    }

    protected final void setCenter(double x, double y) {
        setLocation(x, y);
    }

    public final void setLocation(double x, double y) {
        exactX = wrap(x);
        exactY = wrap(y);
        syncPosition();
    }

    public final void setLocation(int x, int y) {
        setLocation((double) x, y);
    }

    protected final double getCenterX() {
        return exactX;
    }

    protected final double getCenterY() {
        return exactY;
    }

    protected final void setVelocity(Vector newVelocity) {
        velocity.set(newVelocity.getX(), newVelocity.getY());
    }

    public final Vector getVelocity() {
        return velocity.copy();
    }

    public final void addToVelocity(Vector boost) {
        velocity.add(boost);
    }

    protected final void move(float delta) {
        exactX = wrap(exactX + velocity.getX() * delta);
        exactY = wrap(exactY + velocity.getY() * delta);
        syncPosition();
    }

    public final void accelerate(double factor) {
        velocity.scale(factor);
        if (velocity.getLength() < 2) {
            velocity.set(0, 0);
        }
    }

    protected final void rotateVelocity(double angleDegrees) {
        velocity.rotate(angleDegrees);
    }

    protected final Rectangle getBounds() {
        return bounds.set(getX(), getY(), getWidth(), getHeight());
    }

    protected final void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        if (getStage() != null) {
            exactX = getX() + getWidth() / 2.0;
            exactY = getY() + getHeight() / 2.0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(),
            0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }

    private void syncPosition() {
        setPosition((float) exactX - getWidth() / 2f, (float) exactY - getHeight() / 2f);
    }

    private static double wrap(double coordinate) {
        double wrapped = coordinate % Space.WORLD_SIZE;
        return wrapped < 0 ? wrapped + Space.WORLD_SIZE : wrapped;
    }
}
