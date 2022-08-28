
--> Uploaded video is encoded into multiple resolutions (360p, 480p, 720p, 1080p, etc.)
--> Video Encoding System further applies codecs like VP9 or H264
--> For above
	--> encoding job requests are created
	--> assigned a *PRIORITY* value
	--> and put into a priority queue
	--> Specialized encoding compute pool handles the job
--> After encoding, all the various video files will be stored
--> Facebook client and backend can then coordinate to stream highest quality + least buffering video to viewers


--> *Priority* = *Benefit* / *Cost*
--> Benefit = (*relative compression efficiency of the encoding family at fixed quality*) * (*effective predicted watch time*)
	--> relative compression efficiency = how much a user benefits from a codecs efficiency
		--> based on Minutes of Video on HQ (MVHQ) = how many minutes of HQ video can be streamed per GB of data
		--> Facebook compares MVHQ of different encodings to find relative compression efficiency
	--> effective predicted watch time = estimated TOTAL watch time of the video in near future
		--> this is derived using ML $^1$
--> Cost = amount of logical computing cycles needed to make the encoding family (consisting of all the different resolutions) deliverable
	--> some jobs may require more resolutions than others before they're considered deliverable

#VideoEncoding #Facebook


##### References
1. https://engineering.fb.com/2021/04/05/video-engineering/how-facebook-encodes-your-videos/