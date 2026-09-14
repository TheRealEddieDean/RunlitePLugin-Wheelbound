package com.wheelbound;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class WheelboundPluginTest
{
	// RuneLite's loadBuiltin API uses a generic varargs array.
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(WheelboundPlugin.class);
		RuneLite.main(args);
	}
}
