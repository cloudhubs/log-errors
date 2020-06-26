# Welcome to Log-Errors

## This Repository contains a multitude of sub projects. 
1. Log Scraper - tokenizes log files of the format in [here](https://github.com/cloudhubs/logscraper/tree/master/logs/from_prod_anonymized) 

2. StackOverflow Scraper - an api that generates a database of posts from stack overflow

3. Mongo Matcher - a matching algorithm for comparing errors to data scraped by the StackOverflow Scraper

4. Mongo DB API - an API for the mongo db containing information from StackOverflow Scraper 

## Installation Of LogScraper
`git clone https://github.com/cloudhubs/log-errors.git`

[Install Lombok for your IDE](https://www.baeldung.com/lombok-ide)


# Log Parser API
**Start the parser [here](src/main/java/ires/baylor/edu/logerrors/LogErrorsApplication.java)**
- [Parse File](src/main/java/ires/baylor/edu/logerrors/controller/README.md): `POST /errors/`



# StackOverflow Scraper API
**The mongo and scraper apis are run simultaneusly. Start them from [this file](/scraper/api/main.py)**
- [Start Scraper](/scraper/api/README.md)  `POST /scrape/{language}`
- [Scrape Meta-data](/scraper/api/README.md) `POST /scrape-meta/{language}`
- [Stop Scraper](/scraper/api/README.md) `POST /scrape/stop`

# MongoDB API
**The mongo and scraper apis are run simultaneusly. Start them from [this file](/scraper/api/main.py)**
- [Home](/scraper/api/README.md) `GET /mongo`
- [Find All](/scraper/api/README.md) `GET /mongo/test`
- [Add](/scraper/api/README.md) `POST /mongo/test`
- [Empty](/scraper/api/README.md) `DELETE /mongo/test`


# Mongo Matcher
**Start the parser [here](src/main/java/ires/baylor/edu/logerrors/LogErrorsApplication.java)**

-[Find Matches](src/main/java/ires/baylor/edu/logerrors/controller/README.md) `GET /matcher`



