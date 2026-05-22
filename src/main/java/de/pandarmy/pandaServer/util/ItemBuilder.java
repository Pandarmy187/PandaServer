package de.pandarmy.pandaServer.util;

import de.pandarmy.pandaServer.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private ItemMeta itemMeta;
    private ItemStack itemStack;


    public ItemBuilder(Material mat) {
        itemStack = new ItemStack(mat);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setItem(ItemStack localItem) {
        itemStack = localItem.clone(); // <- clone damit das Original nicht verändert wird
        itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().remove(new NamespacedKey(Main.getInstance(), "id"));
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setID(String id) {
        Main main = Main.getInstance();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, "id"), PersistentDataType.STRING, id);
        return this;
    }

    public ItemBuilder setCustomId(String key, String id) {
        Main main = Main.getInstance();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, key), PersistentDataType.STRING, id);
        return this;
    }

    public ItemBuilder setCustomIntId(String key, int id) {
        Main main = Main.getInstance();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, key), PersistentDataType.INTEGER, id);
        return this;
    }

    public ItemBuilder setDisplayname(String displayname) {
        itemMeta.displayName(Component.text(displayname));
        return this;
    }

    public ItemBuilder setLore(String... strings) {
        itemMeta.setLore(List.of(strings));
        return this;
    }

    public ItemBuilder setMultiLore(ArrayList<Component> componentArrayList) {
        itemMeta.lore(componentArrayList);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean s) {
        itemMeta.setUnbreakable(s);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... s) {
        itemMeta.addItemFlags(s);
        return this;
    }

    public ItemBuilder hideAllItemFlags() {
        itemMeta.addItemFlags(ItemFlag.values());
        return this;
    }

    public ItemBuilder addGlintOnly() {
        itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
        addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setHideTooltips(boolean s) {
        itemMeta.setHideTooltip(s);
        return this;
    }

    public ItemBuilder setPotionEffectTypeSplash(PotionEffectType potionEffectType, int duration, int aplifier) {

        if (itemMeta instanceof PotionMeta potionMeta) {
            potionMeta.clearCustomEffects();

            potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, aplifier), true);

            itemMeta = potionMeta;
        }

        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}