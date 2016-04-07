/**
 * Copyright (C) 2011
 * Can Yaman <can@yaman.me>
 * Michael Mosmann <michael@mosmann.de>
 * Martin Jöhren <m.joehren@googlemail.com>
 * <p/>
 * with contributions from
 * konstantin-ba@github,Archimedes Trajano	(trajano@github)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.embed.mongo;

import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.runtime.Network;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MongoRestoreExecutableTest extends TestCase {

   private static final Logger _logger = LoggerFactory.getLogger(MongoRestoreExecutableTest.class.getName());

   @Test
   public void testStartMongoRestore() throws IOException, InterruptedException {

      int serverPort = Network.getFreeServerPort();

      IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION).net(new Net(serverPort, Network.localhostIsIPv6())).build();

      IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder().defaults(Command.MongoD).build();

      MongodExecutable mongodExe = MongodStarter.getInstance(runtimeConfig).prepare(mongodConfig);
      MongodProcess mongod = mongodExe.start();

      String jsonFile = Thread.currentThread().getContextClassLoader().getResource("dump").getFile();
      MongoRestoreExecutable mongoRestoreExecutable = mongoRestoreExecutable(serverPort, jsonFile, true);
      MongoRestoreProcess mongoRestoreProcess = null;
      Boolean dataRestored = false;
      try {
         mongoRestoreProcess = mongoRestoreExecutable.start();
         dataRestored = true;

      } catch (Exception e) {
         _logger.info("MongoRestore exception: {}", e.getStackTrace());
         dataRestored = false;
      } finally {
         Assert.assertTrue("mongoDB restore data in json format", dataRestored);
         mongoRestoreProcess.stop();
      }

      mongod.stop();
      mongodExe.stop();
   }

   private MongoRestoreExecutable mongoRestoreExecutable(int port, String dumpLocation, Boolean drop) throws IOException {
      IMongoRestoreConfig mongoRestoreConfig = new MongoRestoreConfigBuilder()
         .version(Version.Main.PRODUCTION)
         .net(new Net(port, Network.localhostIsIPv6()))
         .dropCollection(drop)
         .dir(dumpLocation)
         .build();

      return MongoRestoreStarter.getDefaultInstance().prepare(mongoRestoreConfig);
   }
}
