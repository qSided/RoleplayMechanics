package qsided.rpmechanics.mixin.bows;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import qsided.rpmechanics.attributes.RoleplayMechanicsAttributes;
import qsided.rpmechanics.events.ShootBowCallback;

@Mixin(BowItem.class)
public class BowItemMixin {
    
    @WrapOperation(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    public void accountForAttribute(ProjectileEntity instance, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence, Operation<Void> original) {
        if (shooter instanceof LivingEntity livingShooter) {
            original.call(
                    instance,
                    shooter,
                    pitch,
                    yaw,
                    roll,
                    speed + (float) livingShooter.getAttributeInstance(RoleplayMechanicsAttributes.BOW_PROJECTILE_SPEED).getValue(),
                    divergence - (float) livingShooter.getAttributeInstance(RoleplayMechanicsAttributes.BOW_PROJECTILE_ACCURACY).getValue());
        } else {
            original.call(instance, shooter, pitch, yaw, roll, speed, divergence);
        }
    }
    
    @WrapMethod(method = "use")
    public ActionResult callback(World world, PlayerEntity user, Hand hand, Operation<ActionResult> original) {
        if (!world.isClient()) {
            ShootBowCallback.EVENT.invoker().shoot(world, user, hand);
        }
        return original.call(world, user, hand);
    }
    
}
