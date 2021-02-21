package com.deadnight.f3mini;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = F3Mini.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class F3MiniConfig {
    public static final GeneralConfig GENERAL;

    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        GENERAL = new GeneralConfig(BUILDER);

        CLIENT_SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            GENERAL.bake();
        }
    }

    public static class GeneralConfig {

        public static boolean enabled;
        private final ForgeConfigSpec.BooleanValue ENABLED;

        public static String regex;
        private final ForgeConfigSpec.ConfigValue<String> REGEX;

        public static boolean removeMatching;
        private final ForgeConfigSpec.BooleanValue REMOVE_MATCHING;

        public GeneralConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("General config").push("general");

            ENABLED = builder
                    .comment("Enable the mod (true) or disable the mod (false)")
                    .define("enabled", true);

            REGEX = builder
                    .comment("Regular expression to match against F3 overlay screen lines. Matching lines will be removed.")
                    .define("regex", "^(C|E|P|Client Chunk Cache|ServerChunkCache|CH S|SH S|SC|Sounds|CPU|Display):.*");

            REMOVE_MATCHING = builder
                    .comment("Remove matching lines (true) or remove non-matching lines (false)")
                    .define("removeMatching", true);

            builder.pop();
        }

        public void bake() {
            enabled = ENABLED.get();
            regex = REGEX.get();
            removeMatching = REMOVE_MATCHING.get();
        }
    }
}
