# Multi Threaded Port Simulator

## General info
This project is an effect of concurrent programming training. Ships are trying to dock into the Port and spend there some time, then depart and spend time on sea, and so on a few iterations. There is limited amount of quays and tugs. Each ship uses specified amount of tugs to dock and undock from Port. 

## Simulation assumptions
- there are n ships available (0 < n <= 25)
- there are m quays available (0 < m < n)
- each ship requires k tugs (0 < k < 5)
- the sum of tugs available at port is lesser than tugs required for all ships to dock at once

## Additional info

### Ship
- Has a randomly generated (in range k) number of tugs required
- Each number has a color representation which is assigned to a ship:
    - blue - 1 tug 
    - red - 2 tugs
    - green - 3 tugs
    - yellow - 4 tugs
- Each ship has a unique name
- There are 3 states the ship can be in, which are signalized during simulation above each ship with colors:
    - on sea - signalized with blue color 
    - tugged - signalized with yellow color
    - docked - signalized with green color
- Ship has 3 simulation modes which change the time the ship spends on sea/in the port and count of iterations: the more time the lesser iterations.
