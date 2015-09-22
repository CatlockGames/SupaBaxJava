/**
 * 
 */
package crate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Aaron
 * @author Ryan
 *
 */
public abstract class Entity {

	/**
	 * 
	 */
	public Entity(){
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