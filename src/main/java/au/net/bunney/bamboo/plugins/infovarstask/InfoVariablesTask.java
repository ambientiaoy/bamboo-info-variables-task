/*
 * Copyright 2012 Gavin Bunney, Brisbane, Australia.
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

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.commit.CommitContext;
import com.atlassian.bamboo.task.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Info Variables Task.
 *
 * @author Gavin Bunney
 */
public class InfoVariablesTask implements TaskType
{
    private static final String VARIABLE_PREFIX = "info.";
    private static final String VARIABLE_AUTHOR_COMMENT_LIST = VARIABLE_PREFIX + "authorAndCommentList";
    private static final String VARIABLE_COMMENT_LIST = VARIABLE_PREFIX + "commentList";

    @NotNull
    @java.lang.Override
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException
    {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final TaskResultBuilder builder = TaskResultBuilder.create(taskContext);

        final Map<String, String> customBuildData = taskContext.getBuildContext().getBuildResult().getCustomBuildData();

        customBuildData.put(VARIABLE_AUTHOR_COMMENT_LIST, getChangesAsStringList(taskContext.getBuildContext().getBuildChanges().getChanges(), true));
        buildLogger.addBuildLogEntry("Injected variable: bamboo." + VARIABLE_AUTHOR_COMMENT_LIST);

        customBuildData.put(VARIABLE_COMMENT_LIST, getChangesAsStringList(taskContext.getBuildContext().getBuildChanges().getChanges(), false));
        buildLogger.addBuildLogEntry("Injected variable: bamboo." + VARIABLE_COMMENT_LIST);

        return builder.success().build();
    }

    protected String getChangesAsStringList(List<CommitContext> changes, boolean includeAuthor) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CommitContext change : changes) {

            stringBuilder.append("+ ");
            if (   (includeAuthor)
                && (change.getAuthor() != null)
                && (change.getAuthor().getName() != null)) {
                stringBuilder.append(change.getAuthor().getName());
                stringBuilder.append(" - ");
            }

            stringBuilder.append(change.getComment());
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }
}