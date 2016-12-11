package de.flapdoodle.embed.mongo.config;

import com.google.common.collect.ImmutableMap;
import de.flapdoodle.embed.process.io.directories.UserHome;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class DownloadConfigBuilderTest extends DownloadConfigBuilder {
	private final String description;
	private final String expectedDirectory;

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		String defaultArtifactDownloadLocation = new UserHome(".embedmongo").asFile().getAbsolutePath();
		return Arrays.asList(new Object[][] {
				{ "Use home directory when environment variable not present", defaultArtifactDownloadLocation, Optional.empty()},
				{ "Environment variable overrides default when supplied", "/some/explicit/directory", Optional.of("/some/explicit/directory") },
				{ "Environment variable overrides default when supplied", "/another/explicit/directory", Optional.of("/another/explicit/directory") },
		});
	}

	public DownloadConfigBuilderTest(String description, String expectedDirectory, Optional<String> artifactDownloadLocationEnvironmentVariable) {
		super(artifactDownloadLocationEnvironmentVariable);
		this.description = description;
		this.expectedDirectory = expectedDirectory;
	}

	@Test
	public void artifactStorePathChosenProperly() {
		defaults();
		Assert.assertEquals(description, expectedDirectory, this.artifactStorePath().get().asFile().getAbsolutePath());
	}
}
