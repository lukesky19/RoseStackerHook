//
// Created by BONNe
// Copyright - 2021
//

package lv.id.bonne.rosestackerhook.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import dev.rosewood.rosestacker.event.*;
import lv.id.bonne.rosestackerhook.RoseStackerHookAddon;
import org.eclipse.jdt.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.api.flags.FlagListener;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;


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


    /**
     * Check if StackGUI can be opened.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onGUIOpening(StackGUIOpenEvent event)
    {
        // Cancel stacking if ItemStacking is disabled in island settings.
        if (!this.checkIsland(event,
            event.getPlayer(),
            event.getStack().getLocation(),
            RoseStackerHookAddon.ROSE_STACKER_GUI))
        {
            this.noGo(event, RoseStackerHookAddon.ROSE_STACKER_GUI);
        }
    }

    /**
     * Check if monster or animal spawn flags are disabled and if so, cancel RoseStacker's SpawnerSpawnEvent.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSpawnerSpawn(PreStackedSpawnerSpawnEvent event)
    {
        Location spawnerLocation = event.getStack().getLocation();
        Optional<Island> optionalIsland = BentoBox.getInstance().getIslandsManager().getIslandAt(spawnerLocation);

        if (optionalIsland.isPresent())
        {
            Island island = optionalIsland.get();

            @NonNull Optional<Flag> monsterSpawnersSpawnOptionalFlag = BentoBox.getInstance().getFlagsManager().getFlag("MONSTER_SPAWNERS_SPAWN");
            @NonNull Optional<Flag> animalSpawnersSpawnOptionalFlag = BentoBox.getInstance().getFlagsManager().getFlag("ANIMAL_SPAWNERS_SPAWN");

            if (monsterSpawnersSpawnOptionalFlag.isPresent())
            {
                Flag monsterSpawnersSpawnFlag = monsterSpawnersSpawnOptionalFlag.orElseThrow();

                if (isHostile(event.getStack().getSpawner().getSpawnedType()))
                {
                    if (!island.isAllowed(monsterSpawnersSpawnFlag))
                    {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if (animalSpawnersSpawnOptionalFlag.isPresent())
            {
                Flag animalSpawnersSpawnFlag = animalSpawnersSpawnOptionalFlag.orElseThrow();

                if (isPassive(event.getStack().getSpawner().getSpawnedType()))
                {
                    if (!island.isAllowed(animalSpawnersSpawnFlag))
                    {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private boolean isHostile(@NotNull EntityType type)
    {
        if(type.getEntityClass() == null) return false;

        return Monster.class.isAssignableFrom(type.getEntityClass()) || type.equals(EntityType.SLIME);
    }

    private boolean isPassive(@NotNull EntityType type)
    {
        if(type.getEntityClass() == null) return false;

        return Animals.class.isAssignableFrom(type.getEntityClass());
    }
}
