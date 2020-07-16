# Risk-Map-UI

This project is the UI for the board game [Risk](https://en.wikipedia.org/wiki/Risk_(game)). 
No Game Logic is implemented. The countries are stored as a graph and displayed in the window. 
This is meant as a tool for anyone wanting to implement the boardgame without bothering with the UI. 

## Sea connections

To add all usual sea connections paste this code into yours:

>Map.getCountry("ontario").addNeighbour(Map.getCountry("greenland"));  
>Map.getCountry("quebec").addNeighbour(Map.getCountry("greenland"));  
>Map.getCountry("greenland").addNeighbour(Map.getCountry("iceland"));  
>Map.getCountry("great_britain").addNeighbour(Map.getCountry("iceland"));  
>Map.getCountry("iceland").addNeighbour(Map.getCountry("scandinavia"));  
>Map.getCountry("scandinavia").addNeighbour(Map.getCountry("great_britain"));  
>Map.getCountry("great_britain").addNeighbour(Map.getCountry("northern_europe"));  
>Map.getCountry("northern_europe").addNeighbour(Map.getCountry("scandinavia"));  
>Map.getCountry("great_britain").addNeighbour(Map.getCountry("western_europe"));  
>Map.getCountry("southern_europe").addNeighbour(Map.getCountry("egypt"));  
>Map.getCountry("north_africa").addNeighbour(Map.getCountry("brazil"));  
>Map.getCountry("east_africa").addNeighbour(Map.getCountry("middle_east"));  
>Map.getCountry("east_africa").addNeighbour(Map.getCountry("madagascar"));  
>Map.getCountry("madagascar").addNeighbour(Map.getCountry("south_africa"));  
>Map.getCountry("western_australia").addNeighbour(Map.getCountry("indonesia"));  
>Map.getCountry("indonesia").addNeighbour(Map.getCountry("siam"));  
>Map.getCountry("mongolia").addNeighbour(Map.getCountry("japan"));  
>Map.getCountry("japan").addNeighbour(Map.getCountry("kamchatka"));  
>Map.getCountry("kamchatka").addNeighbour(Map.getCountry("alaska"));  
>Map.getCountry("indonesia").addNeighbour(Map.getCountry("new_guinea"));  
>Map.getCountry("eastern_australia").addNeighbour(Map.getCountry("new_guinea"));  
>Map.getCountry("western_australia").addNeighbour(Map.getCountry("new_guinea"));  
>Map.getCountry("northwest_territory").addNeighbour(Map.getCountry("greenland"));  
>Map.getCountry("southern_europe").addNeighbour(Map.getCountry("north_africa"));'
