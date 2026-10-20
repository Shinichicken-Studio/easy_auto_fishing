package com.shinichicken.easyautofishing.mixin.client;

import com.shinichicken.easyautofishing.client.AutoFishingController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
	// TAIL, not HEAD: the method is first entered on the netty thread, where ensureRunningOnSameThread
	// re-schedules it and throws; only the run on the client thread reaches the end.
	@Inject(method = "handleSoundEvent", at = @At("TAIL"))
	private void easyAutoFishing$onSoundEvent(ClientboundSoundPacket packet, CallbackInfo ci) {
		if (!packet.getSound().value().location().equals(SoundEvents.FISHING_BOBBER_SPLASH.location())) {
			return;
		}

		Player player = Minecraft.getInstance().player;
		FishingHook hook = player == null ? null : player.fishing;
		if (hook != null && hook.distanceToSqr(packet.getX(), packet.getY(), packet.getZ()) < 4.0) {
			AutoFishingController.INSTANCE.onBite();
		}
	}
}
