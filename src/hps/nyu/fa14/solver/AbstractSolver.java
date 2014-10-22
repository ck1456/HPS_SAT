package hps.nyu.fa14.solver;

import hps.nyu.fa14.Assignment;
import hps.nyu.fa14.Formula;
import hps.nyu.fa14.ISolutionMonitor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSolver implements ISolver {

	@Override
	public abstract Assignment solve(Formula f);

	protected List<ISolutionMonitor> monitors = new ArrayList<ISolutionMonitor>();
	
	@Override
	public void addSolutionMonitor(ISolutionMonitor monitor) {
		monitors.add(monitor);
	}

	protected void notifyNewAssignment(Assignment a){
		for(ISolutionMonitor m : monitors){
			m.updateAssignment(a);
		}
	}
	
}
