package com.prongbang.quartz.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(new Date());
	}
	
}
