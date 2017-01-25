/**
 * Copyright (C) 2011
 *   Michael Mosmann <michael@mosmann.de>
 *   Martin Jöhren <m.joehren@googlemail.com>
 *
 * with contributions from
 * 	konstantin-ba@github,Archimedes Trajano	(trajano@github)
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
package de.flapdoodle.embed.mongo.examples;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Ignore;
import org.junit.Test;

import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.MongosExecutable;
import de.flapdoodle.embed.mongo.MongosProcess;
import de.flapdoodle.embed.mongo.MongosStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.IMongosConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.MongosConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;


public class StartConfigAndMongoDBServerTest {

	/*
	 // ->
	 this is an very easy example to use mongos and mongod
	 // <- 
	 */
	@Test
	@Ignore("does not work anymore")
	public void startAndStopMongosAndMongod() throws UnknownHostException, IOException {
			// ->
		int port = Network.getFreeServerPort();
		int mongodPort1 = port+1;
		int mongodPort2 = port+2;
		
		String defaultHost = "localhost";

		MongodProcess mongod1 = startMongod(mongodPort1);
		MongodProcess mongod2 = startMongod(mongodPort2);

		try {
			MongosProcess mongos = startMongos(port, defaultHost, mongodPort1, defaultHost, mongodPort2);
			try {
				MongoClient mongoClient = new MongoClient(defaultHost, port);
				System.out.println("DB Names: " + mongoClient.getDatabaseNames());
			} finally {
				mongos.stop();
			}
		} finally {
			mongod1.stop();
			mongod2.stop();
		}
			// <-
	}
	
	// ->
	private MongosProcess startMongos(int port, String host1, int port1, String host2, int port2) throws UnknownHostException,
			IOException {
		IMongosConfig mongosConfig = new MongosConfigBuilder()
			.version(Version.Main.PRODUCTION)
			.net(new Net(port, Network.localhostIsIPv6()))
			.configDB(host1 + ":" + port1)
			.replicaSet("testRepSet")
			.build();

		MongosExecutable mongosExecutable = MongosStarter.getDefaultInstance().prepare(mongosConfig);
		MongosProcess mongos = mongosExecutable.start();
		return mongos;
	}

	private MongodProcess startMongod(int port) throws UnknownHostException, IOException {
		IMongodConfig mongoConfigConfig = new MongodConfigBuilder()
			.version(Version.Main.PRODUCTION)
			.net(new Net(port, Network.localhostIsIPv6()))
//			.replication(new Storage(null, "testRepSet", 0))
			.build();

		MongodExecutable mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongoConfigConfig);
		MongodProcess mongod = mongodExecutable.start();
		return mongod;
	}
	// <-
}
