package dev.team.screen.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import dev.team.screen.ScreenManager;

public class Assets {
    private static final Assets ourInstance = new Assets();

    public static Assets getInstance() {
        return ourInstance;
    }

    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private Assets() {
        assetManager = new AssetManager();
    }

    public void loadAssets(ScreenManager.ScreenType type) {
        switch (type) {
            case GAME:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/shoot.mp3", Sound.class);
                assetManager.load("audio/mortal.mp3", Music.class);
                assetManager.load("audio/fight.mp3", Music.class);
                assetManager.load("audio/hahaha.mp3", Music.class);
                assetManager.load("audio/general_music.mp3", Music.class);
                assetManager.load("audio/mk2021.mp3", Music.class);
                assetManager.load("audio/boom.mp3", Music.class);


                createStandardFont(32);
                createStandardFont(24);
                createStandardFont(20);
                createStandardFont(72);
                break;

            case MENU:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/music.mp3", Music.class);
                assetManager.load("audio/cosmos_music.mp3", Music.class);

                createStandardFont(72);
                createStandardFont(24);
                break;

            case GAMEOVER:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/game_over.mp3", Music.class);
                createStandardFont(72);
                createStandardFont(48);
                createStandardFont(24);
                break;

        }
    }

    private void createStandardFont(int size) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "fonts/Roboto-Medium.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.WHITE;
        fontParameter.fontParameters.shadowOffsetX = 1;
        fontParameter.fontParameters.shadowOffsetY = 1;
        fontParameter.fontParameters.shadowColor = Color.DARK_GRAY;
        assetManager.load("fonts/font" + size + ".ttf", BitmapFont.class, fontParameter);
    }

    public void makeLinks() {
        textureAtlas = assetManager.get("images/game.pack", TextureAtlas.class);
    }

    public void clear() {
        assetManager.clear();
    }
}