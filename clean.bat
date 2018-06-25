REM Deletes all files and folders listed as ignored in the '.gitignore'-file.
git clean -X -d -f

REM https://git-scm.com/docs/git-clean

REM -X
REM Remove only files ignored by Git. This may be useful to rebuild everything from scratch, but keep manually created files.

REM -d
REM Remove untracked directories in addition to untracked files.
REM If an untracked logDirectory is managed by a different Git repository, it is not removed by default.
REM Use -f option twice if you really want to remove such a logDirectory.

REM -f
REM If the Git configuration variable clean.requireForce is not set to false, git clean will refuse to delete files or directories unless given -f, -n or -i.
REM Git will refuse to delete directories with .git sub logDirectory or file unless a second -f is given.
