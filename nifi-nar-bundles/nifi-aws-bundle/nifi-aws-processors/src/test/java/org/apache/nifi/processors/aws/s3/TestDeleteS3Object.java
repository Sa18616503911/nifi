/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.processors.aws.s3;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("For local testing only - interacts with S3 so the credentials file must be configured and all necessary buckets created")
public class TestDeleteS3Object extends AbstractS3Test {

    @Test
    public void testSimpleDelete() throws IOException {
        // Prepares for this test
        putTestFile("delete-me", getFileFromResourceName(SAMPLE_FILE_RESOURCE_NAME));

        final TestRunner runner = TestRunners.newTestRunner(new DeleteS3Object());

        runner.setProperty(DeleteS3Object.CREDENTAILS_FILE, CREDENTIALS_FILE);
        runner.setProperty(DeleteS3Object.REGION, REGION);
        runner.setProperty(DeleteS3Object.BUCKET, BUCKET_NAME);

        final Map<String, String> attrs = new HashMap<>();
        attrs.put("filename", "delete-me");
        runner.enqueue(new byte[0], attrs);

        runner.run(1);

        runner.assertAllFlowFilesTransferred(DeleteS3Object.REL_SUCCESS, 1);
    }

    @Test
    public void testDeleteFolder() throws IOException {
        // Prepares for this test
        putTestFile("folder/delete-me", getFileFromResourceName(SAMPLE_FILE_RESOURCE_NAME));

        final TestRunner runner = TestRunners.newTestRunner(new DeleteS3Object());

        runner.setProperty(DeleteS3Object.CREDENTAILS_FILE, CREDENTIALS_FILE);
        runner.setProperty(DeleteS3Object.REGION, REGION);
        runner.setProperty(DeleteS3Object.BUCKET, BUCKET_NAME);

        final Map<String, String> attrs = new HashMap<>();
        attrs.put("filename", "folder/delete-me");
        runner.enqueue(new byte[0], attrs);

        runner.run(1);

        runner.assertAllFlowFilesTransferred(DeleteS3Object.REL_SUCCESS, 1);
    }

    @Test
    public void testDeleteFolderNoExpressionLanguage() throws IOException {
        // Prepares for this test
        putTestFile("folder/delete-me", getFileFromResourceName(SAMPLE_FILE_RESOURCE_NAME));

        final TestRunner runner = TestRunners.newTestRunner(new DeleteS3Object());

        runner.setProperty(DeleteS3Object.CREDENTAILS_FILE, CREDENTIALS_FILE);
        runner.setProperty(DeleteS3Object.REGION, REGION);
        runner.setProperty(DeleteS3Object.BUCKET, BUCKET_NAME);
        runner.setProperty(DeleteS3Object.KEY, "folder/delete-me");

        final Map<String, String> attrs = new HashMap<>();
        attrs.put("filename", "a-different-name");
        runner.enqueue(new byte[0], attrs);

        runner.run(1);

        runner.assertAllFlowFilesTransferred(DeleteS3Object.REL_SUCCESS, 1);
    }

    @Test
    public void testTryToDeleteNotExistingFile() throws IOException {
        final TestRunner runner = TestRunners.newTestRunner(new DeleteS3Object());

        runner.setProperty(DeleteS3Object.CREDENTAILS_FILE, CREDENTIALS_FILE);
        runner.setProperty(DeleteS3Object.REGION, REGION);
        runner.setProperty(DeleteS3Object.BUCKET, BUCKET_NAME);

        final Map<String, String> attrs = new HashMap<>();
        attrs.put("filename", "no-such-a-file");
        runner.enqueue(new byte[0], attrs);

        runner.run(1);

        runner.assertAllFlowFilesTransferred(DeleteS3Object.REL_SUCCESS, 1);
    }

}
