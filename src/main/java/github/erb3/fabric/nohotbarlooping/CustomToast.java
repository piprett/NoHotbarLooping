package github.erb3.fabric.nohotbarlooping;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class CustomToast implements Toast {
    public static final int titleColor = 0xFFFFFFFF;
    public static final int textColor = 0xFFD3D3D3;

    private final ItemStack icon;
    private final Text title;
    private final Text text;
    private final long duration;
    private boolean timeStarted = false;
    private long start;
    private Visibility visibility = Visibility.HIDE;

    public CustomToast(@NotNull Item item, @NotNull String title, @NotNull String text) {
        this.icon = item.getDefaultStack();
        this.title = Text.literal(title).setStyle(Style.EMPTY.withColor(titleColor));
        this.text = Text.literal(text).setStyle(Style.EMPTY.withColor(textColor));
        this.duration = 4500;
    }

    @Override
    public Visibility getVisibility() {
        return this.visibility;
    }

    @Override
    public Object getType() {
        return NoHotbarLooping.modid;
    }

    @Override
    public void update(ToastManager manager, long currentTime) {
        if (!timeStarted) {
            start = currentTime;
            timeStarted = true;
        }

        this.visibility = currentTime - start >= this.duration ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void draw(DrawContext context, TextRenderer textRenderer, long currentTime) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("toast/advancement"), 0, 0, getWidth(), getHeight());

        int x = 28;
        MinecraftClient mc = MinecraftClient.getInstance();

        context.drawText(mc.textRenderer, title, x, 7, titleColor, false);
        context.drawText(mc.textRenderer, text, x, 18, textColor, false);
        context.drawItem(icon, 8, 8);
    }

    public void remove() {
        this.start = Short.MIN_VALUE;
    }
}
