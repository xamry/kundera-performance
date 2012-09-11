/**
 * 
 */
package com.impetus.kundera;

import java.io.IOException;

import org.junit.Test;

import com.impetus.kundera.mongodb.perf.executor.MongoRunner;


/**
 * @author vivek.mishra
 *
 */
public class PerfTest {

	
	@Test
	public void testRun() throws IOException, InterruptedException
	{
		MongoRunner.main(null);
	}
}
