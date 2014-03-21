package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.MainMenuStage;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends GameState implements InputProcessor {
	private static final Logger logger = LoggerFactory.getLogger(MainMenuState.class);
	
    InputInterceptor inputProcessor;
    private LocalMusic music;
	private int stateChangeDuration=500;
	private MainMenuStage stage;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        this.music = new LocalMusic(assetManager);
        inputProcessor = new InputInterceptor(this) {
            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:                    	
                    	//Wird aktuelle nicht benutzt (laut Jerry)
                        return true;
                }
                return isActive && mainProcessor.keyUp(keycode);
            }
        };
        Main.inputMultiplexer.addProcessor(inputProcessor);
        
		stage = new MainMenuStage();
		stage.init(assetManager);
		
		stage.getStartServerButton().addListener(new StartServerClick());
		stage.getStartClientButton().addListener(new StartClientClick());

		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
		stage.render();
	}

	float stateTime = 0f;

	@Override
	public void update(float delta) {
		stateTime += delta;
		music.update(stateChangeDuration);
    }

    @Override
    public void onEnter() {
		inputProcessor.setActive(true);
        
        if (this.music.isMusicPlaying())
			//this.music.deMute();
			this.music.setFade('i', 5000);
        else
        this.music.play("music-lobby-loop");
    }

    @Override
    public void onLeave() {
		//this.music.mute();
		this.music.setFade('o', this.stateChangeDuration);
        inputProcessor.setActive(false);
	}

	@Override
	public void onLeaveComplete() {
        
    }

    @Override
    public void dispose() {
		stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    private class StartServerClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Changing State to Server-Lobby...");
			GameStates.SERVERLOBBY.init(assetManager);
			GameStates.SERVERLOBBY.activate();
		}
    }
    
    private class StartClientClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Changing State to Client-Lobby...");
			GameStates.CLIENTLOBBY.init(assetManager);
			GameStates.CLIENTLOBBY.activate();
		}
    }
}
