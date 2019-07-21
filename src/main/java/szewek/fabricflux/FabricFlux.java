package szewek.fabricflux;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import szewek.fabricflux.items.BatteryItem;

public class FabricFlux implements ModInitializer {
	public static final Item ITEM_BATTERY = new BatteryItem(new Item.Settings().maxCount(1).maxDamage(50000).group(ItemGroup.MISC));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("fabricflux", "battery"), ITEM_BATTERY);
		System.out.println("Fabric-Flux");
	}
}
