package com.ethylol.magical_meringue.client.gui;

import com.ethylol.magical_meringue.MagicalMeringueCore;
import com.ethylol.magical_meringue.capabilities.Capabilities;
import com.ethylol.magical_meringue.capabilities.mana.IManaHandler;
import com.ethylol.magical_meringue.capabilities.mana.ManaHandler;
import com.ethylol.magical_meringue.magic.Spell;
import com.ethylol.magical_meringue.network.UpdateBookMessage;
import com.ethylol.magical_meringue.utils.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;

import java.io.IOException;
import java.util.*;

public class GuiSpellbook extends Screen {

    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(MagicalMeringueCore.MODID, "textures/gui/spellbook.png");
    private static int BLUE = Utils.colorFromHexString("0000FF");
    private PlayerEntity player;
    private ItemStack bookStack;
    private int level;
    private int currPage;

    private Button buttonExit;
    private Button buttonLeft;
    private Button buttonRight;

    private Map<Button, String> spellMap;

    private Button selectedButton;


    public GuiSpellbook(PlayerEntity player, ItemStack bookStack) {
        super(StringTextComponent.EMPTY);
        this.player = player;
        this.bookStack = bookStack;

        LazyOptional<IManaHandler> manaHandler = player.getCapability(Capabilities.MANA_HANDLER_CAPABILITY, null);
        if (manaHandler.isPresent()) {
            level = manaHandler.orElse(new ManaHandler()).getLvl();
        }
    }

    @Override
    public void init(Minecraft mc, int width, int height) {
        super.init(mc,width,height);

        this.buttons.clear();

        this.buttonLeft = this.addButton(new Button( width/2 - 20-90, height-50, 90, 20, new StringTextComponent("Left"), new SpellButton()));
        this.buttonRight = this.addButton(new Button(width/2 + 20, height-50, 90, 20, new StringTextComponent("Right"), new SpellButton()));
        this.buttonExit = this.addButton(new Button( width-100, height-25, 90, 20, new StringTextComponent("Exit"), new SpellButton()));

        currPage = 0;

        refreshSpells();

        this.buttonLeft.visible = false;
        if (level == 1) {
            this.buttonRight.visible = false;
        }
    }

    private void refreshSpells() {
        if (spellMap != null && !spellMap.keySet().isEmpty()) {
            this.buttons.removeAll(spellMap.keySet());
            this.children.removeAll(spellMap.keySet());
        }

        spellMap = new HashMap<>();
        List<Spell> list = new ArrayList<>(Spell.list);
        list.removeIf(s -> s.getEffect().tier() != currPage);
        if (list.size() > 0) {
            list.sort(Comparator.comparing(c -> c.getEffect().name()));
            int column = 0;
            int y = 34-25;
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i).getEffect().name();
                y += 25;
                if (y >= 194-20) {
                    column = 1;
                    y = 34;
                }
                Button spellButton = this.addButton(new Button(width / 2 + ((column == 0)? (-150+36) : (150-36-90)), y, 90, 20, new StringTextComponent(s), new SpellButton()));
                spellMap.put(spellButton, s);
                //MagicalMeringueCore.getLogger().debug(spellButton.getMessage().getString());
                if (bookStack.getTag().contains("spell") && bookStack.getTag().getString("spell").equals(s)) {
                    spellButton.setFGColor(BLUE);
                    selectedButton = spellButton;
                }
            }
        }

        this.buttonLeft.visible = (currPage != 0);
        this.buttonRight.visible = (currPage != level - 1);

    }

    public class SpellButton implements Button.IPressable {

        public void onPress(Button button) {
            //MagicalMeringueCore.getLogger().debug(button.getMessage().getString());
            if (button.active) {
                if (button == buttonLeft) {
                    //left
                    if (currPage != 0) {
                        currPage--;
                        refreshSpells();
                    }
                }
                if (button == buttonRight) {
                    //right
                    if (currPage != level - 1) {
                        currPage++;
                        refreshSpells();
                    }
                }
                if (button == buttonExit) {
                    //exit
                    minecraft.displayGuiScreen(null);
                }
                if (spellMap.get(button) != null) {
                    if (button != selectedButton) {
                        String selected = spellMap.get(button);
                        CompoundNBT compound = bookStack.getTag();
                        compound.putString("spell", selected);
                        bookStack.setTag(compound);

                        button.setFGColor(BLUE);
                        if (selectedButton != null) {
                            selectedButton.setFGColor(Widget.UNSET_FG_COLOR);
                        }
                        selectedButton = button;

                    } else {
                        //deselect current button
                        CompoundNBT compound = bookStack.getTag();
                        compound.remove("spell");
                        bookStack.setTag(compound);
                        selectedButton.setFGColor(Widget.UNSET_FG_COLOR);
                        selectedButton = null;
                    }

                    MagicalMeringueCore.network.sendToServer(new UpdateBookMessage(bookStack));
                }
            }
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
        blit(stack, width/2-150, 2, 0, 0, 300, 192, 300, 192);

        this.font.drawString(stack,"Tier " + (currPage+1),width/2-150+10, 10, 0);
        if (spellMap.size() == 0) {
            this.font.drawString(stack,"No spells here...", width/2 - 150 + 36, 34, 0);
        }

        super.render(stack,mouseX, mouseY, partialTicks);
    }
}
