# Simple pure Java game engine

---

<br/>

#####	Motivation

I initially made this to save some coding time. I used to start a new project and dump the old one every week.
Hence I had to write all the basic stuff over and over again. That made me create a library for games.

It is made with only the standard Java API and thus highly portable.

---

<br/>

#####	Example

```java
final Clock gameClock = new Clock();
final Screen screen = new Screen(800, 600, "My game");
    
gameClock.addListener(delta -> {
	System.out.println("Game is updating!");
	System.out.println("Delta time: " + delta);
});
    
screen.addListener(graphics -> {
	graphics.setColor(Color.RED);
	graphics.drawString("Awesome text on the screen", 50, 50);
});
screen.show();
    
while (screen.isOpen()) {
	gameClock.update();
	screen.redraw();
}
```

There is a more detailed example over here: [On Hastebin](http://hastebin.com/nagaxidoka.java)

---

<br>

##### Note!

If running an application in full screen mode I recommend to start a separate thread for drawing.
Full screen caps the frame rate at 60 on most devices and thus makes the update chain lag.

If you need help or want to give feedback, hit me up on my Twitter @TheRealSogomn!

---

<br/>

<p align="center">
	<img src="http://img5.fotos-hochladen.net/uploads/untitledwvetyg9u7k.png" alt="Icon"/>
</p>
