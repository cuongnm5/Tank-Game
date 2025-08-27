# Java project - Game Tank

A classic tank battle game implemented in Java using Swing framework.

![Game Screenshot](https://github.com/dodoproptit99/Tank-Game/blob/master/intro.png)

## System Requirements

- **Java Development Kit (JDK)**: Version 8 or higher
- **IDE**: NetBeans IDE (recommended)
- **Java Runtime**: Properly configured within NetBeans

## Getting Started

### For Players

To launch the game:
1. Navigate to `/src/main/Start.java`
2. Execute the file using the Run command

![Launch Screenshot](https://github.com/dodoproptit99/Tank-Game/blob/master/image/Screenshot%20from%202019-05-07%2021-20-00.png)

### Game Modes

- **Single Player**: Battle against AI-controlled tanks
- **Two Player**: Local multiplayer mode

### Objective

Eliminate all enemy tanks while protecting your base to achieve victory.

## Development Guide

### 1. Graphics System

The graphics engine utilizes pre-rendered sprite images for tank animations, including movement in four directions. All visual assets (tanks, walls, rivers, base) are stored in the `/image` directory.

The rendering system continuously redraws sprites based on the `FRESHTIME` parameter:
- **Higher FRESHTIME**: Faster tank movement and smoother animation
- **Lower FRESHTIME**: Slower gameplay with reduced frame rate

All graphics-related code is located in the `/frame` directory, which handles:
- Game interface creation and window management
- Size and speed configuration
- Keyboard input processing (`KeyEvent` handling)
- Object movement function calls
- Screen rendering operations

### 2. Game Model Architecture

The game employs an object-oriented approach with distinct classes for each game entity:

- **Map**: Game environment and terrain
- **Bullet**: Projectile mechanics
- **Tank**: Player-controlled vehicles
- **Boom**: Explosion effects
- **Bot Tank**: AI-controlled enemies (inherits from Tank class)

#### Tank Configuration

Tank properties are configurable through the following parameters:

```java
private boolean attackCoolDown = true;        // Attack cooling mechanism
private int attackCoolDownTime = 500;         // Attack cooldown in milliseconds
protected boolean hasBullet;                  // Bullet existence check to prevent rapid fire
private int life;                             // Tank health/lives
```

#### AI Behavior

Bot tanks feature autonomous movement using randomized direction selection:

```java
private Direction randomDirection() {
    Direction[] dirs = Direction.values();    // Get direction enumeration values
    Direction oldDir = dir;
    Direction newDir = dirs[random.nextInt(4)];
    
    if (oldDir == newDir || newDir == Direction.UP) {
        // Re-randomize if same direction or moving up
        return dirs[random.nextInt(4)];
    }
    return newDir;
}
```

### 3. Audio System

Audio assets are organized in the `/audio` directory, containing:
- Background music tracks
- Sound effects for game events

The utility classes handle loading and playing audio files during gameplay.

### 4. Project Structure

The codebase is thoroughly commented for educational purposes. Key components include:

- **Graphics Layer**: Visual rendering and user interface
- **Game Logic**: Core mechanics and rules
- **Audio System**: Sound management
- **Utility Classes**: Helper functions for asset loading

## Contributing

This project serves as a learning resource for Java game development. Feel free to explore the code comments for detailed implementation explanations.


## License

This project is available for educational and personal use.
