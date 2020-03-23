# GameObject

As introduced before, I talk about some entities managed by the game. This where the `core.object.GameObject` is going to play in the framwork.  

Any entity manage by the game,  the player character, the NPC (non playable characters), the score, the life, the all background display are objects in a game. The `core.object.GameObject` is going to be the core matter of the `core.Game`.

To be able to draw anything on screen, we will need for each object , a position, a size. We would need some other properties like an image of a color, a shape (square, circle, rectangle), and to be animated more new attributes we will discover later.

### Before enhancing our framework

In the previous `SampleGameLoop` example, we try to move a red square box from side to side. Let start from the requirement for this sample code:

- position(x,y)
- speed( dx,dy)
- color

#### GameObject class

All those  will be centralized into only one object, the `GameObject` class.

So, to start with sustainable things, create our class:

```java
public class GameObject {
	public int x;
	public int y;
	public int width;
	public int height;
	public int dx;
    public int dy;
  	public Color color;   
}
```

Now we have some attributes, we need to update and render things. In a common way to Object development, we need to specialized objects. In the previous sample, all things were performed by the main class. let's delegate some of the operations to this new `GameObject` class. The update process can be performed into the object itself, and the draw process so:

```java
public void udpate(Game ga, long elapsed){
    x += dx;
    y += dx;
}
public void draw(Game ga, Graphics2D g){
    g.setColor(color);
    g.fillRect(x,y,16,16);
}
```

The main class now will just have to initialize a `GameObject` and delegate its update and its draw to.

```java
public class SampleGameObject {
  ...
  GameObject object;
  ...
  public void initialize(){
    objects = new GameObject();
  }
}
```

And now adapt update processing to objects update:

```java
void update(long elapsed){
    object.update(this, elapsed);
}
```
And adapt the rendering process:
```java
void render(Graphics2D g){
	object.draw(this, g);
}
```

Ok, we moved from a One big class to 2 smaller classes. But where are the benefits ? The impact of such class is louder if we decide to maintain multiple `GameObject` instances.

So let's manage more objects !

#### One class to rules them all

lets change a little bit our main class `SampleGameObject` by update some things.

First, replace the object  attribute by a more useful list of object:

```java
private List<GameObject> objects = new ArrayList<>();
```

And then modify the initialization, to add our object to the list:

```java
  public void initialize(){
    objects.add(new GameObject());
  }
```

And let's adapt the update and render methods:

```java
void update(long elapsed){
    for(GameObject go:objects){
        go.update(this, elapsed);
        constrainGameObject(this,go);
    }
}
void render(Graphics2D g){
    for(GameObject go:objects){
        go.draw(this, g);
    }
}
```

So, now, adding one or a dozen of `GameObject`, the main class `SampleGameObject` will remain the same (except the initialize method).

```java
  public void initialize(){
      for(int i=0;i<20;i++){
          GameObject go = new GameObject()
          go.x = (int) Math.random() * (screenBuffer.getWidth() - 16);
          go.y = (int) Math.random() * (screenBuffer.getHeight() - 16);
          go.width = 16;
          go.height = 16;
          go.dx = (int) (Math.random() * 8);
          go.dy = (int) (Math.random() * 8);             
          objects.add(go);   
      }      
  }
```

Then run this sample code :

```bash
$> cd src/main/samples
$> javac SampleGameObject.java
$> java SampleGameObject
```

And you will get something like this :

![More than just one GameObject](./resources/illustrations/SampleGameObject.png "Using a GameObject class for multiple objects on screen")

#### Adding a Pause mode

We also create a small Pause mode by adding a pause boolean flag, changing its state with the `P` or the `PAUSE` key.

```java
public class SampleGameObject implements KeyListener{
	...
    // pause flag
    boolean pause = false;
    
    ...
    public void keyReleased(KeyEvent e) {
        GameObject go = objects.get(0);
        switch (e.getKeyCode()) {
			...
            case KeyEvent.VK_P:
            case KeyEvent.VK_PAUSE:
                pause = !pause;
                break;
            default:
                break;
        }
    }
    ...
    public void loop() {
        ...
        while (!exit) {
            nextTime = System.currentTimeMillis();
            if (!pause) {
                update(elapsed);
            }
            render();
            ...
        }
    }
    ...
}
```

So if you press a first  time the pause key, the update of all objects will stop. pressing the pause key again will restart the update process.

### Map of objects

There is a small difficulties in this approach, how to identify one particular `GameObject` in the list without maintaining some index values ? For this main reason, we are going to add a name to each of our object, and a mechanism to provide a default name for each of those.

First , extract the sub-class `GameObject` to a normal class, and then add the `name` attribute and the static `index` counter like bellow. 

The `GameObject()` constructor will initialize the default name for each object according to index value.

```java
public class GameObject {
    private static int index=0;
    String name;
    ...
        
    public GameObject(){
        name = "gameobject_"+(index++);
        ...
    }
    ...
}
```

And we need to replace our `GameObject`'s list in the `SampleGameObject` class with an other object list structure that allow us to retrieve directly an entity on its own name: a [`Map`](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html "go and read the official JDK doc on Map object structure").

```java
public class SampleGameObject implements KeyListener {
    ...
    Map<String,GameObject> objects = new HashMap<>();    
    ...        
}
```

Then we need to adapt our code with this new Map. first the initialize() method:

```java
public void initialize(){
    for(int i=0;i<20;i++){
        GameObject go = new GameObject();
        ...
        objects.put(go.name,go);
    }
}
```

And the update processing must no more parse a list, but the map elements. We will get the  [`Map#values()`](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#values-- "what is exactly the values from a map ?") collection instead of direct objects attribute:

```java
public void update(long elpased){
    ...
    for(GameObject go: objects.values()){
        go.update(this,elapsed);
        ...
    }
} 
```

And finally, the rendering process must be also adapted to the Map values parsing:

```java
    
public void render(){
    Graphics2D g = ... ;
    ...
    for(GameObject go: objects.values()){
        go.draw(this,g);
        ...
    }
}
```

This will resulting in the same window display as previous version, but now we can get a particular `GameObject` directly from the map, without indexing list. Names are the index.

Just notice that we add a fancy feature to move one of the object, just press the SPACE bar to recenter the first object in the map, the one identify by the name '`gameobject_1`'. 

Then like in the previous ` SampleGameLoop` code, you can move this object according to the speed you set through the directional keys.

![The Map object list used in the SampleGameObject](./resources/illustrations/SampleGameObject-map.png "using a Map instead of a list to identify all GameObject's")



 ### Can we display an image ?

Until now, we only draw some rectangles as `GameObject`. It's now time to try some more fancy and interesting things. Let's add a `GameObjectType` to add new capabilities to our `GameObject` class. This `enum` will be added as an internal enumeration to `GameObject`.

First, we need to know what kind of things we will need to display. Maybe some squares, but also some ellipse as circle or dots, some point, some lines ?  and mainly some images. All the beautiful characters in games are images. So, to define properly this new `GameObject` type attribute, we need to define a new enumeration of possibilities:

```java
public enum GameObjectType{
    POINT,
    LINE,
    RECT,
    ELLIPSE,
    IMAGE;
}
```

With such type we may cover near all future cases.

So we now add the `type` attribute in the `GameObject` class:

```java
public class GameObject{
    ...
    GameObjectType type;
    ...
    public GameObject(){
        ...
        type=GameObject.RECT;
        ...
    }
}
```

Here we've define a default value to `RECT`, to get a rectangle as default type.

Now, we must adapt our `GameObject#draw()` method to take in account the type of object to render.

```java
public void draw(Game ga, Graphics2D g){
    g.setColor(color);
    switch(type){
        case POINT:
            g.drawLine(x,y,x,y);
            break;
        case LINE:
            g.drawLine(x,y,x+dx,y+dy);
            break;
        case RECT:
            g.fillRect(x,y,width,height);
            break;
        case ELLIPSE:
            g.fillEllipse(x,y,width,height);
            break;
        case IMAGE:
            break;            
    }
}
```

To get some demonstration of all those new object's types, let's introduce some randomness in our game initialization. We first need to randomly select a type of `GameObject`, we will delegate randomizing to the Java Math library `random()` method, generating positive float values:

```java
private GameObjectType randomType(){
    // all type but not IMAGE => max 4
    int vt = (int)(Math.random()*4);
    return GameObjectType.values()[vt];
}
```

and then call this `ramdomType()` from the `initialize()` phase:

```java
public void initialize() {
    collidingColor = Color.WHITE;
    squareColor = Color.RED;
    for (int i = 0; i < 20; i++) {
        GameObject go = new GameObject();
		...
        go.type = randomType();
        ...
    }
}
```

this is amazing, no ?

![Adding random Objects type to the objects list](./resources/illustrations/GameObjectTypeRandom.png "When randomness is fancy")

### Anti-Aliasing

Rendering square is  like rendering a big pixel. But rendering some ellipse or circle is not to so visually good.

[TODO]

### Adding image

If we add some Image in the rendering process, we will get a more interesting UX.

In the Initialization phase, just add some kinky image in place of the good old rectangle.

```java
public void initialize(){
    
}
```

Then, run this master piece, you will get a brand new game experience:

[capture here] 

In the latest version, I've update the displayDebug() method to get something more fancy and more usable:

```java
private void displayDebug(Graphics2D sg, GameObject go) {
    Font f = sg.getFont().deriveFont(9);
    sg.setFont(f);
    FontMetrics fm = sg.getFontMetrics();
    int lineHeight = fm.getHeight();
    int xOffset = (go.x + go.width + 8);

    sg.setColor(Color.DARK_GRAY);
    sg.fillRect((xOffset-4) * scale, go.y * scale, 150, 6*lineHeight);

    sg.setColor(Color.ORANGE);
    sg.drawString(
        String.format("name:%s", go.name), 
        xOffset * scale,
        (go.y * scale) + (1 * lineHeight));
    sg.drawString(
        String.format("pos:%03d,%03d", go.x, go.y), 
        xOffset * scale,
        (go.y * scale) + (2 * lineHeight));
    sg.drawString(
        String.format("vel:%03d,%03d", go.dx, go.dy), 
        xOffset * scale,
        (go.y * scale) + (3 * lineHeight));
    sg.drawString(
        String.format("type:%s", go.type.name()), 
        xOffset * scale,
        (go.y * scale) + (4 * lineHeight));
    sg.drawString(
        String.format("siz:%03d,%03d", go.width, go.height), 
        xOffset * scale,
        (go.y * scale) + (5 * lineHeight));
}
```

this will produce a more pro look :

![the latest displayDebug() method implementation](./resources/illustrations/displayDebug.png "when displaying debug information count, the lokk and feel is important (going UX !) ")

And the debug information line goes bottom screen. Just  isolate the draw-to-screen buffer method and modified the debug output:

```java
if (debug > 1) {
    for (GameObject go : objects.values()) {
        if (debug > 2) {
            displayDebug(sg, go);
        }
    }
    sg.setColor(new Color(0.6f, 0.3f, 0.0f, 0.7f));
    sg.fillRect(0, frame.getHeight() - 20, frame.getWidth(), 20);
    sg.setColor(Color.ORANGE);
    sg.drawString(String.format("debug:%d | pause:%s", debug, (pause ? "on" : "off")), 10,
                  frame.getHeight() - 4);
}
```

This small piece of code will be rendered like bellow :



![When having some useful debug information on screen](./resources/illustrations/bottomDebugLine.png "Add a screen bottom debug information line")

We now can go to framework things by adding some just develop fancy objects.

## In out Framework

This new `GameObject` and its internal enumeration `GameObjectType` will go as is in the `core.object` package.

The latest version of the `SampleGameObject` class will be moved as the `Game` class into the core package.

```txt
src
|_ main
   |_ java
      |_ core
      |  |_ Game.java
      |_ object
         |_ GameObject.java
```

It's time to go to the next chapter and decrypt what is a Scene and how to manage these new things.

