package com.shinichicken.easyautofishing.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class EasyAutoFishingClient implements ClientModInitializer {
	public static final String MOD_ID = "easy-auto-fishing";

	// Translation key: key.category.easy-auto-fishing.main
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(id("main"));

	private static KeyMapping toggleKey;

	@Override
	public void onInitializeClient() {
		toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.easy-auto-fishing.toggle",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKey.consumeClick()) {
				AutoFishingController.INSTANCE.toggle(client);
			}
			AutoFishingController.INSTANCE.tick(client);
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
