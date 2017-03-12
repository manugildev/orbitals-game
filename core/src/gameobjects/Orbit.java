package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import Tweens.SpriteAccessor;
import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 23/02/15.
 */
public class Orbit {

    public OrbitState orbitState;


    public enum OrbitState {
        ALIVE, DEAD
    }

    private GameWorld world;
    public Circle circle;
    private Sprite sprite, orbitSprite, effectSprite;
    private Vector2 position;
    private float angle;
    private float angleInc;

    private TweenManager manager;
    private Value yValue = new Value();
    private Value scaleValue = new Value();
    private Value scaleEffectValue = new Value();
    private Tween timerTween;

    public Orbit(GameWorld world, float x, float y, float radius) {
        this.world = world;
        circle = new Circle(x, y, radius);
        sprite = new Sprite(AssetLoader.dot);
        sprite.setPosition(circle.x, circle.y);
        sprite.setAlpha(0.9f);
        position = new Vector2(circle.x, circle.y);
        sprite.setSize(radius * 2 - 80, radius * 2 - 80);

        orbitSprite = new Sprite(AssetLoader.orbit);
        orbitSprite.setAlpha(0.6f);
        orbitSprite.setPosition(circle.x, circle.y);
        orbitSprite.setSize(radius * 2, radius * 2);

        effectSprite = new Sprite(AssetLoader.dotEffect);
        effectSprite.setAlpha(0.6f);
        effectSprite.setPosition(circle.x, circle.y);
        effectSprite.setSize(radius * 2, radius * 2);

        Tween.registerAccessor(Value.class, new ValueAccessor());
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        manager = new TweenManager();
        yValue.setValue(circle.y);
        angleInc = MathUtils.random(0.5f, 1f);
        if (Math.random() < 0.5f) {
            angleInc *= -1;
        }

        timerTween = Tween.to(scaleValue, -1, 0f);
        scaleValue.setValue(1);
        scaleEffectValue.setValue(1);
        orbitState = OrbitState.ALIVE;

        sprite.setAlpha(0);
        orbitSprite.setAlpha(0);
        effectSprite.setAlpha(0);
    }

    public void update(float delta) {
        angle += angleInc;
        manager.update(delta);
        //sprite.setScale(scaleValue.getValue());
        effectSprite.setScale(scaleValue.getValue());
        position.set(circle.x, yValue.getValue());

        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        sprite.setOriginCenter();
        effectSprite.setPosition(position.x - circle.radius,
                position.y - circle.radius);
        effectSprite.setOriginCenter();
        orbitSprite.setScale(scaleValue.getValue());
        orbitSprite.setPosition(position.x - circle.radius, position.y - circle.radius);
        orbitSprite.setRotation(angle);
        orbitSprite.setOriginCenter();
        circle.setPosition(position);

        if (position.y < world.w * 24 && orbitState == OrbitState.ALIVE) {
            finish();
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        sprite.draw(batch);
        orbitSprite.draw(batch);
        effectSprite.draw(batch);
        if (Configuration.DEBUG) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
            shapeRenderer.end();
            batch.begin();

        }
    }

    public void dissapearAndAppear() {
        /*Vector2 point;
        Vector2 dist;
        Vector2 pos = new Vector2(circle.x, circle.y);
        int i = -1;
        do {
            i++;
            point = world.getPoints().get((int) Math.floor(Math.random() * world.getPoints().size));

            dist = new Vector2(point.x - pos.x, point.y - pos.y);
            Gdx.app.log("Dist",  i + "");
            Gdx.app.log("Dist2", dist.len() + "");



        } while (point.x > world.getCamera()
                .getPoint().x + world.gameWidth / 2 || point.x < world
                .getCamera().getPoint().x - world.gameWidth / 2 || point.y > world
                .getCamera()
                .getPoint().y + world.gameHeight / 2 || point.y < world
                .getCamera().getPoint().y - world.gameHeight / 2 || dist.len() < 900);

        circle.x = point.x;
        circle.y = point.y;
        position.set(circle.x, circle.y);

       /* do {
            // circle.x = (float) (Math.random() * world.gameWidth + world.getCamera().getBounds().getX());

        } while (circle.y > world.getCamera()
                .getPoint().y + world.gameHeight / 2 || circle.y < world
                .getCamera().getPoint().y - world.gameHeight / 2);
        */
    }

    public void scrollTo(float dist) {
        Tween.to(yValue, -1, .5f).target(yValue.getValue() + dist)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public float getRadius() {
        return circle.radius * scaleValue.getValue();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPositionY(float positionY) {
        this.position.y = positionY;
        yValue.setValue(position.y);
        circle.y = positionY;
        sprite.setY(circle.y - circle.radius);
    }

    public void reset(float y) {
        timerTween.kill();
        scaleValue.setValue(1);
        start();
        orbitState = OrbitState.ALIVE;
        setPositionY(y + world.w * (MathUtils.random() * 30 + 20));
        setPositionX(MathUtils.random(circle.radius + 30, world.gameWidth - circle.radius - 30));

    }

    private void setPositionX(float positionX) {
        this.position.x = positionX;
        circle.x = positionX;
        sprite.setX(circle.x - circle.radius);
    }

    public void startTimer() {

    }

    public void finish() {
        orbitState = OrbitState.DEAD;
        Tween.to(sprite, SpriteAccessor.ALPHA, .4f).delay(0f).target(0)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
        Tween.to(orbitSprite, SpriteAccessor.ALPHA, .4f).target(0)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
        Tween.to(effectSprite, SpriteAccessor.ALPHA, .4f).target(0)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
        //timerTween.pause();
        // Tween.to(scaleValue, -1, MathUtils.random() * .4f + .4f).target(1).ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void start() {
        orbitState = OrbitState.ALIVE;
        sprite.setAlpha(0);
        orbitSprite.setAlpha(0);
        effectSprite.setAlpha(0);
        Tween.to(sprite, SpriteAccessor.ALPHA, .6f).delay(.15f).target(1)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
        Tween.to(orbitSprite, SpriteAccessor.ALPHA, .6f).delay(.2f).target(.6f)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
        Tween.to(effectSprite, SpriteAccessor.ALPHA, .6f).delay(.2f).target(.6f)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
        //timerTween.pause();
        // Tween.to(scaleValue, -1, MathUtils.random() * .4f + .4f).target(1).ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void effect() {
        scaleValue.setValue(1);

        Tween.to(scaleValue, -1, .2f).target(1.1f).repeatYoyo(1, 0f)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
    }

    public void startMenu() {
        orbitState = OrbitState.ALIVE;
        sprite.setAlpha(0);
        orbitSprite.setAlpha(0);
        effectSprite.setAlpha(0);
        Tween.to(sprite, SpriteAccessor.ALPHA, .4f).delay(.25f).target(1)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
        Tween.to(orbitSprite, SpriteAccessor.ALPHA, .4f).delay(.4f).target(.6f)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
        Tween.to(effectSprite, SpriteAccessor.ALPHA, .4f).delay(.4f).target(.6f)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
    }

    public float getScaleValue() {
        return scaleValue.getValue();
    }
}
