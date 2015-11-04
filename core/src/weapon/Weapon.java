/**
 * 
 */
package weapon;

import crate.Entity;
import crate.GameScreen;

/**
 * Abstract definition of a weapon.
 * @author Aaron
 * @author Ryan
 *
 */
public abstract class Weapon extends Entity {
	protected float firerate;
	
	private long timeSinceLastShot;
	private boolean automatic;

	/**
	 * 
	 * @param gameScreen
	 * @param firerate
	 * @param automatic
	 */
	public Weapon(GameScreen gameScreen, float firerate, boolean automatic) {
		super(gameScreen);
		this.firerate = firerate;
		this.automatic = automatic;
	}
	
	protected abstract void createBullet(float direction);
	
	/**
	 * Fire a bullet based on firerate
	 * @param direction
	 */
	public void fire(float direction){
		//Check if it can fire
		long currentTime = System.currentTimeMillis();
		if((currentTime - timeSinceLastShot) / 1000f >= 1f / firerate){
			timeSinceLastShot = currentTime;
			createBullet(direction);
		}
	}
	
	/**
	 * Gets whether the weapon is automatic or semi-automatic.
	 * @return
	 */
	public boolean isAutomatic(){
		return automatic;
	}

}
