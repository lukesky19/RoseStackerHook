package lv.id.bonne.rosestackerhook.events;


import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.eclipse.jdt.annotation.NonNull;

import dev.rosewood.rosestacker.api.RoseStackerAPI;
import lv.id.bonne.rosestackerhook.RoseStackerHookAddon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.util.Util;


/**
 * This listener checks every player event and acts if event name is "HopperAccessEvent".
 * As EpicHoppers plugin is premium with hidden under protected repository, it is not
 * possible to use proper event class. Need to use this workaround.
 */
public class BlockInteractListener implements Listener
{
    /**
     * Constructor HopperAccessListener creates a new HopperAccessListener instance.
     *
     * @param addon of type EpicHooksAddon
     */
    public BlockInteractListener(@NonNull RoseStackerHookAddon addon)
    {
        this.addon = addon;
    }


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHopperAccess(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() == null)
        {
            // Ignore clicking in air.
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            // Ignore non-right click actions.
            return;
        }

        Player player = event.getPlayer();

        if (!player.isSneaking())
        {
            // RoseStacker GUI opens only on sneaking. Weird.
            return;
        }

        if (!this.addon.getPlugin().getIWM().inWorld(Util.getWorld(player.getWorld())))
        {
            // Ignore non-bentobox worlds.
            return;
        }

        if (!RoseStackerAPI.getInstance().isBlockStacked(event.getClickedBlock()) &&
            !RoseStackerAPI.getInstance().isSpawnerStacked(event.getClickedBlock()))
        {
            // Ignore non-stacked blocks and spawners.
            return;
        }

        if (!this.addon.getIslands().getIslandAt(player.getLocation()).
            map(i -> i.isAllowed(User.getInstance(player), RoseStackerHookAddon.ROSE_STACKER_GUI)).
            orElse(false))
        {
            // Player does not have access to the stacking GUI's.
            event.setCancelled(true);
        }
    }


    /**
     * Current addon.
     */
    private final RoseStackerHookAddon addon;
}
