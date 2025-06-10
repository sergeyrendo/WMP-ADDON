package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.init.ModRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        SpecialRecipeBuilder.special(ModRecipes.POTION_MORTAR_SHELL_SERIALIZER.get()).save(pWriter, "potion_mortar_shell");
        SpecialRecipeBuilder.special(ModRecipes.AMMO_BOX_ADD_AMMO_SERIALIZER.get()).save(pWriter, "ammo_box_add_ammo");
        SpecialRecipeBuilder.special(ModRecipes.AMMO_BOX_EXTRACT_AMMO_SERIALIZER.get()).save(pWriter, "ammo_box_extract_ammo");
    }
}
