package net.epoxide.bladecraft;

import java.util.Arrays;

import net.epoxide.bladecraft.command.CommandDye;
import net.epoxide.bladecraft.handler.ConfigurationHandler;
import net.epoxide.bladecraft.proxy.ProxyCommon;
import net.epoxide.bladecraft.util.Reference;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.FACTORY)
public class Bladecraft {

    @SidedProxy(serverSide = Reference.PROXY_COMMON, clientSide = Reference.PROXY_CLIENT)
    public static ProxyCommon proxy;

    @Mod.Instance(Reference.MOD_ID)
    public static Bladecraft instance;

    public static SimpleNetworkWrapper networkChannels;
        
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) 
    {
        setModMeta(event.getModMetadata());
        new ConfigurationHandler(event.getSuggestedConfigurationFile());
        proxy.registerSidededEvents();
        
        TileEntity.addMapping(net.epoxide.bladecraft.tileentity.TileEntityForge.class, "BladeCraftForge");
        TileEntity.addMapping(net.epoxide.bladecraft.tileentity.TileEntityMixer.class, "BladeCraftMixer");
         
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        networkChannels = NetworkRegistry.INSTANCE.newSimpleChannel("BC|NetworkChannel");
    }
    
    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) 
    {
        ServerCommandManager manager = (ServerCommandManager) event.getServer().getCommandManager();
        manager.registerCommand(new CommandDye());
    }


    private void setModMeta(ModMetadata meta) 
    {
        // TODO add more details
        meta.name = "Bladecraft";
        meta.modId = Reference.MOD_ID;
        meta.version = Reference.VERSION;
        meta.authorList = Arrays.asList("Ghostrec35", "Darkhax", "Thisguy1045");
        meta.autogenerated = false;
        meta.description = "A mod that provides the ability to color swords.";
        meta.logoFile = "";
        meta.url = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1288864-1-5-2-forge-bladecraft-ssp-smp-lan";
    }
    
    @EventHandler
    public void onIMCMessage(FMLInterModComms.IMCEvent event)
    {
        for(FMLInterModComms.IMCMessage message : event.getMessages())
        {
            if(message.isItemStackMessage())
            {
                ItemStack stack = message.getItemStackValue();
                if(stack.hasTagCompound())
                {
                    if(isValidMessage(stack))
                    {
                        proxy.addIconRegistration(stack);
                    }
                }
            }
        }
    }

    private boolean isValidMessage(ItemStack stack)
    {
        return stack.getItem() != null && stack.stackTagCompound.hasKey("bladeIconLoc") && stack.stackTagCompound.hasKey("insetIconLoc") && stack.stackTagCompound.hasKey("hiltIconLoc");
    }
}