package com.ethylol.magical_meringue.utils;

import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;

import java.util.List;

public class Utils {



    /**Determine the max amount of mana of a certain tier a player at a given level has.
     *
     * @param tier
     * @param level
     * @return
     */
    public static int maxMana(int tier, int level) {
        return (tier > level)? 0 : 12 << (level-tier-1);
    }

    public static int colorFromHexString(String hex) {
        return Integer.parseInt(hex, 16);
    }
}
