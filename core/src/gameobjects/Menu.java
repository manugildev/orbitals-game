package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

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
import ui.Banner;
import ui.BannerGameOver;
import ui.Button;
import ui.SimpleButton;

/**
 * Created by ManuGil on 13/02/15.
 */
public class Menu {

    private final BannerGameOver scoreBanner, highScoreBanner, gamesPlayedBanner;
    private final float buttonSize = 600;
    public final Button backToMenuButton;
    private GameWorld world;
    private Sprite backSprite;
    private TweenManager manager;
    private Banner banner;
    private Orbit orbit;
    private Hero hero;
    private Value alphaValue = new Value();

    private SimpleButton playButton, shareButton, achievementsButton, playButton1, shareButton1, removeAds1, removeAds;
    private Array<SimpleButton> menuButtons = new Array<SimpleButton>();

    private final float w;

    public Menu(GameWorld world) {

        this.world = world;
        this.w = world.w;
        backSprite = new Sprite(AssetLoader.square);
        backSprite.setPosition(0, 0);
        backSprite.setSize(world.gameWidth, world.gameHeight);
        backSprite.setColor(Color.BLACK);

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        alphaValue.setValue(0.0f);

        banner = new Banner(world, Configuration.GAME_NAME, 72 * world.w,
                world.parseColor(Configuration.COLOR_GAME_NAME_BANNER, 1f));

        playButton = new SimpleButton(world, (world.gameWidth - buttonSize) / 2, 36 * world.w,
                buttonSize, 120,
                AssetLoader.buttonB, AssetLoader.playButtonUp, Configuration.COLOR_PLAY_BUTTON,
                0f, 0f, "Play");

        achievementsButton = new SimpleButton(world, (world.gameWidth - buttonSize) / 2,
                28 * world.w,
                buttonSize, 120,
                AssetLoader.buttonB, AssetLoader.achieveButtonUp,
                Configuration.COLOR_LEADERBOARD_BUTTON, 0f, 0f, "Leaderboards");

        shareButton = new SimpleButton(world, (world.gameWidth - buttonSize) / 2, 20 * world.w,
                buttonSize, 120,
                AssetLoader.buttonB, AssetLoader.shareButtonUp, Configuration.COLOR_SHARE_BUTTON,
                0f, 0f, "Share");

        removeAds = new SimpleButton(world, (world.gameWidth - buttonSize) / 2, 12 * world.w,
                buttonSize, 120,
                AssetLoader.buttonB, AssetLoader.shareButtonUp, Configuration.COLOR_REMOVE_ADS,
                0f, 0f, "Remove Ads");

        /*learderboardButton = new SimpleButton(world, 400, world.gameHeight / 2 - 450,
                world.gameWidth - 800, 150,
                AssetLoader.square, AssetLoader.rankButtonUp,
                Configuration.COLOR_LEADERBOARD_BUTTON, .1f, "Leaderboard");*/

        //BUTTONS ON GAMEOVER
        /*playButton1 = new SimpleButton(world, world.gameWidth / 2 - 75 - 200,
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 200 - 250 - 25, 150, 150,
                AssetLoader.square, AssetLoader.playButtonUp, Configuration.COLOR_PLAY_BUTTON, .1f,
                "Play");

        restartButton = new SimpleButton(world, world.gameWidth / 2 - 75,
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 200 - 250 - 25,
                150, 150,
                AssetLoader.square, AssetLoader.rateButtonUp,
                Configuration.COLOR_RETURN_HOME_BUTTON, .05f, "Menu");

        shareButton1 = new SimpleButton(world, world.gameWidth / 2 + 50 + 75,
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 200 - 250 - 25,
                150, 150,
                AssetLoader.square, AssetLoader.shareButtonUp, Configuration.COLOR_SHARE_BUTTON,
                .0f, "Share");*/

        playButton1 = new SimpleButton(world, (world.gameWidth - buttonSize) / 2, 28 * world.w,
                buttonSize, 120,
                AssetLoader.buttonB, AssetLoader.playButtonUp, Configuration.COLOR_PLAY_BUTTON,
                0f, 0f, "Play");


        shareButton1 = new SimpleButton(world, (world.gameWidth - buttonSize) / 2, 20 * world.w,
                buttonSize, 120,
                AssetLoader.buttonB, AssetLoader.shareButtonUp, Configuration.COLOR_SHARE_BUTTON,
                0f, 0f, "Share");

        removeAds1 = new SimpleButton(world, (world.gameWidth - buttonSize) / 2, 12 * world.w,
                buttonSize, 120,
                AssetLoader.buttonB, AssetLoader.shareButtonUp, Configuration.COLOR_REMOVE_ADS,
                0f, 0f, "Remove Ads");

        menuButtons.add(playButton);
        menuButtons.add(achievementsButton);
        menuButtons.add(shareButton);
        menuButtons.add(removeAds);
        menuButtons.add(playButton1);
        menuButtons.add(shareButton1);
        menuButtons.add(removeAds1);


        scoreBanner = new BannerGameOver(world, world.getScore() + "",
                world.gameHeight / 2 + 95 - (85 / 2) + 150 - 25 + (2*w),
                world.parseColor(
                        Configuration.COLOR_SCORE_BANNER, 1f));
        highScoreBanner = new BannerGameOver(world, AssetLoader.getHighScore() + "",
                world.gameHeight / 2 - (85 / 2) + 150 - 25 + (2*w)-25,
                world.parseColor(Configuration.COLOR_HIGH_SCORE_BANNER, 1f));
        gamesPlayedBanner = new BannerGameOver(world, AssetLoader.getGamesPlayed() + "",
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 25 + (2*w)-50,
                world.parseColor(Configuration.COLOR_GAMES_PLAYED_BANNER, 1f));


        orbit = new Orbit(world, world.worldWidth / 2, w * 57, 120);
        hero = new Hero(world, orbit, 20);

        backToMenuButton = new Button(world, 50, w * 90,
                100, 100,
                AssetLoader.button, AssetLoader.backButton, Configuration.BACK_TO_MENU_BUTTON,
                .05f);


    }

    public void update(float delta) {
        manager.update(delta);
        //backSprite.setColor(world.colorManager.getColor());
        backToMenuButton.update(delta);
        orbit.update(delta);
        hero.update(delta);
        backSprite.setAlpha(alphaValue.getValue());
        banner.update(delta);
        for (int i = 0; i < menuButtons.size; i++) {
            menuButtons.get(i).update(delta);
        }
        scoreBanner.update(delta);
        highScoreBanner.update(delta);
        gamesPlayedBanner.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontshader) {
        //backSprite.draw(batch);
        banner.render(batch, shapeRenderer, fontshader);
        for (int i = 0; i < menuButtons.size; i++) {
            menuButtons.get(i).draw(batch, fontshader);
        }
        scoreBanner.render(batch, shapeRenderer, fontshader);
        highScoreBanner.render(batch, shapeRenderer, fontshader);
        gamesPlayedBanner.render(batch, shapeRenderer, fontshader);
        orbit.render(batch, shapeRenderer);
        hero.render(batch, shapeRenderer);
        backToMenuButton.draw(batch);
    }

    public void finish() {
        TweenCallback cbFinish = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                //world.gameState = GameState.RUNNING;
                world.renewMap();
            }
        };
        Tween.to(alphaValue, -1, .8f).target(0).ease(TweenEquations.easeInOutSine)
                .setCallback(cbFinish).delay(0f).setCallbackTriggers(
                TweenCallback.COMPLETE).start(manager);
        banner.dissapear();
        orbit.finish();
        hero.finish();
        for (int i = 0; i < menuButtons.size - 3; i++) {
            menuButtons.get(i).finish();
        }
    }

    public void start() {
        alphaValue.setValue(0);
        banner.appear();

        TweenCallback cbstart = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.MENU;
            }
        };
        Tween.to(alphaValue, -1, .9f).target(0.7f).ease(TweenEquations.easeInOutSine)
                .setCallback(cbstart).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);

        orbit.start();
        hero.startMenu();
        world.gameState = GameState.MENU;

        for (int i = 0; i < menuButtons.size - 3; i++) {
            menuButtons.get(i).start();
        }
    }

    public void startGameOver() {
        alphaValue.setValue(0);
        banner.appear();
        TweenCallback cbstart = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.MENU;
                if (Math.random() < Configuration.AD_FREQUENCY) {
                    if(!AssetLoader.getAds()){
                        world.actionResolver.showOrLoadInterstital();
                    }
                }
            }
        };
        Tween.to(alphaValue, -1, .8f).target(0.7f).ease(TweenEquations.easeInOutSine)
                .setCallback(cbstart).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);

        world.gameState = GameState.MENU;
        scoreBanner.setText("Score: " + world.getScore() + "");
        highScoreBanner.setText("High Score: " + AssetLoader.getHighScore() + "");
        gamesPlayedBanner.setText("Games Played: " + AssetLoader.getGamesPlayed() + "");
        scoreBanner.appear();
        highScoreBanner.appear();
        gamesPlayedBanner.appear();
        backToMenuButton.start();

        playButton1.start();
        shareButton1.start();
        removeAds1.start();


    }

    public void finishGameOver() {
        TweenCallback cbFinish = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                //world.gameState = GameState.RUNNING;
                world.renewMap();
            }
        };
        Tween.to(alphaValue, -1, .9f).target(0).ease(TweenEquations.easeInOutSine)
                .setCallback(cbFinish).delay(0f).setCallbackTriggers(
                TweenCallback.COMPLETE).start(manager);
        banner.dissapear();
        scoreBanner.dissapear();
        highScoreBanner.dissapear();
        gamesPlayedBanner.dissapear();
        backToMenuButton.finish();

        removeAds1.finish();
        shareButton1.finish();
        playButton1.finish();
    }

    public void finishGameOverStartMenu() {
        playButton1.finish();
        playButton.start();
        removeAds.start();
        removeAds1.finish();
        shareButton.start();
        shareButton1.finish();
        achievementsButton.start();
        orbit.startMenu();
        scoreBanner.dissapear();
        gamesPlayedBanner.dissapear();
        highScoreBanner.dissapear();
        backToMenuButton.finish();
    }


    public Array<SimpleButton> getMenuButtons() {
        return menuButtons;
    }


}