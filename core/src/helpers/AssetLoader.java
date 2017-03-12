package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import configuration.Configuration;

public class AssetLoader {

    public static Texture logoTexture;
    public static TextureRegion logo;
    public static TextureRegion square;
    public static BitmapFont font, font1, font2;
    private static Preferences prefs;
    public static Sound sound;
    public static Texture bonus, buttonsT, backgroundT, starT, orbitT, buttonBT, titleT, titleGameoverT,
            buttonT, dotEffectT, scoreBannerT, timerBarT;

    public static Array<Sound> sounds = new Array<Sound>();
    public static TextureRegion playButtonUp, rankButtonUp, shareButtonUp, achieveButtonUp,
            rateButtonUp, pauseButton, background, star, orbit, buttonB, title, titleGameover,
            scoreBanner, button, dotEffect, backButton, timerBar;
    public static TextureRegion enemieshape, dot;
    private static Texture enemieShapeT, dotT;

    public static void load() {
        logoTexture = getAssetTexture("logo.png");
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        logo = new TextureRegion(logoTexture, 0, 0, logoTexture.getWidth(),
                                 logoTexture.getHeight());
        logo.flip(false, false);

        square = new TextureRegion(new Texture(Gdx.files.internal("square.png")), 0, 0, 10, 10);

        dotT = getAssetTexture("dot.png");
        dotT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        dot = new TextureRegion(dotT, 0, 0, dotT.getWidth(), dotT.getHeight());

        dotEffectT = getAssetTexture("dot_effect.png");
        dotEffect = new TextureRegion(dotEffectT, 0, 0, dotEffectT.getWidth(),
                                      dotEffectT.getHeight());

        backgroundT = getAssetTexture("background.png");
        background = new TextureRegion(backgroundT, 0, 0, backgroundT.getWidth(),
                                       backgroundT.getHeight());

        titleGameoverT = getAssetTexture("title_banner_gameover.png");
        titleGameoverT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        titleGameover = new TextureRegion(titleGameoverT, 0, 0, titleGameoverT.getWidth(),
                                          titleGameoverT.getHeight());

        buttonT = getAssetTexture("button.png");
        buttonT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        button = new TextureRegion(buttonT, 0, 0, buttonT.getWidth(),
                                   buttonT.getHeight());

        scoreBannerT = getAssetTexture("score_banner.png");
        scoreBanner = new TextureRegion(scoreBannerT, 0, 0, scoreBannerT.getWidth(),
                                        scoreBannerT.getHeight());

        timerBarT = getAssetTexture("timer_bar.png");
        timerBarT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        timerBar = new TextureRegion(timerBarT, 0, 0, timerBarT.getWidth(),
                                     timerBarT.getHeight());

        titleT = getAssetTexture("title_banner.png");
        titleT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        title = new TextureRegion(titleT, 0, 0, titleT.getWidth(),
                                  titleT.getHeight());

        starT = getAssetTexture("star.png");
        star = new TextureRegion(starT, 0, 0, starT.getWidth(),
                                 starT.getHeight());

        buttonBT = getAssetTexture("button_border_1.png");

        buttonBT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        buttonB = new TextureRegion(buttonBT, 0, 0, buttonBT.getWidth(),
                                    buttonBT.getHeight());

        orbitT = getAssetTexture("orbit.png");
        orbitT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        orbit = new TextureRegion(orbitT, 0, 0, orbitT.getWidth(),
                                  orbitT.getHeight());

        pauseButton = new TextureRegion(getAssetTexture("pause.png"), 0, 0, 240,
                                        240);

        backButton = new TextureRegion(getAssetTexture("back_to_menu.png"), 0, 0,
                                       240,
                                       240);
        buttonsT = getAssetTexture("buttons.png");
        buttonsT.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        bonus = getAssetTexture("bonus.png");
        bonus.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Texture tfont = new Texture(Gdx.files.internal("font.png"), true);
        tfont.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);

        // FONT
        font = new BitmapFont(Gdx.files.internal("font.fnt"),
                              new TextureRegion(tfont), true);
        font.getData().setScale(2f, -2f);
        font.setColor(parseColor(Configuration.COLOR_FONT, 1f));

        font1 = new BitmapFont(Gdx.files.internal("font.fnt"),
                               new TextureRegion(tfont), true);
        font1.getData().setScale(1.5f, -1.5f);
        font1.setColor(Color.WHITE);

        font2 = new BitmapFont(Gdx.files.internal("font.fnt"),
                               new TextureRegion(tfont), true);
        font2.getData().setScale(1.3f, -1.3f);
        font2.setColor(Color.WHITE);

        // MENU BG TEXTURE

        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        if (!prefs.contains("games")) {
            prefs.putInteger("games", 0);
        }

        sound = getAssetSound("sound.wav");
        sounds.add(sound);

        //CROP BUTTONS
        playButtonUp = new TextureRegion(buttonsT, 0, 0, 240, 240);
        rankButtonUp = new TextureRegion(buttonsT, 240, 0, 240, 240);
        shareButtonUp = new TextureRegion(buttonsT, 720, 0, 240, 240);
        achieveButtonUp = new TextureRegion(buttonsT, 960, 0, 240, 240);
        rateButtonUp = new TextureRegion(buttonsT, 480, 0, 240, 240);


    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void addGamesPlayed() {
        prefs.putInteger("games", prefs.getInteger("games") + 1);
        prefs.flush();
    }

    public static int getGamesPlayed() {
        return prefs.getInteger("games");
    }

    public static void dispose() {
        font.dispose();
        font1.dispose();
        font2.dispose();
        bonus.dispose();
        logoTexture.dispose();
        sound.dispose();
        buttonsT.dispose();
        orbitT.dispose();
        dotEffectT.dispose();
        dotT.dispose();
        buttonsT.dispose();
        buttonT.dispose();
        titleGameoverT.dispose();
        titleT.dispose();
    }

    public static int getBonusNumber() {
        return prefs.getInteger("bonus");
    }

    public static void addBonusNumber(int number) {
        prefs.putInteger("bonus", prefs.getInteger("bonus") + number);
        prefs.flush();
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

    public static void setAds(boolean removeAdsVersion) {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
        prefs.putBoolean("ads", removeAdsVersion);
        prefs.flush();
    }

    public static boolean getAds() {
        Gdx.app.log("ADS", prefs.getBoolean("ads") + "");
        return prefs.getBoolean("ads", false);
    }

    public static Texture getAssetTexture(String fileName) {
        return Assets.manager.get(fileName, Texture.class);
    }

    public static Sound getAssetSound(String fileName) {
        return Assets.manager.get(fileName, Sound.class);
    }
}
