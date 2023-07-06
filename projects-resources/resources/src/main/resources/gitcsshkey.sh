#!/bin/bash
cd E:
cd $1
ssh-keygen -t rsa -C "$2"
echo -e "\n"
echo -e "y"
echo -e "\n"
echo -e "\n"

