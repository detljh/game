# Stickman Game
## Configuration File
Store your configuration JSON file named `config.json` in `//src/main/resources`.

It must specify:
1. `"floorHeight"`
2. `"width"`
3. `"height"`
4. `"stickman": {`
       
        "x"
        "size"
        "jumpHeight"
    `}`
5. `"cloudVelocity"`
6. `"enemy": {`
        
        You may leave this block empty.
        
        "enemyType": []
        "x": []
        "y": []
    `}`
7. `"platform": {`

        You may leave this block empty.
        
        "platformType": []
        "x": []
        "y": []
    `}`
8. `"finish": {`

        "x":
        "y":
    `}`

See the default `config.json` in `//src/main/resources` for more details.
## Steps to Run Application
1. Run the command `gradle build`.
2. Run the command `gradle run`.

## Style Guide
The style guide used is: https://google.github.io/styleguide/javaguide.html
