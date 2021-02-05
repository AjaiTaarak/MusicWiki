About
MusicWiki is an unofficial Last.fm app that contains information about different music genres, the albums, artists and tracks listed under the genre. The app uses Last.fm’s API for all the information.
Features:
This android app lets you:
1.	Displays a list of top genres
2.	Uses Firebase phone number authentication to authenticate the user
3.	Clicking on a genre it takes you to a new page where the information about the genre’s top tracks, top albums, top artists are listed.
4.	Gives detailed information about the song, album and artist
5.	Requires no permission to use the app.
Screenshots:
 
Decisions:
1.	The app uses a wrapper class of Last.fm API provided by vpaily. Also the wrapper class did not have all the required methods, so I used AsyncTask to fetch the response(JSON file). 
2.	The app  has a simple UI and was designed with reference from other popular music apps and few other webpages.
3.	Due to limited time, I did not add new features and focused on what was asked.
4.	Few fields were missing from the response, so I used dummy values (Images of artists and genres)





