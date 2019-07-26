package szewek.fabricflux.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import szewek.fabricflux.api.IFlux;
import szewek.fabricflux.api.IFluxContainer;

import java.util.List;

public class BatteryItem extends Item implements IFluxContainer {
	private static final String FLUX = "Flux";

	public BatteryItem(Settings settings) {
		super(settings);
	}

	@Override @Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext tooltipContext) {
		int e = 0;
		CompoundTag tag = itemStack.getTag();
		if (tag != null) {
			e = tag.getInt(FLUX);
		}
		list.add(new TranslatableText("item.fabricflux.battery.charge", e, 50000));
	}

	@Override
	public IFlux getFluxFor(Object that) {
		if (that instanceof ItemStack) {
			return new Flux(((ItemStack) that).getOrCreateTag());
		}
		return null;
	}

	static class Flux implements IFlux {
		CompoundTag tag;

		Flux(CompoundTag tag) {
			this.tag = tag;
		}

		@Override
		public int getFluxAmount() {
			return tag.getInt(FLUX);
		}

		@Override
		public int getFluxCapacity() {
			return 50000;
		}

		@Override
		public int extractFlux(int n, boolean sim) {
			int energy = tag.getInt(FLUX);
			if (n > 0) {
				if (n > energy)
					n = energy;
				if (!sim)
					tag.putInt(FLUX, energy - n);
			}
			return n;
		}

		@Override
		public int receiveFlux(int n, boolean sim) {
			int energy = tag.getInt(FLUX);
			if (n > 0) {
				final int r = 50000 - energy;
				if (n > r)
					n = r;
				if (!sim)
					tag.putInt(FLUX, energy + n);
			}
			return n;
		}
	}
}
