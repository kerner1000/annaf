package de.mpg.mpiz.koeln.anna;

import java.util.ArrayList;

import de.kerner.commons.workflow.WorkFlow;
import de.kerner.commons.workflow.WorkFlowElement;
import de.kerner.commons.workflow.WorkFlowException;

public class ExecutionWorkflow implements WorkFlow {
	
	private final ArrayList<WorkFlowElement> elements = new ArrayList<WorkFlowElement>();
	
	public synchronized void addElement(WorkFlowElement e){
		elements.add(e);
	}

	public void work() throws WorkFlowException {
		for(WorkFlowElement e : elements){
			e.work();
		}
	}
}
