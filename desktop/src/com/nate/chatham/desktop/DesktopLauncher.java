package com.nate.chatham.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nate.chatham.Chatham;

class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 930;
		config.width = 1663;
		System.setProperty("sun.java2d.opengl", "true");
		new LwjglApplication(new Chatham(), config);
	}
}
