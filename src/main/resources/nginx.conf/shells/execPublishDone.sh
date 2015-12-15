#!bin/sh

## Live PlatForm Project
# Designed By: NetLab, SDCS, SYSU, GuangZhou, PRC
# Author: Joshua Shaw (CHangzhou Xiao)
 
#---------------------------------------------------------------
# This shell script will be called after publish is over.
# DO:
# 1. Cancel registered information for publish stream to interact mudule.
# 2. Remove the poster.
#---------------------------------------------------------------

# define
name=$1
addr=$2
imageFormat=gif
# end define

#curl "http://172.18.219.201/LivePlatform/callback/execPublishDone?name=${name}&addr=${addr}"
rm /home/live/nginx/posters/${name}.${imageFormat}
