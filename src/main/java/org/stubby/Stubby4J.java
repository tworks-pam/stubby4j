/*
A Java-based HTTP stub server

Copyright (C) 2012 Alexander Zagniotov, Isa Goksu and Eric Mrak

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.stubby;

import org.apache.commons.cli.ParseException;
import org.stubby.cli.CommandLineIntepreter;
import org.stubby.server.JettyOrchestrator;
import org.stubby.server.JettyOrchestratorFactory;

import java.io.IOException;
import java.util.Map;

public final class Stubby4J {

   public static final String KEY_STATUS = "status";
   public static final String KEY_RESPONSE = "response";
   public static final String KEY_RESPONSE_HEADERS = "responseHeaders";

   private Stubby4JApiExposer stubby4JApiExposer;
   private Stubby4jMocker stubby4jMocker;

   public Stubby4J() {
      stubby4jMocker = new Stubby4jMocker();
   }

   public Stubby4J(final String yamlConfigurationFilename) {
      stubby4JApiExposer = new Stubby4JApiExposer(yamlConfigurationFilename);
   }

   public void start() throws Exception {
      stubby4JApiExposer.start(JettyOrchestrator.DEFAULT_CLIENT_PORT, JettyOrchestrator.DEFAULT_ADMIN_PORT);
   }

   public void start(final int clientPort, final int adminPort) throws Exception {
      stubby4JApiExposer.start(clientPort, adminPort);
   }

   public void stop() throws Exception {
      stubby4JApiExposer.stop();
   }

   public Map<String, String> doGetOnURI(final String uri) throws IOException {
      return stubby4JApiExposer.doGetOnURI(uri);
   }

   public Map<String, String> doPostOnURI(final String uri, final String postData) throws IOException {
      return stubby4JApiExposer.doPostOnURI(uri, postData);
   }

   public Stubby4J whenRequest() {
      stubby4jMocker.whenRequest();
      return this;
   }

   public Stubby4J thenResponse() {
      stubby4jMocker.thenResponse();
      return this;
   }

   public Stubby4J hasMethod(final String method) {
      stubby4jMocker.hasMethod(method);
      return this;
   }

   public Stubby4J hasUri(final String uri) {
      stubby4jMocker.hasUri(uri);
      return this;
   }

   public Stubby4J hasPostBody(final String postBody) {
      stubby4jMocker.hasPostBody(postBody);
      return this;
   }

   public Stubby4J withStatus(final String status) {
      stubby4jMocker.withStatus(status);
      return this;
   }

   public Stubby4J withBody(final String body) {
      stubby4jMocker.withBody(body);
      return this;
   }

   public Stubby4J withHeader(final String header, final String value) {
      stubby4jMocker.withHeader(header, value);
      return this;
   }

   public void configure() {
      stubby4jMocker.configure();
   }

   public void clearMocks() {
      stubby4jMocker.clear();
   }

   public Map<String, String> simulateGetOnURI(final String uri) throws IOException {
      return stubby4jMocker.simulateGetOnURI(uri);
   }

   public Map<String, String> simulatePostOnURI(final String uri, final String postData) throws IOException {
      return stubby4jMocker.simulatePostOnURI(uri, postData);
   }

   public static void main(final String[] args) {

      try {
         CommandLineIntepreter.parseCommandLine(args);
      } catch (ParseException e) {
         e.printStackTrace();
         System.exit(1);
      }
      if (CommandLineIntepreter.isHelp()) {
         CommandLineIntepreter.printHelp(Stubby4J.class);

      } else if (!CommandLineIntepreter.isYamlProvided()) {
         System.err.println("\n\nYAML configuration was not provided using command line option '-f' or '--config'.\nPlease run again with option '--help'\n\n");
         System.exit(1);

      } else {

         try {
            final Map<String, String> commandLineArgs = CommandLineIntepreter.getCommandlineParams();
            final String yamlConfigFilename = commandLineArgs.get(CommandLineIntepreter.OPTION_CONFIG);

            JettyOrchestratorFactory.getInstance(yamlConfigFilename, commandLineArgs).startJetty();

         } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
         }
      }
   }
}