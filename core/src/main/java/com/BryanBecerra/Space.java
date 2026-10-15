package com.BryanBecerra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** Owns the game world, its resources, score, and game-over state. */
public class Space extends ScreenAdapter {
    public static final float WORLD_SIZE = 900;
    private static final int START_ASTEROIDS = 6;
    
    private final AssetManager assets = new AssetManager();
    private Texture gameOverPanelTexture;
    private SpriteBatch batch = new SpriteBatch();
    private final Stage stage = new Stage(new FitViewport(WORLD_SIZE, WORLD_SIZE), batch);
    private final ShapeRenderer starRenderer = new ShapeRenderer();
    private final BitmapFont font = new BitmapFont();
    private final Array<Star> stars = new Array<>();
    private Rocket yourRocket;
    private final Counter scoreCounter;
    private boolean gameOver;

    public Space() {
        loadAssets();
        createStars(150);
        scoreCounter = new Counter(font);
        //stage is the batch draw
        startGame();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (gameOver && (keycode == Input.Keys.R || keycode == Input.Keys.ENTER)) {
                    
                    startGame();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        drawStars();
        stage.act(delta);
        stage.draw();
    }

    private void startGame() {
        stage.clear();
        gameOver = false;
        scoreCounter.setValue(0);
        yourRocket = new Rocket(this);
        stage.addActor(scoreCounter);
        stage.addActor(yourRocket);
        addAsteroids(START_ASTEROIDS);
    }

    Stage getStage() {
        return stage;
    }

    void addScore(int amount) {
        scoreCounter.add(amount);
    }
    
    boolean isGameOver() {
        return gameOver;
    }

    void addAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            double x = MathUtils.random(55f, WORLD_SIZE / 2f - 30);
            double y = MathUtils.random(55f, WORLD_SIZE - 55);
            //make sure that the astroids are away from the center of the screen
            while (Math.hypot(x - WORLD_SIZE / 2, y - WORLD_SIZE / 2) < 190) {
                x = MathUtils.random(55f, WORLD_SIZE / 2f - 30);
                y = MathUtils.random(55f, WORLD_SIZE - 55);
            }
            stage.addActor(new Asteroid(this, 3, x, y));
        }
    }

    public void gameOver() {
        if (gameOver) {
            return;
        }
        gameOver = true;
        stage.addActor(new ScoreBoard(font, gameOverPanelTexture, scoreCounter.getValue()));
    }

    void addExplosion(double x, double y, boolean large) {
        stage.addActor(new Explosion(this, x, y, large));
    }

    Texture getTexture(String fileName) {
        return assets.get(fileName, Texture.class);
    }

    private void loadAssets() {
        assets.load("bullet.png", Texture.class);
        assets.load("explosion-big.png", Texture.class);
        assets.load("explosion.png", Texture.class);
        assets.load("rock.png", Texture.class);
        assets.load("rocket.png", Texture.class);
        assets.load("rocketWithThrust.png", Texture.class);
        assets.load("wave.png", Texture.class);
        assets.finishLoading();

        Pixmap pixmap = new Pixmap(580, 250, Pixmap.Format.RGBA8888);
        try {
            pixmap.setColor(0.025f, 0.055f, 0.12f, 0.94f);
            pixmap.fill();
            pixmap.setColor(0.15f, 0.78f, 0.98f, 1);
            pixmap.drawRectangle(1, 1, 578, 248);
            gameOverPanelTexture = new Texture(pixmap);
        } finally {
            pixmap.dispose();
        }
    }

    private void createStars(int number) {
        for (int i = 0; i < number; i++) {
            stars.add(new Star(MathUtils.random(WORLD_SIZE), MathUtils.random(WORLD_SIZE),
                MathUtils.random(0.7f, 2f), MathUtils.random(0.35f, 0.95f)));
        }
    }

    private void drawStars() {
        stage.getViewport().apply();
        starRenderer.setProjectionMatrix(stage.getCamera().combined);
        starRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Star star : stars) {
            starRenderer.setColor(star.brightness, star.brightness, star.brightness, 1);
            starRenderer.circle(star.x, star.y, star.radius);
        }
        starRenderer.end();
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        starRenderer.dispose();
        font.dispose();
        batch.dispose();
        gameOverPanelTexture.dispose();
        assets.dispose();
    }

    private static final class Star {
        private final float x;
        private final float y;
        private final float radius;
        private final float brightness;

        private Star(float x, float y, float radius, float brightness) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.brightness = brightness;
        }
    }
}
