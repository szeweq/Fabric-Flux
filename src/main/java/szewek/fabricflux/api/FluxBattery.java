package szewek.fabricflux.api;

public class FluxBattery implements IFlux {
	protected int energy = 0;
	protected final int maxEnergy;

	public FluxBattery() {
		this(50000);
	}

	public FluxBattery(int max) {
		maxEnergy = max;
	}

	@Override
	public int getFluxAmount() {
		return energy;
	}

	public void setFluxAmount(int flux) {
		energy = flux;
	}

	@Override
	public int getFluxCapacity() {
		return maxEnergy;
	}

	@Override
	public int extractFlux(int n, boolean sim) {
		if (n > 0) {
			if (n > energy)
				n = energy;
			if (!sim)
				energy -= n;
		}
		return n;
	}

	@Override
	public int receiveFlux(int n, boolean sim) {
		if (n > 0) {
			final int r = maxEnergy - energy;
			if (n > r)
				n = r;
			if (!sim)
				energy += n;
		}
		return n;
	}
}
