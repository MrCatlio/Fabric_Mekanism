package mekanism.api.recipes;

import java.util.List;
import java.util.Objects;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.util.TriPredicate;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Input: ItemStack
 * <br>
 * Input: FluidStack
 * <br>
 * Input: GasStack
 * <br>
 * Item Output: ItemStack (can be empty if gas output is not empty)
 * <br>
 * Gas Output: GasStack (can be empty if item output is not empty)
 *
 * @apiNote Pressurized Reaction Chambers can process this recipe type.
 */
@NothingNullByDefault
public abstract class PressurizedReactionRecipe extends MekanismRecipe implements TriPredicate<@NotNull ItemStack, @NotNull FluidStack, @NotNull GasStack> {

    /**
     * Gets the item input ingredient.
     */
    public abstract ItemStackIngredient getInputSolid();

    /**
     * Gets the fluid input ingredient.
     */
    public abstract FluidStackIngredient getInputFluid();

    /**
     * Gets the gas input ingredient.
     */
    public abstract GasStackIngredient getInputGas();

    /**
     * Gets the amount of "extra" energy this recipe requires, compared to the base energy requirements of the machine performing the recipe.
     */
    public abstract FloatingLong getEnergyRequired();

    /**
     * Gets the base duration in ticks that this recipe takes to complete.
     */
    public abstract int getDuration();

    @Override
    public abstract boolean test(ItemStack solid, FluidStack liquid, GasStack gas);

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public abstract List<PressurizedReactionRecipeOutput> getOutputDefinition();

    /**
     * Gets a new output based on the given inputs.
     *
     * @param solid  Specific item input.
     * @param liquid Specific fluid input.
     * @param gas    Specific gas input.
     *
     * @return New output.
     *
     * @apiNote While Mekanism does not currently make use of the inputs, it is important to support it and pass the proper value in case any addons define input based
     * outputs where things like NBT may be different.
     * @implNote The passed in inputs should <strong>NOT</strong> be modified.
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public abstract PressurizedReactionRecipeOutput getOutput(ItemStack solid, FluidStack liquid, GasStack gas);

    @Override
    public boolean isIncomplete() {
        return getInputSolid().hasNoMatchingInstances() || getInputFluid().hasNoMatchingInstances() || getInputGas().hasNoMatchingInstances();
    }

    @Override
    public final RecipeType<?> getType() {
        return MekanismRecipeTypes.TYPE_REACTION.get();
    }

    /**
     * @apiNote Both item and gas may be present or one may be empty.
     */
    public record PressurizedReactionRecipeOutput(@NotNull ItemStack item, @NotNull GasStack gas) {

        public PressurizedReactionRecipeOutput {
            Objects.requireNonNull(item, "Item output cannot be null.");
            Objects.requireNonNull(gas, "Gas output cannot be null.");
            if (item.isEmpty() && gas.isEmpty()) {
                throw new IllegalArgumentException("At least one output must be present.");
            }
        }
    }
}
