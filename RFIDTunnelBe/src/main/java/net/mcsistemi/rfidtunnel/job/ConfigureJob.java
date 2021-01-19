package net.mcsistemi.rfidtunnel.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureJob {

	@Bean
	public JobDetail jobTunnelDetails() {
		return JobBuilder.newJob(TunnelJob.class).withIdentity("TunnelJob").storeDurably().build();
	}

	@Bean
	public Trigger jobTunnelTrigger(JobDetail jobTunnelDetails) {

		return TriggerBuilder.newTrigger().forJob(jobTunnelDetails)

				.withIdentity("TunnelTrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule()
				// .withIntervalInSeconds(5)
				// .repeatForever()
				).build();

	}

}
