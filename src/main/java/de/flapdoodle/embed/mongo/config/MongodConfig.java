/**
 * Copyright (C) 2011
 *   Michael Mosmann <michael@mosmann.de>
 *   Martin Jöhren <m.joehren@googlemail.com>
 *
 * with contributions from
 * 	konstantin-ba@github,Archimedes Trajano (trajano@github)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.embed.mongo.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import de.flapdoodle.embed.process.config.ExecutableProcessConfig;
import de.flapdoodle.embed.process.distribution.IVersion;
import de.flapdoodle.embed.process.runtime.Network;

/**
 *
 */
public class MongodConfig extends ExecutableProcessConfig {

	private final Net network;
	private final Storage storage;
	private final Timeout timeout;

	public MongodConfig(IVersion version) throws UnknownHostException, IOException {
		this(version, new Net(), new Storage(), new Timeout());
	}

	public MongodConfig(IVersion version, int port, boolean ipv6) {
		this(version, new Net(null, port, ipv6), new Storage(), new Timeout());
	}

	@Deprecated
	public MongodConfig(IVersion version, int port, boolean ipv6, String databaseDir) {
		this(version, new Net(null, port, ipv6), new Storage(databaseDir, null, 0), new Timeout());
	}

	@Deprecated
	public MongodConfig(IVersion version, String bindIp, int port, boolean ipv6, String databaseDir, String replSetName,
			int oplogSize) {
		this(version, new Net(bindIp, port, ipv6), new Storage(databaseDir, replSetName, oplogSize), new Timeout());
	}

	public MongodConfig(IVersion version, Net network, Storage storage, Timeout timeout) {
		super(version);
		this.network = network;
		this.storage = storage;
		this.timeout = timeout;
	}

	public Net net() {
		return network;
	}

	public Storage replication() {
		return storage;
	}

	public Timeout timeout() {
		return timeout;
	}

	/**
	 * @see MongodConfig#net()
	 * @return
	 */
	@Deprecated
	public String getBindIp() {
		return network.getBindIp();
	}

	/**
	 * @see MongodConfig#net()
	 * @return
	 */
	@Deprecated
	public int getPort() {
		return network.getPort();
	}

	/**
	 * @see MongodConfig#net()
	 * @return
	 */
	@Deprecated
	public boolean isIpv6() {
		return network.isIpv6();
	}

	public static class Storage {

		private final int oplogSize;
		private final String replSetName;
		private final String databaseDir;

		public Storage() {
			this(null, null, 0);
		}

		public Storage(String databaseDir, String replSetName, int oplogSize) {
			this.databaseDir = databaseDir;
			this.replSetName = replSetName;
			this.oplogSize = oplogSize;
		}

		public int getOplogSize() {
			return oplogSize;
		}

		public String getReplSetName() {
			return replSetName;
		}

		public String getDatabaseDir() {
			return databaseDir;
		}

	}

	public static class Net {

		private final String bindIp;
		private final int port;
		private final boolean ipv6;

		public Net() throws UnknownHostException, IOException {
			this(null, Network.getFreeServerPort(), Network.localhostIsIPv6());
		}

		public Net(int port, boolean ipv6) {
			this(null, port, ipv6);
		}

		public Net(String bindIp, int port, boolean ipv6) {
			this.bindIp = bindIp;
			this.port = port;
			this.ipv6 = ipv6;
		}

		public String getBindIp() {
			return bindIp;
		}

		public int getPort() {
			return port;
		}

		public boolean isIpv6() {
			return ipv6;
		}

		public InetAddress getServerAddress() throws UnknownHostException {
			if (bindIp != null) {
				return InetAddress.getByName(bindIp);
			}
			return Network.getLocalHost();
		}
	}

	public static class Timeout {

		private final long startupTimeout;

		public Timeout() {
			this(20000);
		}

		public Timeout(long startupTimeout) {
			this.startupTimeout = startupTimeout;
		}

		public long getStartupTimeout() {
			return startupTimeout;
		}
	}
}
