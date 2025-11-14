//package qsided.rpmechanics.mixin.client;
//
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.Identifier;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import qsided.rpmechanics.networking.SendPlayerFallPayload;
//
//@Mixin(PlayerEntity.class)
//public class JumpAndHandleFallDamageMixin {
//
//    @WrapOperation(method = "handleFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V"))
//    public void rpmechanics$onHandleFallDamage(PlayerEntity player,
//                                               Identifier stat,
//                                               int fallDistance,
//                                               Operation<Void> original) {
//        ClientPlayNetworking.send(new SendPlayerFallPayload(fallDistance));
//        original.call(player, stat, fallDistance);
//    }
//}

// Moved to server side with PlayerFallDamageScaleMixin.java + PlayerFallMixin.java