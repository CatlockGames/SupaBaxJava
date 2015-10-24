package crate.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import crate.SupaBax;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Supa Bax";
		config.addIcon("crate.png", FileType.Internal);
		config.width = 768;
		config.height = 512;
		new LwjglApplication(new SupaBax(), config);
	}
}
