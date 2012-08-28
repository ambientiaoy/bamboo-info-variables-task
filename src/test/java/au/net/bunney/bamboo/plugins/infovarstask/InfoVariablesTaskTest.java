/*
 * Copyright 2012 Bunney Apps, Brisbane, Australia.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package au.net.bunney.bamboo.plugins.infovarstask;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.author.AuthorImpl;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.commit.CommitContext;
import com.atlassian.bamboo.commit.CommitContextImpl;
import com.atlassian.bamboo.commit.CommitFile;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.v2.build.BuildChanges;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.CurrentBuildResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gavin Bunney
 */
public class InfoVariablesTaskTest {

    private TaskContext mockTaskContext;
    private InfoVariablesTask task;

    @Before
    public void setUp() throws Exception
    {
        BuildContext mockBuildContext = mock(BuildContext.class);
        BuildLogger mockBuildLogger = mock(BuildLogger.class);

        mockTaskContext = mock(TaskContext.class);
        when(mockTaskContext.getBuildContext()).thenReturn(mockBuildContext);
        when(mockTaskContext.getBuildLogger()).thenReturn(mockBuildLogger);
        when(mockBuildLogger.addBuildLogEntry("")).thenReturn(null);

        BuildChanges mockBuildChanges = mock(BuildChanges.class);
        when(mockBuildContext.getBuildChanges()).thenReturn(mockBuildChanges);

        Author author1 = new AuthorImpl("Gavin Bunney");
        ArrayList<CommitContext> dummyCommits = new ArrayList<CommitContext>() { };
        dummyCommits.add(new CommitContextImpl(author1, new ArrayList<CommitFile>(){},
                   "BAM-123: Fixed bug with things that are broken", new Date(), "ChangeSetId"));
        dummyCommits.add(new CommitContextImpl(author1, new ArrayList<CommitFile>(){},
                   "BAM-123: Fixed the bug correctly this time", new Date(), "ChangeSetId"));

        when(mockBuildChanges.getChanges()).thenReturn(dummyCommits);

        CurrentBuildResult mockBuildResult = mock(CurrentBuildResult.class);
        when(mockBuildContext.getBuildResult()).thenReturn(mockBuildResult);

        when(mockBuildResult.getCustomBuildData()).thenReturn(new HashMap<String, String>(){ });

        task = new InfoVariablesTask();
    }

    @Test
    public void testVariablesSet() throws Exception
    {
        task.execute(mockTaskContext);

        Map<String, String> variables = mockTaskContext.getBuildContext().getBuildResult().getCustomBuildData();
        assertEquals(3, variables.size());
        assertEquals(true, variables.containsKey("info.authorAndCommentList"));
        assertEquals(true, variables.containsKey("info.authorList"));
        assertEquals(true, variables.containsKey("info.commentList"));
    }

    @Test
    public void testAuthorAndCommentList() throws Exception
    {
        task.execute(mockTaskContext);

        String list = mockTaskContext.getBuildContext().getBuildResult().getCustomBuildData().get("info.authorAndCommentList");
        assertEquals("+ Gavin Bunney - BAM-123: Fixed bug with things that are broken\r\n" +
                     "+ Gavin Bunney - BAM-123: Fixed the bug correctly this time\r\n", list);
    }

    @Test
    public void testCommentList() throws Exception
    {
        task.execute(mockTaskContext);

        String list = mockTaskContext.getBuildContext().getBuildResult().getCustomBuildData().get("info.commentList");
        assertEquals("+ BAM-123: Fixed bug with things that are broken\r\n" +
                "+ BAM-123: Fixed the bug correctly this time\r\n", list);
    }

    @Test
    public void testAuthorList() throws Exception
    {
        task.execute(mockTaskContext);

        String list = mockTaskContext.getBuildContext().getBuildResult().getCustomBuildData().get("info.authorList");
        assertEquals("+ Gavin Bunney\r\n", list);
    }
}
