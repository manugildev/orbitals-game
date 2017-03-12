package helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

    public static AssetManager manager;

    public static void load() {
        manager = new AssetManager();
        Texture.setAssetManager(manager);

        //TEXTURE FILTER
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.genMipMaps = true;

        //param = null;

        //Textures
        manager.load("back_to_menu.png", Texture.class, param);
        manager.load("background.png", Texture.class, param);
        manager.load("bonus.png", Texture.class, param);
        manager.load("button.png", Texture.class, param);
        manager.load("button_border.png", Texture.class, param);
        manager.load("button_border_1.png", Texture.class, param);
        manager.load("buttons.png", Texture.class, param);
        manager.load("dot.png", Texture.class, param);
        manager.load("dot_effect.png", Texture.class, param);
        manager.load("hero.png", Texture.class, param);
        manager.load("logo.png", Texture.class, param);
        manager.load("orbit.png", Texture.class, param);
        manager.load("particle.png", Texture.class, param);
        manager.load("pause.png", Texture.class, param);
        manager.load("score_banner.png", Texture.class, param);
        manager.load("square.png", Texture.class, param);
        manager.load("squareAura.png", Texture.class, param);
        manager.load("star.png", Texture.class, param);
        manager.load("timer_bar.png", Texture.class, param);
        manager.load("title_banner.png", Texture.class, param);
        manager.load("title_banner_gameover.png", Texture.class, param);
        //Font

        //Sounds
        manager.load("sound.wav", Sound.class);


    }

    public static void dispose() {
        manager.dispose();
    }
}
