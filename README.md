# Welcome to Log-Errors

## This Repository contains a multitude of sub projects. 
1. Log Scraper - tokenizes log files of the format in [here](https://github.com/cloudhubs/logscraper/tree/master/logs/from_prod_anonymized) 

2. StackOverflow Scraper - an api that generates a database of posts from stack overflow

3. Mongo Matcher - a matching algorithm for comparing errors to data scraped by the StackOverflow Scraper

4. Mongo DB API - an API for the mongo db containing information from StackOverflow Scraper 

## Installation Of LogScraper
`git clone https://github.com/cloudhubs/log-errors.git`

[Install Lombok for your IDE](https://www.baeldung.com/lombok-ide)


# LogScraper API
- [Scrape File](src/main/java/ires/baylor/edu/logerrors/controller/README.md): `POST /errors/`


# StackOverflow Scraper API
- [Scrape File](/scraper/api/README.md) 


# Mongo DB API
## Endpoint: `/mongo`
### Welcome Page

## Endpoint: `/mongo/test/add`
### Action: Adds json (from body) to collection `testdb.coll_name`

## Endpoint: `/mongo/test/find`
### Returns: all documents in `testdb.coll_name`

## Endpoint: `/mongo/test/empty`
### empties `testdb.coll_name`

# Mongo Matcher
# API
## Endpoint: `/match`
## Body:
```json
{
    "pathToScraper": "not implemented",
    "currentError": {
        "errorMessage": "AttributeError: 'asdf' object has no attribute 'test'",
        "isExternal": false,
        "lineNumber": 123,
        "source": "not implementd",
        "stackOverflow": null,
        "nestedError": null
    },
    "variance": 0.85
}
```



