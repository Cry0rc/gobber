package com.kwpugh.gobber2.items.staffs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BambooBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.MelonBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;



public class ItemCustomStaffHarvest extends Item
{

	public ItemCustomStaffHarvest(Properties properties)
	{
		super(properties);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		Block block;
		boolean maxAge;
		
		if (!world.isRemote)
		{
			List<BlockPos> poslist = new ArrayList<BlockPos>();

			for (int x = -12; x <= 12; x++)
			{
				for (int y = -1; y <= 1; y++)
				{
					for (int z = -12; z <= 12; z++)
					{
						BlockPos pos = player.getPosition().add(x, y, z);
						block = world.getBlockState(pos).getBlock();
						
						if(block instanceof CropsBlock ||
								block instanceof CactusBlock ||
								block instanceof NetherWartBlock ||
								block instanceof SugarCaneBlock ||
								block instanceof BambooBlock ||
								block instanceof MelonBlock ||
								block instanceof CocoaBlock ||
								block instanceof PumpkinBlock)
						{
							poslist.add(player.getPosition().add(x, y, z));
						}
					}
				}
			}

			if (!poslist.isEmpty())
			{
				for (int i = 0; i <= poslist.size() - 1; i++)
				{
					BlockPos targetPos = poslist.get(i);
					BlockState state = world.getBlockState(targetPos);
					block = world.getBlockState(targetPos).getBlock();	
					BlockState defaultState = block.getDefaultState();
					
					//These plants are simply broken with drops
					if(block instanceof CocoaBlock ||
							block instanceof MelonBlock ||
							block instanceof PumpkinBlock ||
							block instanceof CactusBlock ||
							block instanceof SugarCaneBlock ||
							block instanceof NetherWartBlock ||
							block instanceof BambooBlock)
					{
						world.destroyBlock(targetPos, true);
					}
					
					//Crops are harvested, if at max age, and re-planted
					if(block instanceof CropsBlock)
					{
						maxAge = state.get(((CropsBlock) block).getAgeProperty()) >= ((CropsBlock) block).getMaxAge();
						
						if(maxAge)
						{
							world.destroyBlock(targetPos, true);
							world.setBlockState(targetPos, defaultState);	
						}
					}
				}
			}
			
		}
		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add((new TranslationTextComponent("item.gobber2.gobber2_staff_harvest.line1").applyTextStyle(TextFormatting.GREEN)));
		tooltip.add((new TranslationTextComponent("item.gobber2.gobber2_staff_harvest.line2").applyTextStyle(TextFormatting.GREEN)));
		tooltip.add((new TranslationTextComponent("item.gobber2.gobber2_staff_harvest.line3").applyTextStyle(TextFormatting.YELLOW)));
	}
}
