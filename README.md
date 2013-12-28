Multimedia_project:

Content-based video matching: Parameters used for the match are: 
a) Scale and rotation invariant feature of images are matched
b) Color based matching in HSV color space 
c) Audio fingerprint based similarity 
d) Wavelet based image matching

How to run the Code: 
a) 3 external libraries must be added: OpenCV 2.4.6, javacv, musicg 1.4.2 
b) Put all the client side videos + audio in a seperate folder in resources 
c) Put all the server side videos + audio under a seperate folder in resources

Server is an offline process: Running it takes around 3-4 mins per video to extract the content from it 
RUN command:12 Rsrc_d/soccer1 Rsrc_d/soccer2 Rsrc_d/soccer3 Rsrc_d/soccer4.... 
[Total No of videos..................video filenames] 
[Note: This only runs on rgb+wav type videoaudio content, both should be named the same i.e soccer.rgb+soccer.wav 
should be stored under resources folder]

Client is an online process: Running it takes around a min to get the UI up and running 
RUN command: 1 Rsrc_q/soccer_query1 
[Total No of videos..................Query video filenames] 
[Note: This only runs on rgb+wav type videoaudio content, both should be named the same i.e soccer.rgb+soccer.wav 
should be stored under resources folder]

Brief description: Contains 4 packages 
a) com.server.offlineprocess: Offline server process to extract content for the database videos 
b) com.client.onlineprocess: online process to handle query videos and find out the best matched videos 
c) com.common.videoAudioUtility: common utility modules for both the processes 
d) com.display.uielement: video-audio player implementation, graph display

Package Info: Detailed package_info is attached for each of packages. Please refer to it for more details
