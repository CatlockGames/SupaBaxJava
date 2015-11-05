/**
 * 
 */
package weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import crate.GameScreen;

/**
 * Definition of a pistol
 * @author Aaron
 * @author Ryan
 *
 */
public class Pistol extends Weapon {

	/**
	 * 
	 * @param gameScreen
	 */
	public Pistol(GameScreen gameScreen) {
		super(gameScreen, 6f, false);
	}

	@Override
	protected void createBullet(float direction) {
		gameScreen.addEntity(new PistolBullet(gameScreen, gameScreen.getPlayer().getPosition(), direction));
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
	}

	@Override
	public void dispose() {
		System.out.println("Pistol disposed");
	}

}
