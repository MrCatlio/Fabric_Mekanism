package mekanism.api.recipes;

import java.util.List;
import java.util.function.Predicate;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Input: Chemical
 * <br>
 * Output: ItemStack
 *
 * @apiNote Chemical Crystallizers can process this recipe type.
 */
@NothingNullByDefault
public abstract class ChemicalCrystallizerRecipe extends MekanismRecipe implements Predicate<@NotNull BoxedChemicalStack> {

    /**
     * Gets the output based on the given input.
     *
     * @param input Specific input.
     *
     * @return Output as a constant.
     *
     * @apiNote While Mekanism does not currently make use of the input, it is important to support it and pass the proper value in case any addons define input based
     * outputs where things like NBT may be different.
     * @implNote The passed in input should <strong>NOT</strong> be modified.
     */
    @Contract(value = "_ -> new", pure = true)
    public abstract ItemStack getOutput(BoxedChemicalStack input);

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public abstract List<ItemStack> getOutputDefinition();

    @Override
    public abstract boolean test(BoxedChemicalStack chemicalStack);

    /**
     * Helper to test this recipe against a chemical stack without having to first box it up.
     *
     * @param stack Input stack.
     *
     * @return {@code true} if the stack matches the input.
     *
     * @apiNote See {@link #test(BoxedChemicalStack)}.
     */
    public abstract boolean test(ChemicalStack<?> stack);

    /**
     * Helper to test this recipe against a chemical stack's type without having to first box it up.
     *
     * @param stack Input stack.
     *
     * @return {@code true} if the stack's type matches the input.
     *
     * @apiNote See {@link #testType(BoxedChemicalStack)}.
     */
    public abstract boolean testType(ChemicalStack<?> stack);

    /**
     * Helper to test this recipe against a chemical stack's type without having to first box it up.
     *
     * @param stack Input stack.
     *
     * @return {@code true} if the stack's type matches the input.
     */
    public abstract boolean testType(BoxedChemicalStack stack);

    /**
     * Gets the input ingredient.
     */
    public abstract ChemicalStackIngredient<?, ?> getInput();

    @Override
    public boolean isIncomplete() {
        return getInput().hasNoMatchingInstances();
    }

    @Override
    public final RecipeType<ChemicalCrystallizerRecipe> getType() {
        return MekanismRecipeTypes.TYPE_CRYSTALLIZING.get();
    }
}