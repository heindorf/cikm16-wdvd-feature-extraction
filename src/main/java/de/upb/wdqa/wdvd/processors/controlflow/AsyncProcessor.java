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

package de.upb.wdqa.wdvd.processors.controlflow;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.processors.RevisionProcessor;

public class AsyncProcessor implements RevisionProcessor {
	
	final Logger logger;
	
	BlockingQueue<Revision> queue;
	
	private static final Revision DONE = new Revision();
	
	RevisionProcessor processor;
	
	Thread thread;
	
	String name;
	
	public AsyncProcessor(
			RevisionProcessor processor, String name, int bufferSize){
		
		logger = LoggerFactory.getLogger(AsyncProcessor.class + "(" + name +")");
		
		this.processor = processor;
		this.name = name;
		queue = new ArrayBlockingQueue<>(bufferSize);
	}

	@Override
	public void startRevisionProcessing() {
		logger.debug("Starting...");
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					Revision revision = queue.take();
					
					processor.startRevisionProcessing();
					
					while(revision != DONE){
						processor.processRevision(revision);
						revision = queue.take();
					}
						
					processor.finishRevisionProcessing();
					
				} catch (Throwable t) {
					// This should never happen
					logger.error("", t);
				}				
			}
		};
		
		thread = new Thread(runnable, "Async Revision Processor (" + name + ")");
		thread.setPriority(3);
		thread.start();
		
	}

	@Override
	public void processRevision(Revision revision) {
		// copy revision and add to queue
		try {
			queue.put(revision);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		
	}
	
	@Override
	public void finishRevisionProcessing() {
		logger.debug("Starting to finish...");
		
		// Notify the thread to stop
		try {
			queue.put(DONE);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		
		// Wait for the thread to have stopped
		try {
			thread.join();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		logger.debug("Finished.");
	}
}
