package net.epoxide.bladecraft.block;

import net.epoxide.bladecraft.Bladecraft;
import net.epoxide.bladecraft.tileentity.TileEntityForge;
import net.epoxide.bladecraft.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockForge extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    public IIcon forgeSideIcon;

    @SideOnly(Side.CLIENT)
    private IIcon forgeBottomIcon;

    public BlockForge()
    {
        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {   
        return new TileEntityForge();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        player.openGui(Bladecraft.instance(), Reference.FORGE_GUI_ID, world, x, y, z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 1)
            return this.blockIcon;
        if (side == 0)
            return this.forgeBottomIcon;
        return this.forgeSideIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        this.forgeBottomIcon = register.registerIcon(Reference.MOD_ID + ":blockForgeBottom");
        this.forgeSideIcon = register.registerIcon(Reference.MOD_ID + ":blockForgeSide");
        this.blockIcon = register.registerIcon(Reference.MOD_ID + ":blockForge");
    }
    
    public void breakBlock(World world, int x, int y, int z, Block block, int tbd)
    {
        TileEntityForge forgeTE = (TileEntityForge) world.getTileEntity(x, y, z);
        for(int slotInd = 0; slotInd < forgeTE.getSizeInventory(); slotInd++)
        {
            ItemStack stack = forgeTE.getStackInSlot(slotInd);
            if(stack != null && stack.getItem() != null)
            {
                EntityItem entityItem = new EntityItem(world, x, y, z, stack);
                entityItem.motionX += world.rand.nextGaussian() * 1F;
                entityItem.motionY += world.rand.nextGaussian() * 1F;
                entityItem.motionZ += world.rand.nextGaussian() * 1F;
                world.spawnEntityInWorld(entityItem);
            }
        }
        super.breakBlock(world, x, y, z, block, tbd);
    }
}
