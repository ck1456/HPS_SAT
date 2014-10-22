package hps.nyu.fa14;

public interface ISolutionMonitor {

	/**
	 * Notify a montor that an new (better) assignment has been produced
	 * @param a
	 */
	void updateAssignment(Assignment a);
}
