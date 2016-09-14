package de.upb.wdqa.wdvd.processors.controlflow;

import java.util.List;

import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public interface Reducer {
	
	public void reduce(List<RevisionProcessor> workerProcessors);

}
