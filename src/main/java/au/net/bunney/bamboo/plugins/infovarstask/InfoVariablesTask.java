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
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.commit.CommitContext;
import com.atlassian.bamboo.task.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private static final String VARIABLE_AUTHOR_LIST = VARIABLE_PREFIX + "authorList";

    @NotNull
    @java.lang.Override
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException
    {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final List<CommitContext> changesList = taskContext.getBuildContext().getBuildChanges().getChanges();

        final Map<String, String> customBuildData = taskContext.getBuildContext().getBuildResult().getCustomBuildData();

        customBuildData.put(VARIABLE_AUTHOR_COMMENT_LIST, getChangesAsStringList(changesList, true));
        buildLogger.addBuildLogEntry("Injected variable: bamboo." + VARIABLE_AUTHOR_COMMENT_LIST);

        customBuildData.put(VARIABLE_COMMENT_LIST, getChangesAsStringList(changesList, false));
        buildLogger.addBuildLogEntry("Injected variable: bamboo." + VARIABLE_COMMENT_LIST);

        customBuildData.put(VARIABLE_AUTHOR_LIST, getAuthorsAsStringList(changesList));
        buildLogger.addBuildLogEntry("Injected variable: bamboo." + VARIABLE_AUTHOR_LIST);

        return TaskResultBuilder.create(taskContext).success().build();
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

    protected String getAuthorsAsStringList(List<CommitContext> changes) {
        Set<Author> authors = getAuthors(changes);
        if (authors.size() == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        for (Author author : authors) {

            if (author.getName() == null)
                continue;

            stringBuilder.append("+ ");
            stringBuilder.append(author.getName());
            stringBuilder.append("\r\n");
        }

        return stringBuilder.toString();
    }

    protected Set<Author> getAuthors(List<CommitContext> changes) {
        Set<Author> authors = new HashSet<Author>();
        for (CommitContext change : changes) {
            if (change.getAuthor() != null)
                authors.add(change.getAuthor());
        }
        return authors;
    }
}