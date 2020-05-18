package szewek.fabricflux.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public final class FluxHelper {

	public static int exchangeWithItem(IFlux flux, ItemStack stack, int amount, boolean receive) {
		final Item item = stack.getItem();
		if (item instanceof IFluxContainer) {
			final Optional<IFlux> opt = ((IFluxContainer) item).getFluxFor(stack);
			return exchange(flux, opt, amount, receive);
		}
		return 0;
	}

	public static int exchangeWithBlockEntity(IFlux flux, BlockEntity blockEntity, Direction direction, int amount, boolean receive) {
		if (blockEntity instanceof IFluxContainer) {
			final Optional<IFlux> opt = ((IFluxContainer) blockEntity).getFluxFor(direction);
			return exchange(flux, opt, amount, receive);
		}
		return 0;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static int exchange(IFlux flux, Optional<IFlux> optFlux, int amount, boolean receive) {
		if (optFlux.isPresent()) {
			final IFlux flux2 = optFlux.get();
			return receive ? flux2.to(flux, amount) : flux.to(flux2, amount);
		}
		return 0;
	}

	private FluxHelper() {}
}
