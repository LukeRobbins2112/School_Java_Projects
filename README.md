# VidPlayer

Creates User and Video objects from XML files and video files, then presents a GUI login window that verifies username and password 
before displaying a list of video options. Only videos appropriate for User’s Rating (determined by comparing enum values) are displayed.

Once the user starts a video, closing the window saves the timestamp; playback picks up from the
same point when opened by the same User.

Utilizes object serialization as well as Singleton, Strategy, Factory, and Façade design patterns.
