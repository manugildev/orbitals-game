package gameworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import gameobjects.Background;
import gameobjects.Hero;
import gameobjects.Menu;
import gameobjects.Orbit;
import gameobjects.Star;
import helpers.AssetLoader;
import helpers.ColorManager;
import helpers.Rumble;
import orbital.ActionResolver;
import orbital.MainGame;
import ui.Banner;
import ui.Button;
import ui.ScoreBanner;
import ui.TimerBar;

public class GameWorld {

    public final float w;
    //GENERAL VARIABLES
    public float gameWidth;
    public float gameHeight;
    public float worldWidth;
    public float worldHeight;

    public ActionResolver actionResolver;
    public MainGame game;
    public GameWorld world = this;


    //GAME OBJECTS
    //private Hero hero;
    private Menu menu;
    private GameCam camera;
    public GameState gameState;
    public ColorManager colorManager;
    private Orbit orbit, orbit1, orbit2, orbit3, orbit4, orbit5, orbit6;
    private Hero hero;
    private Background background;
    private TimerBar timerBar;

    private Button pauseButton;
    private final int numberOfStars;
    private final int numberOfEnemies;
    private Array<Star> stars = new Array<Star>();
    private Array<Orbit> orbits = new Array<Orbit>();
    private Array<Vector2> points = new Array<Vector2>();

    public Banner banner;

    private int score;

    //TWEENS
    private TweenManager manager;
    public Rumble rumble;
    private Rectangle bounds;
    private float distance;
    private Orbit currentOrbit;
    private float iORadius = 120;
    private int numOfOrbits = 6;


    private final ScoreBanner scoreBanner;

    public GameWorld(MainGame game, ActionResolver actionResolver, float gameWidth,
                     float gameHeight, float worldWidth, float worldHeight) {

        this.gameWidth = gameWidth;
        this.w = gameHeight / 100;
        this.gameHeight = gameHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.game = game;
        this.actionResolver = actionResolver;
        this.numberOfStars = 110;
        this.numberOfEnemies = 0;


        colorManager = new ColorManager();
        camera = new GameCam(world, worldWidth / 2, worldHeight / 2, worldWidth, worldHeight);

        gameState = GameState.MENU;

        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new Star(world));
        }

        createPoints(0);


        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();

        this.rumble = new Rumble(this);
        banner = new Banner(this, "Pause", world.gameHeight / 2 - 100, parseColor("#34495e", 1f));
        pauseButton = new Button(this, 50, world.gameHeight - 150,
                100, 100,
                AssetLoader.square, AssetLoader.pauseButton, Configuration.COLOR_PAUSE_BUTTON,
                .05f);

        bounds = new Rectangle(-50, -50, worldWidth + 100, worldHeight + 100);

        orbit = new Orbit(this, world.worldWidth / 2, w * 25, iORadius);
        orbit1 = new Orbit(this, world.worldWidth / 2 + 300, w * 60, iORadius);
        orbit2 = new Orbit(this, world.worldWidth / 2 - 200, w * 90, iORadius);
        orbit3 = new Orbit(this, MathUtils.random(iORadius + 30, world.gameWidth - iORadius - 30),
                w * 120, iORadius);
        orbit4 = new Orbit(this, MathUtils.random(iORadius + 30, world.gameWidth - iORadius - 30),
                w * 150, iORadius);
        orbit5 = new Orbit(this, MathUtils.random(iORadius + 30, world.gameWidth - iORadius - 30),
                w * 180, iORadius);

        orbit6 = new Orbit(this, MathUtils.random(iORadius + 30, world.gameWidth - iORadius - 30),
                w * 210, iORadius);

        currentOrbit = orbit;
        orbits.add(orbit);
        orbits.add(orbit1);
        orbits.add(orbit2);
        orbits.add(orbit3);
        orbits.add(orbit4);

        orbits.add(orbit5);
        orbits.add(orbit6);
        hero = new Hero(this, orbit, 20);
        background = new Background(this);
        menu = new Menu(this);
        menu.start();

        pauseButton = new Button(this, 50, w * 85,
                100, 100,
                AssetLoader.button, AssetLoader.pauseButton, Configuration.COLOR_PAUSE_BUTTON,
                .05f);
        scoreBanner = new ScoreBanner(this, score + "", w * 85,
                parseColor(Configuration.COLOR_BANNER_WITH_SCORE, 1f));

        timerBar = new TimerBar(this, "", w * 85, Color.WHITE);

        if (AssetLoader.getAds()) {
            world.actionResolver.viewAd(false);
        } else {
            world.actionResolver.viewAd(true);
        }
    }


    public void update(float delta) {
        manager.update(delta);
        background.update(delta);
        colorManager.update(delta);
        menu.update(delta);
        banner.update(delta);
        pauseButton.update(delta);
        scoreBanner.update(delta);
        if (!isPaused()) {
            for (int i = 0; i < numberOfStars; i++) {
                stars.get(i).update(delta);
            }
            for (int i = 0; i < orbits.size; i++) {
                orbits.get(i).update(delta);
            }
            hero.update(delta);
            timerBar.update(delta);
        }

        if (hero.getHeroState() == Hero.HeroState.FLYING) {
            camera.setPoint(hero.getPosition());
        }

        if (isRunning()) {
            collisions();
        } else if (isMenu()) {
        } else if (isGameOver()) {
        } else if (isPaused()) {
        }
    }

    private void collisions() {

        for (int i = 0; i < orbits.size; i++) {
            if (hero.getHeroState() != Hero.HeroState.ORBIT) {
                if (Intersector.overlaps(hero.circle, orbits.get(i).circle)) {
                    AssetLoader.sound.play();
                    distance = w * 25 - orbits.get(i).circle.y;
                    collisionBetweenOrbitAndHero(orbits.get(i));
                    score++;
                    scoreBanner.setText(score + "");
                    orbits.get(i).effect();
                    timerBar.reset();
                    for (int j = 0; j < orbits.size; j++) {
                        if (i != j) {
                            //orbits.get(j).finish();
                        }
                    }

                }
            }
        }

        for (int i = 0; i < orbits.size; i++) {
            if (orbits.get(i).getPosition().y < -orbits.get(i).circle.radius * 4) {
                if (i == 0) {
                    orbits.get(i).reset(orbits.get(orbits.size - 1).getPosition().y);
                } /*else if (i == 1) {
                    orbits.get(i).reset(orbits.get(0).getPosition().y);
                } else if (i == 2) {
                    orbits.get(i).reset(orbits.get(1).getPosition().y);
                } else if (i == 3) {
                    orbits.get(i).reset(orbits.get(2).getPosition().y);
                } else if (i == 4) {
                    orbits.get(i).reset(orbits.get(3).getPosition().y);
                } else if (i == 6) {
                    orbits.get(i).reset(orbits.get(4).getPosition().y);
                } */ else {
                    orbits.get(i).reset(orbits.get(i - 1).getPosition().y);
                }
            }
        }


    }

    private void collisionBetweenOrbitAndHero(Orbit or) {

        currentOrbit = or;
        or.startTimer();
        camera.setPoint(or.getPosition());
        hero.setCurrentOrbit(or);
        hero.getAngle2Vecs(hero.getPosition(), or.getPosition());
        hero.setAngle();
        hero.setHeroState(Hero.HeroState.ORBIT);

        for (int i = 0; i < orbits.size; i++) {
            orbits.get(i).scrollTo(distance);
        }
    }

    public void finish() {

        pauseButton.finish();
        menu.startGameOver();
        scoreBanner.dissapear();
        saveScoresLogic();
        hero.finish();
        timerBar.dissapear();

        for (int i = 0; i < orbits.size; i++) {
            orbits.get(i).finish();
        }
    }

    private void saveScoresLogic() {
        AssetLoader.addGamesPlayed();
        int gamesPlayed = AssetLoader.getGamesPlayed();
        // GAMES PLAYED ACHIEVEMENTS!
        actionResolver.submitScore(score);
        actionResolver.submitGamesPlayed(gamesPlayed);

        if (score > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(score);
        }
        //checkAchievements();
    }

    private void checkAchievements() {
        if (actionResolver.isSignedIn()) {

        }
    }


    public void pause() {
        banner.appear();
        gameState = GameState.PAUSE;
    }


    public ColorManager getColorManager() {
        return colorManager;
    }

    public boolean isRunning() {
        return gameState == GameState.RUNNING;
    }

    public void renewMap() {
        //menu.finishGameOver(10);
        renewOrbits();
        currentOrbit = orbit;
        pauseButton.start();
        hero.reset();
        hero.setCurrentOrbit(currentOrbit);
        hero.start();
        for (int i = 0; i < orbits.size; i++) {
            orbits.get(i).start();
        }
        score = 0;
        scoreBanner.appear();
        timerBar.appear();

        if (AssetLoader.getAds()) {
            world.actionResolver.viewAd(false);
        } else {
            world.actionResolver.viewAd(true);
        }

    }

    private void renewOrbits() {
        orbit = new Orbit(this, world.worldWidth / 2, w * 25, iORadius);
        orbits.clear();
        orbits.add(orbit);

        for (int i = 0; i < numOfOrbits - 1; i++) {
            orbit1 = new Orbit(this,
                    MathUtils.random(iORadius + 30, world.gameWidth - iORadius - 30),
                    w * (60 + i * 30), iORadius);
            orbits.add(orbit1);
        }

    }

    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
            // Gdx.app.log("Hex", hex1);
        }
        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }

    public void createPoints(int numOfS) {
        /*for (int i = 0; i < numOfS; i++) {
            float margin = 0;
            float distance = ((world.gameWidth - (margin * 2)) / (numOfS - 1));
            points.add(new Vector2(margin + (distance * i), (worldHeight)));
            points.add(new Vector2(margin + (distance * i), (0)));
        }

        for (int i = 0; i < numOfS; i++) {
            float margin = 0;
            float distance = ((world.gameHeight - (margin * 2)) / (numOfS - 1));
            points.add(new Vector2((worldWidth), margin + (distance * i)));
            points.add(new Vector2((0), margin + (distance * i)));
        }*/

        for (int i = 0; i < numOfS; i++) {
            float angle = MathUtils.random(0, 360);
            float dist = MathUtils.random(0, worldHeight / 2);
            float x = MathUtils.cos(angle) * dist + worldWidth / 2;
            float y = MathUtils.sin(angle) * dist + worldHeight / 2;
            points.add(new Vector2(x, y));
        }
    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public Array<Star> getStars() {
        return stars;
    }

    public Array<Orbit> getBalls() {
        return orbits;
    }

    public void addScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public boolean isPaused() {
        return gameState == GameState.PAUSE;
    }


    public GameCam getCamera() {
        return camera;
    }

    public Banner getBanner() {
        return banner;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAMEOVER;
    }

    public boolean isMenu() {
        return gameState == GameState.MENU;
    }

    public Menu getMenu() {
        return menu;
    }


    public boolean isTransition() {
        return gameState == GameState.TRANSITION;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public void setPauseMode() {
        if (gameState == GameState.RUNNING) {
            gameState = GameState.PAUSE;
            banner.appearPause();
        }
    }

    public Vector2 getRandomPoint() {
        return points.get((int) Math.floor(Math.random() * points.size));
    }

    public Rectangle getRectangle() {
        return bounds;
    }

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer, ShaderProgram fontShader) {

        background.render(batcher, shapeRenderer);
        pauseButton.draw(batcher);
        for (int i = 0; i < orbits.size; i++) {
            orbits.get(i).render(batcher, shapeRenderer);
        }
        scoreBanner.render(batcher, shapeRenderer, fontShader);

        hero.render(batcher, shapeRenderer);
        banner.render(batcher, shapeRenderer, fontShader);
        timerBar.render(batcher, shapeRenderer, fontShader);

    }

    public Hero getHero() {
        return hero;
    }
}

