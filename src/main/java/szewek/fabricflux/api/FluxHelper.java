package szewek.fabricflux.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public final class FluxHelper {

	public static int exchangeWithItem(IFlux flux, ItemStack stack, int amount, boolean receive) {
		final Item item = stack.getItem();
		if (item instanceof IFluxContainer) {
			final IFlux fluxItem = ((IFluxContainer) item).getFluxFor(stack);
			return receive ? fluxItem.to(flux, amount) : flux.to(fluxItem, amount);
		}
		return 0;
	}

	public static int exchangeWithBlockEntity(IFlux flux, BlockEntity blockEntity, Direction direction, int amount, boolean receive) {
		if (blockEntity instanceof IFluxContainer) {
			final IFlux fluxBlockEntity = ((IFluxContainer) blockEntity).getFluxFor(direction);
			return receive ? fluxBlockEntity.to(flux, amount): flux.to(fluxBlockEntity, amount);
		}
		return 0;
	}

	private FluxHelper() {}
}
