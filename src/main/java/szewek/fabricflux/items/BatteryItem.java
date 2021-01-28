package szewek.fabricflux.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import szewek.fabricflux.api.IFlux;
import szewek.fabricflux.api.IFluxContainer;

import java.util.List;
import java.util.Optional;

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
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		Flux f = new Flux(stack.getOrCreateTag());
		if (f.getFluxAmount() >= 50) {
			if (!entity.world.isClient) {
				Vec3d pos = entity.getPos();
				LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, entity.world);
				lightning.setPos(pos.x, pos.y, pos.z);
				entity.onStruckByLightning((ServerWorld) entity.world, lightning);
				if (!user.isCreative()) f.extractFlux(50, false);
			}
			return ActionResult.SUCCESS;
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player) {
		stack.getOrCreateTag().putInt(FLUX, 10000);
	}

	@Override
	public Optional<IFlux> getFluxFor(Object that) {
		if (that instanceof ItemStack) {
			return Optional.of(new Flux(((ItemStack) that).getOrCreateTag()));
		}
		return Optional.empty();
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
