# memory-card-game

- This is a memory matching card game made with Android Studio and Java. The user data and images are stored in mongoDB cloud. For the API, I've slightly modified some parts of my former project [social medi api](https://github.com/talhaunal7/social_media_api) you can see the code I used for API [here](https://github.com/talhaunal7/memory-card-game/tree/main/api).

- The images used for cards are stored as base64 strings on the cloud. To fetch them, I create a http request to my node.js API and convert them into images.

- There is a detailed [report](https://github.com/talhaunal7/memory-card-game/blob/main/Project%20Report.pdf) that explains the project

- There are 2 game modes which is 1 player and 2 players. Also, there are 2x2, 4x4 and 6x6 card count options.

![](https://github.com/talhaunal7/memory-card-game/blob/main/gifs/game.gif)

![](https://github.com/talhaunal7/memory-card-game/blob/main/gifs/ss1.png)
![](https://github.com/talhaunal7/memory-card-game/blob/main/gifs/ss2.png)
