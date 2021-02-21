package com.deadnight.f3mini;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;
import java.util.ListIterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(F3Mini.MOD_ID)
public class F3Mini {

    public static final String MOD_ID = "f3mini";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static Supplier<Minecraft> minecraftSupplier;

    public F3Mini() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, F3MiniConfig.CLIENT_SPEC);
    }

    private void setup(final FMLClientSetupEvent event)
    {
        minecraftSupplier = event.getMinecraftSupplier();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (!F3MiniConfig.GeneralConfig.enabled) {
            return;
        } else if (!minecraftSupplier.get().gameSettings.showDebugInfo) {
            return;
        }

        Pattern p;

        try {
            p = Pattern.compile(F3MiniConfig.GeneralConfig.regex);
        } catch (PatternSyntaxException ex) {
            event.getRight().add("");
            event.getRight().add("F3 Mini:");
            event.getRight().add(ex.getDescription() + " near index " + ex.getIndex());
            return;
        }

        ListIterator<String> it = event.getLeft().listIterator();
        while(it.hasNext()) {
            if(shouldRemove(p, it.next())) {
                it.remove();
            }
        }

        it = event.getRight().listIterator();
        while(it.hasNext()) {
            if(shouldRemove(p, it.next())) {
                it.remove();
            }
        }
    }

    private static Boolean shouldRemove(Pattern pattern, String line) {
        if(pattern.matcher(line).matches()) {
            return F3MiniConfig.GeneralConfig.removeMatching;
        } else {
            return !F3MiniConfig.GeneralConfig.removeMatching;
        }
    }
}
