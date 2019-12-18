package szewek.fabricflux;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import szewek.fabricflux.items.BatteryItem;

public class FabricFlux implements ModInitializer {
	public static final Item ITEM_BATTERY = new BatteryItem(new Item.Settings().maxCount(1));

	public static final ItemGroup FF_ITEMS = FabricItemGroupBuilder.create(new Identifier("fabricflux", "ffitems"))
			.icon(() -> new ItemStack(ITEM_BATTERY))
			.appendItems(stacks -> {
				ItemStack stack = new ItemStack(ITEM_BATTERY);
				stack.getOrCreateTag().putInt(BatteryItem.FLUX, BatteryItem.FLUX_CAP);
				stacks.add(stack);
			})
			.build();


	@Override
	public void onInitialize() {
		System.out.println("Loading Fabric-Flux...");
		Registry.register(Registry.ITEM, new Identifier("fabricflux", "battery"), ITEM_BATTERY);
	}
}
