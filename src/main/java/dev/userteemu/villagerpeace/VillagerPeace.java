package dev.userteemu.villagerpeace;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.message.MessageSender;
import net.minecraft.network.message.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class VillagerPeace implements ModInitializer {
    public boolean isEnabled = true;

    @Override
    public void onInitialize() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (isEnabled && entity instanceof VillagerEntity) {
                player.sendMessage(Text.translatable("villagerpeace.blocked"), true);
                return ActionResult.FAIL;
            } else {
                return ActionResult.PASS;
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                ClientCommandManager.literal("villagerpeace")
                    .then(ClientCommandManager.literal("on")
                        .executes((ctx) -> {
                            isEnabled = true;
                            ctx.getSource().sendFeedback(Text.translatable("villagerpeace.on"));
                            return 0;
                        })
                    ).then(ClientCommandManager.literal("off")
                        .executes(ctx -> {
                            isEnabled = false;
                            ctx.getSource().sendFeedback(Text.translatable("villagerpeace.off"));
                            return 0;
                        })
                    )
            );
        });
    }
}
