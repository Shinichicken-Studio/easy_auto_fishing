package com.shinichicken.easyautofishing.client;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Client-side auto fishing state machine, driven once per client tick.
 * Timers count down to zero and fire on the tick they reach it; -1 means inactive.
 */
public final class AutoFishingController {
	public static final AutoFishingController INSTANCE = new AutoFishingController();

	private static final int REEL_DELAY_MIN = 4;
	private static final int REEL_DELAY_MAX = 8;
	private static final int RECAST_DELAY_MIN = 10;
	private static final int RECAST_DELAY_MAX = 20;
	// After casting, the bobber entity needs a few ticks to reach the client; don't treat "no bobber" as "cast again" meanwhile.
	private static final int CAST_COOLDOWN = 20;

	private boolean enabled;
	private int reelTimer = -1;
	private int recastTimer = -1;
	private int castCooldown;

	private AutoFishingController() {
	}

	public void toggle(Minecraft client) {
		enabled = !enabled;
		resetTimers();
		if (client.player != null) {
			client.player.sendOverlayMessage(enabled
					? Component.translatable("message.easy-auto-fishing.enabled").withStyle(ChatFormatting.GREEN)
					: Component.translatable("message.easy-auto-fishing.disabled").withStyle(ChatFormatting.RED));
		}
	}

	public void tick(Minecraft client) {
		LocalPlayer player = client.player;
		if (!enabled || player == null || client.gameMode == null) {
			return;
		}
		// Pause while any screen (inventory, chat, ...) is open; timers resume afterwards.
		if (client.gui.screen() != null) {
			return;
		}

		InteractionHand hand = findRodHand(player);
		if (hand == null) {
			resetTimers();
			return;
		}

		ItemStack rod = player.getItemInHand(hand);
		if (rod.isDamageableItem() && rod.getMaxDamage() - rod.getDamageValue() <= 1) {
			// Reel in first, but only when retrieving costs no durability: a pending bite, a hooked entity
			// or a grounded bobber would damage (and break) the last point.
			FishingHook hook = player.fishing;
			if (hook != null && reelTimer < 0 && hook.getHookedIn() == null && !hook.onGround()) {
				client.gameMode.useItem(player, hand);
			}
			enabled = false;
			resetTimers();
			player.sendOverlayMessage(Component.translatable("message.easy-auto-fishing.low_durability").withStyle(ChatFormatting.YELLOW));
			return;
		}

		if (castCooldown > 0) {
			castCooldown--;
		}

		if (reelTimer > 0) {
			if (--reelTimer == 0) {
				reelTimer = -1;
				if (player.fishing != null) {
					client.gameMode.useItem(player, hand);
				}
				recastTimer = randomBetween(RECAST_DELAY_MIN, RECAST_DELAY_MAX);
			}
			return;
		}

		if (recastTimer > 0) {
			if (--recastTimer == 0) {
				recastTimer = -1;
				if (player.fishing == null) {
					cast(client, player, hand);
				}
			}
			return;
		}

		if (player.fishing == null && castCooldown == 0) {
			cast(client, player, hand);
		}
	}

	/** Called on the client thread when a bobber splash sound plays next to our own bobber. */
	public void onBite() {
		if (enabled && reelTimer < 0 && recastTimer < 0) {
			reelTimer = randomBetween(REEL_DELAY_MIN, REEL_DELAY_MAX);
		}
	}

	private void cast(Minecraft client, LocalPlayer player, InteractionHand hand) {
		client.gameMode.useItem(player, hand);
		castCooldown = CAST_COOLDOWN;
	}

	private void resetTimers() {
		reelTimer = -1;
		recastTimer = -1;
		castCooldown = 0;
	}

	private static InteractionHand findRodHand(LocalPlayer player) {
		if (player.getMainHandItem().is(Items.FISHING_ROD)) {
			return InteractionHand.MAIN_HAND;
		}
		if (player.getOffhandItem().is(Items.FISHING_ROD)) {
			return InteractionHand.OFF_HAND;
		}
		return null;
	}

	private static int randomBetween(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}
