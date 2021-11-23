//
// Created by BONNe
// Copyright - 2021
//


package lv.id.bonne.rosestackerhook.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import dev.rosewood.rosestacker.event.BlockStackEvent;
import dev.rosewood.rosestacker.event.EntityStackEvent;
import dev.rosewood.rosestacker.event.ItemStackEvent;
import dev.rosewood.rosestacker.event.SpawnerStackEvent;
import lv.id.bonne.rosestackerhook.RoseStackerHookAddon;
import world.bentobox.bentobox.api.flags.FlagListener;


/**
 * This listener checks if RoseStacker should stack objects based on island settings.
 */
public class RoseStackerListener extends FlagListener
{
    /**
     * Check if Blocks can be stacked.
     *
     * @param event BlockStackEvent.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockStacking(BlockStackEvent event)
    {
        // Cancel stacking if BlockStacking is disabled in island settings.
        if (!this.checkIsland(event,
            event.getPlayer(),
            event.getStack().getLocation(),
            RoseStackerHookAddon.ROSE_STACKER_BLOCKS))
        {
            this.noGo(event, RoseStackerHookAddon.ROSE_STACKER_BLOCKS);
        }
    }


    /**
     * Check if Items can be stacked.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemStacking(ItemStackEvent event)
    {
        // Cancel stacking if ItemStacking is disabled in island settings.
        event.setCancelled(!this.checkIsland(event,
            null,
            event.getStack().getLocation(),
            RoseStackerHookAddon.ROSE_STACKER_ITEMS));
    }


    /**
     * Check if Entities can be stacked.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityStacking(EntityStackEvent event)
    {
        // Cancel stacking if ItemStacking is disabled in island settings.
        event.setCancelled(!this.checkIsland(event,
            null,
            event.getStack().getLocation(),
            RoseStackerHookAddon.ROSE_STACKER_ENTITIES));
    }


    /**
     * Check if Spawners can be stacked.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSpawnerStacking(SpawnerStackEvent event)
    {
        // Cancel stacking if ItemStacking is disabled in island settings.
        if (!this.checkIsland(event,
            event.getPlayer(),
            event.getStack().getLocation(),
            RoseStackerHookAddon.ROSE_STACKER_SPAWNERS))
        {
            this.noGo(event, RoseStackerHookAddon.ROSE_STACKER_SPAWNERS);
        }
    }
}
