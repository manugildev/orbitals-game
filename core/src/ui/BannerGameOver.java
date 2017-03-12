package ui;

/**
 * Created by ManuGil on 16/02/15.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 13/02/15.
 */
public class BannerGameOver {

    private GameWorld world;
    private Rectangle rectangle;
    private String text;
    private Sprite sprite;


    private TweenManager manager;
    private Value xValue = new Value();
    private Value xValueText = new Value();
    private Value yValue = new Value();
    private TweenCallback cbDissapear;

    private boolean appear = false;
    private Color color;

    public BannerGameOver(final GameWorld world, String text, float y, Color color) {
        this.text = text;
        this.world = world;
        this.color = color;
        cbDissapear = new TweenCallback() {

            @Override
            public void onEvent(int type, BaseTween<?> source) {
                xValue.setValue(-world.gameWidth);
                xValueText.setValue(-world.gameWidth);
                appear = false;
            }
        };

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        rectangle = new Rectangle(-world.gameWidth, y, world.gameWidth, 100);
        sprite = new Sprite(AssetLoader.titleGameover);
        sprite.setPosition(rectangle.x, rectangle.y);
        sprite.setSize(rectangle.width, rectangle.height);
        sprite.setColor(color);
        sprite.setAlpha(1f);
        yValue.setValue(rectangle.y);
        xValue.setValue(rectangle.x);
        xValueText.setValue(rectangle.x);


    }

    public void update(float delta) {
        manager.update(delta);
        rectangle.setPosition(xValue.getValue(), yValue.getValue());
        sprite.setPosition(rectangle.getX(), rectangle.getY());
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontShader) {
        if (appear) {
            sprite.draw(batch);
            batch.setShader(fontShader);
            AssetLoader.font2
                    .draw(batch, text, xValueText.getValue(),
                            rectangle.y + rectangle.height - 24,
                            rectangle.width,
                                 Align.center,false);

            batch.setShader(null);
        }
    }

    public void appear() {
        appear = true;
        xValue.setValue(-world.gameWidth);
        xValueText.setValue(-world.gameWidth);
        Tween.to(xValue, -1, .3f).target(0).ease(TweenEquations.easeInOutSine)
                .delay(0f).start(
                manager);
        Tween.to(xValueText, -1, .3f).target(0).ease(TweenEquations.easeInOutSine).delay(0f).start(
                manager);
    }

    public void dissapear() {
        xValue.setValue(0);
        xValueText.setValue(0);
        Tween.to(xValue, -1, .3f).target(world.gameWidth).ease(TweenEquations.easeInOutSine)
                .setCallback(cbDissapear).delay(0f).setCallbackTriggers(
                TweenCallback.COMPLETE).start(manager);
        Tween.to(xValueText, -1, .3f).delay(0f).target(world.gameWidth).ease(
                TweenEquations.easeInOutSine).start(manager);
    }

    public void appearDissapear(float time) {
        appear = true;
        Timeline.createSequence()
                .push(Tween.to(xValue, -1, .5f).target(0).ease(TweenEquations.easeInOutSine))
                .pushPause(time)
                .push(Tween.to(xValue, -1, .5f).target(world.gameWidth)
                        .ease(TweenEquations.easeInOutSine)
                        .setCallback(cbDissapear).setCallbackTriggers(
                                TweenCallback.COMPLETE))
                .start(manager);
    }

    public void setText(String text) {
        this.text = text;
    }
}
