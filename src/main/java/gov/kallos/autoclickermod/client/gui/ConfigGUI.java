package gov.kallos.autoclickermod.client.gui;


import gov.kallos.autoclickermod.Autoclicker_Mod;
import gov.kallos.autoclickermod.client.Autoclicker_ModClient;
import gov.kallos.autoclickermod.client.DefaultValues;
import gov.kallos.autoclickermod.client.gui.model.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration GUI
 * Holds all client configurable values.
 */
public class ConfigGUI extends GuiRoot {

    private int clicksPerSecond = DefaultValues.CPS;

    public ConfigGUI(Screen parentScreen) {
        super(parentScreen, (Text) Text.literal(Formatting.GRAY + "Autoclicker"));
    }

    @Override
    public GuiElement build() {
        FlexListLayout root = new FlexListLayout(Vec2.Direction.VERTICAL);
        root.add(new FlexListLayout(Vec2.Direction.HORIZONTAL)
                .add(new Label(this.title.getString()).align(Label.Alignment.ALIGN_CENTER).setWeight(new Vec2(1, 0)))
                .add((GuiElement) new Button("Save & Close").onClick(b -> {
                    saveToConfig();
                    this.client.setScreen(null);
                })));

        //TODO Holy shit i am retarded and there is most definitely a better way to do this
        FlexListLayout autoClickerList = new FlexListLayout(Vec2.Direction.HORIZONTAL);
        Button autoclickerButton = new Button("Autoclicker: " + getBooleanString(Autoclicker_ModClient.getCONFIG().autoclickerEnabled()));
        autoclickerButton.onClick(client -> {
            boolean autoclicker = Autoclicker_ModClient.getCONFIG().toggleAutoclicker();
            autoclickerButton.setText(Text.literal("Autoclicker: " + getBooleanString(autoclicker)));
        });
        autoClickerList.add(autoclickerButton);
        TextField clicksPerSecondField = new TextField(b -> {
            if(isInteger(b)) {
                this.clicksPerSecond = Integer.parseInt(b);
                return true;
            }
            return false;
        }, "", "CPS");
        autoClickerList.add(clicksPerSecondField);

        root.add(autoClickerList);
        return new TableLayout()
                .addRow((List<GuiElement>) Arrays.asList((GuiElement[])new GuiElement[]{new Spacer().setWeight(new Vec2(999, 1))}))
                .addRow((List<GuiElement>)Arrays.asList((GuiElement[])new GuiElement[]{null, root}))
                .addRow((List<GuiElement>) Arrays.asList((GuiElement[])new GuiElement[]{null, null, new Spacer().setWeight(new Vec2(999, 1))}));
    }

    private void saveToConfig() {
        if(clicksPerSecond != DefaultValues.CPS) {
            Autoclicker_ModClient.getCONFIG().setClicksPerSecond(clicksPerSecond);
        }

        //Finally, write config.
        Autoclicker_ModClient.getCONFIG().save();
    }

    private boolean validPersonalSpace(String b) {
        try {
            Integer intTranslate = Integer.parseInt(b);
            return !(intTranslate > 10) && !(intTranslate < 0);
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private String getBooleanString(boolean bool) {
        return bool ? Formatting.GREEN + "Enabled" : Formatting.RED + "Disabled";
    }

    private String getConnectedString(boolean bool) {
        return bool ? Formatting.GREEN + "Connected" : Formatting.RED + "Disconnected";
    }

    private boolean isInteger(String b) {
        try {
            Integer.parseInt(b);
            return true;
        } catch (NumberFormatException ex) {
            return  false;
        }
    }

    private boolean validRgb(String b) {
        try {
            Integer intTranslate = Integer.parseInt(b);
            return !(intTranslate > 255) && !(intTranslate < 0);
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public void init() {
        super.init();
        int left = this.width / 2 - 155;
        int centre = left + 80;
        int right = left + 160;
        int offset = this.height / 6 - 18;

        TextFieldWidget textTestWidget = new TextFieldWidget(textRenderer, left, height / 6, 60, 20, Text.literal("Test Box"));
        addDrawable(textTestWidget);
        offset += 24;


    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }
}
