#!/bin/bash
cd E:
cd $1
git status
git add .
git commit -m "$2"
git pull
git push