package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 13/02/15.
 */
public class TimerBar {

    private GameWorld world;
    private Rectangle rectangle;
    private String text;
    private Sprite sprite, barSprite;


    private TweenManager manager;
    private Value xValue = new Value();
    private Value xValueText = new Value();
    private Value yValue = new Value();
    private TweenCallback cbDissapear;

    private boolean appear = false;
    private Color color;
    private Value widthValue = new Value();
    TweenCallback cb;
    Tween timerTween;

    public TimerBar(final GameWorld world, String text, float y, Color color) {
        this.text = text;
        this.world = world;
        this.color = color;
        cbDissapear = new TweenCallback() {

            @Override
            public void onEvent(int type, BaseTween<?> source) {
                xValue.setValue((world.gameWidth - AssetLoader.timerBar
                        .getRegionWidth() / 2) / 2 - world.gameWidth);
                xValueText.setValue((world.gameWidth - AssetLoader.timerBar
                        .getRegionWidth() / 2) / 2 - world.gameWidth);
                appear = false;
            }
        };

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        rectangle = new Rectangle((world.gameWidth - AssetLoader.timerBar.getRegionWidth() / 2) / 2,
                world.gameHeight - 100, AssetLoader.timerBar.getRegionWidth() / 2, 50);
        sprite = new Sprite(AssetLoader.timerBar);
        sprite.setColor(world.parseColor(Configuration.COLOR_TIME_BAR, 1f));
        sprite.setPosition(rectangle.x, rectangle.y);
        sprite.setSize(rectangle.width, rectangle.height);
        sprite.setAlpha(1f);

        barSprite = new Sprite(AssetLoader.square);
        barSprite.setPosition(rectangle.x + 6, rectangle.y + 6);
        barSprite.setSize(rectangle.width - 12, rectangle.height-12);
        barSprite.setColor(world.parseColor(Configuration.COLOR_MOVING_TIMER_BAR,1f));
        barSprite.setAlpha(0.5f);
        yValue.setValue(rectangle.y);
        xValue.setValue(rectangle.x);
        xValueText.setValue(rectangle.x);
        widthValue.setValue(rectangle.width - 12);

        cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                widthValue = new Value();
                widthValue.setValue(rectangle.width - 12);
                timerTween = Tween.to(widthValue, -1, Configuration.TIMER_TIME).delay(0f).target(0).ease(
                        TweenEquations.easeNone).start(manager);
                widthValue.setValue(rectangle.width - 12);
            }
        };

    }

    public void update(float delta) {
        manager.update(delta);
        rectangle.setPosition(xValue.getValue(), yValue.getValue());
        sprite.setPosition(rectangle.getX(), rectangle.getY());
        barSprite.setPosition(rectangle.getX() + 6, rectangle.getY() + 6);
        barSprite.setSize(widthValue.getValue(), rectangle.height-12);

        if(widthValue.getValue()<=world.gameWidth&& widthValue.getValue()>=600){
            sprite.setColor(world.parseColor("FFFFFF",1f));
            barSprite.setColor(world.parseColor("#27ae60",1f));
        }else if(widthValue.getValue()<600&& widthValue.getValue()>=200){
            sprite.setColor(world.parseColor("FFFFFF",1f));
            barSprite.setColor(world.parseColor("#e67e22",1f));
        }else {
            sprite.setColor(world.parseColor("FFFFFF",1f));
            barSprite.setColor(world.parseColor("#e74c3c",1f));
        }
        barSprite.setAlpha(0.5f);

        if(widthValue.getValue()<=0 && world.isRunning()){
            world.finish();
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontShader) {
        if (appear) {
            sprite.draw(batch);
            barSprite.draw(batch);

        }
    }

    public void appear() {
        appear = true;

        xValue.setValue((world.gameWidth - AssetLoader.timerBar
                .getRegionWidth() / 2) / 2 - world.gameWidth);
        xValueText.setValue(-(world.gameWidth - AssetLoader.timerBar.getRegionWidth() / 2) / 2);

        Tween.to(xValue, -1, .3f).target(xValue.getValue() + world.gameWidth)
                .ease(TweenEquations.easeInOutSine)
                .delay(0f).start(
                manager);
        Tween.to(xValueText, -1, .3f).target((world.gameWidth - AssetLoader.timerBar
                .getRegionWidth() / 2) / 2 + world.gameWidth).ease(TweenEquations.easeInOutSine)
                .delay(0f).start(
                manager);
        reset();
    }

    public void dissapear() {
        xValue.setValue(rectangle.getX());
        xValueText.setValue(rectangle.getX());
        timerTween.kill();
        Tween.to(xValue, -1, .3f).target(xValue.getValue() + world.gameWidth)
                .ease(TweenEquations.easeInOutSine)
                .setCallback(cbDissapear).delay(0f).setCallbackTriggers(
                TweenCallback.COMPLETE).start(manager);
        Tween.to(xValueText, -1, .3f).delay(0f).target(world.gameWidth).ease(
                TweenEquations.easeInOutSine).start(manager);
        Tween.to(widthValue, -1, .3f).delay(0f).target(rectangle.width - 12).ease(
                TweenEquations.easeInOutSine).start(manager);
    }

    private void kill() {
        timerTween = null;
        TweenCallback cb1 = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                timerTween.kill();
            }
        };
        timerTween = Tween.to(widthValue, -1, .9f).delay(0f).target(0.1f).ease(
                TweenEquations.easeInOutSine).setCallback(cb1)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(
                        manager);
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


    public void startTimer() {
        widthValue.setValue(rectangle.width - 12);
        timerTween = null;
        timerTween = Tween.to(widthValue, -1, 5).delay(0f).target(0).ease(
                TweenEquations.easeInOutSine).start(manager);

    }

    public void reset() {
        timerTween = null;
        timerTween = Tween.to(widthValue, -1, .3f).delay(0f).target(rectangle.width - 12).ease(
                TweenEquations.easeInOutSine).setCallback(cb)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(
                        manager);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void pause() {
        timerTween.pause();
    }
}
