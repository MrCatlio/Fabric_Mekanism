package mekanism.common.integration.crafttweaker.ingredient;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.item.MCIngredientList;
import com.blamejared.crafttweaker.impl.tag.MCTag;
import com.blamejared.crafttweaker.impl.tag.MCTagWithAmount;
import com.blamejared.crafttweaker.impl.tag.manager.TagManagerItem;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.inputs.ItemStackIngredient;
import mekanism.common.integration.crafttweaker.CrTConstants;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@NativeTypeRegistration(value = ItemStackIngredient.class, zenCodeName = CrTConstants.CLASS_ITEM_STACK_INGREDIENT)
public class CrTItemStackIngredient {

    private CrTItemStackIngredient() {
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given item stack.
     *
     * @param stack Item stack to match
     *
     * @return A {@link ItemStackIngredient} that matches a given item stack.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(IItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException("ItemStackIngredients cannot be created from an empty stack.");
        }
        return from(stack, stack.getAmount());
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given item with an amount of one.
     *
     * @param item Item to match
     *
     * @return A {@link ItemStackIngredient} that matches a given item with an amount of one.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(Item item) {
        return from(item, 1);
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given item and amount.
     *
     * @param item   Item to match
     * @param amount Amount needed
     *
     * @return A {@link ItemStackIngredient} that matches a given item and amount.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(Item item, int amount) {
        CrTIngredientHelper.assertValidAmount("ItemStackIngredients", amount);
        return ItemStackIngredient.from(item, amount);
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given item tag with a given amount.
     *
     * @param itemTag Tag to match
     * @param amount  Amount needed
     *
     * @return A {@link ItemStackIngredient} that matches a given item tag with a given amount.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(MCTag<Item> itemTag, int amount) {
        ITag<Item> tag = CrTIngredientHelper.assertValidAndGet(itemTag, amount, TagManagerItem.INSTANCE::getInternal, "ItemStackIngredients");
        return ItemStackIngredient.from(tag, amount);
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given item tag with an amount of one.
     *
     * @param itemTag Tag to match
     *
     * @return A {@link ItemStackIngredient} that matches a given item tag with an amount of one.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(MCTag<Item> itemTag) {
        return from(itemTag, 1);
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given item tag with amount.
     *
     * @param itemTag Tag and amount to match
     *
     * @return A {@link ItemStackIngredient} that matches a given item tag with amount.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(MCTagWithAmount<Item> itemTag) {
        return from(itemTag.getTag(), itemTag.getAmount());
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given ingredient with an amount of one.
     *
     * @param ingredient Ingredient to match
     *
     * @return A {@link ItemStackIngredient} that matches a given ingredient with an amount of one.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(IIngredient ingredient) {
        return from(ingredient, 1);
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given ingredient with amount.
     *
     * @param ingredient Ingredient and amount to match
     *
     * @return A {@link ItemStackIngredient} that matches a given ingredient with amount.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(IIngredientWithAmount ingredient) {
        return from(ingredient.getIngredient(), ingredient.getAmount());
    }

    /**
     * Creates a {@link ItemStackIngredient} that matches a given ingredient and amount.
     *
     * @param ingredient Ingredient to match
     * @param amount     Amount needed
     *
     * @return A {@link ItemStackIngredient} that matches a given ingredient and amount.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(IIngredient ingredient, int amount) {
        CrTIngredientHelper.assertValidAmount("ItemStackIngredients", amount);
        //Note: the IIngredient cases also handle item tags/item stacks
        Ingredient vanillaIngredient = ingredient.asVanillaIngredient();
        if (vanillaIngredient == Ingredient.EMPTY) {
            throw new IllegalArgumentException("ItemStackIngredients cannot be made using the empty ingredient: " + amount);
        }
        return ItemStackIngredient.from(vanillaIngredient, amount);
    }

    /**
     * Creates a {@link ItemStackIngredient} out of all the ingredients in the given {@link MCIngredientList}.
     *
     * @param ingredientList Ingredients to match
     *
     * @return A {@link ItemStackIngredient} made up of all the ingredients in the given {@link MCIngredientList}.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient from(MCIngredientList ingredientList) {
        IIngredient[] ingredients = ingredientList.getIngredients();
        if (ingredients.length == 0) {
            throw new IllegalArgumentException("ItemStackIngredients cannot be created from an empty ingredient list!");
        }
        List<ItemStackIngredient> itemStackIngredients = new ArrayList<>();
        addIngredients(itemStackIngredients, ingredients);
        return createMulti(itemStackIngredients.toArray(new ItemStackIngredient[0]));
    }

    private static void addIngredients(List<ItemStackIngredient> itemStackIngredients, IIngredient[] ingredients) {
        for (IIngredient ingredient : ingredients) {
            if (ingredient instanceof IItemStack) {
                //If the ingredient is an IItemStack make sure to process it as such so
                itemStackIngredients.add(from((IItemStack) ingredient));
            } else if (ingredient instanceof MCIngredientList) {
                //If it is another multi ingredient add the different components
                addIngredients(itemStackIngredients, ((MCIngredientList) ingredient).getIngredients());
            } else {
                itemStackIngredients.add(from(ingredient));
            }
        }
    }

    /**
     * Combines multiple {@link ItemStackIngredient}s into a single {@link ItemStackIngredient}.
     *
     * @param ingredients Ingredients to combine
     *
     * @return A single {@link ItemStackIngredient} representing all the passed in ingredients.
     */
    @ZenCodeType.StaticExpansionMethod
    public static ItemStackIngredient createMulti(ItemStackIngredient... ingredients) {
        return CrTIngredientHelper.createMulti("ItemStackIngredients", ItemStackIngredient::createMulti, ingredients);
    }
}