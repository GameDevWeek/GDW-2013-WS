package de.hochschuletrier.gdw.ws1314.entity.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

/**
 * 
 * @author Sonic
 *
 */

public class ServerProjectile extends ServerEntity {
	protected float speed;
	protected Vector2 direction;
	protected float flightDistance;
	protected TeamColor teamColor;
	protected long sourceID;
	
	public ServerProjectile() {
		super();
	}
	
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Vector2 getDirection() {
		return direction;
	}
	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}
	public void setTeamColor(TeamColor teamColor) {
		this.teamColor = teamColor;
	}

	public float getFlightDistance() {
		return flightDistance;
	}
	public void setFlightDistance(float flightDistance) {
		this.flightDistance = flightDistance;
	}

	public long getSourceID() {
		return sourceID;
	}
	public void setSourceID(long sourceID) {
		this.sourceID = sourceID;
	}

	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void enable() {
	}

	@Override
	public void disable() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void initialize() {
		this.type = EntityType.Projectil;
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
}