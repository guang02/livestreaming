#!bin/sh

## Live PlatForm Project
# Designed By: NetLab, SDCS, SYSU, GuangZhou, PRC
# Author: Joshua Shaw (CHangzhou Xiao)

#---------------------------------------------------------------
# This shell script will be called after publish call started.
# DO:
# 1. Cut one frame for poster.
# 2. Register information for publish stream to interact mudule.
# 3. Cut one current frame for poster and loop it until the publish 
#    stream is over.
#---------------------------------------------------------------

# define
name=$1
addr=$2
imageFormat=gif
# end define

ffmpeg -v warning -i rtmp://localhost/hls/${name} -y -vf scale=300:-1:sws_dither=ed -t 5 -r 9 -f gif /home/live/nginx/posters/${name}.${imageFormat}

#curl "http://172.18.219.201/LivePlatform/callback/execPublish?name=$name&addr=$addr"

while [ -e /home/live/nginx/posters/${name}.${imageFormat} ]
do
	sleep 2m
	ffmpeg -v warning -i rtmp://localhost/hls/${name} -y -vf scale=300:-1:sws_dither=ed -t 5 -r 9 -f gif /home/live/nginx/posters/${name}.${imageFormat}
	# log here
done

# PS: ps -ax | grep command | awk '{print $1}' | xargs -i kill -9 {}
