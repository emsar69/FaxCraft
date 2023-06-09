package com.emsar.faxcraft.recipe;

import com.emsar.faxcraft.ModMain;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CompressorRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public CompressorRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems){
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer container, Level p_44003_) {
        return recipeItems.get(0).test(container.getItem(0)) && recipeItems.get(1).test(container.getItem(1));
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CompressorRecipe>{
        private Type(){}
        public static final Type INSTANCE = new Type();
        public static final String ID = "compressor";
    }

    public static class Serializer implements RecipeSerializer<CompressorRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ModMain.MODID, "compressor");

        @Override
        public CompressorRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            JsonArray ing = GsonHelper.getAsJsonArray(json, "ing");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromJson(ing.get(i)));
            }

            return new CompressorRecipe(id, output, inputs);
        }

        @Nullable
        @Override
        public CompressorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromNetwork(buf));
            }
            return new CompressorRecipe(id, buf.readItem(), inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CompressorRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for(Ingredient ing : recipe.getIngredients()){
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }

        private static <G> Class<G> castClass(Class<RecipeSerializer> cls) {
            return (Class<G>)cls;
        }
    }
}
