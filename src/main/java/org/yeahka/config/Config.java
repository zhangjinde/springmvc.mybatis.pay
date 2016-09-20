package org.yeahka.config;
import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;


public class Config {

	private static Configuration config;

	public static Configuration getConfiguration()
	{
		return config;
	}

	static {

		Configurations configs = new Configurations();
			try
			{
				 config = configs.properties(new File("pay-config.properties"));
			}
			catch (ConfigurationException cex)
			{
			    // Something went wrong
				cex.printStackTrace();
			}

	}
}
