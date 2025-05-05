package qsided.rpmechanics.items.materials;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

public class QuesToolMaterials {
    public static final ToolMaterial MYTHRIL;
    static {
        MYTHRIL = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2330, 10.0F, 5.0F, 18, null);
    }
}
