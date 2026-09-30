/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.shared.mapping;

import org.apache.maven.api.Artifact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the mapping of file names.
 *
 * @author Stephane Nicoll
 */
class MappingUtilsTest {

    private static Artifact artifact(String classifier) {
        return new StubArtifact("org.apache.sample", "maven-test-lib", "1.0", "1.0", classifier, "jar");
    }

    @Test
    void completeMapping() throws Exception {
        Artifact jar = artifact(null);
        assertEquals(
                "maven-test-lib-1.0.jar",
                MappingUtils.evaluateFileNameMapping("@{artifactId}@-@{version}@.@{extension}@", jar));
    }

    @Test
    void noVersionMapping() throws Exception {
        Artifact jar = artifact(null);
        assertEquals("maven-test-lib.jar", MappingUtils.evaluateFileNameMapping("@{artifactId}@.@{extension}@", jar));
    }

    @Test
    void mappingWithGroupId() throws Exception {
        Artifact jar = artifact(null);
        assertEquals(
                "org.apache.sample-maven-test-lib-1.0.jar",
                MappingUtils.evaluateFileNameMapping("@{groupId}@-@{artifactId}@-@{version}@.@{extension}@", jar));
    }

    @Test
    void mappingWithClassifier() throws Exception {
        Artifact jar = artifact("classifier");
        assertEquals(
                "maven-test-lib-1.0-classifier.jar",
                MappingUtils.evaluateFileNameMapping(MappingUtils.DEFAULT_FILE_NAME_MAPPING_CLASSIFIER, jar));
    }

    @Test
    void mappingWithNullClassifier() throws Exception {
        Artifact jar = artifact(null);
        assertEquals(
                "maven-test-lib-1.0.jar",
                MappingUtils.evaluateFileNameMapping(MappingUtils.DEFAULT_FILE_NAME_MAPPING_CLASSIFIER, jar));
    }

    /**
     * Test for #65. When classifier is null, the default classifier mapping should
     * produce a clean filename without a trailing dash.
     */
    @Test
    void mappingWithNullClassifierShouldNotHaveTrailingDash() throws Exception {
        Artifact jar = artifact(null);
        assertEquals(
                "maven-test-lib-1.0.jar",
                MappingUtils.evaluateFileNameMapping(MappingUtils.DEFAULT_FILE_NAME_MAPPING_CLASSIFIER, jar));
    }

    @Test
    void mappingWithEmptyClassifierShouldNotHaveTrailingDash() throws Exception {
        Artifact jar = artifact("");
        assertEquals(
                "maven-test-lib-1.0.jar",
                MappingUtils.evaluateFileNameMapping(MappingUtils.DEFAULT_FILE_NAME_MAPPING_CLASSIFIER, jar));
    }

    /**
     * Test for MWAR-212.
     */
    @Test
    void mappingWithOptionalClassifier() throws Exception {
        final String mappingWithOptionalClassifier1 = "@{artifactId}@-@{version}@@{dashClassifier}@.@{extension}@";
        final String mappingWithOptionalClassifier2 = "@{artifactId}@-@{version}@@{dashClassifier?}@.@{extension}@";

        Artifact jar = artifact(null);
        assertEquals(
                "maven-test-lib-1.0.jar", MappingUtils.evaluateFileNameMapping(mappingWithOptionalClassifier1, jar));
        assertEquals(
                "maven-test-lib-1.0.jar", MappingUtils.evaluateFileNameMapping(mappingWithOptionalClassifier2, jar));

        jar = artifact("classifier");
        assertEquals(
                "maven-test-lib-1.0-classifier.jar",
                MappingUtils.evaluateFileNameMapping(mappingWithOptionalClassifier1, jar));
        assertEquals(
                "maven-test-lib-1.0-classifier.jar",
                MappingUtils.evaluateFileNameMapping(mappingWithOptionalClassifier2, jar));
    }

    @Test
    void mappingUsesBaseVersionForTimestampedSnapshot() throws Exception {
        Artifact snapshot = new StubArtifact(
                "org.apache.sample", "maven-test-lib", "1.0-20200101.120000-1", "1.0-SNAPSHOT", null, "jar");
        assertEquals(
                "maven-test-lib-1.0-SNAPSHOT.jar",
                MappingUtils.evaluateFileNameMapping(MappingUtils.DEFAULT_FILE_NAME_MAPPING, snapshot));
        assertEquals(
                "maven-test-lib-1.0-20200101.120000-1.jar",
                MappingUtils.evaluateFileNameMapping("@{artifactId}@-@{version}@.@{extension}@", snapshot));
    }

    @Test
    void mappingUsesExtensionOfArtifact() throws Exception {
        Artifact war = new StubArtifact("org.apache.sample", "maven-test-lib", "1.0", "1.0", "tests", "war");
        assertEquals(
                "maven-test-lib-1.0-tests.war",
                MappingUtils.evaluateFileNameMapping(MappingUtils.DEFAULT_FILE_NAME_MAPPING_CLASSIFIER, war));
    }
}
