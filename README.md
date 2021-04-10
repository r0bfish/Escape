# Lost Penguin

Adventurous game about a lost penguin trying to find the way back to its friends. You will face multiple levels with different challenges from ice that gives the penguin constant movement, necessity to jump between cliffs over the water, and monsters stoppping your path. There are 18 levels for the player to explore that entails a lot of fun moments to experience. You can find the game at [Google Play](https://play.google.com/store/apps/details?id=com.robin.lostpenguin "Lost Penguin on Google Play") for Android

![Image from the game](https://github.com/r0bfish/LostPenguin/blob/master/pictures/github.png "Image from game")

---

## Development
##### Framework
[Libgdx framwork](https://libgdx.com/ "Libgdx's webpage") is a cross-platform Java game development framework based on OpenGL (ES) that works on Windows, Linux, macOS, Android, your browser and iOS.

#### Programming
Lots of things were created from the scratch due to use of a framework instead of an engine. Example of implementations that were needed was:
- Game state design
- Map editor that saves and loads the levels as a json document
- Collision detection with logic response
- Z-indexing to draw in layers for the 2.5D visuals
- Frustum culling to increase performance
- Input handling

#### Art
The tileset and sprites are created in a pixel-art style. Every image and animation was created with a shadow to give an illusion to be a 2.5D game. That entailed some problems for the collision check later on due to extra size needed for the shadow. A separate collider with a different size for every sprite was needed to account for the shadow. 
![Sprites from the game](https://github.com/r0bfish/LostPenguin/blob/master/pictures/sprites.png "Sprites from game")

#### Sound
Two soundtracks were made by trying out different melodies on the piano and then recreating them in FL Studio. The music also includes sound effect of wind to give the player a more immersive experience of being on a cold location. 
