# Bamboo Information Variables Task
====================================

Bamboo Task to inject various information variables for use within a Job.

## Author and Comments List

List of the authors and comments from the SCM from the build.

Format of `+ <author> - <comment>` seperated by new lines.

### Usage
```
${bamboo.info.authorAndCommentList}
```

### Example
```
+ Gavin Bunney - BAM123: Fixed bug with things that are broken
+ Gavin Bunney - BAM123: Fixed the bug correctly this time
```

## Comments List

List of the comments from the SCM from the build.

Format of `+ <comment>` seperated by new lines.

### Usage
```
${bamboo.info.commentList}
```


### Example
```
+ BAM123: Fixed bug with things that are broken
+ BAM123: Fixed the bug correctly this time
```


----

# Development

* `atlas-run`   -- installs this plugin into Bamboo and starts it on http://localhost:6990/bamboo
* `atlas-debug` -- same as atlas-run, but allows a debugger to attach at port 5005
* `atlas-cli`   -- after atlas-run or atlas-debug, opens a Maven command line window:
                 - 'pi' reinstalls the plugin into the running Bamboo instance
* `atlas-help`  -- prints description for all commands in the SDK

Full documentation is always available at: https://developer.atlassian.com/display/DOCS/Developing+with+the+Atlassian+Plugin+SDK
