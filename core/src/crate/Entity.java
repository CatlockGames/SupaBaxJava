/**
 * 
 */
package crate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This class is an abstract definition of an entity.
 * @author Aaron
 * @author Ryan
 *
 */
public abstract class Entity {
	protected GameScreen gameScreen;

	/**
	 * 
	 * @param gameScreen
	 */
	public Entity(GameScreen gameScreen){
		this.gameScreen = gameScreen;
	}
	
	/**
	 * Every entity object must have an update method
	 * @param delta
	 */
	public abstract void update(float delta);
	
	/**
	 * Every entity object must have a render method
	 * @param delta
	 */
	public abstract void render(SpriteBatch batch, float delta);
	
	/**
	 * Every entity object must have a dispose method
	 */
	public abstract void dispose();

}