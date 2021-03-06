package com.kwpugh.gobber2.util;

import com.kwpugh.gobber2.Gobber2;
import com.kwpugh.gobber2.config.GobberConfigBuilder;
import com.kwpugh.gobber2.init.ItemInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Gobber2.modid, bus = EventBusSubscriber.Bus.FORGE )
public class RepairTickUtil
{
	public static int repairTickRate = GobberConfigBuilder.RING_REPAIR_DELAY.get();
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		PlayerEntity player = event.player;
		PlayerInventory inv = event.player.inventory;
		EnderChestInventory end_inv = player.getInventoryEnderChest();

		for (int slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			ItemStack stack = inv.getStackInSlot(slot);
			if (stack.getItem() == ItemInit.GOBBER2_RING_REPAIR.get())
			{	
				if (player.ticksExisted % repairTickRate == 0)
        		{
					repair(player, inv);
       		 	} 
			}
		}
		
		for (int slot = 0; slot < end_inv.getSizeInventory(); slot++)
		{
			ItemStack stack = end_inv.getStackInSlot(slot);
			if (stack.getItem() == ItemInit.GOBBER2_RING_REPAIR.get())
			{	
				if (player.ticksExisted % repairTickRate == 0)
        		{
					repair(player, inv);
       		 	} 
			}
		}
		
		if (SupportMods.CURIOS.isLoaded())
	    {
			if (CuriosUtil.findItem(ItemInit.GOBBER2_RING_REPAIR.get(), player) != ItemStack.EMPTY)
			{
				if (player.ticksExisted % repairTickRate == 0)
	    		{
					repair(player, inv);
	   		 	}  
		    }
	    }  
	}
	
	private static void repair(PlayerEntity player, PlayerInventory inv)
	{		
		for(int slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			ItemStack target = inv.getStackInSlot(slot);
			if (!target.isEmpty())
			{
				if (!(target == player.getHeldItemMainhand()))
				{
					if (target.isDamaged() && target.getItem().isRepairable(target))
					{
						target.setDamage(target.getDamage() - 1);
						return; 
					}
				}
			}
		}
	}
}

