package com.prongbang.quartz.listener;

import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.prongbang.quartz.job.QuartzJob;

public class QuartzContextListener implements ServletContextListener {
	
	public static StdSchedulerFactory factory;
	
	public QuartzContextListener() {
		
		factory = new StdSchedulerFactory();
		
	}
	
	public void contextInitialized(ServletContextEvent servletContext) {
		
		try { 
			
			String jobName = "CronQuartzJob";
			String groupName = "Group";
			String triggerName = "TrigerName";
			String cronSchedule = "01 * * * * ?";	// every 1 minutes.
			
			// Setup Locale US
			Locale.setDefault(Locale.US);
			
			// Setup the Job class and the Job group
            JobDetail job = createJob(jobName, groupName, QuartzJob.class);

            // Create a Trigger every 1 minutes.
            Trigger trigger = createTrigger(triggerName, groupName, cronSchedule);
            
            // Setup the JobKey
            JobKey jobKey = new JobKey(jobName, groupName);

            // Setup Scheduler 
            Scheduler scheduler = factory.getScheduler();
            scheduler.start();
            
            // Delete Job
         	if(scheduler.checkExists(jobKey)) scheduler.deleteJob(jobKey);
            
            // Setup schedule jobs
            scheduler.scheduleJob(job, trigger);
			
		} catch(SchedulerException e){
			
		}
	}
	
	public void contextDestroyed(ServletContextEvent servletContext) {
		
		// Stop the scheduler now
		try {
			
			StdSchedulerFactory.getDefaultScheduler().shutdown();
			
		} catch (SchedulerException ex) {
			
			ex.printStackTrace();
		}
	}
	
	private JobDetail createJob(String jobName, String groupName, Class<? extends Job> T){
		
		return newJob(T).withIdentity(jobName, groupName).build();
	}
	
	private Trigger createTrigger(String triggerName, String groupName, String cronSchedule) {
		
		return newTrigger()
	            .withIdentity(triggerName, groupName)
	            .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
	            .build();
	}
	
}
