package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import Tweens.SpriteAccessor;
import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import gameworld.GameState;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 23/02/15.
 */
public class Hero {

    private final ParticleEffect effect;
    private Vector2 velocity, position, acceleration;
    private Sprite sprite;
    private GameWorld world;
    private Orbit currentOrbit;
    public Circle circle;
    private float angle;
    private float angleInc = 3.2f;
    private float radius;
    private HeroState heroState;
    private float velocityValue = 2800;
    private TweenManager manager;
    private TweenCallback cb;


    public enum HeroState {
        FLYING, DEAD, ORBIT
    }

    public Hero(final GameWorld world, Orbit orbit, float radius) {
        this.world = world;
        this.currentOrbit = orbit;
        this.radius = radius;

        position = new Vector2();
        velocity = new Vector2();
        acceleration = new Vector2(0, -500);

        sprite = new Sprite(AssetLoader.dot);
        sprite.setSize(radius * 2, radius * 2);
        circle = new Circle();
        circle.setRadius(radius);

        heroState = HeroState.ORBIT;
        Tween.registerAccessor(Value.class, new ValueAccessor());
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        manager = new TweenManager();

        sprite.setAlpha(0);
        cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.RUNNING;
            }
        };

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("emitter1.p"), Gdx.files.internal(""));
        effect.setPosition(-100, -100);
    }

    public void update(float delta) {
        manager.update(delta);
        sprite.setOriginCenter();
        sprite.setRotation(angle);
        effect.update(delta);
        effect.setPosition(position.x, position.y);
        if (heroState == HeroState.ORBIT) {
            angle -= angleInc;
            circle.setPosition(calculatePosition());
            position.set(calculatePosition());
        } else if (heroState == HeroState.FLYING) {
            velocity.add(acceleration.cpy().scl(delta));
            position.add(velocity.cpy().scl(delta));
            circle.setPosition(position);
            if (outOfBounds() && world.isRunning()) {
                die();
            }
        }
        if (angle >= 360) {
            angle = 0;
        }

        sprite.setPosition(circle.x - circle.radius, circle.y - circle.radius);

        //Gdx.app.log("Angle", "" + angle);
    }

    private void die() {
        world.finish();
    }

    private boolean outOfBounds() {

        if (sprite.getX() < -100 || sprite.getX() > world.gameWidth + 100 || sprite
                .getY() > world.gameHeight + 600 || sprite.getY() < -100) {
            heroState = HeroState.DEAD;
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (heroState == HeroState.FLYING) {
            effect.draw(batch);
        }
        sprite.draw(batch);
        if (Configuration.DEBUG) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.line(new Vector2(position.x,
                            position.y),
                    new Vector2(currentOrbit.getPosition().x,
                            currentOrbit.getPosition().y));
            shapeRenderer.end();
            batch.begin();
        }
    }

    public void setCurrentOrbit(Orbit orbit) {
        currentOrbit = orbit;
    }

    private Vector2 calculatePosition() {
        float cx = currentOrbit.getPosition().x;
        float cy = currentOrbit.getPosition().y;
        return new Vector2((float) (cx + (currentOrbit.getRadius() + radius / 2 - 13)
                * Math.sin(Math.toRadians(-angle))),
                (float) (cy + (currentOrbit.getRadius() + radius / 2 - 13) * Math
                        .cos(Math.toRadians(-angle))));
    }

    public void launch() {
        heroState = HeroState.FLYING;
        Vector2 vel = new Vector2(
                (currentOrbit.getPosition().x) - (position.x),
                (currentOrbit.getPosition().y) - (position.y));
        velocity.set(-vel.nor().x * velocityValue, -vel.nor().y * velocityValue);
        currentOrbit.finish();
    }

    public void reset() {
        heroState = HeroState.ORBIT;
        angle = 0;
    }

    public void setHeroState(HeroState heroState) {
        this.heroState = heroState;
    }

    public float getAngle2Vecs(Vector2 vec, Vector2 vec1) {
        float angle1 = (float) Math.toDegrees(Math.atan2(vec1.y - vec.y, vec.x - vec1.x));
        //angle = (int) (angle1 + 90);
        return Math.abs(angle1 - 180 - 90);
    }

    public void setAngle() {
        angle = (int) getAngle2Vecs(new Vector2(position.x, position.y),
                new Vector2(currentOrbit.getPosition().x, currentOrbit.getPosition().y));
        //Gdx.app.log("Angle", angle + "");
        if (angle < 180) {
            if (angleInc < 0) {
                angleInc *= -1;
            }
        } else if (angle < 360) {
            if (angleInc > 0) {
                angleInc *= -1;
            }
        } else {
            angleInc *= -1;
        }

    }

    public Vector2 getPosition() {
        return position;
    }

    public void finish() {
        Tween.to(sprite, SpriteAccessor.ALPHA, .2f).target(0)
                .ease(TweenEquations.easeInOutSine)
                .start(manager);
    }

    public void start() {
        world.gameState = GameState.MENU;
        Tween.to(sprite, SpriteAccessor.ALPHA, .4f).target(1)
                .ease(TweenEquations.easeInOutSine).setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(cb).delay(
                .2f)
                .start(manager);
    }

    public void startMenu() {
        Tween.to(sprite, SpriteAccessor.ALPHA, .5f).target(1)
                .ease(TweenEquations.easeInOutSine).delay(.2f)
                .start(manager);
    }


    public HeroState getHeroState() {
        return heroState;
    }
}

