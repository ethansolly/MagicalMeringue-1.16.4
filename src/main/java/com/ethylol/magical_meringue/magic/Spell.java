package com.ethylol.magical_meringue.magic;

import com.ethylol.magical_meringue.magic.effects.one.*;
import com.ethylol.magical_meringue.magic.effects.two.*;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

public class Spell {

    //Object

    private ISpellEffect effect;

    public Spell(ISpellEffect effect) {
        this.effect = effect;
    }

    public ISpellEffect getEffect() {
        return effect;
    }

    //Static

    public static final Spell acidSplash = new Spell(new AcidSplash());
    public static final Spell breakBlock = new Spell(new BreakBlock());
    public static final Spell changeColor = new Spell(new ChangeColor());
    public static final Spell cureMinorWounds = new Spell(new CureMinorWounds());
    public static final Spell doNothing = new Spell(new DoNothing());
    public static final Spell invisibilityFlash = new Spell(new InvisibilityFlash());
    public static final Spell levitate = new Spell(new Levitate());
    public static final Spell makeMorsel = new Spell(new MakeMorsel());
    public static final Spell smeltItem = new Spell(new SmeltItem());
    public static final Spell summonUselessCat = new Spell(new SummonUselessCat());

    public static final Spell astralProjection = new Spell(new AstralProjection());
    public static final Spell exchange = new Spell(new Exchange());
    public static final Spell featherFalling = new Spell(new FeatherFalling());
    public static final Spell lasso = new Spell(new Lasso());
    public static final Spell phasing = new Spell(new Phasing());
    public static final Spell separate = new Spell(new Separate());

    public static final List<Spell> list = new ArrayList<>();
    static {
        //Tier 1
        list.add(acidSplash);
        list.add(breakBlock);
        list.add(changeColor);
        list.add(cureMinorWounds);
        list.add(doNothing);
        list.add(invisibilityFlash);
        list.add(levitate);
        list.add(makeMorsel);
        list.add(smeltItem);
        list.add(summonUselessCat);

        //Tier 2
        list.add(astralProjection);
        list.add(exchange);
        list.add(featherFalling);
        list.add(lasso);
        list.add(phasing);
        list.add(separate);
    }
}
