/*
 * Wikidata Vandalism Detector 2016 (WDVD-2016)
 * 
 * Copyright (c) 2016 Stefan Heindorf, Martin Potthast, Benno Stein, Gregor Engels
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.upb.wdqa.wdvd.processors.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;
import de.upb.wdqa.wdvd.processors.controlflow.ParallelProcessor;
import de.upb.wdqa.wdvd.test.TestProcessor;
import de.upb.wdqa.wdvd.test.TestUtils;

public class ParallelProcessorTest{
	
	@BeforeClass
	public static void SetUp(){
		TestUtils.initializeLogger();
	}	
	
	@Test
	public void testParallelProcessor(){
		List<Revision> revisionsBefore= new ArrayList<Revision>();
		
		TestProcessor workProcessor1 = new TestProcessor();
		TestProcessor workProcessor2 = new TestProcessor();
		TestProcessor workProcessor3 = new TestProcessor();
		
		List<RevisionProcessor> list = new ArrayList<RevisionProcessor>();
		list.add(workProcessor1);
		list.add(workProcessor2);
		list.add(workProcessor3);
		
		TestProcessor nextProcessor = new TestProcessor();
		ParallelProcessor parallelProcessor =
				new ParallelProcessor(list, null, nextProcessor, "test");
		
		final int REVISION_NUMBER = 10 * parallelProcessor.MAX_QUEUE_SIZE;
		
		for (int i = 0; i < REVISION_NUMBER; i++){
			revisionsBefore.add(new Revision());
		}
		
		parallelProcessor.startRevisionProcessing();
		
		for(Revision revision: revisionsBefore){
			parallelProcessor.processRevision(revision);
		}
		
		parallelProcessor.finishRevisionProcessing();
		
		List<Revision> revisionsAfter = nextProcessor.getRevisions();
		
		Assert.assertArrayEquals(revisionsBefore.toArray(), revisionsAfter.toArray());
		
		Assert.assertEquals(1, workProcessor3.getStartRevisionProcessingCount());		
		Assert.assertEquals(1, workProcessor3.getFinishRevisionProcessingCount());
		
		Assert.assertEquals(1, nextProcessor.getStartRevisionProcessingCount());		
		Assert.assertEquals(REVISION_NUMBER, nextProcessor.getProcessRevisionProcessingCount());		
		Assert.assertEquals(1, nextProcessor.getFinishRevisionProcessingCount());

	}
	
	@Test
	public void testParallelProcessor2(){
		// try to find non-deterministic errors that only occur sometimes
		for (int i = 0; i < 10; i++){
			testParallelProcessor();
		}
	}
}

