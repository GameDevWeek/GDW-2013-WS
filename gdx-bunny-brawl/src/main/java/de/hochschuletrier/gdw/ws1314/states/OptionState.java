
package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.OptionStage;
import de.hochschuletrier.gdw.ws1314.sound.LocalSound2;

public class OptionState extends GameState {
	
	private Logger logger;
	private OptionStage stage;
	
	private BackListener backListener;
	private MusicListener musicListener;
	private SoundListener soundListener;
	
	public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        logger = LoggerFactory.getLogger(OptionState.class);
        
        backListener = new BackListener();
        
        stage = new OptionStage();
        stage.init(assetManager);
		musicListener = new MusicListener(stage.getMusicSlider());
		soundListener = new SoundListener(stage.getSoundSlider());
    }

    public void render() {
    	stage.render();
    }

    public void update(float delta) {
    }

    public void onEnter() {
    	stage.init(assetManager);
    	Main.getInstance().addScreenListener(stage);
	    Main.inputMultiplexer.addProcessor(stage);
	    stage.getMusicSlider().addListener(musicListener);
		stage.getBackButton().addListener(backListener);
		stage.getSoundSlider().addListener(soundListener);
    }

    public void onEnterComplete() {
    }

    public void onLeave() {
    	Main.getInstance().removeScreenListener(stage);
    	stage.getMusicSlider().removeListener(musicListener);
    	stage.getBackButton().removeListener(backListener);
    	stage.getSoundSlider().removeListener(soundListener);
    	Main.inputMultiplexer.removeProcessor(stage);
		stage.clear();
    }

    public void onLeaveComplete() {
    }

    public void dispose() {
    }
    
    private class BackListener extends ClickListener {
    	public void clicked(InputEvent event, float x, float y) {
    		logger.info("Change state to MainMenuState");
    		GameStates.MAINMENU.activate();
    	}
    }
    
    private class MusicListener extends DragListener {
    	private Slider slider;
    	public MusicListener(Slider slider) {
    		this.slider = slider;
    	}
    	public void dragStop (InputEvent event, float x, float y, int pointer) {
    		LocalMusic.setSystemVolume(slider.getValue()*0.01f);
    	}
    }
    
    private class SoundListener extends DragListener {
    	private Slider slider;
    	public SoundListener(Slider slider) {
    		this.slider = slider;
    	}
    	public void dragStop (InputEvent event, float x, float y, int pointer) {
    		LocalSound2.setSystemVolume(slider.getValue()*0.01f);
    	}
    }
}




