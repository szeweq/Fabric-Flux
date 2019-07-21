package szewek.fabricflux.api;


/**
 * Interface for energy (Flux) storage and transfer
 */
public interface IFlux {
	/**
	 * Returns current amount of Flux
	 * @return Stored Flux
	 */
	int getFluxAmount();

	/**
	 * Returns current capacity of Flux
	 * @return Flux capacity
	 */
	int getFluxCapacity();

	/**
	 * Output Flux method
	 * @param n Maximum amount of Flux to be extracted
	 * @param sim Simulation switch. Set to {@code false} only when you need to transfer Flux.
	 * @return Amount of Flux extracted or {@code -1} if output is not acceptable
	 */
	int extractFlux(int n, boolean sim);

	/**
	 * Input Flux method
	 * @param n Amount of Flux ready to be received
	 * @param sim Simulation switch. Set to {@code false} only when you need to transfer Flux.
	 * @return Amount of Flux extracted of {@code -1} if input is not acceptable
	 */
	int receiveFlux(int n, boolean sim);

	/**
	 * Whole energy transfer in a single method.
	 * @param dest Object to be charged with Flux
	 * @param amount Maximum amount of Flux that can be transferred
	 * @return Transferred amount of Flux. Objects that should not have their Flux extracted should return {@code 0}
	 */
	default int to(IFlux dest, final int amount) {
		if (amount > 0) {
			int sim = extractFlux(amount, true);
			if (sim > 0) {
				sim = dest.receiveFlux(sim, true);
				if (sim > 0) {
					return dest.receiveFlux(extractFlux(sim, false), false);
				}
			}
		}
		return 0;
	}
}
