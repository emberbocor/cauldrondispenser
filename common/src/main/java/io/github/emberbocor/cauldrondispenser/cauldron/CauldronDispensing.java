package io.github.emberbocor.cauldrondispenser.cauldron;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public final class CauldronDispensing {
    private CauldronDispensing() {
    }

    /**
     * Applies the player's cauldron interaction for this item to the vanilla cauldron in front.
     * Returns the item the interaction hands back, or null when the dispenser's own behavior should run.
     */
    @Nullable
    public static ItemStack interact(BlockSource source, ItemStack stack, DefaultDispenseItemBehavior behavior) {
        // Only the registered behavior itself: nested fallback dispenses of other instances stay untouched.
        if (!source.state().is(Blocks.DISPENSER) || behavior != DispenserBlock.DISPENSER_REGISTRY.get(stack.getItem())) {
            return null;
        }
        ServerLevel level = source.level();
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        BlockState state = level.getBlockState(pos);
        if (stack.is(Items.WATER_BUCKET)) {
            if (state.is(Blocks.CAULDRON) || state.is(Blocks.WATER_CAULDRON) && !isFull(state)) {
                return empty(level, pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
                        SoundEvents.BUCKET_EMPTY, new ItemStack(Items.BUCKET));
            }
        } else if (stack.is(Items.LAVA_BUCKET)) {
            if (state.is(Blocks.CAULDRON)) {
                return empty(level, pos, Blocks.LAVA_CAULDRON.defaultBlockState(),
                        SoundEvents.BUCKET_EMPTY_LAVA, new ItemStack(Items.BUCKET));
            }
        } else if (stack.is(Items.POWDER_SNOW_BUCKET)) {
            if (state.is(Blocks.CAULDRON) || state.is(Blocks.POWDER_SNOW_CAULDRON) && !isFull(state)) {
                return empty(level, pos, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
                        SoundEvents.BUCKET_EMPTY_POWDER_SNOW, new ItemStack(Items.BUCKET));
            }
        } else if (stack.is(Items.BUCKET)) {
            if (state.is(Blocks.WATER_CAULDRON) && isFull(state)) {
                return fill(level, pos, SoundEvents.BUCKET_FILL, new ItemStack(Items.WATER_BUCKET));
            }
            if (state.is(Blocks.LAVA_CAULDRON)) {
                return fill(level, pos, SoundEvents.BUCKET_FILL_LAVA, new ItemStack(Items.LAVA_BUCKET));
            }
            if (state.is(Blocks.POWDER_SNOW_CAULDRON) && isFull(state)) {
                return fill(level, pos, SoundEvents.BUCKET_FILL_POWDER_SNOW, new ItemStack(Items.POWDER_SNOW_BUCKET));
            }
        } else if (stack.is(Items.GLASS_BOTTLE)) {
            if (state.is(Blocks.WATER_CAULDRON)) {
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                return PotionContents.createItemStack(Items.POTION, Potions.WATER);
            }
        } else if (stack.is(Items.POTION) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER)) {
            if (state.is(Blocks.CAULDRON)) {
                return empty(level, pos, Blocks.WATER_CAULDRON.defaultBlockState(),
                        SoundEvents.BOTTLE_EMPTY, new ItemStack(Items.GLASS_BOTTLE));
            }
            if (state.is(Blocks.WATER_CAULDRON) && !isFull(state)) {
                return empty(level, pos, state.cycle(LayeredCauldronBlock.LEVEL),
                        SoundEvents.BOTTLE_EMPTY, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return null;
    }

    private static boolean isFull(BlockState state) {
        return state.getValue(LayeredCauldronBlock.LEVEL) == LayeredCauldronBlock.MAX_FILL_LEVEL;
    }

    private static ItemStack empty(ServerLevel level, BlockPos pos, BlockState filled, SoundEvent sound, ItemStack result) {
        level.setBlockAndUpdate(pos, filled);
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        return result;
    }

    private static ItemStack fill(ServerLevel level, BlockPos pos, SoundEvent sound, ItemStack result) {
        level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        return result;
    }
}
