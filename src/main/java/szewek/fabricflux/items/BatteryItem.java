package szewek.fabricflux.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import szewek.fabricflux.api.IFlux;
import szewek.fabricflux.api.IFluxContainer;

import java.util.List;

public class BatteryItem extends Item implements IFluxContainer {
	public static final String FLUX = "Flux";
	public static final int FLUX_CAP = 100000;

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
		list.add(new TranslatableText("item.fabricflux.battery.charge", e, FLUX_CAP));
	}

	@Override
	public boolean useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		Flux f = new Flux(stack.getOrCreateTag());
		if (f.getFluxAmount() >= 50) {
			System.out.println("Extracted " + f.extractFlux(250, false));
			System.out.println("Energy " + f.getFluxAmount());
			Vec3d pos = entity.getPos();
			entity.onStruckByLightning(new LightningEntity(entity.world, pos.x, pos.y, pos.z, true));
			return true;
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player) {
		stack.getOrCreateTag().putInt(FLUX, 10000);
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
			return FLUX_CAP;
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
				final int r = FLUX_CAP - energy;
				if (n > r)
					n = r;
				if (!sim)
					tag.putInt(FLUX, energy + n);
			}
			return n;
		}
	}
}
