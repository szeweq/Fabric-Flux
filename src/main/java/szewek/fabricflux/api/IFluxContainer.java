package szewek.fabricflux.api;

import java.util.Optional;

/**
 * Interface for Flux containers (like items, block entities, etc.)
 */
public interface IFluxContainer {
	/**
	 * Gets a Flux interface from a container
	 * @param that Object (like {@link net.minecraft.item.ItemStack ItemStack} for items
	 *              or {@link net.minecraft.util.math.Direction Direction} for block entities)
	 * @return Flux interface (this should be not stored outside the container)
	 */
	Optional<IFlux> getFluxFor(Object that);
}
