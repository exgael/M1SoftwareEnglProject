# M1SoftwareEnglProject

Add file to staging
```
git add [file name]
```

Add all files to staging ( except those declare in .gitignore )
```
git add .
```

Commit locally
```
git commit -m "commit message"
```

Push the local to remote
```
git push
```

Print existing branches
```
git branch
```

Switch to another branch ( must exist ) 
```
git switch [nom de branch si existante]
```

Create a new branch based on the current branch.
```
git checkout -b [nom de branch]
```

When you want to push a local branch which is not yet on the remote
```
git push --set-upstream origin [branch name]
```

e.g.
I am on dev, i want to copy [nom de branch] -> dev
```
git rebase [nom de branch]
```

Undo last commit made without deleting changes.
```
git reset --soft HEAD~1
```

Undo last commit made and deletes changes.
```
git reset --hard HEAD~1
```


