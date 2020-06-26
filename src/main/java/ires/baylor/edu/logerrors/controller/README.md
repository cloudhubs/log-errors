# Scrape Logs

Used to create POJO from raw log files found [here](https://github.com/cloudhubs/logscraper/tree/master/logs/from_prod_anonymized)

**URL** : `/errors/`

**Method** : `POST`

**Data Constraints** : 

```json
{
        "pathToLogFile": "/path/",
        "pathToLogDirectory": "not currently supported"
}
```
**Data Example**
```json
{
    "pathToLogFile": "/home/mark/Documents/logs.txt",
    "pathToLogDirectory": ""
}
```

## Success Response
**Code**: `200 OK`

**Content Example**
```json
{
        "errorMessage" : "AttributeError: 'NoneType' object has no attribute 'split'\n",
                "gitHub" : null,
                "isExternal" : false,
                "lineNumber" : 2666,
                "nestedError" : null,
                "source" : "../logscraper/logs/from_prod_anonymized/ccx_data_pipeline_1_anonymized.log",
                "stackOverflow" : null,
                "traceBacks" : [
                        "File \"/opt/app-root/lib/python3.6/site-packages/insights/core/dr.py\", line 962, in run\nresult = DELEGATES[component].process(broker)",
                        "File \"/opt/app-root/lib/python3.6/site-packages/insights/core/dr.py\", line 681, in process\nreturn self.invoke(broker)",
                        "File \"/opt/app-root/lib/python3.6/site-packages/insights/core/plugins.py\", line 64, in invoke\nreturn super(PluginType, self).invoke(broker)",
                        "File \"/opt/app-root/lib/python3.6/site-packages/insights/core/dr.py\", line 661, in invoke\nreturn self.component(*args)",
                        "File \"/opt/app-root/lib/python3.6/site-packages/ccx_ocp_core/models/nodes.py\", line 108, in Nodes\nint(node.q.status.capacity.memory.value.split(\"Ki\")[0]) / (1000 * 1000), 2",
                        "AttributeError: 'NoneType' object has no attribute 'split'\n"
                ]
}
```

## Error Response

**Condition**: If file is not found
**Code**: `500 Server Error`

# Matcher API
**URL** : `/matcher`

**Method** : `GET`

**Data Constraints** : 

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

## Success Response
**Code**: `200 OK`

**Content Example**
```json
{
  "data": [
    {
      "url": "https://stackoverflow.com/questions/62586014/cant-save-as-a-csv-file",
      "title": "Cant't save CSV file",
      "code": "Traceback (most recent call last):File c:/Users/Louis Charron/Documents/GitHub/hemnet-scraper/hemnet_scra.p,line 196, in <module>SlutPriserScraper(start_page=1,num_of_pages=50,use_google_maps_api=False).to_csv()File c:/Users/Louis Charron/Documents/GitHub/hemnet-scraper/hemnet_scraper.py, line 189, in to_csv with open(csv_filepath, 'w') as output_file:FileNotFoundError: [Errno 2] No such file or directory: 'csv/20200625-housingprices.csv' ",
      "text": "Any clues on where to start debugging this? It might sound strange but I wonder if its even a code issue (considering I had it working just less than 1 hour ago)",
      "tags": [
        "python",
        "csv",
        "web-scraping"
      ]
    }
  ]
}
```

## Error Response

**Condition**: If file is not found
**Code**: `500 Serv
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
