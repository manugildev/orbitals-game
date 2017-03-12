package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Tweens.SpriteAccessor;
import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import helpers.AssetLoader;
import helpers.Assets;
import helpers.FlatColors;
import orbital.ActionResolver;
import orbital.MainGame;

public class LoadingScreen implements Screen {

    private Sprite background, logo, progressBar, top;
    private MainGame game;
    private SpriteBatch batch = new SpriteBatch();
    private TweenManager manager;
    private boolean goingToMenu = false, finishedTween = false;
    private ActionResolver actionResolver;

    public LoadingScreen(MainGame game, ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
        this.game = game;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float desiredWidth = width * 0.5f;

        background = new Sprite(new Texture("square.png"));
        background.setPosition(0, 0);
        background.setColor(0.0f, 0.106f, 0.13f, 1);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        top = new Sprite(background.getTexture());
        top.setPosition(0, 0);
        top.setColor(0.0f, 0.106f, 0.13f, 1);
        top.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        top.setAlpha(0);

        logo = new Sprite(new Texture("logo.png"));
        float scale = desiredWidth / logo.getWidth();
        logo.setSize(logo.getWidth() * scale, logo.getHeight() * scale);
        logo.setPosition(Gdx.graphics.getWidth() / 2 - (logo.getWidth() / 2),
                         Gdx.graphics.getHeight() / 2 - (logo.getHeight() / 2));

        progressBar = new Sprite(background.getTexture());
        progressBar.setPosition(0, 0);
        progressBar.setSize(0, 20);
        progressBar.setColor(FlatColors.LOGO_GREEN);
        setupTween();
    }

    private void setupTween() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        logo.setAlpha(0);
        Tween.to(logo, SpriteAccessor.ALPHA, .9f).target(1).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                finishedTween = true;
            }
        }).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        manager.update(delta);
        batch.begin();
        background.draw(batch);
        progressBar.setSize(background.getWidth() * Assets.manager.getProgress(),
                            progressBar.getHeight());
        progressBar.draw(batch);
        logo.draw(batch);
        top.draw(batch);
        batch.end();

        if (finishedTween) {
            if (Assets.manager.update() && !goingToMenu) {
                goToMenu();
            }
        }
    }

    private void goToMenu() {
        goingToMenu = true;
        Value timer = new Value();
        Tween.to(logo, SpriteAccessor.ALPHA, .9f).target(0).start(manager);
        Tween.to(progressBar, SpriteAccessor.ALPHA, .9f).target(0).start(manager);
        Tween.to(top, SpriteAccessor.ALPHA, .5f).target(1).delay(.4f).start(manager);
        Tween.to(timer, -1, 1f).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                AssetLoader.load();
                game.setScreen(new GameScreen(game, actionResolver));
            }
        }).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}