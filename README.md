# Welcome to Log-Errors

## This Repository contains a multitude of sub projects. 
1. Log Scraper - tokenizes log files of the format in [here](https://github.com/cloudhubs/logscraper/tree/master/logs/from_prod_anonymized) 

2. StackOverflow Scraper - an api that generates a database of posts from stack overflow

3. Mongo Matcher - a matching algorithm for comparing errors to data scraped by the StackOverflow Scraper

4. Mongo DB API - an API for the mongo db containing information from StackOverflow Scraper 

## Installation Of Project
### Java
`git clone https://github.com/cloudhubs/log-errors.git`

[Install Lombok for your IDE](https://www.baeldung.com/lombok-ide)
### Python
This will use pycharm as the IDE of choice. (Assuming you have already cloned the repo)
1. Open `log-errors/scraper` in [Pycharm](https://www.jetbrains.com/pycharm/download/#section=windows)
2. Indicate the python interpreter if prompted at the top. 
3. Open a new terminal at the bottom of the window. 
4. If the line does not start with the name of your virtual environment, follow [this](https://www.jetbrains.com/help/pycharm/creating-virtual-environment.html)

    for example: note the (venv) indicating the virtual environment, the path and the brach (master)
    ``` bash
    (venv) mark@MarksComputer ~/Documents/scraper/log-errors (master) $ 
    ```
5. Run the following command in `scraper/` root
    ``` bash 
    pip install -r requirements.txt 
    ```
6. To run the project right click in `api/main.py` and select `run`

7. Alternatively using bash:

```
$ python3 -m venv venv
$ source venv/bin/activate
$ pip3 install -r requirements.txt
$ python3 main.py
```

# Log Parser API
**Start the parser [here](src/main/java/ires/baylor/edu/logerrors/LogErrorsApplication.java)**
- [Parse File](README-matcher.md): `POST /errors/`



# StackOverflow Scraper API
**The mongo and scraper apis are run simultaneusly. Start them from [this file](/scraper/api/main.py)**
- [Start Scraper](README-scraper.md)  `POST /scrape/{language}`
- [Scrape Meta-data](README-scraper.md) `POST /scrape-meta/{language}`
- [Stop Scraper](README-scraper.md) `POST /scrape/stop`

# MongoDB API
**The mongo and scraper apis are run simultaneusly. Start them from [this file](/scraper/api/main.py)**
- [Home](README-scraper.md) `GET /mongo`
- [Find All](README-scraper.md) `GET /mongo/test`
- [Add](README-scraper.md) `POST /mongo/test`
- [Empty](README-scraper.md) `DELETE /mongo/test`


# Mongo Matcher
**Start the parser [here](src/main/java/ires/baylor/edu/logerrors/LogErrorsApplication.java)**

-[Find Matches](README-matcher.md) `GET /matcher`



