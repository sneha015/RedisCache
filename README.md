# RedisCache
Restful web service with Spring, HTML, Java which loads and displays, static images from MySQL. Improved the performance of the application by implementing Redis Cache to access the images.

Create a table image in mysql :
 
giving id=1 for image_sjsu.png
id=2 for image_cpme.png
id=3 for image_library.png

Build maven project, Run Application.java which starts the spring server

On terminal meanwhile clear the cache using following command
$ redis-cli flushall

launch index.html file from the folder 
 
click on image sjsu button for the first time, image will load from db.
 
click on image sjsu button for the again, image will load from cache now.

