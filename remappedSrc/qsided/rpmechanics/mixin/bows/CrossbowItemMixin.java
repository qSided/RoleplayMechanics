package qsided.rpmechanics.mixin.bows;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import qsided.rpmechanics.attributes.RoleplayMechanicsAttributes;
import qsided.rpmechanics.events.ShootBowCallback;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    
    @WrapMethod(method = "shoot")
    public void accountForAttribute(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, Operation<Void> original) {
        original.call(
                shooter,
                projectile,
                index,
                speed + (float) shooter.getAttributeInstance(RoleplayMechanicsAttributes.BOW_PROJECTILE_SPEED).getValue(),
                divergence - (float) shooter.getAttributeInstance(RoleplayMechanicsAttributes.BOW_PROJECTILE_ACCURACY).getValue(),
                yaw,
                target);
    }
    
    @WrapMethod(method = "use")
    public ActionResult callback(World world, PlayerEntity user, Hand hand, Operation<ActionResult> original) {
        if (!world.isClient()) {
            ShootBowCallback.EVENT.invoker().shoot(world, user, hand);
        }
        return original.call(world, user, hand);
    }
    
}
