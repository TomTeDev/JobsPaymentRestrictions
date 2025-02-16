package more.mucho.jobspaymentrestrictions.listeners;

import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import more.mucho.jobspaymentrestrictions.JobsPaymentRestrictions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class JobsListener implements Listener{
    private final JobsPaymentRestrictions plugin;
    private HashMap<String,Long> lastMessageTimeMap = new HashMap<>();
    public JobsListener(JobsPaymentRestrictions plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPayment(JobsPrePaymentEvent event){
        OfflinePlayer offlinePlayer = event.getPlayer();
        if(offlinePlayer == null||!offlinePlayer.isOnline())return;
        Player player = offlinePlayer.getPlayer();
        if(player == null)return;
        ItemStack tool = player.getInventory().getItemInMainHand();
        if(!isToolRestricted(tool))return;
        event.setCancelled(true);
        sendMessage(player);
    }
    private boolean isToolRestricted(ItemStack tool){
        if(tool == null||tool.getType().isAir())return false;
        ItemMeta meta = tool.getItemMeta();
        if(meta == null)return false;
        for(String restrictedEnchantment : plugin.getConfig().getStringList("blocked-enchants")){
            String[] restrictedEnchantmentSplit = restrictedEnchantment.split(":");
            Enchantment enchantment = Enchantment.getByName(restrictedEnchantmentSplit[0]);
            if(enchantment == null)continue;
            if(!meta.hasEnchant(enchantment))continue;
            int level = -1;
            try {
                level = Integer.parseInt(restrictedEnchantmentSplit[1]);
            }catch (Exception ignored){}
            if(level<0)return true;
            if(meta.getEnchantLevel(enchantment)>=level)return true;
        }
        return false;
    }
    private void sendMessage(Player player){
        long lastTimeSent = lastMessageTimeMap.getOrDefault(player.getName(),0L);
        long currentTime = System.currentTimeMillis();
        if(currentTime-lastTimeSent<60*1000L)return;
        lastMessageTimeMap.put(player.getName(),currentTime);
        String message = plugin.getConfig().getString("messages.wrong_tool_notification");
        if(message!=null&&!message.isEmpty()){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
        }
    }
}
