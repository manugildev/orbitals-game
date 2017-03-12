//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package helpers;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.Random;

public class ColorManager {
    private TweenManager manager;
    private Value c1 = new Value();
    private Value c2 = new Value();
    private Value c3 = new Value();
    private TweenCallback cb;
    private TweenCallback cb1;
    private TweenCallback cb2;
    private Color color;
    private ArrayList<String> colors = new ArrayList();
    private float target1;
    private float target2;
    private float target3;
    private Random randomGenarator;
    private Integer random;
    private Integer rtime;

    public ColorManager() {
        this.colors.add("00495E");
        this.colors.add("#34495e");
        this.colors.add("103D5D");
        this.colors.add("403D5D");
        this.colors.add("22285E");
        this.colors.add("00303B");
        this.color = parseColor((String)this.colors.get(0), 1.0F);
        this.c1.setValue(this.color.r);
        this.c2.setValue(this.color.g);
        this.c3.setValue(this.color.b);
        Tween.registerAccessor(Value.class, new ValueAccessor());
        this.manager = new TweenManager();
        this.randomGenarator = new Random();
        this.cb = new TweenCallback() {
            public void onEvent(int type, BaseTween<?> source) {
                ColorManager.this.random = Integer.valueOf(ColorManager.this.randomGenarator.nextInt(ColorManager.this.colors.size()));
                ColorManager.this.rtime = Integer.valueOf(ColorManager.this.randomGenarator.nextInt(5) + 2);
                ColorManager.this.target1 = ColorManager.parseColor((String)ColorManager.this.colors.get(ColorManager.this.random.intValue()), 1.0F).r;
                ((Tween)((Tween)((Tween)Tween.to(ColorManager.this.c1, -1, (float)ColorManager.this.rtime.intValue()).target(ColorManager.this.target1).repeatYoyo(0, 0.0F)).setCallback(ColorManager.this.cb)).setCallbackTriggers(8)).ease(TweenEquations.easeInOutSine).start(ColorManager.this.manager);
            }
        };
        this.cb1 = new TweenCallback() {
            public void onEvent(int type, BaseTween<?> source) {
                ColorManager.this.target2 = ColorManager.parseColor((String)ColorManager.this.colors.get(ColorManager.this.random.intValue()), 1.0F).g;
                ((Tween)((Tween)((Tween)Tween.to(ColorManager.this.c2, -1, (float)ColorManager.this.rtime.intValue() + 1.0E-5F).target(ColorManager.this.target2).repeatYoyo(0, 0.0F)).setCallback(ColorManager.this.cb1)).setCallbackTriggers(8)).ease(TweenEquations.easeInOutSine).start(ColorManager.this.manager);
            }
        };
        this.cb2 = new TweenCallback() {
            public void onEvent(int type, BaseTween<?> source) {
                ColorManager.this.target3 = ColorManager.parseColor((String)ColorManager.this.colors.get(ColorManager.this.random.intValue()), 1.0F).b;
                ((Tween)((Tween)((Tween)Tween.to(ColorManager.this.c3, -1, (float)ColorManager.this.rtime.intValue() + 2.0E-5F).target(ColorManager.this.target3).repeatYoyo(0, 0.0F)).setCallback(ColorManager.this.cb2)).setCallbackTriggers(8)).ease(TweenEquations.easeInOutSine).start(ColorManager.this.manager);
            }
        };
        ((Tween)((Tween)((Tween)Tween.to(this.c1, -1, 5.0F).target(this.color.r).repeatYoyo(0, 0.0F)).setCallback(this.cb)).setCallbackTriggers(8)).ease(TweenEquations.easeInOutSine).start(this.manager);
        ((Tween)((Tween)((Tween)Tween.to(this.c2, -1, 5.00001F).target(this.color.g).repeatYoyo(0, 0.0F)).setCallback(this.cb1)).setCallbackTriggers(8)).ease(TweenEquations.easeInOutSine).start(this.manager);
        ((Tween)((Tween)((Tween)Tween.to(this.c3, -1, 5.00002F).target(this.color.b).repeatYoyo(0, 0.0F)).setCallback(this.cb2)).setCallbackTriggers(8)).ease(TweenEquations.easeInOutSine).start(this.manager);
    }

    public Color getColor() {
        return this.color;
    }

    public void update(float delta) {
        this.manager.update(delta);
        this.color = new Color(this.c1.getValue(), this.c2.getValue(), this.c3.getValue(), 1.0F);
    }

    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if(hex.indexOf("#") != -1) {
            hex1 = hex.substring(1);
        }

        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }
}
