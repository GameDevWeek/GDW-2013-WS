package de.hochschuletrier.gdw.ws1314.states;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.hud.ClientLobbyStage;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.DisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.GameStateCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;

public class ClientLobbyState extends GameState implements GameStateCallback,
		DisconnectCallback {

	private static final Logger logger = LoggerFactory.getLogger(ClientLobbyState.class);

	protected ClientLobbyManager clientLobby;

	private ClientLobbyStage stage;

	// Console Commands
	private ConsoleCmd sendPlayerUpdate;
	private ConsoleCmd cpAccept;
	private ConsoleCmd cpTeam;
	private ConsoleCmd cpClass;

	private boolean isInitialized;

	@Override
	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
	}

	@Override
	public void render() {
		if (isInitialized) {
			this.stage.render();
		}
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void dispose() {
	}

	// GameStateCallback
	@Override
	public void gameStateCallback(GameStates gameStates) {
		logger.info("GameStateChange received");
		if (gameStates == GameStates.CLIENTGAMEPLAY) {

			((ClientGamePlayState) gameStates.get())
					.setMapName(this.clientLobby.getMap());
			ClientEntityManager.getInstance().setPlayerData(
					this.clientLobby.getPlayerData());
			gameStates.init(assetManager);
			gameStates.activate();
			logger.info("ClientGamePlayState activated.");
		}
	}

	@Override
	public void onEnter() {
		super.onEnter();

		this.clientLobby = new ClientLobbyManager(
				Main.getInstance().gamePreferences.getString(PreferenceKeys.playerName,
						"Player"));

		this.stage = new ClientLobbyStage(this.clientLobby);
		this.stage.init(assetManager);
		Main.getInstance().addScreenListener(stage);
		Main.inputMultiplexer.addProcessor(stage);
		this.clientLobby.sendChanges();

		NetworkManager.getInstance().setGameStateCallback(this);
		NetworkManager.getInstance().setDisconnectCallback(this);

		logger.info("Client-Lobby entered.");

		this.cpAccept = new ConsoleCmd("cpAccept", 0, "[DEBUG]", 0) {
			@Override
			public void execute(List<String> args) {
				clientLobby.toggleReadyState();
			}
		};

		this.cpTeam = new ConsoleCmd("cpTeam", 0, "[DEBUG]", 0) {
			@Override
			public void execute(List<String> args) {
				clientLobby.swapTeam();
			}
		};

		this.cpClass = new ConsoleCmd("cpClass", 0, "[DEBUG]", 1) {

			@Override
			public void showUsage() {
				super.showUsage("<ClassName> => [hunter, knight, tank]");
			}

			@Override
			public void execute(List<String> args) {
				EntityType t;
				switch (args.get(1)) {
				case "hunter":
					t = EntityType.Hunter;
					break;
				case "knight":
					t = EntityType.Knight;
					break;
				case "tank":
					t = EntityType.Tank;
					break;
				default:
					t = EntityType.Hunter;
				}

				clientLobby.changeEntityType(t);
			}
		};

		Main.getInstance().console.register(this.cpAccept);
		Main.getInstance().console.register(this.cpTeam);
		Main.getInstance().console.register(this.cpClass);

		if (!NetworkManager.getInstance().isClient()) {
			onLeave();
		}
		isInitialized = true;
	}

	@Override
	public void onEnterComplete() {
		// TODO Auto-generated method stub
		super.onEnterComplete();
	}

	@Override
	public void onLeave() {
		super.onLeave();
		Main.getInstance().removeScreenListener(stage);
		Main.inputMultiplexer.removeProcessor(this.stage);
		stage.dispose();

		Main.getInstance().console.unregister(this.cpAccept);
		Main.getInstance().console.unregister(this.cpTeam);
		Main.getInstance().console.unregister(this.cpClass);

		NetworkManager.getInstance().setGameStateCallback(null);
		NetworkManager.getInstance().setDisconnectCallback(null);

		this.clientLobby = null;
		this.stage = null;
		isInitialized = false;
	}

	@Override
	public void onLeaveComplete() {
		// TODO Auto-generated method stub
		super.onLeaveComplete();
	}

	@Override
	public void disconnectCallback(String msg) {
		logger.warn(msg);
		GameStates.MAINMENU.init(assetManager);
		GameStates.MAINMENU.activate();
	}
}
