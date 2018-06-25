# Deletes all files and folders listed as ignored in the '.gitignore'-file.
git clean -X -d -f

# https://git-scm.com/docs/git-clean
#
# -X
# Remove only files ignored by Git. This may be useful to rebuild everything from scratch, but keep manually created files.
#
# -d
# Remove untracked directories in addition to untracked files.
# If an untracked logDirectory is managed by a different Git repository, it is not removed by default.
# Use -f option twice if you really want to remove such a logDirectory.
#
# -f
# If the Git configuration variable clean.requireForce is not set to false, git clean will refuse to delete files or directories unless given -f, -n or -i.
# Git will refuse to delete directories with .git sub logDirectory or file unless a second -f is given.
