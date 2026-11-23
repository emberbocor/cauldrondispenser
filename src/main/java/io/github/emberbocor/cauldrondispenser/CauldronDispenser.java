package io.github.emberbocor.cauldrondispenser;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(CauldronDispenser.MODID)
public class CauldronDispenser {
    public static final String MODID = "cauldrondispenser";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CauldronDispenser(IEventBus modEventBus, ModContainer modContainer) {
    }
}
