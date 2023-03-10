package lv.id.bonne.rosestackerhook;

import org.bukkit.Material;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.api.flags.clicklisteners.CycleClick;
import world.bentobox.bentobox.managers.RanksManager;
import lv.id.bonne.rosestackerhook.listeners.RoseStackerListener;


/**
 * This class inits RoseStackerHook addon.
 */
public final class RoseStackerHookAddon extends Addon
{
    /**
     * Executes code when loading the addon. This is called before {@link #onEnable()}.
     * This <b>must</b> be used to setup configuration, worlds and commands.
     */
    public void onLoad()
    {
    }


    /**
     * Executes code when enabling the addon. This is called after {@link #onLoad()}.
     * <br/> Note that commands and worlds registration <b>must</b> be done in {@link
     * #onLoad()}, if need be. Failure to do so <b>will</b> result in issues such as
     * tab-completion not working for commands.
     */
    public void onEnable()
    {
        // Check if it is enabled - it might be loaded, but not enabled.
        if (this.getPlugin() == null || !this.getPlugin().isEnabled())
        {
            this.logError("BentoBox is not available or disabled!");
            this.setState(State.DISABLED);
            return;
        }

        // Check if RoseStacker exists.
        if (!this.getServer().getPluginManager().isPluginEnabled("RoseStacker"))
        {
            this.logError("RoseStacker is not available or disabled!");
            this.setState(State.DISABLED);
            return;
        }

        // Register listener
        this.registerListener(new RoseStackerListener());
        this.registerFlag(ROSE_STACKER_GUI);
        this.registerFlag(ROSE_STACKER);
        this.registerFlag(ROSE_STACKER_ENTITIES);
        this.registerFlag(ROSE_STACKER_ITEMS);
        this.registerFlag(ROSE_STACKER_BLOCKS);
        this.registerFlag(ROSE_STACKER_SPAWNERS);
    }


    /**
     * Executes code when disabling the addon.
     */
    public void onDisable()
    {
    }


    /**
     * This flag allows to change who have access to RoseStackerGUI option. Owner can change it from
     * member rank till owner rank. Default value is set to member.
     */
    public final static Flag ROSE_STACKER_GUI =
        new Flag.Builder("ROSE_STACKER_GUI", Material.ROSE_BUSH).
            type(Flag.Type.PROTECTION).
            defaultRank(RanksManager.MEMBER_RANK).
            clickHandler(new CycleClick("ROSE_STACKER_GUI",
                RanksManager.VISITOR_RANK,
                RanksManager.OWNER_RANK)).
            build();

    /**
     * This flag allows toggling block stacking.
     */
    public final static Flag ROSE_STACKER_BLOCKS =
        new Flag.Builder("ROSE_STACKER_BLOCKS", Material.DIAMOND_BLOCK).
            type(Flag.Type.SETTING).
            defaultSetting(true).
            build();

    /**
     * This flag allows toggling item stacking.
     */
    public final static Flag ROSE_STACKER_ITEMS =
        new Flag.Builder("ROSE_STACKER_ITEMS", Material.POPPY).
            type(Flag.Type.SETTING).
            defaultSetting(true).
            build();

    /**
     * This flag allows toggling entity stacking.
     */
    public final static Flag ROSE_STACKER_ENTITIES =
        new Flag.Builder("ROSE_STACKER_ENTITIES", Material.SKELETON_SKULL).
            type(Flag.Type.SETTING).
            defaultSetting(true).
            build();

    /**
     * This flag allows toggling spawner stacking.
     */
    public final static Flag ROSE_STACKER_SPAWNERS =
        new Flag.Builder("ROSE_STACKER_SPAWNERS", Material.SPAWNER).
            type(Flag.Type.SETTING).
            defaultSetting(true).
            build();

    /**
     * This flag allows toggling all stacking with one button.
     */
    public static final Flag ROSE_STACKER =
        new Flag.Builder("ROSE_STACKER", Material.ROSE_BUSH).
            mode(Flag.Mode.BASIC).
            type(Flag.Type.SETTING).
            defaultSetting(true).
            subflags(ROSE_STACKER_BLOCKS, ROSE_STACKER_SPAWNERS, ROSE_STACKER_ENTITIES, ROSE_STACKER_ITEMS).
            build();
}
