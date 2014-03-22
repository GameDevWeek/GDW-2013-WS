package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.game.ClientGame;
import de.hochschuletrier.gdw.ws1314.game.ClientServerConnect;
import de.hochschuletrier.gdw.ws1314.game.ServerGame;
import de.hochschuletrier.gdw.ws1314.network.DisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.GameStateCallback;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;
import de.hochschuletrier.gdw.ws1314.sound.LocalSound;

/**
 * Menu state
 * 
 * @author Santo Pfingsten
 */
public class ClientGamePlayState extends GameState implements DisconnectCallback, GameStateCallback {
	private static final Logger logger = LoggerFactory.getLogger(ClientGamePlayState.class);
	
	private ClientGame clientGame;
	private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
	private LocalMusic stateMusic;
	private LocalSound stateSound;
	private String mapName;
	
	public ClientGamePlayState() {
	}
	
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
	}

	public void render() {
		DrawUtil.batch.setProjectionMatrix(DrawUtil.getCamera().combined);
		clientGame.render();
	}

	@Override
	public void update(float delta) {
		clientGame.update(delta);
		fpsCalc.addFrame();
		
		//TODO: @Eppi connect ui to gamelogic
		//debug healthbar till connected to gamelogic
	}

	@Override
	public void onEnter() {
		clientGame = new ClientGame();
		clientGame.init(assetManager, mapName);
		stateMusic = new LocalMusic(assetManager);
		stateSound = LocalSound.getInstance();
		stateSound.init(assetManager);
	}

	@Override
	public void onLeave() {
		clientGame = null;
	}

	@Override
	public void dispose() {
		//stage.dispose();
	}

	@Override
	public void callback(String msg) {
		logger.warn(msg);
		GameStates.MAINMENU.init(assetManager);
		GameStates.MAINMENU.activate();
	}

	@Override
	public void callback(GameStates gameStates) {
		if (gameStates != GameStates.CLIENTGAMEPLAY) {
			gameStates.init(assetManager);
			gameStates.activate();
		}
	}
}
