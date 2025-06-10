package com.atsuishio.superbwarfare.recipe;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PotionMortarShellRecipe extends CustomRecipe {

    public PotionMortarShellRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        if (pContainer.getWidth() == 3 && pContainer.getHeight() == 3) {
            for (int i = 0; i < pContainer.getWidth(); ++i) {
                for (int j = 0; j < pContainer.getHeight(); ++j) {
                    int index = i + j * pContainer.getWidth();

                    ItemStack itemstack = pContainer.getItem(index);

                    if (index % 2 == 0) {
                        if (i == 1 && j == 1) {
                            if (!itemstack.is(Items.LINGERING_POTION)) {
                                return false;
                            }
                        } else if (!itemstack.isEmpty()) {
                            return false;
                        }
                    } else if (!itemstack.is(ModItems.MORTAR_SHELL.get())) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        ItemStack itemstack = pContainer.getItem(1 + pContainer.getWidth());
        if (!itemstack.is(Items.LINGERING_POTION)) {
            return ItemStack.EMPTY;
        } else {
            ItemStack res = new ItemStack(ModItems.POTION_MORTAR_SHELL.get(), 4);
            PotionUtils.setPotion(res, PotionUtils.getPotion(itemstack));
            PotionUtils.setCustomEffects(res, PotionUtils.getCustomEffects(itemstack));
            return res;
        }
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= 2 && pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.POTION_MORTAR_SHELL_SERIALIZER.get();
    }
}
