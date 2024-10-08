package org.randomlima.runetest.managers;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.randomlima.runetest.utilities.Colorize;
import org.randomlima.runetest.RuneTest;
import org.randomlima.runetest.abilities.Ability;
import org.randomlima.runetest.objects.Staff;
import org.randomlima.runetest.objects.StaffState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotManager implements Listener {
    private RuneTest plugin;
    private Staff manager;
    private List<Ability> abilities = new ArrayList<>();
    private int slot = 0;
    private int maxSlots;

    public SlotManager(RuneTest plugin, Staff manager, ArrayList<Ability> abilities) {
        this.manager = manager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        Collections.reverse(abilities); //yea yea I have to reverse it again because of stupid lore list stuff

        for (Ability ability : abilities) {
            if (ability.getAbilityType().isToggled()) {
                this.abilities.add(ability);
            }
        }
        this.maxSlots = this.abilities.size() - 1;
    }

    public void nextSlot() {
        slot += 1;
        if (slot > maxSlots) {
            slot = 0;
        }
        Bukkit.getPlayer(manager.getOwner()).sendActionBar(Colorize.format(abilities.get(slot).getDisplayName()));
    }

    public Ability getActiveAbility() {
        return abilities.get(slot);
    }

    @EventHandler
    public void onShiftLeftClick(PlayerInteractEvent event) {
        if (!event.getPlayer().getUniqueId().equals(manager.getOwner())) return;
        if (!event.getAction().isLeftClick()) return;
        if (manager.getState() != StaffState.HELD) return;
        if (!event.getPlayer().isSneaking()) return;
        nextSlot();
    }

    @EventHandler
    public void onRun(PlayerJumpEvent event) {
        //Bukkit.broadcastMessage("jump");
    }

    public void delete() {
        HandlerList.unregisterAll(this);
    }
}

