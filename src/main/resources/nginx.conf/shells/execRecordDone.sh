#!bin/sh

## Live PlatForm Project
# Designed By: NetLab, SDCS, SYSU, GuangZhou, PRC
# Author: Joshua Shaw (CHangzhou Xiao)

#---------------------------------------------------------------
# This shell script will be called after the record action accomplished.
# DO:
# 1. Check whether the video path is exist.
#     - if exist, save the size.
#     - else do nothing.
# 2. Check whether the size of video is greater than the expected value.
#     - if true, transform it from flv to mp4, and call callback module.
#     - else do nothing.
# 3. Delete temp video.
#---------------------------------------------------------------


#define
savePath=/home/live/nginx/videos
basename="$1"
path=$2
minFlvSize=$((1024 * 1024 * 50))
flvSize=0
imageFormat=gif
#end define


if [ -e $path ]; then
    flvSize=`ls -l $path | cut -d ' ' -f 5`
fi

if [ $flvSize -gt $minFlvSize ]; then
    ffmpeg -i $path -vcodec copy -acodec copy -y "${savePath}/${basename}.mp4" 
	ffmpeg -v warning -i "${savePath}/${basename}.mp4" -y -vf scale=300:-1:sws_dither=ed -ss 120 -t 5 -r 9 -f gif /home/live/nginx/posters/${basename}.${imageFormat}
    #curl "http://172.18.219.201/LivePlatform/callback/execRecordDone?filename=${basename}"
    # log here
fi

rm $path

# imagemagik :
# convert -layers Optimize animation.gif animation_small.gif
