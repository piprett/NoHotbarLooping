package github.erb3.fabric.nohotbarlooping;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoHotbarLooping implements ClientModInitializer {
    public static MinecraftClient client;
    private static KeyBinding keyBinding;

    @SuppressWarnings("SpellCheckingInspection")
    public static final String modid = "nohotbarlooping";
    public static final Logger logger = LoggerFactory.getLogger("NoHotbarLooping");
    public static final Config config = new Config();
    public static boolean shouldLoopHotbar = false;

    @Override
    public void onInitializeClient() {
        logger.info("Hello from No Hotbar Looping!");
        client = MinecraftClient.getInstance();
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of(modid, "keybinding.category"));

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            modid + ".keybinding.name",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_PERIOD,
            category
        ));

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            while (keyBinding.wasPressed()) {
                config.toggle();
                renderToast(shouldLoopHotbar);
            }
        });

        config.loadConfig();
    }

    private static void renderToast(boolean shouldLoopHotbar) {
        Item item;
        String title;

        if (shouldLoopHotbar) {
            item = Items.EMERALD_BLOCK;
            title = translate("toast.disabled").getString();
        } else {
            item = Items.BARRIER;
            title = translate("toast.enabled").getString();
        }

        // Access the ToastManager here as the mod gets initialized before the field is assigned
        ToastManager toaster = client.getToastManager();
        CustomToast oldToast = toaster.getToast(CustomToast.class, NoHotbarLooping.modid);
        if (oldToast != null) oldToast.remove();
        toaster.add(new CustomToast(item, title, "- NoHotbarLooping"));
    }

    public static Text translate(String key) {
        return Text.translatable(modid + "." + key);
    }
}
