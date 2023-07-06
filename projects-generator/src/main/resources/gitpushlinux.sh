#!/bin/bash
cd $1
git status
git add -all
git commit -m "$2"
git pull
git push